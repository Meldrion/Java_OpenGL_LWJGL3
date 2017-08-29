package lu.innocence.opengl.core.entities;

import lu.innocence.opengl.core.maths.Vector3f;
import lu.innocence.opengl.core.models.TexturedModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Entity {

    private static final Logger LOGGER = LogManager.getLogger(Entity.class);
    private TexturedModel model;
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float scale;
    private Vector3f positionScreenCoords;

    public Entity(TexturedModel model) {
        this(model, new Vector3f(0, 0, 0), 0, 0, 0, 1);
    }

    public Entity(TexturedModel model, Vector3f position,
                  float rotationX, float rotationY, float rotationZ,
                  float scale) {
        this.setModel(model);
        this.setPosition(position);
        this.setRotationX(rotationX);
        this.setRotationY(rotationY);
        this.setRotationZ(rotationZ);
        this.setScale(scale);
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.positionScreenCoords = new Vector3f(position);
        position.set((position.x / 1280.0f * 2 + (640.0f / 1280.0f) - 1.0f),
                (-position.y / 800.0f * 2 - (400.0f / 800.0f) + 1.0f),
                position.z);
        this.position = position;
    }

    public float getRotationX() {
        return rotationX;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.setPosition(new Vector3f(this.positionScreenCoords.x + dx,
                this.positionScreenCoords.y + dy,
                this.positionScreenCoords.z + dz));
    }

    public void increaseRotation(float rx, float ry, float rz) {
        this.rotationX += rx;
        this.rotationY += ry;
        this.rotationZ += rz;
    }

}
