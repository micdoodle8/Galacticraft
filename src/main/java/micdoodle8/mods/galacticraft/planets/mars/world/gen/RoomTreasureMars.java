package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockTier2TreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.ChestGenHooks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class RoomTreasureMars extends RoomTreasure
{
    public static String MARSCHEST = "moonchest";

    static
    {
        List<WeightedRandomChestContent> list = Lists.newArrayList(new WeightedRandomChestContent[] {
                new WeightedRandomChestContent(MarsItems.carbonFragments, 0, 18, 64, 10),
                new WeightedRandomChestContent(GCItems.foodItem, 0, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 1, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 2, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.foodItem, 3, 1, 2, 6),
                new WeightedRandomChestContent(GCItems.oilCanister, ItemCanisterGeneric.EMPTY, 1, 1, 5),
                new WeightedRandomChestContent(MarsItems.deshBoots, 0, 1, 1, 2),
                new WeightedRandomChestContent(MarsItems.deshChestplate, 0, 1, 1, 2),
                new WeightedRandomChestContent(MarsItems.deshHelmet, 0, 1, 1, 2),
                new WeightedRandomChestContent(MarsItems.deshLeggings, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.redstone, 0, 4, 21, 10),
                new WeightedRandomChestContent(Items.record_mall, 0, 1, 1, 4),
                new WeightedRandomChestContent(Items.record_mellohi, 0, 1, 1, 4),
                new WeightedRandomChestContent(MarsItems.marsItemBasic, 0, 1, 2, 5),
                new WeightedRandomChestContent(MarsItems.marsItemBasic, 1, 1, 1, 5)
                });;
        ChestGenHooks.init(MARSCHEST, list, 6, 9);
        ChestGenHooks.addItem(MARSCHEST, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 8));
        ChestGenHooks.addItem(MARSCHEST, new WeightedRandomChestContent(new net.minecraft.item.ItemStack(Items.enchanted_book, 1, 0), 1, 1, 8));
    }

    public RoomTreasureMars()
    {
    }

    public RoomTreasureMars(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, rand.nextInt(4) + 6, configuration.getRoomHeight(), rand.nextInt(4) + 6, entranceDir);
    }

    public RoomTreasureMars(DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, EnumFacing entranceDir)
    {
        super(configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox boundingBox)
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
                        if (getDirection().getAxis() == EnumFacing.Axis.Z)
                        {
                            int start = (this.boundingBox.maxX - this.boundingBox.minX) / 2 - 1;
                            int end = (this.boundingBox.maxX - this.boundingBox.minX) / 2 + 1;
                            if (i > start && i <= end && j < 3 && j > 0)
                            {
                                if (getDirection() == EnumFacing.SOUTH && k == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == EnumFacing.NORTH && k == this.sizeZ)
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
                                if (getDirection() == EnumFacing.EAST && i == 0)
                                {
                                    placeBlock = false;
                                }
                                else if (getDirection() == EnumFacing.WEST && i == this.sizeX)
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
                            this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                        }
                    }
                    else if ((i == 1 && k == 1) || (i == 1 && k == this.sizeZ - 1) || (i == this.sizeX - 1 && k == 1) || (i == this.sizeX - 1 && k == this.sizeZ - 1))
                    {
                        this.setBlockState(worldIn, Blocks.glowstone.getDefaultState(), i, j, k, boundingBox);
                    }
                    else if (i == this.sizeX / 2 && j == 1 && k == this.sizeZ / 2)
                    {
                        this.setBlockState(worldIn, MarsBlocks.treasureChestTier2.getDefaultState().withProperty(BlockTier2TreasureChest.FACING, this.getDirection().getOpposite()), i, j, k, boundingBox);
                    }
                    else
                    {
                        this.setBlockState(worldIn, Blocks.air.getDefaultState(), i, j, k, boundingBox);
                    }
                }
            }
        }

        return true;
    }
}