package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

import java.util.Random;

public class RoomChest extends RoomEmpty
{
    public RoomChest(IStructurePieceType type)
    {
        super(type);
    }

    public RoomChest(IStructurePieceType type, DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(type, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean addComponentParts(IWorld worldIn, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos)
    {
        if (super.addComponentParts(worldIn, random, boundingBox, chunkPos))
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
                if (worldIn.getDimension() instanceof IGalacticraftWorldProvider)
                {
                    chesttype = ((IGalacticraftWorldProvider)worldIn.getDimension()).getDungeonChestType();
                }
                chest.setLootTable(chesttype, random.nextLong());
            }

            return true;
        }

        return false;
    }
}