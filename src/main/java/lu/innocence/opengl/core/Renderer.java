package lu.innocence.opengl.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;


public class Renderer {

    private static final Logger LOGGER = LogManager.getLogger(Renderer.class);

    public Renderer() {
        String openGLVersion = glGetString(GL_VERSION);
        LOGGER.info(openGLVersion);
    }

    public void prepare() {
        GL11.glClearColor(0,0,0,1);
    }

    public void render(RawModel model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES,0,model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
