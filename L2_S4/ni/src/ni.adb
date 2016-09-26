with Ada.Text_IO, Ada.Command_Line;

with Sources;
with Lexer;
with Parser;

with Compiler;
with Asts;
with Vm;
with Programmes;
with Programmes.Entree;

with Standard;
with Graphique;

procedure Ni is
   use Ada.Text_IO, Ada.Command_Line;

   procedure lex is
      use Lexer;
   begin
      demarrer(argument(2));
      while courant.nature /= FIN_SEQUENCE loop 
         courant.afficher;
         new_line;
         avancer;
      end loop;

      arreter;
   end;

   procedure ast is
   begin
      Parser.analyser(argument(2)).afficher;
   end;

   procedure cpl is
   begin
      Programmes.ecrire(argument(2) & 'c',
         Compiler.compiler(
               Parser.analyser(argument(2)),
               Sources.creer_source(argument(2))));
   end;

   procedure run is
      use Programmes;
      pgm: Programme;
   begin
      if argument(2)(argument(2)'last) = 'c' then
         pgm := Programmes.Entree.lire(argument(2));
      else
         pgm := Compiler.compiler(
               Parser.analyser(argument(2)),
               Sources.creer_source(argument(2)));
      end if;
      Vm.executer(pgm);
   end;

begin
   if argument_count < 1 then
      put_line("Nom de commande manquant");
      put("Utilisation : ");
      put(command_name);
      put_line(" lex|ast|cpl|run <fichier>");
      return;
   elsif argument_count < 2 then 
      put_line("Nom de fichier attendu");
      put("Utilisation : ");
      put(command_name);
      put(" ");
      put(argument(1));
      put_line(" <fichier>");
      return;
   elsif argument(1) = "lex" then
      lex;
   elsif argument(1) = "ast" then
      ast;
   elsif argument(1) = "cpl" then
      cpl;
   elsif argument(1) = "run" then
      run;
   else
      put("Commande ");
      put(argument(1));
      put_line(" inconnu");
      put("Utilisation : ");
      put(command_name);
      put_line(" lex|ast|cpl|run <fichier>");
   end if;
end;
