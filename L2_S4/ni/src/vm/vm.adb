with Ada.Text_IO, Ada.Integer_Text_IO;
with Ada.Exceptions;

with Genres;
with Primitives;
with Operations;
with Instructions;

package body Vm is

   use Genres;
   use Instructions;

   type TraceEntree is record
      routine: Natural;
      instruction: Positive;
   end record;

   pgm: Programme;

   subtype PtrTrace is Integer range 1..1000001;
   type TraceAry is array (1..1000000) of TraceEntree;
   ptr_trace: PtrTrace;
   trace_pile: TraceAry;

   subtype PtrMemoire is Integer range 0..10000001;
   type MemoireAry is array (1..10000000) of Valeur;
   pile_appel: PtrMemoire;
   pile_locales: PtrMemoire;
   memoire: MemoireAry;

   function trace return TraceEntree is
   begin
      return trace_pile(ptr_trace);
   end;

   procedure trace_routine(i: Natural) is
   begin
      trace_pile(ptr_trace).routine := i;
   end;

   procedure trace_instruction(i: Positive) is
   begin
      trace_pile(ptr_trace).instruction := i;
   end;

   procedure executer(pgm_arg: Programme) is
      use Ada.Text_IO, Ada.Exceptions;

      instr: Instruction;
   begin
      pgm := pgm_arg;

      ptr_trace := PtrTrace'first;
      trace_routine(0);
      trace_instruction(1);
      ptr_trace := ptr_trace + 1;
      trace_routine(1);
      trace_instruction(1);

      pile_appel := PtrMemoire'first;
      pile_locales := PtrMemoire'last -
         pgm.rtns.element(trace.routine).allocation;

      while trace.routine > 0 loop
         instr := pgm.rtns.element(trace.routine)
               .corps.element(trace.instruction);
         trace_instruction(trace.instruction + 1);

         begin
            case instr.nature is
               when Empiler => empiler(instr.litteral);
               when Depiler => depiler;
               when Charger => charger(instr.addr);
               when Stocker => stocker(instr.addr);
               when Primitive => primitive(instr.nom);
               when Cloture => cloture(instr.routine, instr.rec, instr.clotures);
               when Saut => saut(instr.decalage);
               when Saut_Vrai => saut_vrai(instr.decalage);
               when Saut_Faux => saut_faux(instr.decalage);
               when Appel => appel;
               when Retour => retour;
            end case;
         exception
            when error: others =>
               new_line;
               new_line;
               put_line("/!\ Erreur /!\");
               put(exception_information(error));
               afficher_trace;
               return;
         end;
      end loop;
   end;

   procedure afficher_trace_entree(tr: TraceEntree);

   procedure afficher_trace is
      use Ada.Text_IO;

      start: Natural;
   begin
      put_line("-- Trace ------------------------------");

      if ptr_trace > 21 then
         start := ptr_trace - 21;
      else
         start := 2;
      end if;

      afficher_trace_entree(trace_pile(ptr_trace-1));
      for i in reverse start..(ptr_trace-2) loop
         afficher_trace_entree(trace_pile(i));
      end loop;

      put_line("---------------------------------------");
   end;

   procedure afficher_trace_entree(tr: TraceEntree) is
      use Ada.Text_IO, Ada.Integer_Text_IO;
      instr: Instruction;
   begin
      instr := pgm.rtns.element(tr.routine).corps.element(tr.instruction - 1);

      put("[");
      put(pgm.rtns.element(tr.routine).nom.all);
      put("]:");
      put(tr.instruction - 1, 0);
      put(": ");
      afficher(instr);
      new_line;
   end;

   procedure empiler(litteral: Valeur) is
   begin
      pile_appel := pile_appel + 1;
      memoire(pile_appel) := litteral;
      if pile_appel >= pile_locales then
         raise Stack_Overflow;
      end if;
   end;

   procedure depiler is
   begin
      pile_appel := pile_appel - 1;
   end;

   function depiler return Valeur is
   begin
      pile_appel := pile_appel - 1;
      return memoire(pile_appel + 1);
   end;

   procedure charger(addr: Natural) is
   begin
      empiler(memoire(pile_locales + addr));
   end;

   procedure stocker(addr: Natural) is
   begin
      memoire(pile_locales + addr) := depiler;
   end;

   procedure primitive(nom: PString) is
      use Primitives;
      prm: Primitives.Primitive;
   begin
      empiler(val(element(nom)));
   end;

   procedure cloture(nom: PString; rec: Boolean; clotures: Natural) is
      vals: Valeur_Tabs.TabCreation;
   begin
      for i in 1..clotures loop
         vals.ajouter(depiler);
      end loop;

      for i in 1..pgm.rtns.taille loop
         if pgm.rtns.element(i).nom.all = nom.all then
            empiler(creer_cloture(i, rec, vals.creer));
            return;
         end if;
      end loop;

      raise Routine_Non_Defini with "Routine " & nom.all;
   end;

   procedure saut(decalage: Integer) is
   begin
      trace_instruction(trace.instruction + decalage - 1);
   end;

   procedure saut_vrai(decalage: Integer) is
   begin
      if depiler = vrai then
         saut(decalage);
      end if;
   end;

   procedure saut_faux(decalage: Integer) is
   begin
      if depiler = faux then
         saut(decalage);
      end if;
   end;

   procedure appel is
      val: Valeur;
   begin
      val := depiler;
      if val.genre = Primitive then
         val.ptr.all;
      else
         for i in reverse 1..val.clotures.taille loop
            empiler(val.clotures.element(i));
         end loop;
         if val.rec then
            empiler(val);
         end if;

         ptr_trace := ptr_trace + 1;
         if ptr_trace = PtrTrace'last then
            raise Stack_Overflow;
         end if;

         trace_routine(val.routine);
         trace_instruction(1);
         pile_locales := pile_locales -
               pgm.rtns.element(trace.routine).allocation;
         if pile_appel >= pile_locales then
            raise Stack_Overflow;
         end if;
      end if;
   end;

   procedure retour is
   begin
      pile_locales := pile_locales +
            pgm.rtns.element(trace.routine).allocation;
      ptr_trace := ptr_trace - 1;
   end;

   procedure pile is
      use Ada.Text_IO;
   begin
      put("-- Pile ---------------------");
      new_line;
      for i in reverse 1..pile_appel loop
         memoire(i).afficher;
         new_line;
      end loop;
      put("-----------------------------");
      new_line;
   end;

end;
