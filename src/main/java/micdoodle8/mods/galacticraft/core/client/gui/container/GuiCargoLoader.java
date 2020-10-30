package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoBase;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoBase.ContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiCargoLoader extends GuiContainerGC<ContainerCargoLoader>
{
    public static final ResourceLocation loaderTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/cargo_loader.png");

    private final TileEntityCargoLoader cargoLoader;

    private Button buttonLoadItems;
    private final GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 107, (this.height - this.ySize) / 2 + 101, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiCargoLoader(ContainerCargoLoader container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.cargoLoader = (TileEntityCargoLoader) container.getCargoTile();
        this.ySize = 201;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.cargoLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 107;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 101;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        this.buttons.add(this.buttonLoadItems = new Button(this.width / 2 - 1, this.height / 2 - 23, 76, 20, GCCoreUtil.translate("gui.button.loaditems"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionType(this.cargoLoader.getWorld()), new Object[]{this.cargoLoader.getPos(), 0}));
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        int offsetX = -17;
        int offsetY = 45;
        this.font.drawString(this.title.getFormattedText(), 60, 12, 4210752);
        this.buttonLoadItems.active = this.cargoLoader.disableCooldown == 0;
        this.buttonLoadItems.setMessage(!this.cargoLoader.getDisabled(0) ? GCCoreUtil.translate("gui.button.stoploading") : GCCoreUtil.translate("gui.button.loaditems"));
        this.font.drawString(GCCoreUtil.translate("gui.message.status") + ": " + this.getStatus(), 28 + offsetX, 45 + 23 - 46 + offsetY, 4210752);
        //this.font.drawString("" + this.cargoLoader.storage.getMaxExtract(), 28 + offsetX, 56 + 23 - 46 + offsetY, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 90, 4210752);
    }

    private String getStatus()
    {
        if (this.cargoLoader.outOfItems)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.noitems");
        }

        if (this.cargoLoader.noTarget)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.notargetload");
        }

        if (this.cargoLoader.targetNoInventory)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.noinvtarget");
        }

        if (this.cargoLoader.targetFull)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.targetfull");
        }

        return this.cargoLoader.getGUIstatus();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiCargoLoader.loaderTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        EnergyDisplayHelper.getEnergyDisplayTooltip(this.cargoLoader.getEnergyStoredGC(), this.cargoLoader.getMaxEnergyStoredGC(), electricityDesc);
//		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ((int) Math.floor(this.cargoLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.cargoLoader.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;

        if (this.cargoLoader.getEnergyStoredGC() > 0)
        {
            this.blit(var5 + 94, var6 + 101, 176, 0, 11, 10);
        }

        this.blit(var5 + 108, var6 + 102, 187, 0, Math.min(this.cargoLoader.getScaledElecticalLevel(54), 54), 7);
    }
}
