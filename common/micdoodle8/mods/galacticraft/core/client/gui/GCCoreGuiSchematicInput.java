package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.API.ISchematicResultPage;
import micdoodle8.mods.galacticraft.API.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerSchematic;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GCCoreGuiSchematicInput extends GuiContainer implements ISchematicResultPage
{
    private GuiButton backButton;
    private GuiButton nextButton;
    private GuiButton unlockButton;
    private int pageIndex;

    public GCCoreGuiSchematicInput(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
    {
        super(new GCCoreContainerSchematic(par1InventoryPlayer, x, y, z));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(this.backButton = new GuiButton(0, this.width / 2 - 130, this.height / 2 - 30 + 27, 40, 20, "Back"));
        this.buttonList.add(this.nextButton = new GuiButton(1, this.width / 2 + 90, this.height / 2 - 30 + 27, 40, 20, "Next"));
        this.buttonList.add(this.unlockButton = new GuiButton(2, this.width / 2 - 46, this.height / 2 - 52, 92, 20, "Unlock Schematic"));
        this.nextButton.enabled = false;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                SchematicRegistry.flipToLastPage(this.pageIndex);
                break;
            case 1:
                SchematicRegistry.flipToNextPage(this.pageIndex);
                break;
            case 2:
                final Object[] toSend2 =
                { 0 };
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 16, toSend2));
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("Add New Schematic", 7, -22, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 56, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/galacticraftcore/textures/gui/schematicpage.png");
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - 220) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, 220);
    }

    @Override
    public void setPageIndex(int index)
    {
        this.pageIndex = index;
    }
}
