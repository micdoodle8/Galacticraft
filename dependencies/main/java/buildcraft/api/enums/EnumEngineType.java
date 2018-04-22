package buildcraft.api.enums;

import net.minecraft.util.IStringSerializable;

import buildcraft.api.core.IEngineType;

public enum EnumEngineType implements IStringSerializable, IEngineType {
    WOOD("core", "wood"),
    STONE("energy", "stone"),
    IRON("energy", "iron"),
    CREATIVE("energy", "creative");

    public final String unlocalizedTag;
    public final String resourceLocation;

    public static final EnumEngineType[] VALUES = values();

    EnumEngineType(String mod, String loc) {
        unlocalizedTag = loc;
        resourceLocation = "buildcraft" + mod + ":blocks/engine/inv/" + loc;
    }

    @Override
    public String getItemModelLocation() {
        return resourceLocation;
    }

    @Override
    public String getName() {
        return unlocalizedTag;
    }

    public static EnumEngineType fromMeta(int meta) {
        if (meta < 0 || meta >= VALUES.length) {
            meta = 0;
        }
        return VALUES[meta];
    }
}
