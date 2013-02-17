package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class GCMoonComponentVillageWell extends GCMoonComponentVillage
{
    private final boolean field_74924_a = true;
    private int averageGroundLevel = -1;

    public GCMoonComponentVillageWell(GCMoonComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, int par4, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par3Random.nextInt(4);

        switch (this.coordBaseMode)
        {
            case 0:
            case 2:
                this.boundingBox = new StructureBoundingBox(par4, 64, par5, par4 + 6 - 1, 78, par5 + 6 - 1);
                break;
            default:
                this.boundingBox = new StructureBoundingBox(par4, 64, par5, par4 + 6 - 1, 78, par5 + 6 - 1);
        }
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    @Override
	public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        GCMoonStructureVillagePieces.getNextStructureComponentVillagePath((GCMoonComponentVillageStartPiece)par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, 1, this.getComponentType());
        GCMoonStructureVillagePieces.getNextStructureComponentVillagePath((GCMoonComponentVillageStartPiece)par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, 3, this.getComponentType());
        GCMoonStructureVillagePieces.getNextStructureComponentVillagePath((GCMoonComponentVillageStartPiece)par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, 2, this.getComponentType());
        GCMoonStructureVillagePieces.getNextStructureComponentVillagePath((GCMoonComponentVillageStartPiece)par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, 0, this.getComponentType());
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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 3, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 4, 12, 4, Block.cobblestone.blockID, Block.waterMoving.blockID, false);
        this.placeBlockAtCurrentPosition(par1World, 0, 0, 2, 12, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, 0, 0, 3, 12, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, 0, 0, 2, 12, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, 0, 0, 3, 12, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 13, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 14, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 13, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 14, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 13, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 1, 14, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 13, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Block.fence.blockID, 0, 4, 14, 4, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 15, 1, 4, 15, 4, Block.cobblestone.blockID, Block.cobblestone.blockID, false);

        for (int var4 = 0; var4 <= 5; ++var4)
        {
            for (int var5 = 0; var5 <= 5; ++var5)
            {
                if (var5 == 0 || var5 == 5 || var4 == 0 || var4 == 5)
                {
                    this.placeBlockAtCurrentPosition(par1World, Block.gravel.blockID, 0, var5, 11, var4, par3StructureBoundingBox);
                    this.clearCurrentPositionBlocksUpwards(par1World, var5, 12, var4, par3StructureBoundingBox);
                }
            }
        }

        return true;
    }
}
