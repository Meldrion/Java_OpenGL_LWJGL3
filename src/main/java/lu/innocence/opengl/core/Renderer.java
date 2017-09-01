package lu.innocence.opengl.core;

import lu.innocence.opengl.core.entities.Entity;
import lu.innocence.opengl.core.maths.Maths;
import lu.innocence.opengl.core.maths.Matrix4f;
import lu.innocence.opengl.core.models.RawModel;
import lu.innocence.opengl.core.models.TexturedModel;
import lu.innocence.opengl.core.shaders.StaticShader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;


public class Renderer {

    private static final Logger LOGGER = LogManager.getLogger(Renderer.class);

    public Renderer() {
        String openGLVersion = glGetString(GL_VERSION);
        LOGGER.info(openGLVersion);
        this.init();
    }

    private void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void prepare() {
        GL11.glClearColor(1,0,0,1);
    }

    public void render(Entity entity, StaticShader staticShader) {
        TexturedModel texturedModel = entity.getModel();
        RawModel model = texturedModel.getRawModel();

        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f matrix4f = Maths.createTransformationMatrix(entity);
        staticShader.setUVCoordinates(entity.getUVCoords());
        staticShader.setTransformationValue(matrix4f);


        GL11.glDrawElements(GL11.GL_TRIANGLES,model.getVertexCount(),GL11.GL_UNSIGNED_INT,0);

        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

    }

    public void bindTexture(Entity entity) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,entity.getModel().getModelTexture().getTextureId());
    }

    public void unbindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
    }

}
