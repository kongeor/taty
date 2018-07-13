package jisp;

public class Cons extends JispExp {

    private final JispExp car;
    private final JispExp cdr;

    public Cons(JispExp car) {
        this(car, null);
    }

    public Cons(JispExp car, JispExp cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public JispExp car() {
        return car;
    }

    public JispExp cdr() {
        return cdr;
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
