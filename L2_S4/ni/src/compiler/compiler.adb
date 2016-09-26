with PStrings;
with Genres;
with Valeurs;
with Inference;
with Instructions;

package body Compiler is

   use PStrings;
   use Genres;
   use Valeurs;
   use Instructions;

   function compiler(arbre: Ast) return Programme is
   begin
      return compiler(arbre, null);
   end;

   function compiler(arbre: Ast; src: Source) return Programme is
      rtns: Routine_Tabs.TabCreation;
   begin
      Inference.inferer(arbre, src);
      compiler_routine(arbre, rtns);
      return creer_programme(rtns.creer);
   end;

   procedure compiler_arbre(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      case arbre.nature is
         when Litteral    => compiler_litteral(arbre, rtns, corps);
         when Identifiant => compiler_identifiant(arbre, rtns, corps);
         when Fonction    => compiler_fonction(arbre, rtns, corps);
         when Operation   => compiler_operation(arbre, rtns, corps);
         when Unaire      => compiler_unaire(arbre, rtns, corps);
         when Appel       => compiler_appel(arbre, rtns, corps);
         when Affectation => compiler_affectation(arbre, rtns, corps);
         when Retour      => compiler_retour(arbre, rtns, corps);
         when Si          => compiler_si(arbre, rtns, corps);
         when Tq          => compiler_tq(arbre, rtns, corps);
         when Boucle_Tq   => compiler_boucle_tq(arbre, rtns, corps);
      end case;
   end;

   procedure compiler_arbres(arbres: Ast_Tabs.Tab; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      for i in 1..arbres.taille loop
         compiler_arbre(arbres.element(i), rtns, corps);
      exit when arbres.element(i).nature = Retour;
      end loop;
   end;

   procedure compiler_litteral(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      corps.ajouter(empiler(arbre.pos, arbre.val));
   end;

   procedure compiler_identifiant(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      if arbre.idf_primitive then
         corps.ajouter(primitive(arbre.pos, arbre.idf_nom));
      else 
         corps.ajouter(charger(arbre.pos, arbre.idf_nom, arbre.idf_indice));
      end if;
   end;

   procedure compiler_fonction(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
      instr: Instruction;
      r: PString;
      c: Natural;
   begin
      for i in 1..arbre.fn_clotures.taille loop
         corps.ajouter(charger(arbre.pos,
            arbre.fn_clotures.element(i).idf,
            arbre.fn_clotures.element(i).origine_indice));
      end loop;
      r := arbre.fn_nom;
      c := arbre.fn_clotures.taille;
      instr := Instructions.cloture(arbre.pos, r, arbre.fn_rec, c);
      corps.ajouter(instr);
      compiler_routine(arbre, rtns);
   end;

   procedure compiler_routine(arbre: Ast; rtns: in out Routine_Tabs.TabCreation) is
      corps: Code.TabCreation;
      rtn: Routine;
   begin
      rtn := creer_routine(arbre.fn_nom, arbre.fn_allocation);
      rtns.ajouter(rtn);

      if arbre.fn_rec then
         corps.ajouter(stocker(arbre.pos, arbre.fn_nom, arbre.fn_rec_indice));
      end if;
      for i in reverse 1..arbre.fn_clotures.taille loop
         corps.ajouter(stocker(arbre.pos, 
            arbre.fn_clotures.element(i).idf,
            arbre.fn_clotures.element(i).destination_indice));
      end loop;
      for i in reverse 1..arbre.fn_args.taille loop
         corps.ajouter(stocker(arbre.pos, arbre.fn_args.element(i), i - 1));
      end loop;
      compiler_arbres(arbre.fn_corps, rtns, corps);
      if not arbre.fn_retourne then
         corps.ajouter(empiler(arbre.pos, unit));
         corps.ajouter(retour(arbre.pos));
      end if;

      init_routine_corps(rtn, corps.creer);
   end;

   procedure compiler_operation(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      case arbre.bin_operateur is
         when Et | Ou =>
            compiler_operation_paresseuse(arbre, rtns, corps);
         when others =>
            compiler_arbre(arbre.bin_gauche, rtns, corps);
            compiler_arbre(arbre.bin_droite, rtns, corps);
            corps.ajouter(primitive(arbre.pos, arbre.bin_primitive));       
            corps.ajouter(appel(arbre.pos));
      end case;
   end;

   procedure compiler_operation_paresseuse(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
      droite_corps: Code.TabCreation;
   begin
      compiler_arbre(arbre.bin_gauche, rtns, corps);
      if arbre.bin_courtcircuite then
         return;
      end if;

      compiler_arbre(arbre.bin_droite, rtns, droite_corps);
      if arbre.bin_operateur = Et then
         corps.ajouter(saut_faux(arbre.pos, droite_corps.taille + 2));
      else 
         corps.ajouter(saut_vrai(arbre.pos, droite_corps.taille + 2));
      end if;
      corps.integrer(droite_corps);
      corps.ajouter(saut(arbre.pos, 2));
      if arbre.bin_operateur = Et then
         corps.ajouter(empiler(arbre.pos, faux));
      else
         corps.ajouter(empiler(arbre.pos, vrai));
      end if;
   end;

   procedure compiler_unaire(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      compiler_arbre(arbre.unr_operande, rtns, corps);
      if arbre.unr_operateur /= Plus then
         corps.ajouter(primitive(arbre.pos, arbre.unr_primitive));
         corps.ajouter(appel(arbre.pos));
      end if;
   end;

   procedure compiler_appel(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      compiler_arbres(arbre.app_args, rtns, corps);
      compiler_arbre(arbre.app_fn, rtns, corps);
      corps.ajouter(appel(arbre.pos));
   end;

   procedure compiler_affectation(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      compiler_arbre(arbre.var_expr, rtns, corps);
      corps.ajouter(stocker(arbre.pos, arbre.var_idf, arbre.var_indice));
   end;

   procedure compiler_retour(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
   begin
      compiler_arbre(arbre.ret_expr, rtns, corps);
      corps.ajouter(retour(arbre.pos));
   end;

   procedure compiler_si(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
      si_corps: Code.TabCreation;
      sinon_corps: Code.TabCreation;
   begin
      compiler_arbre(arbre.si_cond, rtns, corps);
      if arbre.si_cond_genre = base_vrai then
         corps.ajouter(depiler(arbre.pos));
         compiler_arbres(arbre.si_corps, rtns, corps);
      elsif arbre.si_cond_genre = base_faux then
         corps.ajouter(depiler(arbre.pos));
         compiler_arbres(arbre.sinon_corps, rtns, corps);
      else
         compiler_arbres(arbre.si_corps, rtns, si_corps);
         compiler_arbres(arbre.sinon_corps, rtns, sinon_corps);

         corps.ajouter(saut_faux(arbre.pos, si_corps.taille + 2));
         corps.integrer(si_corps);
         corps.ajouter(saut(arbre.pos, sinon_corps.taille + 1));
         corps.integrer(sinon_corps);
      end if;
   end;

   procedure compiler_tq(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
      tq_cond: Code.TabCreation;
      tq_cond_taille: Natural;
      tq_corps: Code.TabCreation;
      tq_corps_taille: Natural;
   begin
      compiler_arbre(arbre.tq_cond, rtns, tq_cond);
      if arbre.tq_cond_genre = base_faux then
         corps.ajouter(depiler(arbre.pos));
      else
         tq_cond_taille := tq_cond.taille;
         compiler_arbres(arbre.tq_corps, rtns, tq_corps);
         tq_corps_taille := tq_corps.taille;

         corps.integrer(tq_cond);
         corps.ajouter(saut_faux(arbre.pos, tq_corps_taille + 2));
         corps.integrer(tq_corps);
         corps.ajouter(saut(arbre.pos, - tq_cond_taille - tq_corps_taille - 1));
      end if;
   end;

   procedure compiler_boucle_tq(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation) is
      tq_cond: Code.TabCreation;
      tq_cond_taille: Natural;
      tq_corps: Code.TabCreation;
      tq_corps_taille: Natural;
   begin
      compiler_arbre(arbre.tq_cond, rtns, tq_cond);
      compiler_arbres(arbre.tq_corps, rtns, tq_corps);
      tq_corps_taille := tq_corps.taille;

      corps.integrer(tq_corps);
      tq_cond_taille := tq_cond.taille;
      corps.integrer(tq_cond);

      if arbre.tq_cond_genre = base_faux then
         corps.ajouter(depiler(arbre.pos));
      else
         corps.ajouter(saut_vrai(arbre.pos, - tq_cond_taille - tq_corps_taille));
      end if;
   end;

end;
