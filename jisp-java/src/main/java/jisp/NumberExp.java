package jisp;

import java.util.Objects;

public class NumberExp extends JispExp {

    private final int val;

    public NumberExp(int val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberExp numberExp = (NumberExp) o;
        return val == numberExp.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
