package body Programmes is

   function creer_routine(nom: PString; allocation: Natural) return Routine is
      rtn: Routine := new Routine_Record;
   begin
      rtn.nom := nom;
      rtn.allocation := allocation;
      return rtn;
   end;

   procedure init_routine_corps(rtn: Routine; corps: Code.Tab) is
   begin
      rtn.corps := corps;
   end;

   function creer_programme(rtns: Routine_Tabs.Tab) return Programme is
      pgm: Programme;
   begin
      pgm.rtns := rtns;
      return pgm;
   end;

   procedure afficher(prog: Programme) is
   begin
      ecrire(Standard_Output, prog);
   end;

   procedure ecrire(nom_fichier: String; prog: Programme) is
      sortie: File_Type;
   begin
      create(sortie, Out_File, nom_fichier);
      ecrire(sortie, prog);
      close(sortie);
   end;

   procedure ecrire(sortie: File_Type; prog: Programme) is
      rtn: Routine;
   begin
      for i in 1..prog.rtns.taille loop
         rtn := prog.rtns.element(i);
         put(sortie, "[");
         put(sortie, rtn.nom);
         put(sortie, "] ");
         put(sortie, integer_str(rtn.allocation));
         new_line(sortie);
         for j in 1..rtn.corps.taille loop
            ecrire(sortie, rtn.corps.element(j));
            new_line(sortie);
         end loop;
         new_line(sortie);
      end loop;
   end;
end;