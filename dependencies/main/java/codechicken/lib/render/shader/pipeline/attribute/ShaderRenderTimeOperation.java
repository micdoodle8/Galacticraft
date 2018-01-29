package codechicken.lib.render.shader.pipeline.attribute;

import codechicken.lib.render.shader.ShaderProgram;
import codechicken.lib.render.shader.pipeline.CCShaderPipeline;
import codechicken.lib.util.ClientUtils;
import org.lwjgl.opengl.ARBShaderObjects;

import java.util.HashMap;

/**
 * Created by covers1624 on 18/10/2016.
 */
public class ShaderRenderTimeOperation implements IShaderOperation {

    public static final int operationID = CCShaderPipeline.registerOperation();
    private final HashMap<ShaderProgram, Float> shaderRenderTimeCache = new HashMap<ShaderProgram, Float>();

    @Override
    public boolean load(ShaderProgram program) {
        return true;
    }

    @Override
    public void operate(ShaderProgram program) {
        float renderTime = (float) ClientUtils.getRenderTime();
        if (renderTime != shaderRenderTimeCache.get(program)) {
            int location = program.getAttribLoc("time");
            ARBShaderObjects.glUniform1fARB(location, renderTime);
            shaderRenderTimeCache.put(program, renderTime);
        }
    }

    @Override
    public int operationID() {
        return operationID;
    }
}
