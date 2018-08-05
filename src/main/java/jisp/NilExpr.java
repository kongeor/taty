package jisp;

public class NilExpr extends Cons {

    public static final NilExpr NIL = new NilExpr();

    private NilExpr() {
        super(NIL); //TODO?
    }

    @Override
    public Object eval(Env env) {
        return this;
    }

    @Override
    public String toString() {
        return "nil";
    }
}
