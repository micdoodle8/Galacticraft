package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockGlowstoneTorch;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class StructureComponentVillageWoodHut extends StructureComponentVillage
{
    private int averageGroundLevel = -1;

    public StructureComponentVillageWoodHut()
    {
    }

    public StructureComponentVillageWoodHut(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, EnumFacing par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        this.setCoordBaseMode(par5);
        this.boundingBox = par4StructureBoundingBox;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound nbt)
    {
        super.writeStructureToNBT(nbt);

        nbt.setInteger("AvgGroundLevel", this.averageGroundLevel);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
    {
        super.readStructureFromNBT(nbt, manager);

        this.averageGroundLevel = nbt.getInteger("AvgGroundLevel");
    }

    public static StructureComponentVillageWoodHut func_74908_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructureComponent> par1List, Random par2Random, int par3, int par4, int par5, EnumFacing par6, int par7)
    {
        final StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 17, 9, 17, par6);
        return StructureComponent.findIntersecting(par1List, var8) == null ? new StructureComponentVillageWoodHut(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
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

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 9 - 1, 0);
        }

        this.fillWithAir(par1World, par3StructureBoundingBox, 3, 0, 3, 13, 9, 13);
        this.fillWithAir(par1World, par3StructureBoundingBox, 5, 0, 2, 11, 9, 14);
        this.fillWithAir(par1World, par3StructureBoundingBox, 2, 0, 5, 14, 9, 11);

        for (int i = 3; i <= 13; i++)
        {
            for (int j = 3; j <= 13; j++)
            {
                this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(3), i, 0, j, par3StructureBoundingBox);
            }
        }

        for (int i = 5; i <= 11; i++)
        {
            for (int j = 2; j <= 14; j++)
            {
                this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(3), i, 0, j, par3StructureBoundingBox);
            }
        }

        for (int i = 2; i <= 14; i++)
        {
            for (int j = 5; j <= 11; j++)
            {
                this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(3), i, 0, j, par3StructureBoundingBox);
            }
        }

        int yLevel = 0;

        for (yLevel = -8; yLevel < 4; yLevel++)
        {
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 2, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 2, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 3, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 4, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 5, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 6, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 7, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.basicBlock.getStateFromMeta(4) : Blocks.AIR.getDefaultState(), 1, yLevel, 8, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 9, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 10, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 11, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 12, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 13, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 14, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 14, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.basicBlock.getStateFromMeta(4) : Blocks.AIR.getDefaultState(), 8, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 15, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 15, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 14, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 14, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 13, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 12, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 11, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 10, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 9, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.basicBlock.getStateFromMeta(4) : Blocks.AIR.getDefaultState(), 15, yLevel, 8, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 7, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 6, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 5, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 4, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 3, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 2, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 2, par3StructureBoundingBox);

            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, yLevel <= 1 ? GCBlocks.basicBlock.getStateFromMeta(4) : Blocks.AIR.getDefaultState(), 8, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 1, par3StructureBoundingBox);
            this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 1, par3StructureBoundingBox);
        }

        yLevel = 4;

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 4, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 11, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 14, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 15, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 12, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 5, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 2, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 1, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode()), 8, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode().rotateYCCW()), 14, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode().getOpposite()), 8, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.glowstoneTorch.getDefaultState().withProperty(BlockGlowstoneTorch.FACING, this.getCoordBaseMode().rotateY()), 2, yLevel, 8, par3StructureBoundingBox);

        yLevel = 5;

        // corner 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 5, par3StructureBoundingBox);

        // side 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 1, yLevel, 10, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 14, par3StructureBoundingBox);

        // side 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 15, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 15, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 11, par3StructureBoundingBox);

        // side 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 15, yLevel, 6, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 2, par3StructureBoundingBox);

        // side 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 1, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 1, par3StructureBoundingBox);

        yLevel = 6;

        // corner 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 4, par3StructureBoundingBox);

        // side 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 11, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 13, par3StructureBoundingBox);

        // side 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 14, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 12, par3StructureBoundingBox);

        // side 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 5, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 3, par3StructureBoundingBox);

        // side 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 2, par3StructureBoundingBox);

        yLevel = 7;

        // corner 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 6, par3StructureBoundingBox);

        // side 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 2, yLevel, 9, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 13, par3StructureBoundingBox);

        // side 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 14, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 14, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 10, par3StructureBoundingBox);

        // side 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 14, yLevel, 7, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 3, par3StructureBoundingBox);

        // side 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 2, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 2, par3StructureBoundingBox);

        yLevel = 8;

        // corner 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 6, par3StructureBoundingBox);

        // side 1
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 3, yLevel, 9, par3StructureBoundingBox);

        // corner 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 10, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 6, yLevel, 12, par3StructureBoundingBox);

        // side 2
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 13, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 13, par3StructureBoundingBox);

        // corner 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 10, par3StructureBoundingBox);

        // side 3
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 9, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 13, yLevel, 7, par3StructureBoundingBox);

        // corner 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 6, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 10, yLevel, 4, par3StructureBoundingBox);

        // side 4
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 3, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 3, par3StructureBoundingBox);

        // extras
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 5, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 5, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 11, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 11, yLevel, 5, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 4, yLevel, 9, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 12, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 12, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 9, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 8, yLevel, 4, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 7, yLevel, 4, par3StructureBoundingBox);

        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 7, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 8, par3StructureBoundingBox);
        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), 12, yLevel, 9, par3StructureBoundingBox);

        yLevel = 9;

        for (int i = 5; i <= 11; i++)
        {
            for (int j = 5; j <= 11; j++)
            {
                if (!(j == 5 && i == 5 || j == 5 && i == 11 || j == 11 && i == 5 || j == 11 && i == 11))
                {
                    if (i >= 7 && i <= 9 && j >= 7 && j <= 9)
                    {
                        this.setBlockState(par1World, Blocks.GLASS.getDefaultState(), i, yLevel, j, par3StructureBoundingBox);
                    }
                    else
                    {
                        this.setBlockState(par1World, GCBlocks.basicBlock.getStateFromMeta(4), i, yLevel, j, par3StructureBoundingBox);
                    }
                }
            }
        }

        this.spawnVillagers(par1World, par3StructureBoundingBox, 6, 5, 6, 4);
        return true;
    }
}
