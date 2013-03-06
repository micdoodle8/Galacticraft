package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerAirCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreGuiAirCompressor extends GuiContainer
{
    private final GCCoreTileEntityOxygenCompressor distributorInv;
    
	public GCCoreGuiAirCompressor(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenCompressor par2TileEntityAirDistributor)
	{
        super(new GCCoreContainerAirCompressor(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.distributorInv = par2TileEntityAirDistributor;
	}

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Air Compressor", 8, 10, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 121 + 2, 4210752);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		final int texture = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/compressor.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);
		
		if (distributorInv != null)
		{
			this.drawTexturedModalRect(var5 + 68, var6 + 34, 176, 0, (int) Math.min(distributorInv.getPower(), 32), 12);
		}
	}
}