package jisp;

public class SymbExp extends JispExp {

    private final String symb;

    public SymbExp(String symb) {
        this.symb = symb;
    }

    public static SymbExp SymbExp_(String symb) {
        return new SymbExp(symb);
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
