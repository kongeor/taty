package jisp;

public class Bool {

    public static boolean isTruthy(Object obj) {
        return obj != null && obj != Boolean.FALSE;
    }
}
