package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematic;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiSchematicInput extends GuiPositionedContainer implements ISchematicResultPage
{
    private static final ResourceLocation schematicInputTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/schematicpage.png");

    private int pageIndex;

    public GuiSchematicInput(InventoryPlayer par1InventoryPlayer, BlockPos pos)
    {
        super(new ContainerSchematic(par1InventoryPlayer, pos), pos);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> schematicSlotDesc = new ArrayList<String>();
        GuiButton nextButton;
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.0"));
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.1"));
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.2"));
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 79, (this.height - this.ySize) / 2, 18, 18, schematicSlotDesc, this.width, this.height, this));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back.name")));
        this.buttonList.add(nextButton = new GuiButton(1, this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next.name")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 92 / 2, this.height / 2 - 52, 92, 20, GCCoreUtil.translate("gui.button.unlockschematic.name")));
        nextButton.enabled = false;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                SchematicRegistry.flipToLastPage(this, this.pageIndex);
                break;
            case 1:
                SchematicRegistry.flipToNextPage(this, this.pageIndex);
                break;
            case 2:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UNLOCK_NEW_SCHEMATIC, GCCoreUtil.getDimensionID(mc.world), new Object[] {}));
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.addnewsch.name"), 7, -22, 4210752);
        this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, 56, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiSchematicInput.schematicInputTexture);
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
