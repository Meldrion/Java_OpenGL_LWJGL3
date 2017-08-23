package lu.innocence.opengl;

import lu.innocence.opengl.core.*;
import lu.innocence.opengl.core.models.RawModel;
import lu.innocence.opengl.core.models.TexturedModel;
import lu.innocence.opengl.core.shaders.ShaderProgram;
import lu.innocence.opengl.core.shaders.StaticShader;
import lu.innocence.opengl.core.texture.ModelTexture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.net.URL;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private Main() {

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
            private TexturedModel texturedModel;
            private ShaderProgram shaderProgram;

            @Override
            public void create() throws Exception {
                this.loader = new Loader();
                RawModel model = loader.loadToVAO(vertices,textureCoords,indices);

                String fileName = "textures/chara.png";
                URL url = Main.class.getClassLoader().getResource(fileName);
                if (url == null)
                    throw new FileNotFoundException(fileName);

                ModelTexture texture = new ModelTexture(loader.loadTexture
                        (url.toString()));

                this.texturedModel = new TexturedModel(model,texture);

                this.shaderProgram = new StaticShader();
                this.shaderProgram.link();

                LOGGER.info("Creating and Loading worked fine");

            }

            @Override
            public void render(Renderer renderer) {
                this.shaderProgram.bind();
                renderer.render(this.texturedModel);
            }

            @Override
            public void destroy() {
                this.loader.cleanUp();
            }

        }, 1280, 800);
    }

    public static void main(String[] args) {
        new Main();
    }

}
