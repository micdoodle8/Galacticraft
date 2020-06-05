package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public class BlockIceAsteroids extends BreakableBlock implements ISortableBlock
{
    public BlockIceAsteroids(Properties builder)
    {
        super(builder, false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getBlockLayer()
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
        player.addStat(Stats.getBlockStats(this));
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0)
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            items.add(this.getSilkTouchDrop(state));

            ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack is : items)
            {
                Block.spawnAsEntity(worldIn, pos, is);
            }
        }
        else
        {
            if (worldIn.provider.getDimension() == -1 || worldIn.provider instanceof IGalacticraftWorldProvider)
            {
                worldIn.removeBlock(pos, false);
                return;
            }

            int i1 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
            harvesters.set(player);
            this.dropBlockAsItem(worldIn, pos, state, i1);
            harvesters.set(null);
            BlockState state1 = worldIn.getBlockState(pos.down());
            Material material = state1.getMaterial();

            if (material.blocksMovement() || material.isLiquid())
            {
                worldIn.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState());
            }
        }
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand)
    {
        if (worldIn.getLightFor(LightType.BLOCK, pos) > 13 - this.getLightOpacity(state))
        {
            if (GCCoreUtil.getDimensionID(worldIn) == -1 || worldIn.provider instanceof IGalacticraftWorldProvider)
            {
                worldIn.removeBlock(pos, false);
                return;
            }

            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
        }
    }

    @Override
    public PushReaction getMobilityFlag(BlockState state)
    {
        return PushReaction.NORMAL;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
