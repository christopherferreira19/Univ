package fr.aumgn.whilelang.ast;

import fr.aumgn.whilelang.utils.Counter;

public class BlockId {

    private final int[] counts;
    private final int to;

    public BlockId(BlockId parentId, Counter counter) {
        if (parentId == null) {
            this.counts = new int[0];
            this.to = 0;
        }
        else {
            this.to = parentId.counts.length + 1;
            this.counts = new int[to];
            System.arraycopy(parentId.counts, 0, this.counts, 0, parentId.counts.length);
            this.counts[to - 1] = counter.next();
        }
    }

    public BlockId(BlockId childId) {
        this.counts = childId.counts;
        this.to = childId.to - 1;
    }

    public boolean isEmpty() {
        return counts.length == 0;
    }

    public BlockId parent() {
        if (to == 0) {
            return null;
        }

        return new BlockId(this);
    }

    @Override
    public int hashCode() {
        if (counts == null)
            return 0;

        int result = 1;
        for (int i = 0; i < to; i++) {
            result = 31 * result + counts[i];
        }

        return result;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof BlockId)) {
            return false;
        }

        BlockId that = (BlockId) object;
        if (this.to != that.to) {
            return false;
        }

        if (this.counts == that.counts) {
            return true;
        }

        for (int i = 0; i < this.to; i++) {
            if (this.counts[i] != that.counts[i]) {
                return false;
            }
        }

        return true;
    }

    public void asString(StringBuilder builder) {
        if (this.to > 0) {
            builder.append(this.counts[0]);
            for (int i = 1; i < this.to; i++) {
                builder.append('.');
                builder.append(this.counts[i]);
            }
        }
    }
}
