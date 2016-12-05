package fr.aumgn.whilelang;

import com.google.common.io.Files;
import fr.aumgn.whilelang.asm.Asm;
import fr.aumgn.whilelang.asm.Compile;
import fr.aumgn.whilelang.ast.Ast;
import fr.aumgn.whilelang.cfg.Graphviz;
import fr.aumgn.whilelang.cfg.IntermediateToCfg;
import fr.aumgn.whilelang.cfg.WhileCfg;
import fr.aumgn.whilelang.ir.WhileIr;
import fr.aumgn.whilelang.parser.WhileParserToAst;
import fr.aumgn.whilelang.type.WhileTypeChecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WhileLang {

    public static void main(String[] args) throws IOException {
        run("examples/test0");
        //run("examples/test1");
        run("examples/test2");
        //run("examples/test3");
        //run("examples/test4");
        //run("examples/test5");
    }

    private static void run(String basename) throws IOException {
        System.out.println("# File " + basename);
        WhileParserToAst parser = new WhileParserToAst();
        Ast ast = parser.parse(basename + ".wl");

        WhileTypeChecker typeChecker = new WhileTypeChecker();
        typeChecker.visit(ast);

        WhileIr intermediate = WhileIr.of(ast);

        WhileCfg cfg = IntermediateToCfg.create(intermediate);
        Graphviz.output(cfg, Files.asCharSink(new File(basename + ".dot"), StandardCharsets.UTF_8));

        Asm.generate(intermediate, Files.asCharSink(new File(basename + ".S"), StandardCharsets.UTF_8));

        Compile.compile(basename);
    }
}

/*
1 ===========
LIT b 4
LIT a 5
LIT b 6
LT %1 a b
JIF l1: %1
=============

2 ===========
LIT 2.c 3
ADD a a 2.c
LIT %3 2
MUL %2 %3 2.c
SUB b b %2
JMP l2:
=============

3 ===========
l1:
LIT 1.c 1
SUB a a 1.c
ADD b b 1.c
=============

4 ===========
l2:
JMP l3:
=============

5 ===========
l4:
LIT %4 2
ADD b b %4
LIT %5 1
SUB a a %5
=============

6 ===========
l3:
LIT %7 0
GT %6 a %7
JIF l4: %6
=============

7 ===========
LIT a 1
=============
 */