package buildcraft.api;

import net.minecraft.item.Item;

import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import buildcraft.api.items.FluidItemDrops;

public class BCItems {

    @ObjectHolder("buildcraftlib")
    public static class Lib {
        public static final Item GUIDE = null;
        public static final Item GUIDE_NOTE = null;
        public static final Item DEBUGGER = null;
    }

    @ObjectHolder("buildcraftcore")
    public static class Core {
        public static final Item GEAR_WOOD = null;
        public static final Item GEAR_STONE = null;
        public static final Item GEAR_IRON = null;
        public static final Item GEAR_GOLD = null;
        public static final Item GEAR_DIAMOND = null;
        public static final Item WRENCH = null;
        public static final Item PAINTBRUSH = null;
        public static final Item LIST = null;
        public static final Item MAP_LOCATION = null;
        public static final Item MARKER_CONNECTOR = null;
        public static final Item VOLUME_BOX = null;
        public static final Item GOGGLES = null;

        /** It is recommended that you refer to {@link FluidItemDrops#item} when creating fluid drops rather than
         * this. */
        public static final Item FRAGILE_FLUID_SHARD = null;
    }

    @ObjectHolder("buildcraftbuilders")
    public static class Builders {

    }

    @ObjectHolder("buildcraftenergy")
    public static class Energy {
        public static final Item GLOB_OF_OIL = null;
    }

    @ObjectHolder("buildcraftfactory")
    public static class Factory {
        public static final Item PLASTIC_SHEET = null;
        public static final Item WATER_GEL = null;
        public static final Item GELLED_WATER = null;
    }

    @ObjectHolder("buildcrafttransport")
    public static class Transport {

    }

    @ObjectHolder("buildcraftsilicon")
    public static class Silicon {
        public static final Item REDSTONE_CHIPSET = null;
    }

    @ObjectHolder("buildcraftrobotics")
    public static class Robotics {

    }
}
