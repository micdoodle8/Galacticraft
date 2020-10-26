package micdoodle8.mods.galacticraft.planets;

import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.dimension.DimensionMoon;
import micdoodle8.mods.galacticraft.core.dimension.DimensionOverworldOrbit;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.dimension.DimensionMars;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import micdoodle8.mods.galacticraft.planets.venus.dimension.DimensionVenus;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlanetDimensions
{
    public static DimensionType MARS_DIMENSION;
    public static DimensionType ASTEROIDS_DIMENSION;
    public static DimensionType VENUS_DIMENSION;
    public static final String NAME_MARS = "mars";
    public static final String NAME_ASTEROIDS = "asteroids";
    public static final String NAME_VENUS = "venus";
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + NAME_MARS)
    public static final ModDimension MARS_MOD_DIMENSION = null;
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + NAME_ASTEROIDS)
    public static final ModDimension ASTEROIDS_MOD_DIMENSION = null;
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + NAME_VENUS)
    public static final ModDimension VENUS_MOD_DIMENSION = null;

    @SubscribeEvent
    public static void onDimensionRegistryEvent(RegistryEvent.Register<ModDimension> event)
    {
        ModDimension modDimension = new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionMars::new;
            }
        };
        event.getRegistry().register(modDimension.setRegistryName(Constants.MOD_ID_PLANETS, NAME_MARS));

        modDimension = new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionAsteroids::new;
            }
        };
        event.getRegistry().register(modDimension.setRegistryName(Constants.MOD_ID_PLANETS, NAME_ASTEROIDS));

        modDimension = new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionVenus::new;
            }
        };
        event.getRegistry().register(modDimension.setRegistryName(Constants.MOD_ID_PLANETS, NAME_VENUS));
    }

    @SubscribeEvent
    public static void onModDimensionRegister(final RegisterDimensionsEvent event)
    {
        ResourceLocation id = new ResourceLocation(Constants.MOD_ID_PLANETS, NAME_MARS);
        if (DimensionType.byName(id) == null)
        {
            MARS_DIMENSION = DimensionManager.registerDimension(id, MARS_MOD_DIMENSION, new PacketBuffer(Unpooled.buffer()), true);
            DimensionManager.keepLoaded(MARS_DIMENSION, false);
        }
        else
        {
            MARS_DIMENSION = DimensionType.byName(id);
        }

        id = new ResourceLocation(Constants.MOD_ID_PLANETS, NAME_ASTEROIDS);
        if (DimensionType.byName(id) == null)
        {
            ASTEROIDS_DIMENSION = DimensionManager.registerDimension(id, ASTEROIDS_MOD_DIMENSION, new PacketBuffer(Unpooled.buffer()), true);
            DimensionManager.keepLoaded(ASTEROIDS_DIMENSION, false);
        }
        else
        {
            ASTEROIDS_DIMENSION = DimensionType.byName(id);
        }

        id = new ResourceLocation(Constants.MOD_ID_PLANETS, NAME_VENUS);
        if (DimensionType.byName(id) == null)
        {
            VENUS_DIMENSION = DimensionManager.registerDimension(id, VENUS_MOD_DIMENSION, new PacketBuffer(Unpooled.buffer()), true);
            DimensionManager.keepLoaded(VENUS_DIMENSION, false);
        }
        else
        {
            VENUS_DIMENSION = DimensionType.byName(id);
        }

        AsteroidsModule.planetAsteroids.setDimensionInfo(PlanetDimensions.ASTEROIDS_DIMENSION, DimensionAsteroids.class).setTierRequired(3);
        MarsModule.planetMars.setDimensionInfo(PlanetDimensions.MARS_DIMENSION, DimensionMars.class).setTierRequired(2);
        VenusModule.planetVenus.setDimensionInfo(PlanetDimensions.VENUS_DIMENSION, DimensionVenus.class).setTierRequired(3);
    }
}
