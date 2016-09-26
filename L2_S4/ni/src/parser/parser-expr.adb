with Sources;
with Valeurs;
with Parser.Genre;

package body Parser.Expr is

   use Sources;
   use Valeurs;

   use Parser.Genre;

   function rec_expression return Ast is
   begin
      return rec_suite_disj(rec_disj);
   end;

   function rec_disj return Ast is
   begin
      return rec_suite_disj(rec_conj);
   end;

   function rec_suite_disj(ag: Ast) return Ast is
      pos: Source_Pos := courant.pos_debut;
   begin
      if courant.nature = Ou then
         avancer;
         return rec_suite_disj(creer_operation(pos, Ou, ag, rec_conj));
      else
         return ag;
      end if;
   end;

   function rec_conj return Ast is
   begin
      return rec_suite_conj(rec_rel);
   end;

   function rec_suite_conj(ag: Ast) return Ast is
      pos: Source_Pos := courant.pos_debut;
   begin
      if courant.nature = Et then
         avancer;
         return rec_suite_conj(creer_operation(pos, Et, ag, rec_rel));
      else
         return ag;
      end if;
   end;

   function rec_rel return Ast is
      ag: Ast;
      pos: Source_Pos;
   begin
      ag := rec_somme;
      pos := courant.pos_debut;
      case courant.nature is
         when Egale =>
            avancer;
            return creer_operation(pos, Egale, ag, rec_somme);
         when Different =>
            avancer;
            return creer_operation(pos, Different, ag, rec_somme);
         when Inf =>
            avancer;
            return creer_operation(pos, Inf, ag, rec_somme);
         when Inf_egale =>
            avancer;
            return creer_operation(pos, Inf_egale, ag, rec_somme);
         when Sup =>
            avancer;
            return creer_operation(pos, Sup, ag, rec_somme);
         when Sup_egale =>
            avancer;
            return creer_operation(pos, Sup_egale, ag, rec_somme);
         when others =>
            return ag;
      end case;
   end;

   function rec_somme return Ast is
   begin
      return rec_suite_somme(rec_terme);
   end;

   function rec_suite_somme(ag: Ast) return Ast is
      pos: Source_Pos := courant.pos_debut;
   begin
      case courant.nature is
         when Plus =>
            avancer;
            return rec_suite_somme(creer_operation(pos, Plus, ag, rec_terme));
         when Moins =>
            avancer;
            return rec_suite_somme(creer_operation(pos, Moins, ag, rec_terme));
         when others =>
            return ag;
      end case;
   end;

   function rec_terme return Ast is
   begin
      return rec_suite_terme(rec_facteur);
   end;

   function rec_suite_terme(ag: Ast) return Ast is
      pos: Source_Pos := courant.pos_debut;
   begin
      case courant.nature is
         when Mul =>
            avancer;
            return rec_suite_terme(creer_operation(pos, Mul, ag, rec_facteur));
         when Div =>
            avancer;
            return rec_suite_terme(creer_operation(pos, Div, ag, rec_facteur));
         when Modulo =>
            avancer;
            return rec_suite_terme(creer_operation(pos, Modulo, ag, rec_facteur));
         when others =>
            return ag;
      end case;
   end;

   function rec_facteur return Ast is
   begin
      return rec_appel(rec_unaire);
   end;

   function rec_appel(expr: Ast) return Ast is
      pos: Source_Pos := courant.pos_debut;
   begin
      if courant.nature /= Par_O then
         return expr;
      end if;

      return rec_appel(creer_appel(pos, expr, rec_appel_args));
   end;

   function rec_appel_args return Ast_Tabs.Tab is
      args: Ast_Tabs.TabCreation;
   begin
      empiler_ignorer_fdl(true);
      avancer;
      if courant.nature = Par_F then
         depiler_ignorer_fdl;
         avancer;
         return args.creer;
      end if;

      args.ajouter(rec_expression);
      rec_appel_suite_args(args);
      verifie_lexeme(Par_F);
      depiler_ignorer_fdl;
      avancer;
      return args.creer;
   end;

   procedure rec_appel_suite_args(args: in out Ast_Tabs.TabCreation) is
   begin
      if courant.nature = Virgule then
         avancer;
         args.ajouter(rec_expression);
         rec_appel_suite_args(args);
      end if;
   end;

   function rec_unaire return Ast is
      pos: Source_Pos := courant.pos_debut;
   begin
      case courant.nature is
         when Plus =>
            avancer;
            return creer_unaire(pos, PLUS, rec_unaire);
         when Moins =>
            avancer;
            return creer_unaire(pos, MOINS, rec_unaire);
         when others => 
            return rec_atome;
      end case;
   end;

   function rec_atome return Ast is
      pos: Source_Pos := courant.pos_debut;
      arbre: Ast;
   begin
      case courant.nature is
         when Par_O =>
            avancer;
            empiler_ignorer_fdl(true);
            arbre := rec_expression;
            verifie_lexeme(Par_F);
            depiler_ignorer_fdl;
            avancer;
         when Unit =>
            arbre := creer_litteral(pos, unit);
            avancer;
         when Vrai =>
            arbre := creer_litteral(pos, vrai);
            avancer;
         when Faux =>
            arbre := creer_litteral(pos, faux);
            avancer;
         when Entier =>
            arbre := creer_litteral(pos, creer_entier(courant.entier));
            avancer;
         when Flottant =>
            arbre := creer_litteral(pos, creer_flottant(courant.flottant));
            avancer;
         when Chaine =>
            arbre := creer_litteral(pos, creer_chaine(courant.chaine));
            avancer;
         when Idf_Min =>
            arbre := creer_identifiant(pos, courant.identifiant);
            avancer;
         when Fn =>
            arbre := rec_fn;
         when others =>
            traiter_erreur("Debut de facteur attendu");
            return null;
      end case;

      return arbre;
   end;

   function rec_fn return Ast is
      pos: Source_Pos := courant.pos_debut;
      args_n: PString_Tabs.TabCreation;
      args_g: Genre_Decl_Tabs.TabCreation;
      retour: Genre_Decl;

      fn_ast: Ast;
   begin
      empiler_ignorer_fdl(true);

      verifie_lexeme_avancer(Fn);
      rec_fn_args(args_n, args_g);
      retour := rec_fn_retour;
      fn_ast := creer_fonction(pos,
            fonction(args_g.creer, retour),
            args_n.creer,
            rec_bloc);

      depiler_ignorer_fdl;
      return fn_ast;
   end;

   procedure rec_fn_args(
         args_n: in out PString_Tabs.TabCreation;
         args_g: in out Genre_Decl_Tabs.TabCreation) is
   begin
      if courant.nature /= Par_O then
         return;
      end if;
      avancer;

      if courant.nature = Par_F then
         avancer;
         return;
      end if;

      rec_fn_arg(args_n, args_g);
      rec_fn_suite_arg(args_n, args_g);
      verifie_lexeme_avancer(Par_F);
   end;

   procedure rec_fn_arg(
         args_n: in out PString_Tabs.TabCreation;
         args_g: in out Genre_Decl_Tabs.TabCreation) is
   begin
      verifie_lexeme(Idf_Min);
      args_n.ajouter(courant.identifiant);
      avancer;

      verifie_lexeme_avancer(Deux_Points);
      args_g.ajouter(rec_genre);
   end;

   procedure rec_fn_suite_arg(
         args_n: in out PString_Tabs.TabCreation;
         args_g: in out Genre_Decl_Tabs.TabCreation) is
   begin
      if courant.nature = Virgule then
         avancer;
         rec_fn_arg(args_n, args_g);
         rec_fn_suite_arg(args_n, args_g);
      end if;
   end;

   function rec_fn_retour return Genre_Decl is
   begin
      if courant.nature = Appli then
         avancer;
         return rec_genre;
      else
         return base_unit;
      end if;
   end;

end;