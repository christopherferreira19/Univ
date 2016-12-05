package fr.aumgn.whilelang.cfg;

import com.google.common.io.CharSink;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class Graphviz {

    public static void output(WhileCfg cfg, CharSink charSink) {
        try {
            doOutput(cfg, charSink);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void doOutput(WhileCfg cfg, CharSink charSink) throws IOException {
        Writer writer = charSink.openBufferedStream();

        writer.write("digraph finite_state_machine {\n");
        writer.write("   rankdir=TB;\n");
        writer.write("   ratio = auto\n");
        for (BasicBlock block : cfg.getBlocks()) {
            String blockString = "block" + block.getIndex();
            String blockLabel = block.toStringWithoutLabelAndJump().replaceAll("\n", "<br/>");
            writer.write("   " + blockString + "[ shape=rect label=<" + blockLabel + "> ]\n");
        }

        for (BasicBlock from : cfg.getBlocks()) {
            String fromString = "block" + from.getIndex();
            for (Map.Entry<String, BasicBlock> dest : from.getOutgoing().entrySet()) {
                String destString = "block" + dest.getValue().getIndex();
                writer.write("   " + fromString + " -> " + destString + " [ label=\"" + dest.getKey() + "\" ]\n");
            }
        }
        writer.write("}\n");

        writer.close();
    }
}
