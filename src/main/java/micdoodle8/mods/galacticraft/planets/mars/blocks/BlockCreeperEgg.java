package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCreeperEgg extends BlockDragonEgg implements IShiftDescription, ISortableBlock
{
    public BlockCreeperEgg(String assetName)
    {
        super();
        this.setUnlocalizedName(assetName);
    }

    @Override
    public boolean isOpaqueCube()
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
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return null;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
        if (!world.isRemote)
        {
            EntityEvolvedCreeper creeper = new EntityEvolvedCreeper(world);
            creeper.setPosition(pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5);
            creeper.setChild(true);
            world.spawnEntityInWorld(creeper);
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
        if (stack == null)
        {
            return player.canHarvestBlock(this);
        }
        return stack.getItem() == MarsItems.deshPickSlime;
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer playerIn, World worldIn, BlockPos pos)
    {
        ItemStack stack = playerIn.inventory.getCurrentItem();
        if (stack != null && stack.getItem() == MarsItems.deshPickSlime)
        {
            return 0.2F;
        }
        return super.getPlayerRelativeBlockHardness(playerIn, worldIn, pos);
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
