package taty;

public interface IFn extends TatyExpr {

    TatyExpr apply(Env env, TatyExpr args);

}
