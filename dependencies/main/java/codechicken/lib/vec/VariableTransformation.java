package codechicken.lib.vec;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class VariableTransformation extends Transformation {

    public Matrix4 mat;

    public VariableTransformation(Matrix4 mat) {
        this.mat = mat;
    }

    @Override
    public void applyN(Vector3 normal) {
        apply(normal);
    }

    @Override
    public void apply(Matrix4 mat) {
        mat.multiply(this.mat);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void glApply() {
        mat.glApply();
    }
}
