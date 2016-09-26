-- Gestion des primitives
-- (définition, stockage, récupération)

with Ada.Strings.Hash;
with Ada.Containers.Indefinite_Hashed_Maps;

with PStrings;
with Genres;
with Valeurs;

package Primitives is

   use PStrings;
   use Genres;
   use Valeurs;

   type Primitive is tagged private;

   Primitive_Inexistante: exception;

   procedure enregistrer(nom: PString; val: Valeur; genre: Genre_Decl);
   procedure enregistrer(nom: PString; ptr: Ptr_Primitive;
         retour: Genre_Decl);
   procedure enregistrer(nom: PString; ptr: Ptr_Primitive;
         retour: Genre_Decl;
         arg: Genre_Decl);
   procedure enregistrer(nom: PString; ptr: Ptr_Primitive;
         retour: Genre_Decl;
         args: Genre_Decl_Tabs.TabAry);

   function existe(nom: PString) return Boolean;
   function element(nom: PString) return Primitive;

   function nom(prm: Primitive) return PString;
   function genre(prm: Primitive) return Genre_Decl;
   function val(prm: Primitive) return Valeur;

private

   type Primitive is tagged record
      nom: PString;
      genre: Genre_Decl;
      val: Valeur;
   end record;

   package PrimitiveMap is new Ada.Containers.Indefinite_Hashed_Maps(
      Key_Type => String,
      Element_Type => Primitive,
      Hash => Ada.Strings.Hash,
      Equivalent_Keys => "="
   );

end;
