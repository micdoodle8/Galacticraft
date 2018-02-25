package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageWell extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageWell()
    {
    }

    public StructureComponentVillageWell(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, int par4, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.coordBaseMode = EnumFacing.getFront(par3Random.nextInt(4));

        switch (this.coordBaseMode.getHorizontalIndex())
        {
        case 0:
        case 2:
            this.boundingBox = new StructureBoundingBox(par4, 64, par5, par4 + 6 - 1, 78, par5 + 6 - 1);
            break;
        default:
            this.boundingBox = new StructureBoundingBox(par4, 64, par5, par4 + 6 - 1, 78, par5 + 6 - 1);
        }
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

    @Override
    public void buildComponent(StructureComponent par1StructureComponent, List<StructureComponent> par2List, Random par3Random)
    {
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.getHorizontal(1), this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.getHorizontal(3), this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.getHorizontal(2), this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.getHorizontal(0), this.getComponentType());
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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 3, 0);
        }

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 4, 12, 4, GCBlocks.basicBlock.getStateFromMeta(4), Blocks.flowing_water.getDefaultState(), false);
        this.setBlockState(par1World, Blocks.air.getDefaultState(), 2, 12, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.air.getDefaultState(), 3, 12, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.air.getDefaultState(), 2, 12, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.air.getDefaultState(), 3, 12, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 1, 13, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 1, 14, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 4, 13, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 4, 14, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 1, 13, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 1, 14, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 4, 13, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 4, 14, 4, par3StructureBoundingBox);
        this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 15, 1, 4, 15, 4, GCBlocks.basicBlock.getStateFromMeta(4), GCBlocks.basicBlock.getStateFromMeta(4), false);

        for (int var4 = 0; var4 <= 5; ++var4)
        {
            for (int var5 = 0; var5 <= 5; ++var5)
            {
                if (var5 == 0 || var5 == 5 || var4 == 0 || var4 == 5)
                {
                    this.setBlockState(par1World, Blocks.planks.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), var5, 11, var4, par3StructureBoundingBox);
                    this.clearCurrentPositionBlocksUpwards(par1World, var5, 12, var4, par3StructureBoundingBox);
                }
            }
        }
        return true;
    }
}
