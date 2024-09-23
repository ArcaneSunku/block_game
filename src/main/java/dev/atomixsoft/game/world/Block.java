package dev.atomixsoft.game.world;

import dev.atomixsoft.render.Texture;
import dev.atomixsoft.render.render3D.Model;
import org.joml.Vector3f;

public class Block {

    private BlockData m_Data;
    private Model m_Model;

    public Block(BlockData data) {
        m_Data = data;
    }

    public Block() {
        this(new BlockData());
    }

    public byte getId() {
        return m_Data.id;
    }

    public Model getModel() {
        return m_Model;
    }

    public void setModel(Model model) {
        m_Model = model;
    }

    public void setTexture(Texture texture) {
        m_Model.setTexture(texture);
    }

    public Vector3f getPosition() {
        return m_Model.getPosition();
    }

    public Vector3f getRotation() {
        return m_Model.getRotation();
    }

    public static class BlockData {
        public byte id;
        public byte solid;
        public byte breakable;
        public byte lightSource;
    }

}
