with PStrings;
with Sources;
with Genres;
with Valeurs;
with Primitives;
with Asts;
with Envs;

package Inference is

   Ast_Invalide: exception;

   use PStrings;
   use Sources;
   use Genres;
   use Asts;

   procedure inferer(arbre: Ast; src_arg: Source);
   procedure inferer(arbre: Ast);

private

   use Valeurs;
   use Primitives;
   use Envs;

   type Fn_InferenceRecord;
   type Fn_Inference is access Fn_InferenceRecord;
   type Fn_InferenceRecord is record
      parent: Fn_Inference;
      arbre: Ast;
      env: Environnement;
      clotures: Cloture_Tabs.TabCreation;
   end record;

   src: Source;
   erreur_rencontre: Boolean;
   Inference_Erreur: exception;
   fn: Fn_Inference;
   fn_compte: Natural;

   procedure inferer_fonction(nom: PString; arbre: Ast);

   procedure initialiser_fn_nom(nom: PString);

   procedure inferer_corps(arbres: Ast_Tabs.Tab);

   procedure inferer_instruction(arbre: Ast);

   function inferer_expression(arbre: Ast) return Genre_Decl;

   function inferer_expression(nom: PString; arbre: Ast) return Genre_Decl;

   function inferer_litteral(arbre: Ast) return Genre_Decl;

   function inferer_identifiant(arbre: Ast) return Genre_Decl;

   function inferer_binaire(arbre: Ast) return Genre_Decl;

   function inferer_unaire(arbre: Ast) return Genre_Decl;

   function inferer_appel(arbre: Ast) return Genre_Decl;

   procedure inferer_appel(arbre: Ast);

   function inferer_appel_args(args: Ast_Tabs.Tab) return Genre_Decl_Tabs.Tab;

   procedure inferer_affectation(arbre: Ast);

   procedure inferer_retour(arbre: Ast);

   procedure inferer_si(arbre: Ast);

   procedure inferer_tq(arbre: Ast);

   procedure inferer_boucle_tq(arbre: Ast);

end;