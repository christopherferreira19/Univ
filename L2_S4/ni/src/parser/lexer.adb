with Ada.Text_IO, Ada.Integer_Text_IO, Ada.Float_Text_IO;

with Lecture;

package body Lexer is

   lex: Lexeme;

   -- Gestion des fins de lignes
   --
   -- Dans certains contextes s'embêter avec les fin de lignes est inutile
   -- Typiquement pour toutes expressions délimitées :
   --  - Par_O quelque_chose Par_F
   --  - SI quelque_chose Accol_O
   --  - Fn signature_fonction Accol_O
   -- Dans ces règles, dès lors que l'on a recontré le
   -- premier lexème, on sait que on aura pas terminé l'instruction
   -- en cours tant que l'on a aura pas rencontré le lexème de fin
   -- Les fins de lignes sont donc non significatif dans ces contextes,
   -- il est donc possible d'ignorer tout simplement leur présence en
   -- tant que lexème
   -- Pour gérer cela et eviter pas mal de code redondant dans le parser,
   -- on ignore les fins de lignes dans la fonction avancer du parser à
   -- l'aide d'un flag que le parser peut modifier de manière appropriée.
   --
   -- Seul problème, comme le langage supporte les fonctions anonymes on peut
   -- avoir ce cas : 
   --    (fn(args..) { <- expression parenthesé donc fdl non significative
   --       instr      <- suite d'instructions donc fdl significative
   --    })
   -- Pour pouvoir gérer ce cas on mémorise l'état d'ignorement ou non des
   -- fin de lignes sous forme de pile.
   -- (100 comme maximum de taille de pile semble un niveau largement
   -- suffisant puisqu'au final la taille dépend du niveau d'imbrication
   -- du code)
   type Fin_Lignes_Ary is array(0..100) of Boolean;
   ignorer_fdl: Fin_Lignes_Ary;
   ignorer_fdl_indice: Natural range 0..100;

   procedure demarrer(nom_fichier : String) is
   begin
      Lecture.demarrer(nom_fichier);
      ignorer_fdl_indice := 0;
      ignorer_fdl(0) := true;
      avancer;
   end;

   function courant return Lexeme is
   begin
      return lex;
   end;

   procedure arreter is
   begin
      Lecture.arreter;
   end;

   procedure empiler_ignorer_fdl(ignorer: Boolean) is
   begin
      ignorer_fdl_indice := ignorer_fdl_indice + 1;
      ignorer_fdl(ignorer_fdl_indice) := ignorer;
   end;

   procedure depiler_ignorer_fdl is
   begin
      ignorer_fdl_indice := ignorer_fdl_indice - 1;
   end;

   procedure reconnaitre_lexeme;

   procedure avancer is 
   begin
      loop
         reconnaitre_lexeme;
      exit when lex.nature /= Fin_Ligne;
      exit when not ignorer_fdl(ignorer_fdl_indice);
      end loop;
   end;

   procedure reconnaitre_lexeme is

      procedure reconnaitre_idf is
      begin
         if lex.string.all = "unit" then
            lex.nature := Unit;
         elsif lex.string.all = "vrai" then
            lex.nature := Vrai;
         elsif lex.string.all = "faux" then
            lex.nature := Faux;
         elsif lex.string.all = "et" then
            lex.nature := Et;
         elsif lex.string.all = "ou" then
            lex.nature := Ou;
         elsif lex.string.all = "fn" then
            lex.nature := Fn;
         elsif lex.string.all = "Fn" then
            lex.nature := Fn_Type;
         elsif lex.string.all = "retour" then
            lex.nature := Retour;
         elsif lex.string.all = "si" then
            lex.nature := Si;
         elsif lex.string.all = "sinon" then
            lex.nature := Sinon;
         elsif lex.string.all = "tq" then
            lex.nature := Tq;
         elsif lex.string.all = "boucle" then
            lex.nature := Boucle;
         end if;
      end;

      type Etat_Automate is (
            Init,
            Entier, Decimal, Chaine, Identifiant,
            Plus, Moins, Mul, Div, Modulo,
            Aff, Inf, Sup,
            Fin_Ligne,
            Fin
      );

      etat: Etat_Automate;

      -- À chaque transition de l'automate peut être associée
      -- une suite d'actions parmis:
      --   - Changer d'état ou non
      --   - Changer la nature du lexème ou non
      --   - Avancer ou non
      --   - Integrer le caractère à la chaine de caractère
      --        du lexème ou non

      -- Les 4 procédures suivantes representes les 4 cas
      -- largement majoritaires de suite d'actions associée aux
      -- transitions de l'automate, permettant de rendre le code
      -- de la logique de l'automate plus concis et lisible.

      procedure boucler is
      begin
         lex.string := new String'(lex.string.all & Lecture.courant);
         Lecture.avancer;
      end;

      procedure transition(nature: Nature_Lexeme; suivant: Etat_Automate) is
      begin
         boucler;
         lex.nature := nature;
         etat := suivant;
      end;

      procedure finir_sans is
      begin
         etat := Fin;
      end;

      procedure finir_avec(nature: Nature_Lexeme) is
      begin
         transition(nature, Fin);
      end;

   begin
      if Lecture.est_fin then
         lex.nature := Fin_Sequence;
         return;
      end if;

      etat := Init;

      loop 
         case etat is 
            when Init =>
               lex.nature := Erreur;
               lex.string := new String'("");
               lex.pos_debut := Lecture.pos;

               case Lecture.courant is
                  when '('      => finir_avec(Par_O);
                  when ')'      => finir_avec(Par_F);
                  when '{'      => finir_avec(Accol_O);
                  when '}'      => finir_avec(Accol_F);
                  when ','      => finir_avec(Virgule);

                  when ':'      => finir_avec(Deux_Points);
                  when '|'      => finir_avec(Pipe);

                  when '+'      => transition(Plus, Plus);
                  when '-'      => transition(Moins, Moins);
                  when '*'      => transition(Mul, Mul);
                  when '/'      => transition(Div, Div);
                  when '%'      => transition(Modulo, Modulo);
                  when '='      => transition(Aff, Aff);
                  when '<'      => transition(Inf, Inf);
                  when '>'      => transition(Sup, Sup);

                  when '"'      => transition(Chaine, Chaine);
                  when '0'..'9' => transition(Entier, Entier);
                  when '.'      => transition(Flottant, Decimal);
                  when 'a'..'z' => transition(Idf_Min, Identifiant);
                  when 'A'..'Z' => transition(Idf_Maj, Identifiant);

                  when '#'      => finir_avec(Comment);

                  when Ascii.LF => transition(Fin_Ligne, Fin_Ligne);
                  when Ascii.CR => transition(Fin_Ligne, Fin_Ligne);

                  when ' '      => Lecture.avancer;

                  when others   => finir_avec(Erreur);

               end case;
            when Entier =>
               case Lecture.courant is
                  when '0'..'9' => boucler;
                  when '_'      => boucler;
                  when '.'      => transition(Flottant, Decimal);
                  when others   => finir_sans;
               end case;
            when Decimal =>
               case Lecture.courant is
                  when '0'..'9' => boucler;
                  when '_'      => boucler;
                  when others   => finir_sans;
               end case;
            when Chaine =>
               if Lecture.est_fin then
                  finir_avec(Chaine_Non_Fermee);
               else
                  case Lecture.courant is
                     when '"'      => boucler; finir_sans;
                     when others   => boucler;
                  end case;
               end if;
            when Identifiant =>
               case Lecture.courant is
                  when 'a'..'z' => boucler;
                  when 'A'..'Z' => boucler;
                  when '0'..'9' => boucler;
                  when '_'      => boucler;
                  when others   =>
                     finir_sans;
                     reconnaitre_idf;
               end case;
            when Plus =>
               case Lecture.courant is
                  when '='    => finir_avec(Aff_Plus);
                  when '+'    => finir_avec(Incr);
                  when others => finir_sans;
               end case;
            when Moins =>
               case Lecture.courant is
                  when '>'    => finir_avec(Appli);
                  when '='    => finir_avec(Aff_Moins);
                  when '-'    => finir_avec(Decr);
                  when others => finir_sans;
               end case;
            when Mul =>
               case Lecture.courant is
                  when '='    => finir_avec(Aff_Mul);
                  when others => finir_sans;
               end case;
            when Div =>
               case Lecture.courant is
                  when '='    => finir_avec(Aff_Div);
                  when others => finir_sans;
               end case;
            when Modulo =>
               case Lecture.courant is
                  when '='    => finir_avec(Aff_Modulo);
                  when others => finir_sans;
               end case;
            when Aff =>
               case Lecture.courant is
                  when '='    => finir_avec(Egale);
                  when others => finir_sans;
               end case;
            when Inf =>
               case Lecture.courant is
                  when '>'    => finir_avec(Different);
                  when '='    => finir_avec(Inf_Egale);
                  when others => finir_sans;
               end case;
            when Sup =>
               case Lecture.courant is
                  when '='    => finir_avec(Sup_egale);
                  when others => finir_sans;
               end case;
            when Fin_Ligne =>
               case Lecture.courant is
                  when Ascii.LF => boucler;
                  when Ascii.CR => boucler;
                  when others => finir_sans;
               end case;
            when Fin => null;
         end case;
      exit when etat = Fin;
      end loop;
   end;

   function nature(lex: Lexeme) return Nature_Lexeme is
   begin
      return lex.nature;
   end;

   function pos_debut(lex: Lexeme) return Source_Pos is
   begin
      return lex.pos_debut;
   end;

   function chaine(lex: Lexeme) return PString is
   begin
      return new String'(
         lex.string.all((lex.string.all'first + 1)..(lex.string.all'last - 1))
      );
   end;

   function identifiant(lex: Lexeme) return PString is
   begin
      return new String'(lex.string.all);
   end;

   function chiffre(char: Character) return Natural is
   begin
      return Character'pos(char) - Character'pos('0');
   end;

   function entier(lex: Lexeme) return Integer is
      ent: Integer;
   begin
      ent := 0;
      for i in lex.string.all'range loop
         if lex.string.all(i) /= '_' then
            ent := ent * 10;
            ent := ent + chiffre(lex.string.all(i));
         end if;
      end loop;
      return ent;
   end;

   function flottant(lex: Lexeme) return Float is
      flot: Float;
      decimal: Float;
   begin
      flot := 0.0;
      decimal := 0.0;
      for i in lex.string.all'range loop
         if lex.string.all(i) = '/' then
            null;
         elsif lex.string.all(i) = '.' then
            decimal := 10.0;
         elsif decimal > 1.0 then
            flot := flot + Float(chiffre(lex.string.all(i))) / decimal;
            decimal := decimal * 10.0;
         else
            flot := flot * 10.0;
            flot := flot + Float(chiffre(lex.string.all(i)));
         end if;
      end loop;
      return flot;
   end;

   procedure afficher(l : Lexeme) is
      use Ada.Text_IO, Ada.Integer_Text_IO, Ada.Float_Text_IO;
      TAB_NATURE : constant Count := col;
      TAB_LIGNE_COLONNE : constant Count := TAB_NATURE + 17;
      TAB_CHAINE : constant Count := TAB_LIGNE_COLONNE + 10;
      TAB_DESCRIPTION : constant Count := TAB_CHAINE + 10;
   begin
      put(Nature_Lexeme'image(l.nature));
      case l.nature is
         when FIN_SEQUENCE =>
            null;
         when others =>
            if col < TAB_LIGNE_COLONNE then
               set_col(TAB_LIGNE_COLONNE);
            else
               set_col(col + 1);
            end if;
            put(l.pos_debut.ligne, 3);
            put(':');
            put(l.pos_debut.colonne, 0);
            case l.nature is
               when FIN_LIGNE =>
                  null;
               when others =>
                  if col < TAB_CHAINE then
                     set_col(TAB_CHAINE);
                  else
                     set_col(col + 1);
                  end if;
                  put(l.string.all);
                  if col < TAB_DESCRIPTION then
                     set_col(TAB_DESCRIPTION);
                  else
                     set_col(col + 1);
                  end if;
            end case;
      end case;
   end;

end;
