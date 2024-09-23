package dev.atomixsoft.util.game;

import dev.atomixsoft.event.interfaces.EventListener;
import dev.atomixsoft.event.types.game.BlockRegEvent;
import dev.atomixsoft.game.world.Block;
import dev.atomixsoft.render.Texture;
import dev.atomixsoft.render.render3D.Mesh;
import dev.atomixsoft.render.render3D.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static dev.atomixsoft.game.world.Block.*;

public class BlockRegistry implements EventListener<BlockRegEvent> {

    private static final Logger LOG = LogManager.getLogger();
    private static BlockRegistry s_Instance = null;

    private final Map<Byte, BlockData> m_BlockTypes;
    private final Texture m_BlockAtlas;

    public BlockRegistry() {
        m_BlockTypes = new HashMap<>();
        m_BlockAtlas = new Texture("/graphics/blocks.png");
        if(s_Instance == null) s_Instance = this;
    }

    @Override
    public void handleEvent(BlockRegEvent event) {
        if(event.handled || m_BlockTypes.containsKey(event.getData().id)) {
            LOG.error("Block ID is already registered!");
            event.handled = true;
            return;
        }

        m_BlockTypes.put(event.getData().id, event.getData());
        event.handled = true;
    }

    private static Mesh createMesh(Texture texture, float x, float y, float width, float height) {
        float minX = x == 0 ? 0 : x / texture.getWidth(), maxX = minX + (width / texture.getWidth());
        float minY = y == 0 ? 0 : y / texture.getHeight(), maxY = minY + (height / texture.getHeight());

        float[] vertices = {
                // Right/East
                -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, minX, maxY, 1.0f,
                 0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, maxX, maxY, 1.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, maxX, minY, 1.0f,
                -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, minX, minY, 1.0f,
                // Front/North
                 0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, minX, maxY, 1.0f,
                 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, maxX, maxY, 1.0f,
                 0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, maxX, minY, 1.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, minX, minY, 1.0f,
                // Back/South
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, minX, maxY, 1.0f,
                -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, maxX, maxY, 1.0f,
                -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, maxX, minY, 1.0f,
                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, minX, minY, 1.0f,
                // Left/West
                 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, minX, maxY, 1.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, maxX, maxY, 1.0f,
                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, maxX, minY, 1.0f,
                 0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, minX, minY, 1.0f,
                // Top
                -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, minX, maxY, 1.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, maxX, maxY, 1.0f,
                 0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, maxX, minY, 1.0f,
                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, minX, minY, 1.0f,
                // Bottom
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, minX, maxY, 1.0f,
                 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, maxX, maxY, 1.0f,
                 0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, maxX, minY, 1.0f,
                -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, minX, minY, 1.0f,
        };

        // Shouldn't need changing for any whole block
        int[] indices = {
                // Right/East
                0, 1, 2,
                2, 3, 0,
                // Front/North
                4, 5, 6,
                6, 7, 4,
                // Back/South
                8, 9, 10,
                10, 11, 8,
                // Left/West
                12, 13, 14,
                14, 15, 12,
                // Top
                16, 17, 18,
                18, 19, 16,
                // Bottom
                20, 21, 22,
                22, 23, 20,
        };

        return new Mesh(vertices, indices);
    }

    public static Block CreateBlock(byte id) {
        Block block = new Block(s_Instance.m_BlockTypes.get(id));

        Texture atlas = s_Instance.m_BlockAtlas;
        switch (id) {
            case 0 -> block.setModel(new Model(atlas, createMesh(atlas,  0f, 0f, 16f, 16f)));
            case 1 -> block.setModel(new Model(atlas, createMesh(atlas, 16f, 0f, 16f, 16f)));
            case 2 -> block.setModel(new Model(atlas, createMesh(atlas, 32f, 0f, 16f, 16f)));
        }

        return block;
    }

}
