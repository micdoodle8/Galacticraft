package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiPositionedContainer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSchematicCargoRocket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiSchematicCargoRocket extends GuiPositionedContainer implements ISchematicResultPage
{
    private static final ResourceLocation cargoRocketTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_cargo.png");

    private int pageIndex;

    public GuiSchematicCargoRocket(InventoryPlayer par1InventoryPlayer, BlockPos pos)
    {
        super(new ContainerSchematicCargoRocket(par1InventoryPlayer, pos), pos);
        this.ySize = 220;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back.name")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next.name")));
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
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(GCCoreUtil.translate("item.rocket_t2.cargo_rocket.name"), 7, -20 + 27, 4210752);
        this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 220 - 104 + 2 + 9, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiSchematicCargoRocket.cargoRocketTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void setPageIndex(int index)
    {
        this.pageIndex = index;
    }
}
