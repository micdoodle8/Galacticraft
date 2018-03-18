package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

public class RoomChestVenus extends RoomEmptyVenus
{
    public static String VENUSCHEST = "moonchest";

    static
    {
        List<WeightedRandomChestContent> list = Lists.newArrayList(new WeightedRandomChestContent[] {
                new WeightedRandomChestContent(GCItems.foodItem, 0, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 1, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 2, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 3, 1, 2, 6),
                new WeightedRandomChestContent(MarsItems.deshBoots, 0, 1, 1, 2),
                new WeightedRandomChestContent(MarsItems.deshChestplate, 0, 1, 1, 2),
                new WeightedRandomChestContent(MarsItems.deshHelmet, 0, 1, 1, 2),
                new WeightedRandomChestContent(MarsItems.deshLeggings, 0, 1, 1, 2),
                new WeightedRandomChestContent(GCItems.oilCanister, ItemCanisterGeneric.EMPTY, 1, 1, 10),
                new WeightedRandomChestContent(VenusItems.bucketSulphuricAcid, 0, 1, 1, 4),
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.redstone, 0, 4, 21, 10),
                new WeightedRandomChestContent(Items.record_chirp, 0, 1, 1, 4),
                new WeightedRandomChestContent(Items.record_wait, 0, 1, 1, 4),
                new WeightedRandomChestContent(Items.record_ward, 0, 1, 1, 4),
                new WeightedRandomChestContent(VenusItems.basicItem, 3, 3, 12, 10),
                new WeightedRandomChestContent(Items.gold_nugget, 0, 3, 12, 10),
                });;
        ChestGenHooks.init(VENUSCHEST, list, 6, 9);
        ChestGenHooks.addItem(VENUSCHEST, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 8));
        ChestGenHooks.addItem(VENUSCHEST, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 8));
        ChestGenHooks.addItem(VENUSCHEST, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 8));
    }

    public RoomChestVenus()
    {
    }

    public RoomChestVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
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

                ChestGenHooks info = ChestGenHooks.getInfo(VENUSCHEST);

                WeightedRandomChestContent.generateChestContents(rand, info.getItems(rand), chest, info.getCount(rand));
            }

            return true;
        }

        return false;
    }
}