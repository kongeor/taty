package jisp;

public class NumberExp extends JispExp {

    private final int val;

    public NumberExp(int val) {
        this.val = val;
    }

    @Override
    public Object eval(Env env) {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberExp numberExp = (NumberExp) o;
        return val == numberExp.val;
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
