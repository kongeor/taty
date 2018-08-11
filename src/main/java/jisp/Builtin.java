package jisp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static jisp.BoolExpr.BoolExpr_;
import static jisp.Cons.Cons_;
import static jisp.NumberExpr.NumberExpr_;

public abstract class Builtin implements IFn {

    private final String name;

    public Builtin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "<BuiltinFn:" + name + ">";
    }

    public static Builtin PRINTLN = new Builtin("println") {

        @Override
        public Object apply(Env env, Cons args) {
            StringBuilder sb = new StringBuilder();
            Cons a = args;
            while (a != NilExpr.NIL) {
                sb.append(a.car());
                a = (Cons) a.cdr();
            }
            System.out.println(sb.toString());
            return NilExpr.NIL;
        }
    };

    public static Builtin PLUS = new Builtin("+") {

        @Override
        public Object apply(Env env, Cons args) {
            long sum = 0;
            Cons c = args;
            while (c != NilExpr.NIL) {
                sum = Math.addExact(sum, ((NumberExpr) c.car()).longVal());
                c = (Cons) c.cdr();
            }
            return NumberExpr_(sum);
        }
    };

    public static Builtin MULT = new Builtin("*") {

        @Override
        public Object apply(Env env, Cons args) {
            long sum = 1;
            Cons c = args;
            while (c != NilExpr.NIL) {
                sum = Math.multiplyExact(sum, ((NumberExpr) c.car()).longVal());
                c = (Cons) c.cdr();
            }
            return NumberExpr_(sum);
        }
    };

    public static Builtin MINUS = new Builtin("-") {

        @Override
        public Object apply(Env env, Cons args) {
            long n1 = ((NumberExpr) args.car()).longVal();
            long n2 = ((NumberExpr)((Cons)args.cdr()).car()).longVal();
            return NumberExpr_(n1 - n2);
        }
    };

    public static Builtin DIV = new Builtin("/") {

        @Override
        public Object apply(Env env, Cons args) {
            long n1 = ((NumberExpr) args.car()).longVal();
            long n2 = ((NumberExpr)((Cons)args.cdr()).car()).longVal();
            return NumberExpr_(n1 / n2);
        }
    };

    public static Builtin LT = new Builtin("<") {

        @Override
        public Object apply(Env env, Cons args) {
            long left = ((NumberExpr) args.car()).longVal();
            long right = ((NumberExpr)((Cons)args.cdr()).car()).longVal();
            return BoolExpr_(left < right);
        }
    };

    public static Builtin GT = new Builtin(">") {

        @Override
        public Object apply(Env env, Cons args) {
            long left = ((NumberExpr) args.car()).longVal();
            long right = ((NumberExpr)((Cons)args.cdr()).car()).longVal();
            return BoolExpr_(left > right);
        }
    };

    public static Builtin LTE = new Builtin("<=") {

        @Override
        public Object apply(Env env, Cons args) {
            long left = ((NumberExpr) args.car()).longVal();
            long right = ((NumberExpr)((Cons)args.cdr()).car()).longVal();
            return BoolExpr_(left <= right);
        }
    };

    public static Builtin GTE = new Builtin(">=") {

        @Override
        public Object apply(Env env, Cons args) {
            long left = ((NumberExpr) args.car()).longVal();
            long right = ((NumberExpr)((Cons)args.cdr()).car()).longVal();
            return BoolExpr_(left >= right);
        }
    };

    public static Builtin EQ = new Builtin("=") {

        @Override
        public Object apply(Env env, Cons args) {
            Object left = args.car();
            Object right = ((Cons)args.cdr()).car();
            return BoolExpr_(Objects.equals(left, right));
        }
    };

    public static Builtin DEC = new Builtin("dec") {

        @Override
        public Object apply(Env env, Cons args) {
            long param = ((NumberExpr)args.car()).longVal();
            return NumberExpr_(Math.subtractExact(param, 1));
        }
    };

    public static Builtin INC = new Builtin("inc") {

        @Override
        public Object apply(Env env, Cons args) {
            long param = ((NumberExpr)args.car()).longVal();
            return NumberExpr_(Math.addExact(param, 1));
        }
    };

    public static Builtin CAR = new Builtin("car") {

        @Override
        public Object apply(Env env, Cons args) {
            Cons cons = (Cons)args.car();
            return cons.car();
        }
    };

    public static Builtin CDR = new Builtin("cdr") {

        @Override
        public Object apply(Env env, Cons args) {
            Cons cons = (Cons)args.car();
            return cons.cdr();
        }
    };

    public static Builtin CONS = new Builtin("cons") {

        @Override
        public Object apply(Env env, Cons args) {
            Object left = args.car();
            Object right = NilExpr.NIL;
            if (args.cdr() != NilExpr.NIL) {
                right = ((Cons) args.cdr()).car();
            }
            return Cons_(left, right);
        }
    };

    public static Builtin CONS_Q = new Builtin("cons?") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return BoolExpr_(param instanceof Cons);
        }
    };

    public static Builtin SYMBOL_Q = new Builtin("symbol?") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return BoolExpr_(param instanceof SymbExpr);
        }
    };

    public static Builtin APPLY = new Builtin("apply") {

        @Override
        public Object apply(Env env, Cons args) {
            IFn f = (IFn) args.car();
            Cons a = (Cons) ((Cons)args.cdr()).car();
//            return f.apply(env, evalArgs(env, a));
            return f.apply(env, a);
        }

//        // TODO copy-pasta
//        private Cons evalArgs(Env env, Cons args) {
//            if (args == NilExpr.NIL) {
//                return NilExpr.NIL;
//            } else {
//                return new Cons(((JispExpr)args.car()).eval(env),
//                        evalArgs(env, (Cons)args.cdr()));
//            }
//        }
    };

    public static Builtin NIL_Q = new Builtin("nil?") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return BoolExpr_(param == NilExpr.NIL);
        }
    };

    public static Builtin VARARG = new Builtin("vararg") {

        @Override
        public Object apply(Env env, Cons parentArgs) {
            return (IFn) (nestedEnv, args) -> {
                IFn f = (IFn) parentArgs.car();
                return f.apply(nestedEnv, Cons_(args));
            };
        }
    };


    public static Builtin READ_FILE = new Builtin("read-file") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            Path file = Paths.get("" + param);
            try {
                byte[] bytes = Files.readAllBytes(file);
                String content = new String(bytes, Charset.defaultCharset());
                Reader reader = new Reader();
                return reader.read("(do " + content + ")").car();
            } catch (IOException e) {
                throw new JispException("Cannot read file: " + file.toUri(), e);
            }
        }
    };

    public static Builtin LOAD_FILE = new Builtin("load-file") {

        @Override
        public Object apply(Env env, Cons args) {
            JispExpr form = (JispExpr) READ_FILE.apply(env, args);
            // TODO error check
            return Eval.eval(env, form);
        }
    };

    public static Builtin DIE = new Builtin("die") {

        @Override
        public Object apply(Env env, Cons args) {
            Object msg = args.car();
            System.out.println(msg);
            System.exit(1);
            return NilExpr.NIL;
        }
    };
}

