package jisp;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Repl().start();
    }
}

/**
 *
 (def fibo
    (fn [n]
        (if (< n 3)
            1
            (+ (fibo (- n 1)) (fibo (- n 2))))))

(def fact
        (fn [n]
        (if (< n 1)
            1
        (* n (fact (- n 1))))))


 */