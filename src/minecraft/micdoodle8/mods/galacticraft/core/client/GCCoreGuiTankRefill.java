package micdoodle8.mods.galacticraft.core.client;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import micdoodle8.mods.galacticraft.core.GCContainerTankRefill;
import micdoodle8.mods.galacticraft.core.GCCoreInventoryTankRefill;
import micdoodle8.mods.galacticraft.mars.*;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreGuiTankRefill extends GuiContainer 
{
	static Container tank = new GCContainerTankRefill(FMLClientHandler.instance().getClient().thePlayer.inventory, new GCCoreInventoryTankRefill());
	
	public GCCoreGuiTankRefill(EntityPlayer player)
    {
        super(tank);
        this.allowUserInput = true;
    }

	@Override
	protected void drawGuiContainerForegroundLayer() 
	{
		this.fontRenderer.drawString(("Oxygen Tank Refill"), 8, 10, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		int texture = this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/mars/client/gui/airtank.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(texture);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);
	}
}
