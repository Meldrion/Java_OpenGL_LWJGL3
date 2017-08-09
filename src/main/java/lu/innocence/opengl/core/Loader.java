package lu.innocence.opengl.core;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos;
    private List<Integer> vbos;

    public Loader() {
        this.vaos = new ArrayList<>();
        this.vbos = new ArrayList<>();
    }

    public RawModel loadToVAO(float[] positions) {
        int voaID = createVAO();
        storeDataInAttributeList(0,positions);
        unbindVAO();
        return new RawModel(voaID,positions.length / 3);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        this.vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer = storeDataIndFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,3, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataIndFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUp() {
        for (int vao : this.vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : this.vbos) {
            GL30.glDeleteVertexArrays(vbo);
        }
    }

}