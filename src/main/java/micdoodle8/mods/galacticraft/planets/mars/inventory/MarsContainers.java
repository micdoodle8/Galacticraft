package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.*;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.IForgeRegistry;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.*;

public class MarsContainers
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<ContainerType<?>> evt)
    {
        IForgeRegistry<ContainerType<?>> r = evt.getRegistry();

        ContainerType<ContainerElectrolyzer> electrolyzer = IForgeContainerType.create((windowId, inv, data) -> new ContainerElectrolyzer(windowId, inv, (TileEntityElectrolyzer) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerGasLiquefier> gasLiquefier = IForgeContainerType.create((windowId, inv, data) -> new ContainerGasLiquefier(windowId, inv, (TileEntityGasLiquefier) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerLaunchController> launchController = IForgeContainerType.create((windowId, inv, data) -> new ContainerLaunchController(windowId, inv, (TileEntityLaunchController) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerLaunchController> launchControllerAdvanced = IForgeContainerType.create((windowId, inv, data) -> new ContainerLaunchController(windowId, inv, (TileEntityLaunchController) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerMethaneSynthesizer> methaneSynthesizer = IForgeContainerType.create((windowId, inv, data) -> new ContainerMethaneSynthesizer(windowId, inv, (TileEntityMethaneSynthesizer) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerSchematicTier2Rocket> schematicT2Rocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicTier2Rocket(windowId, inv));
        ContainerType<ContainerSchematicCargoRocket> schematicCargoRocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicCargoRocket(windowId, inv));
        ContainerType<ContainerSlimeling> slimeling = IForgeContainerType.create((windowId, inv, data) -> new ContainerSlimeling(windowId, inv, (EntitySlimeling) inv.player.world.getEntityByID(data.readInt())));
        ContainerType<ContainerTerraformer> terraformer = IForgeContainerType.create((windowId, inv, data) -> new ContainerTerraformer(windowId, inv, (TileEntityTerraformer) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));

        register(r, electrolyzer, MarsContainerNames.ELECTROLYZER);
        register(r, gasLiquefier, MarsContainerNames.GAS_LIQUEFIER);
        register(r, launchController, MarsContainerNames.LAUNCH_CONTROLLER);
        register(r, launchControllerAdvanced, MarsContainerNames.LAUNCH_CONTROLLER_ADVANCED);
        register(r, methaneSynthesizer, MarsContainerNames.METHANE_SYNTHESIZER);
        register(r, schematicT2Rocket, MarsContainerNames.SCHEMATIC_T2_ROCKET);
        register(r, schematicCargoRocket, MarsContainerNames.SCHEMATIC_CARGO_ROCKET);
        register(r, slimeling, MarsContainerNames.SLIMELING);
        register(r, terraformer, MarsContainerNames.TERRAFORMER);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ScreenManager.registerFactory(electrolyzer, GuiWaterElectrolyzer::new);
            ScreenManager.registerFactory(gasLiquefier, GuiGasLiquefier::new);
            ScreenManager.registerFactory(launchController, GuiLaunchController::new);
            ScreenManager.registerFactory(launchControllerAdvanced, GuiLaunchController::new);
            ScreenManager.registerFactory(methaneSynthesizer, GuiMethaneSynthesizer::new);
            ScreenManager.registerFactory(schematicT2Rocket, GuiSchematicTier2Rocket::new);
            ScreenManager.registerFactory(schematicCargoRocket, GuiSchematicCargoRocket::new);
            ScreenManager.registerFactory(slimeling, GuiSlimelingInventory::new);
            ScreenManager.registerFactory(terraformer, GuiTerraformer::new);
        });
    }
}
