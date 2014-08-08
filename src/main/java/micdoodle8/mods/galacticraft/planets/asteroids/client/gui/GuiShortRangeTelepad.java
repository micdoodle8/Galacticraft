package micdoodle8.mods.galacticraft.planets.asteroids.client.gui;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementTextBox.ITextBoxCallback;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiShortRangeTelepad extends GuiContainerGC implements ITextBoxCallback
{
	private static final ResourceLocation launchControllerGui = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/gui/shortRangeTelepad.png");

	private TileEntityShortRangeTelepad telepad;

	private GuiButton enableControllerButton;
	private GuiElementTextBox address;
	private GuiElementTextBox targetAddress;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 52, 9, null, 0, 0, this);

	private Map<Integer, Integer> cannotEditMap = Maps.newHashMap();

	public GuiShortRangeTelepad(InventoryPlayer playerInventory, TileEntityShortRangeTelepad telepad)
	{
		super(new ContainerShortRangeTelepad(playerInventory, telepad));
		this.ySize = 209;
		this.telepad = telepad;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		if (this.telepad.disableCooldown > 0)
		{
			this.enableControllerButton.enabled = false;
		}
		else
		{
			this.enableControllerButton.enabled = true;
		}

		this.enableControllerButton.displayString = this.telepad.getDisabled(0) ? GCCoreUtil.translate("gui.button.enable.name") : GCCoreUtil.translate("gui.button.disable.name");

		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char keyChar, int keyID)
	{
		if (keyID != Keyboard.KEY_ESCAPE && keyID != this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			if (this.address.keyTyped(keyChar, keyID))
			{
				return;
			}
	
			if (this.targetAddress.keyTyped(keyChar, keyID))
			{
				return;
			}
		}
		
		super.keyTyped(keyChar, keyID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.enableControllerButton = new GuiButton(0, var5 + 70 + 124 - 72, var6 + 16, 48, 20, GCCoreUtil.translate("gui.button.enable.name"));
		this.address = new GuiElementTextBox(1, this, var5 + 66, var6 + 16, 48, 20, "", true, 6, false);
		this.targetAddress = new GuiElementTextBox(2, this, var5 + 122, var6 + 16 + 22, 48, 20, "", true, 6, false);
		this.buttonList.add(this.enableControllerButton);
		this.buttonList.add(this.address);
		this.buttonList.add(this.targetAddress);
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 98;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 113;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 104, 18, 18, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.telepad.desc.0"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 20, 59, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.telepad.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 5, (this.height - this.ySize) / 2 + 42, 117, 13, batterySlotDesc, this.width, this.height, this));
	}

	@Override
	protected void mouseClicked(int px, int py, int par3)
	{
		super.mouseClicked(px, py, par3);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			switch (par1GuiButton.id)
			{
			case 0:
				GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.telepad.xCoord, this.telepad.yCoord, this.telepad.zCoord, 0 }));
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String displayString = this.telepad.getInventoryName();
		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, 4210752);

		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 115, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.address.name") + ":", 7, 22, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.destAddress.name") + ":", 7, 44, 4210752);
        this.fontRendererObj.drawString(this.telepad.getReceivingStatus(), 7, 66, 4210752);
        if (!this.telepad.getReceivingStatus().equals(this.telepad.getSendingStatus()))
            this.fontRendererObj.drawString(this.telepad.getSendingStatus(), 7, 88, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GuiShortRangeTelepad.launchControllerGui);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.telepad.getEnergyStoredGC(), this.telepad.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.telepad.getEnergyStoredGC() > 0)
		{
			int scale = this.telepad.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(var5 + 99, var6 + 114, 176, 0, Math.min(scale, 54), 7);
		}

		GL11.glPopMatrix();
	}

	@Override
	public boolean canPlayerEdit(GuiElementTextBox textBox, EntityPlayer player)
	{
		return player.getGameProfile().getName().equals(this.telepad.getOwner());
	}

	@Override
	public void onTextChanged(GuiElementTextBox textBox, String newText)
	{
		if (textBox.equals(this.address))
		{
			this.telepad.address = textBox.getIntegerValue();
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.S_UPDATE_ADVANCED_GUI, new Object[] { 0, this.telepad.xCoord, this.telepad.yCoord, this.telepad.zCoord, this.telepad.address }));
		}
		else if (textBox.equals(this.targetAddress))
		{
			this.telepad.targetAddress = textBox.getIntegerValue();
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.S_UPDATE_ADVANCED_GUI, new Object[] { 1, this.telepad.xCoord, this.telepad.yCoord, this.telepad.zCoord, this.telepad.targetAddress }));
		}
	}

	@Override
	public String getInitialText(GuiElementTextBox textBox)
	{
		if (textBox.equals(this.address))
		{
			return String.valueOf(this.telepad.address);
		}
		else if (textBox.equals(this.targetAddress))
		{
			return String.valueOf(this.telepad.targetAddress);
		}

		return "";
	}

	@Override
	public int getTextColor(GuiElementTextBox textBox)
	{
		if (textBox.equals(this.address))
		{
			return this.telepad.addressValid ? GCCoreUtil.to32BitColor(255, 20, 255, 20) : GCCoreUtil.to32BitColor(255, 255, 25, 25);
		}
		else if (textBox.equals(this.targetAddress))
		{
			return this.telepad.targetAddressResult == TileEntityShortRangeTelepad.EnumTelepadSearchResult.VALID ? GCCoreUtil.to32BitColor(255, 20, 255, 20) : GCCoreUtil.to32BitColor(255, 255, 25, 25);
		}

		return 0;
	}

	@Override
	public void onIntruderInteraction(GuiElementTextBox textBox)
	{
		this.cannotEditMap.put(textBox.id, 50);
	}
}
