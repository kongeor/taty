package jisp;

public class NumberExp extends JispExp {

    private final int val;

    public NumberExp(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "<NumberExp: " + val + ">";
    }
}
