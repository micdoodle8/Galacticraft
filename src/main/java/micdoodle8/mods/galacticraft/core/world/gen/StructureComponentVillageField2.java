package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageField2 extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    private Block cropTypeA;
    private Block cropTypeB;
    private Block cropTypeC;
    private Block cropTypeD;

    public StructureComponentVillageField2()
    {
    }

    public StructureComponentVillageField2(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, EnumFacing par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
        this.cropTypeA = this.getRandomCrop(par3Random);
        this.cropTypeB = this.getRandomCrop(par3Random);
        this.cropTypeC = this.getRandomCrop(par3Random);
        this.cropTypeD = this.getRandomCrop(par3Random);
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound nbt)
    {
        super.writeStructureToNBT(nbt);

        nbt.setInteger("AvgGroundLevel", this.averageGroundLevel);
        nbt.setInteger("CropTypeA", Block.getIdFromBlock(this.cropTypeA));
        nbt.setInteger("CropTypeB", Block.getIdFromBlock(this.cropTypeB));
        nbt.setInteger("CropTypeC", Block.getIdFromBlock(this.cropTypeC));
        nbt.setInteger("CropTypeD", Block.getIdFromBlock(this.cropTypeD));
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound nbt)
    {
        super.readStructureFromNBT(nbt);

        this.averageGroundLevel = nbt.getInteger("AvgGroundLevel");
        this.cropTypeA = Block.getBlockById(nbt.getInteger("CropTypeA"));
        this.cropTypeB = Block.getBlockById(nbt.getInteger("CropTypeB"));
        this.cropTypeC = Block.getBlockById(nbt.getInteger("CropTypeC"));
        this.cropTypeD = Block.getBlockById(nbt.getInteger("CropTypeD"));
    }

    private Block getRandomCrop(Random par1Random)
    {
        switch (par1Random.nextInt(5))
        {
        case 0:
            return Blocks.carrots;
        case 1:
            return Blocks.potatoes;
        default:
            return Blocks.wheat;
        }
    }

    public static StructureComponentVillageField2 func_74900_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructureComponent> par1List, Random par2Random, int par3, int par4, int par5, EnumFacing par6, int par7)
    {
        final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);
        return StructureComponentVillage.canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(par1List, structureboundingbox) == null ? new StructureComponentVillageField2(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6) : null;
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

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
        int i;

        for (i = 1; i <= 7; ++i)
        {
            this.setBlockState(par1World, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 1, 1, i, par3StructureBoundingBox);
            this.setBlockState(par1World, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 2, 1, i, par3StructureBoundingBox);
            this.setBlockState(par1World, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 4, 1, i, par3StructureBoundingBox);
            this.setBlockState(par1World, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 5, 1, i, par3StructureBoundingBox);
            this.setBlockState(par1World, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 7, 1, i, par3StructureBoundingBox);
            this.setBlockState(par1World, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 8, 1, i, par3StructureBoundingBox);
            this.setBlockState(par1World, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 10, 1, i, par3StructureBoundingBox);
            this.setBlockState(par1World, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 2, 7)), 11, 1, i, par3StructureBoundingBox);
        }

        for (i = 0; i < 9; ++i)
        {
            for (int j = 0; j < 13; ++j)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, j, 4, i, par3StructureBoundingBox);
                this.replaceAirAndLiquidDownwards(par1World, Blocks.dirt.getDefaultState(), j, -1, i, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
