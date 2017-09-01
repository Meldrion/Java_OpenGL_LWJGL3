package lu.innocence.opengl;

import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.exception.GLFWException;
import lu.innocence.testgame.TestGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private Main() throws GLFWException {

        DisplayManager displayManager = new DisplayManager();
        displayManager.run(new TestGame(displayManager),
                1280, 800, "OpenGL Test");
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
