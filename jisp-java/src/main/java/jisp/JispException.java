package jisp;

public class JispException extends RuntimeException {
    
    public JispException(String msg) {
        super(msg);
    }

    public JispException(String msg, Exception e) {
        super(msg, e);
    }
}
