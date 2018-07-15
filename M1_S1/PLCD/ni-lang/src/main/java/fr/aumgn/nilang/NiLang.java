package fr.aumgn.nilang;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import fr.aumgn.nilang.ast.Ast;
import fr.aumgn.nilang.parser.NiLangAstParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NiLang {

    public static void main(String[] args) throws IOException {
        CharSource source = Files.asCharSource(new File(args[0]), StandardCharsets.UTF_8);
        NiLangAstParser parser = new NiLangAstParser();
        Ast ast = parser.parse(source);

        System.out.println("Ok !");
    }
}
