//package micdoodle8.mods.galacticraft.core.world.gen;
//
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.MutableBoundingBox;
//import net.minecraft.world.World;
//import net.minecraft.world.gen.feature.StructurePiece;
//import net.minecraft.world.gen.feature.template.TemplateManager;
//
//import java.util.List;
//import java.util.Random;
//
//public class StructureComponentVillagePathGen extends StructureComponentVillageRoadPiece
//{
//    private int averageGroundLevel;
//
//    public StructureComponentVillagePathGen()
//    {
//    }
//
//    public StructureComponentVillagePathGen(StructureComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, MutableBoundingBox par4StructureBoundingBox, Direction par5)
//    {
//        super(par1ComponentVillageStartPiece, par2);
//        this.setCoordBaseMode(par5);
//        this.boundingBox = par4StructureBoundingBox;
//        this.averageGroundLevel = Math.max(par4StructureBoundingBox.getXSize(), par4StructureBoundingBox.getZSize());
//    }
//
//    @Override
//    protected void writeStructureToNBT(CompoundNBT nbt)
//    {
//        super.writeStructureToNBT(nbt);
//
//        nbt.putInt("AvgGroundLevel", this.averageGroundLevel);
//    }
//
//    @Override
//    protected void readStructureFromNBT(CompoundNBT nbt, TemplateManager manager)
//    {
//        super.readStructureFromNBT(nbt, manager);
//
//        this.averageGroundLevel = nbt.getInt("AvgGroundLevel");
//    }
//
//    @Override
//    public void buildComponent(StructurePiece par1StructureComponent, List<StructurePiece> par2List, Random par3Random)
//    {
//        boolean var4 = false;
//        int var5;
//        StructurePiece var6;
//
//        for (var5 = par3Random.nextInt(5); var5 < this.averageGroundLevel - 8; var5 += 2 + par3Random.nextInt(5))
//        {
//            var6 = this.getNextComponentNN((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, 0, var5);
//
//            if (var6 != null)
//            {
//                var5 += Math.max(var6.getBoundingBox().getXSize(), var6.getBoundingBox().getZSize());
//                var4 = true;
//            }
//        }
//
//        for (var5 = par3Random.nextInt(5); var5 < this.averageGroundLevel - 8; var5 += 2 + par3Random.nextInt(5))
//        {
//            var6 = this.getNextComponentPP((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, 0, var5);
//
//            if (var6 != null)
//            {
//                var5 += Math.max(var6.getBoundingBox().getXSize(), var6.getBoundingBox().getZSize());
//                var4 = true;
//            }
//        }
//
//        if (var4 && par3Random.nextInt(3) > 0)
//        {
//            switch (this.getCoordBaseMode().getHorizontalIndex())
//            {
//            case 0:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, Direction.byHorizontalIndex(1), this.getComponentType());
//                break;
//            case 1:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.byHorizontalIndex(2), this.getComponentType());
//                break;
//            case 2:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.byHorizontalIndex(1), this.getComponentType());
//                break;
//            case 3:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.byHorizontalIndex(2), this.getComponentType());
//            }
//        }
//
//        if (var4 && par3Random.nextInt(3) > 0)
//        {
//            switch (this.getCoordBaseMode().getHorizontalIndex())
//            {
//            case 0:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, Direction.byHorizontalIndex(3), this.getComponentType());
//                break;
//            case 1:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.byHorizontalIndex(0), this.getComponentType());
//                break;
//            case 2:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.byHorizontalIndex(3), this.getComponentType());
//                break;
//            case 3:
//                StructureVillagePiecesMoon.getNextStructureComponentVillagePath((StructureComponentVillageStartPiece) par1StructureComponent, par2List, par3Random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.byHorizontalIndex(0), this.getComponentType());
//            }
//        }
//    }
//
//    public static MutableBoundingBox func_74933_a(StructureComponentVillageStartPiece par0ComponentVillageStartPiece, List<StructurePiece> par1List, Random par2Random, int par3, int par4, int par5, Direction par6)
//    {
//        for (int var7 = 7 * MathHelper.nextInt(par2Random, 3, 5); var7 >= 7; var7 -= 7)
//        {
//            final MutableBoundingBox var8 = MutableBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 3, 3, var7, par6);
//
//            if (StructurePiece.findIntersecting(par1List, var8) == null)
//            {
//                return var8;
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * second Part of Structure generating, this for example places Spiderwebs,
//     * Mob Spawners, it closes Mineshafts at the end, it adds Fences...
//     */
//    @Override
//    public boolean addComponentParts(World par1World, Random par2Random, MutableBoundingBox par3StructureBoundingBox)
//    {
//        final Block var4 = Blocks.PLANKS;
//
//        for (int var5 = this.boundingBox.minX; var5 <= this.boundingBox.maxX; ++var5)
//        {
//            for (int var6 = this.boundingBox.minZ; var6 <= this.boundingBox.maxZ; ++var6)
//            {
//                BlockPos pos = new BlockPos(var5, par1World.getTopSolidOrLiquidBlock(new BlockPos(var5, 0, var6)).getY() - 1, var6);
//                BlockState state = par1World.getBlockState(pos);
//                if (par3StructureBoundingBox.isVecInside(new BlockPos(var5, 64, var6)) && (state.getBlock() == GCBlocks.blockMoon && state.getBlock().getMetaFromState(state) == 5 || Blocks.AIR == state.getBlock()))
//                {
//                    final int var7 = par1World.getTopSolidOrLiquidBlock(new BlockPos(var5, 0, var6)).getY() - 1;
//                    par1World.setBlockState(new BlockPos(var5, var7, var6), var4.getStateFromMeta(1), 2);
//                }
//            }
//        }
//
//        return true;
//    }
//}
