with Sources;
with Asts;
with Instructions;
with Programmes;

package Compiler is

   use Sources;
   use Asts;
   use Instructions;
   use Programmes;

   function compiler(arbre: Ast) return Programme;

   function compiler(arbre: Ast; src: Source) return Programme;

private
   
   procedure compiler_arbre(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_arbres(arbres: Ast_Tabs.Tab; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_litteral(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_identifiant(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_fonction(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_routine(arbre: Ast; rtns: in out Routine_Tabs.TabCreation);
   procedure compiler_operation(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_operation_paresseuse(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_unaire(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_appel(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_affectation(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_retour(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_si(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_tq(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);
   procedure compiler_boucle_tq(arbre: Ast; rtns: in out Routine_Tabs.TabCreation; corps: in out Code.TabCreation);

end;
