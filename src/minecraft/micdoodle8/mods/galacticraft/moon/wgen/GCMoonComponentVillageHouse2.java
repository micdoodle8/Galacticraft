package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraftforge.common.ChestGenHooks;

public class GCMoonComponentVillageHouse2 extends GCMoonComponentVillage
{
    /** List of items that Village's Blacksmith chest can contain. */
    public static final WeightedRandomChestContent[] villageBlacksmithChestContents = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 3, 3), new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5), new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.appleRed.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.pickaxeSteel.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.swordSteel.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.plateSteel.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.helmetSteel.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.legsSteel.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.bootsSteel.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Block.obsidian.blockID, 0, 3, 7, 5), new WeightedRandomChestContent(Block.sapling.blockID, 0, 3, 7, 5)};
    private int averageGroundLevel = -1;
    private boolean hasMadeChest;

    public GCMoonComponentVillageHouse2(GCMoonComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
    }

    public static GCMoonComponentVillageHouse2 func_74915_a(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        final StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 10, 6, 7, par6);
        return GCMoonComponentVillage.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(par1List, var8) == null ? new GCMoonComponentVillageHouse2(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    @Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 6 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 9, 4, 6, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 9, 0, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 4, 0, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 5, 0, 9, 5, 6, Block.stoneSingleSlab.blockID, Block.stoneSingleSlab.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 5, 1, 8, 5, 5, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 0, 2, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 0, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 1, 0, 3, 4, 0, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 6, 0, 4, 6, Block.wood.blockID, Block.wood.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 3, 3, 1, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 1, 2, 3, 3, 2, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 1, 3, 5, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 1, 0, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 6, 5, 3, 6, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 1, 0, 5, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 1, 0, 9, 3, 0, Block.fence.blockID, Block.fence.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 1, 4, 9, 4, 6, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, Block.lavaMoving.blockID, 0, 7, 1, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.lavaMoving.blockID, 0, 8, 1, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fenceIron.blockID, 0, 9, 2, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fenceIron.blockID, 0, 9, 2, 4, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 2, 4, 8, 2, 5, 0, 0, false);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 6, 1, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stoneOvenIdle.blockID, 0, 6, 2, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stoneOvenIdle.blockID, 0, 6, 3, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stoneDoubleSlab.blockID, 0, 8, 1, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 0, 2, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 0, 2, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 2, 2, 6, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 4, 2, 6, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 2, 1, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.pressurePlatePlanks.blockID, 0, 2, 2, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 1, 1, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairCompactPlanks.blockID, this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3), 2, 1, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairCompactPlanks.blockID, this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 1), 1, 1, 4, par3StructureBoundingBox);
        int var4;
        int var5;

        if (!this.hasMadeChest)
        {
            var4 = this.getYWithOffset(1);
            var5 = this.getXWithOffset(5, 5);
            final int var6 = this.getZWithOffset(5, 5);

            if (par3StructureBoundingBox.isVecInside(var5, var4, var6))
            {
                this.hasMadeChest = true;
                this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 5, 1, 5, ChestGenHooks.getItems(ChestGenHooks.VILLAGE_BLACKSMITH, par2Random), ChestGenHooks.getCount(ChestGenHooks.VILLAGE_BLACKSMITH, par2Random));
            }
        }

        for (var4 = 6; var4 <= 8; ++var4)
        {
            if (this.getBlockIdAtCurrentPosition(par1World, var4, 0, -1, par3StructureBoundingBox) == 0 && this.getBlockIdAtCurrentPosition(par1World, var4, -1, -1, par3StructureBoundingBox) != 0)
            {
                this.placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), var4, 0, -1, par3StructureBoundingBox);
            }
        }

        for (var4 = 0; var4 < 7; ++var4)
        {
            for (var5 = 0; var5 < 10; ++var5)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, var5, 6, var4, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.cobblestone.blockID, 0, var5, -1, var4, par3StructureBoundingBox);
            }
        }

        this.spawnVillagers(par1World, par3StructureBoundingBox, 7, 1, 1, 1);
        return true;
    }

    /**
     * Returns the villager type to spawn in this component, based on the number of villagers already spawned.
     */
    @Override
	protected int getVillagerType(int par1)
    {
        return 3;
    }
}
