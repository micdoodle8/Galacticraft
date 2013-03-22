package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFuelCanister;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
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
public class GCCoreGuiFuelLoader extends GuiContainer
{
    private final GCCoreTileEntityFuelLoader fuelLoaderInv;

	public GCCoreGuiFuelLoader(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityFuelLoader par2TileEntityAirDistributor)
	{
        super(new GCCoreContainerFuelLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.fuelLoaderInv = par2TileEntityAirDistributor;
        this.ySize = 180;
	}

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Fuel Loader", 60, 10, 4210752);
        String status = "Status: ";
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 30, 4210752);
        status = this.getStatus();
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 40, 4210752);
        status = ElectricityDisplay.getDisplay(this.fuelLoaderInv.WATTS_PER_TICK * 20, ElectricUnit.WATT);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 50, 4210752);
        status = ElectricityDisplay.getDisplay(this.fuelLoaderInv.getVoltage(), ElectricUnit.VOLTAGE);
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2 + 35, 60, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 102, 4210752);
    }
    
    private String getStatus()
    {
    	if (this.fuelLoaderInv.getStackInSlot(1) == null)
    	{
    		return EnumColor.DARK_RED + "No Fuel To Load!";
    	}
    	
    	if (this.fuelLoaderInv.getStackInSlot(1).getItem() instanceof GCCoreItemFuelCanister && this.fuelLoaderInv.wattsReceived > 0)
    	{
    		return EnumColor.DARK_GREEN + "Active";
    	}
    	
    	if (this.fuelLoaderInv.wattsReceived == 0)
    	{
    		return EnumColor.DARK_RED + "Not Enough Power";
    	}
    	
    	return EnumColor.DARK_RED + "Unknown";
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.func_98187_b("/micdoodle8/mods/galacticraft/core/client/gui/loader.png");
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);
		
    	final ItemStack stack = this.fuelLoaderInv.getStackInSlot(1);

    	if (stack != null)
    	{
			int scale = (int) ((double) (stack.getMaxDamage() - stack.getItemDamage()) / (double) stack.getMaxDamage() * 124);

        	if (stack != null)
        	{
        		final int fuelLevel = stack.getMaxDamage() - stack.getItemDamage();
        		
        		float ratio = stack.getMaxDamage() / 40.0F;
        		
                this.drawTexturedModalRect(var5 + 29, MathHelper.ceiling_float_int(var6 + 58 - fuelLevel / ratio), 176, 0, 10, MathHelper.ceiling_float_int(fuelLevel / ratio));
        	}
        }
	}
}