package dev.atomixsoft.render.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {

    protected final Matrix4f m_Projection;
    protected final Matrix4f m_View;

    protected final Vector3f m_Position;
    protected float m_Zoom;

    public Camera() {
        m_Projection = new Matrix4f();
        m_View = new Matrix4f();
        m_Position = new Vector3f();

        m_Zoom = 1.0f;
    }

    public Vector3f getPosition() {
        return m_Position;
    }

    public Matrix4f getProjection() {
        return m_Projection;
    }

    public Matrix4f getView() {
        return m_View;
    }

    public float getZoom() {
        return m_Zoom;
    }

    public Matrix4f getCombined() {
        return new Matrix4f(m_Projection).mul(m_View);
    }

}
