package jisp;

public class SymbExp extends JispExp {

    private final String symb;


    public SymbExp(String symb) {
        this.symb = symb;
    }

    @Override
    public String toString() {
        return "<SymbExp: " + symb + ">";
    }
}
