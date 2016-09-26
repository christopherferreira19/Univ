with Ada.Text_IO;

package body Genres is

   gd_unit: aliased Genre_Decl_Record(Base);
   gd_vrai: aliased Genre_Decl_Record(Base);
   gd_faux: aliased Genre_Decl_Record(Base);
   gd_entier: aliased Genre_Decl_Record(Base);
   gd_flottant: aliased Genre_Decl_Record(Base);
   gd_chaine: aliased Genre_Decl_Record(Base);

   gd_booleen: aliased Genre_Decl_Record(Union);
   gd_booleen2: aliased Genre_Decl_Record(Union);
   gd_nombre: aliased Genre_Decl_Record(Union);
   gd_nombre2: aliased Genre_Decl_Record(Union);

   gd_tous: aliased Genre_Decl_Record(Tous);

   function base(genre: Genre_Base) return Genre_Decl is
   begin
      case genre is
         when Unit => return base_unit;
         when Vrai => return base_vrai;
         when Faux => return base_faux;
         when Entier => return base_entier;
         when Flottant => return base_flottant;
         when Chaine => return base_chaine;
         -- Type présent uniquement à l'éxécution
         when Cloture | Primitive => raise Genre_Invalide;
      end case;
   end;

   function base_unit return Genre_Decl is
   begin
      return gd_unit'access;
   end;

   function base_vrai return Genre_Decl is
   begin
      return gd_vrai'access;
   end;

   function base_faux return Genre_Decl is
   begin
      return gd_faux'access;
   end;

   function union_booleen return Genre_Decl is
   begin
      return gd_booleen'access;
   end;

   function base_entier return Genre_Decl is
   begin
      return gd_entier'access;
   end;

   function base_flottant return Genre_Decl is
   begin
      return gd_flottant'access;
   end;

   function union_nombre return Genre_Decl is
   begin
      return gd_nombre'access;
   end;

   function base_chaine return Genre_Decl is
   begin
      return gd_chaine'access;
   end;

   function tous return Genre_Decl is
   begin
      return gd_tous'access;
   end;

   function union(gauche: Genre_Decl; droite: Genre_Decl) return Genre_Decl is
      tm: Genre_Decl;
   begin
      if gauche.generalise(droite) then
         return gauche;
      elsif droite.generalise(gauche) then
         return droite;
      else
         tm := new Genre_Decl_Record(Union);
         tm.gauche := gauche;
         tm.droite := droite;
         return tm;
      end if;
   end;

   function fonction(args: Genre_Decl_Tabs.Tab; retour: Genre_Decl) return Genre_Decl is
      tm: Genre_Decl;
   begin
      tm := new Genre_Decl_Record(Fonction);
      tm.args := args;
      tm.retour := retour;
      return tm;
   end;

   function generalise(attendu: Genre_Decl_Record; recu: Genre_Decl) return Boolean is
   begin
      return attendu.generalise(recu.all);
   end;

   function generalise(attendu: Genre_Decl_Record; recu: Genre_Decl_Record) return Boolean is
   begin
      case attendu.nature is
         when Tous => return true;
         when Base =>
            case recu.nature is
               when Base => return attendu.genre = recu.genre;
               when others => return false;
            end case;
         when Fonction =>
            case recu.nature is
               when Fonction =>
                  if attendu.args.taille /= recu.args.taille then
                     return false;
                  end if;
                  for i in 1..attendu.args.taille loop
                     if not recu.args.element(i).generalise(attendu.args.element(i)) then
                        return false;
                     end if;
                  end loop;
                  return attendu.retour.generalise(recu.retour);
               when others => return false;
            end case;
         when Union =>
            case recu.nature is
               when Union =>
                  return attendu.generalise(recu.gauche)
                        and attendu.generalise(recu.droite);
               when others =>
                  return attendu.gauche.generalise(recu)
                     or attendu.droite.generalise(recu);
            end case;
      end case;
   end;

   procedure afficher(genre: Genre_Decl_Record) is
      use Ada.Text_IO;
   begin
      if genre = gd_booleen or genre = gd_booleen2 then
         put("Booleen");
      elsif genre = gd_nombre or genre = gd_nombre2 then
         put("Nombre");
      else
         case genre.nature is
            when Tous => put("Tous");
            when Base =>
               case genre.genre is
                  when Unit => put("Unit");
                  when Vrai => put("Vrai");
                  when Faux => put("Faux");
                  when Entier => put("Entier");
                  when Flottant => put("Flottant");
                  when Chaine => put("Chaine");
                  -- Type présent uniquement à l'éxécution
                  when Cloture | Primitive => raise Genre_Invalide;
               end case;
            when Union =>
               genre.gauche.afficher;
               put("|");
               genre.droite.afficher;
            when Fonction =>
               put("Fn");
               if genre.args.taille > 0 then
                  put("(");
                  genre.args.element(1).afficher;
                  for i in 2..genre.args.taille loop 
                     put(", ");
                     genre.args.element(i).afficher;
                  end loop;
                  put(")");
               end if;
               if genre.retour /= base_unit then
                  put(" -> ");
                  genre.retour.afficher;
               end if;
         end case;
      end if;
   end;

begin
   gd_unit.genre := Unit;
   gd_vrai.genre := Vrai;
   gd_faux.genre := Faux;
   gd_entier.genre := Entier;
   gd_flottant.genre := Flottant;
   gd_chaine.genre := Chaine;

   gd_booleen.gauche := gd_vrai'access;
   gd_booleen.droite := gd_faux'access;
   gd_booleen2.gauche := gd_vrai'access;
   gd_booleen2.droite := gd_faux'access;
   gd_nombre.gauche := gd_entier'access;
   gd_nombre.droite := gd_flottant'access;
   gd_nombre2.gauche := gd_entier'access;
   gd_nombre2.droite := gd_flottant'access;
end;