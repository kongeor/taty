package jisp;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
//        Cons read = (Cons) reader.read("((fn (a b) (+ a b)) 1 2)").car();
//        JispExp read = (JispExp) reader.read("(do 1 2 (+ 1 2))").car();
//        JispExp read = (JispExp) reader.read("(do (def a 1) (+ a 5))").car();
        JispExp read = (JispExp) reader.read("(do (def add (fn [a b] (+ a b))) (add 4 5))").car();
//        JispExp read = (JispExp) reader.read("(quote a)").car();
//        Special.LetExp read = (Special.LetExp) reader.read("(let [a 1 b 2] (+ a b 5))").car();
        Env.bindGlobal(SymbExp.SymbExp_("+"), Builtin.PLUS);
        System.out.println(read.eval(Env.Env_()));
    }
}
