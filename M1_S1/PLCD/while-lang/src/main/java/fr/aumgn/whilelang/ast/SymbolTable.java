package fr.aumgn.whilelang.ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SymbolTable<E> {

    final Map<SymbolKey, E> map;

    public SymbolTable() {
        this.map = new HashMap<>();
    }

    public void insert(BlockId blockId, String name, E type) {
        map.put(new SymbolKey(blockId, name), type);
    }

    BlockId findBlockId(BlockId blockId, String name) {
        SymbolKey key = new SymbolKey(blockId, name);

        E value = map.get(key);
        while (value == null && key.blockId != null) {
            key.blockId = key.blockId.parent();
            value = map.get(key);
        }

        if (value == null) {
            throw new RuntimeException("Variable " + name + " not found");
        }

        return key.blockId;
    }

    public E lookUp(BlockId blockId, String name) {
        SymbolKey key = new SymbolKey(blockId, name);
        E value = map.get(key);

        if (value == null) {
            throw new RuntimeException("Variable " + name + " not found");
        }

        return value;
    }

    private static class SymbolKey {
        private BlockId blockId;
        private String name;

        public SymbolKey() {
            this(null, null);
        }

        public SymbolKey(BlockId blockId, String name) {
            this.blockId = blockId;
            this.name = name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(blockId, name);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof SymbolKey)) {
                return false;
            }

            SymbolKey other = (SymbolKey) object;
            return blockId.equals(other.blockId) && name.equals(other.name);
        }
    }
}
