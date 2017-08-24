package lu.innocence.opengl.core.shaders;


import lu.innocence.opengl.core.exception.ShaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram implements ShaderInterface {

    private static final Logger LOGGER = LogManager.getLogger(ShaderProgram.class);

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram(String vertexShader,String fragmentShader) throws ShaderException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new ShaderException("Could not create Shader");
        }
        this.vertexShaderId = this.createShader(vertexShader,GL_VERTEX_SHADER);
        this.fragmentShaderId = this.createShader(fragmentShader,GL_FRAGMENT_SHADER);
        glAttachShader(programId, this.vertexShaderId);
        glAttachShader(programId, this.fragmentShaderId);
        bindAttributes();
        link();
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

    public void link() throws ShaderException {
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

    public void bindAttribute(int attribute,String variableName) {
        GL20.glBindAttribLocation(this.programId,attribute,variableName);
    }

    public void setUniformFloat(String uniformName,float value) {
        int loc = GL20.glGetUniformLocation(this.programId, uniformName);
        GL20.glUniform1f(loc,value);
    }

    public void setUniformVec4(String uniformName,float x, float y, float z,  float a) {
        int loc = GL20.glGetUniformLocation(this.programId,uniformName);
        GL20.glUniform4f(loc,x,y,z,a);
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