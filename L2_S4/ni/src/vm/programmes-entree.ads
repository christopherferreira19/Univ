-- Package pour la lecture d'un programme depuis
-- sa representation sous forme texte.
--
-- Le langage de cette representation étant un
-- langage régulier, la lecture est réalisé sans
-- grammaire et parser
--
-- Le format attendu suit le schéma suivant :
-- [] allocation
-- IST arguments
-- ...
-- [nom_routine] allocation
-- IST
-- ...

with PStrings;
with Sources;
with Valeurs;
with Instructions;
with Programmes;

package Programmes.Entree is

   use Programmes;

   Erreur_Lecture: exception;

   function lire(nom_fichier: String) return Programme;

private 

   use PStrings;
   use Sources;
   use Instructions;
   use Valeurs;

   procedure consommer_separateurs;
   procedure consommer_separateurs_lignes;
   procedure raise_erreur_lecture(msg: String);
   procedure separateurs;
   procedure retour_a_la_ligne;
   function lire return Programme;
   function lire_corps return Code.Tab;
   function lire_entier return Integer;
   function lire_string return PString;
   function lire_litteral return Valeur;
   function lire_pos return Source_Pos;
   function lire_instruction return Instruction;

end;