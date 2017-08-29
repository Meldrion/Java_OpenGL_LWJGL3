package lu.innocence.opengl.core.texture;

public class ModelTexture {

    private int textureId;
    private int width;
    private int height;

    public ModelTexture(int textureId,int width,int height) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
    }

    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
