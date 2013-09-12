package micdoodle8.mods.galacticraft.mars.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GCMarsGuiLaunchController extends GuiContainer
{
    private static final ResourceLocation launchControllerGui = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/gui/launchController.png");

    private GCMarsTileEntityLaunchController launchController;

    private GuiButton enableControllerButton;
    private GuiButton enablePadRemovalButton;

    public GCMarsGuiLaunchController(InventoryPlayer playerInventory, GCMarsTileEntityLaunchController launchController)
    {
        super(new GCMarsContainerLaunchController(playerInventory, launchController));
        this.ySize = 209;
        this.launchController = launchController;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        if (this.launchController.disableCooldown > 0)
        {
            this.enableControllerButton.enabled = false;
            this.enablePadRemovalButton.enabled = false;
        }
        else
        {
            this.enableControllerButton.enabled = true;
            this.enablePadRemovalButton.enabled = true;
        }

        this.enableControllerButton.displayString = (this.launchController.getDisabled(0) ? "Enable" : "Disable");
        this.enablePadRemovalButton.displayString = (this.launchController.getDisabled(1) ? "Enable" : "Disable") + " Launch Pad Removal";

        super.drawScreen(par1, par2, par3);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.enableControllerButton = new GuiButton(0, var5 + 98, var6 + 24, 68, 20, "Enable");
        this.enablePadRemovalButton = new GuiButton(1, (this.width / 2) - 78, var6 + 61, 156, 20, "Disable Launch Pad Removal");
        this.buttonList.add(this.enableControllerButton);
        this.buttonList.add(this.enablePadRemovalButton);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] { this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, 0 }));
                break;
            case 1:
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] { this.launchController.xCoord, this.launchController.yCoord, this.launchController.zCoord, 1 }));
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = this.launchController.getInvName();
        this.fontRenderer.drawString(displayString, this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 5, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 115, 4210752);
        displayString = this.getStatus();
        this.fontRenderer.drawString(displayString, this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 85, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.launchController.ueWattsPerTick * 20, ElectricUnit.WATT), 5, 38, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.launchController.getVoltage(), ElectricUnit.VOLTAGE), 5, 50, 4210752);
    }

    private String getStatus()
    {
        if (this.launchController.getEnergyStored() <= 0.0F)
        {
            return EnumColor.RED + "Not Enough Energy";
        }

        if (this.launchController.getDisabled(0))
        {
            return EnumColor.ORANGE + "Disabled";
        }

        return EnumColor.BRIGHT_GREEN + "Active";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GCMarsGuiLaunchController.launchControllerGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int scale = this.launchController.getScaledElecticalLevel(54);
        this.drawTexturedModalRect(var5 + 7, var6 + 27, 176, 0, Math.min(scale, 54), 7);
    }
}
