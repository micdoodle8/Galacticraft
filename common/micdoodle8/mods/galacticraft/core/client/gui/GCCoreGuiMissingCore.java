package micdoodle8.mods.galacticraft.core.client.gui;

import java.net.URI;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiMissingCore extends GuiScreen
{
    private String url = "http://micdoodle8.com/mods/galacticraft/downloads";
    private int urlX;
    private int urlY;
    private int urlWidth;
    private int urlHeight;
    
    public void initGui()
    {
        super.initGui();
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        int offset = this.height / 2 - 50;
        this.drawCenteredString(this.fontRenderer, "MicdoodleCore not found in mods folder", this.width / 2, offset, 0xFF5555);
        offset += 25;
        this.drawCenteredString(this.fontRenderer, "Galacticraft failed to load", this.width / 2, offset, 0xFF5555);
        offset += 20;
        this.drawCenteredString(this.fontRenderer, "Close Minecraft and install MicdoodleCore", this.width / 2, offset, 0x999999);
        offset += 20;
        String s = EnumChatFormatting.UNDERLINE + "Click here to open download page";
        this.urlX = (this.width / 2) - (this.fontRenderer.getStringWidth(s) / 2) - 10;
        this.urlY = offset - 2;
        this.urlWidth = this.fontRenderer.getStringWidth(s) + 20;
        this.urlHeight = 14;
        GCCoreGuiMissingCore.drawRect(this.urlX, this.urlY, this.urlX + this.urlWidth, this.urlY + this.urlHeight, GCCoreUtil.convertTo32BitColor(50, 0, 255, 0));
        this.drawCenteredString(this.fontRenderer, s, this.width / 2, offset, 0x999999);
    }

    protected void keyTyped(char par1, int par2) {}

    public void actionPerformed()
    {
        this.actionPerformed(null);
    }

    protected void actionPerformed(GuiButton par1GuiButton)
    {
        FMLClientHandler.instance().getClient().displayGuiScreen((GuiScreen)null);
    }
    
    @Override
    protected void mouseClicked(int x, int y, int which)
    {
        if (x > this.urlX && x < this.urlX + this.urlWidth && y > this.urlY && y < this.urlY + this.urlHeight)
        {
            try
            {
                Class<?> oclass = Class.forName("java.awt.Desktop");
                Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(url)});
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
}
