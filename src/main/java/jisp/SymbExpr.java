package jisp;

public class SymbExpr extends JispExpr {

    private final String symb;

    private SymbExpr(String symb) {
        this.symb = symb;
    }

    public static SymbExpr SymbExp_(String symb) {
        return new SymbExpr(symb);
    }

    public String name() {
        return symb;
    }

    @Override
    public Object eval(Env env) {
        return env.lookup(this);
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
    public String toString() {
        return symb;
    }
}
