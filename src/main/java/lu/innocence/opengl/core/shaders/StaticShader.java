package lu.innocence.opengl.core.shaders;

import lu.innocence.opengl.Utils;

public class StaticShader extends ShaderProgram implements ShaderInterface {

    public StaticShader() throws Exception {
        super();
        this.createVertexShader(Utils.loadResource("/shaders/vertex.vert"));
        this.createFragmentShader(Utils.loadResource("/shaders/fragment.frag"));
    }

    public void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
    }


}
