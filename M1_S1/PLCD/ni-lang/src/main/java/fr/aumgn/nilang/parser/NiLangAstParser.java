package fr.aumgn.nilang.parser;

import com.google.common.io.CharSource;
import fr.aumgn.nilang.ast.Ast;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class NiLangAstParser {

    public Ast parse(CharSource source) throws IOException {
        NiLangLexer lexer = new NiLangLexer(new ANTLRInputStream(source.openStream()));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        NiLangParser parser = new NiLangParser(tokenStream);
        ParseTreeToAst parseTreeToAst = new ParseTreeToAst();
        return parseTreeToAst.visit(parser.ni());
    }
}
