package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggy;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiBuggy.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreGuiBuggy extends GCCoreGuiContainer
{
	private static ResourceLocation[] sealerTexture = new ResourceLocation[4];

	static
	{
		for (int i = 0; i < 4; i++)
		{
			GCCoreGuiBuggy.sealerTexture[i] = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/buggy_" + i * 18 + ".png");
		}
	}

	private final IInventory upperChestInventory;
	private final int type;

	public GCCoreGuiBuggy(IInventory par1IInventory, IInventory par2IInventory, int type)
	{
		super(new GCCoreContainerBuggy(par1IInventory, par2IInventory, type));
		this.upperChestInventory = par1IInventory;
		this.allowUserInput = false;
		this.type = type;
		this.ySize = 145 + this.type * 36;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		List<String> oxygenDesc = new ArrayList<String>();
		oxygenDesc.add("Buggy fuel tank. Requires");
		oxygenDesc.add("fuel loader to fill");
		this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 71, (this.height - this.ySize) / 2 + 6, 36, 40, oxygenDesc, this.width, this.height));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(StatCollector.translateToLocal("gui.message.fuel.name"), 8, 2 + 3, 4210752);

		this.fontRenderer.drawString(StatCollector.translateToLocal(this.upperChestInventory.getInvName()), 8, this.type == 0 ? 50 : 39, 4210752);

		if (this.mc.thePlayer != null && this.mc.thePlayer.ridingEntity != null && this.mc.thePlayer.ridingEntity instanceof GCCoreEntityBuggy)
		{
			this.fontRenderer.drawString(StatCollector.translateToLocal("gui.message.fuel.name") + ":", 125, 15 + 3, 4210752);
			final double percentage = ((GCCoreEntityBuggy) this.mc.thePlayer.ridingEntity).getScaledFuelLevel(100);
			final String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.code : percentage > 40.0D ? EnumColor.ORANGE.code : EnumColor.RED.code;
			final String str = percentage + "% " + StatCollector.translateToLocal("gui.message.full.name");
			this.fontRenderer.drawString(color + str, 117 - str.length() / 2, 20 + 8, 4210752);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.getTextureManager().bindTexture(GCCoreGuiBuggy.sealerTexture[this.type]);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, 176, this.ySize);

		if (this.mc.thePlayer != null && this.mc.thePlayer.ridingEntity != null && this.mc.thePlayer.ridingEntity instanceof GCCoreEntityBuggy)
		{
			final int fuelLevel = ((GCCoreEntityBuggy) this.mc.thePlayer.ridingEntity).getScaledFuelLevel(38);

			this.drawTexturedModalRect((this.width - this.xSize) / 2 + 72, (this.height - this.ySize) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
		}
	}
}
