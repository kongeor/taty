package taty;

public class NumberExpr implements TatyExpr {

    private final long val;

    public NumberExpr(long val) {
        this.val = val;
    }

    public static NumberExpr of(long val) {
        return new NumberExpr(val);
    }

    public long longVal() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberExpr numberExp = (NumberExpr) o;
        return val == numberExp.val;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
