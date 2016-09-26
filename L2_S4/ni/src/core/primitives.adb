package body Primitives is

   use PrimitiveMap;

   map: PrimitiveMap.Map;

   procedure enregistrer(nom: PString; val: Valeur; genre: Genre_Decl) is
      prm: Primitive := (nom, genre, val);
   begin
      map.insert(nom.all, prm);
   end;

   procedure enregistrer(nom: PString; ptr: Ptr_Primitive;
         retour: Genre_Decl) is
   begin
      enregistrer(nom, creer_primitive(ptr),
         fonction(Genre_Decl_Tabs.creer_vide, retour));
   end;

   procedure enregistrer(nom: PString; ptr: Ptr_Primitive;
         retour: Genre_Decl;
         arg: Genre_Decl) is
   begin
      enregistrer(nom, creer_primitive(ptr),
         fonction(Genre_Decl_Tabs.creer_singleton(arg), retour));
   end;

   procedure enregistrer(nom: PString; ptr: Ptr_Primitive;
         retour: Genre_Decl;
         args: Genre_Decl_Tabs.TabAry) is
   begin
      enregistrer(nom, creer_primitive(ptr),
         fonction(Genre_Decl_Tabs.creer(args), retour));
   end;

   function existe(nom: PString) return Boolean is
   begin
      return map.contains(nom.all);
   end;

   function element(nom: PString) return Primitive is
      c: Cursor;
   begin
      c := map.find(nom.all);
      if not has_element(c) then
         raise Primitive_Inexistante with "Primitive '" & nom.all & "'";
      end if;

      return element(c);
   end;

   function nom(prm: Primitive) return PString is
   begin
      return prm.nom;
   end;

   function genre(prm: Primitive) return Genre_Decl is
   begin
      return prm.genre;
   end;

   function val(prm: Primitive) return Valeur is
   begin
      return prm.val;
   end;

end;
