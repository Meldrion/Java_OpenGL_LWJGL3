package lu.innocence.opengl.core;


import lu.innocence.opengl.core.models.RawModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private static final Logger LOGGER = LogManager.getLogger(Loader.class);

    private List<Integer> vaos;
    private List<Integer> vbos;
    private List<Integer> textures;

    public Loader() {
        this.vaos = new ArrayList<>();
        this.vbos = new ArrayList<>();
        this.textures = new ArrayList<>();
    }

    public RawModel loadToVAO(float[] positions,float[] textureCoords, int[] indices) {
        int voaID = createVAO();
        bindIndecesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        unbindVAO();
        return new RawModel(voaID,indices.length);
    }

    public int[] loadTexture(String filename,boolean internal) {
        int[] texture;
        try {
            texture = TextureLoader.loadTexture(filename,internal);
        } catch (IOException e) {
            LOGGER.error(e);
            int[] returnValue = new int[0];
            returnValue[0] = 0;
            return returnValue;
        }
        this.textures.add(texture[0]);
        return texture;
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        this.vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber,int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer = storeDataIndFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }

    private void bindIndecesBuffer(int[] indeces) {
        int vboId = GL15.glGenBuffers();
        this.vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboId);
        IntBuffer buffer = storeDataIndIntBuffer(indeces);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
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

    private IntBuffer storeDataIndIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUp() {

        for (int vao : this.vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo : this.vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        for (int texture : this.textures) {
            GL11.glDeleteTextures(texture);
        }
    }

}
