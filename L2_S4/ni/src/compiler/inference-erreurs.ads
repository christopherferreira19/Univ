with Sources;
with PStrings;
with Genres;

package Inference.Erreurs is

   use Sources;
   use PStrings;
   use Genres;

   procedure type_invalide(
         pos: Source_Pos;
         attendu: Genre_Decl;
         recu: Genre_Decl);

   procedure retour_manquant(pos: Source_Pos);
   procedure variable_indefini(pos: Source_Pos; nom: PString);
   procedure appel_non_fonction(
         pos: Source_Pos;
         fn: Genre_Decl;
         args: Genre_Decl_Tabs.Tab);
   procedure nombre_arguments_invalide(
         pos: Source_Pos;
         fn: Genre_Decl;
         args: Genre_Decl_Tabs.Tab);
   procedure argument_invalide(
         pos: Source_Pos;
         fn: Genre_Decl;
         args: Genre_Decl_Tabs.Tab;
         indice: Positive);
end;