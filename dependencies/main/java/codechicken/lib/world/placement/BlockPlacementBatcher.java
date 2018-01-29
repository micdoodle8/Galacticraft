package codechicken.lib.world.placement;

import codechicken.lib.world.placement.lighting.LightingCalcEntry;
import codechicken.lib.world.placement.lighting.LightingCalcEntryBuilder;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.LinkedList;

/**
 * Created by covers1624 on 8/1/2016.
 * TODO, Rethink this entire system, we need to cache blocks for the delayed lighting calculation.
 */
public class BlockPlacementBatcher {

    private final WorldServer serverWorld;
    private THashSet<Chunk> modifiedChunks = new THashSet<Chunk>();
    private LinkedList<LightingCalcEntry> blockLightingUpdates = new LinkedList<LightingCalcEntry>();

    public BlockPlacementBatcher(WorldServer serverWorld) {
        this.serverWorld = serverWorld;
    }

    public void setBlockState(BlockPos pos, IBlockState newState) {
        if (!hasBlockStorage(pos) && newState.getBlock() == Blocks.AIR) {
            return;
        }
        IBlockState oldState = getChunk(pos).getBlockState(pos);
        if (oldState == newState) {
            return;
        }
        setRecalcPrecipitationHeightMap(pos);
        LightingCalcEntryBuilder builder = new LightingCalcEntryBuilder();
        builder.setDimension(serverWorld.provider.getDimension());
        builder.setBasePos(pos);
        builder.setOldState(oldState);
        builder.setNewState(newState);
        builder.setOldLightOpacity(oldState.getLightOpacity(serverWorld, pos));

        createBlockStorageIfNeeded(pos, builder);
        ExtendedBlockStorage storage = getBlockStorage(pos);
        Chunk chunk = getChunk(pos);
        storage.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, newState);

        fireBlockBreak(pos, oldState, newState);
        tileEntityRemoveCheck(pos, oldState, newState);

        builder.setNewLightOpacity(newState.getLightOpacity(serverWorld, pos));
        builder.setSkyLight(chunk.getLightFor(EnumSkyBlock.SKY, pos));
        builder.setBlockLight(chunk.getLightFor(EnumSkyBlock.BLOCK, pos));

        fireBlockPlace(pos, oldState, newState);
        tileEntityPlaceCheck(pos, newState);

        setChunkModified(pos);
        blockLightingUpdates.add(builder.build());
    }

    public void setChunkModified(BlockPos blockPos) {
        Chunk chunk = getChunk(blockPos);
        setChunkModified(chunk);
    }

    public void setChunkModified(Chunk chunk) {
        modifiedChunks.add(chunk);
    }

    private Chunk getChunk(BlockPos pos) {
        return serverWorld.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
    }

    private boolean hasBlockStorage(BlockPos pos) {
        Chunk chunk = getChunk(pos);
        return chunk.storageArrays[pos.getY() >> 4] != null;
    }

    private ExtendedBlockStorage getBlockStorage(BlockPos pos) {
        Chunk chunk = getChunk(pos);
        return chunk.storageArrays[pos.getY() >> 4];
    }

    /**
     * Creates a ExtendedBlockStorage for the given BlockPos if needed.
     *
     * @param pos     Position to create a storage for.
     * @param builder Lighting Calculation builder, can be null.
     */
    private void createBlockStorageIfNeeded(BlockPos pos, LightingCalcEntryBuilder builder) {
        Chunk chunk = getChunk(pos);
        ExtendedBlockStorage storage = chunk.storageArrays[pos.getY() >> 4];
        if (storage == null) {
            int height = chunk.heightMap[(pos.getZ() & 15) << 4 | (pos.getX() & 15)];
            storage = new ExtendedBlockStorage(pos.getY() >> 4 << 4, !serverWorld.provider.getHasNoSky());
            chunk.storageArrays[pos.getY() >> 4] = storage;
            builder.setHeight(height);
            if (builder != null && pos.getY() >= height) {
                builder.setChunkHeightModified();
            }
        }
    }

    private void setRecalcPrecipitationHeightMap(BlockPos pos) {
        Chunk chunk = getChunk(pos);
        int i = (pos.getZ() & 15) << 4 | (pos.getX() & 15);
        if (pos.getY() >= chunk.precipitationHeightMap[i] - 1) {
            chunk.precipitationHeightMap[i] = -999;
        }
    }

    private void fireBlockBreak(BlockPos pos, IBlockState oldState, IBlockState newState) {
        if (oldState.getBlock() != newState.getBlock()) {
            oldState.getBlock().breakBlock(serverWorld, pos, oldState);
        }
    }

    private void fireBlockPlace(BlockPos pos, IBlockState oldState, IBlockState newState) {
        if (oldState.getBlock() != newState.getBlock() && newState.getBlock().hasTileEntity(newState)) {
            oldState.getBlock().onBlockAdded(serverWorld, pos, oldState);
        }
    }

    private void tileEntityRemoveCheck(BlockPos pos, IBlockState oldState, IBlockState newState) {

        TileEntity tileEntity = getChunk(pos).getTileEntity(pos, EnumCreateEntityType.CHECK);
        if (tileEntity != null && tileEntity.shouldRefresh(serverWorld, pos, oldState, newState)) {
            serverWorld.removeTileEntity(pos);
        }
    }

    private void tileEntityPlaceCheck(BlockPos pos, IBlockState newState) {
        if (newState.getBlock().hasTileEntity(newState)) {
            TileEntity tileEntity = getChunk(pos).getTileEntity(pos, EnumCreateEntityType.CHECK);

            if (tileEntity == null) {
                tileEntity = newState.getBlock().createTileEntity(serverWorld, newState);
                serverWorld.setTileEntity(pos, tileEntity);
            } else {
                tileEntity.updateContainingBlockInfo();
            }
        }
    }

    /**
     * Call when finished placing blocks to que lighting and send chunk updates to the client.
     */
    public void finish() {
        for (Chunk chunk : modifiedChunks) {
            PlayerChunkMap playerChunkMap = serverWorld.getPlayerChunkMap();
            if (playerChunkMap == null) {
                return;
            }
            PlayerChunkMapEntry watcher = playerChunkMap.getEntry(chunk.xPosition, chunk.zPosition);
            if (watcher != null) {//TODO Change chunk mask to only the sub chunks changed.
                watcher.sendPacket(new SPacketChunkData(chunk, 65535));
            }
        }
        modifiedChunks.clear();
        BlockPlacementLightingManager.uploadToLightingQue(blockLightingUpdates);
        blockLightingUpdates.clear();
    }

    public boolean isAirBlock(BlockPos pos) {
        return serverWorld.isAirBlock(pos);
    }

    public IBlockState getBlockState(BlockPos pos) {
        ExtendedBlockStorage storage = getBlockStorage(pos);
        if (storage == null) {
            return Blocks.AIR.getDefaultState();
        }
        return storage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
    }
}
