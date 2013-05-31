package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class GCMoonComponentVillageField2 extends GCMoonComponentVillage
{
    private int averageGroundLevel = -1;

    /** First crop type for this field. */
    private final int cropTypeA;

    /** Second crop type for this field. */
    private final int cropTypeB;

    /** Third crop type for this field. */
    private final int cropTypeC;

    /** Fourth crop type for this field. */
    private final int cropTypeD;

    public GCMoonComponentVillageField2(GCMoonComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
        this.cropTypeA = this.getRandomCrop(par3Random);
        this.cropTypeB = this.getRandomCrop(par3Random);
        this.cropTypeC = this.getRandomCrop(par3Random);
        this.cropTypeD = this.getRandomCrop(par3Random);
    }

    /**
     * Returns a crop type to be planted on this field.
     */
    private int getRandomCrop(Random par1Random)
    {
        switch (par1Random.nextInt(5))
        {
        case 0:
            return Block.carrot.blockID;
        case 1:
            return Block.potato.blockID;
        default:
            return Block.crops.blockID;
        }
    }

    public static GCMoonComponentVillageField2 func_74900_a(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);
        return GCMoonComponentVillage.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(par1List, structureboundingbox) == null ? new GCMoonComponentVillageField2(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6) : null;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs,
     * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
        int i;

        for (i = 1; i <= 7; ++i)
        {
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeA, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 1, 1, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeA, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 2, 1, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeB, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 4, 1, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeB, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 5, 1, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeC, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 7, 1, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeC, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 8, 1, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeD, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 10, 1, i, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.cropTypeD, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 11, 1, i, par3StructureBoundingBox);
        }

        for (i = 0; i < 9; ++i)
        {
            for (int j = 0; j < 13; ++j)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, j, 4, i, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.dirt.blockID, 0, j, -1, i, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
