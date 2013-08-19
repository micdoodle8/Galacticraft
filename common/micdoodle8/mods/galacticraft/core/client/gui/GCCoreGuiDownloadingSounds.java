package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.client.GCCoreThreadDownloadSound;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiDownloadingSounds extends GuiScreen
{
    private int displayCount = 0;
    private GCCoreThreadDownloadSound downloadResourcesThread;
    public String displayStatus = "";
    public int displayStatusColor = 0xFFFFFF;
    
    public GCCoreGuiDownloadingSounds(GCCoreThreadDownloadSound thread)
    {
        this.downloadResourcesThread = thread;
    }
    
    public void initGui()
    {
        super.initGui();
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        int offset = this.height / 2 - 50;
        this.drawCenteredString(this.fontRenderer, "Galacticraft is downloading sounds to your minecraft directory", this.width / 2, offset, 0xFFFFFF);
        offset += 12;
        this.drawCenteredString(this.fontRenderer, "This will only occur once", this.width / 2, offset, 0x777777);
        offset += 20;
        this.drawCenteredString(this.fontRenderer, "Ensure you have an internet connection available", this.width / 2, offset, 0xFFFFFF);
        offset += 20;
        
        if (this.displayStatusColor != 0xFF1111)
        {
            this.drawCenteredString(this.fontRenderer, this.getEllipsis(), this.width / 2, offset, 0xFFFFFF);
        }
        
        offset += 18;
        String str = this.displayStatus;
        String[] strSplit = str.split("#");
        
        if (strSplit.length > 1)
        {
            if (strSplit.length == 2)
            {
                this.drawCenteredString(this.fontRenderer, strSplit[0], this.width / 2, offset, displayStatusColor);
                offset += 14;
                this.drawCenteredString(this.fontRenderer, strSplit[1], this.width / 2, offset, displayStatusColor);
            }
        }
        else
        {
            this.drawCenteredString(this.fontRenderer, this.displayStatus, this.width / 2, offset, displayStatusColor);
        }
        
        displayCount++;
    }
    
    private String getEllipsis()
    {
        String ellipsis = "";
        
        for (int i = 0; i < Math.floor(displayCount / 40.0) % 5; i++)
        {
            ellipsis += ".";
        }
        
        return ellipsis;
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
}
