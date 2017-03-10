package codechicken.lib.util;

/**
 * Created by covers1624 on 8/29/2016.
 * TODO Copyable.
 */
public class TripleABC<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    public TripleABC(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
}
