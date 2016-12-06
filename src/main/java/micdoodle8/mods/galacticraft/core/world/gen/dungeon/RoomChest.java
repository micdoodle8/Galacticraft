package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.blocks.BlockT1TreasureChest;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.ChestGenHooks;

import java.util.Random;

public class RoomChest extends RoomEmpty
{
    public RoomChest()
    {
    }

    public RoomChest(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random rand, StructureBoundingBox boundingBox)
    {
        if (super.addComponentParts(worldIn, rand, boundingBox))
        {
            int chestX = this.sizeX / 2;
            int chestY = 1;
            int chestZ = this.sizeZ / 2;
            this.setBlockState(worldIn, Blocks.chest.getDefaultState().withProperty(BlockT1TreasureChest.FACING, this.getDirection().getOpposite()), chestX, chestY, chestZ, boundingBox);

            BlockPos blockpos = new BlockPos(this.getXWithOffset(chestX, chestZ), this.getYWithOffset(chestY), this.getZWithOffset(chestX, chestZ));
            TileEntityChest chest = (TileEntityChest) worldIn.getTileEntity(blockpos);

            if (chest != null)
            {
                for (int i = 0; i < chest.getSizeInventory(); ++i)
                {
                    // Clear contents
                    chest.setInventorySlotContents(i, null);
                }

                ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);

                WeightedRandomChestContent.generateChestContents(rand, info.getItems(rand), chest, info.getCount(rand));
            }

            return true;
        }

        return false;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound)
    {
        super.readStructureFromNBT(tagCompound);
    }
}