-- Le type valeur represente une valeur à l'execution
-- Les valeurs sont cachés derrière un pointeur par
-- commodité, principalement pour pouvoir les representer
-- par un type à champs variant et pouvoir les stocker
-- dans des records et tableaux

with Tabs;
with PStrings;
with Genres;

package Valeurs is

   use PStrings;
   use Genres;

   type Valeur_Record(genre: Genre_Base);
   type Valeur is access all Valeur_Record;
   package Valeur_Tabs is new Tabs(Valeur);

   type Ptr_Primitive is access procedure;

   function unit return Valeur;
   function vrai return Valeur;
   function faux return Valeur;
   function booleen(bool: Boolean) return Valeur;

   function creer_entier(ent: Integer) return Valeur;
   function creer_flottant(flot: Float) return Valeur;
   function ada_float(val: Valeur_Record; conversion: Boolean) return Float;
   function creer_chaine(chn: PString) return Valeur;
   function creer_cloture(routine: Positive; rec: Boolean; clotures: Valeur_Tabs.Tab) return Valeur;
   function creer_primitive(ptr: Ptr_Primitive) return Valeur;

   function genre(val: Valeur_Record) return Genre_Base;

   function str(val: Valeur_Record) return String;

   function str_code(val: Valeur_Record) return String;

   procedure afficher(val: Valeur_Record);

   procedure afficher_code(val: Valeur_Record);

   type Valeur_Record(genre: Genre_Base) is tagged record
      case genre is
         when Unit | Vrai | Faux => null;
         when Entier => ent: Integer;
         when Flottant => flot: Float;
         when Chaine => chn: PString;
         when Cloture =>
            routine: Positive;
            rec: Boolean;
            clotures: Valeur_Tabs.Tab;
         when Primitive => ptr: Ptr_Primitive;
      end case;
   end record;

end;