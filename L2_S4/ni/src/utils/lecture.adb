package body Lecture is

   use Ada.Text_IO;

   l_courant: Character;
   l_pos : Source_Pos;
   fichier : Boolean;
   entree : Ada.Text_IO.File_Type;

   procedure lire_caractere;

   procedure demarrer is
   begin
      l_pos.ligne := 1;
      l_pos.colonne := 1;
      fichier := false;
      lire_caractere;
   end;

   procedure demarrer(nom_fichier : in String) is
   begin
      l_pos.ligne := 1;
      l_pos.colonne := 1;
      fichier := true;
      open(entree, In_File, nom_fichier);
      lire_caractere;
   end;

   procedure avancer is
   begin
      if l_courant = ASCII.NUL then
         return;
      elsif l_courant = ASCII.LF then
         l_pos.ligne := l_pos.ligne + 1;
         l_pos.colonne := 1;
      else
         l_pos.colonne := l_pos.colonne + 1;
      end if;
      lire_caractere;
   end;

   procedure lire_caractere is
   begin
      if fichier then
         if end_of_file(entree) then
            l_courant := ASCII.NUL;
         elsif end_of_line(entree) then
            l_courant := ASCII.LF;
            skip_line(entree);
         else
            get(entree, l_courant);
         end if;
      else
         if end_of_file(Standard_Input) then
            l_courant := ASCII.NUL;
         elsif end_of_line(Standard_Input) then
            l_courant := ASCII.LF;
            skip_line(Standard_Input);
         else
            get(Standard_Input, l_courant);
         end if;
      end if;
   end;

   procedure consommer(car: Character) is
      cars: String(1..1);
   begin
      cars(1) := car;
      consommer(cars);
   end;

   procedure consommer(cars: String) is
      ok: Boolean;
   begin
      loop
         ok := true;
         for i in cars'range loop
            ok := ok and l_courant /= cars(i);
         end loop;
      exit when est_fin or ok;
         avancer;
      end loop;
   end;

   function courant return Character is
   begin
      return l_courant;
   end;

   function est_fin return Boolean is
   begin
      return l_courant = ASCII.NUL;
   end;

   function pos return Source_Pos is
   begin
      return l_pos;
   end;

   procedure arreter is
   begin
      if fichier then
         close(entree);
      end if;
   end;

end;
