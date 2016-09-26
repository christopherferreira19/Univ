with Ada.Text_IO, Ada.Integer_Text_IO, Ada.Float_Text_IO;

with Genres;
with Valeurs;
with Primitives;
with Vm;

package body Operations is

   use Genres;
   use Valeurs;
   use Primitives;
   use Vm;

   procedure non is
   begin
      case depiler.genre is
         when Vrai => empiler(faux);
         when Faux => empiler(vrai);
         when others => raise Genre_Invalide;
      end case;
   end;

   procedure moins_unaire is
   begin
      empiler(creer_entier(-depiler.ent));
   end;

   procedure moins_unaire_f is
   begin
      empiler(creer_flottant(-depiler.flot));
   end;

   procedure egale is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      case gauche.genre is
         when Unit | Vrai | Faux => empiler(booleen(gauche = droite));
         when Entier =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.ent = droite.ent));
               when Flottant => empiler(booleen(Float(gauche.ent) = droite.flot));
               when others => empiler(faux);
            end case;
         when Flottant =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.flot = Float(droite.ent)));
               when Flottant => empiler(booleen(gauche.flot = droite.flot));
               when others => empiler(faux);
            end case;
         when Chaine =>
            case droite.genre is
               when Chaine => empiler(booleen(gauche.chn.all = droite.chn.all));
               when others => empiler(faux);
            end case;
         when Cloture | Genres.Primitive =>
            empiler(booleen(gauche = droite));
      end case;
   end;

   procedure different is
   begin
      egale;
      non;
   end;

   procedure inferieur is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      case gauche.genre is
         when Entier =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.ent < droite.ent));
               when Flottant => empiler(booleen(Float(gauche.flot) < droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when Flottant =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.flot < Float(droite.ent)));
               when Flottant => empiler(booleen(gauche.flot < droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when others => raise Genre_Invalide;
      end case;
   end;

   procedure inferieur_egale is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      case gauche.genre is
         when Entier =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.ent <= droite.ent));
               when Flottant => empiler(booleen(Float(gauche.flot) <= droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when Flottant =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.flot <= Float(droite.ent)));
               when Flottant => empiler(booleen(gauche.flot <= droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when others => raise Genre_Invalide;
      end case;
   end;

   procedure superieur is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      case gauche.genre is
         when Entier =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.ent > droite.ent));
               when Flottant => empiler(booleen(Float(gauche.flot) > droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when Flottant =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.flot > Float(droite.ent)));
               when Flottant => empiler(booleen(gauche.flot > droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when others => raise Genre_Invalide;
      end case;
   end;

   procedure superieur_egale is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      case gauche.genre is
         when Entier =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.ent >= droite.ent));
               when Flottant => empiler(booleen(Float(gauche.flot) >= droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when Flottant =>
            case droite.genre is
               when Entier => empiler(booleen(gauche.flot >= Float(droite.ent)));
               when Flottant => empiler(booleen(gauche.flot >= droite.flot));
               when others => raise Genre_Invalide;
            end case;
         when others => raise Genre_Invalide;
      end case;
   end;

   procedure plus is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_entier(gauche.ent + droite.ent));
   end;

   procedure plus_f is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_flottant(gauche.flot + droite.flot));
   end;

   procedure moins is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_entier(gauche.ent - droite.ent));
   end;

   procedure moins_f is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_flottant(gauche.flot - droite.flot));
   end;

   procedure multiplier is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_entier(gauche.ent * droite.ent));
   end;

   procedure multiplier_f is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_flottant(gauche.flot * droite.flot));
   end;

   procedure diviser is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_entier(gauche.ent / droite.ent));
   end;

   procedure diviser_f is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_flottant(gauche.flot / droite.flot));
   end;

   procedure modulo is
      gauche, droite: Valeur;
   begin
      droite := depiler;
      gauche := depiler;
      empiler(creer_entier(gauche.ent mod droite.ent));
   end;

   procedure tronquer is
      val: Valeur;
   begin
      val := depiler;
      case val.genre is
         when Entier => empiler(val);
         when Flottant => empiler(creer_entier(
               Integer(Float'Floor(val.flot))));
         when others => raise Genre_Invalide;
      end case;
   end;

   procedure arrondir is
      val: Valeur;
   begin
      val := depiler;
      case val.genre is
         when Entier => empiler(val);
         when Flottant => empiler(creer_entier(
               Integer(Float'Rounding(val.flot))));
         when others => raise Genre_Invalide;
      end case;
   end;

   procedure flottant is
      val: Valeur;
   begin
      val := depiler;
      case val.genre is
         when Entier => empiler(creer_flottant(Float(val.ent)));
         when Flottant => empiler(val);
         when others => raise Genre_Invalide;
      end case;
   end;

begin
   enregistrer(new String'("non"),
         non'access,
         arg => union_booleen,
         retour => union_booleen);

   enregistrer(new String'("moins_unaire"),
         moins_unaire'access,
         arg => base_entier,
         retour => base_entier);
   enregistrer(new String'("moins_unaire_f"),
         moins_unaire_f'access,
         arg => base_flottant,
         retour => base_flottant);

   enregistrer(new String'("egale"),
         egale'access,
         args => (tous, tous),
         retour => union_booleen);
   enregistrer(new String'("different"),
         different'access,
         args => (tous, tous),
         retour => union_booleen);

   enregistrer(new String'("inferieur"),
         inferieur'access,
         args => (union_nombre, union_nombre),
         retour => union_booleen);
   enregistrer(new String'("inferieur_egale"),
         inferieur_egale'access,
         args => (union_nombre, union_nombre),
         retour => union_booleen);
   enregistrer(new String'("superieur"),
         superieur'access,
         args => (union_nombre, union_nombre),
         retour => union_booleen);
   enregistrer(new String'("superieur_egale"),
         superieur_egale'access,
         args => (union_nombre, union_nombre),
         retour => union_booleen);

   enregistrer(new String'("plus"),
         plus'access,
         args => (base_entier, base_entier),
         retour => base_entier);
   enregistrer(new String'("plus_f"),
         plus_f'access,
         args => (base_flottant, base_flottant),
         retour => base_flottant);
   enregistrer(new String'("moins"),
         moins'access,
         args => (base_entier, base_entier),
         retour => base_entier);
   enregistrer(new String'("moins_f"),
         moins_f'access,
         args => (base_flottant, base_flottant),
         retour => base_flottant);
   enregistrer(new String'("multiplier"),
         multiplier'access,
         args => (base_entier, base_entier),
         retour => base_entier);
   enregistrer(new String'("multiplier_f"),
         multiplier_f'access,
         args => (base_flottant, base_flottant),
         retour => base_flottant);
   enregistrer(new String'("diviser"),
         diviser'access,
         args => (base_entier, base_entier),
         retour => base_entier);
   enregistrer(new String'("diviser_f"),
         diviser_f'access,
         args => (base_flottant, base_flottant),
         retour => base_flottant);
   enregistrer(new String'("modulo"),
         modulo'access,
         args => (base_entier, base_entier),
         retour => base_entier);

   enregistrer(new String'("tronquer"),
         tronquer'access,
         arg => union_nombre,
         retour => base_entier);
   enregistrer(new String'("arrondir"),
         arrondir'access,
         arg => union_nombre,
         retour => base_entier);
   enregistrer(new String'("flottant"),
         flottant'access,
         arg => union_nombre,
         retour => base_flottant);

end;
