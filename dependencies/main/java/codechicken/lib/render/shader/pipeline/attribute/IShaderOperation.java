package codechicken.lib.render.shader.pipeline.attribute;

import codechicken.lib.render.shader.ShaderProgram;

/**
 * Created by covers1624 on 18/10/2016.
 */
public interface IShaderOperation {

    boolean load(ShaderProgram program);

    void operate(ShaderProgram program);

    int operationID();
}
