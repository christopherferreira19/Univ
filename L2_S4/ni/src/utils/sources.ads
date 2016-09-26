with PStrings;

package Sources is

   use PStrings;

   type Source_Pos is record
      ligne: Positive;
      colonne: Positive;
   end record;

   type Source_Record is tagged private;
   type Source is access all Source_Record;

   function creer_source(fichier: String) return Source;

   procedure afficher_pos(vue: Source_Record; pos: Source_Pos);

private

   type Source_Record is tagged record
      fichier: PString;
   end record;

end;