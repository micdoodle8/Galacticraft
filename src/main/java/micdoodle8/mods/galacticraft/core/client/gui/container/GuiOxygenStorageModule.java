package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiOxygenStorageModule extends GuiContainerGC
{
    private static final ResourceLocation batteryBoxTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/oxygen_storage_module.png");

    private TileEntityOxygenStorageModule tileEntity;

    public GuiOxygenStorageModule(InventoryPlayer par1InventoryPlayer, TileEntityOxygenStorageModule storageModule)
    {
        super(new ContainerOxygenStorageModule(par1InventoryPlayer, storageModule));
        this.tileEntity = storageModule;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> oxygenSlotDesc = new ArrayList<String>();
        oxygenSlotDesc.add(GCCoreUtil.translate("gui.oxygen_slot.desc.0"));
        oxygenSlotDesc.add(GCCoreUtil.translate("gui.oxygen_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 16, (this.height - this.ySize) / 2 + 21, 18, 18, oxygenSlotDesc, this.width, this.height, this));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String guiTitle = GCCoreUtil.translate("tile.machine2.6.name");
        this.fontRenderer.drawString(guiTitle, this.xSize / 2 - this.fontRenderer.getStringWidth(guiTitle) / 2, 6, 4210752);
        String displayJoules = (int) (this.tileEntity.getOxygenStored() + 0.5F) + " " + GCCoreUtil.translate("gui.message.of.name");
        String displayMaxJoules = "" + (int) this.tileEntity.getMaxOxygenStored();
        String maxOutputLabel = GCCoreUtil.translate("gui.max_output.desc") + ": " + TileEntityOxygenStorageModule.OUTPUT_PER_TICK * 20 + GCCoreUtil.translate("gui.per_second");

        this.fontRenderer.drawString(displayJoules, 122 - this.fontRenderer.getStringWidth(displayJoules) / 2 - 35, 30, 4210752);
        this.fontRenderer.drawString(displayMaxJoules, 122 - this.fontRenderer.getStringWidth(displayMaxJoules) / 2 - 35, 40, 4210752);
        this.fontRenderer.drawString(maxOutputLabel, 122 - this.fontRenderer.getStringWidth(maxOutputLabel) / 2 - 35, 60, 4210752);
        this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.renderEngine.bindTexture(GuiOxygenStorageModule.batteryBoxTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (this.width - this.xSize) / 2;
        int containerHeight = (this.height - this.ySize) / 2;
        // Background energy bar
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        // Foreground energy bar
        int scale = (int) ((double) this.tileEntity.getOxygenStored() / (double) this.tileEntity.getMaxOxygenStored() * 72);
        this.drawTexturedModalRect(containerWidth + 52, containerHeight + 52, 176, 0, scale, 3);
    }
}
