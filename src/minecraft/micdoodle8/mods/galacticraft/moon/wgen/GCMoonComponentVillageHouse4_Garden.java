package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class GCMoonComponentVillageHouse4_Garden extends GCMoonComponentVillage
{
    private int averageGroundLevel = -1;
    private final boolean isRoofAccessible;

    public GCMoonComponentVillageHouse4_Garden(GCMoonComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
        this.isRoofAccessible = par3Random.nextBoolean();
    }

    public static GCMoonComponentVillageHouse4_Garden func_74912_a(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        final StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 5, 6, 5, par6);
        return StructureComponent.findIntersecting(par1List, var8) != null ? null : new GCMoonComponentVillageHouse4_Garden(par0ComponentVillageStartPiece, par7, par2Random, var8, par6);
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

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 4, 0, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 4, 0, 4, 4, 4, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 4, 1, 3, 4, 3, Block.planks.blockID, Block.planks.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 0, 1, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 0, 2, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 0, 3, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 1, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 2, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 3, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 0, 1, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 0, 2, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 0, 3, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 1, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 2, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.cobblestone.blockID, 0, 4, 3, 4, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 1, 0, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 1, 1, 4, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 4, 3, 3, 4, Block.planks.blockID, Block.planks.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 0, 2, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 2, 2, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.thinGlass.blockID, 0, 4, 2, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 1, 1, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 1, 2, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 1, 3, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 2, 3, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 3, 3, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 3, 2, 0, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, 3, 1, 0, par3StructureBoundingBox);

        if (this.getBlockIdAtCurrentPosition(par1World, 2, 0, -1, par3StructureBoundingBox) == 0 && this.getBlockIdAtCurrentPosition(par1World, 2, -1, -1, par3StructureBoundingBox) != 0)
        {
            this.placeBlockAtCurrentPosition(par1World, Block.stairCompactCobblestone.blockID, this.getMetadataWithOffset(Block.stairCompactCobblestone.blockID, 3), 2, 0, -1, par3StructureBoundingBox);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 3, 3, 3, 0, 0, false);

        if (this.isRoofAccessible)
        {
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 0, 5, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 5, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 2, 5, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 3, 5, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 5, 0, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 0, 5, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 5, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 2, 5, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 3, 5, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 5, 4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 5, 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 5, 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 5, 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 0, 5, 1, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 0, 5, 2, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 0, 5, 3, par3StructureBoundingBox);
        }

        int var4;

        if (this.isRoofAccessible)
        {
            var4 = this.getMetadataWithOffset(Block.ladder.blockID, 3);
            this.placeBlockAtCurrentPosition(par1World, Block.ladder.blockID, var4, 3, 1, 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.ladder.blockID, var4, 3, 2, 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.ladder.blockID, var4, 3, 3, 3, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Block.ladder.blockID, var4, 3, 4, 3, par3StructureBoundingBox);
        }

        this.placeBlockAtCurrentPosition(par1World, Block.torchWood.blockID, 0, 2, 3, 1, par3StructureBoundingBox);

        for (var4 = 0; var4 < 5; ++var4)
        {
            for (int var5 = 0; var5 < 5; ++var5)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, var5, 6, var4, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.cobblestone.blockID, 0, var5, -1, var4, par3StructureBoundingBox);
            }
        }

        this.spawnVillagers(par1World, par3StructureBoundingBox, 1, 1, 2, 1);
        return true;
    }
}
