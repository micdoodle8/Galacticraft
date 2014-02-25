package micdoodle8.mods.galacticraft.mars.proxy;

import micdoodle8.mods.galacticraft.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.galacticraft.mars.items.MarsItems.EnumArmorIndexMars;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.galacticraft.mars.util.ConfigManagerMars;
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

	public int getArmorRenderID(EnumArmorIndexMars type)
	{
		return -1;
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

	public void opengSlimelingGui(EntitySlimeling slimeling, int gui)
	{
		;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);

		if (ID == ConfigManagerMars.idGuiMachine)
		{
			if (tile instanceof TileEntityTerraformer)
			{
				return new ContainerTerraformer(player.inventory, (TileEntityTerraformer) tile);
			}
			else if (tile instanceof TileEntityLaunchController)
			{
				return new ContainerLaunchController(player.inventory, (TileEntityLaunchController) tile);
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
