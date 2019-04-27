package taty;

public class StringExpr implements TatyExpr {

    private final String val;

    public StringExpr(String val) {
        this.val = val;
    }

    public static StringExpr of(String val) {
        return new StringExpr(val);
    }

    @Override
    public String toString() {
        return val;
    }
}
