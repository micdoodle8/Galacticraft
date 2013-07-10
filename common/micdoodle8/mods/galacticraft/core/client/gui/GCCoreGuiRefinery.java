package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiRefinery extends GCCoreGuiContainer
{
    private static final ResourceLocation refineryTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/refinery.png");

    private final GCCoreTileEntityRefinery tileEntity;

    private GuiButton buttonDisable;

    private int containerWidth;
    private int containerHeight;

    public GCCoreGuiRefinery(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityRefinery tileEntity)
    {
        super(new GCCoreContainerRefinery(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        this.ySize += 20;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> oilTankDesc = new ArrayList<String>();
        oilTankDesc.add("The refinery oil tank");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 28, 16, 38, oilTankDesc, this.width, this.height));
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add("Refinery battery slot, place battery here");
        batterySlotDesc.add("if not using a connected power source");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 49, (this.height - this.ySize) / 2 + 68, 18, 18, batterySlotDesc, this.width, this.height));
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add("The refinery fuel tank");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 28, 16, 38, fuelTankDesc, this.width, this.height));
        List<String> oilSlotDesc = new ArrayList<String>();
        oilSlotDesc.add("Refinery oil input. Place oil canisters" + (GCCoreCompatibilityManager.isBCraftLoaded() ? " or oil buckets" : ""));
        oilSlotDesc.add("into this slot to load it into the oil tank.");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 6, (this.height - this.ySize) / 2 + 6, 18, 18, oilSlotDesc, this.width, this.height));
        List<String> fuelSlotDesc = new ArrayList<String>();
        fuelSlotDesc.add("Refinery fuel output. Place empty liquid");
        fuelSlotDesc.add("canisters into this slot to fill them");
        fuelSlotDesc.add("from the fuel tank.");
        this.infoRegions.add(new GCCoreInfoRegion((this.width - this.xSize) / 2 + 152, (this.height - this.ySize) / 2 + 6, 18, 18, fuelSlotDesc, this.width, this.height));
        this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 38, this.height / 2 - 49, 76, 20, LanguageRegistry.instance().getStringLocalization("gui.button.refine.name")));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] { this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord }));
            break;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 68, 5, 4210752);
        String displayText = "";

        if (this.tileEntity.oilTank.getFluid() == null || this.tileEntity.oilTank.getFluidAmount() == 0)
        {
            displayText = EnumColor.RED + LanguageRegistry.instance().getStringLocalization("gui.status.nooil.name");
        }
        else if (this.tileEntity.oilTank.getFluidAmount() > 0 && this.tileEntity.disabled)
        {
            displayText = EnumColor.ORANGE + LanguageRegistry.instance().getStringLocalization("gui.status.ready.name");
        }
        else if (this.tileEntity.ueWattsReceived == 0 && this.tileEntity.ic2Energy == 0 && this.tileEntity.bcEnergy == 0)
        {
            displayText = EnumColor.ORANGE + LanguageRegistry.instance().getStringLocalization("gui.status.idle.name");
        }

        else if (this.tileEntity.processTicks > 0)
        {
            displayText = EnumColor.BRIGHT_GREEN + LanguageRegistry.instance().getStringLocalization("gui.status.refining.name");
        }
        else
        {
            displayText = EnumColor.RED + LanguageRegistry.instance().getStringLocalization("gui.status.unknown.name");
        }

        this.buttonDisable.enabled = this.tileEntity.disableCooldown == 0;
        this.buttonDisable.displayString = this.tileEntity.processTicks == 0 ? LanguageRegistry.instance().getStringLocalization("gui.button.refine.name") : LanguageRegistry.instance().getStringLocalization("gui.button.stoprefine.name");
        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.status.name") + ": " + displayText, 72, 45 + 23, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileEntity.ueWattsPerTick * 20, ElectricUnit.WATT), 72, 56 + 23, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 72, 68 + 23, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 118 + 2 + 23, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.func_110577_a(GCCoreGuiRefinery.refineryTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);

        if (this.tileEntity.processTicks > 0)
        {
            final int scale = (int) ((double) this.tileEntity.processTicks / (double) GCCoreTileEntityRefinery.PROCESS_TIME_REQUIRED * 124);
            this.drawTexturedModalRect(this.containerWidth + 26, this.containerHeight + 24, 0, 186, 124 - scale, 20);
        }

        int displayInt = this.tileEntity.getScaledOilLevel(38);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 17 + 49 - displayInt, 176, 38 - displayInt, 16, displayInt);

        displayInt = this.tileEntity.getScaledFuelLevel(38);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 153, (this.height - this.ySize) / 2 + 17 + 49 - displayInt, 176 + 16, 38 - displayInt, 16, displayInt);
    }
}
