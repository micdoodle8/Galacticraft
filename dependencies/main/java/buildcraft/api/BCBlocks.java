package buildcraft.api;

import net.minecraft.block.Block;

import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class BCBlocks {

    @ObjectHolder("buildcraftcore")
    public static class Core {
        public static final Block SPRING = null;
        public static final Block DECORATED = null;
        public static final Block ENGINE = null;
        public static final Block MARKER_VOLUME = null;
        public static final Block MARKER_PATH = null;
    }

    @ObjectHolder("buildcraftbuilders")
    public static class Builders {
        public static final Block ARCHITECT = null;
        public static final Block BUILDER = null;
        public static final Block FILLER = null;
        public static final Block LIBRARY = null;
        public static final Block REPLACER = null;
        public static final Block QUARRY = null;
        public static final Block FRAME = null;
    }

    @ObjectHolder("buildcraftenergy")
    public static class Energy {
        // Fluid blocks can be accessed ~somewhere else~
    }

    @ObjectHolder("buildcraftfactory")
    public static class Factory {
        public static final Block AUTOWORKBENCH_ITEM = null;
        public static final Block MINING_WELL = null;
        public static final Block PUMP = null;
        public static final Block TUBE = null;
        public static final Block FLOOD_GATE = null;
        public static final Block TANK = null;
        public static final Block CHUTE = null;
        public static final Block WATER_GEL = null;
        public static final Block DISTILLER = null;
        public static final Block HEAT_EXCHANGE = null;
    }

    @ObjectHolder("buildcrafttransport")
    public static class Transport {
        public static final Block FILTERED_BUFFER = null;
        public static final Block PIPE_HOLDER = null;
    }

    @ObjectHolder("buildcraftsilicon")
    public static class Silicon {
        public static final Block LASER = null;
        public static final Block ASSEMBLY_TABLE = null;
        public static final Block ADVANCED_CRAFTING_TABLE = null;
        public static final Block INTEGRATION_TABLE = null;
        public static final Block CHARGING_TABLE = null;
        public static final Block PROGRAMMING_TABLE = null;
    }

    @ObjectHolder("buildcraftrobotics")
    public static class Robotics {

    }
}
