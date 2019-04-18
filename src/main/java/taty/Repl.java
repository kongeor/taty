package taty;

import java.util.Scanner;

public class Repl {

    private void printWelcome() {
        System.out.println("Welcome to Taty!");
    }

    public void start() {

        printWelcome();

        Scanner scanner = new Scanner(System.in);

        Reader reader = new Reader();

        Env env = Env.initBaseEnv();

        long parenDiff = 0;

        StringBuilder sb = new StringBuilder();

        while (true) {
            try {
                if (parenDiff == 0) {
                    System.out.print("(\\)=> ");
                } else {
                    System.out.print(parenDiff + ") ");
                }

                String data = scanner.nextLine();
                if (data == null) {
                    break;
                }

                parenDiff += parenDiff(data);
                if (parenDiff < 0) {
                    sb = new StringBuilder();
                    parenDiff = 0;
                    throw new TatyException("Unbalanced parens");
                }
                if (parenDiff > 0) {
                    sb.append(data);
                    continue;
                }

                sb.append(data);


                TatyExpr read = reader.read(sb.toString());
                sb = new StringBuilder();

                Object result = NilExpr.NIL;

                if (read != NilExpr.NIL) {
                    TatyExpr exp = ((Cons) read).car();

                    while (exp != NilExpr.NIL) {
                        result = Eval.eval(env, exp);
                        exp = ((Cons) read).cdr();
                    }
                    System.out.println(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long parenDiff(String line) {
        long open = line.chars().filter(c -> c == '[' || c == '(').count();
        long close = line.chars().filter(c -> c == ']' || c == ')').count();
        return open - close;
    }
}
