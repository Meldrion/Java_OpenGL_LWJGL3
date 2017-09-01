package lu.innocence.opengl.core.shaders;

import lu.innocence.opengl.Utils;
import lu.innocence.opengl.core.maths.Matrix4f;
import lu.innocence.opengl.core.maths.Vector2f;
import lu.innocence.opengl.core.maths.Vector4f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StaticShader extends ShaderProgram implements ShaderInterface {

    private static final Logger LOGGER = LogManager.getLogger(StaticShader.class);

    private int greyScaleUniformLocation;
    private int colorLocation;
    private int transformationMatrixLocation;
    private int uvLocation;
    private int disableTextureLocation;

    public StaticShader() throws Exception {
        super(Utils.loadResource("/shaders/vertex.vert"),
              Utils.loadResource("/shaders/fragment.frag"));
    }

    public void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
    }

    public void setGrayScaleValue(float value) {
        super.setUniformFloat(this.greyScaleUniformLocation,value);
    }

    public void setColorValue(Vector4f color) {
        super.setUniformVec4(this.colorLocation ,color);
    }

    public void setTransformationValue(Matrix4f matrix) {
        super.setUniformMatrix4(this.transformationMatrixLocation,matrix);
    }

    public void setUVCoordinates(Vector4f uvCoordinates) {
        super.setUniformVec4(this.uvLocation,uvCoordinates);
    }

    public void setTextureDisabled(boolean flag) {
        super.setUniformBoolean(this.disableTextureLocation,flag);
    }

    @Override
    public void getAllUniformLocations() {
        this.greyScaleUniformLocation = this.getUniform("greyscale");
        this.colorLocation = this.getUniform("colorManipulation");
        this.transformationMatrixLocation = this.getUniform("transformationMatrix");
        this.uvLocation = this.getUniform("uvCoordinates");
        this.disableTextureLocation = this.getUniform("disableTexture");
    }
}
