package lu.innocence.opengl;

import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.Loader;
import lu.innocence.opengl.core.RenderInterface;
import lu.innocence.opengl.core.Renderer;
import lu.innocence.opengl.core.entities.Entity;
import lu.innocence.opengl.core.exception.GLFWException;
import lu.innocence.opengl.core.maths.Vector3f;
import lu.innocence.opengl.core.maths.Vector4f;
import lu.innocence.opengl.core.models.TexturedModel;
import lu.innocence.opengl.core.shaders.StaticShader;
import lu.innocence.opengl.core.texture.ModelTexture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private Main() throws GLFWException {

        DisplayManager displayManager = new DisplayManager();
        displayManager.run(new RenderInterface() {

            private Loader loader;
            private Entity entity;
            private StaticShader shaderProgram;
            private Vector4f colorVector;

            @Override
            public void create() throws Exception {
                this.loader = new Loader();
                String fileName = "textures/chara.png";
                URL url = Main.class.getClassLoader().getResource(fileName);
                if (url == null)
                    throw new FileNotFoundException(fileName);
                File file = Paths.get(url.toURI()).toFile();
                int[] textureDetails = loader.loadTexture
                        (file.getAbsolutePath());
                ModelTexture texture = new ModelTexture(textureDetails[0], textureDetails[1], textureDetails[2]);
                TexturedModel texturedModel = new TexturedModel(loader, texture);
                this.entity = new Entity(texturedModel);
                this.entity.setScale(1.0f);
                this.entity.setPosition(new Vector3f(32.0f, 32.0f, 0));
                this.entity.setUVCoords(128,0,160,48);
                this.colorVector = new Vector4f(1, 1, 1, 1);
                this.shaderProgram = new StaticShader();
                LOGGER.info("Creating and Loading worked fine");
            }

            @Override
            public void render(Renderer renderer) {
                this.shaderProgram.bind();
                this.shaderProgram.setGrayScaleValue(0.75f);
                this.shaderProgram.setColorValue(this.colorVector);
                //this.shaderProgram.
                //this.entity.increasePosition(1,1,0);
                renderer.render(this.entity, this.shaderProgram);
                this.shaderProgram.unbind();
            }

            @Override
            public void destroy() {
                this.loader.cleanUp();
            }

        }, 1280, 800,"OpenGL Test");
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (GLFWException e) {
            LOGGER.error(e);
            System.exit(-1);
        }
    }

}
