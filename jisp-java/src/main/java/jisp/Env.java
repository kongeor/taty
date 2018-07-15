package jisp;

import java.util.concurrent.atomic.AtomicReference;

public class Env {
    
    private static final AtomicReference<Cons> globals = 
        new AtomicReference<>(null);

    public static void bind(SymbExp sym, JispExp val) {
        globals.accumulateAndGet(new Cons(sym, val),
            (g, v) -> new Cons(v, g));
    }

    public static Object lookup(SymbExp sym) {
        return globals.get().lookup(sym);
    }

}
