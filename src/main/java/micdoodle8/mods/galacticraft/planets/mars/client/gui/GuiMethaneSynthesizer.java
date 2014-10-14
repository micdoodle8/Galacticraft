package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMethaneSynthesizer extends GuiContainerGC
{
    private static final ResourceLocation refineryTexture = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/gui/methaneSynthesizer.png");

    private static final ResourceLocation gasTextures = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/gui/gasesMethaneOxygenNitrogen.png");

    private final TileEntityMethaneSynthesizer tileEntity;

    private GuiButton buttonDisable;

    private GuiElementInfoRegion fuelTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private GuiElementInfoRegion gasTankRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 28, 16, 38, new ArrayList<String>(), this.width, this.height, this);
    private GuiElementInfoRegion gasTank2Region = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 28, 16, 20, new ArrayList<String>(), this.width, this.height, this);
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 62, (this.height - this.ySize) / 2 + 16, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiMethaneSynthesizer(InventoryPlayer par1InventoryPlayer, TileEntityMethaneSynthesizer tileEntity)
    {
        super(new ContainerMethaneSynthesizer(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        this.ySize = 168;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

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
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 53, edgeTop + 53, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> carbonSlotDesc = new ArrayList<String>();
        carbonSlotDesc.add(GCCoreUtil.translate("gui.carbonSlot.desc.0"));
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 27, edgeTop + 53, 18, 18, carbonSlotDesc, this.width, this.height, this));

        
        this.fuelTankRegion.xPosition = edgeLeft + 153;
        this.fuelTankRegion.yPosition = edgeTop + 28;
        this.fuelTankRegion.parentWidth = this.width;
        this.fuelTankRegion.parentHeight = this.height;
        this.infoRegions.add(this.fuelTankRegion);

        List<String> fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.add(GCCoreUtil.translate("gui.fuelOutput.desc.0"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.fuelOutput.desc.1"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.methaneOutput.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 152, edgeTop + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 131, edgeTop + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.hydrogenInput.desc.0"));
        fuelSlotDesc.addAll(GCCoreUtil.translateWithSplit("gui.hydrogenInput.desc.1"));
        fuelSlotDesc.add("(" + GCCoreUtil.translate("gui.message.withAtmosphere0.name"));
        fuelSlotDesc.add(GCCoreUtil.translate("fluid.hydrogen.name"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.message.withAtmosphere1.name") + ")");
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 6, edgeTop + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.add(GCCoreUtil.translate("item.atmosphericValve.name"));
        fuelSlotDesc.add("(" + GCCoreUtil.translate("gui.message.withAtmosphere0.name"));
        fuelSlotDesc.add(GCCoreUtil.translate("gas.carbondioxide.name"));
        fuelSlotDesc.add(GCCoreUtil.translate("gui.message.withAtmosphere1.name") + ")");
        this.infoRegions.add(new GuiElementInfoRegion(edgeLeft + 27, edgeTop + 6, 18, 18, fuelSlotDesc, this.width, this.height, this));

        this.electricInfoRegion.xPosition = edgeLeft + 66;
        this.electricInfoRegion.yPosition = edgeTop + 16;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);

        this.addToolTips();

        this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 28, this.height / 2 - 56, 76, 20, GCCoreUtil.translate("gui.button.liquefy.name")));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord, 0 }));
            break;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(this.tileEntity.getInventoryName(), 47, 5, 4210752);
        String displayText = "";
        int yOffset = -18;

        if ((this.tileEntity.processTicks > -8 || this.tileEntity.canProcess()) && this.tileEntity.hasEnoughEnergyToRun)
        {
            displayText = EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.status.processing.name");
        }
        else if (this.tileEntity.gasTank.getFluid() == null || this.tileEntity.gasTank.getFluidAmount() == 0)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.nogas.name");
        }
        else if (this.tileEntity.gasTank.getFluidAmount() > 0 && this.tileEntity.disabled)
        {
            displayText = EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready.name");
        }
        else if (!this.tileEntity.hasEnoughEnergyToRun)
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.message.lowEnergy.name");
        }
        else if (this.tileEntity.liquidTank.getFluidAmount() == this.tileEntity.liquidTank.getCapacity())
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.tankfull.name");
        }
        else
        {
            displayText = EnumColor.RED + GCCoreUtil.translate("gui.status.needsCarbon.name");
        }

        this.buttonDisable.enabled = this.tileEntity.disableCooldown == 0;
        this.buttonDisable.displayString = this.tileEntity.processTicks <= -8 ? GCCoreUtil.translate("gui.button.liquefy.name") : GCCoreUtil.translate("gui.button.liquefyStop.name");
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.status.name") + ":", 72, 45 + 23 + yOffset, 4210752);
        this.fontRendererObj.drawString(displayText, 75, 45 + 33 + yOffset, 4210752);
        //		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23 + yOffset, 4210752);
        //		this.fontRendererObj.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23 + yOffset, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 118 + 2 + 23, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.bindTexture(GuiMethaneSynthesizer.refineryTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int edgeLeft = (this.width - this.xSize) / 2;
        int edgeTop = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(edgeLeft, edgeTop, 0, 0, this.xSize, this.ySize);

        this.mc.renderEngine.bindTexture(GuiMethaneSynthesizer.gasTextures);
        int displayInt = this.tileEntity.getScaledGasLevel(38);
        this.drawTexturedModalRect(edgeLeft + 7, edgeTop + 17 + 49 - displayInt, 1 + 2 * 17, 38 - displayInt, 16, displayInt);
        displayInt = this.tileEntity.getScaledGasLevel2(20);
        this.drawTexturedModalRect(edgeLeft + 28, edgeTop + 48 - displayInt, 1 + 2 * 17, 38 - displayInt, 16, displayInt);
        displayInt = this.tileEntity.getScaledFuelLevel(38);
        this.drawTexturedModalRect(edgeLeft + 153, edgeTop + 17 + 49 - displayInt, 1, 38 - displayInt, 16, displayInt);

        this.addToolTips();

        this.mc.renderEngine.bindTexture(GuiMethaneSynthesizer.refineryTexture);

        if (this.tileEntity.getEnergyStoredGC() > 0)
        {
            this.drawTexturedModalRect(edgeLeft + 52, edgeTop + 16, 208, 0, 11, 10);
        }

        this.drawTexturedModalRect(edgeLeft + 66, edgeTop + 17, 176, 38, Math.min(this.tileEntity.getScaledElecticalLevel(54), 54), 7);
    }

    private void addToolTips()
    {
        List<String> gasTankDesc = new ArrayList<String>();
        gasTankDesc.add(GCCoreUtil.translate("gui.gasTank.desc.0"));
        FluidStack gasTankContents = this.tileEntity.gasTank != null ? this.tileEntity.gasTank.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = FluidRegistry.getFluid("hydrogen").getUnlocalizedName();
            if (gasname == null || gasname.equals("fluid.hydrogen"))
            {
                gasname = GCCoreUtil.translate(gasTankContents.getFluid().getUnlocalizedName());
            }
            gasTankDesc.add("(" + gasname + ")");
        }
        else
        {
            gasTankDesc.add(" ");
        }
        int gasLevel = gasTankContents != null ? gasTankContents.amount : 0;
        int gasCapacity = this.tileEntity.gasTank != null ? this.tileEntity.gasTank.getCapacity() : 0;
        gasTankDesc.add(EnumColor.YELLOW + " " + gasLevel + " / " + gasCapacity);
        this.gasTankRegion.tooltipStrings = gasTankDesc;

        gasTankDesc = new ArrayList<String>();
        gasTankDesc.add(GCCoreUtil.translate("gas.carbondioxide.name"));
        gasTankDesc.add(GCCoreUtil.translate("gui.gasTank.desc.0"));
        gasTankContents = this.tileEntity.gasTank2 != null ? this.tileEntity.gasTank2.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = FluidRegistry.getFluid("carbondioxide").getUnlocalizedName();
            if (gasname == null || gasname.equals("fluid.carbondioxide"))
            {
                gasname = GCCoreUtil.translate(gasTankContents.getFluid().getUnlocalizedName());
            }
            gasTankDesc.add("(" + gasname + ")");
        }
        else
        {
            gasTankDesc.add(" ");
        }
        gasLevel = gasTankContents != null ? gasTankContents.amount : 0;
        gasCapacity = this.tileEntity.gasTank2 != null ? this.tileEntity.gasTank2.getCapacity() : 0;
        gasTankDesc.add(EnumColor.YELLOW + " " + gasLevel + " / " + gasCapacity);
        this.gasTank2Region.tooltipStrings = gasTankDesc;

        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.gasTank.desc.0"));
        gasTankContents = this.tileEntity.liquidTank != null ? this.tileEntity.liquidTank.getFluid() : null;
        if (gasTankContents != null)
        {
            String gasname = FluidRegistry.getFluid("methane").getUnlocalizedName();
            if (gasname == null || gasname.equals("fluid.methane"))
            {
                gasname = GCCoreUtil.translate(gasTankContents.getFluid().getUnlocalizedName());
            }
            fuelTankDesc.add("(" + gasname + ")");
        }
        else
        {
            fuelTankDesc.add(" ");
        }
        int fuelLevel = gasTankContents != null ? gasTankContents.amount : 0;
        int fuelCapacity = this.tileEntity.liquidTank != null ? this.tileEntity.liquidTank.getCapacity() : 0;
        fuelTankDesc.add(EnumColor.YELLOW + " " + fuelLevel + " / " + fuelCapacity);
        this.fuelTankRegion.tooltipStrings = fuelTankDesc;

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.tileEntity.getEnergyStoredGC()) + " / " + (int) Math.floor(this.tileEntity.getMaxEnergyStoredGC())));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.tileEntity.getEnergyStoredGC(), this.tileEntity.getMaxEnergyStoredGC(), electricityDesc);
        this.electricInfoRegion.tooltipStrings = electricityDesc;
    }
}
