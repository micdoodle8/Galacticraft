package micdoodle8.mods.galacticraft.api.entity;

public interface IRocketType
{
    public static enum EnumRocketType
    {
        DEFAULT(0, "", false, 3), INVENTORY27(1, "Storage Space: 18", false, 21), INVENTORY36(2, "Storage Space: 36", false, 39), INVENTORY54(3, "Storage Space: 54", false, 57), PREFUELED(4, "Pre-fueled", true, 3);

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

    public EnumRocketType getType();

    public int getRocketTier();
}
