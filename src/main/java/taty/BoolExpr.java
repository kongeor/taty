package taty;

public class BoolExpr implements TatyExpr {

    public static final BoolExpr T = new BoolExpr(true);
    public static final BoolExpr F = new BoolExpr(false);

    private final Boolean val;

    private BoolExpr(Boolean val) {
        this.val = val;
    }

    public static BoolExpr BoolExpr_(Boolean val) {
        return val ? T : F;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
