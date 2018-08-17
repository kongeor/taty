package taty;


import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsTest {

    @Test
    public void one_element() {
        Cons cons = new Cons(new NumberExpr(1), NilExpr.NIL);
        assertEquals("(1)", cons.toString());
    }

    @Test
    public void two_elements() {
        Cons cons = new Cons(new NumberExpr(1), new Cons(new NumberExpr(2)));
        assertEquals("(1 2)", cons.toString());
    }

    @Test
    public void cons_cell() {
        Cons cons = new Cons(new NumberExpr(1), new NumberExpr(2));
        assertEquals("(1 . 2)", cons.toString());
    }

    @Test
    public void cons_in_cons() {
        Cons cons = new Cons(new Cons(new NumberExpr(1), new NumberExpr(2)));
        assertEquals("((1 . 2))", cons.toString());
    }

    @Test
    public void reverse_one() {
        Cons cons = new Cons(new NumberExpr(1));
        assertEquals("(1)", cons.reverse().toString());
    }

    @Test
    public void reverse_two() {
        Cons cons = new Cons(new NumberExpr(1), new Cons(new NumberExpr(2)));
        assertEquals("(2 1)", cons.reverse().toString());
    }

}
