package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBatteryBox;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCoalGenerator;
import micdoodle8.mods.galacticraft.core.inventory.ContainerElectricFurnace;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirCollector;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirCompressor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirDistributor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirSealer;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerCircuitFabricator;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerIngotCompressor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerParachest;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRocketRefill;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerSolar;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class CommonProxyCore implements IGuiHandler
{
    public void preInit(FMLPreInitializationEvent event)
    {
        ;
    }

    public void init(FMLInitializationEvent event)
    {
        ;
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        ;
    }

    public void registerRenderInformation()
    {
        ;
    }

    public int getGCUnlitTorchRenderID()
    {
        return -1;
    }

    public int getGCBreathableAirRenderID()
    {
        return -1;
    }

    public int getGCOxygenPipeRenderID()
    {
        return -1;
    }

    public int getGCTreasureChestRenderID()
    {
        return -1;
    }

    public int getGCMeteorRenderID()
    {
        return -1;
    }

    public int getGCCraftingTableRenderID()
    {
        return -1;
    }

    public int getGCFullLandingPadRenderID()
    {
        return -1;
    }

    public int getTitaniumArmorRenderIndex()
    {
        return 0;
    }

    public int getSensorArmorRenderIndex()
    {
        return 0;
    }

    public World getClientWorld()
    {
        return null;
    }

    public void addSlotRenderer(ICelestialBodyRenderer slotRenderer)
    {
        ;
    }

    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
        ;
    }

    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b)
    {
        ;
    }

    public void displayParachestGui(EntityPlayer player, IInventory lander)
    {
        ;
    }

    // IGUIHANDLER IMPLEMENTATION:

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player);

        if (ID == GCCoreConfigManager.idGuiSpaceshipInventory && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase && player.ridingEntity instanceof IRocketType)
        {
            return new GCCoreContainerRocketRefill(player.inventory, (EntityTieredRocket) player.ridingEntity, ((IRocketType) player.ridingEntity).getType());
        }
        else if (ID == GCCoreConfigManager.idGuiRefinery)
        {
            return new GCCoreContainerRefinery(player.inventory, (GCCoreTileEntityRefinery) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiAirCompressor)
        {
            return new GCCoreContainerAirCompressor(player.inventory, (GCCoreTileEntityOxygenCompressor) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiAirCollector)
        {
            return new GCCoreContainerAirCollector(player.inventory, (GCCoreTileEntityOxygenCollector) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiAirDistributor)
        {
            return new GCCoreContainerAirDistributor(player.inventory, (GCCoreTileEntityOxygenDistributor) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiFuelLoader)
        {
            return new GCCoreContainerFuelLoader(player.inventory, (GCCoreTileEntityFuelLoader) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiAirSealer)
        {
            return new GCCoreContainerAirSealer(player.inventory, (GCCoreTileEntityOxygenSealer) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiCargoLoader)
        {
            return new GCCoreContainerCargoLoader(player.inventory, (IInventory) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiParachest)
        {
            return new GCCoreContainerParachest(player.inventory, (IInventory) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiSolarPanel)
        {
            return new GCCoreContainerSolar(player.inventory, (GCCoreTileEntitySolar) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiExtendedInventory)
        {
            return new GCCoreContainerExtendedInventory(player, playerBase.getExtendedInventory());
        }
        else
        {
            for (final ISchematicPage page : playerBase.getUnlockedSchematics())
            {
                if (ID == page.getGuiID())
                {
                    final Container container = page.getResultContainer(playerBase, x, y, z);

                    return container;
                }
            }
        }

        if (tileEntity != null)
        {
            if (tileEntity instanceof GCCoreTileEntityEnergyStorageModule)
            {
                return new ContainerBatteryBox(player.inventory, (GCCoreTileEntityEnergyStorageModule) tileEntity);
            }
            else if (tileEntity instanceof GCCoreTileEntityCoalGenerator)
            {
                return new ContainerCoalGenerator(player.inventory, (GCCoreTileEntityCoalGenerator) tileEntity);
            }
            else if (tileEntity instanceof GCCoreTileEntityElectricFurnace)
            {
                return new ContainerElectricFurnace(player.inventory, (GCCoreTileEntityElectricFurnace) tileEntity);
            }
            else if (tileEntity instanceof GCCoreTileEntityIngotCompressor)
            {
                return new GCCoreContainerIngotCompressor(player.inventory, (GCCoreTileEntityIngotCompressor) tileEntity);
            }
            else if (tileEntity instanceof GCCoreTileEntityElectricIngotCompressor)
            {
                return new GCCoreContainerElectricIngotCompressor(player.inventory, (GCCoreTileEntityElectricIngotCompressor) tileEntity);
            }
            else if (tileEntity instanceof GCCoreTileEntityCircuitFabricator)
            {
                return new GCCoreContainerCircuitFabricator(player.inventory, (GCCoreTileEntityCircuitFabricator) tileEntity);
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
