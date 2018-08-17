package taty;

public interface IFn extends TatyExpr {

    Object apply(Env env, Cons args);

}
