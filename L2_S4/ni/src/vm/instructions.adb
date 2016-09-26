with Ada.Integer_Text_IO;

package body Instructions is

   use Ada.Integer_Text_IO;

   function empiler(pos: Source_Pos; litteral: Valeur) return Instruction is
      instr: Instruction := new Instruction_Record(Empiler);
   begin
      instr.pos := pos;
      instr.litteral := litteral;
      return instr;
   end;

   function depiler(pos: Source_Pos) return Instruction is
      instr: Instruction := new Instruction_Record(Depiler);
   begin
      instr.pos := pos;
      return instr;
   end;

   function charger(pos: Source_Pos; idf: PString; addr: Natural) return Instruction is
      instr: Instruction := new Instruction_Record(Charger);
   begin
      instr.pos := pos;
      instr.idf := idf;
      instr.addr := addr;
      return instr;
   end;

   function stocker(pos: Source_Pos; idf: PString; addr: Natural) return Instruction is
      instr: Instruction := new Instruction_Record(Stocker);
   begin
      instr.pos := pos;
      instr.idf := idf;
      instr.addr := addr;
      return instr;
   end;

   function primitive(pos: Source_Pos; nom: PString) return Instruction is
      instr: Instruction := new Instruction_Record(Primitive);
   begin
      instr.pos := pos;
      instr.nom := nom;
      return instr;
   end;

   function appel(pos: Source_Pos) return Instruction is
      instr: Instruction := new Instruction_Record(Appel);
   begin
      instr.pos := pos;
      return instr;
   end;

   function cloture(pos: Source_Pos; routine: PString; rec: Boolean; clotures: Natural) return Instruction is
      instr: Instruction := new Instruction_Record(Cloture);
   begin
      instr.pos := pos;
      instr.routine := routine;
      instr.rec := rec;
      instr.clotures := clotures;
      return instr;
   end;

   function saut(pos: Source_Pos; decalage: Integer) return Instruction is
      instr: Instruction := new Instruction_Record(Saut);
   begin
      instr.pos := pos;
      instr.decalage := decalage;
      return instr;
   end;

   function saut_vrai(pos: Source_Pos; decalage: Integer) return Instruction is
      instr: Instruction := new Instruction_Record(Saut_Vrai);
   begin
      instr.pos := pos;
      instr.decalage := decalage;
      return instr;
   end;

   function saut_faux(pos: Source_Pos; decalage: Integer) return Instruction is
      instr: Instruction := new Instruction_Record(Saut_Faux);
   begin
      instr.pos := pos;
      instr.decalage := decalage;
      return instr;
   end;

   function retour(pos: Source_Pos) return Instruction is
      instr: Instruction := new Instruction_Record(Retour);
   begin
      instr.pos := pos;
      return instr;
   end;

   procedure afficher(instr: Instruction) is
   begin
      ecrire(Standard_Output, instr);
   end;

   procedure ecrire(sortie: File_Type; instr: Instruction) is
   begin
      case instr.nature is
         when Empiler =>
            put(sortie, "EPL ");
            put(sortie, instr.litteral.str_code);
         when Depiler =>
            put(sortie, "DPL");
         when Charger =>
            put(sortie, "CHR ");
            put(sortie, instr.addr, 0);
            put(sortie, " &");
            put(sortie, instr.idf);
         when Stocker =>
            put(sortie, "STK ");
            put(sortie, instr.addr, 0);
            put(sortie, " &");
            put(sortie, instr.idf);
         when Primitive =>
            put(sortie, "PRM ");
            if instr.nom /= NULL then
               put(sortie, instr.nom);
            end if;
         when Cloture =>
            put(sortie, "CLT ");
            put(sortie, instr.routine);
            put(sortie, " ");
            if instr.rec then
               put(sortie, "~");
            end if;
            put(sortie, instr.clotures, 0);
         when Saut =>
            put(sortie, "STI ");
            put(sortie, instr.decalage, 0);
         when Saut_Vrai =>
            put(sortie, "STV ");
            put(sortie, instr.decalage, 0);
         when Saut_Faux =>
            put(sortie, "STF ");
            put(sortie, instr.decalage, 0);
         when Appel =>
            put(sortie, "APP");
         when Retour =>
            put(sortie, "RET");
      end case;
      put(sortie, " @");
      put(sortie, integer_str(instr.pos.ligne));
      put(sortie, ":");
      put(sortie, integer_str(instr.pos.colonne));
   end;

end;
