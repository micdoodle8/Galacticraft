package codechicken.lib.render;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.ARBShaderObjects.*;

public class ShaderProgram
{
    int programID;
    
    public ShaderProgram()
    {
        programID = glCreateProgramObjectARB();
        if(programID == 0)
            throw new RuntimeException("Unable to allocate shader program object.");
    }
    
    public void attach(int shaderType, String resource)
    {
        InputStream stream = ShaderProgram.class.getResourceAsStream(resource);
        if(stream == null)
            throw new RuntimeException("Unable to locate resource: "+resource);
        
        attach(shaderType, stream);
    }
    
    public void use()
    {
        glUseProgramObjectARB(programID);
    }
    
    public static void restore()
    {
        glUseProgramObjectARB(0);
    }
    
    public void link()
    {
        glLinkProgramARB(programID);
        if(glGetObjectParameteriARB(programID, GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
            throw new RuntimeException("Error linking program: "+getInfoLog(programID));
        
        glValidateProgramARB(programID);
        if(glGetObjectParameteriARB(programID, GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
            throw new RuntimeException("Error validating program: "+getInfoLog(programID));
        
        use();
        onLink();
        restore();
    }
    
    public void attach(int shaderType, InputStream stream)
    {
        if(stream == null)
            throw new RuntimeException("Invalid shader inputstream");
        
        int shaderID = 0;
        try
        {
            shaderID = glCreateShaderObjectARB(shaderType);
            if(shaderID == 0)
                throw new RuntimeException("Unable to allocate shader object.");
            
            try
            {
                glShaderSourceARB(shaderID, asString(stream));
            }
            catch(IOException e)
            {
                throw new RuntimeException("Error reading inputstream.", e);
            }
            
            glCompileShaderARB(shaderID);
            if(glGetObjectParameteriARB(shaderID, GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error compiling shader: "+getInfoLog(shaderID));
            
            glAttachObjectARB(programID, shaderID);
        }
        catch(RuntimeException e)
        {
            glDeleteObjectARB(shaderID);
            throw e;
        }    
    }

    public static String asString(InputStream stream) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader bin = new BufferedReader(new InputStreamReader(stream));
        String line;
        while((line = bin.readLine()) != null)
            sb.append(line).append('\n');
        stream.close();
        return sb.toString();
    }
    
    private static String getInfoLog(int shaderID)
    {
        return glGetInfoLogARB(shaderID, glGetObjectParameteriARB(shaderID, GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
    
    public int getUniformLoc(String name)
    {
        return ARBShaderObjects.glGetUniformLocationARB(programID, name);
    }

    public int getAttribLoc(String name)
    {
        return ARBVertexShader.glGetAttribLocationARB(programID, name);
    }

    public void uniformTexture(String name, int textureIndex)
    {
        ARBShaderObjects.glUniform1iARB(getUniformLoc(name), textureIndex);
    }
    
    public void onLink()
    {
        
    }
    
    public void glVertexAttributeMat4(int loc, Matrix4f matrix)
    {
        ARBVertexShader.glVertexAttrib4fARB(loc  , matrix.m00, matrix.m01, matrix.m02, matrix.m03);
        ARBVertexShader.glVertexAttrib4fARB(loc+1, matrix.m10, matrix.m11, matrix.m12, matrix.m13);
        ARBVertexShader.glVertexAttrib4fARB(loc+2, matrix.m20, matrix.m21, matrix.m22, matrix.m23);
        ARBVertexShader.glVertexAttrib4fARB(loc+3, matrix.m30, matrix.m31, matrix.m32, matrix.m33);
    }
}
