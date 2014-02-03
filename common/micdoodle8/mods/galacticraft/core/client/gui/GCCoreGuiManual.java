package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCCoreManualUtil;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreBookPage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreRenderItemCustom;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreSmallFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiManual.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreGuiManual extends GuiScreen
{
	ItemStack itemstackBook;
	Document manual;
	public GCCoreRenderItemCustom renderitem = new GCCoreRenderItemCustom();
	int bookImageWidth = 206;
	int bookImageHeight = 200;
	int bookTotalPages = 1;
	int currentPage;
	int maxPages;

	private static final ResourceLocation bookRight = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/bookright.png");
	private static final ResourceLocation bookLeft = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/bookleft.png");

	private GCCoreBookPage pageLeft;
	private GCCoreBookPage pageRight;

	public GCCoreSmallFontRenderer font;

	public GCCoreGuiManual(ItemStack stack, Document doc)
	{
		this.mc = Minecraft.getMinecraft();
		this.font = new GCCoreSmallFontRenderer(this.mc.gameSettings, new ResourceLocation("assets/textures/font/ascii.png"), this.mc.renderEngine, false);
		this.itemstackBook = stack;
		this.currentPage = 0; // Stack page
		this.manual = doc;
		// renderitem.renderInFrame = true;
	}

	/*
	 * @Override public void setWorldAndResolution (Minecraft minecraft, int w,
	 * int h) { this.guiParticles = new GuiParticle(minecraft); this.mc =
	 * minecraft; this.width = w; this.height = h; this.buttonList.clear();
	 * this.initGui(); }
	 */

	@Override
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		this.maxPages = this.manual.getElementsByTagName("page").getLength();
		this.updateText();
		int xPos = this.width / 2;
		this.buttonList.add(new GCCoreGuiTurnPageButton(1, xPos + this.bookImageWidth - 50, 195, true));
		this.buttonList.add(new GCCoreGuiTurnPageButton(2, xPos - this.bookImageWidth + 24, 195, false));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.enabled)
		{
			if (button.id == 1)
			{
				this.currentPage += 2;
			}
			if (button.id == 2)
			{
				this.currentPage -= 2;
			}

			this.updateText();
		}
	}

	void updateText()
	{
		if (this.maxPages % 2 == 1)
		{
			if (this.currentPage > this.maxPages)
			{
				this.currentPage = this.maxPages;
			}
		}
		else
		{
			if (this.currentPage >= this.maxPages)
			{
				this.currentPage = this.maxPages - 2;
			}
		}
		if (this.currentPage % 2 == 1)
		{
			this.currentPage--;
		}
		if (this.currentPage < 0)
		{
			this.currentPage = 0;
		}

		NodeList nList = this.manual.getElementsByTagName("page");

		Node node = nList.item(this.currentPage);
		if (node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element element = (Element) node;
			Class<?> clazz = GCCoreManualUtil.getPageClass(element.getAttribute("type"));
			if (clazz != null)
			{
				try
				{
					this.pageLeft = (GCCoreBookPage) clazz.newInstance();
					this.pageLeft.init(this, 0);
					this.pageLeft.readPageFromXML(element);
				}
				catch (Exception e)
				{
				}
			}
			else
			{
				this.pageLeft = null;
			}
		}

		node = nList.item(this.currentPage + 1);
		if (node != null && node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element element = (Element) node;
			Class<?> clazz = GCCoreManualUtil.getPageClass(element.getAttribute("type"));
			if (clazz != null)
			{
				try
				{
					this.pageRight = (GCCoreBookPage) clazz.newInstance();
					this.pageRight.init(this, 1);
					this.pageRight.readPageFromXML(element);
				}
				catch (Exception e)
				{
				}
			}
			else
			{
				this.pageLeft = null;
			}
		}
		else
		{
			this.pageRight = null;
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GCCoreGuiManual.bookRight);
		int localWidth = this.width / 2;
		byte localHeight = 23;
		this.drawTexturedModalRect(localWidth, localHeight, 0, 0, this.bookImageWidth, this.bookImageHeight);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GCCoreGuiManual.bookLeft);
		localWidth = localWidth - this.bookImageWidth;
		this.drawTexturedModalRect(localWidth, localHeight, 256 - this.bookImageWidth, 0, this.bookImageWidth, this.bookImageHeight);

		super.drawScreen(par1, par2, par3); // 16, 12, 220, 12

		if (this.pageLeft != null)
		{
			this.pageLeft.renderBackgroundLayer(localWidth + 16, localHeight + 12);
		}
		if (this.pageRight != null)
		{
			this.pageRight.renderBackgroundLayer(localWidth + 220, localHeight + 12);
		}

		if (this.pageLeft != null)
		{
			this.pageLeft.renderContentLayer(localWidth + 16, localHeight + 12);
		}
		if (this.pageRight != null)
		{
			this.pageRight.renderContentLayer(localWidth + 220, localHeight + 12);
		}
	}

	public Minecraft getMC()
	{
		return this.mc;
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
