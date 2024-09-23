package dev.atomixsoft.render.render3D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final Logger m_Log;

    private final List<Integer> m_VBOList;
    private final int m_VAO;
    private final int m_IndexCount;
    private final boolean m_Dynamic;

    private float[] m_VertexData;
    private int[] m_IndexData;

    public Mesh(float[] vertices, int[] indices) {
        this(vertices, indices, false);
    }

    public Mesh(float[] vertices, int[] indices, boolean dynamic) {
        m_VertexData = vertices;
        m_IndexData = indices;

        m_Log = LogManager.getLogger();
        m_VBOList = new ArrayList<>();

        m_VAO = glGenVertexArrays();
        m_IndexCount = indices.length;
        m_Dynamic = dynamic;

        initialize(m_VertexData, m_IndexData);
    }

    public void dispose() {
        m_VBOList.forEach(GL15::glDeleteBuffers);
        m_VBOList.clear();
        glDeleteVertexArrays(m_VAO);
    }

    public void debug() {
        m_Log.info("""
                \nMesh {
                    VAO:         {}
                    VBO_Count:   {}
                    Index_Count: {}
                    Dynamic: {}
                }""", m_VAO, m_VBOList.size(), m_IndexCount, m_Dynamic);
    }

    public void setVertexData(float[] vertices) {
        m_VertexData = vertices;
    }

    public void setIndexData(int[] indices) {
        m_IndexData = indices;
    }

    public boolean isDynamic() {
        return m_Dynamic;
    }

    public float[] getVertices() {
        return m_VertexData;
    }

    public int[] getIndices() {
        return m_IndexData;
    }

    public int getVAO() {
        return m_VAO;
    }

    public int getIndexCount() {
        return getIndices().length;
    }

    private void initialize(float[] vertices, int[] indices) {
        glBindVertexArray(m_VAO);

        int vbo = glGenBuffers();
        m_VBOList.add(vbo);

        int dyn = m_Dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW;

        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glBufferData(GL_ARRAY_BUFFER, vertices, dyn);

        if(m_Dynamic) {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
            buffer.put(vertices);

            glBufferSubData(GL_ARRAY_BUFFER, 0, buffer.flip());
            MemoryUtil.memFree(buffer);
        }

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 9 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 9 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 9 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, 1, GL_FLOAT, false, 9 * Float.BYTES, 9 * Float.BYTES);
        glEnableVertexAttribArray(3);

        int ebo = glGenBuffers();
        m_VBOList.add(ebo);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, dyn);

        if(m_Dynamic) {
            IntBuffer buffer = MemoryUtil.memAllocInt(indices.length);
            buffer.put(indices);

            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, buffer.flip());
            MemoryUtil.memFree(buffer);
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

}
