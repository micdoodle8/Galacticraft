package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

public class GuiSlimelingInventory extends GuiContainerGC<ContainerSlimeling>
{
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/slimeling_panel2.png");
    private final EntitySlimeling slimeling;

    private int invX;
    private int invY;
    private final int invWidth = 18;
    private final int invHeight = 18;

    public GuiSlimelingInventory(ContainerSlimeling container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
        this.slimeling = container.getSlimeling();
        this.xSize = 176;
        this.ySize = 210;
    }

    @Override
    public void init()
    {
        super.init();
        this.buttons.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.invX = var5 + 151;
        this.invY = var6 + 108;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public boolean mouseClicked(double px, double py, int par3)
    {
        if (px >= this.invX && px < this.invX + this.invWidth && py >= this.invY && py < this.invY + this.invHeight)
        {
            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.minecraft.displayGuiScreen(new GuiSlimeling(this.slimeling));
            return true;
        }

        return super.mouseClicked(px, py, par3);
    }

    @Override
    public void renderBackground()
    {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;

        RenderSystem.pushMatrix();
        AbstractGui.fill(var5, var6, var5 + this.xSize, var6 + this.ySize, 0xFF000000);
        RenderSystem.popMatrix();

        int yOffset = (int) Math.floor(30.0D * (1.0F - this.slimeling.getScale()));

        GuiSlimeling.drawSlimelingOnGui(this.slimeling, this.width / 2, var6 + 62 - yOffset, 70, var5 + 51 - i, var6 + 75 - 50 - j);


        RenderSystem.translatef(0, 0, 100);

        RenderSystem.pushMatrix();
        this.minecraft.textureManager.bindTexture(GuiSlimelingInventory.slimelingPanelGui);
        this.blit(var5, var6, 0, 0, this.xSize, this.ySize);
        this.blit(var5 + this.xSize - 15, var6 + 9, 176, 0, 9, 9);
        this.blit(var5 + this.xSize - 15, var6 + 22, 185, 0, 9, 9);
        this.blit(var5 + this.xSize - 15, var6 + 35, 194, 0, 9, 9);
        String str = "" + Math.round(this.slimeling.getColorRed() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, var5 + this.xSize - 15 - this.font.getStringWidth(str), var6 + 10, ColorUtil.to32BitColor(255, 255, 0, 0));
        str = "" + Math.round(this.slimeling.getColorGreen() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, var5 + this.xSize - 15 - this.font.getStringWidth(str), var6 + 23, ColorUtil.to32BitColor(255, 0, 255, 0));
        str = "" + Math.round(this.slimeling.getColorBlue() * 1000) / 10.0F + "% ";
        this.drawString(this.font, str, var5 + this.xSize - 15 - this.font.getStringWidth(str), var6 + 36, ColorUtil.to32BitColor(255, 0, 0, 255));

        this.minecraft.textureManager.bindTexture(GuiSlimelingInventory.slimelingPanelGui);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(this.invX, this.invY, 176, 27, this.invWidth, this.invHeight);
        this.blit(var5 + 8, var6 + 8, 176, 9, 18, 18);
        this.blit(var5 + 8, var6 + 29, 176, 9, 18, 18);

        ItemStack stack = this.slimeling.slimelingInventory.getStackInSlot(1);

        if (stack != null && stack.getItem() == MarsItems.slimelingCargo)
        {
            int offsetX = 7;
            int offsetY = 53;

            for (int y = 0; y < 3; ++y)
            {
                for (int x = 0; x < 9; ++x)
                {
                    this.blit(var5 + offsetX + x * 18, var6 + offsetY + y * 18, 176, 9, 18, 18);
                }
            }
        }

        RenderSystem.popMatrix();
    }
}
