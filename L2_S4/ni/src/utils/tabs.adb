with Ada.Text_IO, Ada.Integer_Text_IO;

package body Tabs is

   function creer_vide return Tab is
   begin
      return new TabRecord(0);
   end;

   function creer_singleton(element: T_Element) return Tab is
      t: Tab;
   begin
      t := new TabRecord(1);
      t.ary(t.ary'first) := element;
      return t;
   end;

   function creer(ary: TabAry) return Tab is
      t: Tab;
   begin
      t := new TabRecord(ary'last);
      for i in ary'range loop
         t.ary(i) := ary(i);
      end loop;
      return t;
   end;

   function taille(creation: TabCreation) return Natural is
   begin
      return creation.taille;
   end;

   procedure ajouter(creation: in out TabCreation; element: T_Element) is
      cell: TabCreationCellule := new TabCreationCelluleRecord;
   begin
      cell.all.element := element;
      cell.all.precedent := creation.queue;
      creation.taille := creation.taille + 1;
      creation.queue := cell;
   end;

   function cell(creation: in out TabCreation; i: Positive) return TabCreationCellule is
      indice: Positive := creation.taille;
      cell: TabCreationCellule := creation.queue;
   begin
      while indice > i and cell /= null loop
         indice := indice - 1;
         cell := cell.precedent;
      end loop;
      return cell;
   end;

   function element(creation: in out TabCreation; i: Positive) return T_Element is
   begin
      return cell(creation, i).all.element;
   end;

   procedure remplacer(creation: in out TabCreation; i: Positive; element: T_Element) is
   begin
      cell(creation, i).all.element := element;
   end;

   procedure integrer(creation: in out TabCreation; autre: in out TabCreation) is
      cell: TabCreationCellule := autre.queue;
   begin
      if cell = null then
         return;
      end if;

      while cell.precedent /= null loop
         cell := cell.precedent;
      end loop;
      cell.precedent := creation.queue;
      creation.taille := creation.taille + autre.taille;
      creation.queue := autre.queue;
      autre.taille := 0;
      autre.queue := null;
   end;

   function creer(creation: TabCreation) return Tab is
      t: Tab := new TabRecord(creation.taille);
      i: Natural := creation.taille;
      c: TabCreationCellule := creation.queue;
   begin
      while i > 0 loop
         t.ary(i) := c.all.element;
         i := i - 1;
         c := c.precedent;
      end loop;
      return t;
   end;

   function taille(t: TabRecord) return Natural is
   begin
      return t.taille;
   end;

   function element(t: TabRecord; i: Positive) return T_Element is
   begin
      return t.ary(i);
   end;

end;
