package dev.atomixsoft.event.types.game;

import dev.atomixsoft.event.Event;

import static dev.atomixsoft.game.world.Block.BlockData;

public class BlockRegEvent extends Event {

    private final BlockData m_Data;

    public BlockRegEvent(BlockData data) {
        super("BlockRegister");
        m_Data = data;
    }

    public BlockData getData() {
        return m_Data;
    }
}
