package jisp;

public class SymbExp extends JispExp {

    private final String symb;
    private final boolean isRest;

    private SymbExp(String symb, boolean isRest) {
        this.symb = symb;
        this.isRest = isRest;
    }

    public static SymbExp SymbExp_(String symb) {
        return new SymbExp(symb, false);
    }

    public static SymbExp SymbExp_(String symb, boolean isRest) {
        return new SymbExp(symb, isRest);
    }

    public boolean isRest() {
        return isRest;
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
        if (!(o instanceof SymbExp)) {
            return false;
        }
        return symb.equals(((SymbExp)o).symb);
    }

    @Override
    public String toString() {
        return "'" + symb;
    }
}
