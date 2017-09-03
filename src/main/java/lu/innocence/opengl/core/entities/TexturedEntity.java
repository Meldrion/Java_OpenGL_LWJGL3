package lu.innocence.opengl.core.entities;

import lu.innocence.opengl.core.Renderer;
import lu.innocence.opengl.core.maths.Vector2f;
import lu.innocence.opengl.core.maths.Vector3f;
import lu.innocence.opengl.core.maths.Vector4f;
import lu.innocence.opengl.core.models.Texture;
import lu.innocence.opengl.core.shaders.EntityShader;
import lu.innocence.opengl.core.shaders.ShaderProgram;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TexturedEntity {

    private static final Logger LOGGER = LogManager.getLogger(TexturedEntity.class);
    private Texture model;
    private Vector3f position;
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    private float scale;
    private Vector4f uvCoords;
    private Vector4f color;

    public TexturedEntity(Texture texture) {
        this(texture, new Vector3f(0, 0, 0), 0, 0, 0, 1);
    }

    public TexturedEntity(Texture model, Vector3f position,
                          float rotationX, float rotationY, float rotationZ,
                          float scale) {

        this.color = new Vector4f(1,1,1,1);
        this.uvCoords = new Vector4f(0,0,1,1);
        this.setModel(model);
        this.setPosition(position);
        this.setRotationX(rotationX);
        this.setRotationY(rotationY);
        this.setRotationZ(rotationZ);
        this.setScale(scale);

        LOGGER.info("Created TexturedEntity");
    }

    public Texture getModel() {
        return model;
    }

    public void setModel(Texture model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
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
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float rx, float ry, float rz) {
        this.rotationX += rx;
        this.rotationY += ry;
        this.rotationZ += rz;
    }

    public void setUVCoords(int x1,int y1,int x2,int y2) {
        Vector2f dimension = this.model.getDimension();
        this.uvCoords.set(x1 / dimension.getX(),
                          y1 / dimension.getY(),
                          x2 / dimension.getX(),
                          y2 / dimension.getY());
    }

    public Vector4f getUVCoords() {
        return this.uvCoords;
    }

    public Vector2f getDimension() {

        float uDelta = this.uvCoords.getZ() - this.uvCoords.getX();
        float vDelta = this.uvCoords.getW() - this.uvCoords.getY();

        return new Vector2f(this.getModel().getDimension().getX() * uDelta,
                            this.getModel().getDimension().getY() * vDelta);
    }

    public void setColor(Vector4f color) {
        this.color = new Vector4f(color);
    }

    public void bind(EntityShader shader,Renderer renderer) {
        shader.bind();
        this.bindVertexArray(renderer,shader);
        renderer.bindTexture(this);
    }

    public void unbindVertexArray(Renderer renderer, EntityShader shader) {
        renderer.unbindVertexArray();
        shader.unbind();
    }

    public void bindVertexArray(Renderer renderer, EntityShader shader) {
        shader.bind();
        renderer.bindVertexArray(this);
    }

    public void unbind(Renderer renderer, EntityShader shader) {
        renderer.unbindTexture();
        shader.setTextureDisabled(true);
        this.unbindVertexArray(renderer,shader);
        shader.unbind();
    }

    public void draw(Renderer renderer, EntityShader shader) {
        shader.setColorValue(color);
        renderer.render(this, shader);
    }

}
