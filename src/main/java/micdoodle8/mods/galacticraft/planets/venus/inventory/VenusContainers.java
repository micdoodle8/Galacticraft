package micdoodle8.mods.galacticraft.planets.venus.inventory;

import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.*;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiGeothermal;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiSolarArrayController;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityGeothermalGenerator;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySolarArrayController;
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

public class VenusContainers
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<ContainerType<?>> evt)
    {
        IForgeRegistry<ContainerType<?>> r = evt.getRegistry();

        ContainerType<ContainerCrashedProbe> crashedProbe = IForgeContainerType.create((windowId, inv, data) -> new ContainerCrashedProbe(windowId, inv, (TileEntityCrashedProbe) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerGeothermal> geothermal = IForgeContainerType.create((windowId, inv, data) -> new ContainerGeothermal(windowId, inv, (TileEntityGeothermalGenerator) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerLaserTurret> laserTurret = IForgeContainerType.create((windowId, inv, data) -> new ContainerLaserTurret(windowId, inv, (TileEntityLaserTurret) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerSolarArrayController> solarArrayController = IForgeContainerType.create((windowId, inv, data) -> new ContainerSolarArrayController(windowId, inv, (TileEntitySolarArrayController) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));

        register(r, crashedProbe, VenusContainerNames.CRASHED_PROBE);
        register(r, geothermal, VenusContainerNames.GEOTHERMAL);
        register(r, laserTurret, VenusContainerNames.LASER_TURRET);
        register(r, solarArrayController, VenusContainerNames.SOLAR_ARRAY_CONTROLLER);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ScreenManager.registerFactory(crashedProbe, GuiCrashedProbe::new);
            ScreenManager.registerFactory(geothermal, GuiGeothermal::new);
            ScreenManager.registerFactory(laserTurret, GuiLaserTurret::new);
            ScreenManager.registerFactory(solarArrayController, GuiSolarArrayController::new);
        });
    }
}
