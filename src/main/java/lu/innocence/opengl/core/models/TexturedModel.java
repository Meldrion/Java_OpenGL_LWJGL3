package lu.innocence.opengl.core.models;

import lu.innocence.opengl.core.Loader;
import lu.innocence.opengl.core.maths.Vector2f;
import lu.innocence.opengl.core.texture.ModelTexture;

public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture modelTexture;
    private Vector2f dimension;

    private float[] vertices;

    private int [] indices = {
            0,1,3, // Top Left Triangle (V0,V1,V3)
            3,1,2 // Bottom Right Triangle (V3,V1,V2)
    };

    private float [] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0
    };

    public TexturedModel(Loader loader, ModelTexture modelTexture) {
        this.dimension = new Vector2f(modelTexture.getWidth(),modelTexture.getHeight());
        float w = modelTexture.getWidth() / 1280f * 2;
        float h = modelTexture.getHeight() / 800f * 2;
        this.vertices = new float[]{
                    -0.5f * w,  0.5f * h, 0.0f, // V0
                    -0.5f * w, -0.5f * h, 0.0f, // V1
                    0.5f * w,  -0.5f * h, 0.0f, // V2
                    0.5f * w,  0.5f * h, 0.0f // V3
        };
        this.rawModel = loader.loadToVAO(vertices,textureCoords,indices);
        this.modelTexture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }

    public void setDimension(Vector2f dimension) {
        this.dimension = dimension;
    }

    public Vector2f getDimension() {
        return dimension;
    }
}
