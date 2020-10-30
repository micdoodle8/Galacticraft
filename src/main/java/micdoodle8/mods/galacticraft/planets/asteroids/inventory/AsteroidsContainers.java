package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiGeothermal;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.client.gui.GuiSolarArrayController;
import micdoodle8.mods.galacticraft.planets.venus.inventory.*;
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

public class AsteroidsContainers
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<ContainerType<?>> evt)
    {
        IForgeRegistry<ContainerType<?>> r = evt.getRegistry();

        ContainerType<ContainerAstroMinerDock> minerDock = IForgeContainerType.create((windowId, inv, data) -> new ContainerAstroMinerDock(windowId, inv, (TileEntityMinerBase) inv.player.world.getTileEntity(data.readBlockPos())));
        ContainerType<ContainerSchematicAstroMiner> schematicAstroMiner = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicAstroMiner(windowId, inv));
        ContainerType<ContainerSchematicTier3Rocket> schematicTier3Rocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicTier3Rocket(windowId, inv));
        ContainerType<ContainerShortRangeTelepad> telepad = IForgeContainerType.create((windowId, inv, data) -> new ContainerShortRangeTelepad(windowId, inv, (TileEntityShortRangeTelepad) inv.player.world.getTileEntity(data.readBlockPos())));

        register(r, minerDock, AsteroidsContainerNames.ASTRO_MINER_DOCK);
        register(r, schematicAstroMiner, AsteroidsContainerNames.SCHEMATIC_ASTRO_MINER);
        register(r, schematicTier3Rocket, AsteroidsContainerNames.SCHEMATIC_TIER_3_ROCKET);
        register(r, telepad, AsteroidsContainerNames.SHORT_RANGE_TELEPAD);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ScreenManager.registerFactory(minerDock, GuiAstroMinerDock::new);
            ScreenManager.registerFactory(schematicAstroMiner, GuiSchematicAstroMinerDock::new);
            ScreenManager.registerFactory(schematicTier3Rocket, GuiSchematicTier3Rocket::new);
            ScreenManager.registerFactory(telepad, GuiShortRangeTelepad::new);
        });
    }
}
