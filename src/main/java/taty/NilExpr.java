package taty;

public class NilExpr implements TatyExpr {

    public static final NilExpr NIL = new NilExpr();

    private NilExpr() {
    }

    @Override
    public String toString() {
        return "nil";
    }
}
