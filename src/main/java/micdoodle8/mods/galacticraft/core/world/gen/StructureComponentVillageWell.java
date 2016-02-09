package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
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

    @Override
    protected void func_143012_a(NBTTagCompound nbt)
    {
        super.func_143012_a(nbt);

        nbt.setInteger("AvgGroundLevel", this.averageGroundLevel);
    }

    @Override
    protected void func_143011_b(NBTTagCompound nbt)
    {
        super.func_143011_b(nbt);

        this.averageGroundLevel = nbt.getInteger("AvgGroundLevel");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, 1, this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, 3, this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, 2, this.getComponentType());
        StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, 0, this.getComponentType());
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

        this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 4, 12, 4, GCBlocks.basicBlock, 4, Blocks.flowing_water, 0, false);
        this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 2, 12, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 3, 12, 2, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 2, 12, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 3, 12, 3, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 1, 13, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 1, 14, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 4, 13, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 4, 14, 1, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 1, 13, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 1, 14, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 4, 13, 4, par3StructureBoundingBox);
        this.placeBlockAtCurrentPosition(par1World, Blocks.fence, 0, 4, 14, 4, par3StructureBoundingBox);
        this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 15, 1, 4, 15, 4, GCBlocks.basicBlock, 4, GCBlocks.basicBlock, 4, false);

        for (int var4 = 0; var4 <= 5; ++var4)
        {
            for (int var5 = 0; var5 <= 5; ++var5)
            {
                if (var5 == 0 || var5 == 5 || var4 == 0 || var4 == 5)
                {
                    this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, var5, 11, var4, par3StructureBoundingBox);
                    this.clearCurrentPositionBlocksUpwards(par1World, var5, 12, var4, par3StructureBoundingBox);
                }
            }
        }

        return true;
    }

    protected void fillWithBlocksAndMetadata(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, Block par9, Block par10, boolean par11)
    {
        final Block var12 = this.getBiomeSpecificBlock(par9, 0);
        final int var13 = this.getBiomeSpecificBlockMetadata(par9, 0);
        final Block var14 = this.getBiomeSpecificBlock(par10, 0);
        final int var15 = this.getBiomeSpecificBlockMetadata(par10, 0);
        super.fillWithMetadataBlocks(par1World, par2StructureBoundingBox, par3, par4, par5, par6, par7, par8, var12, var13, var14, var15, par11);
    }
}
