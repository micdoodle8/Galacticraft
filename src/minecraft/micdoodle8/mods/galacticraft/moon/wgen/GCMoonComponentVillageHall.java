package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class GCMoonComponentVillageHall extends GCMoonComponentVillage
{
    private int averageGroundLevel = -1;

    public GCMoonComponentVillageHall(GCMoonComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
    }

    public static GCMoonComponentVillageHall func_74906_a(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 9, 7, 11, par6);
        return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(par1List, var8) == null ? new GCMoonComponentVillageHall(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 7, 4, 4, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 1, 6, 8, 4, 10, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 0, 6, 8, 0, 10, Block.dirt.blockID, Block.dirt.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 6, 0, 6, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 1, 6, 2, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 1, 6, 8, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 1, 10, 7, 1, 10, Block.fence.blockID, Block.fence.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 7, 0, 4, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 0, 0, 8, 3, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 7, 1, 0, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 5, 7, 1, 5, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 2, 0, 7, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 2, 5, 7, 3, 5, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 4, 1, 8, 4, 1, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 4, 4, 8, 4, 4, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 5, 2, 8, 5, 3, Block.planks.blockID, Block.planks.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 0, 4, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 0, 4, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 8, 4, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 8, 4, 3, par3StructureBoundingBox);
        int var4 = this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3);
        int var5 = this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 2);
        int var6;
        int var7;

        for (var6 = -1; var6 <= 2; ++var6)
        {
            for (var7 = 0; var7 <= 8; ++var7)
            {
                this.placeBlockAtCurrentPosition(par1World, Block.stairCompactPlanks.blockID, var4, var7, 4 + var6, var6, par3StructureBoundingBox);
                this.placeBlockAtCurrentPosition(par1World, Block.stairCompactPlanks.blockID, var5, var7, 4 + var6, 5 - var6, par3StructureBoundingBox);
            }
        }

        this.placeBlockAtCurrentPosition(par1World, Block.wood.blockID, 0, 0, 2, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.wood.blockID, 0, 0, 2, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.wood.blockID, 0, 8, 2, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.wood.blockID, 0, 8, 2, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 0, 2, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 0, 2, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 8, 2, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 8, 2, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 2, 2, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 3, 2, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 5, 2, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 6, 2, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 2, 1, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.pressurePlatePlanks.blockID, 0, 2, 2, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 1, 1, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairCompactPlanks.blockID, this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 3), 2, 1, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stairCompactPlanks.blockID, this.getMetadataWithOffset(Block.stairCompactPlanks.blockID, 1), 1, 1, 3, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 5, 0, 1, 7, 0, 3, Block.stoneDoubleSlab.blockID, Block.stoneDoubleSlab.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, Block.stoneDoubleSlab.blockID, 0, 6, 1, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.stoneDoubleSlab.blockID, 0, 6, 1, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, 0, 0, 2, 1, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, 0, 0, 2, 2, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 2, 3, 1, par3StructureBoundingBox);
        this.placeDoorAtCurrentPosition(par1World, par3StructureBoundingBox, par2Random, 2, 1, 0, this.getMetadataWithOffset(Block.doorWood.blockID, 1));

        if (this.getBlockIdAtCurrentPosition(par1World, 2, 0, -1, par3StructureBoundingBox) == 0 && this.getBlockIdAtCurrentPosition(par1World, 2, -1, -1, par3StructureBoundingBox) != 0)
        {
            this.placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, par3StructureBoundingBox);
        }

        this.placeBlockAtCurrentPosition(par1World, 0, 0, 6, 1, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, 0, 0, 6, 2, 5, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 6, 3, 4, par3StructureBoundingBox);
        this.placeDoorAtCurrentPosition(par1World, par3StructureBoundingBox, par2Random, 6, 1, 5, this.getMetadataWithOffset(Block.doorWood.blockID, 1));

        for (var6 = 0; var6 < 5; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, var7, 7, var6, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.cobblestone.blockID, 0, var7, -1, var6, par3StructureBoundingBox);
            }
        }

        this.spawnVillagers(par1World, par3StructureBoundingBox, 4, 1, 2, 2);
        return true;
    }

    /**
     * Returns the villager type to spawn in this component, based on the number of villagers already spawned.
     */
    protected int getVillagerType(int par1)
    {
        return par1 == 0 ? 4 : 0;
    }
}
