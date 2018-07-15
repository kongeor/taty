package jisp;

import java.util.concurrent.atomic.AtomicLong;

public class Special {

    public static class LetExp extends JispExp {

        private final Cons binds;
        private final JispExp body;

        public LetExp(Cons binds, JispExp body) {
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

        private static final AtomicLong ids = new AtomicLong(1);

        private final Cons params;
        private final JispExp body;
        private final long id;

        public FnExp(Cons params, JispExp body) {
            this.params = params;
            this.body = body;
            this.id = ids.getAndIncrement();
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
                        lambdaEnv = lambdaEnv.bind((SymbExp) p.car(), a.car());
                        p = (Cons) p.cdr();
                        a = (Cons) a.cdr();
                    }

//                Cons b = body;
//                Object result = null;
//                while (b != null) {
//                    result = ((JispExp) b.car()).eval(lambdaEnv);
//                    b = (Cons) b.cdr();
//                }

                    return body.eval(lambdaEnv);
                }

                @Override
                public String toString() {
                    return "<Lambda:" + id + ">";
                }
            };
        }

    }

    public static class QuoteExp extends JispExp {

        private final Object body;

        public QuoteExp(Object body) {
            this.body = body;
        }

        @Override
        public Object eval(Env env) {
            return body;
        }
    }

    public static class DoExp extends JispExp {

        private final Cons exprs;

        public DoExp(Cons exprs) {
            this.exprs = exprs;
        }

        @Override
        public Object eval(Env env) {
            Object result = null;

            Cons e = exprs;
            while (e != null) {
                result = ((JispExp) e.car()).eval(env);
                e = (Cons) e.cdr();
            }

            return result;
        }
    }

    public static class DefExp extends JispExp {

        private final SymbExp symb;
        private final JispExp val;

        public DefExp(SymbExp symb, JispExp val) {
            this.symb = symb;
            this.val = val;
        }

        @Override
        public Object eval(Env env) {
            Object result = val.eval(env);
            Env.bindGlobal(symb, result);
            return result;
        }
    }

    public static class IfExpr extends JispExp {

        private final JispExp pred;
        private final JispExp ifExp;
        private final JispExp elseExp;

        public IfExpr(JispExp pred, JispExp ifExp, JispExp elseExp) {
            this.pred = pred;
            this.ifExp = ifExp;
            this.elseExp = elseExp;
        }

        @Override
        public Object eval(Env env) {
            if (pred.eval(env) == Boolean.TRUE) {
                return ifExp.eval(env);
            } else {
                return elseExp.eval(env);
            }
        }
    }

    public static JispExp checkForm(Cons cons) {
        if (cons == null) {
            return null;
        } else {
            Object car = cons.car();
            if (SymbExp.SymbExp_("let").equals(car)) {
                return new LetExp((Cons)((Cons)cons.cdr()).car(), (JispExp)((Cons)((Cons)cons.cdr()).cdr()).car());
            } else if (SymbExp.SymbExp_("fn").equals(car)) {
                return new FnExp((Cons)((Cons)cons.cdr()).car(), (JispExp) ((Cons)((Cons)cons.cdr()).cdr()).car());
            } else if (SymbExp.SymbExp_("quote").equals(car)) {
                return new QuoteExp(((Cons)cons.cdr()).car());
            } else if (SymbExp.SymbExp_("do").equals(car)) {
                return new DoExp((Cons)cons.cdr());
            } else if (SymbExp.SymbExp_("def").equals(car)) {
                return new DefExp((SymbExp)((Cons)cons.cdr()).car(), (JispExp)((Cons)((Cons)cons.cdr()).cdr()).car());
            } else if (SymbExp.SymbExp_("if").equals(car)) {
                return new IfExpr((JispExp)((Cons)cons.cdr()).car(),
                        (JispExp)((Cons)((Cons)cons.cdr()).cdr()).car(),
                        (JispExp)((Cons)(((Cons)((Cons)cons.cdr()).cdr()).cdr())).car());
            }

            return cons;
        }
    }
}
