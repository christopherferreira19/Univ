with PStrings;
with Sources;

package Lexer is

   use PStrings;
   use Sources;

   type Nature_Lexeme is (
      -- Litt�ral & Identifiant
      Unit, Vrai, Faux,
      Entier, Flottant, Chaine,
      Idf_Min, Idf_Maj, Fn_Type,

      -- Op�rateur
      Et, Ou,
      Egale, Different, Inf, Inf_Egale, Sup, Sup_Egale,
      Plus, Moins, Mul, Div, Modulo,

      -- D�finition Types
      Deux_Points, Pipe, Appli,

      -- D�limiteurs & S�parateurs
      Par_O, Par_F, Accol_O, Accol_F,
      Virgule,
      Fin_Ligne, Fin_Sequence,

      -- Affectations
      Aff, Incr, Decr,
      Aff_Plus, Aff_Moins,
      Aff_Mul, Aff_Div, Aff_Modulo,

      -- Structure de Contr�les
      Fn, Retour,
      Si, Sinon, Tq, Boucle,

      Comment,
      Chaine_Non_Fermee, Erreur
   );

   type Lexeme is tagged private;

   procedure demarrer(nom_fichier : String);

   function courant return Lexeme;

   procedure empiler_ignorer_fdl(ignorer: Boolean);

   procedure depiler_ignorer_fdl;

   procedure avancer;

   procedure arreter;

   function nature(lex: Lexeme) return Nature_Lexeme;

   function pos_debut(lex: Lexeme) return Source_Pos;

   function chaine(lex: Lexeme) return PString;

   function identifiant(lex: Lexeme) return PString;

   function entier(lex: Lexeme) return Integer;

   function flottant(lex: Lexeme) return Float;

   procedure afficher(l: Lexeme);

private

   type Lexeme is tagged record
      nature: Nature_Lexeme;
      pos_debut: Source_Pos;
      string: PString;
   end record;

end;
