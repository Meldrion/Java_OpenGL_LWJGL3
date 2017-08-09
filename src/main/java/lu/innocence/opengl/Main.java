package lu.innocence.opengl;

import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.Loader;
import lu.innocence.opengl.core.RawModel;

public class Main {

    private Main() {

        Loader loader = new Loader();

        float[] vertices = {
                // Left bottom triangle
                -0.5f,  0.5f,  0f,
                -0.5f, -0.5f, 0f,
                 0.5f,  -0.5f,  0f,
                // Right top triangle
                 0.5f, -0.5f,  0f,
                 0.5f,  0.5f,  0f,
                -0.5f,  0.5f,  0f
        };

        RawModel model = loader.loadToVAO(vertices);

        DisplayManager displayManager = new DisplayManager();
        displayManager.run((renderer) -> {
            //renderer.render(model);
        },1280, 800);
    }

    public static void main(String[] args) {
        new Main();
    }

}
