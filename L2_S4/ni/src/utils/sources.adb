with Ada.Text_IO;
with Lecture;

package body Sources is

   function creer_source(fichier: String) return Source is
      sv: Source := new Source_Record;
   begin
      sv.fichier := new String'(fichier);
      return sv;
   end;

   procedure afficher_pos(vue: Source_Record; pos: Source_Pos) is
      use Ada.Text_IO;
   begin
      Lecture.demarrer(vue.fichier.all);
      while not Lecture.est_fin
            and Lecture.pos.ligne < pos.ligne loop
         Lecture.avancer;
      end loop;

      while not Lecture.est_fin
            and Lecture.courant /= ASCII.LF
            and Lecture.courant /= ASCII.CR
            and Lecture.pos.ligne = pos.ligne loop
         put(Lecture.courant);
         Lecture.avancer;
      end loop;
      new_line;
      for i in 1..(pos.colonne-1) loop
         put(" ");
      end loop;
      put("^");
      new_line;

      Lecture.arreter;
   end;
end;