package lu.innocence.opengl;

import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.Loader;
import lu.innocence.opengl.core.RenderInterface;
import lu.innocence.opengl.core.Renderer;
import lu.innocence.opengl.core.entities.Entity;
import lu.innocence.opengl.core.exception.GLFWException;
import lu.innocence.opengl.core.maths.Vector2f;
import lu.innocence.opengl.core.maths.Vector3f;
import lu.innocence.opengl.core.maths.Vector4f;
import lu.innocence.opengl.core.models.RawModel;
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

        float[] vertices = {
                -0.5f,  0.5f, 0.0f, // V0
                -0.5f, -0.5f, 0.0f, // V1
                0.5f,  -0.5f, 0.0f, // V2
                0.5f,  0.5f, 0.0f // V3
        };

        int [] indices = {
                0,1,3, // Top Left Triangle (V0,V1,V3)
                3,1,2 // Bottom Right Triangle (V3,V1,V2)
        };

        float [] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0
        };

        DisplayManager displayManager = new DisplayManager();
        displayManager.run(new RenderInterface() {

            private Loader loader;
            private Entity entity;
            private StaticShader shaderProgram;
            private Vector4f colorVector;

            @Override
            public void create() throws Exception {
                this.loader = new Loader();
                RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
                String fileName = "textures/chara.png";
                URL url = Main.class.getClassLoader().getResource(fileName);
                if (url == null)
                    throw new FileNotFoundException(fileName);
                File file = Paths.get(url.toURI()).toFile();
                ModelTexture texture = new ModelTexture(loader.loadTexture
                        (file.getAbsolutePath()));
                TexturedModel texturedModel = new TexturedModel(model,texture);
                this.entity = new Entity(texturedModel);
                this.entity.setScale(1);
                this.entity.setPosition(new Vector3f(640,400,0));
                this.colorVector = new Vector4f(1,1,1,1);
                this.shaderProgram = new StaticShader();
                LOGGER.info("Creating and Loading worked fine");
            }

            @Override
            public void render(Renderer renderer) {
                this.shaderProgram.bind();
                this.shaderProgram.setGrayScaleValue(0.75f);
                this.shaderProgram.setColorValue(this.colorVector);
                this.entity.increasePosition(1,0,0);
                //this.entity.increaseRotation(0,1,0);

                renderer.render(this.entity,this.shaderProgram);
                this.shaderProgram.unbind();
            }

            @Override
            public void destroy() {
                this.loader.cleanUp();
            }

        }, 1280, 800);
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
