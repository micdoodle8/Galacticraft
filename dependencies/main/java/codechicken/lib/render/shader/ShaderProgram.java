package codechicken.lib.render.shader;

import codechicken.lib.render.shader.pipeline.CCShaderPipeline;
import codechicken.lib.render.shader.pipeline.attribute.IShaderOperation;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.lwjgl.opengl.ARBShaderObjects.*;

public class ShaderProgram {

    private int programID;
    public CCShaderPipeline pipeline = new CCShaderPipeline(this);
    private ArrayList<IShaderOperation> ops = new ArrayList<IShaderOperation>();

    public ShaderProgram() {
        programID = glCreateProgramObjectARB();
        if (programID == 0) {
            throw new RuntimeException("Unable to allocate shader program object.");
        }
    }

    public void attachShaderOperation(IShaderOperation operation) {
        ops.add(operation);
    }

    public void bindShader() {
        glUseProgramObjectARB(programID);
    }

    /**
     * Allows you to bind the shader for use outside an IShaderOperation.
     * You can still pass variables to the shader using an IShaderOperation.
     *
     * Call this before you do your rendering.
     * Then call ShaderProgram.unbindShader() when you have finished rendering.
     */
    public void freeBindShader() {
        pipeline.reset();
        pipeline.setPipeline(ops);

        glUseProgramObjectARB(programID);
        pipeline.operate();
    }

    public static void unbindShader() {
        glUseProgramObjectARB(0);
    }

    public void runShader() {
        pipeline.reset();
        pipeline.setPipeline(ops);

        bindShader();
        pipeline.operate();
        unbindShader();
    }

    public ShaderProgram attachVert(String resource) {
        return attach(ARBVertexShader.GL_VERTEX_SHADER_ARB, resource);
    }

    public ShaderProgram attachFrag(String resource) {
        return attach(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB, resource);
    }

    public ShaderProgram attach(int shaderType, String resource) {
        InputStream stream = ShaderProgram.class.getResourceAsStream(resource);
        if (stream == null) {
            throw new RuntimeException("Unable to locate resource: " + resource);
        }

        return attach(shaderType, stream);
    }

    public ShaderProgram attach(int shaderType, InputStream stream) {
        if (stream == null) {
            throw new RuntimeException("Invalid shader inputstream");
        }

        int shaderID = 0;
        try {
            shaderID = glCreateShaderObjectARB(shaderType);
            if (shaderID == 0) {
                throw new RuntimeException("Unable to allocate shader object.");
            }

            try {
                glShaderSourceARB(shaderID, asString(stream));
            } catch (IOException e) {
                throw new RuntimeException("Error reading inputstream.", e);
            }

            glCompileShaderARB(shaderID);
            if (glGetObjectParameteriARB(shaderID, GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
                throw new RuntimeException("Error compiling shader: " + getInfoLog(shaderID));
            }

            glAttachObjectARB(programID, shaderID);
        } catch (RuntimeException e) {
            glDeleteObjectARB(shaderID);
            throw e;
        }
        return this;
    }

    /**
     * Call this once you have bound your frag and vert shader.
     */
    public ShaderProgram validate() {
        glLinkProgramARB(programID);
        if (glGetObjectParameteriARB(programID, GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            throw new RuntimeException("Error linking program: " + getInfoLog(programID));
        }

        glValidateProgramARB(programID);
        if (glGetObjectParameteriARB(programID, GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            throw new RuntimeException("Error validating program: " + getInfoLog(programID));
        }
        return this;
    }

    public static String asString(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bin = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = bin.readLine()) != null) {
            sb.append(line).append('\n');
        }
        stream.close();
        return sb.toString();
    }

    private static String getInfoLog(int shaderID) {
        return glGetInfoLogARB(shaderID, glGetObjectParameteriARB(shaderID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public int getUniformLoc(String name) {
        return ARBShaderObjects.glGetUniformLocationARB(programID, name);
    }

    public int getAttribLoc(String name) {
        return ARBVertexShader.glGetAttribLocationARB(programID, name);
    }

    public void uniformTexture(String name, int textureIndex) {
        ARBShaderObjects.glUniform1iARB(getUniformLoc(name), textureIndex);
    }

    public void glVertexAttributeMat4(int loc, Matrix4f matrix) {
        ARBVertexShader.glVertexAttrib4fARB(loc, matrix.m00, matrix.m01, matrix.m02, matrix.m03);
        ARBVertexShader.glVertexAttrib4fARB(loc + 1, matrix.m10, matrix.m11, matrix.m12, matrix.m13);
        ARBVertexShader.glVertexAttrib4fARB(loc + 2, matrix.m20, matrix.m21, matrix.m22, matrix.m23);
        ARBVertexShader.glVertexAttrib4fARB(loc + 3, matrix.m30, matrix.m31, matrix.m32, matrix.m33);
    }

    /**
     * This method will completely remove the shader.
     */
    public void cleanup() {
        ops.clear();
        ARBShaderObjects.glDeleteObjectARB(programID);
    }
}
