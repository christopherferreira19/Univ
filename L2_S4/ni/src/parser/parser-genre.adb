package body Parser.Genre is

   function rec_genre return Genre_Decl is
   begin
      return rec_union;
   end;

   function rec_union return Genre_Decl is
   begin
      return rec_suite_union(rec_genre_terme);
   end;

   function rec_genre_terme return Genre_Decl is
   begin
      return rec_genre_atome;
   end;

   function rec_suite_union(g: Genre_Decl) return Genre_Decl is
   begin
      if courant.nature = Pipe then
         avancer;
         return rec_suite_union(union(g, rec_genre_terme));
      else
         return g;
      end if;
   end;

   function rec_genre_atome return Genre_Decl is
      genre: Genre_Decl;
   begin
      if courant.nature = Fn_Type then
         return rec_genre_fn;
      elsif courant.nature = Par_O then
         avancer;
         genre := rec_genre;
         verifie_lexeme_avancer(Par_F);
         return genre;
      end if;

      verifie_lexeme(Idf_Maj);
      if courant.identifiant.all = "Tous" then
         avancer;
         return tous;
      elsif courant.identifiant.all = "Unit" then
         avancer;
         return base_unit;
      elsif courant.identifiant.all = "Vrai" then
         avancer;
         return base_vrai;
      elsif courant.identifiant.all = "Faux" then
         avancer;
         return base_faux;
      elsif courant.identifiant.all = "Booleen" then
         avancer;
         return union_booleen;
      elsif courant.identifiant.all = "Entier" then
         avancer;
         return base_entier;
      elsif courant.identifiant.all = "Flottant" then
         avancer;
         return base_flottant;
      elsif courant.identifiant.all = "Nombre" then
         avancer;
         return union_nombre;
      elsif courant.identifiant.all = "Chaine" then
         avancer;
         return base_chaine;
      else
         traiter_erreur("Type Invalide");
         return null;
      end if;
   end;

   function rec_genre_fn return Genre_Decl is
      args: Genre_Decl_Tabs.Tab;
   begin
      avancer;
      args := rec_genre_fn_args;
      return fonction(args, rec_genre_fn_retour);
   end;

   function rec_genre_fn_args return Genre_Decl_Tabs.Tab is
      args: Genre_Decl_Tabs.TabCreation;
   begin
      if courant.nature /= Par_O then
         return Genre_Decl_Tabs.creer_vide;
      end if;
      avancer;

      if courant.nature = Par_F then
         avancer;
         return Genre_Decl_Tabs.creer_vide;
      end if;

      args.ajouter(rec_genre);
      rec_genre_fn_suite_args(args);

      verifie_lexeme_avancer(Par_F);
      return args.creer;
   end;

   procedure rec_genre_fn_suite_args(args: in out Genre_Decl_Tabs.TabCreation) is
   begin
      if courant.nature = Virgule then
         avancer;
         args.ajouter(rec_genre);
         rec_genre_fn_suite_args(args);
      end if;
   end;

   function rec_genre_fn_retour return Genre_Decl is
   begin
      if courant.nature = Appli then
         avancer;
         return rec_genre;
      else
         return base_unit;
      end if;
   end;

end;
