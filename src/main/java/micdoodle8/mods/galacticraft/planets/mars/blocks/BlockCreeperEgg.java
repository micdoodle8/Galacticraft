package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCreeperEgg extends BlockDragonEgg implements IShiftDescription, ISortableBlock
{
    protected static final AxisAlignedBB DRAGON_EGG_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public BlockCreeperEgg(String assetName)
    {
        super();
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return DRAGON_EGG_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
        if (!world.isRemote)
        {
            EntityEvolvedCreeper creeper = new EntityEvolvedCreeper(world);
            creeper.setPosition(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
            creeper.setChild(true);
            world.spawnEntity(creeper);
        }

        world.setBlockToAir(pos);
        this.onBlockDestroyedByExplosion(world, pos, explosion);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explose)
    {
        return false;
    }

    //Can only be harvested with a Sticky Desh Pickaxe
    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.isEmpty())
        {
            return player.canHarvestBlock(world.getBlockState(pos));
        }
        return stack.getItem() == MarsItems.deshPickSlime;
    }

    @Override
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
    {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null && stack.getItem() == MarsItems.deshPickSlime)
        {
            return 0.2F;
        }
        return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.EGG;
    }
}
