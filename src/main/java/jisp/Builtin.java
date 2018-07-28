package jisp;

import com.sun.org.apache.bcel.internal.generic.IFNE;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static jisp.Cons.Cons_;

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
            while (a != null) {
                sb.append(a.car());
                a = (Cons) a.cdr();
            }
            System.out.println(sb.toString());
            return null;
        }
    };

    public static Builtin PLUS = new Builtin("+") {

        @Override
        public Object apply(Env env, Cons args) {
            int sum = 0;
            Cons c = args;
            while (c != null) {
                sum = Math.addExact(sum, (Integer) c.car());
                c = (Cons) c.cdr();
            }
            return sum;
        }
    };

    public static Builtin MULT = new Builtin("*") {

        @Override
        public Object apply(Env env, Cons args) {
            int sum = 1;
            Cons c = args;
            while (c != null) {
                sum = Math.multiplyExact(sum, (Integer) c.car());
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

    public static Builtin DIV = new Builtin("/") {

        @Override
        public Object apply(Env env, Cons args) {
            int n1 = (int) args.car();
            int n2 = (int) ((Cons)args.cdr()).car();
            return n1 / n2;
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

    public static Builtin GT = new Builtin(">") {

        @Override
        public Object apply(Env env, Cons args) {
            int left = (int) args.car();
            int right = (int) ((Cons)args.cdr()).car();
            return left > right;
        }
    };

    public static Builtin LTE = new Builtin("<=") {

        @Override
        public Object apply(Env env, Cons args) {
            int left = (int) args.car();
            int right = (int) ((Cons)args.cdr()).car();
            return left <= right;
        }
    };

    public static Builtin GTE = new Builtin(">=") {

        @Override
        public Object apply(Env env, Cons args) {
            int left = (int) args.car();
            int right = (int) ((Cons)args.cdr()).car();
            return left >= right;
        }
    };

    public static Builtin EQ = new Builtin("=") {

        @Override
        public Object apply(Env env, Cons args) {
            Object left = args.car();
            Object right = ((Cons)args.cdr()).car();
            return Objects.equals(left, right);
        }
    };

    public static Builtin DEC = new Builtin("dec") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return ((Integer) param) - 1;
        }
    };

    public static Builtin INC = new Builtin("inc") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return ((Integer) param) + 1;
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
            Object right = null;
            if (args.cdr() != null) {
                right = ((Cons) args.cdr()).car();
            }
            return Cons_(left, right);
        }
    };

    public static Builtin CONS_Q = new Builtin("cons?") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return param instanceof Cons;
        }
    };

    public static Builtin SYMBOL_Q = new Builtin("symbol?") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return param instanceof SymbExp;
        }
    };

    public static Builtin APPLY = new Builtin("apply") {

        @Override
        public Object apply(Env env, Cons args) {
            IFn f = (IFn) args.car();
            Cons a = (Cons) ((Cons)args.cdr()).car();
            return f.apply(env, evalArgs(env, a));
        }

        // TODO copy-pasta
        private Cons evalArgs(Env env, Cons args) {
            if (args == null) {
                return null;
            } else {
                return new Cons(((JispExp)args.car()).eval(env),
                        evalArgs(env, (Cons)args.cdr()));
            }
        }
    };

    public static Builtin NIL_Q = new Builtin("nil?") {

        @Override
        public Object apply(Env env, Cons args) {
            Object param = args.car();
            return param == null;
        }
    };

    public static Builtin VARARG = new Builtin("vararg") {

        @Override
        public Object apply(Env env, Cons parentArgs) {
            return (IFn) (nestedEnv, args) -> {
//                IFn f = (IFn) parentArgs.car();
//                return f.apply(nestedEnv, Cons_(new Special.QuoteExp(args)));
                // FIXME
                return null;
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
                return new String(bytes, Charset.defaultCharset());
            } catch (IOException e) {
                throw new JispException("Cannot read file: " + file.toUri(), e);
            }
        }
    };

    public static Builtin LOAD_FILE = new Builtin("load-file") {

        @Override
        public Object apply(Env env, Cons args) {
            Object content = READ_FILE.apply(env, args);
            // TODO error check
            Reader reader = new Reader();
            return ((JispExp)reader.read("(do " + content + ")").car()).eval(env);
        }
    };

    public static Builtin DIE = new Builtin("die") {

        @Override
        public Object apply(Env env, Cons args) {
            Object msg = args.car();
            System.out.println(msg);
            System.exit(1);
            return null;
        }
    };
}

