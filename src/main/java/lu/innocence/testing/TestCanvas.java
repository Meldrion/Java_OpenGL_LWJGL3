package lu.innocence.testing;

import lu.innocence.opengl.SWTCanvas;
import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.Loader;
import lu.innocence.opengl.core.Renderer;
import lu.innocence.opengl.core.entities.TexturedEntity;
import lu.innocence.opengl.core.exception.ShaderException;
import lu.innocence.opengl.core.maths.Vector2f;
import lu.innocence.opengl.core.maths.Vector3f;
import lu.innocence.opengl.core.maths.Vector4f;
import lu.innocence.opengl.core.models.Texture;
import lu.innocence.opengl.core.shaders.EntityShader;
import lu.innocence.opengl.core.texture.ModelTexture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Fabien Steines on 02.09.2017.
 */
@SuppressWarnings("Duplicates")
public class TestCanvas extends SWTCanvas {

    private Loader loader;
    private TexturedEntity texturedEntity;
    private EntityShader shaderProgram;
    private Vector4f colorVector;
    private Vector2f mousePosition;

    public TestCanvas(Display display, Composite parent)
            throws URISyntaxException, IOException, ShaderException {
        super(display, parent);
        this.mousePosition = new Vector2f(0,0);
        this.loader = new Loader();

        int[] textureDetails = loader.loadTexture("/textures/cave.png",true);

        ModelTexture texture = new ModelTexture(textureDetails[0], textureDetails[1], textureDetails[2]);
        Texture texturedModel = new Texture(loader, texture);
        this.texturedEntity = new TexturedEntity(texturedModel);
        this.texturedEntity.setScale(1.0f);
        this.texturedEntity.setPosition(new Vector3f(0f, 0f, 0));
        this.texturedEntity.setUVCoords(0, 0, 32, 32);
        this.colorVector = new Vector4f(1f, 1, 1, 1);
        this.shaderProgram = new EntityShader();

        this.getCanvasHandle().addListener(SWT.MouseMove, e -> {

            int mapX = (e.x / 32) * 32;
            int mapY = (e.y / 32) * 32;
            this.mousePosition.set(mapX,mapY);
        });
    }

    @Override
    public void render(long delta,Renderer renderer) {

        this.texturedEntity.bind(shaderProgram,renderer);
        this.shaderProgram.setGrayScaleValue(0f);
        this.shaderProgram.setColorValue(this.colorVector);
        this.shaderProgram.setTextureDisabled(false);

        float scale = 1f;
        for (int i = 0; i < DisplayManager.getWindowSize().getX() / (32 * scale); i++) {
            for (int j = 0; j < DisplayManager.getWindowSize().getY() / (32 * scale); j++) {

                for (int z = 0; z < 3; z++) {
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
                    this.texturedEntity.draw(renderer,shaderProgram);
                }
            }
        }

        this.texturedEntity.unbind(renderer,shaderProgram);
        this.texturedEntity.bindVertexArray(renderer,shaderProgram);
        this.texturedEntity.setPosition(new Vector3f((int) (this.mousePosition.getX() / (32 * scale)) * (32 * scale),
                (int) (this.mousePosition.getY() / (32 * scale)) * (32 * scale),
                0));

        this.shaderProgram.setGrayScaleValue(0f);
        this.texturedEntity.setColor(new Vector4f(1,0,0,0.5f));

        this.texturedEntity.draw(renderer,shaderProgram);
        this.texturedEntity.unbindVertexArray(renderer,shaderProgram);

        this.texturedEntity.setColor(new Vector4f(1,1,1,1));
    }

}
