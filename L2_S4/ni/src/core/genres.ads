-- Les genres sont les types du langages
-- Ils sont de deux types representés par
-- les deux types :
--   - `Genre_Base`: Types concret présent à l'éxecution
--   - `Genre_Decl`: Types logiques utilisés pour les
--                   définitions de fonctions et l'inférence
-- Notes: 
--   - Le type Tous le types Top du langage
--   - Le langage étant très laxiste sur les affectations de
-- variables (i.e. il est possible d'affecter à une variable
-- successivement différent types) le type Union permet de
-- representer le type d'une variable affecté différemment
-- dans deux branches alternatives du code.

with Tabs;

package Genres is

   Genre_Invalide: exception;

   type Genre_Base is (
         Unit,
         Vrai,
         Faux,
         Entier,
         Flottant,
         Chaine,
         Cloture,
         Primitive
   );

   type Nature_Genre_Decl is (
      Base,
      Tous,
      Union,
      Fonction
   );

   type Genre_Decl_Record(nature: Nature_Genre_Decl);
   type Genre_Decl is access all Genre_Decl_Record;
   package Genre_Decl_Tabs is new Tabs(Genre_Decl);

   use Genre_Decl_Tabs;

   function base(genre: Genre_Base) return Genre_Decl;

   function base_unit return Genre_Decl;

   function base_vrai return Genre_Decl;

   function base_faux return Genre_Decl;

   function union_booleen return Genre_Decl;

   function base_entier return Genre_Decl;

   function base_flottant return Genre_Decl;

   function union_nombre return Genre_Decl;

   function base_chaine return Genre_Decl;

   function tous return Genre_Decl;

   function union(gauche: Genre_Decl; droite: Genre_Decl) return Genre_Decl;

   function fonction(args: Genre_Decl_Tabs.Tab; retour: Genre_Decl) return Genre_Decl;

   function generalise(attendu: Genre_Decl_Record; recu: Genre_Decl) return Boolean;

   function generalise(attendu: Genre_Decl_Record; recu: Genre_Decl_Record) return Boolean;

   procedure afficher(genre: Genre_Decl_Record);

   type Genre_Decl_Record(nature: Nature_Genre_Decl) is tagged record
      case nature is
         when Base => genre: Genre_Base;
         when Tous => null;
         when Union =>
            gauche: Genre_Decl;
            droite: Genre_Decl;
         when Fonction =>
            args: Genre_Decl_Tabs.Tab;
            retour: Genre_Decl;
      end case;
   end record;

end;