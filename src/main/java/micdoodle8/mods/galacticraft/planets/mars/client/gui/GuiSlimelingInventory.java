package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiSlimelingInventory extends GuiContainer
{
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/gui/slimelingPanel2.png");
    private final EntitySlimeling slimeling;

    public static RenderItem drawItems = new RenderItem();

    private int invX;
    private int invY;
    private final int invWidth = 18;
    private final int invHeight = 18;

    public GuiSlimelingInventory(EntityPlayer player, EntitySlimeling slimeling)
    {
        super(new ContainerSlimeling(player.inventory, slimeling));
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
    protected void mouseClicked(int px, int py, int par3)
    {
        if (px >= this.invX && px < this.invX + this.invWidth && py >= this.invY && py < this.invY + this.invHeight)
        {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
            this.mc.displayGuiScreen(new GuiSlimeling(this.slimeling));
        }

        super.mouseClicked(px, py, par3);
    }

    public static void drawSlimelingOnGui(GuiSlimelingInventory screen, EntitySlimeling slimeling, int par1, int par2, int par3, float par4, float par5)
    {
        GuiSlimeling.renderingOnGui = true;
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef(-par3 / 2.0F, par3 / 2.0F, par3 / 2.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = slimeling.renderYawOffset;
        float f3 = slimeling.rotationYaw;
        float f4 = slimeling.rotationPitch;
        par4 += 40;
        par5 -= 20;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float) Math.atan(par5 / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        slimeling.renderYawOffset = (float) Math.atan(par4 / 40.0F) * 20.0F;
        slimeling.rotationYaw = (float) Math.atan(par4 / 40.0F) * 40.0F;
        slimeling.rotationPitch = -((float) Math.atan(par5 / 40.0F)) * 20.0F;
        slimeling.rotationYawHead = slimeling.rotationYaw;
        GL11.glTranslatef(0.0F, slimeling.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(slimeling, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        slimeling.renderYawOffset = f2;
        slimeling.rotationYaw = f3;
        slimeling.rotationPitch = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GuiSlimeling.renderingOnGui = false;
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        GL11.glPushMatrix();
        super.drawScreen(par1, par2, par3);
        GL11.glPopMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;

        GL11.glPushMatrix();
        Gui.drawRect(var5, var6, var5 + this.xSize, var6 + this.ySize, 0xFF000000);
        GL11.glPopMatrix();

        int yOffset = (int) Math.floor(30.0D * (1.0F - this.slimeling.getScale()));

        GuiSlimelingInventory.drawSlimelingOnGui(this, this.slimeling, this.width / 2, var6 + 62 - yOffset, 70, var5 + 51 - i, var6 + 75 - 50 - j);


        GL11.glTranslatef(0, 0, 100);

        GL11.glPushMatrix();
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
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
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

        GL11.glPopMatrix();
    }
}
