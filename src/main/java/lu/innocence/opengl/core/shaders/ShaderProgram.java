package lu.innocence.opengl.core.shaders;


import lu.innocence.opengl.core.exception.ShaderException;
import lu.innocence.opengl.core.maths.Matrix4f;
import lu.innocence.opengl.core.maths.Vector2f;
import lu.innocence.opengl.core.maths.Vector4f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram implements ShaderInterface {

    private static final Logger LOGGER = LogManager.getLogger(ShaderProgram.class);

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);;


    ShaderProgram(String vertexShader, String fragmentShader) throws ShaderException {
        this.programId = glCreateProgram();
        if (this.programId == 0) {
            throw new ShaderException("Could not create Shader");
        }
        this.vertexShaderId = this.createShader(vertexShader,GL_VERTEX_SHADER);
        this.fragmentShaderId = this.createShader(fragmentShader,GL_FRAGMENT_SHADER);
        glAttachShader(programId, this.vertexShaderId);
        glAttachShader(programId, this.fragmentShaderId);
        bindAttributes();
        this.link();
        this.getAllUniformLocations();
    }

    private int createShader(String shaderCode, int shaderType) throws ShaderException{
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new ShaderException(String.format("Error creating shader. Type: %s", shaderType));
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new ShaderException(String.format("Error compiling Shader code: %s" ,
                    glGetShaderInfoLog(shaderId, 1024)));
        }

        return shaderId;
    }

    private void link() throws ShaderException {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new ShaderException(String.format("Error linking Shader code: %s" ,
                    glGetProgramInfoLog(programId, 1024)));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            LOGGER.error("Warning validating Shader code: {}" , glGetProgramInfoLog(programId, 1024));
        }
    }

    public abstract void getAllUniformLocations();

    int getUniform(String uniformName) {
        return GL20.glGetUniformLocation(this.programId, uniformName);
    }

    void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(this.programId,attribute,variableName);
    }

    void setUniformFloat(int loc, float value) {
        GL20.glUniform1f(loc,value);
    }

    void setUniformVec2(int loc, Vector2f uniform2) {
        GL20.glUniform2f(loc,uniform2.x,uniform2.y);
    }

    void setUniformVec4(int loc, Vector4f uniform4) {
        GL20.glUniform4f(loc,uniform4.x,uniform4.y,uniform4.z,uniform4.w);
    }

    public void setUniformMatrix4(int loc, Matrix4f matrix4f) {
        matrix4f.store(ShaderProgram.matrixBuffer);
        ShaderProgram.matrixBuffer.flip();
        GL20.glUniformMatrix4fv(loc,false,ShaderProgram.matrixBuffer);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDetachShader(programId,vertexShaderId);
            glDetachShader(programId,fragmentShaderId);
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);
            glDeleteProgram(programId);
        }
    }
}