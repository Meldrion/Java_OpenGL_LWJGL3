package lu.innocence.opengl.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Fabien Steines on 02.09.2017.
 */
public class FrameHandler {

    private static final Logger LOGGER = LogManager.getLogger(FrameHandler.class);
    private static final int MIN_RENDER_TIME = 16;

    private FrameHandler() {}

    public static void watchRenderTime(long delta) {
        // Dont use to much CPU
        if (delta < MIN_RENDER_TIME) {
            try {
                Thread.sleep(MIN_RENDER_TIME - delta);
            } catch (InterruptedException e) {
                LOGGER.error(e);
                Thread.currentThread().interrupt();
            }
        }
        // Frame Skipping
        if (delta > MIN_RENDER_TIME) {
            try {
                Thread.sleep(delta - MIN_RENDER_TIME);
            } catch (InterruptedException e) {
                LOGGER.error(e);
                Thread.currentThread().interrupt();
            }
        }

    }

}
