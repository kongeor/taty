package jisp;

public class Special {

    public static class LetExp extends JispExp {

        private final Cons binds;
        private final Cons body;

        public LetExp(Cons binds, Cons body) {
            this.binds = binds;
            this.body = body;
        }

        @Override
        public Object eval(Env env) {

            Cons b = binds;
            while (b != null) {
                SymbExp car = (SymbExp) b.car();
                JispExp form = (JispExp) ((Cons) b.cdr()).car();
                Object val = form.eval(env);
                env = env.bind(car, val);
                b = (Cons) ((Cons) b.cdr()).cdr();
            }

            return body.eval(env);
        }
    }

    public static class FnExp extends JispExp {

        private final Cons params;
        private final Cons body;

        public FnExp(Cons params, Cons body) {
            this.params = params;
            this.body = body;
        }

        @Override
        public Object eval(Env parentEnv) {
            return new IFn() {
                @Override
                public Object apply(Env env, Cons args) {
                    Env lambdaEnv = parentEnv;

                    Cons p = params;
                    Cons a = args;
                    while (p != null) {
                        lambdaEnv.bind((SymbExp) p.car(), a.car());
                        p = (Cons) p.cdr();
                        a = (Cons) a.cdr();
                    }

                    Cons b = body;
                    Object result = null;
                    while (b != null) {

                    }

                    return result;
                }
            };
        }
    }

    public static JispExp checkForm(Cons cons) {
        if (cons == null) {
            return null;
        } else {
            Object car = cons.car();
            if (SymbExp.SymbExp_("let").equals(car)) {
                return new LetExp((Cons)((Cons)cons.cdr()).car(), (Cons)((Cons)((Cons)cons.cdr()).cdr()).car());
            }

            return cons;
        }
    }
}
