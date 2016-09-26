with Ada.Text_IO;

with Sources;

package Lecture is

   use Sources;

   procedure demarrer;

   procedure demarrer(nom_fichier : in String);

   procedure avancer;

   procedure consommer(car: Character);

   procedure consommer(cars: String);

   function courant return Character;

   function est_fin return Boolean;

   function pos return Source_Pos;

   procedure arreter;

end;
