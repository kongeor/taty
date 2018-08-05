package jisp;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConsTest {

    @Test
    public void one_element() throws IOException {
        Cons cons = new Cons(new NumberExpr(1), NilExpr.NIL);
        assertEquals("(1)", cons.toString());
    }

    @Test
    public void two_elements() throws IOException {
        Cons cons = new Cons(new NumberExpr(1), new Cons(new NumberExpr(2)));
        assertEquals("(1 2)", cons.toString());
    }

    @Test
    public void cons_cell() throws IOException {
        Cons cons = new Cons(new NumberExpr(1), new NumberExpr(2));
        assertEquals("(1 . 2)", cons.toString());
    }

    @Test
    public void cons_in_cons() throws IOException {
        Cons cons = new Cons(new Cons(new NumberExpr(1), new NumberExpr(2)));
        assertEquals("((1 . 2))", cons.toString());
    }

    @Test
    public void reverse_one() throws IOException {
        Cons cons = new Cons(new NumberExpr(1));
        assertEquals("(1)", cons.reverse().toString());
    }

    @Test
    public void reverse_two() throws IOException {
        Cons cons = new Cons(new NumberExpr(1), new Cons(new NumberExpr(2)));
        assertEquals("(2 1)", cons.reverse().toString());
    }

}
