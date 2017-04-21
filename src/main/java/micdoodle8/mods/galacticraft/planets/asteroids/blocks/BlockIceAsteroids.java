package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public class BlockIceAsteroids extends BlockBreakable implements ISortableBlock
{
    public BlockIceAsteroids(String assetName)
    {
        super(Material.ice, false);
        this.slipperiness = 0.98F;
        this.setTickRandomly(true);
        this.setHardness(0.5F);
        this.setUnlocalizedName(assetName);
        this.setStepSound(soundTypeGlass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return super.shouldSideBeRendered(world, pos, EnumFacing.getFront(1 - side.getIndex()));
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
    {
        player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(worldIn, pos, worldIn.getBlockState(pos), player) && EnchantmentHelper.getSilkTouchModifier(player))
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = this.createStackedBlock(state);

            if (itemstack != null)
            {
                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack is : items)
            {
                Block.spawnAsEntity(worldIn, pos, is);
            }
        }
        else
        {
            if (GCCoreUtil.getDimensionID(worldIn) == -1 || worldIn.provider instanceof IGalacticraftWorldProvider)
            {
                worldIn.setBlockToAir(pos);
                return;
            }

            int i1 = EnchantmentHelper.getFortuneModifier(player);
            harvesters.set(player);
            this.dropBlockAsItem(worldIn, pos, state, i1);
            harvesters.set(null);
            Material material = worldIn.getBlockState(pos.down()).getBlock().getMaterial();

            if (material.blocksMovement() || material.isLiquid())
            {
                worldIn.setBlockState(pos, Blocks.flowing_water.getDefaultState());
            }
        }
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 13 - this.getLightOpacity())
        {
            if (GCCoreUtil.getDimensionID(worldIn) == -1 || worldIn.provider instanceof IGalacticraftWorldProvider)
            {
                worldIn.setBlockToAir(pos);
                return;
            }

            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockState(pos, Blocks.water.getDefaultState());
        }
    }

    @Override
    public int getMobilityFlag()
    {
        return 0;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
