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
    private final int field_82675_b;
    private final int field_82676_c;

    public GCMoonComponentVillageField2(GCMoonComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
        this.field_82675_b = this.pickRandomCrop(par3Random);
        this.field_82676_c = this.pickRandomCrop(par3Random);
    }

    /**
     * Returns a crop type to be planted on this field.
     */
    private int pickRandomCrop(Random par1Random)
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

    public static GCMoonComponentVillageField2 func_74902_a(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        final StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 7, 4, 9, par6);
        return GCMoonComponentVillage.canVillageGoDeeper(var8) && StructureComponent.findIntersecting(par1List, var8) == null ? new GCMoonComponentVillageField2(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 4 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 6, 4, 8, 0, 0, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 5, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 5, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
        int var4;

        for (var4 = 1; var4 <= 7; ++var4)
        {
            this.placeBlockAtCurrentPosition(par1World, this.field_82675_b, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 1, 1, var4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.field_82675_b, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 2, 1, var4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.field_82676_c, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 4, 1, var4, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, this.field_82676_c, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 5, 1, var4, par3StructureBoundingBox);
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            for (int var5 = 0; var5 < 7; ++var5)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, var5, 4, var4, par3StructureBoundingBox);
                this.fillCurrentPositionBlocksDownwards(par1World, Block.dirt.blockID, 0, var5, -1, var4, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
