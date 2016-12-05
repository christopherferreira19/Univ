package fr.aumgn.whilelang.ir;

import fr.aumgn.whilelang.ast.BooleanLiteral;

public interface IrLiteralValue {

    IrLiteralValue ZERO = new Numeral(0);

    void asString(StringBuilder builder);

    int intValue();

    enum Boolean implements IrLiteralValue {
        TRUE,
        FALSE;

        public int intValue() {
            return this == TRUE ? 1 : 0;
        }

        @Override
        public void asString(StringBuilder builder) {
            builder.append(name());
        }

        public static IrLiteralValue of(BooleanLiteral value) {
            return value == BooleanLiteral.TRUE ? TRUE : FALSE;
        }
    }

    class Numeral implements IrLiteralValue {
        private final int value;

        public Numeral(int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }

        @Override
        public void asString(StringBuilder builder) {
            builder.append(value);
        }
    }
}
