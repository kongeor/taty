package jisp;

public class Bool {

    public static boolean isTruthy(Object obj) {
        return obj != NilExpr.NIL && obj != BoolExpr.F;
    }
}
