-- Le type Ast est un structure de données permettant de representer le code
-- source sous forme d'arbre en l'abstrayant des détails de syntaxe non
-- significatifs
--
-- De plus, il contient des champs initialisé par l'inférence pour aider à la
-- compilation. (Note: Dans un projet plus important, il serait plus avisé de
-- recréer un nouvelle "ast typé" voire un graphe de contrôle de flot à partir
-- de l'ast plutôt que d'annoté l'existant)

with Tabs;
with PStrings;
with Sources;
with Genres;
with Valeurs;

package Asts is

   use PStrings;
   use Sources;
   use Genres;
   use Valeurs;

   type Op_Binaire is (
         Et, Ou,
         Egale, Different,
         Inf, Inf_Egale,
         Sup, Sup_Egale,
         Plus, Mul, Modulo, Moins, Div
   );

   type Op_Unaire is (Plus, Moins);

   type Nature_Ast is (
         Litteral, Identifiant, Fonction,
         Operation, Unaire, Appel,
         Affectation, Retour,
         Si, Tq, Boucle_Tq
   );

   type Ast_Record(nature : Nature_Ast);
   type Ast is access Ast_Record;

   package Ast_Tabs is new Tabs(Ast);

   function creer_litteral(pos: Source_Pos; val: Valeur) return Ast;

   function creer_identifiant(pos: Source_Pos; nom: PString) return Ast;

   function creer_fonction(pos: Source_Pos; genre: Genre_Decl; args: PString_Tabs.Tab; corps: Ast_Tabs.Tab) return Ast;

   function creer_operation(pos: Source_Pos; op: Op_Binaire; gauche: Ast; droite: Ast) return Ast;

   function creer_unaire(pos: Source_Pos; unr: Op_Unaire; opde: Ast) return Ast;

   function creer_appel(pos: Source_Pos; fn: Ast; args: Ast_Tabs.Tab) return Ast;

   function creer_affectation(pos: Source_Pos; idf: PString; expr: Ast) return Ast;

   function creer_op_affectation(pos: Source_Pos; idf: PString; op: Op_Binaire; expr: Ast) return Ast;

   function creer_retour(pos: Source_Pos; expr: Ast) return Ast;

   function creer_si(pos: Source_Pos; cond: Ast; si_corps: Ast_Tabs.Tab) return Ast;

   procedure maj_sinon(arbre: Ast; sinon_corps: Ast_Tabs.Tab);

   function creer_tq(pos: Source_Pos; cond: Ast; corps: Ast_Tabs.Tab) return Ast;

   function creer_boucle_tq(pos: Source_Pos; cond: Ast; corps: Ast_Tabs.Tab) return Ast;

   procedure afficher(arbre: Ast_Record);

   procedure afficher(arbres: Ast_Tabs.Tab);

   -- Represente une variable enfermé par une fonction
   type Cloture is record
      idf: PString;
      -- Indice de la variable dans le contexte parent à la fonction
      origine_indice: Natural;
      -- Indice de la variable dans la fonction
      destination_indice: Natural;
   end record;

   package Cloture_Tabs is new Tabs(Cloture);

   type Ast_Record(nature : Nature_Ast) is tagged record
      pos: Source_Pos;

      case nature is
         when Litteral =>
            val: Valeur;
         when Identifiant =>
            idf_nom: PString;
            -- ** Inference
            -- L'identifiant represente t'il une primitive
            idf_primitive: Boolean;
            idf_indice: Natural;
         when Fonction =>
            fn_genre: Genre_Decl;
            fn_args: PString_Tabs.Tab;
            fn_corps: Ast_Tabs.Tab;
            fn_nom: PString;
            -- ** Inference
            -- Nombre de variable locale de la fonction
            fn_allocation: Natural;
            -- Indique s'il est necessaire d'ajouter
            -- "manuellement" un retour à la fin de
            -- la fonction
            fn_retourne: Boolean;
            -- Indique si la fonction est recursive
            fn_rec: Boolean;
            fn_rec_nom: PString;
            fn_rec_indice: Natural;
            -- Variables enfermées par la fonction
            fn_clotures: Cloture_Tabs.Tab;
         when Operation =>
            bin_operateur: Op_Binaire;
            bin_gauche: Ast;
            bin_droite: Ast;
            -- ** Inference
            -- Nom de la primitive correspondante à cet opérateur
            -- La raison pour laquelle il est necessaire pour
            -- l'inférence de déterminer ce nom est que les
            -- opérateurs arithmétiques sont disptachés
            -- statiquement selon que les opérandes soit des
            -- Entiers ou des Flottants.
            bin_primitive: PString;
            -- Indique si l'inférence a déterminé que le deuxième
            -- opérande d'un opérateur paresseux est inutile
            -- (E.g. vrai et 'expr')
            bin_courtcircuite: Boolean := false;
         when Unaire =>
            unr_operateur: Op_Unaire;
            unr_operande: Ast;
            -- ** Inference
            -- Même justification que pour les opérateurs binaires
            unr_primitive: PString;
         when Appel =>
            app_fn: Ast;
            app_args: Ast_Tabs.Tab;
         when Affectation =>
            var_idf: PString;
            var_expr: Ast;
            -- ** Inference
            var_indice: Natural;
         when Retour =>
            ret_expr: Ast;
         when Si =>
            si_cond: Ast;
            si_corps: Ast_Tabs.Tab;
            sinon_corps: Ast_Tabs.Tab;
            -- ** Inference
            -- Permet de determiner si la condition
            -- est toujours vrai ou fausse
            si_cond_genre: Genre_Decl;
         when Tq | Boucle_Tq =>
            tq_cond: ast;
            tq_corps: Ast_Tabs.Tab;
            -- ** Inference
            -- Permet de determiner si la condition
            -- est toujours vrai ou fausse
            tq_cond_genre: Genre_Decl;
      end case;
   end record;

end;
