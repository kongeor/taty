package jisp;

import java.util.Objects;

public class Cons extends JispExpr {

    private final Object car;
    private final Object cdr;

    public Cons(Object car) {
        this(car, null);
    }

    public Cons(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public static Cons Cons_(Object car) {
        return new Cons(car, null);
    }

    public static Cons Cons_(Object car, Object cdr) {
        return new Cons(car, cdr);
    }

    public Object car() {
        return car;
    }

    public Object cdr() {
        return cdr;
    }

    @Override
    public Object eval(Env env) {
        Cons evalArgs = evalArgs(env, this);
        if (!(evalArgs.car instanceof IFn)) {
            throw new JispException("Cannot apply f: " + car + " Reason: " + evalArgs.car);
        }
        IFn fn = (IFn) evalArgs.car;
        Cons args = (Cons) evalArgs.cdr;
        return fn.apply(env, args);
    }

    private Cons evalArgs(Env env, Cons args) {
        if (args == null) {
            return null;
        } else {
            return new Cons(((JispExpr)args.car).eval(env),
                evalArgs(env, (Cons)args.cdr));
        }
    }

    public Cons bind(SymbExpr sym, Object val) {
        return Cons_(Cons_(sym, val), this);
    }

    public Object lookup(SymbExpr sym) {
        Cons c = this;
        while (c != null) {
            Cons head = (Cons) c.car;
            if (Objects.equals(head.car, sym)) {
                return head.cdr;
            }
            c = (Cons) c.cdr;
        }
        throw new IllegalArgumentException("Cannot find symbol " + sym + " in env " + this);
    }

    public Cons reverse() {
        if (!(cdr instanceof Cons)) {
            if (cdr == null) {
                return this;
            } else {
                return new Cons(cdr, car);
            }
        } else {
            Cons c = (Cons) cdr;
            Cons rev = new Cons(car);
            while (c != null) {
                rev = new Cons(c.car, rev);
                c = (Cons) c.cdr;
            }
            return rev;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(car, ((Cons)obj).car)
                && Objects.equals(cdr, ((Cons)obj).cdr);
    }

    @Override
    public String toString() {
        return toStrImpl(new StringBuilder());
    }

    private String toStrImpl(StringBuilder sb) {
        Cons c = this;
        sb.append("(");
        while(true) {
            sb.append(c.car);

            if (c.cdr != null) {
                if (!(c.cdr instanceof Cons)) {
                    sb.append(" . ").append(c.cdr);
                    break;
                }  else {
                    sb.append(" ");
                    c = (Cons)c.cdr;
                }
            } else {
                break;
            }
        }
        return sb.append(")").toString();
    }

    public static void main(String[] args) {
        Cons cons = new Cons(new NumberExpr(5),
                new Cons(new NumberExpr(10),
                        new Cons(new NumberExpr(15))));

        // System.out.println(cons.prn());
        // System.out.println(cons.reverse().prn());
    }
}
