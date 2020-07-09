package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockTier3TreasureChest;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_TREASURE;

public class RoomTreasureVenus extends SizedPieceVenus
{
    public static ResourceLocation VENUSCHEST = new ResourceLocation(Constants.MOD_ID_CORE, "dungeon_tier_3");
//    public static final ResourceLocation TABLE_TIER_3_DUNGEON = LootTables.register(VENUSCHEST);

    public RoomTreasureVenus(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CVENUS_DUNGEON_TREASURE, nbt);
    }

    public RoomTreasureVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, Direction entranceDir)
    {
        this(configuration, rand, blockPosX, blockPosZ, rand.nextInt(4) + 6, configuration.getRoomHeight(), rand.nextInt(4) + 6, entranceDir);
    }

    public RoomTreasureVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CVENUS_DUNGEON_TREASURE, configuration, sizeX, sizeY, sizeZ, entranceDir.getOpposite());
        this.setCoordBaseMode(Direction.SOUTH);
        int yPos = configuration.getYPosition();

        this.boundingBox = new MutableBoundingBox(blockPosX, yPos, blockPosZ, blockPosX + this.sizeX, yPos + this.sizeY, blockPosZ + this.sizeZ);
    }

    @Override
    public boolean addComponentParts(IWorld worldIn, Random random, MutableBoundingBox chunkBox, ChunkPos pos)
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
                            DungeonConfigurationVenus venusConfig = (DungeonConfigurationVenus) this.configuration;
                            this.setBlockState(worldIn, j == 0 || j == this.sizeY ? venusConfig.getBrickBlockFloor() : this.configuration.getBrickBlock(), i, j, k, boundingBox);
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
                            worldIn.setBlockState(blockpos, VenusBlocks.treasureChestTier3.getDefaultState().with(BlockTier3TreasureChest.FACING, this.getDirection().getOpposite()), 2);
                            TileEntityTreasureChest treasureChest = (TileEntityTreasureChest) worldIn.getTileEntity(blockpos);
                            if (treasureChest != null)
                            {
//                                ResourceLocation chesttype = TABLE_TIER_3_DUNGEON;
//                                if (worldIn.dimension instanceof IGalacticraftDimension)
//                                {
//                                    chesttype = ((IGalacticraftDimension)worldIn.dimension).getDungeonChestType();
//                                } TODO Loot tables
//                                treasureChest.setLootTable(chesttype, random.nextLong());
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
    public PieceVenus getNextPiece(DungeonStartVenus startPiece, Random rand)
    {
        if (startPiece.attachedComponents.size() > 2)
        {
            StructurePiece component = startPiece.attachedComponents.get(startPiece.attachedComponents.size() - 3);
            if (component instanceof RoomBossVenus)
            {
                BlockPos blockpos = new BlockPos(this.getXWithOffset(this.sizeX / 2, this.sizeZ / 2), this.getYWithOffset(1), this.getZWithOffset(this.sizeX / 2, this.sizeZ / 2));
                ((RoomBossVenus) component).setChestPos(new BlockPos(blockpos));
            }
        }
        return null;
    }
}