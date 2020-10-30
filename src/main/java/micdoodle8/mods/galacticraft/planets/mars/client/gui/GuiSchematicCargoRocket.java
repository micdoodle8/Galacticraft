package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSchematicCargoRocket;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

public class GuiSchematicCargoRocket extends GuiContainerGC<ContainerSchematicCargoRocket> implements ISchematicResultPage
{
    private static final ResourceLocation cargoRocketTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_cargo.png");

    private int pageIndex;

    public GuiSchematicCargoRocket(ContainerSchematicCargoRocket container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.ySize = 220;
    }

    @Override
    public void init()
    {
        super.init();
        this.buttons.clear();
        this.buttons.add(new Button(this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back"), (button) ->
        {
            SchematicRegistry.flipToPrevPage(this, this.pageIndex);
        }));
        this.buttons.add(new Button(this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next"), (button) ->
        {
            SchematicRegistry.flipToNextPage(this, this.pageIndex);
        }));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(GCCoreUtil.translate("item.rocket_t2.cargo_rocket"), 7, -20 + 27, 4210752);
        this.font.drawString(GCCoreUtil.translate("container.inventory"), 8, 220 - 104 + 2 + 9, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(GuiSchematicCargoRocket.cargoRocketTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void setPageIndex(int index)
    {
        this.pageIndex = index;
    }
}
