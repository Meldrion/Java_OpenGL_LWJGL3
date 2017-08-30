package lu.innocence.opengl.core.maths;

import lu.innocence.opengl.core.DisplayManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Maths {

    private static final Logger LOGGER = LogManager.getLogger(Maths.class);

    private Maths() {}

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz,float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Vector2f displaySize = DisplayManager.getWindowSize();
        Vector3f screenCoords = new Vector3f((translation.x / displaySize.getX()) * 2
                                                + (256f / displaySize.getX()) - 1f,
                                             (-translation.y / displaySize.getY()) * 2
                                                - (256f / displaySize.getY()) + 1f,
                                                translation.z);
        LOGGER.info(screenCoords);
        matrix.translate(screenCoords,matrix);
        matrix.rotate((float)Math.toRadians(rx),new Vector3f(1,0,0),matrix);
        matrix.rotate((float)Math.toRadians(ry),new Vector3f(0,1,0),matrix);
        matrix.rotate((float)Math.toRadians(rz),new Vector3f(0,0,1),matrix);
        matrix.scale(new Vector3f(scale , scale ,scale));
        return matrix;
    }

}
