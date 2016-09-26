-- Representation d'un programme executable
--
-- Un programme est constitué d'un ensemble de routine
--
-- Les routines represente la suite d'instructions
-- d'une fonction. La distinction entre routine et fonction
-- est utile car le langage supporte les fonctions
-- de première classe et les fermetures (avec portée lexicale)
-- 
-- Chaque routine porte un nom et indique la plage mémoire qui
-- doit lui être allouée.
--
-- Chaque programme doit commencer par une routine principale.
-- La routine principale est une routine spéciale dont le nom
-- "est vide".

with Ada.Text_IO;
with Tabs;
with PStrings;
with Instructions;

package Programmes is

   use Ada.Text_IO;
   use PStrings;
   use Instructions;

   Routine_Non_Defini: exception;

   type Routine_Record;
   type Routine is access all Routine_Record;

   package Routine_Tabs is new Tabs(Routine);

   type Programme;

   function creer_routine(nom: PString; allocation: Natural) return Routine;

   procedure init_routine_corps(rtn: Routine; corps: Code.Tab);

   function creer_programme(rtns: Routine_Tabs.Tab) return Programme;

   procedure afficher(prog: Programme);

   procedure ecrire(nom_fichier: String; prog: Programme);

   type Routine_Record is record
      nom: PString;
      allocation: Natural; 
      corps: Code.Tab;
   end record;

   type Programme is record
      rtns: Routine_Tabs.Tab;
   end record;

private

   procedure ecrire(sortie: File_Type; prog: Programme);

end;