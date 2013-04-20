package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiRefinery extends GuiContainer
{
	private final GCCoreTileEntityRefinery tileEntity;

    private GuiButton buttonDisable;

	private int containerWidth;
	private int containerHeight;

	public GCCoreGuiRefinery(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityRefinery tileEntity)
	{
		super(new GCCoreContainerRefinery(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
		this.ySize += 20;
	}

	@Override
    public void initGui()
    {
    	super.initGui();

        this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 38, this.height / 2 - 49, 76, 20, "Refine"));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	switch (par1GuiButton.id)
    	{
    	case 0:
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] {this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord}));
			break;
    	}
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Refinery", 68, 5, 4210752);
		String displayText = "";

		if (this.tileEntity.oilTank.getLiquid() == null || this.tileEntity.oilTank.getLiquid().amount == 0)
		{
			displayText = EnumColor.RED + "No Oil";
		}
		else if (this.tileEntity.oilTank.getLiquid().amount > 0 && this.tileEntity.disabled)
		{
			displayText = EnumColor.ORANGE + "Ready";
		}
		else if (this.tileEntity.wattsReceived == 0)
		{
			displayText = EnumColor.ORANGE + "Idle";
		}
		else if ((this.tileEntity.wattsReceived < this.tileEntity.ueWattsPerTick) || (this.tileEntity.ic2Energy < this.tileEntity.ueWattsPerTick))
		{
			displayText = EnumColor.ORANGE + "Heating";
		}
		else if (this.tileEntity.processTicks > 0)
		{
			displayText = EnumColor.BRIGHT_GREEN + "Refining";
		}
		else
		{
			displayText = EnumColor.ORANGE + "Idle";
		}

        this.buttonDisable.enabled = this.tileEntity.processTicks == 0;
		this.fontRenderer.drawString("Status: " + displayText, 72, 45 + 23, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23, 4210752);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 118 + 2 + 23, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/gui/refinery.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.processTicks > 0)
		{
			final int scale = (int) ((double) this.tileEntity.processTicks / (double) GCCoreTileEntityRefinery.PROCESS_TIME_REQUIRED * 124);
			this.drawTexturedModalRect(this.containerWidth + 26, this.containerHeight + 24, 0, 186, 124 - scale, 20);
		}

        int displayInt = this.tileEntity.getScaledOilLevel(38);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 17 + 49 - displayInt, 176, 38 - displayInt, 16, displayInt);

        displayInt = this.tileEntity.getScaledFuelLevel(38);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 17 + 49 - displayInt, 176 + 16, 38 - displayInt, 16, displayInt);
	}
}