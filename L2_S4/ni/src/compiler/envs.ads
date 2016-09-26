with Ada.Strings.Hash;
with Ada.Containers.Indefinite_Hashed_Maps;

with Tabs;
with PStrings;
with Genres;

package Envs is

   use PStrings;
   use Genres;

   Variable_Non_Defini: exception;
   Variable_Non_Alloue: exception;
   Affectation_Primitive: exception;

   type EnvironnementRecord is tagged private;
   type Environnement is access all EnvironnementRecord;

   function creer_env return Environnement;

   function taille(env: EnvironnementRecord) return Natural;

   function est_defini(env: EnvironnementRecord; nom: PString) return Boolean;
   function allocation(env: EnvironnementRecord; nom: PString) return Natural;
   function definition(env: EnvironnementRecord; nom: PString) return Genre_Decl;
   function retourne(env: in out EnvironnementRecord) return Boolean;
   function retour(env: in out EnvironnementRecord) return Genre_Decl;

   function affecter(env: in out EnvironnementRecord; nom: PString; genre: Genre_Decl)
         return Natural;
   procedure retour(env: in out EnvironnementRecord; genre: Genre_Decl);
   procedure finaliser_retour(env: in out EnvironnementRecord);

   type EnvSnapshotRecord is private;
   type EnvSnapshot is access all EnvSnapshotRecord;

   function snapshot(env: EnvironnementRecord) return EnvSnapshot;
   procedure retablir(env: in out EnvironnementRecord; snap: EnvSnapshot);
   function fusionner(snap1: EnvSnapshot; snap2: EnvSnapshot) return EnvSnapshot;

private

   package Allocations is new Ada.Containers.Indefinite_Hashed_Maps(
      Key_Type => String,
      Element_Type => Natural,
      Hash => Ada.Strings.Hash,
      Equivalent_Keys => "="
   );

   package Definitions is new Ada.Containers.Indefinite_Hashed_Maps(
      Key_Type => String,
      Element_Type => Genre_Decl,
      Hash => Ada.Strings.Hash,
      Equivalent_Keys => "="
   );

   type EnvironnementRecord is tagged record
      allocs: Allocations.Map;
      defins: Definitions.Map;
      retourne: Boolean := false;
      retour: Genre_Decl := null;
   end record;

   type EnvSnapshotRecord is record
      defins: Definitions.Map;
      retourne: Boolean;
      retour: Genre_Decl;
   end record;

end;
