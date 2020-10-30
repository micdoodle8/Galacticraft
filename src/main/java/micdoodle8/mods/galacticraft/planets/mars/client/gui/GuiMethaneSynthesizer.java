package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
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
public class GuiMethaneSynthesizer extends GuiContainerGC<ContainerMethaneSynthesizer>
{
    private static final ResourceLocation refineryTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/methane_synthesizer.png");

    private static final ResourceLocation gasTextures = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gases_methane_oxygen_nitrogen.png");

    private final TileEntityMethaneSynthesizer synthesizer;

    private Button buttonDisable;

    private final GuiElementInfoRegion fuelTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion gasTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion gasTank2Region = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 28, 16, 20, new ArrayList<String>(), this.width, this.height, this);
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 16, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiMethaneSynthesizer(ContainerMethaneSynthesizer container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.synthesizer = container.getSynthesizer();
        this.ySize = 168;
    }

    @Override
    public void init()
    {
        super.init();

        int edgeLeft = (this.width - this.xSize) / 2;
        int edgeTop = (this.height - this.ySize) / 2;

        this.gasTankRegion.xPosition = edgeLeft + 7;
        this.gasTankRegion.yPosition = edgeTop + 28;
        this.gasTankRegion.parentWidth = this.width;
        this.gasTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.gasTankRegion);

        this.gasTank2Region.xPosition = edgeLeft + 28;
        this.gasTank2Region.yPosition = edgeTop + 28;
        this.gasTank2Region.parentWidth = this.width;
        this.gasTank2Region.parentHeight = this.height;
        this.infoRegions.add(this.gasTank2Region);

        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 53, edgeTop + 53, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> carbonSlotDesc = new ArrayList<String>();
        carbonSlotDesc.add(GCCoreUtil.translate("gui.carbon_slot.desc.0"));
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 27, edgeTop + 53, 18, 18, carbonSlotDesc, this.width, this.height, this));


        this.fuelTankRegion.xPosition = edgeLeft + 153;
        this.fuelTankRegion.yPosition = edgeTop + 28;
        this.fuelTankRegion.parentWidth = this.width;
        this.fuelTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.fuelTankRegion);

        List<String> fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.add(GCCoreUtil.translate("gui.fuel_output.desc.0"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.fuel_output.desc.1"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.methane_output.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 152, edgeTop + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.hydrogen_input.desc.0"));
        fuelSlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.hydrogen_input.desc.1"));
        fuelSlotDesc.add("(" + GCCoreUtil.translate("gui.message.with_atmosphere0"));
        fuelSlotDesc.add(GCCoreUtil.lowerCaseNoun("fluid.hydrogen"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.message.with_atmosphere1") + ")");
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 6, edgeTop + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.add(GCCoreUtil.translate("item.atmospheric_valve"));
        fuelSlotDesc.add("(" + GCCoreUtil.translate("gui.message.with_atmosphere0"));
        fuelSlotDesc.add(GCCoreUtil.lowerCaseNoun("gas.carbondioxide"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.message.with_atmosphere1") + ")");
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 27, edgeTop + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        this.electricInfoRegion.xPosition = edgeLeft + 66;
        this.electricInfoRegion.yPosition = edgeTop + 16;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);

        this.addToolTips();

        this.buttons.add(this.buttonDisable = new Button(this.width / 2 - 28, this.height / 2 - 56, 76, 20, GCCoreUtil.translate("gui.button.liquefy"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.synthesizer.getWorld()), new Object[]{this.synthesizer.getPos(), 0}));
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 47, 5, 4210752);
        String displayText = "";
        int yOffset = -18;

        if (RedstoneUtil.isBlockReceivingRedstone(this.synthesizer.getWorld(), this.synthesizer.getPos()))
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.off");
        }
        else if (!this.synthesizer.hasEnoughEnergyToRun)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.message.low_energy");
        }
        else if ((this.synthesizer.processTicks > -8 || this.synthesizer.canProcess()))
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.processing");
        }
        else if (this.synthesizer.gasTank.getFluid() == FluidStack.EMPTY || this.synthesizer.gasTank.getFluidAmount() == 0)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.nogas");
        }
        else if (this.synthesizer.gasTank.getFluidAmount() > 0 && this.synthesizer.disabled)
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready");
        }
        else if (this.synthesizer.liquidTank.getFluidAmount() == this.synthesizer.liquidTank.getCapacity())
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.tankfull");
        }
        else
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.needs_carbon");
        }

        this.buttonDisable.active = this.synthesizer.disableCooldown == 0;
        this.buttonDisable.setMessage(this.synthesizer.processTicks <= -8 ? GCCoreUtil.translate("gui.button.liquefy") : GCCoreUtil.translate("gui.button.liquefy_stop"));
        this.font.drawString(GCCoreUtil.translate("gui.message.status") + ":", 72, 45 + 23 + yOffset, 4210752);
        this.font.drawString(displayText, 75, 45 + 33 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23 + yOffset, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23 + yOffset, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 118 + 2 + 23, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.textureManager.bindTexture(GuiMethaneSynthesizer.refineryTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int edgeLeft = (this.width - this.xSize) / 2;
        int edgeTop = (this.height - this.ySize) / 2;
        this.blit(edgeLeft, edgeTop, 0, 0, this.xSize, this.ySize);

        this.minecraft.textureManager.bindTexture(GuiMethaneSynthesizer.gasTextures);
        int displayInt = this.synthesizer.getScaledGasLevel(38);
        this.blit(edgeLeft + 7, edgeTop + 17 + 49 - displayInt, 1 + 17, 38 - displayInt, 16, displayInt);
        displayInt = this.synthesizer.getScaledGasLevel2(20);
        this.blit(edgeLeft + 28, edgeTop + 48 - displayInt, 1 + 2 * 17, 38 - displayInt, 16, displayInt);
        displayInt = this.synthesizer.getScaledFuelLevel(38);
        this.blit(edgeLeft + 153, edgeTop + 17 + 49 - displayInt, 1 + 2 * 17, 38 - displayInt, 16, displayInt);

        this.addToolTips();

        this.minecraft.textureManager.bindTexture(GuiMethaneSynthesizer.refineryTexture);

        if (this.synthesizer.getEnergyStoredGC() > 0)
        {
            this.blit(edgeLeft + 52, edgeTop + 16, 208, 0, 11, 10);
        }

        this.blit(edgeLeft + 66, edgeTop + 17, 176, 38, Math.min(this.synthesizer.getScaledElecticalLevel(54), 54), 7);
    }

    private void addToolTips()
    {
        List<String> gasTankDesc = new ArrayList<String>();
        gasTankDesc.add(GCCoreUtil.translate("gui.gas_tank.desc.0"));
        FluidStack gasTankContents = this.synthesizer.gasTank != null ? this.synthesizer.gasTank.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = GCFluids.HYDROGEN.getFluid().getAttributes().getTranslationKey();
            if (gasname == null || gasname.equals("fluid.hydrogen"))
            {
                gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            }
            gasTankDesc.add("(" + gasname + ")");
        }
        else
        {
            gasTankDesc.add(" ");
        }
        int gasLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        int gasCapacity = this.synthesizer.gasTank != null ? this.synthesizer.gasTank.getCapacity() : 0;
        gasTankDesc.add(EnumColor.YELLOW + " " + gasLevel + " / " + gasCapacity);
        this.gasTankRegion.tooltipStrings = gasTankDesc;

        gasTankDesc = new ArrayList<String>();
        gasTankDesc.add(GCCoreUtil.translate("gas.carbondioxide"));
        gasTankDesc.add(GCCoreUtil.translate("gui.gas_tank.desc.0"));
        gasTankContents = this.synthesizer.gasTank2 != null ? this.synthesizer.gasTank2.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = PlanetFluids.GAS_CARBON_DIOXIDE.getFluid().getAttributes().getTranslationKey();
            if (gasname == null || gasname.equals("fluid.carbondioxide"))
            {
                gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            }
            gasTankDesc.add("(" + gasname + ")");
        }
        else
        {
            gasTankDesc.add(" ");
        }
        gasLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        gasCapacity = this.synthesizer.gasTank2 != null ? this.synthesizer.gasTank2.getCapacity() : 0;
        gasTankDesc.add(EnumColor.YELLOW + " " + gasLevel + " / " + gasCapacity);
        this.gasTank2Region.tooltipStrings = gasTankDesc;

        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.gas_tank.desc.0"));
        gasTankContents = this.synthesizer.liquidTank != null ? this.synthesizer.liquidTank.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = PlanetFluids.GAS_METHANE.getFluid().getAttributes().getTranslationKey();
            if (gasname == null || gasname.equals("fluid.methane"))
            {
                gasname = GCCoreUtil.translate(gasTankContents.getFluid().getAttributes().getTranslationKey());
            }
            fuelTankDesc.add("(" + gasname + ")");
        }
        else
        {
            fuelTankDesc.add(" ");
        }
        int fuelLevel = gasTankContents != null ? gasTankContents.getAmount() : 0;
        int fuelCapacity = this.synthesizer.liquidTank != null ? this.synthesizer.liquidTank.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + " " + fuelLevel + " / " + fuelCapacity);
        this.fuelTankRegion.tooltipStrings = fuelTankDesc;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.tileEntity.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tileEntity.getMaxEnergyStoredGC())));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.synthesizer.getEnergyStoredGC(), this.synthesizer.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;
    }
}
