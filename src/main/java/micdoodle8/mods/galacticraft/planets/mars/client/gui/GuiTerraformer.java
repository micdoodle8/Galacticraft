package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementCheckbox.ICheckBoxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerTerraformer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiTerraformer extends GuiContainerGC implements ICheckBoxCallback
{
    private static final ResourceLocation terraformerGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/terraformer.png");
    private TileEntityTerraformer terraformer;
    private GuiButton enableTreesButton;
    private GuiButton enableGrassButton;
    private GuiElementCheckbox checkboxRenderBubble;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 52, 9, null, 0, 0, this);
    private GuiElementInfoRegion waterTankInfoRegion = new GuiElementInfoRegion(0, 0, 41, 28, null, 0, 0, this);

    public GuiTerraformer(InventoryPlayer par1InventoryPlayer, TileEntityTerraformer terraformer)
    {
        super(new ContainerTerraformer(par1InventoryPlayer, terraformer, FMLClientHandler.instance().getClient().thePlayer));
        this.ySize = 237;
        this.terraformer = terraformer;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        if (this.terraformer.disableCooldown > 0)
        {
            this.enableTreesButton.enabled = false;
            this.enableGrassButton.enabled = false;
        }
        else
        {
            this.enableTreesButton.enabled = true;
            this.enableGrassButton.enabled = true;
        }

        this.enableTreesButton.displayString = GCCoreUtil.translate(this.terraformer.treesDisabled ? "gui.button.enable.name" : "gui.button.disable.name") + " " + GCCoreUtil.translate("gui.message.trees.name");
        this.enableGrassButton.displayString = GCCoreUtil.translate(this.terraformer.grassDisabled ? "gui.button.enable.name" : "gui.button.disable.name") + " " + GCCoreUtil.translate("gui.message.grass.name");

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 44;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 47;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 24, (this.height - this.ySize) / 2 + 38, 18, 18, batterySlotDesc, this.width, this.height, this));
        batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.show_bubble.desc.0"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 85, (this.height - this.ySize) / 2 + 132, 85, 13, batterySlotDesc, this.width, this.height, this));
        this.waterTankInfoRegion.tooltipStrings = new ArrayList<String>();
        this.waterTankInfoRegion.xPosition = (this.width - this.xSize) / 2 + 55;
        this.waterTankInfoRegion.yPosition = (this.height - this.ySize) / 2 + 17;
        this.waterTankInfoRegion.parentWidth = this.width;
        this.waterTankInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.waterTankInfoRegion);
        this.buttonList.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.enableTreesButton = new GuiButton(0, var5 + 98, var6 + 85, 72, 20, GCCoreUtil.translate("gui.message.enable_trees.name"));
        this.enableGrassButton = new GuiButton(1, var5 + 98, var6 + 109, 72, 20, GCCoreUtil.translate("gui.message.enable_grass.name"));
        this.buttonList.add(this.enableTreesButton);
        this.buttonList.add(this.enableGrassButton);
        this.checkboxRenderBubble = new GuiElementCheckbox(2, this, var5 + 85, var6 + 132, GCCoreUtil.translate("gui.message.bubble_visible.name"));
        this.buttonList.add(this.checkboxRenderBubble);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.terraformer.getPos(), 0 }));
                break;
            case 1:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.terraformer.getPos(), 1 }));
                break;
            case 2:
                break;
            default:
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String displayString = terraformer.getName();
        this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 144, 4210752);
        this.fontRendererObj.drawSplitString(this.getStatus(), 105, 24, this.xSize - 105, 4210752);
        //		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.terraformer.ueWattsPerTick * 20, ElectricUnit.WATT), 105, 56, 4210752);
        //		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.terraformer.getVoltage(), ElectricUnit.VOLTAGE), 105, 68, 4210752);
    }

    private String getStatus()
    {
        if (RedstoneUtil.isBlockReceivingRedstone(this.terraformer.getWorld(), this.terraformer.getPos()))
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.status.off.name");
        }

        if (this.terraformer.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_energy.name");
        }

        if (this.terraformer.grassDisabled && this.terraformer.treesDisabled)
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
        }

        if (this.terraformer.waterTank.getFluid() == null || this.terraformer.waterTank.getFluid().amount <= 0)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_water.name");
        }

        if (this.terraformer.getFirstBonemealStack() == null)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_bonemeal.name");
        }

        if (!this.terraformer.grassDisabled && this.terraformer.getFirstSeedStack() == null)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_seeds.name");
        }

        if (!this.terraformer.treesDisabled && this.terraformer.getFirstSaplingStack() == null)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.no_saplings.name");
        }

        if (this.terraformer.getBubbleSize() < this.terraformer.MAX_SIZE - 0.5)
        {
            return EnumColor.YELLOW + GCCoreUtil.translate("gui.message.bubble_exp.name");
        }

        if (!this.terraformer.treesDisabled && this.terraformer.grassBlocksListSize <= 0)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.trees.name") + ": " + GCCoreUtil.translate("gui.message.no_valid_blocks.name");
        }
        else if (!this.terraformer.grassDisabled && this.terraformer.terraformableBlocksListSize <= 0)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.grass.name") + ": " + GCCoreUtil.translate("gui.message.no_valid_blocks.name");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.message.terraforming.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiTerraformer.terraformerGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int scale = this.terraformer.getScaledElecticalLevel(54);
        this.drawTexturedModalRect(var5 + 45, var6 + 48, 176, 26, Math.min(scale, 54), 7);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.terraformer.getEnergyStoredGC(), this.terraformer.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        int waterLevel = this.terraformer.getScaledWaterLevel(100);
        List<String> processDesc = new ArrayList<String>();
        processDesc.clear();
        processDesc.add(GCCoreUtil.translate("gui.terraformer.desc.0") + ": " + waterLevel + "%");
        this.waterTankInfoRegion.tooltipStrings = processDesc;

        waterLevel = this.terraformer.getScaledWaterLevel(26);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 56, (this.height - this.ySize) / 2 + 17 + 27 - waterLevel, 176, 26 - waterLevel, 39, waterLevel);

        this.checkboxRenderBubble.isSelected = this.terraformer.shouldRenderBubble;
    }

    @Override
    public void onSelectionChanged(GuiElementCheckbox checkbox, boolean newSelected)
    {
        this.terraformer.shouldRenderBubble = newSelected;
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ON_ADVANCED_GUI_CLICKED_INT, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { 6, this.terraformer.getPos(), newSelected ? 1 : 0 }));
    }

    @Override
    public boolean canPlayerEdit(GuiElementCheckbox checkbox, EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean getInitiallySelected(GuiElementCheckbox checkbox)
    {
        return this.terraformer.shouldRenderBubble;
    }

    @Override
    public void onIntruderInteraction()
    {

    }
}
