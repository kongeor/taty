package jisp;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static void repl() throws IOException {

        Scanner scanner = new Scanner(System.in);

        Reader reader = new Reader();

        Env env = Env.initBaseEnv();

        while (true) {
            try {
                System.out.print("-> ");
                String data = scanner.nextLine();
                if (data == null) {
                    break;
                }

                Cons read = reader.read(data);

                Object result = null;
                JispExp exp = (JispExp) read.car();

                while (exp != null) {
                    result = exp.eval(env);
                    exp = (JispExp) read.cdr();
                }

                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
//        Reader reader = new Reader();
//        Cons read = (Cons) reader.read("((fn (a b) (+ a b)) 1 2)").car();
//        JispExp read = (JispExp) reader.read("(do 1 2 (+ 1 2))").car();
//        JispExp read = (JispExp) reader.read("(do (def a 1) (+ a 5))").car();
//        JispExp read = (JispExp) reader.read("(do (def add (fn [a b] (+ a b))) (add 4 5))").car();
//        JispExp read = (JispExp) reader.read("(if false 1 2)").car();
//        JispExp read = (JispExp) reader.read("(quote a)").car();
//        Special.LetExp read = (Special.LetExp) reader.read("(let [a 1 b 2] (+ a b 5))").car();
//        System.out.println(read.eval(Env.Env_()));

        repl();
    }
}
