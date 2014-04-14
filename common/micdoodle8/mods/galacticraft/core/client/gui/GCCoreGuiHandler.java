package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirCollector;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirCompressor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirDecompressor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirDistributor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirSealer;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerCircuitFabricator;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerParachest;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRocketRefill;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerSolar;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiHandler.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		if (playerBase == null)
		{
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Galacticraft player instance null server-side. This is a bug."));
			return null;
		}

		if (ID == GCCoreConfigManager.idGuiSpaceshipInventory && player.ridingEntity instanceof EntityTieredRocket)
		{
			return new GCCoreContainerRocketRefill(player.inventory, (EntityTieredRocket) player.ridingEntity, ((EntityTieredRocket) player.ridingEntity).getType());
		}
		else if (ID == GCCoreConfigManager.idGuiExtendedInventory)
		{
			return new GCCoreContainerExtendedInventory(player, playerBase.getExtendedInventory());
		}

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile != null)
		{
			if (tile instanceof GCCoreTileEntityRefinery)
			{
				return new GCCoreContainerRefinery(player.inventory, (GCCoreTileEntityRefinery) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenCollector)
			{
				return new GCCoreContainerAirCollector(player.inventory, (GCCoreTileEntityOxygenCollector) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenDistributor)
			{
				return new GCCoreContainerAirDistributor(player.inventory, (GCCoreTileEntityOxygenDistributor) tile);
			}
			else if (tile instanceof GCCoreTileEntityFuelLoader)
			{
				return new GCCoreContainerFuelLoader(player.inventory, (GCCoreTileEntityFuelLoader) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenSealer)
			{
				return new GCCoreContainerAirSealer(player.inventory, (GCCoreTileEntityOxygenSealer) tile);
			}
			else if (tile instanceof GCCoreTileEntityCargoLoader)
			{
				return new GCCoreContainerCargoLoader(player.inventory, (GCCoreTileEntityCargoLoader) tile);
			}
			else if (tile instanceof GCCoreTileEntityParachest)
			{
				return new GCCoreContainerParachest(player.inventory, (GCCoreTileEntityParachest) tile);
			}
			else if (tile instanceof GCCoreTileEntitySolar)
			{
				return new GCCoreContainerSolar(player.inventory, (GCCoreTileEntitySolar) tile);
			}
			else if (tile instanceof GCCoreTileEntityEnergyStorageModule)
			{
				return new GCCoreContainerEnergyStorageModule(player.inventory, (GCCoreTileEntityEnergyStorageModule) tile);
			}
			else if (tile instanceof GCCoreTileEntityCoalGenerator)
			{
				return new ContainerCoalGenerator(player.inventory, (GCCoreTileEntityCoalGenerator) tile);
			}
			else if (tile instanceof GCCoreTileEntityElectricFurnace)
			{
				return new ContainerElectricFurnace(player.inventory, (GCCoreTileEntityElectricFurnace) tile);
			}
			else if (tile instanceof GCCoreTileEntityIngotCompressor)
			{
				return new GCCoreContainerIngotCompressor(player.inventory, (GCCoreTileEntityIngotCompressor) tile);
			}
			else if (tile instanceof GCCoreTileEntityElectricIngotCompressor)
			{
				return new GCCoreContainerElectricIngotCompressor(player.inventory, (GCCoreTileEntityElectricIngotCompressor) tile);
			}
			else if (tile instanceof GCCoreTileEntityCircuitFabricator)
			{
				return new GCCoreContainerCircuitFabricator(player.inventory, (GCCoreTileEntityCircuitFabricator) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenStorageModule)
			{
				return new GCCoreContainerOxygenStorageModule(player.inventory, (GCCoreTileEntityOxygenStorageModule) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenCompressor)
			{
				return new GCCoreContainerAirCompressor(player.inventory, (GCCoreTileEntityOxygenCompressor) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenDecompressor)
			{
				return new GCCoreContainerAirDecompressor(player.inventory, (GCCoreTileEntityOxygenDecompressor) tile);
			}
		}

		for (ISchematicPage page : playerBase.getUnlockedSchematics())
		{
			if (ID == page.getGuiID())
			{
				final Container container = page.getResultContainer(playerBase, x, y, z);

				return container;
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
		if (ID == GCCoreConfigManager.idGuiGalaxyMap)
		{
			return new GCCoreGuiGalaxyMap(player);
		}
		else if (ID == GCCoreConfigManager.idGuiSpaceshipInventory && player.ridingEntity instanceof EntityTieredRocket)
		{
			return new GCCoreGuiRocketRefill(player.inventory, (EntityTieredRocket) player.ridingEntity, ((EntityTieredRocket) player.ridingEntity).getType());
		}
		else if (ID == GCCoreConfigManager.idGuiExtendedInventory)
		{
			return new GCCoreGuiExtendedInventory(player, ClientProxyCore.dummyInventory);
		}
		else if (ID == GCCoreConfigManager.idGuiKnowledgeBook)
		{
			return new GCCoreGuiManual(new ItemStack(Block.stone), ClientProxyCore.materialsTest);
		}

		TileEntity tile = world.getBlockTileEntity(position.intX(), position.intY(), position.intZ());

		if (tile != null)
		{
			if (tile instanceof GCCoreTileEntityRefinery)
			{
				return new GCCoreGuiRefinery(player.inventory, (GCCoreTileEntityRefinery) world.getBlockTileEntity(position.intX(), position.intY(), position.intZ()));
			}
			else if (tile instanceof GCCoreTileEntityOxygenCollector)
			{
				return new GCCoreGuiAirCollector(player.inventory, (GCCoreTileEntityOxygenCollector) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenDistributor)
			{
				return new GCCoreGuiAirDistributor(player.inventory, (GCCoreTileEntityOxygenDistributor) tile);
			}
			else if (tile instanceof GCCoreTileEntityFuelLoader)
			{
				return new GCCoreGuiFuelLoader(player.inventory, (GCCoreTileEntityFuelLoader) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenSealer)
			{
				return new GCCoreGuiAirSealer(player.inventory, (GCCoreTileEntityOxygenSealer) tile);
			}
			else if (tile instanceof GCCoreTileEntityCargoLoader)
			{
				return new GCCoreGuiCargoLoader(player.inventory, (GCCoreTileEntityCargoLoader) tile);
			}
			else if (tile instanceof GCCoreTileEntityCargoUnloader)
			{
				return new GCCoreGuiCargoUnloader(player.inventory, (GCCoreTileEntityCargoUnloader) tile);
			}
			else if (tile instanceof GCCoreTileEntityParachest)
			{
				return new GCCoreGuiParachest(player.inventory, (GCCoreTileEntityParachest) tile);
			}
			else if (tile instanceof GCCoreTileEntitySolar)
			{
				return new GCCoreGuiSolar(player.inventory, (GCCoreTileEntitySolar) tile);
			}
			else if (tile instanceof GCCoreTileEntityAirLockController)
			{
				return new GCCoreGuiAirLockController((GCCoreTileEntityAirLockController) tile);
			}
			else if (tile instanceof GCCoreTileEntityEnergyStorageModule)
			{
				return new GCCoreGuiEnergyStorageModule(player.inventory, (GCCoreTileEntityEnergyStorageModule) tile);
			}
			else if (tile instanceof GCCoreTileEntityCoalGenerator)
			{
				return new GCCoreGuiCoalGenerator(player.inventory, (GCCoreTileEntityCoalGenerator) tile);
			}
			else if (tile instanceof GCCoreTileEntityElectricFurnace)
			{
				return new GCCoreGuiElectricFurnace(player.inventory, (GCCoreTileEntityElectricFurnace) tile);
			}
			else if (tile instanceof GCCoreTileEntityIngotCompressor)
			{
				return new GCCoreGuiIngotCompressor(player.inventory, (GCCoreTileEntityIngotCompressor) tile);
			}
			else if (tile instanceof GCCoreTileEntityElectricIngotCompressor)
			{
				return new GCCoreGuiElectricIngotCompressor(player.inventory, (GCCoreTileEntityElectricIngotCompressor) tile);
			}
			else if (tile instanceof GCCoreTileEntityCircuitFabricator)
			{
				return new GCCoreGuiCircuitFabricator(player.inventory, (GCCoreTileEntityCircuitFabricator) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenStorageModule)
			{
				return new GCCoreGuiOxygenStorageModule(player.inventory, (GCCoreTileEntityOxygenStorageModule) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenCompressor)
			{
				return new GCCoreGuiAirCompressor(player.inventory, (GCCoreTileEntityOxygenCompressor) tile);
			}
			else if (tile instanceof GCCoreTileEntityOxygenDecompressor)
			{
				return new GCCoreGuiAirDecompressor(player.inventory, (GCCoreTileEntityOxygenDecompressor) tile);
			}
		}

		GCCorePlayerSP playerClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

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
