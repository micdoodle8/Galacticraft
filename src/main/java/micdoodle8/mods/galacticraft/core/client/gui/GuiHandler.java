package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiAirLockController;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiCargoLoader;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiCargoUnloader;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiCircuitFabricator;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiCoalGenerator;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiElectricFurnace;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiExtendedInventory;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiFuelLoader;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiIngotCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiOxygenCollector;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiOxygenCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiOxygenDistributor;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiOxygenSealer;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiParaChest;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiRefinery;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiRocketInventory;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSolar;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiGalaxyMap;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiJoinSpaceRace;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiNewSpaceRace;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCircuitFabricator;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.inventory.ContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.ContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.inventory.ContainerIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenCollector;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenCompressor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenDistributor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenSealer;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRefinery;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSolar;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		GCEntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		if (playerBase == null)
		{
			player.addChatMessage(new ChatComponentText("Galacticraft player instance null server-side. This is a bug."));
			return null;
		}

		if (ID == GuiIdsCore.ROCKET_INVENTORY && player.ridingEntity instanceof EntityTieredRocket)
		{
			return new ContainerRocketInventory(player.inventory, (EntityTieredRocket) player.ridingEntity, ((EntityTieredRocket) player.ridingEntity).getType());
		}
		else if (ID == GuiIdsCore.EXTENDED_INVENTORY)
		{
			return new ContainerExtendedInventory(player, playerBase.getPlayerStats().extendedInventory);
		}

		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile != null)
		{
			if (tile instanceof TileEntityRefinery)
			{
				return new ContainerRefinery(player.inventory, (TileEntityRefinery) tile);
			}
			else if (tile instanceof TileEntityOxygenCollector)
			{
				return new ContainerOxygenCollector(player.inventory, (TileEntityOxygenCollector) tile);
			}
			else if (tile instanceof TileEntityOxygenDistributor)
			{
				return new ContainerOxygenDistributor(player.inventory, (TileEntityOxygenDistributor) tile);
			}
			else if (tile instanceof TileEntityFuelLoader)
			{
				return new ContainerFuelLoader(player.inventory, (TileEntityFuelLoader) tile);
			}
			else if (tile instanceof TileEntityOxygenSealer)
			{
				return new ContainerOxygenSealer(player.inventory, (TileEntityOxygenSealer) tile);
			}
			else if (tile instanceof TileEntityCargoLoader)
			{
				return new ContainerCargoLoader(player.inventory, (TileEntityCargoLoader) tile);
			}
			else if (tile instanceof TileEntityCargoUnloader)
			{
				return new ContainerCargoLoader(player.inventory, (TileEntityCargoUnloader) tile);
			}
			else if (tile instanceof TileEntityParaChest)
			{
				return new ContainerParaChest(player.inventory, (TileEntityParaChest) tile);
			}
			else if (tile instanceof TileEntitySolar)
			{
				return new ContainerSolar(player.inventory, (TileEntitySolar) tile);
			}
			else if (tile instanceof TileEntityEnergyStorageModule)
			{
				return new ContainerEnergyStorageModule(player.inventory, (TileEntityEnergyStorageModule) tile);
			}
			else if (tile instanceof TileEntityCoalGenerator)
			{
				return new ContainerCoalGenerator(player.inventory, (TileEntityCoalGenerator) tile);
			}
			else if (tile instanceof TileEntityElectricFurnace)
			{
				return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace) tile);
			}
			else if (tile instanceof TileEntityIngotCompressor)
			{
				return new ContainerIngotCompressor(player.inventory, (TileEntityIngotCompressor) tile);
			}
			else if (tile instanceof TileEntityElectricIngotCompressor)
			{
				return new ContainerElectricIngotCompressor(player.inventory, (TileEntityElectricIngotCompressor) tile);
			}
			else if (tile instanceof TileEntityCircuitFabricator)
			{
				return new ContainerCircuitFabricator(player.inventory, (TileEntityCircuitFabricator) tile);
			}
			else if (tile instanceof TileEntityOxygenStorageModule)
			{
				return new ContainerOxygenStorageModule(player.inventory, (TileEntityOxygenStorageModule) tile);
			}
			else if (tile instanceof TileEntityOxygenCompressor)
			{
				return new ContainerOxygenCompressor(player.inventory, (TileEntityOxygenCompressor) tile);
			}
			else if (tile instanceof TileEntityOxygenDecompressor)
			{
				return new ContainerOxygenDecompressor(player.inventory, (TileEntityOxygenDecompressor) tile);
			}
		}

		for (ISchematicPage page : playerBase.getPlayerStats().unlockedSchematics)
		{
			if (ID == page.getGuiID())
			{
				return page.getResultContainer(playerBase, x, y, z);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			return this.getClientGuiElement(ID, player, world, new Vector3(x, y, z));
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	private Object getClientGuiElement(int ID, EntityPlayer player, World world, Vector3 position)
	{
		GCEntityClientPlayerMP playerClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

		if (ID == GuiIdsCore.GALAXY_MAP)
		{
			return new GuiGalaxyMap(player);
		}
		else if (ID == GuiIdsCore.ROCKET_INVENTORY && player.ridingEntity instanceof EntityTieredRocket)
		{
			return new GuiRocketInventory(player.inventory, (EntityTieredRocket) player.ridingEntity, ((EntityTieredRocket) player.ridingEntity).getType());
		}
		else if (ID == GuiIdsCore.EXTENDED_INVENTORY)
		{
			return new GuiExtendedInventory(player, ClientProxyCore.dummyInventory);
		}
		else if (ID == GuiIdsCore.SPACE_RACE_START)
		{
			return new GuiNewSpaceRace(player);
		}
		else if (ID == GuiIdsCore.SPACE_RACE_JOIN)
		{
			return new GuiJoinSpaceRace(playerClient);
		}

		TileEntity tile = world.getTileEntity(position.intX(), position.intY(), position.intZ());

		if (tile != null)
		{
			if (tile instanceof TileEntityRefinery)
			{
				return new GuiRefinery(player.inventory, (TileEntityRefinery) world.getTileEntity(position.intX(), position.intY(), position.intZ()));
			}
			else if (tile instanceof TileEntityOxygenCollector)
			{
				return new GuiOxygenCollector(player.inventory, (TileEntityOxygenCollector) tile);
			}
			else if (tile instanceof TileEntityOxygenDistributor)
			{
				return new GuiOxygenDistributor(player.inventory, (TileEntityOxygenDistributor) tile);
			}
			else if (tile instanceof TileEntityFuelLoader)
			{
				return new GuiFuelLoader(player.inventory, (TileEntityFuelLoader) tile);
			}
			else if (tile instanceof TileEntityOxygenSealer)
			{
				return new GuiOxygenSealer(player.inventory, (TileEntityOxygenSealer) tile);
			}
			else if (tile instanceof TileEntityCargoLoader)
			{
				return new GuiCargoLoader(player.inventory, (TileEntityCargoLoader) tile);
			}
			else if (tile instanceof TileEntityCargoUnloader)
			{
				return new GuiCargoUnloader(player.inventory, (TileEntityCargoUnloader) tile);
			}
			else if (tile instanceof TileEntityParaChest)
			{
				return new GuiParaChest(player.inventory, (TileEntityParaChest) tile);
			}
			else if (tile instanceof TileEntitySolar)
			{
				return new GuiSolar(player.inventory, (TileEntitySolar) tile);
			}
			else if (tile instanceof TileEntityAirLockController)
			{
				return new GuiAirLockController((TileEntityAirLockController) tile);
			}
			else if (tile instanceof TileEntityEnergyStorageModule)
			{
				return new GuiEnergyStorageModule(player.inventory, (TileEntityEnergyStorageModule) tile);
			}
			else if (tile instanceof TileEntityCoalGenerator)
			{
				return new GuiCoalGenerator(player.inventory, (TileEntityCoalGenerator) tile);
			}
			else if (tile instanceof TileEntityElectricFurnace)
			{
				return new GuiElectricFurnace(player.inventory, (TileEntityElectricFurnace) tile);
			}
			else if (tile instanceof TileEntityIngotCompressor)
			{
				return new GuiIngotCompressor(player.inventory, (TileEntityIngotCompressor) tile);
			}
			else if (tile instanceof TileEntityElectricIngotCompressor)
			{
				return new GuiElectricIngotCompressor(player.inventory, (TileEntityElectricIngotCompressor) tile);
			}
			else if (tile instanceof TileEntityCircuitFabricator)
			{
				return new GuiCircuitFabricator(player.inventory, (TileEntityCircuitFabricator) tile);
			}
			else if (tile instanceof TileEntityOxygenStorageModule)
			{
				return new GuiOxygenStorageModule(player.inventory, (TileEntityOxygenStorageModule) tile);
			}
			else if (tile instanceof TileEntityOxygenCompressor)
			{
				return new GuiOxygenCompressor(player.inventory, (TileEntityOxygenCompressor) tile);
			}
			else if (tile instanceof TileEntityOxygenDecompressor)
			{
				return new GuiOxygenDecompressor(player.inventory, (TileEntityOxygenDecompressor) tile);
			}
		}

		if (playerClient != null)
		{
			for (ISchematicPage page : playerClient.unlockedSchematics)
			{
				if (ID == page.getGuiID())
				{
					GuiScreen screen = page.getResultScreen(playerClient, position.intX(), position.intY(), position.intZ());

					if (screen instanceof ISchematicResultPage)
					{
						((ISchematicResultPage) screen).setPageIndex(page.getPageID());
					}

					return screen;
				}
			}
		}

		return null;
	}
}
