package jisp;

public abstract class Builtin implements IFn {

    private final String name;

    public Builtin(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "<BuiltinFn:" + name + ">";
    }

    public static Builtin PLUS = new Builtin("+") {

        @Override
        public Object apply(Env env, Cons args) {
            int sum = 0;
            Cons c = args;
            while (c != null) {
                sum += (Integer) c.car();
                c = (Cons) c.cdr();
            }
            return sum;
        }
    };

    public static Builtin MINUS = new Builtin("-") {

        @Override
        public Object apply(Env env, Cons args) {
            int n1 = (int) args.car();
            int n2 = (int) ((Cons)args.cdr()).car();
            return n1 - n2;
        }
    };

    public static Builtin LT = new Builtin("<") {

        @Override
        public Object apply(Env env, Cons args) {
            int left = (int) args.car();
            int right = (int) ((Cons)args.cdr()).car();
            return left < right;
        }
    };
}

