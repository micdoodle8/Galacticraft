package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockFuelLoader extends BlockAdvanced
{
	private Icon iconMachineSide;
	private Icon iconInput;
	
	public GCCoreBlockFuelLoader(int id) 
	{
		super(id, Material.rock);
	}

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
        this.iconInput = par1IconRegister.func_94245_a("galacticraftcore:machine_power_input");
        this.iconMachineSide = par1IconRegister.func_94245_a("galacticraftcore:machine_blank");
    }

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityFuelLoader();
	}

    @Override
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
    	entityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiFuelLoader, world, x, y, z);
    	return true;
    }

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == metadata + 2)
		{
			return this.iconInput;
		}

		return this.iconMachineSide;
    }

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;
		
		// Re-orient the block
		switch (original)
		{
			case 0:
				change = 3;
				break;
			case 3:
				change = 1;
				break;
			case 1:
				change = 2;
				break;
			case 2:
				change = 0;
				break;
		}

		par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
		return true;
	}
    
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
	{
		int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int change = 0;

		switch (angle)
		{
			case 0:
				change = 3;
				break;
			case 1:
				change = 1;
				break;
			case 2:
				change = 2;
				break;
			case 3:
				change = 0;
				break;
		}

		world.setBlockMetadataWithNotify(x, y, z, change, 3);
		
		for (int dX = -2; dX < 3; dX++)
		{
			for (int dZ = -2; dZ < 3; dZ++)
			{
				int id = world.getBlockId(x + dX, y, z + dZ);
				
				if (id == GCCoreBlocks.landingPadFull.blockID)
				{
			        world.markBlockForUpdate(x + dX, y, z + dZ);
				}
			}
		}
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int par5)
	{
		super.onBlockDestroyedByPlayer(world, x, y, z, par5);
		
		for (int dX = -2; dX < 3; dX++)
		{
			for (int dZ = -2; dZ < 3; dZ++)
			{
				int id = world.getBlockId(x + dX, y, z + dZ);
				
				if (id == GCCoreBlocks.landingPadFull.blockID)
				{
			        world.markBlockForUpdate(x + dX, y, z + dZ);
				}
			}
		}
	}
}
