package dev.atomixsoft.render.render3D;

import dev.atomixsoft.render.Texture;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Model {

    private final Mesh m_Mesh;
    private final Vector3f m_Position, m_Rotation, m_Scale;

    private Texture m_Texture;

    public Model(Texture texture, Mesh mesh) {
        this(mesh);
        m_Texture = texture;
    }

    public Model(Mesh mesh) {
        m_Mesh = mesh;

        m_Position = new Vector3f();
        m_Rotation = new Vector3f();
        m_Scale = new Vector3f(1f);
    }

    public void draw() {
        if(m_Texture != null)
            m_Texture.bind();

        glBindVertexArray(m_Mesh.getVAO());
        glDrawElements(GL_TRIANGLES, m_Mesh.getIndexCount(), GL_UNSIGNED_INT, MemoryUtil.NULL);
    }

    public void setTexture(Texture texture) {
        m_Texture = texture;
    }

    public Mesh getMesh() {
        return m_Mesh;
    }

    public Vector3f getPosition() {
        return m_Position;
    }

    public Vector3f getRotation() {
        return m_Rotation;
    }

    public Vector3f getScale() {
        return m_Scale;
    }

}
