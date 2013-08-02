package micdoodle8.mods.galacticraft.mars.client.gui;

import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GCMarsGuiSlimeling extends GuiScreen
{
    private final int xSize;
    private final int ySize;
    private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/gui/slimelingPanel0.png");
    private final GCMarsEntitySlimeling slimeling;

    public static RenderItem drawItems = new RenderItem();
    
    public long timeBackspacePressed;
    public int cursorPulse;
    public int backspacePressed;
    public boolean isTextFocused = false;
    
    public GCMarsGuiSlimeling(GCMarsEntitySlimeling slimeling)
    {
        this.slimeling = slimeling;
        this.xSize = 176;
        this.ySize = 147;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(0, var5 + 120, var6 + 102, 50, 20, "Stay"));
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void keyTyped(char keyChar, int keyID)
    {
        if (!this.isTextFocused)
        {
            super.keyTyped(keyChar, keyID);
            return;
        }

        if (keyID == Keyboard.KEY_BACK)
        {
            if (slimeling.getName().length() > 0)
            {
                slimeling.setName(slimeling.getName().substring(0, slimeling.getName().length() - 1));
                this.timeBackspacePressed = System.currentTimeMillis();
            }
        }
        else if (keyChar == 22)
        {
            String pastestring = GuiScreen.getClipboardString();

            if (pastestring == null)
            {
                pastestring = "";
            }

            if (this.isValid(slimeling.getName() + pastestring))
            {
                slimeling.setName(slimeling.getName() + pastestring);
                slimeling.setName(slimeling.getName().substring(0, Math.min(slimeling.getName().length(), 16)));
            }
        }
        else if (this.isValid(slimeling.getName() + keyChar))
        {
            slimeling.setName(slimeling.getName() + keyChar);
            slimeling.setName(slimeling.getName().substring(0, Math.min(slimeling.getName().length(), 16)));
        }
        
        PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { this.slimeling.entityId, 1, this.slimeling.getName() }));
        
        super.keyTyped(keyChar, keyID);
    }

    public boolean isValid(String string)
    {
        return ChatAllowedCharacters.allowedCharacters.indexOf(string.charAt(string.length() - 1)) >= 0;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
            case 0:
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { this.slimeling.entityId, 0, "" }));
                break;
            }
        }
    }

    @Override
    protected void mouseClicked(int px, int py, int par3)
    {
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        final int startX = var5 + 55;
        final int startY = var6 - 15;
        final int width = this.xSize - 70;
        final int height = 20;

        if (px >= startX && px < startX + width && py >= startY && py < startY + height)
        {
            Gui.drawRect(startX, startY, startX + width, startY + height, 0xffA0A0A0);
            this.isTextFocused = true;
        }
        else
        {
            this.isTextFocused = false;
        }

        super.mouseClicked(px, py, par3);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.mc.renderEngine.func_110577_a(slimelingPanelGui);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 - 20, 0, 0, this.xSize, this.ySize);

        super.drawScreen(par1, par2, par3);
        
        drawSlimelingOnGui(this.slimeling, var5 + 25, var6 + 15, 34, var5 + 51 - par1, var6 + 75 - 50 - par2);

        this.cursorPulse++;
        
        if (this.timeBackspacePressed > 0)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && this.slimeling.getName().length() > 0)
            {
                if (System.currentTimeMillis() - this.timeBackspacePressed > 200 / (1 + this.backspacePressed * 0.3F))
                {
                    this.slimeling.setName(this.slimeling.getName().substring(0, this.slimeling.getName().length() - 1));
                    PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { this.slimeling.entityId, 1, this.slimeling.getName() }));
                    this.timeBackspacePressed = System.currentTimeMillis();
                    this.backspacePressed++;
                }
            }
            else
            {
                this.timeBackspacePressed = 0;
                this.backspacePressed = 0;
            }
        }

        final int startX = var5 + 55;
        final int startY = var6 - 15;
        final int width = this.xSize - 70;
        final int height = 18;
        Gui.drawRect(startX, startY, startX + width, startY + height, 0xffA0A0A0);
        Gui.drawRect(startX + 1, startY + 1, startX + width - 1, startY + height - 1, 0xFF000000);
        this.drawString(this.fontRenderer, this.slimeling.getName() + (this.cursorPulse / 24 % 2 == 0 && this.isTextFocused ? "_" : ""), startX + 4, startY + 5, 0xe0e0e0);
        
        this.fontRenderer.drawString("Kills: 0", var5 + 55, var6 + 7, 0x404040);
        this.fontRenderer.drawString("Scale: " + (Math.round((this.slimeling.getAge() / (float)this.slimeling.MAX_AGE) * 1000.0F) / 10.0F) + "%", var5 + 55, var6 + 20, 0x404040);
        String str = "" + (this.slimeling.isSitting() ? "Sitting" : "Following");
        this.fontRenderer.drawString(str, var5 + 145 - this.fontRenderer.getStringWidth(str) / 2, var6 + 92, 0x404040);
        FMLLog.info("" + (slimeling.getDamage()));
        str = "Attack Damage: " + Math.round(slimeling.getDamage() * 100.0F) / 100.0F;
        this.fontRenderer.drawString(str, var5 + 55, var6 + 33, 0x404040);
        str = "Favorite Food: ";
        this.fontRenderer.drawString(str, var5 + 55, var6 + 46, 0x404040);

        GCMarsGuiSlimeling.drawItems.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(Item.itemsList[this.slimeling.getFavoriteFood()]), var5 + 55 + this.fontRenderer.getStringWidth(str), var6 + 41);
    }

    public static void drawSlimelingOnGui(GCMarsEntitySlimeling slimeling, int par1, int par2, int par3, float par4, float par5)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(par1, par2, 50.0F);
        GL11.glScalef(-par3 / 2.0F, par3 / 2.0F, par3 / 2.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = slimeling.renderYawOffset;
        float f3 = slimeling.rotationYaw;
        float f4 = slimeling.rotationPitch;
        par4 -= 19;
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
    }
}
