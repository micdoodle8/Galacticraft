package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerLaunchController;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerTerraformer;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTerraformer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * CommonProxyMars.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class CommonProxyMars implements IGuiHandler
{
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	public void init(FMLInitializationEvent event)
	{

	}

	public void postInit(FMLPostInitializationEvent event)
	{

	}

	public void registerRenderInformation()
	{

	}

	public int getVineRenderID()
	{
		return -1;
	}

	public int getEggRenderID()
	{
		return -1;
	}

	public int getTreasureRenderID()
	{
		return -1;
	}

	public int getMachineRenderID()
	{
		return -1;
	}

	public int getTintedGlassPaneRenderID()
	{
		return -1;
	}

	public void spawnParticle(String var1, double var2, double var4, double var6)
	{
		;
	}

	public void opengSlimelingGui(GCMarsEntitySlimeling slimeling, int gui)
	{
		;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (ID == GCMarsConfigManager.idGuiMachine)
		{
			if (tile instanceof GCMarsTileEntityTerraformer)
			{
				return new GCMarsContainerTerraformer(player.inventory, (GCMarsTileEntityTerraformer) tile);
			}
			else if (tile instanceof GCMarsTileEntityLaunchController)
			{
				return new GCMarsContainerLaunchController(player.inventory, (GCMarsTileEntityLaunchController) tile);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
