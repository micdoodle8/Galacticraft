package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiFuelLoader extends GuiContainerGC<ContainerFuelLoader>
{
    private static final ResourceLocation fuelLoaderTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/fuel_loader.png");

    private final TileEntityFuelLoader fuelLoader;

    private Button buttonLoadFuel;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 65, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiFuelLoader(ContainerFuelLoader container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerFuelLoader(playerInv, fuelLoader), playerInv, new TranslationTextComponent("container.fuel_loader"));
        this.fuelLoader = container.getFuelLoader();
        this.ySize = 180;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.2"));
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 33, 16, 38, fuelTankDesc, this.width, this.height, this));
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 50, (this.height - this.ySize) / 2 + 54, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.fuelLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.fuelLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 65;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        this.buttons.add(this.buttonLoadFuel = new Button(this.width / 2 + 2, this.height / 2 - 49, 76, 20, GCCoreUtil.translate("gui.button.loadfuel"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.fuelLoader.getWorld()), new Object[]{this.fuelLoader.getPos(), 0}));
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 60, 10, 4210752);
        this.buttonLoadFuel.active = this.fuelLoader.disableCooldown == 0 && this.fuelLoader.fuelTank.getFluid() != FluidStack.EMPTY && this.fuelLoader.fuelTank.getFluid().getAmount() > 0;
        this.buttonLoadFuel.setMessage(!this.fuelLoader.getDisabled(0) ? GCCoreUtil.translate("gui.button.stoploading") : GCCoreUtil.translate("gui.button.loadfuel"));
        this.font.drawString(GCCoreUtil.translate("gui.message.status") + ": " + this.getStatus(), 28, 45 + 23 - 46, 4210752);
        //this.font.drawString("" + this.fuelLoader.storage.getMaxExtract(), 28, 56 + 23 - 46, 4210752);
        //		this.font.drawString(ElectricityDisplay.getDisplay(this.fuelLoader.getVoltage(), ElectricUnit.VOLTAGE), 28, 68 + 23 - 46, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 118 + 2 + 11, 4210752);
    }

    private String getStatus()
    {
        if (this.fuelLoader.fuelTank.getFluid() == FluidStack.EMPTY || this.fuelLoader.fuelTank.getFluid().getAmount() == 0)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.nofuel");
        }

        return this.fuelLoader.getGUIstatus();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiFuelLoader.fuelLoaderTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6 + 5, 0, 0, this.xSize, 181);

        final int fuelLevel = this.fuelLoader.getScaledFuelLevel(38);
        this.blit((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 17 + 54 - fuelLevel, 176, 38 - fuelLevel, 16, fuelLevel);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.fuelLoader.getEnergyStoredGC(), this.fuelLoader.getMaxEnergyStoredGC(), electricityDesc);
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.fuelLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.fuelLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.fuelLoader.getEnergyStoredGC() > 0)
        {
            this.blit(var5 + 99, var6 + 65, 192, 7, 11, 10);
        }

        this.blit(var5 + 113, var6 + 66, 192, 0, Math.min(this.fuelLoader.getScaledElecticalLevel(54), 54), 7);
    }
}
