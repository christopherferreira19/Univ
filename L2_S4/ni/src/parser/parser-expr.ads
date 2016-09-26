with PStrings;

package Parser.Expr is

   function rec_expression return Ast;

   function rec_appel(expr: Ast) return Ast;

private

   use PStrings;

   function rec_disj return Ast;
   function rec_suite_disj(ag: Ast) return Ast;
   function rec_conj return Ast;
   function rec_suite_conj(ag: Ast) return Ast;
   function rec_rel return Ast;
   function rec_somme return Ast;
   function rec_suite_somme(ag: Ast) return Ast;
   function rec_terme return Ast;
   function rec_suite_terme(ag: Ast) return Ast;
   function rec_facteur return Ast;
   function rec_unaire return Ast;
   function rec_atome return Ast;

   function rec_appel_args return Ast_Tabs.Tab;
   procedure rec_appel_suite_args(args: in out Ast_Tabs.TabCreation);

   function rec_fn return Ast;
   procedure rec_fn_args(
            args_n: in out PString_Tabs.TabCreation;
            args_g: in out Genre_Decl_Tabs.TabCreation);
   procedure rec_fn_arg(
            args_n: in out PString_Tabs.TabCreation;
            args_g: in out Genre_Decl_Tabs.TabCreation);
   procedure rec_fn_suite_arg(
            args_n: in out PString_Tabs.TabCreation;
            args_g: in out Genre_Decl_Tabs.TabCreation);
   function rec_fn_retour return Genre_Decl;

end;