package jisp;

public class BoolExpr extends JispExpr {

    public static final BoolExpr T = new BoolExpr(true);
    public static final BoolExpr F = new BoolExpr(false);

    private final Boolean val;

    private BoolExpr(Boolean val) {
        this.val = val;
    }

    @Override
    public Object eval(Env env) {
        return this;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
