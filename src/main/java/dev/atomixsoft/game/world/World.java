package dev.atomixsoft.game.world;

import dev.atomixsoft.render.Shader;
import dev.atomixsoft.render.Texture;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {

    private final List<Chunk> m_Chunks;
    private final List<Block> m_CachedBlocks;

    private final Shader m_Shader;
    private final Texture m_White;
    private final Vector3f m_Size;

    public World() {
        m_Chunks = new ArrayList<>();
        m_CachedBlocks = new ArrayList<>();

        m_Shader = new Shader("/shaders/3d");
        byte[] bytes = new byte[3];
        Arrays.fill(bytes, (byte) 255);

        ByteBuffer col = BufferUtils.createByteBuffer(bytes.length);
        col.put(0, bytes);
        m_White = new Texture(1, 1, col);

        m_Shader.createUniform("uTextures");
        m_Shader.createUniform("uProjectionViewModel");

        int[] sampler = new int[32];
        for(var sample = 0; sample < sampler.length; ++sample)
            sampler[sample] = sample;

        m_Shader.setUniformiv("uTextures", sampler);

        m_Size = new Vector3f(3, 3, 3);
    }

    public void draw() {

    }

    public void dispose() {
        m_Shader.dispose();
        m_White.dispose();

        m_Chunks.clear();
        m_CachedBlocks.clear();
    }

    public void generateChunks() {

    }

    public byte getIdFromChunk(Chunk chunk, int x, int y, int z) {
        return chunk.getIds()[16 * 16 * z + 16 * y + x];
    }

}
