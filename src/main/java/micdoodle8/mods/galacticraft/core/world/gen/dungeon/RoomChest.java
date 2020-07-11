package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_CHEST;

public class RoomChest extends RoomEmpty
{
    public RoomChest(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CMOON_DUNGEON_CHEST, nbt);
    }

    public RoomChest(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CMOON_DUNGEON_CHEST, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
    {
        if (super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn))
        {
            int chestX = this.sizeX / 2;
            int chestY = 1;
            int chestZ = this.sizeZ / 2;
            this.setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(BlockTier1TreasureChest.FACING, this.getDirection().getOpposite()), chestX, chestY, chestZ, boundingBox);

            BlockPos blockpos = new BlockPos(this.getXWithOffset(chestX, chestZ), this.getYWithOffset(chestY), this.getZWithOffset(chestX, chestZ));
            ChestTileEntity chest = (ChestTileEntity) worldIn.getTileEntity(blockpos);

            if (chest != null)
            {
                ResourceLocation chesttype = RoomTreasure.MOONCHEST;
                if (worldIn.getDimension() instanceof IGalacticraftDimension)
                {
                    chesttype = ((IGalacticraftDimension) worldIn.getDimension()).getDungeonChestType();
                }
                chest.setLootTable(chesttype, randomIn.nextLong());
            }

            return true;
        }

        return false;
    }
}