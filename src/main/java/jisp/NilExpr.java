package jisp;

public class NilExpr extends JispExpr {

    public static final NilExpr NIL = new NilExpr();

    private NilExpr() {}

    @Override
    public Object eval(Env env) {
        return this;
    }

    @Override
    public String toString() {
        return "nil";
    }
}
