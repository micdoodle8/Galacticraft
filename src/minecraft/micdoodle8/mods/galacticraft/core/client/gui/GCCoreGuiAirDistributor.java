package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerAirDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreGuiAirDistributor extends GuiContainer
{
    private final GCCoreTileEntityOxygenDistributor distributorInv;
    
	public GCCoreGuiAirDistributor(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenDistributor par2TileEntityAirDistributor) 
	{
        super(new GCCoreContainerAirDistributor(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.distributorInv = par2TileEntityAirDistributor;
	}

    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.airdistributor"), 8, 10, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 92 + 2, 4210752);
        this.fontRenderer.drawString("Power:  " + String.valueOf(9), 45, this.ySize - 142 + 2, 0x208326);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		final int texture = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/distributor.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);
	}
}