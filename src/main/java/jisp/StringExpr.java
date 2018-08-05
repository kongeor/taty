package jisp;

public class StringExpr extends JispExpr {

    private final String val;

    public StringExpr(String val) {
        this.val = val;
    }

    public static StringExpr StringExp_(String val) {
        return new StringExpr(val);
    }

    @Override
    public Object eval(Env env) {
        return this;
    }

    @Override
    public String toString() {
        return val;
    }
}
