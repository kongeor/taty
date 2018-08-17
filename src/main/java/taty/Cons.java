package taty;

import java.util.Objects;

public class Cons implements TatyExpr {

    // TODO taty expr
    private final Object car;
    private final Object cdr;

    public Cons(Object car) {
        this(car, NilExpr.NIL);
    }

    public Cons(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public static Cons Cons_(Object car) {
        return new Cons(car, NilExpr.NIL);
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

    public Cons bind(SymbExpr sym, Object val) {
        return Cons_(Cons_(sym, val), this);
    }

    public Object lookup(SymbExpr sym) {
        Cons c = this;
        while (c != NilExpr.NIL) {
            Cons head = (Cons) c.car;
            if (Objects.equals(head.car, sym)) {
                return head.cdr;
            }
            c = (Cons) c.cdr;
        }
        throw new IllegalArgumentException("Cannot find symbol " + sym + " in env " + this);
    }

    public Object nth(int n) {
        int idx = 0;
        Cons current = this;
        while (current != NilExpr.NIL && idx != n) {
            current = (Cons) current.cdr;
            idx++;
        }
        return current.car;
    }

    public Cons reverse() {
        if (this == NilExpr.NIL) {
            return this;
        } else if (!(cdr instanceof Cons)) {
            if (cdr == NilExpr.NIL) {
                return this;
            } else {
                return new Cons(cdr, car);
            }
        } else {
            Cons c = (Cons) cdr;
            Cons rev = new Cons(car);
            while (c != NilExpr.NIL) {
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

            if (c.cdr != NilExpr.NIL) {
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
