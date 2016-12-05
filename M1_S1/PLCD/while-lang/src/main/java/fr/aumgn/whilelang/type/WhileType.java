package fr.aumgn.whilelang.type;

public enum WhileType {

    INTEGER,

    BOOLEAN,

    UNIT,

    TOP,

    BOTTOM;

    public static WhileType ofString(String str) {
        if ("Boolean".equals(str)) {
            return WhileType.BOOLEAN;
        }
        else if ("Integer".equals(str)) {
            return WhileType.INTEGER;
        }
        else {
            throw new RuntimeException("Unknown WhileType '" + str + "'");
        }
    }
}
