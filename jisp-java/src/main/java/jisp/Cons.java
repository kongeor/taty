package jisp;

public class Cons extends JispExp {

    private final JispExp car;
    private final Cons cdr;

    public Cons(JispExp car) {
        this(car, null);
    }

    public Cons(JispExp car, Cons cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public JispExp car() {
        return car;
    }

    public Cons cdr() {
        return cdr;
    }

    public Cons reverse() {
        Cons c = cdr;
        Cons rev = new Cons(car);
        while (c != null) {
            rev = new Cons(c.car, rev);
            c = c.cdr;
        }
        return rev;
    }

    public String prn() {
        StringBuilder sb = new StringBuilder("(").append(car.toString());
        Cons c = cdr;
        while(c != null) {
            sb.append(" ");
            sb.append(c.car);
            c = c.cdr;
        }
        return sb.append(")").toString();
    }

    @Override
    public String toString() {
        return prn();
    }

    public static void main(String[] args) {
        Cons cons = new Cons(new NumberExp(5),
                new Cons(new NumberExp(10),
                        new Cons(new NumberExp(15))));

        System.out.println(cons.prn());
        System.out.println(cons.reverse().prn());
    }
}
