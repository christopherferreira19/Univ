with Ada.Text_IO;

package body Asts is

   use Ada.Text_IO;

   function creer_litteral(pos: Source_Pos; val: Valeur) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Litteral);
      arbre.pos := pos;
      arbre.val := val;
      return arbre;
   end;

   function creer_identifiant(pos: Source_Pos; nom: PString) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Identifiant);
      arbre.pos := pos;
      arbre.idf_nom := nom;
      return arbre;
   end;

   function creer_fonction(pos: Source_Pos; genre: Genre_Decl; args: PString_Tabs.Tab; corps: Ast_Tabs.Tab) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Fonction);
      arbre.pos := pos;
      arbre.fn_genre := genre;
      arbre.fn_args := args;
      arbre.fn_corps := corps;
      arbre.fn_nom := null;
      return arbre;
   end;

   function creer_operation(pos: Source_Pos; op: Op_Binaire; gauche, droite: Ast) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Operation);
      arbre.pos := pos;
      arbre.bin_operateur := op;
      arbre.bin_gauche := gauche;
      arbre.bin_droite := droite;
      return arbre;
   end;

   function creer_unaire(pos: Source_Pos; unr: Op_Unaire; opde: Ast) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Unaire);
      arbre.pos := pos;
      arbre.unr_operateur := unr;
      arbre.unr_operande := opde;
      return arbre;
   end;

   function creer_appel(pos: Source_Pos; fn: Ast; args: Ast_Tabs.Tab) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Appel);
      arbre.pos := pos;
      arbre.app_fn := fn;
      arbre.app_args := args;
      return arbre;
   end;

   function creer_affectation(pos: Source_Pos; idf: PString; expr: Ast) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Affectation);
      arbre.pos := pos;
      arbre.var_idf := idf;
      arbre.var_expr := expr;
      return arbre;
   end;

   function creer_op_affectation(pos: Source_Pos; idf: PString; op: Op_Binaire; expr: Ast) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Affectation);
      arbre.pos := pos;
      arbre.var_idf := idf;
      arbre.var_expr := creer_operation(pos, op,
            creer_identifiant(pos, idf),
            expr);
      return arbre;
   end;

   function creer_retour(pos: Source_Pos; expr: Ast) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Retour);
      arbre.pos := pos;
      arbre.ret_expr := expr;
      return arbre;
   end;

   function creer_si(pos: Source_Pos; cond: Ast; si_corps: Ast_Tabs.Tab) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Si);
      arbre.pos := pos;
      arbre.si_cond := cond;
      arbre.si_corps := si_corps;
      arbre.sinon_corps := Ast_Tabs.creer_vide;
      return arbre;
   end;

   procedure maj_sinon(arbre: Ast; sinon_corps: Ast_Tabs.Tab) is
   begin
      if arbre.sinon_corps.taille = 0 then
         arbre.sinon_corps := sinon_corps;
      else
         maj_sinon(arbre.sinon_corps.element(1), sinon_corps);
      end if;
   end;

   function creer_tq(pos: Source_Pos; cond: Ast; corps: Ast_Tabs.Tab) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Tq);
      arbre.pos := pos;
      arbre.tq_cond := cond;
      arbre.tq_corps := corps;
      return arbre;
   end;

   function creer_boucle_tq(pos: Source_Pos; cond: Ast; corps: Ast_Tabs.Tab) return Ast is
      arbre: Ast;
   begin
      arbre := new Ast_Record(Boucle_Tq);
      arbre.pos := pos;
      arbre.tq_cond := cond;
      arbre.tq_corps := corps;
      return arbre;
   end;

   procedure afficher(args: PString_Tabs.Tab; args_g: Genre_Decl_Tabs.Tab);

   procedure afficher(arbres: Ast_Tabs.Tab; indent: String);

   procedure afficher(arbre: Ast_Record; indent: String; expr: Boolean) is
      use type Ast_Tabs.Tab;
   begin
      case arbre.nature is
         when Litteral =>
            arbre.val.afficher_code;
         when Identifiant =>
            put(arbre.idf_nom);
         when Fonction =>
            put("fn");
            if arbre.fn_genre.args.taille > 0 then
               put("(");
               afficher(arbre.fn_args, arbre.fn_genre.args);
               put(")");
            end if;
            if arbre.fn_genre.retour /= base_unit then
               put(" -> ");
               arbre.fn_genre.retour.afficher;
            end if;
            put(" {");
            new_line;
            afficher(arbre.fn_corps, indent & "   ");
            put(indent);
            put("}");
         when Operation =>
            put("(");
            afficher(arbre.bin_gauche.all, indent, true);
            case arbre.bin_operateur is
               when Et => put(" et ");
               when Ou => put(" ou ");
               when Egale => put(" == ");
               when Different => put(" <> ");
               when Inf => put(" < ");
               when Inf_Egale => put(" <= ");
               when Sup => put(" > ");
               when Sup_Egale => put(" >= ");
               when Plus => put(" + ");
               when Moins => put(" - ");
               when Mul => put(" * ");
               when Modulo => put(" % ");
               when Div => put(" / ");
            end case;
            afficher(arbre.bin_droite.all, indent, true);
            put(")");
         when Unaire =>
            put("(");
            case arbre.unr_operateur is
               when Plus => put("+");
               when Moins => put("-");
            end case;
            afficher(arbre.unr_operande.all, indent, true);
            put(")");
         when Appel =>
            if not expr then
               put(indent);
            end if;
            afficher(arbre.app_fn.all, indent, true);
            put("(");
            if arbre.app_args.taille > 0 then
               afficher(arbre.app_args.element(1).all, indent, true);
               for i in 2..arbre.app_args.taille loop
                  put(", ");
                  afficher(arbre.app_args.element(i).all, indent, true);
               end loop;
            end if;
            put(")");
         when Affectation =>
            put(indent);
            put(arbre.var_idf);
            put(" = ");
            afficher(arbre.var_expr.all, indent, true);
         when Retour =>
            put(indent);
            put("retour ");
            afficher(arbre.ret_expr.all, indent, true);
         when Si =>
            put(indent);
            put("si ");
            afficher(arbre.si_cond.all, indent, true);
            put(" {");
            new_line;
            afficher(arbre.si_corps, indent & "   ");
            put(indent);
            put("}");
            if arbre.sinon_corps.taille > 0 then
               new_line;
               put(indent);
               put("sinon {");
               new_line;
               afficher(arbre.sinon_corps, indent & "   ");
               put(indent);
               put("}");
            end if;
         when Tq =>
            put(indent);
            put("tq ");
            afficher(arbre.tq_cond.all, indent, true);
            put_line(" {");
            afficher(arbre.tq_corps, indent & "   ");
            put(indent);
            put("}");
         when Boucle_Tq =>
            put(indent);
            put_line("boucle {");
            afficher(arbre.tq_corps, indent & "   ");
            put(indent);
            put("} tq ");
            afficher(arbre.tq_cond.all, indent, true);
      end case;
   end;

   procedure afficher(args: PString_Tabs.Tab; args_g: Genre_Decl_Tabs.Tab) is
   begin
      if args.taille > 0 then
         put(args.element(1));
         put(": ");
         args_g.element(1).afficher;
         for i in 2..args.taille loop
            put(", ");
            put(args.element(i));
            put(": ");
            args_g.element(i).afficher;
         end loop;
      end if;
   end;

   procedure afficher(arbres: Ast_Tabs.Tab; indent: String) is
   begin
      for i in 1..arbres.taille loop
         afficher(arbres.element(i).all, indent, false);
         new_line;
      end loop;
   end;

   procedure afficher(arbre: Ast_Record) is
   begin
      afficher(arbre, "", false);
   end;

   procedure afficher(arbres: Ast_Tabs.Tab) is
   begin
      afficher(arbres, "");
   end;

end;
