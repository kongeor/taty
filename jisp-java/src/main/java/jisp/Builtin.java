package jisp;

public class Builtin {

    public static IFn PLUS = (env, args) -> {
        int sum = 0;
        Cons c = args;
        while (c != null) {
            sum += (Integer) c.car();
            c = (Cons) c.cdr();
        }
        return sum;
    };
}

