package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractTab extends Button
{
	ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	ItemStack renderStack;
	public int potionOffsetLast;
    protected ItemRenderer itemRender;
    private int index;

	public AbstractTab(int index, ItemStack renderStack)
	{
		super(0, 0, 28, 32, "", (b) -> { ((AbstractTab) b).onTabClicked(); });
		this.renderStack = renderStack;
        this.itemRender = Minecraft.getInstance().getItemRenderer();
        this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int newPotionOffset = TabRegistry.getPotionOffsetNEI();
		Screen screen = Minecraft.getInstance().currentScreen;
		if (screen instanceof InventoryScreen)
		{
			newPotionOffset += TabRegistry.getRecipeBookOffset((InventoryScreen) screen) - TabRegistry.recipeBookOffset;
		}
		if (newPotionOffset != this.potionOffsetLast)
		{
			this.x += newPotionOffset - this.potionOffsetLast;
			this.potionOffsetLast = newPotionOffset;
		}
		if (this.visible)
		{
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

			int yTexPos = this.active ? 3 : 32;
			int ySize = this.active ? 25 : 32;
			int yPos = this.y + (this.active ? 3 : 0);

			Minecraft mc = Minecraft.getInstance();
			mc.textureManager.bindTexture(this.texture);
			this.blit(this.x, yPos, index * 28, yTexPos, 28, ySize);

			RenderHelper.enableStandardItemLighting();
			this.setBlitOffset(100);
			this.itemRender.zLevel = 100.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			this.itemRender.renderItemAndEffectIntoGUI(this.renderStack, this.x + 6, this.y + 8);
			this.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.renderStack, this.x + 6, this.y + 8, null);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			this.itemRender.zLevel = 0.0F;
			this.setBlitOffset(0);
			RenderHelper.disableStandardItemLighting();
		}
	}

//	@Override
//	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
//	{
//		boolean inWindow = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
//
//		if (inWindow)
//		{
//			this.onTabClicked();
//		}
//
//		return inWindow;
//	}

	public abstract void onTabClicked();

	public abstract boolean shouldAddToList();
}
