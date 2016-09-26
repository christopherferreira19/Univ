with Ada.Text_IO, Ada.Integer_Text_IO;

with Lecture;

package body Programmes.Entree is

   use Ada.Text_IO, Ada.Integer_Text_IO;

   function lire(nom_fichier: String) return Programme is
      prog: Programme;
   begin
      Lecture.demarrer(nom_fichier);
      prog := lire;
      Lecture.arreter;
      return prog;
   end;

   procedure consommer_separateurs is
   begin
      Lecture.consommer((' ', ASCII.HT));
   end;

   procedure consommer_separateurs_lignes is
   begin
      Lecture.consommer((' ', ASCII.HT, ASCII.LF, ASCII.CR));
   end;

   procedure raise_erreur_lecture(msg: String) is
   begin
      put("Erreur '");
      if Lecture.courant = ASCII.LF or Lecture.courant = ASCII.CR then
         put("\n");
      else
         put(Lecture.courant);
      end if;
      put("'@");
      put(Lecture.pos.ligne, 0);
      put(":");
      put(Lecture.pos.colonne, 0);
      put(" : ");
      put(msg);
      new_line;
      raise Erreur_Lecture;
   end;

   procedure separateurs is
   begin
      if Lecture.courant /= ' '
            and Lecture.courant /= ASCII.HT then
         raise_erreur_lecture("Séparateur attendu");
      end if;
      consommer_separateurs;
   end;

   procedure retour_a_la_ligne is
   begin
      consommer_separateurs;
      if not Lecture.est_fin
            and Lecture.courant /= ASCII.LF
            and Lecture.courant /= ASCII.CR then
         raise_erreur_lecture("Retour à la ligne attendu");
      end if;
      consommer_separateurs_lignes;
   end;

   function lire return Programme is
      use Instructions;

      rtn: Routine;
      rtns: Routine_Tabs.TabCreation;
   begin
      consommer_separateurs_lignes;
      if Lecture.courant /= '[' then
         raise_erreur_lecture("Déclaration de la routine principale manquante");
      end if;
      Lecture.avancer;
      consommer_separateurs;
      if Lecture.courant /= ']' then
         raise_erreur_lecture("Déclaration de la routine principale manquante");
      end if;
      Lecture.avancer;
      consommer_separateurs;

      rtn := new Routine_Record;
      rtn.nom := new String'("");
      rtn.allocation := lire_entier;
      retour_a_la_ligne;

      loop
         rtn.corps := lire_corps;
         rtns.ajouter(rtn);
      exit when Lecture.est_fin;
         Lecture.avancer;
         consommer_separateurs;
         rtn := new Routine_Record;
         rtn.nom := lire_string;
         consommer_separateurs;
         if Lecture.courant /= ']' then
            raise_erreur_lecture("Crochet fermant attendu");
         end if;
         Lecture.avancer;
         consommer_separateurs;
         rtn.allocation := lire_entier;
         retour_a_la_ligne;
      end loop;

      return creer_programme(rtns.creer);
   end;

   function lire_corps return Code.Tab is
      corps: Code.TabCreation;
   begin
      loop
         corps.ajouter(lire_instruction);
         retour_a_la_ligne;
      exit when Lecture.est_fin or Lecture.courant = '[';
      end loop;

      return corps.creer;
   end;

   function chiffre(c : Character) return Natural is
   begin
      return Character'pos(c) - Character'pos('0');
   end;

   function lire_entier return Integer is
      negatif: Boolean;
      ent: Integer;
   begin
      negatif := false;
      if Lecture.courant = '-' then
         negatif := true;
         Lecture.avancer;
      end if;

      if not (Lecture.courant in '0'..'9') then
         raise_erreur_lecture("Entier attendu");
      end if;

      ent := 0;
      loop
         ent := ent * 10;
         ent := ent + chiffre(Lecture.courant);
         Lecture.avancer;
      exit when Lecture.courant not in '0'..'9';
      end loop;

      if negatif then
         return -ent;
      else
         return ent;
      end if;
   end;

   function lire_flottant_decimal(ent: Integer) return Float is
      res: Float := Float(ent);
      decimal: Float := 10.0;
   begin
      while Lecture.courant in '0'..'9' loop
         res := res + (Float(chiffre(Lecture.courant)) / decimal);
         decimal := decimal * 10.0;
         Lecture.avancer;
      end loop;
      return res;
   end;

   function lire_string return PString is
      str: PString := new String'("");
   begin
      if Lecture.courant not in 'a'..'z'
            and Lecture.courant not in 'A'..'Z'
            and Lecture.courant not in '0'..'9'
            and Lecture.courant /= '_'
            and Lecture.courant /= '$' then
         raise_erreur_lecture("Identifiant attendu");
      end if;

      loop
         str := new String'(str.all & Lecture.courant);
         Lecture.avancer;
      exit when not (Lecture.est_fin
            or Lecture.courant in 'a'..'z'
            or Lecture.courant in 'A'..'Z'
            or Lecture.courant in '0'..'9'
            or Lecture.courant = '_'
            or Lecture.courant = '$');
      end loop;
      return str;
   end;

   function lire_litteral return Valeur is
      procedure lire_mot_cle(mot_cle: String) is
      begin
         for i in mot_cle'range loop
            if Lecture.courant /= mot_cle(i) then
               raise_erreur_lecture(mot_cle & " attendu");
            end if;
            Lecture.avancer;
         end loop;
      end;

      str: PString := new String'("");
      ent: Integer;
   begin
      if Lecture.courant = '"' then
         Lecture.avancer;
         while not Lecture.est_fin
               and Lecture.courant /= '"' loop
            str := new String'(str.all & Lecture.courant);
            Lecture.avancer;
         end loop;
         Lecture.avancer;
         return creer_chaine(str);
      elsif Lecture.courant = 'u' then
         lire_mot_cle("unit");
         return unit;
      elsif Lecture.courant = 'v' then
         lire_mot_cle("vrai");
         return vrai;
      elsif Lecture.courant = 'f' then
         lire_mot_cle("faux");
         return faux;
      else
         ent := lire_entier;
         if Lecture.courant = '.' then
            Lecture.avancer;
            return creer_flottant(lire_flottant_decimal(ent));
         else
            return creer_entier(ent);
         end if;
      end if;
   end;

   function lire_pos return Source_Pos is
      pos: Source_Pos;
   begin
      consommer_separateurs;
      if Lecture.courant /= '@' then
         pos.ligne := 1;
         pos.colonne := 1;
      else
         Lecture.avancer;
         pos.ligne := lire_entier;
         if Lecture.courant /= ':' then
            raise_erreur_lecture("Séparateur position attendu");
         end if;
         Lecture.avancer;
         pos.colonne := lire_entier;
      end if;

      return pos;
   end;

   function lire_instruction return Instruction is
      nature_str: String (1..3);
      lit: Valeur;
      rec: Boolean;
      ent: Integer;
      str: PString := new String'("");
   begin
      nature_str(1) := Lecture.courant;
      Lecture.avancer;
      nature_str(2) := Lecture.courant;
      Lecture.avancer;
      nature_str(3) := Lecture.courant;
      Lecture.avancer;

      consommer_separateurs;
      if nature_str = "EPL" then
         lit := lire_litteral;
         return empiler(lire_pos, lit);
      elsif nature_str = "DPL" then
         return depiler(lire_pos);
      elsif nature_str = "CHR" then
         ent := lire_entier;
         separateurs;
         if Lecture.courant = '&' then
            Lecture.avancer;
            str := lire_string;
         else
            str := new String'("");
         end if;
         return charger(lire_pos, str, ent);
      elsif nature_str = "STK" then
         ent := lire_entier;
         separateurs;
         if Lecture.courant = '&' then
            Lecture.avancer;
            str := lire_string;
         else
            str := new String'("");
         end if;
         return stocker(lire_pos, str, ent);
      elsif nature_str = "PRM" then
         str := lire_string;
         return primitive(lire_pos, str);
      elsif nature_str = "CLT" then
         str := lire_string;
         separateurs;
         if Lecture.courant = '~' then
            rec := true;
            Lecture.avancer;
         else
            rec := false;
         end if;
         ent := lire_entier;
         return cloture(lire_pos, str, rec, ent);
      elsif nature_str = "STI" then
         ent := lire_entier;
         return saut(lire_pos, ent);
      elsif nature_str = "STV" then
         ent := lire_entier;
         return saut_vrai(lire_pos, ent);
      elsif nature_str = "STF" then
         ent := lire_entier;
         return saut_faux(lire_pos, ent);
      elsif nature_str = "APP" then
         return appel(lire_pos);
      elsif nature_str = "RET" then
         return retour(lire_pos);
      else
         raise_erreur_lecture("Instruction attendu");
         return null;
      end if;
   end;

end;
