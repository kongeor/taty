package taty;

public class TatyException extends RuntimeException {
    
    public TatyException(String msg) {
        super(msg);
    }

    public TatyException(String msg, Exception e) {
        super(msg, e);
    }
}
