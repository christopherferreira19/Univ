with Ada.Text_IO;

with PStrings;
with Sources;
with Genres;
with Valeurs;

with Parser.Expr;
with Parser.Genre;

package body Parser is

   use PStrings;
   use Sources;
   use Genres;
   use Valeurs;

   use Parser.Expr;
   use Parser.Genre;

   function analyser(nom_fichier : in String) return Ast is
      pgm: Ast;
   begin
      demarrer(nom_fichier);
      pgm := rec_programme;
      arreter;
      return pgm;
   end;

   procedure verifie_lexeme(nature: Nature_Lexeme) is
   begin
      if courant.nature /= nature then
         traiter_erreur(Nature_Lexeme'image(nature) & " attendu");
      end if;
   end;

   procedure verifie_lexeme_avancer(nature: Nature_Lexeme) is
   begin
      verifie_lexeme(nature);
      avancer;
   end;

   procedure traiter_erreur(str: String) is
   begin
      courant.afficher;
      raise Erreur_Syntaxique with str;
   end;

   function rec_programme return Ast is
      pos: Source_Pos := courant.pos_debut;
      corps: Ast_Tabs.Tab;
   begin
      empiler_ignorer_fdl(false);
      corps := rec_corps;
      depiler_ignorer_fdl;
      verifie_lexeme(Fin_Sequence);
      return creer_fonction(
            pos,
            fonction(Genre_Decl_Tabs.creer_vide, base_unit),
            PString_Tabs.creer_vide,
            corps);
   end;

   function rec_bloc return Ast_Tabs.Tab is
      corps: Ast_Tabs.Tab;
   begin
      empiler_ignorer_fdl(false);
      verifie_lexeme_avancer(Accol_O);
      corps := rec_corps;
      verifie_lexeme_avancer(Accol_F);
      depiler_ignorer_fdl;
      return corps;
   end;

   function rec_corps return Ast_Tabs.Tab is
      asts: Ast_Tabs.TabCreation;
   begin
      rec_instruction(asts);
      rec_suite_corps(asts);
      return asts.creer;
   end;

   procedure rec_suite_corps(asts: in out Ast_Tabs.TabCreation) is
   begin
      if courant.nature = Fin_Ligne then
         avancer;
         rec_instruction(asts);
         rec_suite_corps(asts);
      end if;
   end;

   procedure rec_instruction(asts: in out Ast_Tabs.TabCreation) is
   begin
      case courant.nature is
         when Comment => rec_comment;
         when Retour  => asts.ajouter(rec_retour);
         when Si      => asts.ajouter(rec_si);
         when Sinon   => rec_sinon(asts.element(asts.taille));
         when Tq      => asts.ajouter(rec_tq);
         when Boucle  => asts.ajouter(rec_boucle_tq);
         when Idf_Min => asts.ajouter(rec_idf_instruction);
         when others  => null;
      end case;
   end;

   procedure rec_comment is
   begin
      while courant.nature /= Fin_Sequence
            and courant.nature /= Fin_Ligne loop
         avancer;
      end loop;
   end;

   function rec_retour return Ast is
      pos: Source_Pos := courant.pos_debut;
      expr: Ast;
   begin
      avancer;
      if courant.nature = Fin_Ligne
            or courant.nature = Fin_Sequence then
         expr := creer_litteral(pos, unit);
      else
         expr := rec_expression;
      end if;

      return creer_retour(pos, expr);
   end;

   function rec_si return Ast is
      pos: Source_Pos := courant.pos_debut;

      cond: Ast;
      si_corps: Ast_Tabs.Tab;
   begin
      empiler_ignorer_fdl(true);
      verifie_lexeme_avancer(Si);
      cond := rec_expression;
      si_corps := rec_bloc;
      depiler_ignorer_fdl;
      return creer_si(pos, cond, si_corps);
   end;

   procedure rec_sinon(arbre: Ast) is
      sinon_corps: Ast_Tabs.Tab;
   begin
      if arbre.nature /= Si then
         traiter_erreur("Sinon inattendu");
         return;
      end if;

      empiler_ignorer_fdl(true);
      verifie_lexeme_avancer(Sinon);
      if courant.nature = Si then
         sinon_corps := Ast_Tabs.creer_singleton(rec_si);
      else
         sinon_corps := rec_bloc;
      end if;
      depiler_ignorer_fdl;

      maj_sinon(arbre, sinon_corps);
   end;

   function rec_tq return Ast is
      pos: Source_Pos := courant.pos_debut;

      cond: Ast;
      corps: Ast_Tabs.Tab;
   begin
      empiler_ignorer_fdl(true);
      verifie_lexeme_avancer(Tq);
      cond := rec_expression;
      corps := rec_bloc;
      depiler_ignorer_fdl;
      return creer_tq(pos, cond, corps);
   end;

   function rec_boucle_tq return Ast is
      pos: Source_Pos := courant.pos_debut;

      cond: Ast;
      corps: Ast_Tabs.Tab;
   begin
      empiler_ignorer_fdl(true);
      verifie_lexeme_avancer(Boucle);
      corps := rec_bloc;
      verifie_lexeme_avancer(Tq);
      depiler_ignorer_fdl;
      cond := rec_expression;
      return creer_boucle_tq(pos, cond, corps);
   end;

   function rec_idf_instruction return Ast is
      pos: Source_Pos;

      idf: PString;
      op: Op_Binaire;
   begin
      idf := courant.identifiant;
      avancer;
      pos := courant.pos_debut;

      case courant.nature is
         when Par_O =>
            return rec_appel(creer_identifiant(pos, idf));
         when Aff =>
            avancer;
            return creer_affectation(pos, idf, rec_expression);
         when Incr =>
            op := Plus;
            avancer;
            return creer_op_affectation(pos, idf, op,
                creer_litteral(courant.pos_debut, creer_entier(1)));
         when Decr =>
            op := Moins;
            avancer;
            return creer_op_affectation(pos, idf, op,
                creer_litteral(courant.pos_debut, creer_entier(1)));
         when Aff_Plus =>
            op := Plus;
            avancer;
            return creer_op_affectation(pos, idf, op, rec_expression);
         when Aff_Moins =>
            op := Moins;
            avancer;
            return creer_op_affectation(pos, idf, op, rec_expression);
         when Aff_Mul =>
            op := Mul;
            avancer;
            return creer_op_affectation(pos, idf, op, rec_expression);
         when Aff_Div =>
            op := Div;
            avancer;
            return creer_op_affectation(pos, idf, op, rec_expression);
         when Aff_Modulo =>
            op := Modulo;
            avancer;
            return creer_op_affectation(pos, idf, op, rec_expression);
         when others =>
            traiter_erreur("Attendu: Appel ou Affectation");
            return null;
      end case;
   end;

end;
