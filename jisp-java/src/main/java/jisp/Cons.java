package jisp;

import java.util.Objects;

public class Cons extends JispExp {

    private final Object car;
    private final Object cdr;

    public Cons(Object car) {
        this(car, null);
    }

    public Cons(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
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
//        Object f = car;
//        if (!(f instanceof IFn)) {
//            throw new JispException("Cannot apply f: " + f);
//        }
//        IFn fn = (IFn)f;
//        Cons args = (Cons) cdr;
        Cons e = evalArgs(env, this);
        return ((IFn)e.car).apply(env, (Cons) e.cdr());
    }

    private Cons evalArgs(Env env, Cons args) {
        if (args == null) {
            return null;
        } else {
            return new Cons(((JispExp)args.car).eval(env),
                evalArgs(env, (Cons)args.cdr));
        }
    }

    public Cons bind(SymbExp sym, Object val) {
        return Cons_(Cons_(sym, val), this);
    }

    public Object lookup(SymbExp sym) {
        Cons c = this;
        while (c != null) {
            Cons head = (Cons) c.car;
            if (Objects.equals(head.car, sym)) {
                return head.cdr;
            }
            c = (Cons) c.cdr;
        }
        throw new IllegalArgumentException("Cannot find symbol " + sym + " in cons " + this);
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
        Cons cons = new Cons(new NumberExp(5),
                new Cons(new NumberExp(10),
                        new Cons(new NumberExp(15))));

        // System.out.println(cons.prn());
        // System.out.println(cons.reverse().prn());
    }
}
