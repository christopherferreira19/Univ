with Ada.Text_IO;

package body Envs is

   use Definitions;
   use Allocations;

   function creer_env return Environnement is
   begin
      return new EnvironnementRecord;
   end;

   function taille(env: EnvironnementRecord) return Natural is
   begin
      return Natural(env.allocs.length);
   end;

   function est_defini(env: EnvironnementRecord; nom: PString) return Boolean is
   begin
      return env.defins.contains(nom.all);
   end;

   function allocation(env: EnvironnementRecord; nom: PString) return Natural is
      c: Allocations.Cursor;
   begin
      c := env.allocs.find(nom.all);
      if has_element(c) then
         return element(c);
      end if;

      raise Variable_Non_Alloue with "Var " & nom.all;
   end;

   function definition(env: EnvironnementRecord; nom: PString) return Genre_Decl is
      c: Definitions.Cursor;
   begin
      c := env.defins.find(nom.all);
      if has_element(c) then
         return element(c);
      end if;

      raise Variable_Non_Defini with "Var " & nom.all;
   end;

   function retourne(env: in out EnvironnementRecord) return Boolean is
   begin
      return env.retourne;
   end;

   function retour(env: in out EnvironnementRecord) return Genre_Decl is
   begin
      return env.retour;
   end;

   function affecter(env: in out EnvironnementRecord; nom: PString; genre: Genre_Decl)
         return Natural is
      dc: Definitions.Cursor;
      ac: Allocations.Cursor;
      allocation: Natural;
   begin
      dc := env.defins.find(nom.all);
      ac := env.allocs.find(nom.all);
      if has_element(dc) then
         env.defins.replace_element(dc, genre);
         if has_element(ac) then
            allocation := element(ac);
         else
            raise Variable_Non_Alloue with "Var " & nom.all;
         end if;
      else
         env.defins.insert(nom.all, genre);
         if has_element(ac) then
            allocation := element(ac);
         else
            allocation := env.taille;
            env.allocs.insert(nom.all, allocation);
         end if;
      end if;

      return allocation;
   end;

   procedure retour(env: in out EnvironnementRecord; genre: Genre_Decl) is
   begin
      if env.retour = null then
         env.retour := genre;
      else
         env.retour := union(env.retour, genre);
      end if;

      env.retourne := true;
   end;

   procedure finaliser_retour(env: in out EnvironnementRecord) is
   begin
      if not env.retourne then
         if env.retour = null then
            env.retour := base_unit;
         else
            env.retour := union(env.retour, base_unit);
         end if;
      end if;
   end;

   function snapshot(env: EnvironnementRecord) return EnvSnapshot is
      c: Definitions.Cursor;
      snap: EnvSnapshot := new EnvSnapshotRecord;
   begin
      c := env.defins.first;
      while has_element(c) loop
         snap.defins.insert(key(c), element(c));
         next(c);
      end loop;
      snap.retour := env.retour;
      snap.retourne := env.retourne;
      return snap;
   end;

   procedure retablir(env: in out EnvironnementRecord; snap: EnvSnapshot) is
      c: Definitions.Cursor;
   begin
      env.defins.clear;
      c := snap.defins.first;
      while has_element(c) loop
         env.defins.insert(key(c), element(c));
         next(c);
      end loop;
      env.retour := snap.retour;
      env.retourne := snap.retourne;
   end;

   function fusionner(snap1: EnvSnapshot; snap2: EnvSnapshot) return EnvSnapshot is
      c1: Definitions.Cursor;
      c2: Definitions.Cursor;
      snap: EnvSnapshot := new EnvSnapshotRecord;
   begin
      c1 := snap1.defins.first;
      while has_element(c1) loop
         c2 := snap2.defins.find(key(c1));
         if has_element(c2) then
            snap.defins.insert(key(c1),
                  union(element(c1), element(c2)));
         end if;
         next(c1);
      end loop;

      if snap1.retour = null then
         snap.retour := snap2.retour;
      elsif snap2.retour = null then
         snap.retour := snap1.retour;
      else
         snap.retour := union(snap1.retour, snap2.retour);
      end if;

      snap.retourne := snap1.retourne and snap2.retourne;
      return snap;
   end;

end;
