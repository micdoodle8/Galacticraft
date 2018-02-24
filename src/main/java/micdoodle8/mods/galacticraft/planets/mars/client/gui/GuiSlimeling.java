package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiSlimeling extends GuiScreen
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/slimeling_panel0.png");
    private final EntitySlimeling slimeling;

    public GuiTextField nameField;
    public GuiButton stayButton;

    public static boolean renderingOnGui = false;

    private int invX;
    private int invY;
    private final int invWidth = 18;
    private final int invHeight = 18;

    public GuiSlimeling(EntitySlimeling slimeling)
    {
        this.slimeling = slimeling;
        this.xSize = 176;
        this.ySize = 147;
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.stayButton = new GuiButton(0, i + 120, j + 122, 50, 20, "");
        this.stayButton.enabled = this.slimeling.isOwner(this.mc.thePlayer);
        this.stayButton.displayString = this.slimeling.isSitting() ? GCCoreUtil.translate("gui.slimeling.button.follow") : GCCoreUtil.translate("gui.slimeling.button.sit");
        this.buttonList.add(this.stayButton);
        this.nameField = new GuiTextField(0, this.fontRendererObj, i + 44, j + 59, 103, 12);
        this.nameField.setText(this.slimeling.getName());
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(30);
        this.nameField.setFocused(this.slimeling.isOwner(this.mc.thePlayer));
        this.nameField.setCanLoseFocus(false);
        this.invX = i + 151;
        this.invY = j + 76;
    }

    @Override
    public void updateScreen()
    {
        if (this.slimeling.isOwner(this.mc.thePlayer))
        {
            this.nameField.updateCursorCounter();
        }
        this.stayButton.displayString = this.slimeling.isSitting() ? GCCoreUtil.translate("gui.slimeling.button.follow") : GCCoreUtil.translate("gui.slimeling.button.sit");
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.slimeling.isOwner(this.mc.thePlayer))
        {
            if (this.nameField.textboxKeyTyped(typedChar, keyCode))
            {
                this.slimeling.setName(this.nameField.getText());
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionID(this.slimeling.worldObj), new Object[] { this.slimeling.getEntityId(), 1, this.slimeling.getName() }));
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            switch (button.id)
            {
            case 0:
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionID(this.slimeling.worldObj), new Object[] { this.slimeling.getEntityId(), 0, "" }));
                break;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (this.slimeling.isOwner(this.mc.thePlayer))
        {
            this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (mouseX >= this.invX && mouseX < this.invX + this.invWidth && mouseY >= this.invY && mouseY < this.invY + this.invHeight)
        {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_SLIMELING_DATA, GCCoreUtil.getDimensionID(this.slimeling.worldObj), new Object[] { this.slimeling.getEntityId(), 6, "" }));
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -70.0F);
        Gui.drawRect(i, j, i + this.xSize, j + this.ySize - 20, 0xFF000000);
        GlStateManager.popMatrix();

        int yOffset = (int) Math.floor(30.0D * (1.0F - this.slimeling.getScale()));

        GuiSlimeling.drawSlimelingOnGui(this.slimeling, this.width / 2, j + 62 - yOffset, 70, i + 51 - mouseX, j + 75 - 50 - mouseY);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 150.0F);
        this.mc.renderEngine.bindTexture(GuiSlimeling.slimelingPanelGui);
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(i + this.xSize - 15, j + 9, 176, 0, 9, 9);
        this.drawTexturedModalRect(i + this.xSize - 15, j + 22, 185, 0, 9, 9);
        this.drawTexturedModalRect(i + this.xSize - 15, j + 35, 194, 0, 9, 9);
        String str = "" + Math.round(this.slimeling.getColorRed() * 1000) / 10.0F + "% ";
        this.drawString(this.fontRendererObj, str, i + this.xSize - 15 - this.fontRendererObj.getStringWidth(str), j + 10, ColorUtil.to32BitColor(255, 255, 0, 0));
        str = "" + Math.round(this.slimeling.getColorGreen() * 1000) / 10.0F + "% ";
        this.drawString(this.fontRendererObj, str, i + this.xSize - 15 - this.fontRendererObj.getStringWidth(str), j + 23, ColorUtil.to32BitColor(255, 0, 255, 0));
        str = "" + Math.round(this.slimeling.getColorBlue() * 1000) / 10.0F + "% ";
        this.drawString(this.fontRendererObj, str, i + this.xSize - 15 - this.fontRendererObj.getStringWidth(str), j + 36, ColorUtil.to32BitColor(255, 0, 0, 255));

        this.mc.renderEngine.bindTexture(GuiSlimeling.slimelingPanelGui);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.invX, this.invY, 176, 9, this.invWidth, this.invHeight);

        super.drawScreen(mouseX, mouseY, partialTicks);

        int dX = -45;
        int dY = 65;
        int startX = -20 + i + 60;
        int startY = dY + j - 10;
        int width = this.xSize - 60;
        int height = 15;
        Gui.drawRect(startX, startY, startX + width, startY + height, 0xffA0A0A0);
        Gui.drawRect(startX + 1, startY + 1, startX + width - 1, startY + height - 1, 0xFF000000);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.slimeling.name") + ": ", dX + i + 55, dY + j - 6, 0x404040);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.slimeling.owner") + ": " + this.slimeling.getOwnerUsername(), dX + i + 55, dY + j + 7, 0x404040);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.slimeling.kills") + ": " + this.slimeling.getKillCount(), dX + i + 55, dY + j + 20, 0x404040);
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.slimeling.scale") + ": " + Math.round(this.slimeling.getAge() / (float) this.slimeling.MAX_AGE * 1000.0F) / 10.0F + "%", dX + i + 55, dY + j + 33, 0x404040);
        str = "" + (this.slimeling.isSitting() ? GCCoreUtil.translate("gui.slimeling.sitting") : GCCoreUtil.translate("gui.slimeling.following"));
        this.fontRendererObj.drawString(str, i + 145 - this.fontRendererObj.getStringWidth(str) / 2, j + 112, 0x404040);
        str = GCCoreUtil.translate("gui.slimeling.damage") + ": " + Math.round(this.slimeling.getDamage() * 100.0F) / 100.0F;
        this.fontRendererObj.drawString(str, dX + i + 55, dY + j + 33 + 13, 0x404040);
        str = GCCoreUtil.translate("gui.slimeling.food") + ": ";
        this.fontRendererObj.drawString(str, dX + i + 55, dY + j + 46 + 13, 0x404040);

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(this.slimeling.getFavoriteFood()), dX + i + 55 + this.fontRendererObj.getStringWidth(str), dY + j + 41 + 14);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
        GlStateManager.popMatrix();
        ItemStack foodStack = new ItemStack(this.slimeling.getFavoriteFood());

        if (foodStack != null && mouseX >= this.invX - 66 && mouseX < this.invX + this.invWidth - 68 && mouseY >= this.invY + 44 && mouseY < this.invY + this.invHeight + 42)
        {
            this.renderToolTip(foodStack, mouseX, mouseY);
        }
    }

    public static void drawSlimelingOnGui(EntitySlimeling slimeling, int x, int y, int scale, float mouseX, float mouseY)
    {
        GuiSlimeling.renderingOnGui = true;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50.0F);
        GlStateManager.scale(-scale / 2.0F, scale / 2.0F, scale / 2.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = slimeling.renderYawOffset;
        float f3 = slimeling.rotationYaw;
        float f4 = slimeling.rotationPitch;
        mouseX += 40;
        mouseY -= 20;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        slimeling.renderYawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
        slimeling.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
        slimeling.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
        slimeling.rotationYawHead = slimeling.rotationYaw;
        GlStateManager.translate(0.0F, (float) slimeling.getYOffset(), 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(slimeling, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        slimeling.renderYawOffset = f2;
        slimeling.rotationYaw = f3;
        slimeling.rotationPitch = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        GuiSlimeling.renderingOnGui = false;
    }
}
