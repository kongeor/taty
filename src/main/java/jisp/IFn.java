package jisp;

public interface IFn extends JispExpr {

    Object apply(Env env, Cons args);

}
