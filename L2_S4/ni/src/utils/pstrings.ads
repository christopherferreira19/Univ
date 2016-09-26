-- Package contenant notamment un type PString
-- utile pour gérer des chaines à taille variable

with Ada.Text_IO;
with Tabs;

package PStrings is

   use Ada.Text_IO;

   type PString is access all String;

   package PString_Tabs is new Tabs(PString);

   function integer_str(ent: Integer) return PString;

   function float_str(f: Float) return PString;

   procedure put(s: PString);

   procedure put(f: File_Type; s: PString);

   procedure put_line(s: PString);

   procedure put_line(f: File_Type; s: PString);

end;
