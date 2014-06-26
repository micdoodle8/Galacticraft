package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCargoRocket extends GuiContainerGC
{
	private static ResourceLocation[] rocketTextures = new ResourceLocation[4];

	static
	{
		for (int i = 0; i < 4; i++)
		{
			GuiCargoRocket.rocketTextures[i] = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/rocket_" + i * 18 + ".png");
		}
	}

	private final IInventory upperChestInventory;
	private final EnumRocketType rocketType;
	private EntityCargoRocket rocket;
	private GuiButton launchButton;

	public GuiCargoRocket(IInventory par1IInventory, EntityCargoRocket rocket)
	{
		this(par1IInventory, rocket, rocket.rocketType);
	}

	public GuiCargoRocket(IInventory par1IInventory, EntityCargoRocket rocket, EnumRocketType rocketType)
	{
		super(new ContainerRocketInventory(par1IInventory, rocket, rocketType));
		this.upperChestInventory = par1IInventory;
		this.rocket = rocket;
		this.allowUserInput = false;
		this.ySize = rocketType.getInventorySpace() <= 3 ? 132 : 145 + rocketType.getInventorySpace() * 2;
		this.rocketType = rocketType;
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch (button.id)
		{
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_CARGO_ROCKET_STATUS, new Object[] { this.rocket.getEntityId(), 0 }));
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		final int var6 = (this.height - this.ySize) / 2;
		final int var7 = (this.width - this.xSize) / 2;
		this.launchButton = new GuiButton(0, var7 + 116, var6 + 26, 50, 20, "Launch");
		this.buttonList.add(this.launchButton);
		List<String> oxygenDesc = new ArrayList<String>();
		oxygenDesc.add("Rocket fuel tank. Requires");
		oxygenDesc.add("fuel loader to fill");
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + (this.rocket.rocketType.getInventorySpace() == 2 ? 70 : 71), (this.height - this.ySize) / 2 + 6, 36, 40, oxygenDesc, this.width, this.height));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		if (this.rocket.rocketType.getInventorySpace() == 2)
		{
			this.fontRendererObj.drawString(GCCoreUtil.translate(this.upperChestInventory.getInventoryName()), 8, 76 + (this.rocket.rocketType.getInventorySpace() - 20) / 9 * 18, 4210752);
		}
		else
		{
			this.fontRendererObj.drawString(GCCoreUtil.translate(this.upperChestInventory.getInventoryName()), 8, 89 + (this.rocket.rocketType.getInventorySpace() - 20) / 9 * 18, 4210752);
		}

		String str = GCCoreUtil.translate("gui.message.fuel.name") + ":";
		this.fontRendererObj.drawString(str, 140 - this.fontRendererObj.getStringWidth(str) / 2, 5, 4210752);
		final double percentage = this.rocket.getScaledFuelLevel(100);
		String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.code : percentage > 40.0D ? EnumColor.ORANGE.code : EnumColor.RED.code;
		str = percentage + "% " + GCCoreUtil.translate("gui.message.full.name");
		this.fontRendererObj.drawString(color + str, 140 - this.fontRendererObj.getStringWidth(str) / 2, 15, 4210752);
		str = GCCoreUtil.translate("gui.message.status.name") + ":";
		this.fontRendererObj.drawString(str, 40 - this.fontRendererObj.getStringWidth(str) / 2, 9, 4210752);

		String[] spltString = { "" };

		if (this.rocket.statusMessageCooldown == 0 || this.rocket.statusMessage == null)
		{
			spltString = new String[2];
			spltString[0] = EnumColor.YELLOW + "Waiting for";
			spltString[1] = EnumColor.YELLOW + "ignition";

			if (this.rocket.launchPhase != EnumLaunchPhase.UNIGNITED.ordinal())
			{
				spltString = new String[2];
				spltString[0] = EnumColor.YELLOW + "Already";
				spltString[1] = EnumColor.YELLOW + "Launched";
				this.launchButton.enabled = false;
			}
		}
		else
		{
			spltString = this.rocket.statusMessage.split("#");
		}

		int y = 2;
		for (String splitString : spltString)
		{
			color = splitString.substring(0, 6);
			str = splitString.substring(6, splitString.length());
			this.fontRendererObj.drawString(color + str, 30 - this.fontRendererObj.getStringWidth(str) / 2, 9 * y, 4210752);
			y++;
		}

		if (this.rocket.statusValid && this.rocket.statusMessageCooldown > 0 && this.rocket.statusMessageCooldown < 4)
		{
			this.mc.displayGuiScreen(null);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.getTextureManager().bindTexture(GuiCargoRocket.rocketTextures[(this.rocketType.getInventorySpace() - 2) / 18]);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, 176, this.ySize);

		final int fuelLevel = this.rocket.getScaledFuelLevel(38);
		this.drawTexturedModalRect((this.width - this.xSize) / 2 + (this.rocket.rocketType.getInventorySpace() == 2 ? 71 : 72), (this.height - this.ySize) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
	}
}
