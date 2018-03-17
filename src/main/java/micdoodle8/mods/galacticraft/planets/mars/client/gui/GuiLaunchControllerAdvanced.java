package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket.EnumAutoLaunch;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown.IDropboxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

public class GuiLaunchControllerAdvanced extends GuiContainerGC implements IDropboxCallback, ICheckBoxCallback
{
    private static final ResourceLocation launchControllerGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/launch_controller.png");

    private TileEntityLaunchController launchController;

    private GuiElementCheckbox enablePadRemovalButton;
    private GuiElementCheckbox launchWhenCheckbox;
    private GuiElementDropdown dropdownTest;
    private GuiButton closeAdvancedConfig;

    private int cannotEditTimer;

    public GuiLaunchControllerAdvanced(InventoryPlayer playerInventory, TileEntityLaunchController launchController)
    {
        super(new ContainerLaunchController(playerInventory, launchController, FMLClientHandler.instance().getClient().thePlayer));
        this.ySize = 209;
        this.launchController = launchController;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        if (this.launchController.disableCooldown > 0)
        {
            this.enablePadRemovalButton.enabled = false;
        }
        else
        {
            boolean isOwner = PlayerUtil.getName(this.mc.thePlayer).equals(this.launchController.getOwnerName());
            this.enablePadRemovalButton.enabled = isOwner;
        }

        // Hacky way of rendering buttons properly, possibly bugs here:
        List<GuiButton> buttonList = new ArrayList<>(this.buttonList);
        List<GuiLabel> labelList = new ArrayList<>(this.labelList);
        List<GuiElementInfoRegion> infoRegions = new ArrayList<>(this.infoRegions);
        this.buttonList.clear();
        this.labelList.clear();
        this.infoRegions.clear();
        super.drawScreen(par1, par2, par3);

        GL11.glColor3f(1, 1, 1);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int k;
        for (k = 0; k < buttonList.size(); ++k)
        {
            ((GuiButton) buttonList.get(k)).drawButton(this.mc, par1, par2);
        }

        for (k = 0; k < labelList.size(); ++k)
        {
            ((GuiLabel) labelList.get(k)).drawLabel(this.mc, par1, par2);
        }

        for (k = 0; k < infoRegions.size(); ++k)
        {
            infoRegions.get(k).drawRegion(par1, par2);
        }

        this.buttonList = buttonList;
        this.labelList = labelList;
        this.infoRegions = infoRegions;

//		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        final int xLeft = (this.width - this.xSize) / 2;
        final int yTop = (this.height - this.ySize) / 2;
        this.enablePadRemovalButton = new GuiElementCheckbox(1, this, this.width / 2 - 61, yTop + 20, GCCoreUtil.translate("gui.message.remove_pad.name"));
        this.launchWhenCheckbox = new GuiElementCheckbox(2, this, this.width / 2 - 61, yTop + 38, GCCoreUtil.translate("gui.message.launch_when.name") + ": ");
        this.dropdownTest = new GuiElementDropdown(3, this, xLeft + 52, yTop + 52, EnumAutoLaunch.CARGO_IS_UNLOADED.getTitle(), EnumAutoLaunch.CARGO_IS_FULL.getTitle(), EnumAutoLaunch.ROCKET_IS_FUELED.getTitle(), EnumAutoLaunch.INSTANT.getTitle(), EnumAutoLaunch.TIME_10_SECONDS.getTitle(), EnumAutoLaunch.TIME_30_SECONDS.getTitle(), EnumAutoLaunch.TIME_1_MINUTE.getTitle(), EnumAutoLaunch.REDSTONE_SIGNAL.getTitle());
        this.closeAdvancedConfig = new GuiButton(4, xLeft + 5, yTop + 5, 20, 20, "<");
        this.buttonList.add(this.enablePadRemovalButton);
        this.buttonList.add(this.launchWhenCheckbox);
        this.buttonList.add(this.dropdownTest);
        this.buttonList.add(this.closeAdvancedConfig);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion(xLeft + 151, yTop + 104, 18, 18, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launch_controller.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion(xLeft + 27, yTop + 20, 13, 13, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.launch_controller.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion(xLeft + 52, yTop + 53, 99, 13, batterySlotDesc, this.width, this.height, this));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (!PlayerUtil.getName(this.mc.thePlayer).equals(this.launchController.getOwnerName()))
        {
            this.cannotEditTimer = 50;
            return;
        }

        if (button.enabled)
        {
            switch (button.id)
            {
                case 4:
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_SWITCH_LAUNCH_CONTROLLER_GUI, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.launchController.getPos(), 1 }));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = GCCoreUtil.translate("gui.launch_controller.owner") + ": " + this.launchController.getOwnerName();
        this.fontRendererObj.drawString(displayString, this.xSize - this.fontRendererObj.getStringWidth(displayString) - 5, 5, 4210752);

        if (this.cannotEditTimer > 0)
        {
            this.fontRendererObj.drawString(this.launchController.getOwnerName(), this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, this.cannotEditTimer % 30 < 15 ? ColorUtil.to32BitColor(255, 255, 100, 100) : 4210752);
            this.cannotEditTimer--;
        }

        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 115, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiLaunchControllerAdvanced.launchControllerGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.launchController.getEnergyStoredGC() > 0)
        {
            int scale = this.launchController.getScaledElecticalLevel(54);
            this.drawTexturedModalRect(var5 + 99, var6 + 114, 176, 0, Math.min(scale, 54), 7);
        }

        GL11.glPopMatrix();
    }

    @Override
    public boolean canBeClickedBy(GuiElementDropdown dropdown, EntityPlayer player)
    {
        if (dropdown.equals(this.dropdownTest))
        {
            return PlayerUtil.getName(player).equals(this.launchController.getOwnerName());
        }

        return false;
    }

    @Override
    public void onSelectionChanged(GuiElementDropdown dropdown, int selection)
    {
        if (dropdown.equals(this.dropdownTest))
        {
            this.launchController.launchDropdownSelection = selection;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { 1, this.launchController.getPos(), this.launchController.launchDropdownSelection }));
        }
    }

    @Override
    public int getInitialSelection(GuiElementDropdown dropdown)
    {
        if (dropdown.equals(this.dropdownTest))
        {
            return this.launchController.launchDropdownSelection;
        }

        return 0;
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        if (checkbox.equals(this.enablePadRemovalButton))
        {
            this.launchController.launchPadRemovalDisabled = !newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { 3, this.launchController.getPos(), this.launchController.launchPadRemovalDisabled ? 1 : 0 }));
        }
        else if (checkbox.equals(this.launchWhenCheckbox))
        {
            this.launchController.launchSchedulingEnabled = newSelected;
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { 4, this.launchController.getPos(), this.launchController.launchSchedulingEnabled ? 1 : 0 }));
        }
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
    {
        return PlayerUtil.getName(player).equals(this.launchController.getOwnerName());
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
    {
        if (checkbox.equals(this.enablePadRemovalButton))
        {
            return !this.launchController.launchPadRemovalDisabled;
        }
        else if (checkbox.equals(this.launchWhenCheckbox))
        {
            return this.launchController.launchSchedulingEnabled;
        }

        return false;
    }

    @Override
    public void onIntruderInteraction()
    {
        this.cannotEditTimer = 50;
    }
}
