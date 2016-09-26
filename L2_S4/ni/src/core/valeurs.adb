with Ada.Text_IO;
with Ada.Integer_Text_IO;
with Ada.Float_Text_IO;

package body Valeurs is

   v_unit: aliased Valeur_Record(Unit);
   v_vrai: aliased Valeur_Record(Vrai);
   v_faux: aliased Valeur_Record(Faux);

   function unit return Valeur is
   begin
      return v_unit'access;
   end;

   function vrai return Valeur is
   begin
      return v_vrai'access;
   end;

   function faux return Valeur is
   begin
      return v_faux'access;
   end;

   function booleen(bool: Boolean) return Valeur is
   begin
      if bool then
         return vrai;
      else
         return faux;
      end if;
   end;

   function creer_entier(ent: Integer) return Valeur is
      val: Valeur;
   begin
      val := new Valeur_Record(Entier);
      val.ent := ent;
      return val;
   end;

   function creer_flottant(flot: Float) return Valeur is
      val: Valeur;
   begin
      val := new Valeur_Record(Flottant);
      val.flot := flot;
      return val;
   end;

   function ada_float(val: Valeur_Record; conversion: Boolean) return Float is
   begin
      if val.genre = Flottant then
         return val.flot;
      elsif conversion and val.genre = Entier then
         return Float(val.ent);
      else
         raise Genre_Invalide;
      end if;
   end;

   function creer_chaine(chn: PString) return Valeur is
      val: Valeur;
   begin
      val := new Valeur_Record(Chaine);
      val.chn := chn;
      return val;
   end;

   function creer_cloture(routine: Positive; rec: Boolean; clotures: Valeur_Tabs.Tab) return Valeur is
      val: Valeur;
   begin
      val := new Valeur_Record(Cloture);
      val.routine := routine;
      val.rec := rec;
      val.clotures := clotures;
      return val;
   end;

   function creer_primitive(ptr: Ptr_Primitive) return Valeur is
      val: Valeur;
   begin
      val := new Valeur_Record(Primitive);
      val.ptr := ptr;
      return val;
   end;

   function genre(val: Valeur_Record) return Genre_Base is
   begin
      return val.genre;
   end;

   function str(val: Valeur_Record) return String is
   begin
      case val.genre is
         when Unit => return "unit";
         when Vrai => return "vrai";
         when Faux => return "faux";
         when Entier => return integer_str(val.ent).all;
         when Flottant => return float_str(val.flot).all;
         when Chaine => return val.chn.all;
         when Cloture => return "[Cloture " & integer_str(val.routine).all & "]";
         when Primitive => return "[Primitive ]";
      end case;
   end;

   function str_code(val: Valeur_Record) return String is
   begin
      if val.genre = Chaine then
         return '"' & val.str & '"';
      else
         return val.str;
      end if;
   end;

   procedure afficher(val: Valeur_Record) is
      use Ada.Text_IO;
   begin
      put(val.str);
   end;

   procedure afficher_code(val: Valeur_Record) is
      use Ada.Text_IO;
   begin
      put(val.str_code);
   end;

end;
