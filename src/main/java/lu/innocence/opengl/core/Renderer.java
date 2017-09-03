package lu.innocence.opengl.core;

import lu.innocence.opengl.core.entities.TexturedEntity;
import lu.innocence.opengl.core.maths.Maths;
import lu.innocence.opengl.core.maths.Matrix4f;
import lu.innocence.opengl.core.models.RawModel;
import lu.innocence.opengl.core.models.Texture;
import lu.innocence.opengl.core.shaders.EntityShader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;


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

    public void render(TexturedEntity texturedEntity, EntityShader entityShader) {

        Matrix4f matrix4f = Maths.createTransformationMatrix(texturedEntity);
        entityShader.setUVCoordinates(texturedEntity.getUVCoords());
        entityShader.setTransformationValue(matrix4f);

        GL11.glDrawElements(GL11.GL_TRIANGLES, texturedEntity.getModel().getRawModel().getVertexCount(),
                GL11.GL_UNSIGNED_INT,0);

    }

    public void bindTexture(TexturedEntity texturedEntity) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedEntity.getModel().getModelTexture().getTextureId());
    }

    public void bindVertexArray(TexturedEntity texturedEntity) {
        Texture texturedModel = texturedEntity.getModel();
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }

    public void unbindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);

    }

    public void unbindVertexArray() {
        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
    }
}
