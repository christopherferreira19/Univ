-- Package générique pour simplifier l'utilisation des tableaux
--
-- 1) Les tableaux sont cachés derrière des pointeurs,
-- permettant ainsi de les utiliser dans des records sans
-- spécifier de taille arbritraire.
--
-- 2) Il sont prévues pour être utilisés de manière "immuable"
-- i.e. leur taille est fixée (à l'éxecution et non la compilation)
--
-- 3) Quelque fonctions et un type `TabCreation` (ce dernier suivant
-- le design pattern Builder) sont fournies pour créér ces tableaux
-- de manière simple.

generic
   type T_Element is private;

package Tabs is

   type TabAry is array (Positive range <>) of T_Element;

   type TabCreation is tagged private;

   type TabRecord(taille: Natural) is tagged private;
   type Tab is access TabRecord;

   function creer_vide return Tab;

   function creer_singleton(element: T_Element) return Tab;

   function creer(ary: TabAry) return Tab;

   function taille(creation: TabCreation) return Natural;

   procedure ajouter(creation: in out TabCreation; element: T_Element);

   function element(creation: in out TabCreation; i: Positive) return T_Element;

   procedure remplacer(creation: in out TabCreation; i: Positive; element: T_Element);

   procedure integrer(creation: in out TabCreation; autre: in out TabCreation);

   function creer(creation: TabCreation) return Tab;

   function taille(t: TabRecord) return Natural;

   function element(t: TabRecord; i: Positive) return T_Element;

private

   type TabRecord(taille: Natural) is tagged record
      ary: TabAry (1..taille);
   end record;

   type TabCreationCelluleRecord;
   type TabCreationCellule is access TabCreationCelluleRecord;
   type TabCreationCelluleRecord is record
      element: T_Element;
      precedent: TabCreationCellule;
   end record;

   type TabCreation is tagged record
      taille: Natural := 0;
      queue: TabCreationCellule := null;
   end record;

end;
