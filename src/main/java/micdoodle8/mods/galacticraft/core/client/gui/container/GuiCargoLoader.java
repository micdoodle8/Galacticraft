package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCargoLoader extends GuiContainerGC
{
    private static final ResourceLocation loaderTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/cargo_loader.png");

    private final TileEntityCargoLoader cargoLoader;

    private GuiButton buttonLoadItems;
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiCargoLoader(InventoryPlayer par1InventoryPlayer, TileEntityCargoLoader par2TileEntityAirDistributor)
    {
        super(new ContainerCargoLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.cargoLoader = par2TileEntityAirDistributor;
        this.ySize = 201;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, new Object[] { this.cargoLoader.getPos(), 0 }));
            break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.cargoLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 107;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 101;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.batterySlot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.buttonList.add(this.buttonLoadItems = new GuiButton(0, this.width / 2 - 1, this.height / 2 - 23, 76, 20, GCCoreUtil.translate("gui.button.loaditems.name")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int offsetX = -17;
        int offsetY = 45;
        this.fontRendererObj.drawString(this.cargoLoader.getInventoryName(), 60, 12, 4210752);
        this.buttonLoadItems.enabled = this.cargoLoader.disableCooldown == 0;
        this.buttonLoadItems.displayString = !this.cargoLoader.getDisabled(0) ? GCCoreUtil.translate("gui.button.stoploading.name") : GCCoreUtil.translate("gui.button.loaditems.name");
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus(), 28 + offsetX, 45 + 23 - 46 + offsetY, 4210752);
        //this.fontRendererObj.drawString("" + this.cargoLoader.storage.getMaxExtract(), 28 + offsetX, 56 + 23 - 46 + offsetY, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 90, 4210752);
    }

    private String getStatus()
    {
        if (this.cargoLoader.outOfItems)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.noitems.name");
        }

        if (this.cargoLoader.noTarget)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.notargetload.name");
        }

        if (this.cargoLoader.targetNoInventory)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.noinvtarget.name");
        }

        if (this.cargoLoader.targetFull)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.targetfull.name");
        }

        if (this.cargoLoader.getStackInSlot(0) == null && this.cargoLoader.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower.name");
        }

        if (this.cargoLoader.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready.name");
        }

        if (this.cargoLoader.getEnergyStoredGC() > 0)
        {
            return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.active.name");
        }

        return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiCargoLoader.loaderTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energyStorage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.cargoLoader.getEnergyStoredGC(), this.cargoLoader.getMaxEnergyStoredGC(), electricityDesc);
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energyStorage.desc.1") + ((int) Math.floor(this.cargoLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.cargoLoader.getEnergyStoredGC() > 0)
        {
            this.drawTexturedModalRect(var5 + 94, var6 + 101, 176, 0, 11, 10);
        }

        this.drawTexturedModalRect(var5 + 108, var6 + 102, 187, 0, Math.min(this.cargoLoader.getScaledElecticalLevel(54), 54), 7);
    }
}
