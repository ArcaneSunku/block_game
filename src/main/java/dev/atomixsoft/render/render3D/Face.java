package dev.atomixsoft.render.render3D;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A Face is a collection of vertex and texture index data. This will help us differentiate all parts of a model from each other.
 * It is important to remember that that the Mesh is built from the bottom left in a counter-clockwise manner.
 * Supplied and adjusted data should have this kept in mind.</p>
 */
public class Face {

    private final List<Vector3f> m_Positions;
    private final List<Vector2f> m_TextureCoords;

    public Face() {
        m_Positions = new ArrayList<>();
        m_TextureCoords = new ArrayList<>();
    }

    public void pushPosition(Vector3f position) {
        m_Positions.add(position);
    }

    public void pushTextureCoords(Vector2f coords) {
        m_TextureCoords.add(coords);
    }

    public void addPosition(int index, Vector3f position) {
        if(m_Positions.get(index) != null) m_Positions.set(index, position);
        else m_Positions.add(index, position);
    }

    public void addTextureCoords(int index, Vector2f coords) {
        if(m_TextureCoords.get(index) != null) m_TextureCoords.get(index).set(coords);
        else m_TextureCoords.add(index, coords);
    }

    public List<Vector3f> getPositionData() {
        return m_Positions;
    }

    public List<Vector2f> getTextureData() {
        return m_TextureCoords;
    }

    public Vector3f getPosition(int index) {
        return getPositionData().get(index);
    }

    public Vector2f getTextureCoords(int index) {
        return getTextureData().get(index);
    }

}
