package fr.aumgn.whilelang.asm;

import java.io.IOException;

public class Compile {

    public static void compile(String basename) {
        try {
            Process process = new ProcessBuilder("gcc", basename + ".S", "-o", basename)
                    .inheritIO()
                    .start();
            process.waitFor();
        }
        catch (IOException | InterruptedException exc) {
            throw new RuntimeException(exc);
        }
    }
}
