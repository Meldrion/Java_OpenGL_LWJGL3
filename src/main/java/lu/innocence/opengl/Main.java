package lu.innocence.opengl;

import lu.innocence.opengl.core.*;

public class Main {

    private Main() {

        float[] vertices = {
                // Left bottom triangle
                -0.5f,  0.5f,  0f,
                -0.5f, -0.5f,  0f,
                 0.5f, -0.5f,  0f,
                // Right top triangle
                 0.5f, -0.5f,  0f,
                 0.5f,  0.5f,  0f,
                -0.5f,  0.5f,  0f
        };

        DisplayManager displayManager = new DisplayManager();
        displayManager.run(new RenderInterface() {

            private Loader loader;
            private RawModel model;

            @Override
            public void create() {
                this.loader = new Loader();
                this.model = loader.loadToVAO(vertices);
            }

            @Override
            public void render(Renderer renderer) {
                renderer.render(this.model);
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
