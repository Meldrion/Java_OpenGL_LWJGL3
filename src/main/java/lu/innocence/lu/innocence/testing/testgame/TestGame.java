package lu.innocence.lu.innocence.testing.testgame;

import lu.innocence.opengl.Main;
import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.Loader;
import lu.innocence.opengl.core.RenderInterface;
import lu.innocence.opengl.core.Renderer;
import lu.innocence.opengl.core.entities.TexturedEntity;
import lu.innocence.opengl.core.maths.Vector2f;
import lu.innocence.opengl.core.maths.Vector3f;
import lu.innocence.opengl.core.maths.Vector4f;
import lu.innocence.opengl.core.models.Texture;
import lu.innocence.opengl.core.shaders.EntityShader;
import lu.innocence.opengl.core.texture.ModelTexture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;

@SuppressWarnings("Duplicates")
public class TestGame implements RenderInterface {

    private static final Logger LOGGER = LogManager.getLogger(TestGame.class);
    private final DisplayManager displayManager;

    private Loader loader;
    private TexturedEntity texturedEntity;
    private EntityShader shaderProgram;
    private Vector4f colorVector;

    public TestGame(DisplayManager displayManager) {
        this.displayManager = displayManager;
    }

    @Override
    public void create() throws Exception {
        this.loader = new Loader();
        String fileName = "textures/cave.png";
        URL url = Main.class.getClassLoader().getResource(fileName);
        if (url == null)
            throw new FileNotFoundException(fileName);
        File file = Paths.get(url.toURI()).toFile();
        int[] textureDetails = loader.loadTexture
                (file.getAbsolutePath());
        ModelTexture texture = new ModelTexture(textureDetails[0], textureDetails[1], textureDetails[2]);
        Texture texturedModel = new Texture(loader, texture);
        this.texturedEntity = new TexturedEntity(texturedModel);
        this.texturedEntity.setScale(1.0f);
        this.texturedEntity.setPosition(new Vector3f(0f, 0f, 0));
        this.texturedEntity.setUVCoords(0, 0, 32, 32);
        this.colorVector = new Vector4f(1f, 1, 1, 1);
        this.shaderProgram = new EntityShader();
        LOGGER.info("Creating and Loading worked fine");
    }

    @Override
    public void render(Renderer renderer) {
        this.shaderProgram.bind();
        this.shaderProgram.setGrayScaleValue(0f);
        this.shaderProgram.setColorValue(this.colorVector);
        this.shaderProgram.setTextureDisabled(false);

        renderer.bindVertexArray(this.texturedEntity);
        renderer.bindTexture(this.texturedEntity);

        float scale = 1f;
        for (int i = 0; i < DisplayManager.getWindowSize().getX() / (32 * scale); i++) {
            for (int j = 0; j < DisplayManager.getWindowSize().getY() / (32 * scale); j++) {

                for (int z = 0; z < 4; z++) {
                    switch (z) {
                        case 0:
                            this.texturedEntity.setUVCoords(0, 0, 32, 32);
                            break;
                        case 1:
                            this.texturedEntity.setUVCoords(32, 0, 64, 32);
                            break;
                        case 2:
                            this.texturedEntity.setUVCoords(32, 32, 64, 64);
                            break;
                        case 3:
                            this.texturedEntity.setUVCoords(64, 32, 96, 64);
                            break;
                        default:
                            break;
                    }

                    this.texturedEntity.setScale(scale);
                    this.texturedEntity.setPosition(new Vector3f(i * 32.0f * scale, j * 32.0f * scale, 0f));
                    renderer.render(this.texturedEntity, this.shaderProgram);
                }

            }
        }

        renderer.unbindTexture();
        this.shaderProgram.setTextureDisabled(true);

        Vector2f position = displayManager.getCursorPosition();
        this.texturedEntity.setPosition(new Vector3f((int) (position.getX() / (32 * scale)) * (32 * scale),
                (int) (position.getY() / (32 * scale)) * (32 * scale),
                0));

        this.shaderProgram.setGrayScaleValue(0f);
        this.shaderProgram.setColorValue(new Vector4f(1, 0, 0, 0.5f));
        renderer.render(texturedEntity, this.shaderProgram);

        this.shaderProgram.unbind();

        renderer.unbindVertexArray();
    }

    @Override
    public void destroy() {
        this.loader.cleanUp();
    }


}
