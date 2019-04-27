package taty;

import java.util.Objects;

public class SymbExpr implements TatyExpr {

    private final String symb;

    private SymbExpr(String symb) {
        this.symb = symb;
    }

    // TODO intern
    public static SymbExpr of(String symb) {
        return new SymbExpr(symb);
    }

    public String name() {
        return symb;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof SymbExpr)) {
            return false;
        }
        return symb.equals(((SymbExpr)o).symb);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symb);
    }

    @Override
    public String toString() {
        return symb;
    }
}
