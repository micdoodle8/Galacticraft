package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import mekanism.api.EnumGas;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerAirCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreGuiAirCollector extends GuiContainer
{
    private final GCCoreTileEntityOxygenCollector distributorInv;

	public GCCoreGuiAirCollector(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenCollector par2TileEntityAirDistributor)
	{
        super(new GCCoreContainerAirCollector(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.distributorInv = par2TileEntityAirDistributor;
        this.ySize = 180;
	}

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Oxygen Collector", 8, 10, 4210752);
        this.fontRenderer.drawString("In:", 90, 31, 4210752);
        String status = "Status: " + this.getStatus();
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 50, 4210752);
        status = "Oxygen: " + this.distributorInv.getGas(EnumGas.OXYGEN);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 60, 4210752);
        status = ElectricityDisplay.getDisplay(this.distributorInv.WATTS_PER_TICK * 20, ElectricUnit.WATT);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 70, 4210752);
        status = ElectricityDisplay.getDisplay(this.distributorInv.getVoltage(), ElectricUnit.VOLTAGE);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 80, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 90 + 2, 4210752);
    }
    
    private String getStatus()
    {
    	if (this.distributorInv.getGas(EnumGas.OXYGEN) > 1 && this.distributorInv.wattsReceived > 0)
    	{
    		return EnumColor.DARK_GREEN + "Active";
    	}
    	
    	if (this.distributorInv.wattsReceived == 0)
    	{
    		return EnumColor.DARK_RED + "Not Enough Power";
    	}
    	
    	if (this.distributorInv.getGas(EnumGas.OXYGEN) < 1)
    	{
    		return EnumColor.DARK_RED + "Not Enough Leaf Blocks";
    	}
    	
    	return EnumColor.DARK_RED + "Unknown";
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.func_98187_b("/micdoodle8/mods/galacticraft/core/client/gui/oxygen.png");
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);

		if (this.distributorInv != null)
		{
			int scale = (int) ((double) this.distributorInv.getGas(EnumGas.OXYGEN) / (double) 50 * 54);
			this.drawTexturedModalRect(var5 + 108, var6 + 26, 176, 0, Math.min(scale, 54), 16);
		}
	}
}