package codechicken.lib.world;

/**
 * Provides a callback for tile entities when a chunk is loaded, as an alternative to validate when the chunk hasn't been added to the world.
 * To hook all world join/seperate events. Use this, TileEntity.validate with a worldObj.blockExists check, TileEntity.onChunkUnload and TileEntity.invalidate
 * Be sure to call TileChunkLoadHook.init() from your mod during initialisation
 * You could easily implement this in your own mod, but providing it here reduces the number of times the chunkTileEntityMap needs to be iterated
 */
public interface IChunkLoadTile {

    void onChunkLoad();
}
