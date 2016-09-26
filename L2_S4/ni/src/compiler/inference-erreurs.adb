with Ada.Text_IO;
with Ada.Integer_Text_IO;

package body Inference.Erreurs is

   use Ada.Text_IO;
   use Ada.Integer_Text_IO;

   procedure erreur_entete(nature: String; pos: Source_Pos) is
   begin
      put("[Erreur] ");
      put(nature);      
      put(" @");
      put(integer_str(pos.ligne));
      put(":");
      put(integer_str(pos.colonne));
      new_line;

      if src /= null then
         src.afficher_pos(pos);
      end if;
   end;

   procedure type_invalide(
         pos: Source_Pos;
         attendu: Genre_Decl;
         recu: Genre_Decl) is
   begin
      erreur_entete("Type Invalide", pos);
      put(" * Attendu : ");
      attendu.afficher;
      new_line;
      put(" * Recu    : ");
      recu.afficher;
      new_line;
      new_line;
   end;

   procedure retour_manquant(pos: Source_Pos) is
   begin
      erreur_entete("Retour Manquant", pos);
      new_line;
   end;

   procedure variable_indefini(pos: Source_Pos; nom: PString) is
   begin
      erreur_entete("Variable Non Défini", pos);
      put(" * Var : ");
      put(nom);
      new_line;
      new_line;
   end;

   procedure erreur_fonction_corps(pos: Source_Pos; fn: Genre_Decl; args: Genre_Decl_Tabs.Tab) is
   begin
      put(" * Fonction : ");
      fn.afficher;
      new_line;
      put(" * Appel    : Fn(");
      if args.taille > 0 then
         args.element(1).afficher;
         for i in 2..args.taille loop
            put(", ");
            args.element(i).afficher;
         end loop;
      end if;
      put(") -> ?");
      new_line;
      new_line;
   end;

   procedure appel_non_fonction(
         pos: Source_Pos;
         fn: Genre_Decl;
         args: Genre_Decl_Tabs.Tab) is
   begin
      erreur_entete("Non Fonction appelé", pos);
      erreur_fonction_corps(pos, fn, args);
   end;

   procedure nombre_arguments_invalide(
         pos: Source_Pos;
         fn: Genre_Decl;
         args: Genre_Decl_Tabs.Tab) is
   begin
      erreur_entete("Nombre Arguments Invalide", pos);
      erreur_fonction_corps(pos, fn, args);
   end;

   procedure argument_invalide(
         pos: Source_Pos;
         fn: Genre_Decl;
         args: Genre_Decl_Tabs.Tab;
         indice: Positive) is
   begin
      erreur_entete("Argument " & integer_str(indice).all & " Invalide", pos);
      erreur_fonction_corps(pos, fn, args);
   end;

end;
