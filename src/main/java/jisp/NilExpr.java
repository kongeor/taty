package jisp;

public class NilExpr extends Cons {

    public static final NilExpr NIL = new NilExpr();

    private NilExpr() {
        super(NIL); //TODO?
    }

    @Override
    public Object car() {
        return NIL;
    }

    @Override
    public Object cdr() {
        return NIL;
    }

    @Override
    public String toString() {
        return "nil";
    }
}
