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
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiSchematicInput extends GuiContainerGC<ContainerSchematic> implements ISchematicResultPage
{
    private static final ResourceLocation schematicInputTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/schematicpage.png");

    private int pageIndex;

    public GuiSchematicInput(ContainerSchematic container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerSchematic(playerInv, pos), playerInv, new TranslationTextComponent("gui.message.addnewsch"), pos);
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> schematicSlotDesc = new ArrayList<String>();
        Button nextButton;
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.0"));
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.1"));
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.2"));
        schematicSlotDesc.add(GCCoreUtil.translate("gui.new_schematic.slot.desc.3"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 79, (this.height - this.ySize) / 2, 18, 18, schematicSlotDesc, this.width, this.height, this));
        this.buttons.add(new Button(this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back"), (button) ->
        {
            SchematicRegistry.flipToPrevPage(this, this.pageIndex);
        }));
        this.buttons.add(nextButton = new Button(this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next"), (button) ->
        {
            SchematicRegistry.flipToNextPage(this, this.pageIndex);
        }));
        this.buttons.add(new Button(this.width / 2 - 92 / 2, this.height / 2 - 52, 92, 20, GCCoreUtil.translate("gui.button.unlockschematic"), (button) ->
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UNLOCK_NEW_SCHEMATIC, GCCoreUtil.getDimensionType(minecraft.world), new Object[]{}));
        }));
        nextButton.active = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(GCCoreUtil.translate("gui.message.addnewsch"), 7, -22, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, 56, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(GuiSchematicInput.schematicInputTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - 220) / 2;
        this.blit(var5, var6, 0, 0, this.xSize, 220);
    }

    @Override
    public void setPageIndex(int index)
    {
        this.pageIndex = index;
    }
}
