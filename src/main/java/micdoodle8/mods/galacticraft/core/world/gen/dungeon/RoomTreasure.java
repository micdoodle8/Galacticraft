package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructurePiece;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.storage.loot.LootTables;

import java.util.Random;

public class RoomTreasure extends SizedPiece
{
    public static ResourceLocation MOONCHEST = new ResourceLocation(Constants.MOD_ID_CORE, "dungeon_tier_1");
    public static final ResourceLocation TABLE_TIER_1_DUNGEON = LootTables.register(MOONCHEST);

    public RoomTreasure(IStructurePieceType type)
    {
        super(type);
    }

    public RoomTreasure(IStructurePieceType type, DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, Direction entranceDir)
    {
        this(type, configuration, rand, blockPosX, blockPosZ, rand.nextInt(4) + 6, configuration.getRoomHeight(), rand.nextInt(4) + 6, entranceDir);
    }

    public RoomTreasure(IStructurePieceType type, DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(type, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setCoordBaseMode(Direction.SOUTH);
        int yPos = configuration.getYPosition();

        this.boundingBox = new MutableBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean addComponentParts(IWorld worldIn, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos)
    {
        for (int i = 0; i <= this.sizeX; i++)
        {
            for (int j = 0; j <= this.sizeY; j++)
            {
                for (int k = 0; k <= this.sizeZ; k++)
                {
                    if (i == 0 || i == this.sizeX || j == 0 || j == this.sizeY || k == 0 || k == this.sizeZ)
                    {
                        boolean placeBlock = true;
                        if (getDirection().getAxis() == Direction.Axis.Z)
                        {
                            int start = (this.boundingBox.maxX - this.boundingBox.minX) / 2 - 1;
                            int end = (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1;
                            if (i > start && i <= end && j < 3 && j > 0)
                            {
                                if (getDirection() == Direction.SOUTH && k == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.NORTH && k == this.sizeZ)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        else
                        {
                            int start = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 - 1;
                            int end = (this.boundingBox.maxZ - this.boundingBox.minZ) / 2 + 1;
                            if (k > start && k <= end && j < 3 && j > 0)
                            {
                                if (getDirection() == Direction.EAST && i == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == Direction.WEST && i == this.sizeX)
                                {
                                    placeBlock = false;
                                }
                            }
                        }
                        if (placeBlock)
                        {
                            this.setBlockState(worldIn, this.configuration.getBrickBlock(), i, j, k, boundingBox);
                        }
                        else
                        {
                            this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                    else if ((i == 1 && k == 1) || (i == 1 && k == this.sizeZ - 1) || (i == this.sizeX - 1 && k == 1) || (i == this.sizeX - 1 && k == this.sizeZ - 1))
                    {
                        this.setBlockState(worldIn, Blocks.GLOWSTONE.getDefaultState(), i, j, k, boundingBox);
                    }
                    else if (i == this.sizeX / 2 && j == 1 && k == this.sizeZ / 2)
                    {
                        BlockPos blockpos = new BlockPos(this.getXWithOffset(i, k), this.getYWithOffset(j), this.getZWithOffset(i, k));
                        if (boundingBox.isVecInside(blockpos))
                        {
                            worldIn.setBlockState(blockpos, GCBlocks.treasureChestTier1.getDefaultState().with(BlockTier1TreasureChest.FACING, this.getDirection().getOpposite()), 2);
                            TileEntityTreasureChest treasureChest = (TileEntityTreasureChest) worldIn.getTileEntity(blockpos);
                            if (treasureChest != null)
                            {
                                ResourceLocation chesttype = TABLE_TIER_1_DUNGEON;
                                if (worldIn.getDimension() instanceof IGalacticraftWorldProvider)
                                {
                                    chesttype = ((IGalacticraftWorldProvider)worldIn.getDimension()).getDungeonChestType();
                                }
                                treasureChest.setLootTable(chesttype, random.nextLong());
                            }
                        }
                    }
                    else
                    {
                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), i, j, k, boundingBox);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Piece getNextPiece(DungeonStart startPiece, Random rand)
    {
        if (startPiece.attachedComponents.size() > 2)
        {
            StructurePiece component = startPiece.attachedComponents.get(startPiece.attachedComponents.size() - 3);
            if (component instanceof RoomBoss)
            {
                BlockPos blockpos = new BlockPos(this.getXWithOffset(this.sizeX / 2, this.sizeZ / 2), this.getYWithOffset(1), this.getZWithOffset(this.sizeX / 2, this.sizeZ / 2));
                ((RoomBoss) component).setChestPos(new BlockPos(blockpos));
            }
        }
        return null;
    }
}