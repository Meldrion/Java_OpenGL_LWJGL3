package lu.innocence.opengl;

import lu.innocence.opengl.core.*;
import lu.innocence.opengl.shaders.StaticShader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        DisplayManager displayManager = new DisplayManager();
        displayManager.run(new RenderInterface() {

            private Loader loader;
            private RawModel model;
            private ShaderProgram shaderProgram;
            private Texture testTexture;

            @Override
            public void create() throws Exception {
                this.loader = new Loader();
                this.model = loader.loadToVAO(vertices,indices);

                this.shaderProgram = new StaticShader();
                this.shaderProgram.link();

                try {
                    URL url = Main.class.getClass().getResource("/textures/cave.png");
                    this.testTexture = new Texture(url.getFile());
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }

            @Override
            public void render(Renderer renderer) {

                this.testTexture.bind();
                this.shaderProgram.bind();
                renderer.render(this.model);
                this.testTexture.unbind();
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
