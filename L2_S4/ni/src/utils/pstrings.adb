with Ada.Integer_Text_IO;
with Ada.Float_Text_IO;

package body PStrings is

   function integer_str(ent: Integer) return PString is
      use Ada.Integer_Text_IO;

      str: String(1..30);
      i: Integer;
   begin
      put(str, ent);

      i := str'first;
      while str(i) = ' ' loop
         i := i + 1;
      end loop;

      return new String'(str(i..str'last));
   end;

   function float_str(f: Float) return PString is
      use Ada.Float_Text_IO;

      str: String(1..30);
      i, j: Integer;
   begin
      put(str, f, Aft => 5, Exp => 0);

      i := str'first;
      while str(i) = ' ' loop
         i := i + 1;
      end loop;

      j := str'last;
      while str(j) = '0' loop
         j := j - 1;
      end loop;
      if str(j) = '.' then
         j := j + 1;
      end if;

      return new String'(str(i..j));
   end;

   procedure put(s: PString) is
   begin
      put(s.all);
   end;

   procedure put(f: File_Type; s: PString) is
   begin
      put(f, s.all);
   end;

   procedure put_line(s: PString) is
   begin
      put_line(s.all);
   end;

   procedure put_line(f: File_Type; s: PString) is
   begin
      put_line(f, s.all);
   end;

end;