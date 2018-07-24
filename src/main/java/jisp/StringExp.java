package jisp;

public class StringExp extends JispExp {

    private final String val;

    public StringExp(String val) {
        this.val = val;
    }

    public static StringExp StringExp_(String val) {
        return new StringExp(val);
    }

    @Override
    public Object eval(Env env) {
        return val;
    }
}
