package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageField extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageField()
    {
    }

    public StructureComponentVillageField(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, EnumFacing par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound nbt)
    {
        super.writeStructureToNBT(nbt);

        nbt.setInteger("AvgGroundLevel", this.averageGroundLevel);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound nbt)
    {
        super.readStructureFromNBT(nbt);

        this.averageGroundLevel = nbt.getInteger("AvgGroundLevel");
    }

    public static StructureComponentVillageField func_74900_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructureComponent> par1List, Random par2Random, int par3, int par4, int par5, EnumFacing par6, int par7)
    {
        final StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);
        return StructureComponent.findIntersecting(par1List, var8) == null ? new StructureComponentVillageField(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
    }

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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7 - 1, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
        int var4;

        for (var4 = 1; var4 <= 7; ++var4)
        {
            for (int i = 1; i < 12; i++)
            {
                if (i % 3 != 0)
                {
                    if (par2Random.nextInt(3) == 0)
                    {
                        this.setBlockState(par1World, Blocks.sapling.getStateFromMeta(MathHelper.getRandomIntegerInRange(par2Random, 0, 2)), i, 1, var4, par3StructureBoundingBox);
                    }
                }
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            for (int var5 = 0; var5 < 13; ++var5)
            {
                this.clearCurrentPositionBlocksUpwards(par1World, var5, 4, var4, par3StructureBoundingBox);
                this.replaceAirAndLiquidDownwards(par1World, Blocks.dirt.getDefaultState(), var5, -1, var4, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
