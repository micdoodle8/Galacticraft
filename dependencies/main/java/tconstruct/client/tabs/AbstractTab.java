package tconstruct.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class AbstractTab extends GuiButton
{
	ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
	ItemStack renderStack;
	public int potionOffsetLast;

	public AbstractTab(int id, int posX, int posY, ItemStack renderStack)
	{
		super(id, posX, posY, 28, 32, "");
		this.renderStack = renderStack;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		int newPotionOffset = TabRegistry.getPotionOffsetNEI();
		if (newPotionOffset < this.potionOffsetLast)
		{
	    	this.xPosition += newPotionOffset - this.potionOffsetLast;
	    	this.potionOffsetLast = newPotionOffset;
		}
		if (this.visible)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			int yTexPos = this.enabled ? 3 : 32;
			int ySize = this.enabled ? 25 : 32;
			int xOffset = this.id == 2 ? 0 : 1;
			int yPos = this.yPosition + (this.enabled ? 3 : 0);

			mc.renderEngine.bindTexture(this.texture);
			this.drawTexturedModalRect(this.xPosition, yPos, xOffset * 28, yTexPos, 28, ySize);

			RenderHelper.enableGUIStandardItemLighting();
			this.zLevel = 100.0F;
//			FMLClientHandler.instance().getClient().getItemRenderer().zLevel = 100.0F;
//			GL11.glEnable(GL11.GL_LIGHTING);
//			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//            FMLClientHandler.instance().getClient().getItemRenderer().renderOverlays(mc.fontRenderer, mc.renderEngine, this.renderStack, this.xPosition + 6, this.yPosition + 8);
//            FMLClientHandler.instance().getClient().getItemRenderer().renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, this.renderStack, this.xPosition + 6, this.yPosition + 8);
//			GL11.glDisable(GL11.GL_LIGHTING);
//            GL11.glEnable(GL11.GL_BLEND);
//            FMLClientHandler.instance().getClient().getItemRenderer().zLevel = 0.0F;
			this.zLevel = 0.0F;
			RenderHelper.disableStandardItemLighting();
		}
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		boolean inWindow = this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

		if (inWindow)
		{
			this.onTabClicked();
		}

		return inWindow;
	}

	public abstract void onTabClicked();

	public abstract boolean shouldAddToList();
}
