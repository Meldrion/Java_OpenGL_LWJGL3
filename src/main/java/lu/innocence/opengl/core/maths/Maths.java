package lu.innocence.opengl.core.maths;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz,float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        matrix.translate(translation,matrix);
        matrix.rotate((float)Math.toRadians(rx),new Vector3f(1,0,0),matrix);
        matrix.rotate((float)Math.toRadians(ry),new Vector3f(0,1,0),matrix);
        matrix.rotate((float)Math.toRadians(rz),new Vector3f(1,0,1),matrix);
        matrix.scale(new Vector3f(rx,ry,rz));
        return matrix;
    }

}
