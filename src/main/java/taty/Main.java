package taty;

import static taty.Cons.Cons_;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            Env env = Env.initBaseEnv();
            Object res = Builtin.LOAD_FILE.apply(env, Cons_(new StringExpr(args[0])));
            System.out.println(res);
        } else {
            new Repl().start();
        }
    }
}
