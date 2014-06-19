package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockShortRangeTelepad extends BlockContainer
{
	protected BlockShortRangeTelepad(String assetName) 
	{
		super(Material.iron);
		this.blockHardness = 3.0F;
		this.setBlockName(assetName);
		this.setBlockTextureName("stone");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
    public int getRenderType()
    {
        return -1;
    }
    
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityShortRangeTelepad();
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof IMultiBlock)
		{
			((IMultiBlock) tile).onCreate(new BlockVec3(x, y, z));
		}
	}

	@Override
	public void breakBlock(World var1, int var2, int var3, int var4, Block var5, int var6)
	{
		final TileEntity tileAt = var1.getTileEntity(var2, var3, var4);

		if (tileAt instanceof IMultiBlock)
		{
			((IMultiBlock) tileAt).onDestroy(tileAt);
		}

		super.breakBlock(var1, var2, var3, var4, var5, var6);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) 
    {
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 4; j++)
			{
		    	world.spawnParticle("portal", x + 0.2 + rand.nextDouble() * 0.6, y + 0.1, z + 0.2 + rand.nextDouble() * 0.6, 0.0, 1.75, 0.0);
			}
			
	    	world.spawnParticle("portal", x + 0.0 + rand.nextDouble() * 0.2, y + 2.9, z + rand.nextDouble(), 0.0, -2.95, 0.0);
	    	world.spawnParticle("portal", x + 0.8 + rand.nextDouble() * 0.2, y + 2.9, z + rand.nextDouble(), 0.0, -2.95, 0.0);
	    	world.spawnParticle("portal", x + rand.nextDouble(), y + 2.9, z + 0.2 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
	    	world.spawnParticle("portal", x + rand.nextDouble(), y + 2.9, z + 0.8 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
		}
    }
}
