package micdoodle8.mods.galacticraft.core.dimension;

import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
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
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.BiFunction;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCDimensions
{
    public static DimensionType MOON_DIMENSION;

    @ObjectHolder("moon")
    public static final ModDimension MOON_MOD_DIMENSION = null;
    @ObjectHolder("space_station_overworld")
    public static final ModDimension SPACE_STATION_MOD_DIMENSION = null;

    @SubscribeEvent
    public static void onDimensionRegistryEvent(RegistryEvent.Register<ModDimension> event)
    {
        ModDimension modDimension = new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionMoon::new;
            }
        };
        event.getRegistry().register(modDimension.setRegistryName(Constants.MOD_ID_CORE, "moon"));

        modDimension = new ModDimension()
        {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory()
            {
                return DimensionOverworldOrbit::new;
            }
        };
        event.getRegistry().register(modDimension.setRegistryName(Constants.MOD_ID_CORE, "space_station_overworld"));
    }

    @SubscribeEvent
    public static void onModDimensionRegister(final RegisterDimensionsEvent event)
    {
        ResourceLocation id = new ResourceLocation(Constants.MOD_ID_CORE, "moon");
        if (DimensionType.byName(id) == null)
        {
            MOON_DIMENSION = DimensionManager.registerDimension(id, MOON_MOD_DIMENSION, new PacketBuffer(Unpooled.buffer()), true);
            DimensionManager.keepLoaded(MOON_DIMENSION, false);
        }
        else
        {
            MOON_DIMENSION = DimensionType.byName(id);
        }

        GalacticraftCore.moonMoon.setDimensionInfo(GCDimensions.MOON_DIMENSION, DimensionMoon.class).setTierRequired(1);
    }
}