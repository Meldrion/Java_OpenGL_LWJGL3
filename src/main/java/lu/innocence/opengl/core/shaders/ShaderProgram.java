package lu.innocence.opengl.core.shaders;


import lu.innocence.opengl.core.exception.ShaderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private static final Logger LOGGER = LogManager.getLogger(ShaderProgram.class);

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram() throws ShaderException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new ShaderException("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) throws ShaderException {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws ShaderException {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws ShaderException{
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

        glAttachShader(programId, shaderId);

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