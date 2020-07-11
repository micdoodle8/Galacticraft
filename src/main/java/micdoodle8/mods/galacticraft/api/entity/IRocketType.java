package micdoodle8.mods.galacticraft.api.entity;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface IRocketType
{
    enum EnumRocketType
    {
        DEFAULT(0, new StringTextComponent(""), false, 2),
        INVENTORY27(1, new TranslationTextComponent("gui.rocket_type.0"), false, 20),
        INVENTORY36(2, new TranslationTextComponent("gui.rocket_type.1"), false, 38),
        INVENTORY54(3, new TranslationTextComponent("gui.rocket_type.2"), false, 56),
        PREFUELED(4, new TranslationTextComponent("gui.rocket_type.3"), true, 2);

        private final int index;
        private final ITextComponent tooltip;
        private final boolean preFueled;
        private final int inventorySpace;

        EnumRocketType(int index, ITextComponent tooltip, boolean preFueled, int inventorySpace)
        {
            this.index = index;
            this.tooltip = tooltip;
            this.preFueled = preFueled;
            this.inventorySpace = inventorySpace;
        }

        public ITextComponent getTooltip()
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

    EnumRocketType getRocketType();

    int getRocketTier();
}
