package buildcraft.api.transport.pipe;

import net.minecraft.tileentity.TileEntity;

/** Fired when the state of a pipe's tile entity changes. Listen for subclasses, not this one! */
public abstract class PipeEventTileState extends PipeEvent {
    PipeEventTileState(IPipeHolder holder) {
        super(holder);
    }

    /** Fired in {@link TileEntity#invalidate()} */
    public static class Invalidate extends PipeEventTileState {
        public Invalidate(IPipeHolder holder) {
            super(holder);
        }
    }

    /** Fired in {@link TileEntity#validate()} */
    public static class Validate extends PipeEventTileState {
        public Validate(IPipeHolder holder) {
            super(holder);
        }
    }

    /** Fired in {@link TileEntity#onChunkUnload()} */
    public static class ChunkUnload extends PipeEventTileState {
        public ChunkUnload(IPipeHolder holder) {
            super(holder);
        }
    }
}
