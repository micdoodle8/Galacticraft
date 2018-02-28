package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockGlowstoneTorch;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageTorch extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageTorch()
    {
    }

    public StructureComponentVillageTorch(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, EnumFacing par5)
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

    public static StructureBoundingBox func_74904_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructureComponent> par1List, Random par2Random, int par3, int par4, int par5, EnumFacing par6)
    {
        final StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 3, 4, 2, par6);
        return StructureComponent.findIntersecting(par1List, var7) != null ? null : var7;
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

        this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 3, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 1, 0, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 1, 1, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.dark_oak_fence.getDefaultState(), 1, 2, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, Blocks.wool.getStateFromMeta(15), 1, 3, 0, par3StructureBoundingBox);
        boolean flag = this.coordBaseMode == EnumFacing.EAST || this.coordBaseMode == EnumFacing.NORTH;
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.coordBaseMode.rotateY()), flag ? 2 : 0, 3, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.coordBaseMode), 1, 3, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.coordBaseMode.rotateYCCW()), flag ? 0 : 2, 3, 0, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.coordBaseMode.getOpposite()), 1, 3, -1, par3StructureBoundingBox);
        return true;
    }
}
