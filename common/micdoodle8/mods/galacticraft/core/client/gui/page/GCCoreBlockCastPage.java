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
 * GCCoreBlockCastPage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockCastPage extends GCCoreBookPage
{
	String text;
	ItemStack[] icons;
	private static final ResourceLocation background = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/booksmeltery.png");

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
			this.manual.font.drawString("\u00a7n" + this.text, localWidth + 70, localHeight + 4, 0);
		}

		GL11.glScalef(2f, 2f, 2f);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		this.manual.renderitem.zLevel = 100;

		this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[0], (localWidth + 138) / 2, (localHeight + 110) / 2);
		this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[1], (localWidth + 70) / 2, (localHeight + 74) / 2);
		this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[2], (localWidth + 70) / 2, (localHeight + 110) / 2);

		if (this.icons[0].stackSize > 1)
		{
			this.manual.renderitem.renderItemOverlayIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[0], (localWidth + 106) / 2, (localHeight + 74) / 2, String.valueOf(this.icons[0].stackSize));
		}

		this.manual.renderitem.zLevel = 0;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		this.manual.font.drawString("Ingredients:", localWidth + 120, localHeight + 32, 0);
		this.manual.font.drawString("- " + this.icons[1].getDisplayName(), localWidth + 120, localHeight + 42, 0);
		if (this.icons[2] != null)
		{
			this.manual.font.drawString("- " + this.icons[2].getDisplayName(), localWidth + 120, localHeight + 50, 0);
		}
	}

	@Override
	public void renderBackgroundLayer(int localWidth, int localHeight)
	{
		this.manual.getMC().getTextureManager().bindTexture(GCCoreBlockCastPage.background);
		this.manual.drawTexturedModalRect(localWidth, localHeight + 32, 0, 0, 174, 115);
		this.manual.drawTexturedModalRect(localWidth + 62, localHeight + 105, 2, 118, 45, 45);
	}
}
