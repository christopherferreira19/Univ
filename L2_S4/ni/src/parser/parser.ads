with Lexer;
with Asts;
with Genres;

package Parser is

   use Asts;
   use Lexer;
   use Genres;

   Erreur_Syntaxique : Exception;

   function analyser(nom_fichier: in String) return Ast;

private

   procedure verifie_lexeme(nature: Nature_Lexeme);
   procedure verifie_lexeme_avancer(nature: Nature_Lexeme);
   procedure traiter_erreur(str: String);

   function rec_programme return Ast;
   function rec_bloc return Ast_Tabs.Tab;
   function rec_corps return Ast_Tabs.Tab;
   procedure rec_suite_corps(asts: in out Ast_Tabs.TabCreation);

   procedure rec_instruction(asts: in out Ast_Tabs.TabCreation);
   procedure rec_comment;
   function rec_retour return Ast;
   function rec_si return Ast;
   procedure rec_sinon(arbre: Ast);
   function rec_tq return Ast;
   function rec_boucle_tq return Ast;
   function rec_idf_instruction return Ast;

end;
