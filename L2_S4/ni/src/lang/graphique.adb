with Ada.Numerics;
with Ada.Numerics.Generic_Elementary_Functions;
with Ada.Text_IO, Ada.Integer_Text_IO, Ada.Float_Text_IO;
with GraphSimple;

with Genres;
with Valeurs;
with Primitives;
with Vm;

package body Graphique is

   use Genres;
   use Valeurs;
   use Primitives;
   use Vm;

   package Math is new Ada.Numerics.Generic_Elementary_Functions(Float);

   WIDTH : constant Integer := 1000;
   HEIGHT : constant Integer := 1000;

   GRAPHIQUE_NON_INITIALISE: exception;
   GRAPHIQUE_DEJA_INITIALISE: exception;
   COULEUR_INVALIDE: exception;
   COORD_INVALIDE: exception;
   AVANCEMENT_TROP_PETIT: exception;

   subtype X_Coord is Natural range 0..WIDTH;
   subtype Y_Coord is Natural range 0..HEIGHT;

   initialise: Boolean := false;
   x: X_Coord;
   y: Y_Coord;
   angle: Float;
   bas: Boolean;

   coul: GraphSimple.Couleur := GraphSimple.Couleur'first;

   procedure verifie_initialise is
   begin
      if not initialise then
         raise GRAPHIQUE_NON_INITIALISE;
      end if;
   end;

   procedure couleur_debut is
   begin
      coul := GraphSimple.Couleur'val(
            GraphSimple.Couleur'pos(
               GraphSimple.Couleur'first
            ) + 1);
      empiler(unit);
   end;

   procedure couleur_suivante is
      coul_indice: Integer;
   begin
      coul_indice := GraphSimple.Couleur'pos(coul);
      coul_indice := coul_indice + 1;
      coul_indice := coul_indice mod GraphSimple.Couleur'pos(GraphSimple.Couleur'last);
      coul_indice := coul_indice + 1;
      coul := GraphSimple.Couleur'val(coul_indice);
      GraphSimple.changeCouleur(coul);
      empiler(unit);
   end;

   function degree_vers_radians(degree: Float) return Float is
   begin
      return degree * Ada.Numerics.PI / 180.0;
   end;

   procedure avancer is
      d: Float;
      x2: Integer;
      y2: Integer;
   begin
      verifie_initialise;

      d := depiler.ada_float(true);
      x2 := x + Integer(Math.cos(degree_vers_radians(angle)) * d);
      y2 := y + Integer(Math.sin(degree_vers_radians(angle)) * d);

      if not (x2 in X_Coord'range) or not (y2 in Y_Coord'range) then
         raise COORD_INVALIDE with ("(" & Integer'Image(x2) & ", " & Integer'Image(y2) & ")");
      end if;

      if x = x2 and y = y2 then
         raise AVANCEMENT_TROP_PETIT with ("Distance: " & Float'Image(d));
      end if;

      if bas then
         GraphSimple.ligne(x, y, x2, y2);
      end if;

      x := x2;
      y := y2;
      empiler(unit);
   end;

   procedure tourner_g is
   begin
      verifie_initialise;
      angle := angle - depiler.ada_float(true);
      empiler(unit);
   end;

   procedure tourner_d is
   begin
      verifie_initialise;
      angle := angle + depiler.ada_float(true);
      empiler(unit);
   end;

   procedure baisser is
   begin
      verifie_initialise;
      bas := true;
      empiler(unit);
   end;

   procedure lever is
   begin
      verifie_initialise;
      bas := false;
      empiler(unit);
   end;

   procedure ouvrir is
   begin
      if initialise then
         raise GRAPHIQUE_DEJA_INITIALISE;
      end if;

      GraphSimple.initialiser(WIDTH, HEIGHT);
      GraphSimple.changeCouleur(GraphSimple.Noir);
      GraphSimple.RectanglePlein(0, 0, WIDTH, HEIGHT);
      GraphSimple.changeCouleur(GraphSimple.Blanc);
      x := 0;
      y := 0;
      angle := 0.0;
      bas := false;
      initialise := true;
      empiler(unit);
   end;

   procedure debut_dessin is
   begin
      verifie_initialise;
      GraphSimple.DebutDessin;
      empiler(unit);
   end;

   procedure fin_dessin is
   begin
      verifie_initialise;
      GraphSimple.FinDessin;
      empiler(unit);
   end;

   procedure attendre is
   begin
      verifie_initialise;
      GraphSimple.attendreClic;
      empiler(unit);
   end;

   procedure clore is
   begin
      verifie_initialise;
      GraphSimple.clore;
      initialise := false;
      empiler(unit);
   end;

begin
   enregistrer(new String'("couleur_debut"),
      couleur_debut'access,
      retour => base_unit);
   enregistrer(new String'("couleur_suivante"),
      couleur_suivante'access,
      retour => base_unit);

   enregistrer(new String'("avancer"),
      avancer'access,
      arg => union_nombre,
      retour => base_unit);
   enregistrer(new String'("tourner_g"),
      tourner_g'access,
      arg => union_nombre,
      retour => base_unit);
   enregistrer(new String'("tourner_d"),
      tourner_d'access,
      arg => union_nombre,
      retour => base_unit);
   enregistrer(new String'("baisser"),
      baisser'access,
      retour => base_unit);
   enregistrer(new String'("lever"),
      lever'access,
      retour => base_unit);

   enregistrer(new String'("ouvrir"),
      ouvrir'access,
      retour => base_unit);
   enregistrer(new String'("debut_dessin"),
      debut_dessin'access,
      retour => base_unit);
   enregistrer(new String'("fin_dessin"),
      fin_dessin'access,
      retour => base_unit);
   enregistrer(new String'("attendre"),
      attendre'access,
      retour => base_unit);
   enregistrer(new String'("clore"),
      clore'access,
      retour => base_unit);
end;
