package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockIceAsteroids extends BreakableBlock
{
    public BlockIceAsteroids(Properties builder)
    {
        super(builder);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack tool)
    {
        player.addStat(Stats.BLOCK_MINED.get(this));
        player.addExhaustion(0.025F);

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0)
        {
            if (worldIn.dimension.getDimension().getType() == DimensionType.THE_NETHER || worldIn.dimension instanceof IGalacticraftDimension)
            {
                worldIn.removeBlock(pos, false);
                return;
            }

            Material material = worldIn.getBlockState(pos.down()).getMaterial();
            if (material.blocksMovement() || material.isLiquid())
            {
                worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
            }
        }
    }

//    @Override
//    public int quantityDropped(Random rand)
//    {
//        return 0;
//    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
    {
        if (worldIn.getLightFor(LightType.BLOCK, pos) > 13 - state.getOpacity(worldIn, pos))
        {
            if (GCCoreUtil.getDimensionID(worldIn) == DimensionType.THE_NETHER || worldIn.dimension instanceof IGalacticraftDimension)
            {
                worldIn.removeBlock(pos, false);
                return;
            }

            spawnAsEntity(worldIn, pos, new ItemStack(this));
//            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
        }
    }

    @Override
    public PushReaction getPushReaction(BlockState state)
    {
        return PushReaction.NORMAL;
    }

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.GENERAL;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
}
