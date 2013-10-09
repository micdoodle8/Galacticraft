package codechicken.nei.config;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import codechicken.nei.Image;
import codechicken.nei.LayoutManager;
import codechicken.nei.forge.GuiContainerManager;

public class OptionUtilities extends OptionStringSet
{
    public OptionUtilities(String name)
    {
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
    public void drawIcons()
    {
        int x = buttonX();
        LayoutManager.drawIcon(x+4, 6, new Image(120, 24, 12, 12)); x+=24;
        LayoutManager.drawIcon(x+4, 6, new Image(120, 12, 12, 12)); x+=24;
        LayoutManager.drawIcon(x+4, 6, new Image(168, 24, 12, 12)); x+=24;
        LayoutManager.drawIcon(x+4, 6, new Image(144, 12, 12, 12)); x+=24;
        LayoutManager.drawIcon(x+4, 6, new Image(180, 24, 12, 12)); x+=24;
        LayoutManager.drawIcon(x+4, 6, new Image(132, 12, 12, 12)); x+=24;
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        ItemStack sword = new ItemStack(Item.swordDiamond);
        sword.addEnchantment(Enchantment.sharpness, 1);
        GuiContainerManager.drawItem(x+2, 4, sword); x+=24;
        GuiContainerManager.drawItem(x+2, 4, new ItemStack(Item.potion)); x+=24;
        GuiContainerManager.drawItem(x+2, 4, new ItemStack(Block.stone)); x+=24;
    }
}
