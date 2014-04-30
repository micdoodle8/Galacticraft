package codechicken.lib.render.uv;

import codechicken.lib.math.MathHelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class UVTranslation extends UVTransformation {
    public double du;
    public double dv;

    public UVTranslation(double u, double v) {
        du = u;
        dv = v;
    }

    @Override
    public void apply(UV uv) {
        uv.u += du;
        uv.v += dv;
    }

    @Override
    public UVTransformation at(UV point) {
        return this;
    }

    @Override
    public UVTransformation inverse() {
        return new UVTranslation(-du, -dv);
    }

    @Override
    public UVTransformation merge(UVTransformation next) {
        if (next instanceof UVTranslation) {
            UVTranslation t = (UVTranslation)next;
            return new UVTranslation(du+t.du, dv+t.dv);
        }

        return null;
    }

    @Override
    public boolean isRedundant() {
        return MathHelper.between(-1E-5, du, 1E-5) && MathHelper.between(-1E-5, dv, 1E-5);
    }

    @Override
    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "UVTranslation(" + new BigDecimal(du, cont) + ", " + new BigDecimal(dv, cont) + ")";
    }
}
