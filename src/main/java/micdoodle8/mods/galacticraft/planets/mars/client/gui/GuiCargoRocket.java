package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.Constants;
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
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCargoRocket extends GuiContainerGC
{
    private static ResourceLocation[] rocketTextures = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiCargoRocket.rocketTextures[i] = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/rocket_" + i * 18 + ".png");
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
        super(new ContainerRocketInventory(par1IInventory, rocket, rocketType, FMLClientHandler.instance().getClient().player));
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
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_CARGO_ROCKET_STATUS, GCCoreUtil.getDimensionID(mc.world), new Object[] { this.rocket.getEntityId(), 0 }));
            break;
        default:
            break;
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        final int var6 = (this.height - this.ySize) / 2;
        final int var7 = (this.width - this.xSize) / 2;
        this.launchButton = new GuiButton(0, var7 + 116, var6 + 26, 50, 20, GCCoreUtil.translate("gui.message.launch.name"));
        this.buttonList.add(this.launchButton);
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.0"));
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + (this.rocket.rocketType.getInventorySpace() == 2 ? 70 : 71), (this.height - this.ySize) / 2 + 6, 36, 40, fuelTankDesc, this.width, this.height, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        if (this.rocket.rocketType.getInventorySpace() == 2)
        {
            this.fontRenderer.drawString(GCCoreUtil.translate(this.upperChestInventory.getName()), 8, 76 + (this.rocket.rocketType.getInventorySpace() - 20) / 9 * 18, 4210752);
        }
        else
        {
            this.fontRenderer.drawString(GCCoreUtil.translate(this.upperChestInventory.getName()), 8, 89 + (this.rocket.rocketType.getInventorySpace() - 20) / 9 * 18, 4210752);
        }

        String str = GCCoreUtil.translate("gui.message.fuel.name") + ":";
        this.fontRenderer.drawString(str, 140 - this.fontRenderer.getStringWidth(str) / 2, 5, 4210752);
        final double percentage = this.rocket.getScaledFuelLevel(100);
        String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.getCode() : percentage > 40.0D ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode();
        str = percentage + "% " + GCCoreUtil.translate("gui.message.full.name");
        this.fontRenderer.drawString(color + str, 140 - this.fontRenderer.getStringWidth(str) / 2, 15, 4210752);
        str = GCCoreUtil.translate("gui.message.status.name") + ":";
        this.fontRenderer.drawString(str, 40 - this.fontRenderer.getStringWidth(str) / 2, 9, 4210752);

        String[] spltString = { "" };
        String colour = EnumColor.YELLOW.toString();

        if (this.rocket.statusMessageCooldown == 0 || this.rocket.statusMessage == null)
        {
            spltString = new String[2];
            spltString[0] = GCCoreUtil.translate("gui.cargorocket.status.waiting.0");
            spltString[1] = GCCoreUtil.translate("gui.cargorocket.status.waiting.1");

            if (this.rocket.launchPhase != EnumLaunchPhase.UNIGNITED.ordinal())
            {
                spltString = new String[2];
                spltString[0] = GCCoreUtil.translate("gui.cargorocket.status.launched.0");
                spltString[1] = GCCoreUtil.translate("gui.cargorocket.status.launched.1");
                this.launchButton.enabled = false;
            }
        }
        else
        {
            spltString = this.rocket.statusMessage.split("#");
            colour = this.rocket.statusColour;
        }

        int y = 2;
        for (String splitString : spltString)
        {
            this.fontRenderer.drawString(colour + splitString, 35 - this.fontRenderer.getStringWidth(splitString) / 2, 9 * y, 4210752);
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
