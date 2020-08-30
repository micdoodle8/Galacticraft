package micdoodle8.mods.galacticraft.planets;

import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.dimension.DimensionMars;
import micdoodle8.mods.galacticraft.planets.venus.dimension.DimensionVenus;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class PlanetDimensions
{
    private static final DeferredRegister<ModDimension> DIMENSIONS = DeferredRegister.create(ForgeRegistries.MOD_DIMENSIONS, Constants.MOD_ID_PLANETS);

    public static DimensionType MARS_DIMENSION;
    public static DimensionType ASTEROIDS_DIMENSION;
    public static DimensionType VENUS_DIMENSION;
    public static final String NAME_MARS = "mars";
    public static final String NAME_ASTEROIDS = "asteroids";
    public static final String NAME_VENUS = "venus";
    public static final RegistryObject<ModDimension> MARS_MOD_DIMENSION = register(NAME_MARS, PlanetDimensions::marsFactory);
    public static final RegistryObject<ModDimension> ASTEROIDS_MOD_DIMENSION = register(NAME_ASTEROIDS, PlanetDimensions::asteroidsFactory);
    public static final RegistryObject<ModDimension> VENUS_MOD_DIMENSION = register(NAME_VENUS, PlanetDimensions::venusFactory);

    private static ModDimension marsFactory()
    {
        return new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionMars::new;
            }
        };
    }

    private static ModDimension asteroidsFactory()
    {
        return new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionAsteroids::new;
            }
        };
    }

    private static ModDimension venusFactory()
    {
        return new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionVenus::new;
            }
        };
    }

    private static RegistryObject<ModDimension> register(final String name, final Supplier<ModDimension> sup)
    {
        return DIMENSIONS.register(name, sup);
    }

    public static void registerDeferredRegistry()
    {
        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS)
    public static class EventDimensionType
    {
        @SubscribeEvent
        public static void onModDimensionRegister(final RegisterDimensionsEvent event)
        {
            ResourceLocation id = new ResourceLocation(Constants.MOD_ID_PLANETS, NAME_MARS);
            if (DimensionType.byName(id) == null)
            {
                MARS_DIMENSION = DimensionManager.registerDimension(id, MARS_MOD_DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), true);
                DimensionManager.keepLoaded(MARS_DIMENSION, false);
            }
            else
            {
                MARS_DIMENSION = DimensionType.byName(id);
            }

            id = new ResourceLocation(Constants.MOD_ID_PLANETS, NAME_ASTEROIDS);
            if (DimensionType.byName(id) == null)
            {
                ASTEROIDS_DIMENSION = DimensionManager.registerDimension(id, ASTEROIDS_MOD_DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), true);
                DimensionManager.keepLoaded(ASTEROIDS_DIMENSION, false);
            }
            else
            {
                ASTEROIDS_DIMENSION = DimensionType.byName(id);
            }

            id = new ResourceLocation(Constants.MOD_ID_PLANETS, NAME_VENUS);
            if (DimensionType.byName(id) == null)
            {
                VENUS_DIMENSION = DimensionManager.registerDimension(id, VENUS_MOD_DIMENSION.get(), new PacketBuffer(Unpooled.buffer()), true);
                DimensionManager.keepLoaded(VENUS_DIMENSION, false);
            }
            else
            {
                VENUS_DIMENSION = DimensionType.byName(id);
            }
        }
    }
}
