package micdoodle8.mods.galacticraft.api.entity;

import net.minecraft.util.text.translation.I18n;

public interface IRocketType
{
    enum EnumRocketType
    {
        DEFAULT(0, "", false, 2),
        INVENTORY27(1, I18n.translateToLocal("gui.rocket_type.0"), false, 20),
        INVENTORY36(2, I18n.translateToLocal("gui.rocket_type.1"), false, 38),
        INVENTORY54(3, I18n.translateToLocal("gui.rocket_type.2"), false, 56),
        PREFUELED(4, I18n.translateToLocal("gui.rocket_type.3"), true, 2);

        private int index;
        private String tooltip;
        private boolean preFueled;
        private int inventorySpace;

        private EnumRocketType(int index, String tooltip, boolean preFueled, int inventorySpace)
        {
            this.index = index;
            this.tooltip = tooltip;
            this.preFueled = preFueled;
            this.inventorySpace = inventorySpace;
        }

        public String getTooltip()
        {
            return this.tooltip;
        }

        public int getIndex()
        {
            return this.index;
        }

        public int getInventorySpace()
        {
            return this.inventorySpace;
        }

        public boolean getPreFueled()
        {
            return this.preFueled;
        }
    }

    EnumRocketType getType();

    int getRocketTier();
}
