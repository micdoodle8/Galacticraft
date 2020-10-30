package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerGasLiquefier;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiGasLiquefier extends GuiContainerGC<ContainerGasLiquefier>
{
    private static final ResourceLocation refineryTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gas_liquefier.png");

    private static final ResourceLocation gasTextures = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gases_methane_oxygen_nitrogen.png");

    private final TileEntityGasLiquefier gasLiquefier;

    private Button buttonDisable;

    private final GuiElementInfoRegion fuelTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion fuelTank2Region = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion gasTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 16, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiGasLiquefier(ContainerGasLiquefier container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.gasLiquefier = container.getGasLiquefier();
        this.ySize = 168;
    }

    @Override
    public void init()
    {
        super.init();

        this.gasTankRegion.xPosition = (this.width - this.xSize) / 2 + 7;
        this.gasTankRegion.yPosition = (this.height - this.ySize) / 2 + 28;
        this.gasTankRegion.parentWidth = this.width;
        this.gasTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.gasTankRegion);

        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 33, (this.height - this.ySize) / 2 + 49, 18, 18, batterySlotDesc, this.width, this.height, this));

        this.fuelTankRegion.xPosition = (this.width - this.xSize) / 2 + 132;
        this.fuelTankRegion.yPosition = (this.height - this.ySize) / 2 + 28;
        this.fuelTankRegion.parentWidth = this.width;
        this.fuelTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.fuelTankRegion);

        this.fuelTank2Region.xPosition = (this.width - this.xSize) / 2 + 153;
        this.fuelTank2Region.yPosition = (this.height - this.ySize) / 2 + 28;
        this.fuelTank2Region.parentWidth = this.width;
        this.fuelTank2Region.parentHeight = this.height;
        this.infoRegions.add(this.fuelTank2Region);

        List<String> fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.add(GCCoreUtil.translate("gui.fuel_output.desc.0"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.fuel_output.desc.1"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.liquid_output.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 152, (this.height - this.ySize) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 131, (this.height - this.ySize) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.gas_input.desc.0"));
        fuelSlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.gas_input.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 6, (this.height - this.ySize) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 42;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 16;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);

        this.addToolTips();

        this.buttons.add(this.buttonDisable = new Button(this.width / 2 - 49, this.height / 2 - 56, 76, 20, GCCoreUtil.translate("gui.button.liquefy"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.gasLiquefier.getWorld()), new Object[]{this.gasLiquefier.getPos(), 0}));
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 40, 5, 4210752);
        String displayText = "";
        int yOffset = -18;

        if (RedstoneUtil.isBlockReceivingRedstone(this.gasLiquefier.getWorld(), this.gasLiquefier.getPos()))
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.off");
        }
        else if (!this.gasLiquefier.hasEnoughEnergyToRun)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.message.low_energy");
        }
        else if ((this.gasLiquefier.processTicks > -10 || this.gasLiquefier.canProcess()))
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.liquefying");
        }
        else if (this.gasLiquefier.gasTank.getFluid() == FluidStack.EMPTY || this.gasLiquefier.gasTank.getFluidAmount() <= 0)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.nogas");
        }
        else if (this.gasLiquefier.gasTank.getFluidAmount() > 0 && this.gasLiquefier.disabled)
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready");
        }
        else if (this.gasLiquefier.liquidTank.getFluidAmount() == this.gasLiquefier.liquidTank.getCapacity() && this.gasLiquefier.liquidTank2.getFluidAmount() == this.gasLiquefier.liquidTank2.getCapacity())
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.tanksfull");
        }
        else
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.unknown");
        }

        this.buttonDisable.active = this.gasLiquefier.disableCooldown == 0;
        this.buttonDisable.setMessage(this.gasLiquefier.processTicks <= -10 ? GCCoreUtil.translate("gui.button.liquefy") : GCCoreUtil.translate("gui.button.liquefy_stop"));
        this.font.drawString(GCCoreUtil.translate("gui.message.status") + ":", 56, 45 + 23 + yOffset, 4210752);
        this.font.drawString(displayText, 62, 45 + 33 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23 + yOffset, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 118 + 2 + 23, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bindTexture(GuiGasLiquefier.refineryTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int edgeLeft = (this.width - this.xSize) / 2;
        int edgeTop = (this.height - this.ySize) / 2;
        this.blit(edgeLeft, edgeTop, 0, 0, this.xSize, this.ySize);

        //Fluid tank 1
        int liquidType = this.gasLiquefier.fluidTankType;  //0 for fuel, 1 for oxygen, 2 for atmospheric gases
        if (liquidType == 0)
        {
            int displayInt = this.gasLiquefier.getScaledFuelLevel(38);
            this.blit(edgeLeft + 132, edgeTop + 17 + 49 - displayInt, 176 + 16, 38 - displayInt, 16, displayInt);
        }

        //Fluid tank 2
        int liquidType2 = this.gasLiquefier.fluidTank2Type;  //0 for fuel, 1 for oxygen, 2 for atmospheric gases
        if (liquidType2 == 0)
        {
            int displayInt = this.gasLiquefier.getScaledFuelLevel2(38);
            this.blit(edgeLeft + 153, edgeTop + 17 + 49 - displayInt, 176 + 16, 38 - displayInt, 16, displayInt);
        }

        this.minecraft.textureManager.bindTexture(GuiGasLiquefier.gasTextures);
        int displayInt = this.gasLiquefier.getScaledGasLevel(38);
        int gasType = this.gasLiquefier.gasTankType;  //0 for methane, 1 for oxygen, 2 for atmospheric gases
        if (gasType > 2)
        {
            gasType = 2;
        }
        if (gasType >= 0)
        {
            this.blit(edgeLeft + 7, edgeTop + 17 + 49 - displayInt, 1 + gasType * 17, 38 - displayInt, 16, displayInt);
        }
        if (liquidType > 0)
        {
            displayInt = this.gasLiquefier.getScaledFuelLevel(38);
            this.blit(edgeLeft + 132, edgeTop + 17 + 49 - displayInt, (liquidType + 2) * 17 + 1, 38 - displayInt, 16, displayInt);
        }
        if (liquidType2 > 0)
        {
            displayInt = this.gasLiquefier.getScaledFuelLevel2(38);
            this.blit(edgeLeft + 153, edgeTop + 17 + 49 - displayInt, (liquidType2 + 2) * 17 + 1, 38 - displayInt, 16, displayInt);
        }

        this.addToolTips();

        this.minecraft.textureManager.bindTexture(GuiGasLiquefier.refineryTexture);

        if (this.gasLiquefier.getEnergyStoredGC() > 0)
        {
            this.blit(edgeLeft + 28, edgeTop + 16, 208, 0, 11, 10);
        }

        this.blit(edgeLeft + 42, edgeTop + 17, 176, 38, Math.min(this.gasLiquefier.getScaledElecticalLevel(54), 54), 7);
    }

    private void addToolTips()
    {
        List<String> gasTankDesc = new ArrayList<String>();
        gasTankDesc.add(GCCoreUtil.translate("gui.gas_tank_compressed.desc.0"));
        FluidStack gasTankContents = this.gasLiquefier.gasTank != null ? this.gasLiquefier.gasTank.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            gasTankDesc.add("(" + gasname + ")");
        }
        else
        {
            gasTankDesc.add(" ");
        }
        int gasLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        int gasCapacity = this.gasLiquefier.gasTank != null ? this.gasLiquefier.gasTank.getCapacity() : 0;
        gasTankDesc.add(EnumColor.YELLOW + " " + gasLevel + " / " + gasCapacity);
        this.gasTankRegion.tooltipStrings = gasTankDesc;

        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.liquid_tank.desc.0"));
        gasTankContents = this.gasLiquefier.liquidTank != null ? this.gasLiquefier.liquidTank.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            fuelTankDesc.add("(" + gasname + ")");
        }
        else
        {
            fuelTankDesc.add(" ");
        }
        int fuelLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        int fuelCapacity = this.gasLiquefier.liquidTank != null ? this.gasLiquefier.liquidTank.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + " " + fuelLevel + " / " + fuelCapacity);
        this.fuelTankRegion.tooltipStrings = fuelTankDesc;

        fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.liquid_tank.desc.0"));
        gasTankContents = this.gasLiquefier.liquidTank2 != null ? this.gasLiquefier.liquidTank2.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            fuelTankDesc.add("(" + gasname + ")");
        }
        else
        {
            fuelTankDesc.add(" ");
        }
        fuelLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        fuelCapacity = this.gasLiquefier.liquidTank2 != null ? this.gasLiquefier.liquidTank2.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + " " + fuelLevel + " / " + fuelCapacity);
        this.fuelTank2Region.tooltipStrings = fuelTankDesc;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.tileEntity.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tileEntity.getMaxEnergyStoredGC())));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.gasLiquefier.getEnergyStoredGC(), this.gasLiquefier.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;
    }
}
