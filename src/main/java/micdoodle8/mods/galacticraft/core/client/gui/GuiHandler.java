//package micdoodle8.mods.galacticraft.core.client.gui;
//
//import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
//import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
//import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
//import micdoodle8.mods.galacticraft.core.GCItems;
//import micdoodle8.mods.galacticraft.core.client.gui.container.*;
//import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
//import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiJoinSpaceRace;
//import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiNewSpaceRace;
//import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiPreLaunchChecklist;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
//import micdoodle8.mods.galacticraft.core.inventory.*;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.tile.*;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
//import micdoodle8.mods.galacticraft.core.util.WorldUtil;
//import net.minecraft.client.entity.player.ClientPlayerEntity;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.common.network.IGuiHandler;
//
//public class GuiHandler implements IGuiHandler
//{
//    @Override
//    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        ServerPlayerEntity playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);
//
//        if (playerBase == null)
//        {
//            player.sendMessage(new StringTextComponent("Galacticraft player instance null server-LogicalSide. This is a bug."));
//            return null;
//        }
//
//        GCPlayerStats stats = GCPlayerStats.get(playerBase);
//
//        if (ID == GuiIdsCore.ROCKET_INVENTORY && player.getRidingEntity() instanceof EntityTieredRocket)
//        {
//            return new ContainerRocketInventory(player.inventory, (EntityTieredRocket) player.getRidingEntity(), ((EntityTieredRocket) player.getRidingEntity()).getRocketType(), player);
//        }
//        else if (ID == GuiIdsCore.EXTENDED_INVENTORY)
//        {
//            return new ContainerExtendedInventory(player, stats.getExtendedInventory());
//        }
//
//        BlockPos pos = new BlockPos(x, y, z);
//        TileEntity tile = world.getTileEntity(pos);
//
//        if (tile != null)
//        {
//            if (tile instanceof TileEntityCrafting)
//            {
//                return new ContainerCrafting(player.inventory, (TileEntityCrafting) tile);
//            }
//            else if (tile instanceof TileEntityRefinery)
//            {
//                return new ContainerRefinery(player.inventory, (TileEntityRefinery) tile, player);
//            }
//            else if (tile instanceof TileEntityOxygenCollector)
//            {
//                return new ContainerOxygenCollector(player.inventory, (TileEntityOxygenCollector) tile);
//            }
//            else if (tile instanceof TileEntityOxygenDistributor)
//            {
//                return new ContainerOxygenDistributor(player.inventory, (TileEntityOxygenDistributor) tile);
//            }
//            else if (tile instanceof TileEntityFuelLoader)
//            {
//                return new ContainerFuelLoader(player.inventory, (TileEntityFuelLoader) tile);
//            }
//            else if (tile instanceof TileEntityOxygenSealer)
//            {
//                return new ContainerOxygenSealer(player.inventory, (TileEntityOxygenSealer) tile);
//            }
//            else if (tile instanceof TileEntityCargoLoader)
//            {
//                return new ContainerCargoLoader(player.inventory, (TileEntityCargoLoader) tile);
//            }
//            else if (tile instanceof TileEntityCargoUnloader)
//            {
//                return new ContainerCargoLoader(player.inventory, (TileEntityCargoUnloader) tile);
//            }
//            else if (tile instanceof TileEntityParaChest)
//            {
//                return new ContainerParaChest(player.inventory, (TileEntityParaChest) tile, player);
//            }
//            else if (tile instanceof TileEntitySolar)
//            {
//                return new ContainerSolar(player.inventory, (TileEntitySolar) tile);
//            }
//            else if (tile instanceof TileEntityEnergyStorageModule)
//            {
//                return new ContainerEnergyStorageModule(player.inventory, (TileEntityEnergyStorageModule) tile);
//            }
//            else if (tile instanceof TileEntityCoalGenerator)
//            {
//                return new ContainerCoalGenerator(player.inventory, (TileEntityCoalGenerator) tile);
//            }
//            else if (tile instanceof TileEntityElectricFurnace)
//            {
//                return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace) tile);
//            }
//            else if (tile instanceof TileEntityIngotCompressor)
//            {
//                return new ContainerIngotCompressor(player.inventory, (TileEntityIngotCompressor) tile);
//            }
//            else if (tile instanceof TileEntityElectricIngotCompressor)
//            {
//                return new ContainerElectricIngotCompressor(player.inventory, (TileEntityElectricIngotCompressor) tile);
//            }
//            else if (tile instanceof TileEntityCircuitFabricator)
//            {
//                return new ContainerCircuitFabricator(player.inventory, (TileEntityCircuitFabricator) tile);
//            }
//            else if (tile instanceof TileEntityOxygenStorageModule)
//            {
//                return new ContainerOxygenStorageModule(player.inventory, (TileEntityOxygenStorageModule) tile);
//            }
//            else if (tile instanceof TileEntityOxygenCompressor)
//            {
//                return new ContainerOxygenCompressor(player.inventory, (TileEntityOxygenCompressor) tile, player);
//            }
//            else if (tile instanceof TileEntityOxygenDecompressor)
//            {
//                return new ContainerOxygenDecompressor(player.inventory, (TileEntityOxygenDecompressor) tile, player);
//            }
//            else if (tile instanceof TileEntityDeconstructor)
//            {
//                return new ContainerDeconstructor(player.inventory, (TileEntityDeconstructor) tile);
//            }
//            else if (tile instanceof TileEntityPainter)
//            {
//                return new ContainerPainter(player.inventory, (TileEntityPainter) tile);
//            }
//        }
//
//        for (ISchematicPage page : stats.getUnlockedSchematics())
//        {
//            if (ID == page.getGuiID())
//            {
//                return page.getResultContainer(playerBase, new BlockPos(x, y, z));
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
//        {
//            return this.getClientGuiElement(ID, player, world, new BlockPos(x, y, z));
//        }
//
//        return null;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private Object getClientGuiElement(int ID, PlayerEntity player, World world, BlockPos position)
//    {
//        ClientPlayerEntity playerClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
//
//        if (ID == GuiIdsCore.GALAXY_MAP)
//        {
//            return new GuiCelestialSelection(true, null, true);
//        }
//        else if (ID == GuiIdsCore.ROCKET_INVENTORY && player.getRidingEntity() instanceof EntityTieredRocket)
//        {
//            return new GuiRocketInventory(player.inventory, (EntityTieredRocket) player.getRidingEntity(), ((EntityTieredRocket) player.getRidingEntity()).getRocketType());
//        }
//        else if (ID == GuiIdsCore.EXTENDED_INVENTORY)
//        {
//            return new GuiExtendedInventory(player, ClientProxyCore.dummyInventory);
//        }
//        else if (ID == GuiIdsCore.SPACE_RACE_START)
//        {
//            return new GuiNewSpaceRace(player);
//        }
//        else if (ID == GuiIdsCore.SPACE_RACE_JOIN)
//        {
//            return new GuiJoinSpaceRace(playerClient);
//        }
//        else if (ID == GuiIdsCore.PRE_LAUNCH_CHECKLIST)
//        {
//            ItemStack checkList = GCCoreUtil.getMatchingItemEitherHand(playerClient, GCItems.prelaunchChecklist);
//            return new GuiPreLaunchChecklist(WorldUtil.getAllChecklistKeys(), checkList != null && checkList.hasTag() ? (CompoundNBT) checkList.getTag().get("checklistData") : null);
//        }
//
//        TileEntity tile = world.getTileEntity(position);
//
//        if (tile != null)
//        {
//            if (tile instanceof TileEntityCrafting)
//            {
//                return new GuiCrafting(player.inventory, (TileEntityCrafting) tile);
//            }
//            else if (tile instanceof TileEntityRefinery)
//            {
//                return new GuiRefinery(player.inventory, (TileEntityRefinery) world.getTileEntity(position));
//            }
//            else if (tile instanceof TileEntityOxygenCollector)
//            {
//                return new GuiOxygenCollector(player.inventory, (TileEntityOxygenCollector) tile);
//            }
//            else if (tile instanceof TileEntityOxygenDistributor)
//            {
//                return new GuiOxygenDistributor(player.inventory, (TileEntityOxygenDistributor) tile);
//            }
//            else if (tile instanceof TileEntityFuelLoader)
//            {
//                return new GuiFuelLoader(player.inventory, (TileEntityFuelLoader) tile);
//            }
//            else if (tile instanceof TileEntityOxygenSealer)
//            {
//                return new GuiOxygenSealer(player.inventory, (TileEntityOxygenSealer) tile);
//            }
//            else if (tile instanceof TileEntityCargoLoader)
//            {
//                return new GuiCargoLoader(player.inventory, (TileEntityCargoLoader) tile);
//            }
//            else if (tile instanceof TileEntityCargoUnloader)
//            {
//                return new GuiCargoUnloader(player.inventory, (TileEntityCargoUnloader) tile);
//            }
//            else if (tile instanceof TileEntityParaChest)
//            {
//                return new GuiParaChest(player.inventory, (TileEntityParaChest) tile);
//            }
//            else if (tile instanceof TileEntitySolar)
//            {
//                return new GuiSolar(player.inventory, (TileEntitySolar) tile);
//            }
//            else if (tile instanceof TileEntityAirLockController)
//            {
//                return new GuiAirLockController((TileEntityAirLockController) tile);
//            }
//            else if (tile instanceof TileEntityEnergyStorageModule)
//            {
//                return new GuiEnergyStorageModule(player.inventory, (TileEntityEnergyStorageModule) tile);
//            }
//            else if (tile instanceof TileEntityCoalGenerator)
//            {
//                return new GuiCoalGenerator(player.inventory, (TileEntityCoalGenerator) tile);
//            }
//            else if (tile instanceof TileEntityElectricFurnace)
//            {
//                return new GuiElectricFurnace(player.inventory, (TileEntityElectricFurnace) tile);
//            }
//            else if (tile instanceof TileEntityIngotCompressor)
//            {
//                return new GuiIngotCompressor(player.inventory, (TileEntityIngotCompressor) tile);
//            }
//            else if (tile instanceof TileEntityElectricIngotCompressor)
//            {
//                return new GuiElectricIngotCompressor(player.inventory, (TileEntityElectricIngotCompressor) tile);
//            }
//            else if (tile instanceof TileEntityCircuitFabricator)
//            {
//                return new GuiCircuitFabricator(player.inventory, (TileEntityCircuitFabricator) tile);
//            }
//            else if (tile instanceof TileEntityOxygenStorageModule)
//            {
//                return new GuiOxygenStorageModule(player.inventory, (TileEntityOxygenStorageModule) tile);
//            }
//            else if (tile instanceof TileEntityOxygenCompressor)
//            {
//                return new GuiOxygenCompressor(player.inventory, (TileEntityOxygenCompressor) tile);
//            }
//            else if (tile instanceof TileEntityOxygenDecompressor)
//            {
//                return new GuiOxygenDecompressor(player.inventory, (TileEntityOxygenDecompressor) tile);
//            }
//            else if (tile instanceof TileEntityDeconstructor)
//            {
//                return new GuiDeconstructor(player.inventory, (TileEntityDeconstructor) tile);
//            }
//            else if (tile instanceof TileEntityPainter)
//            {
//                return new GuiPainter(player.inventory, (TileEntityPainter) tile);
//            }
//        }
//
//        if (playerClient != null)
//        {
//            GCPlayerStatsClient stats = GCPlayerStatsClient.get(playerClient);
//
//            for (ISchematicPage page : stats.getUnlockedSchematics())
//            {
//                if (ID == page.getGuiID())
//                {
//                    Screen screen = page.getResultScreen(playerClient, position);
//
//                    if (screen instanceof ISchematicResultPage)
//                    {
//                        ((ISchematicResultPage) screen).setPageIndex(page.getPageID());
//                    }
//
//                    return screen;
//                }
//            }
//        }
//
//        return null;
//    }
//}
