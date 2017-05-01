package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.io.IOException;

public class GuiSlimelingInventory extends GuiContainer
{
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/slimeling_panel2.png");
    private final EntitySlimeling slimeling;

    private int invX;
    private int invY;
    private final int invWidth = 18;
    private final int invHeight = 18;

    public GuiSlimelingInventory(EntityPlayer player, EntitySlimeling slimeling)
    {
        super(new ContainerSlimeling(player.inventory, slimeling, FMLClientHandler.instance().getClient().thePlayer));
        this.slimeling = slimeling;
        this.xSize = 176;
        this.ySize = 210;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.invX = var5 + 151;
        this.invY = var6 + 108;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                break;
            }
        }
    }

    @Override
    protected void mouseClicked(int px, int py, int par3) throws IOException
    {
        if (px >= this.invX && px < this.invX + this.invWidth && py >= this.invY && py < this.invY + this.invHeight)
        {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            this.mc.displayGuiScreen(new GuiSlimeling(this.slimeling));
        }

        super.mouseClicked(px, py, par3);
    }

    @Override
    public void drawDefaultBackground()
    {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;

        GlStateManager.pushMatrix();
        Gui.drawRect(var5, var6, var5 + this.xSize, var6 + this.ySize, 0xFF000000);
        GlStateManager.popMatrix();

        int yOffset = (int) Math.floor(30.0D * (1.0F - this.slimeling.getScale()));

        GuiSlimeling.drawSlimelingOnGui(this.slimeling, this.width / 2, var6 + 62 - yOffset, 70, var5 + 51 - i, var6 + 75 - 50 - j);


        GlStateManager.translate(0, 0, 100);

        GlStateManager.pushMatrix();
        this.mc.renderEngine.bindTexture(GuiSlimelingInventory.slimelingPanelGui);
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(var5 + this.xSize - 15, var6 + 9, 176, 0, 9, 9);
        this.drawTexturedModalRect(var5 + this.xSize - 15, var6 + 22, 185, 0, 9, 9);
        this.drawTexturedModalRect(var5 + this.xSize - 15, var6 + 35, 194, 0, 9, 9);
        String str = "" + Math.round(this.slimeling.getColorRed() * 1000) / 10.0F + "% ";
        this.drawString(this.fontRendererObj, str, var5 + this.xSize - 15 - this.fontRendererObj.getStringWidth(str), var6 + 10, ColorUtil.to32BitColor(255, 255, 0, 0));
        str = "" + Math.round(this.slimeling.getColorGreen() * 1000) / 10.0F + "% ";
        this.drawString(this.fontRendererObj, str, var5 + this.xSize - 15 - this.fontRendererObj.getStringWidth(str), var6 + 23, ColorUtil.to32BitColor(255, 0, 255, 0));
        str = "" + Math.round(this.slimeling.getColorBlue() * 1000) / 10.0F + "% ";
        this.drawString(this.fontRendererObj, str, var5 + this.xSize - 15 - this.fontRendererObj.getStringWidth(str), var6 + 36, ColorUtil.to32BitColor(255, 0, 0, 255));

        this.mc.renderEngine.bindTexture(GuiSlimelingInventory.slimelingPanelGui);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.invX, this.invY, 176, 27, this.invWidth, this.invHeight);
        this.drawTexturedModalRect(var5 + 8, var6 + 8, 176, 9, 18, 18);
        this.drawTexturedModalRect(var5 + 8, var6 + 29, 176, 9, 18, 18);

        ItemStack stack = this.slimeling.getCargoSlot();

        if (stack != null && stack.getItem() == MarsItems.marsItemBasic && stack.getItemDamage() == 4)
        {
            int offsetX = 7;
            int offsetY = 53;

            for (int y = 0; y < 3; ++y)
            {
                for (int x = 0; x < 9; ++x)
                {
                    this.drawTexturedModalRect(var5 + offsetX + x * 18, var6 + offsetY + y * 18, 176, 9, 18, 18);
                }
            }
        }

        GlStateManager.popMatrix();
    }
}
