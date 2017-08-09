package lu.innocence.opengl;

import lu.innocence.opengl.core.*;

public class Main {

    private Main() {

        float[] vertices = {
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
        };


        DisplayManager displayManager = new DisplayManager();
        displayManager.run(new RenderInterface() {

            private Loader loader;
            private RawModel model;
            private ShaderProgram shaderProgram;

            @Override
            public void create() throws Exception {
                this.loader = new Loader();
                this.model = loader.loadToVAO(vertices);

                this.shaderProgram = new ShaderProgram();
                this.shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vert"));
                this.shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.frag"));
                this.shaderProgram.link();
            }

            @Override
            public void render(Renderer renderer) {

                this.shaderProgram.bind();
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
