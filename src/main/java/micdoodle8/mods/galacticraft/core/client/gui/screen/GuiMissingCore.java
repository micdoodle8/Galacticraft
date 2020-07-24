package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.net.URI;

@OnlyIn(Dist.CLIENT)
public class GuiMissingCore extends Screen
{
    private int urlX;
    private int urlY;
    private int urlWidth;
    private int urlHeight;

    public GuiMissingCore()
    {
        super(new StringTextComponent("Missing Core"));
    }

    @Override
    protected void init()
    {
        super.init();
    }

    @Override
    public void render(int par1, int par2, float par3)
    {
        this.renderBackground();
        int offset = this.height / 2 - 50;
        this.drawCenteredString(this.font, GCCoreUtil.translate("gui.missing_core.name.0"), this.width / 2, offset, 0xFF5555);
        offset += 25;
        this.drawCenteredString(this.font, GCCoreUtil.translate("gui.missing_core.name.1"), this.width / 2, offset, 0xFF5555);
        offset += 20;
        this.drawCenteredString(this.font, GCCoreUtil.translate("gui.missing_core.name.2"), this.width / 2, offset, 0x999999);
        offset += 20;
        String s = TextFormatting.UNDERLINE + GCCoreUtil.translate("gui.missing_core.name.3");
        this.urlX = this.width / 2 - this.font.getStringWidth(s) / 2 - 10;
        this.urlY = offset - 2;
        this.urlWidth = this.font.getStringWidth(s) + 20;
        this.urlHeight = 14;
        AbstractGui.fill(this.urlX, this.urlY, this.urlX + this.urlWidth, this.urlY + this.urlHeight, ColorUtil.to32BitColor(50, 0, 0, 255));
        this.drawCenteredString(this.font, s, this.width / 2, offset, 0x999999);
    }

//    public void actionPerformed()
//    {
//        this.actionPerformed(null);
//    }

//    @Override
//    protected void actionPerformed(Button par1GuiButton)
//    {
//        Minecraft.getInstance().displayGuiScreen((Screen) null);
//    }

    @Override
    public boolean mouseClicked(double x, double y, int button)
    {
        if (x > this.urlX && x < this.urlX + this.urlWidth && y > this.urlY && y < this.urlY + this.urlHeight)
        {
            try
            {
                Class<?> oclass = Class.forName("java.awt.Desktop");
                Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
                oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new URI("http://micdoodle8.com/mods/galacticraft/downloads"));
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }

        return true;
    }
}
