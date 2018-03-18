package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.ChestGenHooks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class RoomChest extends RoomEmpty
{
    public static String MOONCHEST = "moonchest";

    static
    {
        List<WeightedRandomChestContent> list = Lists.newArrayList(new WeightedRandomChestContent[] {
                new WeightedRandomChestContent(GCItems.cheeseCurd, 0, 1, 4, 10),
                new WeightedRandomChestContent(Items.iron_ingot, 0, 2, 14, 10),
                new WeightedRandomChestContent(GCItems.foodItem, 0, 1, 2, 6),  //Food cans
                new WeightedRandomChestContent(GCItems.foodItem, 1, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 2, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 3, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.oilCanister, ItemCanisterGeneric.EMPTY, 1, 1, 5),
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.redstone, 0, 4, 21, 10),
                new WeightedRandomChestContent(Items.record_blocks, 0, 1, 1, 4),
                new WeightedRandomChestContent(Items.record_far, 0, 1, 1, 4),
                new WeightedRandomChestContent(GCItems.steelSpade, 0, 1, 1, 10),
                new WeightedRandomChestContent(GCItems.itemBasicMoon, 2, 1, 2, 2),  //sapphire
                new WeightedRandomChestContent(GCItems.meteoricIronRaw, 0, 1, 5, 5),
                new WeightedRandomChestContent(GCItems.basicItem, 19, 1, 1, 1)  //Frequency Module
                });;
        ChestGenHooks.init(MOONCHEST, list, 5, 8);
        ChestGenHooks.addItem(MOONCHEST, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 8));
        ChestGenHooks.addItem(MOONCHEST, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 8));
    }

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
            this.setBlockState(worldIn, Blocks.chest.getDefaultState().withProperty(BlockTier1TreasureChest.FACING, this.getDirection().getOpposite()), chestX, chestY, chestZ, boundingBox);

            BlockPos blockpos = new BlockPos(this.getXWithOffset(chestX, chestZ), this.getYWithOffset(chestY), this.getZWithOffset(chestX, chestZ));
            TileEntityChest chest = (TileEntityChest) worldIn.getTileEntity(blockpos);

            if (chest != null)
            {
                for (int i = 0; i < chest.getSizeInventory(); ++i)
                {
                    // Clear contents
                    chest.setInventorySlotContents(i, null);
                }

                String chesttype = MOONCHEST;
                if (worldIn.provider instanceof IGalacticraftWorldProvider)
                {
                    chesttype = ((IGalacticraftWorldProvider)worldIn.provider).getDungeonChestType();
                }
                ChestGenHooks info = ChestGenHooks.getInfo(chesttype);

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