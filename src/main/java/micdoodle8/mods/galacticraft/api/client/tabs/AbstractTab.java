package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public abstract class AbstractTab extends GuiButton
{
	ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	ItemStack renderStack;
	public int potionOffsetLast;
    protected RenderItem itemRender;

	public AbstractTab(int id, int posX, int posY, ItemStack renderStack)
	{
		super(id, posX, posY, 28, 32, "");
		this.renderStack = renderStack;
        this.itemRender = FMLClientHandler.instance().getClient().getRenderItem();
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		int newPotionOffset = TabRegistry.getPotionOffsetNEI();
		GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;
		if (screen instanceof GuiInventory)
		{
		    newPotionOffset += TabRegistry.getRecipeBookOffset((GuiInventory) screen) - TabRegistry.recipeBookOffset;
		}
		if (newPotionOffset != this.potionOffsetLast)
		{
	    	this.x += newPotionOffset - this.potionOffsetLast;
	    	this.potionOffsetLast = newPotionOffset;
		}
		if (this.visible)
		{
		    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			int yTexPos = this.enabled ? 3 : 32;
			int ySize = this.enabled ? 25 : 32;
			int xOffset = this.id == 2 ? 0 : 1;
			int yPos = this.y + (this.enabled ? 3 : 0);

			mc.renderEngine.bindTexture(this.texture);
			this.drawTexturedModalRect(this.x, yPos, xOffset * 28, yTexPos, 28, ySize);

			RenderHelper.enableGUIStandardItemLighting();
			this.zLevel = 100.0F;
            this.itemRender.zLevel = 100.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableRescaleNormal();
            this.itemRender.renderItemAndEffectIntoGUI(this.renderStack, this.x + 6, this.y + 8);
            this.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.renderStack, this.x + 6, this.y + 8, null);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            this.itemRender.zLevel = 0.0F;
			this.zLevel = 0.0F;
			RenderHelper.disableStandardItemLighting();
		}
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		boolean inWindow = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

		if (inWindow)
		{
			this.onTabClicked();
		}

		return inWindow;
	}

	public abstract void onTabClicked();

	public abstract boolean shouldAddToList();
}
