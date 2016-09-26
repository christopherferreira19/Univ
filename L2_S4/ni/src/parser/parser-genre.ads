with Genres;

package Parser.Genre is

   use Genres;

   function rec_genre return Genre_Decl;

private


   function rec_union return Genre_Decl;
   function rec_genre_terme return Genre_Decl;
   function rec_suite_union(g: Genre_Decl) return Genre_Decl;
   function rec_genre_atome return Genre_Decl;
   function rec_genre_fn return Genre_Decl;
   function rec_genre_fn_args return Genre_Decl_Tabs.Tab;
   procedure rec_genre_fn_suite_args(args: in out Genre_Decl_Tabs.TabCreation);
   function rec_genre_fn_retour return Genre_Decl;

end;