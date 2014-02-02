package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerExtendedInventory;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import tconstruct.client.tabs.TabRegistry;

/**
 * GCCoreGuiExtendedInventory.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiExtendedInventory extends InventoryEffectRenderer
{
	private static final ResourceLocation inventoryTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/inventory.png");

	private float xSize_lo_2;
	private float ySize_lo_2;
	private static float rotation;

	public GCCoreGuiExtendedInventory(EntityPlayer entityPlayer, GCCoreInventoryExtended inventory)
	{
		super(new GCCoreContainerExtendedInventory(entityPlayer, inventory));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();

		int cornerX = this.guiLeft;
		int cornerY = (this.height - this.ySize) / 2;

		TabRegistry.updateTabValues(cornerX, cornerY, GCCoreInventoryTabGalacticraft.class);
		TabRegistry.addTabsToList(this.buttonList);

		this.buttonList.add(new GuiButton(0, (this.width - this.xSize) / 2 + 15, (this.height - this.ySize) / 2 + 71, 7, 7, ""));
		this.buttonList.add(new GuiButton(1, (this.width - this.xSize) / 2 + 45, (this.height - this.ySize) / 2 + 71, 7, 7, ""));
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			GCCoreGuiExtendedInventory.rotation -= 10.0F;
			break;
		case 1:
			GCCoreGuiExtendedInventory.rotation += 10.0F;
			break;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GCCoreGuiExtendedInventory.inventoryTexture);
		final int k = this.guiLeft;
		final int l = this.guiTop;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		GCCoreGuiExtendedInventory.drawPlayerOnGui(this.mc, k + 33, l + 75, 29, k + 51 - this.xSize_lo_2, l + 75 - 50 - this.ySize_lo_2);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		this.xSize_lo_2 = par1;
		this.ySize_lo_2 = par2;
	}

	public static void drawPlayerOnGui(Minecraft par0Minecraft, int par1, int par2, int par3, float par4, float par5)
	{
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(par1, par2, 50.0F);
		GL11.glScalef(-par3, par3, par3);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = par0Minecraft.thePlayer.renderYawOffset;
		float f3 = par0Minecraft.thePlayer.rotationYaw;
		float f4 = par0Minecraft.thePlayer.rotationPitch;
		par4 -= 19;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		// GL11.glRotatef(-((float) Math.atan(par5 / 40.0F)) * 20.0F, 1.0F,
		// 0.0F, 0.0F);
		par0Minecraft.thePlayer.renderYawOffset = GCCoreGuiExtendedInventory.rotation;
		par0Minecraft.thePlayer.rotationYaw = (float) Math.atan(par4 / 40.0F) * 40.0F;
		par0Minecraft.thePlayer.rotationYaw = GCCoreGuiExtendedInventory.rotation;
		par0Minecraft.thePlayer.rotationYawHead = par0Minecraft.thePlayer.rotationYaw;
		GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(par0Minecraft.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		par0Minecraft.thePlayer.renderYawOffset = f2;
		par0Minecraft.thePlayer.rotationYaw = f3;
		par0Minecraft.thePlayer.rotationPitch = f4;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
