with Ada.Text_IO, Ada.Integer_Text_IO, Ada.Float_Text_IO;

with Genres;
with Valeurs;
with Primitives;
with Vm;

package body Standard is

   use Genres;
   use Valeurs;
   use Primitives;
   use Vm;

   use Ada.Text_IO, Ada.Integer_Text_IO, Ada.Float_Text_IO;

   procedure pile is
   begin
      Vm.pile;
      empiler(unit);
   end;

   procedure assert is
   begin
      if depiler = faux then
         raise Erreur_Assertion;
      end if;
      empiler(unit);
   end;

   procedure pause is
   begin
      put("<Appuyer sur Entrée pour continuer> ");
      skip_line;
      empiler(unit);
   end;

   procedure ecrire is
   begin
      depiler.afficher;
      empiler(unit);
   end;

   procedure ecrire_ln is
   begin
      depiler.afficher;
      new_line;
      empiler(unit);
   end;

   procedure lire_e is
      int: Integer;
   begin
      ecrire;
      get(int);
      skip_line;
      empiler(creer_entier(int));
   end;

   procedure lire_f is
      flt: Float;
   begin
      ecrire;
      get(flt);
      skip_line;
      empiler(creer_flottant(flt));
   end;

   procedure lire_c is
      retour: String(1..100);
      retour_t: Integer;
   begin
      ecrire;
      get_line(retour, retour_t);
      empiler(creer_chaine(new String'(retour(retour'first..retour_t))));
   end;

begin
   enregistrer(new String'("pile"),
         pile'access,
         retour => base_unit);
   enregistrer(new String'("assert"),
         assert'access,
         arg => union_booleen,
         retour => base_unit);
   enregistrer(new String'("pause"),
         pause'access,
         retour => base_unit);
   enregistrer(new String'("ecrire"),
         ecrire'access,
         arg => tous,
         retour => base_unit);
   enregistrer(new String'("ecrire_ln"),
         ecrire_ln'access,
         arg => tous,
         retour => base_unit);
   enregistrer(new String'("lire_e"),
         lire_e'access,
         arg => base_chaine,
         retour => base_entier);
   enregistrer(new String'("lire_f"),
         lire_f'access,
         arg => base_chaine,
         retour => base_flottant);
   enregistrer(new String'("lire_c"),
         lire_c'access,
         arg => base_chaine,
         retour => base_chaine);
end;