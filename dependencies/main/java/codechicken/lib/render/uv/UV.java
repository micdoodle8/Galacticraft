package codechicken.lib.render.uv;

import codechicken.lib.util.Copyable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class UV implements Copyable<UV> {
    public double u;
    public double v;
    public int tex;

    public UV() {
    }

    public UV(double u, double v) {
        this(u, v, 0);
    }

    public UV(double u, double v, int tex) {
        this.u = u;
        this.v = v;
        this.tex = tex;
    }

    public UV(UV uv) {
        this(uv.u, uv.v, uv.tex);
    }

    public UV set(double u, double v, int tex) {
        this.u = u;
        this.v = v;
        this.tex = tex;
        return this;
    }

    public UV set(double u, double v) {
        return set(u, v, tex);
    }

    public UV set(UV uv) {
        return set(uv.u, uv.v, uv.tex);
    }

    public UV copy() {
        return new UV(this);
    }

    public UV add(UV uv) {
        u += uv.u;
        v += uv.v;
        return this;
    }

    public UV multiply(double d) {
        u *= d;
        v *= d;
        return this;
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "UV(" + new BigDecimal(u, cont) + ", " + new BigDecimal(v, cont) + ")";
    }

    public UV apply(UVTransformation t) {
        t.apply(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UV))
            return false;
        UV uv = (UV) o;
        return u == uv.u && v == uv.v;
    }
}