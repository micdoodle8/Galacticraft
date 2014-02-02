package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggyBench;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

/**
 * GCCoreGuiSchematicBuggy.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiSchematicBuggy extends GCCoreGuiContainer implements ISchematicResultPage
{
	private static final ResourceLocation buggyBenchTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/buggybench.png");

	private int pageIndex;

	public GCCoreGuiSchematicBuggy(InventoryPlayer par1InventoryPlayer)
	{
		super(new GCCoreContainerBuggyBench(par1InventoryPlayer, 0, 0, 0));
		this.ySize = 221;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 30 + 27 - 12, 40, 20, StatCollector.translateToLocal("gui.button.back.name")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 130, this.height / 2 - 30 + 27 + 12, 40, 20, StatCollector.translateToLocal("gui.button.next.name")));
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			switch (par1GuiButton.id)
			{
			case 0:
				SchematicRegistry.flipToLastPage(this.pageIndex);
				break;
			case 1:
				SchematicRegistry.flipToNextPage(this.pageIndex);
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(StatCollector.translateToLocal("schematic.moonbuggy.name"), 7, -20 + 27, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 202 - 104 + 2 + 27, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GCCoreGuiSchematicBuggy.buggyBenchTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - 220) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, 220);
	}

	@Override
	public void setPageIndex(int index)
	{
		this.pageIndex = index;
	}
}
