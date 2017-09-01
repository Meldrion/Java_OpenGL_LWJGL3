package lu.innocence.opengl.core.maths;

import lu.innocence.opengl.core.DisplayManager;
import lu.innocence.opengl.core.entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Maths {

    private static final Logger LOGGER = LogManager.getLogger(Maths.class);

    private Maths() {}

    public static Matrix4f createTransformationMatrix(Entity entity) {



        // Create the Matrix for the homogeneous coordinate system
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        Vector2f displaySize = DisplayManager.getWindowSize();

        Vector2f dimension = entity.getDimension();
        Vector3f scaleVector = new Vector3f(entity.getScale() * dimension.getX() / displaySize.getX() * 2,
                                            entity.getScale() * dimension.getY() / displaySize.getY() * 2 ,
                                               entity.getScale());

        Vector3f translation = entity.getPosition();
        Vector3f screenCoords = new Vector3f();

        screenCoords.x = translation.x / displaySize.getX() * 2 + scaleVector.x -   1f;
        screenCoords.y = -translation.y / displaySize.getY() * 2 - scaleVector.y + 1f;
        screenCoords.z = translation.z;

        matrix.translate(screenCoords,matrix);
        matrix.rotate((float)Math.toRadians(entity.getRotationX()),new Vector3f(1,0,0),matrix);
        matrix.rotate((float)Math.toRadians(entity.getRotationY()),new Vector3f(0,1,0),matrix);
        matrix.rotate((float)Math.toRadians(entity.getRotationZ()),new Vector3f(0,0,1),matrix);
        matrix.scale(scaleVector);

        return matrix;
    }

}
