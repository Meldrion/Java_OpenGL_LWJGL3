package lu.innocence.opengl.core.shaders;

import lu.innocence.opengl.Utils;

public class StaticShader extends ShaderProgram implements ShaderInterface {

    public StaticShader() throws Exception {
        super(Utils.loadResource("/shaders/vertex.vert"),
              Utils.loadResource("/shaders/fragment.frag"));
    }

    public void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2,"greyscale");
    }

    public void setGrayScaleValue(float value) {
        super.setUniformFloat("greyscale",value);
    }

    public void setColorValue(float r, float g, float b, float a) {
        super.setUniformVec4("color_manipulation" ,r,g,b,a);
    }

}
