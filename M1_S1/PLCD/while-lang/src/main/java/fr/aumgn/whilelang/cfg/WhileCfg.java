package fr.aumgn.whilelang.cfg;

import java.util.List;

public class WhileCfg {

    private final List<BasicBlock> blocks;

    public WhileCfg(List<BasicBlock> blocks) {
        this.blocks = blocks;
    }

    public List<BasicBlock> getBlocks() {
        return blocks;
    }
}
