package dev.atomixsoft.render.camera;

import dev.atomixsoft.GameThread;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FPSCamera extends Camera {

    private final Vector2f m_Rotation;

    private final Vector3f m_Direction;
    private final Vector3f m_Up;

    private float m_Sensitivity; // Adjust sensitivity as needed

    public FPSCamera() {
        super();
        getProjection().perspective(70.0f, GameThread.WinSize().x / GameThread.WinSize().y, 0.01f, 100f);
        m_Rotation = new Vector2f();

        m_Direction = new Vector3f(0, 0, -1);
        m_Up = new Vector3f(0, 1, 0);

        m_Sensitivity = 0.1f;
        recalculate();
    }

    public void addRotation(float x, float y) {
        // Adjust the rotation angles with sensitivity
        m_Rotation.y += x * m_Sensitivity; // Yaw (left-right rotation)
        m_Rotation.x += y * m_Sensitivity; // Pitch (up-down rotation)

        // Clamp pitch to avoid flipping the camera
        m_Rotation.x = Math.clamp(-89.0f, 89.0f, m_Rotation.x);

        recalculate();
    }

    private void recalculate() {
        // Calculate new direction vector based on yaw and pitch
        Vector3f front = new Vector3f();
        front.x = (Math.cos(Math.toRadians(m_Rotation.y)) * Math.cos(Math.toRadians(m_Rotation.x)));
        front.y =  Math.sin(Math.toRadians(m_Rotation.x));
        front.z = (Math.sin(Math.toRadians(m_Rotation.y)) * Math.cos(Math.toRadians(m_Rotation.x)));
        m_Direction.set(front).normalize();

        // Recalculate the right and up vectors
        Vector3f right = new Vector3f();
        m_Direction.cross(m_Up, right).normalize(); // Calculate the right vector
        Vector3f up = new Vector3f();
        right.cross(m_Direction, up).normalize(); // Recalculate the up vector

        // Update the view matrix using lookAt
        getView().identity();
        getView().lookAt(getPosition(), new Vector3f(getPosition()).add(m_Direction), up);
    }

    public void moveForward(float inc) {
        Vector3f forwardMovement = new Vector3f(m_Direction.x, 0, m_Direction.z).normalize().mul(inc);
        getPosition().add(forwardMovement); // Move forward in the XZ plane
        recalculate();
    }

    public void moveBackward(float inc) {
        Vector3f backwardMovement = new Vector3f(m_Direction.x, 0, m_Direction.z).normalize().mul(inc);
        getPosition().sub(backwardMovement); // Move backward in the XZ plane
        recalculate();
    }

    public void moveLeft(float inc) {
        Vector3f left = new Vector3f();
        m_Direction.cross(m_Up, left).normalize().mul(inc); // Calculate left movement in the XZ plane
        left.y = 0; // Prevent vertical movement
        getPosition().sub(left);
        recalculate();
    }

    public void moveRight(float inc) {
        Vector3f right = new Vector3f();
        m_Direction.cross(m_Up, right).normalize().mul(inc); // Calculate right movement in the XZ plane
        right.y = 0; // Prevent vertical movement
        getPosition().add(right);
        recalculate();
    }

    public void moveUp(float inc) {
        getPosition().add(new Vector3f(m_Up).mul(inc));
        recalculate();
    }

    public void moveDown(float inc) {
        getPosition().sub(new Vector3f(m_Up).mul(inc));
        recalculate();
    }

    public void setSensitivity(float sensitivity) {
        m_Sensitivity = sensitivity;
    }

    public void setPosition(float x, float y, float z) {
        getPosition().set(x, y, z);
        recalculate();
    }

    public void setRotation(float x, float y) {
        m_Rotation.set(x, y);
        recalculate();
    }
}
