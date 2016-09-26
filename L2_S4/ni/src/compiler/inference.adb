with Inference.Erreurs;

package body Inference is

   procedure inferer(arbre: Ast) is
   begin
      inferer(arbre, null);
   end;

   procedure inferer(arbre: Ast; src_arg: Source) is
   begin
      src := src_arg;
      erreur_rencontre := false;
      fn := null;
      fn_compte := 0;

      if arbre.nature /= Fonction then
         raise Genre_Invalide;
      end if;

      inferer_fonction(null, arbre);
      if erreur_rencontre then
         raise Inference_Erreur;
      end if;
   end;

   procedure verifie_generalise(pos: Source_Pos;
         attendu: Genre_Decl;
         recu: Genre_Decl) is
   begin
      if attendu.generalise(recu) then
         return;
      end if;
         
      Erreurs.type_invalide(
            pos,
            attendu,
            recu);
      erreur_rencontre := true;
   end;

   procedure inferer_fonction(nom: PString; arbre: Ast) is
      nouvelle: Fn_Inference := new Fn_InferenceRecord;
      allocation: Natural;
   begin
      nouvelle.parent := fn;
      fn := nouvelle;
      fn.arbre := arbre;
      fn.env := creer_env;

      fn.arbre.fn_rec := false;
      fn.arbre.fn_rec_nom := nom;

      initialiser_fn_nom(nom);

      for i in 1..arbre.fn_args.taille loop
         allocation := fn.env.affecter(
            arbre.fn_args.element(i),
            arbre.fn_genre.args.element(i));
      end loop;

      inferer_corps(arbre.fn_corps);

      fn.env.finaliser_retour;
      verifie_generalise(arbre.pos, arbre.fn_genre.retour, fn.env.retour);

      arbre.fn_allocation := fn.env.taille;
      arbre.fn_retourne := fn.env.retourne;
      arbre.fn_clotures := fn.clotures.creer;
      fn := fn.parent;
   end;

   procedure initialiser_fn_nom(nom: PString) is
      prefixe: PString;
   begin
      prefixe := new String'("");
      if fn.parent = null then
         fn.arbre.fn_nom := prefixe;
         return;
      end if;

      if fn.parent.arbre.fn_nom.all /= "" then
         prefixe := new String'(fn.parent.arbre.fn_nom.all & '$');
      end if;

      if nom /= null then
         prefixe := new String'(prefixe.all & nom.all); 
      end if;

      fn.arbre.fn_nom := new String'(prefixe.all
            & integer_str(fn_compte).all);
      fn_compte := fn_compte + 1;
   end;

   procedure inferer_corps(arbres: Ast_Tabs.Tab) is
   begin
      for i in 1..arbres.taille loop
         inferer_instruction(arbres.element(i));
      exit when fn.env.retourne;
      end loop;
   end;

   procedure inferer_instruction(arbre: Ast) is
   begin
      case arbre.nature is
         when Litteral | Identifiant | Operation | Unaire | Fonction =>
            raise Ast_Invalide;
         when Appel => inferer_appel(arbre);
         when Affectation => inferer_affectation(arbre);
         when Retour => inferer_retour(arbre);
         when Si => inferer_si(arbre);
         when Tq => inferer_tq(arbre);
         when Boucle_Tq => inferer_boucle_tq(arbre);
      end case;
   end;

   function inferer_expression(arbre: Ast) return Genre_Decl is
   begin
      return inferer_expression(null, arbre);
   end;

   function inferer_expression(nom: PString; arbre: Ast) return Genre_Decl is
   begin
      case arbre.nature is
         when Litteral => return inferer_litteral(arbre);
         when Identifiant => return inferer_identifiant(arbre);
         when Fonction =>
            inferer_fonction(nom, arbre);
            return arbre.fn_genre;
         when Operation => return inferer_binaire(arbre);
         when Unaire => return inferer_unaire(arbre);
         when Appel => return inferer_appel(arbre);
         when Affectation | Retour | Si | Tq | Boucle_Tq =>
            raise Ast_Invalide;
      end case;
   end;

   function inferer_litteral(arbre: Ast) return Genre_Decl is
   begin
      return base(arbre.val.genre);
   end;

   procedure trouver_variable(f: Fn_Inference; arbre: Ast;
         definition: out Genre_Decl;
         allocation: out Natural) is
      clot: Asts.Cloture;
   begin
      if f = null then
         Erreurs.variable_indefini(arbre.pos, arbre.idf_nom);
         erreur_rencontre := true;
         definition := tous;
         allocation := 0;
         return;
      end if;

      if f.env.est_defini(arbre.idf_nom) then
         definition := f.env.definition(arbre.idf_nom);
         allocation := f.env.allocation(arbre.idf_nom);
      elsif f.arbre.fn_rec_nom /= null
            and then f.arbre.fn_rec_nom.all = arbre.idf_nom.all then
         -- Variable correspondant à un appel recursif de la fonction
         definition := f.arbre.fn_genre;
         allocation := f.env.affecter(arbre.idf_nom, f.arbre.fn_genre);
         f.arbre.fn_rec := true;
         f.arbre.fn_rec_indice := allocation;
      else
         trouver_variable(f.parent, arbre, definition, allocation);
         -- Variable à enfermer depuis une fonction parente
         clot.idf := arbre.idf_nom;
         clot.origine_indice := allocation;
         allocation := f.env.affecter(arbre.idf_nom, definition);
         clot.destination_indice := allocation;
         f.clotures.ajouter(clot);
      end if;
   end;

   function inferer_identifiant(arbre: Ast) return Genre_Decl is
      genre: Genre_Decl;
   begin
      if Primitives.existe(arbre.idf_nom) then
         arbre.idf_primitive := true;
         genre := Primitives.element(arbre.idf_nom).genre;
      else
         arbre.idf_primitive := false;
         trouver_variable(fn, arbre, genre, arbre.idf_indice);
      end if;

      return genre;
   end;

   function inferer_binaire(arbre: Ast) return Genre_Decl is
      gd_gauche: Genre_Decl;
      gd_droite: Genre_Decl;
   begin
      gd_gauche := inferer_expression(arbre.bin_gauche);
      case arbre.bin_operateur is
         when Et =>
            arbre.bin_primitive := new String'("et");
            verifie_generalise(arbre.bin_gauche.pos,
                  union_booleen, gd_gauche);
            if gd_gauche = base_faux then
               arbre.bin_courtcircuite := true;
               return base_faux;
            end if;
            gd_droite := inferer_expression(arbre.bin_droite);
            verifie_generalise(arbre.bin_droite.pos,
                  union_booleen, gd_droite);
            return gd_droite;
         when Ou =>
            arbre.bin_primitive := new String'("ou");
            verifie_generalise(arbre.bin_gauche.pos,
                  union_booleen, gd_gauche);
            if gd_gauche = base_vrai then
               arbre.bin_courtcircuite := true;
               return base_vrai;
            end if;
            gd_droite := inferer_expression(arbre.bin_droite);
            verifie_generalise(arbre.bin_droite.pos,
                  union_booleen, gd_droite);
            return gd_droite;
         when others => null;
      end case;

      gd_droite := inferer_expression(arbre.bin_droite);
      case arbre.bin_operateur is
         when Et | Ou =>
            return null;
         when Egale | Different =>
            verifie_generalise(arbre.bin_gauche.pos,
                  tous, gd_gauche);
            verifie_generalise(arbre.bin_droite.pos,
                  tous, gd_droite);
            case arbre.bin_operateur is
               when Egale     => arbre.bin_primitive := new String'("egale");
               when Different => arbre.bin_primitive := new String'("different");
               when others => null;
            end case;
            return union_booleen;
         when Inf | Inf_Egale | Sup | Sup_Egale =>
            verifie_generalise(arbre.bin_gauche.pos,
                  union_nombre, gd_gauche);
            verifie_generalise(arbre.bin_droite.pos,
                  union_nombre, gd_droite);
            case arbre.bin_operateur is
               when Inf       => arbre.bin_primitive := new String'("inferieur");
               when Inf_Egale => arbre.bin_primitive := new String'("inferieur_egale");
               when Sup       => arbre.bin_primitive := new String'("superieur");
               when Sup_Egale => arbre.bin_primitive := new String'("superieur_egale");
               when others => null;
            end case;
            return union_booleen;
         when Plus | Moins | Mul | Div =>
            case arbre.bin_operateur is
               when Plus  => arbre.bin_primitive := new String'("plus");
               when Moins => arbre.bin_primitive := new String'("moins");
               when Mul   => arbre.bin_primitive := new String'("multiplier");
               when Div   => arbre.bin_primitive := new String'("diviser");
               when others => null;
            end case;
            if gd_gauche = base_entier then
               verifie_generalise(arbre.bin_droite.pos,
                     base_entier, gd_droite);
               return base_entier;
            elsif gd_gauche = base_flottant then
               verifie_generalise(arbre.bin_droite.pos,
                     base_flottant, gd_droite);
               arbre.bin_primitive := new String'(arbre.bin_primitive.all & "_f");
               return base_flottant;
            else
               Erreurs.type_invalide(arbre.bin_gauche.pos, base_entier, gd_gauche);
               Erreurs.type_invalide(arbre.bin_gauche.pos, base_flottant, gd_gauche);
               erreur_rencontre := true;
               return union_nombre;
            end if;
         when Modulo =>
            verifie_generalise(arbre.bin_gauche.pos,
                  base_entier, gd_gauche);
            verifie_generalise(arbre.bin_droite.pos,
                  base_entier, gd_droite);
            arbre.bin_primitive := new String'("modulo");
            return base_entier;
      end case;
   end;

   function inferer_unaire(arbre: Ast) return Genre_Decl is
      gd_operande: Genre_Decl;
   begin
      gd_operande := inferer_expression(arbre.unr_operande);
      case arbre.unr_operateur is
         when Plus =>
            verifie_generalise(arbre.unr_operande.pos,
                  union_nombre, gd_operande);
            return gd_operande;
         when Moins =>
            verifie_generalise(arbre.unr_operande.pos,
                  union_nombre, gd_operande);
            if gd_operande = base_entier then
               arbre.unr_primitive := new String'("moins_unaire");
            else
               arbre.unr_primitive := new String'("moins_unaire_f");
            end if;
            return gd_operande;
      end case;
   end;

   function inferer_appel(arbre: Ast) return Genre_Decl is
      gd_fn: Genre_Decl;
      gd_args: Genre_Decl_Tabs.Tab;
   begin
      gd_fn := inferer_expression(arbre.app_fn);
      gd_args := inferer_appel_args(arbre.app_args);
      if gd_fn.nature /= Fonction  then
         Erreurs.appel_non_fonction(arbre.pos, gd_fn, gd_args);
         erreur_rencontre := true;
         return gd_fn;
      end if;

      if gd_fn.args.taille /= gd_args.taille then
         Erreurs.nombre_arguments_invalide(arbre.pos, gd_fn, gd_args);
         erreur_rencontre := true;
      else
         for i in 1..gd_args.taille loop
            if not gd_fn.args.element(i).generalise(gd_args.element(i)) then
               Erreurs.argument_invalide(arbre.pos, gd_fn, gd_args, i);
               erreur_rencontre := true;
         exit;
            end if;
         end loop;
      end if;

      return gd_fn.retour;
   end;

   procedure inferer_appel(arbre: Ast) is
      gd_retour: Genre_Decl;
   begin
      gd_retour := inferer_appel(arbre);
   end;

   function inferer_appel_args(args: Ast_Tabs.Tab) return Genre_Decl_Tabs.Tab is
      gd_args: Genre_Decl_Tabs.TabCreation;
   begin
      for i in 1..args.taille loop
         gd_args.ajouter(inferer_expression(args.element(i)));
      end loop;

      return gd_args.creer;
   end;

   procedure inferer_affectation(arbre: Ast) is
      gd_expr: Genre_Decl;
   begin
      if Primitives.existe(arbre.var_idf) then
         raise Affectation_Primitive with "Primitive " & arbre.var_idf.all;
      else
         gd_expr := inferer_expression(arbre.var_idf, arbre.var_expr);
         arbre.var_indice := fn.env.affecter(arbre.var_idf, gd_expr);
      end if;
   end;

   procedure inferer_retour(arbre: Ast) is
   begin
      fn.env.retour(inferer_expression(arbre.ret_expr));
   end;

   procedure inferer_si(arbre: Ast) is
      snap_avant: EnvSnapshot;
      snap_si: EnvSnapshot;
      snap_sinon: EnvSnapshot;
   begin
      arbre.si_cond_genre := inferer_expression(arbre.si_cond);
      verifie_generalise(arbre.si_cond.pos,
            union_booleen, arbre.si_cond_genre);
      if arbre.si_cond_genre = base_vrai then
         inferer_corps(arbre.si_corps);
      elsif arbre.si_cond_genre = base_faux then
         inferer_corps(arbre.sinon_corps);
      else
         snap_avant := fn.env.snapshot;
         inferer_corps(arbre.si_corps);
         snap_si := fn.env.snapshot;
         fn.env.retablir(snap_avant);
         inferer_corps(arbre.sinon_corps);
         snap_sinon := fn.env.snapshot;
         fn.env.retablir(fusionner(snap_si, snap_sinon));
      end if;
   end;

   procedure inferer_tq(arbre: Ast) is
      snap_avant: EnvSnapshot;
      snap_apres: EnvSnapshot;
   begin
      arbre.tq_cond_genre := inferer_expression(arbre.tq_cond);
      verifie_generalise(arbre.tq_cond.pos,
            union_booleen, arbre.tq_cond_genre);
      if arbre.tq_cond_genre /= base_faux then
         snap_avant := fn.env.snapshot;
         inferer_corps(arbre.tq_corps);
         snap_apres := fn.env.snapshot;
         fn.env.retablir(fusionner(snap_avant, snap_apres));
      end if;
   end;

   procedure inferer_boucle_tq(arbre: Ast) is
   begin
      inferer_corps(arbre.tq_corps);
      arbre.tq_cond_genre := inferer_expression(arbre.tq_cond);
      verifie_generalise(arbre.tq_cond.pos,
            union_booleen, arbre.tq_cond_genre);
   end;

end;