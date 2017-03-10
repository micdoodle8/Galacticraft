package codechicken.nei.config;

import codechicken.nei.Image;
import codechicken.nei.LayoutManager;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class OptionUtilities extends OptionStringSet {
    public OptionUtilities(String name) {
        super(name);
        options.add("time");
        options.add("rain");
        options.add("heal");
        options.add("delete");
        options.add("magnet");
        options.add("gamemode");
        options.add("enchant");
        options.add("potion");
        options.add("item");
        groups.put("gamemode", "creative");
        groups.put("gamemode", "creative+");
        groups.put("gamemode", "adventure");
    }

    @Override
    public void drawIcons() {
        int x = buttonX();
        LayoutManager.drawIcon(x + 4, 4, new Image(120, 24, 12, 12));
        x += 24;
        LayoutManager.drawIcon(x + 4, 4, new Image(120, 12, 12, 12));
        x += 24;
        LayoutManager.drawIcon(x + 4, 4, new Image(168, 24, 12, 12));
        x += 24;
        LayoutManager.drawIcon(x + 4, 4, new Image(144, 12, 12, 12));
        x += 24;
        LayoutManager.drawIcon(x + 4, 4, new Image(180, 24, 12, 12));
        x += 24;
        LayoutManager.drawIcon(x + 4, 4, new Image(132, 12, 12, 12));
        x += 24;
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.getEnchantmentByLocation("sharpness"), 1);
        GuiContainerManager.drawItem(x + 2, 2, sword);
        x += 24;
        GuiContainerManager.drawItem(x + 2, 2, new ItemStack(Items.POTIONITEM));
        x += 24;
        GuiContainerManager.drawItem(x + 2, 2, new ItemStack(Blocks.STONE));
        x += 24;
    }
}
