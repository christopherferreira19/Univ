-- Representation des instructions de la machine
-- virtuelle
-- 
-- Les différentes significations des instructions
-- sont décrites sur les fonctions correspondantes
-- du package Vm
--
-- En plus du type d'instruction et des différentes
-- informations associés à ce type, la structure de
-- donnée contient la position dans le code source
-- à l'origine de cette instruction

with Ada.Text_IO;
with Tabs;
with PStrings;
with Sources;
with Valeurs;

package Instructions is

   use Ada.Text_IO;
   use PStrings;
   use Sources;
   use Valeurs;

   type Nature_Instruction is (
      Empiler,
      Depiler,
      Charger,
      Stocker,

      Primitive,
      Cloture,

      Saut,
      Saut_Vrai,
      Saut_Faux,

      Appel,
      Retour
   );

   type Instruction_Record(nature: Nature_Instruction);
   type Instruction is access all Instruction_Record;

   package Code is new Tabs(Instruction);

   function empiler(pos: Source_Pos; litteral: Valeur) return Instruction;

   function depiler(pos: Source_Pos) return Instruction;

   function charger(pos: Source_Pos; idf: PString; addr: Natural) return Instruction;

   function stocker(pos: Source_Pos; idf: PString; addr: Natural) return Instruction;

   function primitive(pos: Source_Pos; nom: PString) return Instruction;

   function cloture(pos: Source_Pos; routine: PString; rec: Boolean; clotures: Natural) return Instruction;

   function saut(pos: Source_Pos; decalage: Integer) return Instruction;

   function saut_vrai(pos: Source_Pos; decalage: Integer) return Instruction;

   function saut_faux(pos: Source_Pos; decalage: Integer) return Instruction;

   function appel(pos: Source_Pos) return Instruction;

   function retour(pos: Source_Pos) return Instruction;

   procedure afficher(instr: Instruction);

   procedure ecrire(sortie: File_Type; instr: Instruction);

   type Instruction_Record(nature: Nature_Instruction) is record
      pos: Source_Pos;

      case nature is
         when Empiler => litteral: Valeur;
         when Depiler => null;
         when Charger | Stocker =>
            idf: PString;
            addr: Natural;
         when Primitive => nom: PString;
         when Cloture => 
            routine: PString;
            rec: Boolean;
            clotures: Natural;
         when Saut | Saut_Vrai | Saut_Faux =>
            decalage: Integer;
         when Appel => null;
         when Retour => null;
      end case;
   end record;

end;
