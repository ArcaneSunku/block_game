package dev.atomixsoft.game.world;

import org.joml.Vector3f;

public class Chunk {

    private final byte[] m_BlockIds;
    private final Vector3f m_Position;

    public Chunk() {
        m_BlockIds = new byte[16 * 16 * 16];
        m_Position = new Vector3f(0f);
    }

    public byte[] getIds() {
        return m_BlockIds;
    }

    public Vector3f getPosition() {
        return m_Position;
    }

}
