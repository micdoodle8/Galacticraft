package micdoodle8.mods.galacticraft.core.client.gui.page;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCCoreManualUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * GCCoreFurnacePage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreFurnacePage extends GCCoreBookPage
{
	String text;
	ItemStack[] icons;
	private static final ResourceLocation background = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/bookfurnace.png");

	@Override
	public void readPageFromXML(Element element)
	{
		NodeList nodes = element.getElementsByTagName("text");
		if (nodes != null)
		{
			this.text = nodes.item(0).getTextContent();
		}

		nodes = element.getElementsByTagName("recipe");
		if (nodes != null)
		{
			this.icons = GCCoreManualUtil.getRecipeIcons(nodes.item(0).getTextContent());
		}
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight)
	{
		if (this.text != null)
		{
			this.manual.font.drawString("\u00a7n" + this.text, localWidth + 50, localHeight + 4, 0);
		}

		GL11.glScalef(2f, 2f, 2f);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		this.manual.renderitem.zLevel = 100;

		this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, GCCoreManualUtil.getIcon("coal"), (localWidth + 38) / 2, (localHeight + 110) / 2);
		this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[0], (localWidth + 106) / 2, (localHeight + 74) / 2);
		this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[1], (localWidth + 38) / 2, (localHeight + 38) / 2);

		if (this.icons[0].stackSize > 1)
		{
			this.manual.renderitem.renderItemOverlayIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[0], (localWidth + 106) / 2, (localHeight + 74) / 2, String.valueOf(this.icons[0].stackSize));
		}

		this.manual.renderitem.zLevel = 0;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	@Override
	public void renderBackgroundLayer(int localWidth, int localHeight)
	{
		this.manual.getMC().getTextureManager().bindTexture(GCCoreFurnacePage.background);
		this.manual.drawTexturedModalRect(localWidth + 32, localHeight + 32, 0, 0, 111, 114);
	}
}
