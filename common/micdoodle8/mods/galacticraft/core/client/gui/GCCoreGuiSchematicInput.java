package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerSchematic;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCCoreGuiSchematicInput.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiSchematicInput extends GCCoreGuiContainer implements ISchematicResultPage
{
	private static final ResourceLocation schematicInputTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/schematicpage.png");

	private GuiButton nextButton;
	private int pageIndex;

	public GCCoreGuiSchematicInput(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
	{
		super(new GCCoreContainerSchematic(par1InventoryPlayer, x, y, z));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		List<String> schematicSlotDesc = new ArrayList<String>();
		schematicSlotDesc.add("New schematic slot. You");
		schematicSlotDesc.add("can find new schematics");
		schematicSlotDesc.add("by completing dungeons");
		schematicSlotDesc.add("on planets and moons");
		this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 79, (this.height - this.ySize) / 2, 18, 18, schematicSlotDesc, this.width, this.height));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 30 + 27 - 12, 40, 20, StatCollector.translateToLocal("gui.button.back.name")));
		this.buttonList.add(this.nextButton = new GuiButton(1, this.width / 2 - 130, this.height / 2 - 30 + 27 + 12, 40, 20, StatCollector.translateToLocal("gui.button.next.name")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 92 / 2, this.height / 2 - 52, 92, 20, StatCollector.translateToLocal("gui.button.unlockschematic.name")));
		this.nextButton.enabled = false;
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
			case 2:
				final Object[] toSend2 = { 0 };
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UNLOCK_NEW_SCHEMATIC, toSend2));
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(StatCollector.translateToLocal("gui.message.addnewsch.name"), 7, -22, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 56, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GCCoreGuiSchematicInput.schematicInputTexture);
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
