package fr.aumgn.whilelang.parser;

import fr.aumgn.whilelang.ast.*;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class WhileParserToAst {

    public Ast parse(String filename) throws IOException {
        WhileLangLexer lexer = new WhileLangLexer(new ANTLRFileStream(filename));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        WhileLangParser parser = new WhileLangParser(tokenStream);
        WhileParseTreeToAst parseTreeToAst = new WhileParseTreeToAst();
        return parseTreeToAst.visit(parser.program());
    }
}
