package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.exception.MissingAPIException;
import net.minecraft.client.gui.GuiErrorScreen;

public class GCCoreGuiAPIMissing extends GuiErrorScreen
{
    private final MissingAPIException apiMissing;

    public GCCoreGuiAPIMissing(MissingAPIException dupes)
    {
        this.apiMissing = dupes;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }
    
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawBackground(par1);
        int offset = Math.max(85 - this.apiMissing.missingAPIs.size() * 10, 10);
        this.drawCenteredString(this.fontRenderer, "Galacticraft has found a problem with your minecraft installation", this.width / 2, offset, 0xFFFFFF);
        offset+=10;
        this.drawCenteredString(this.fontRenderer, "You are missing a required API", this.width / 2, offset, 0xFFFFFF);
        offset+=10;
        this.drawCenteredString(this.fontRenderer, "Class Name : API", this.width / 2, offset, 0xFFFFFF);
        offset+=5;
        for (String s : this.apiMissing.missingAPIs)
        {
            offset+=10;
            
            String actualName = "";
            
            if (s == "ServerPlayerAPI.class" || s == "PlayerAPI.class")
            {
            	actualName = "Player API";
            }
            
            if (s == "ModelPlayerAPI.class" || s == "RenderPlayerAPI.class")
            {
            	actualName = "Render Player API";
            }
            
            this.drawCenteredString(this.fontRenderer, String.format("%s : %s", s, actualName), this.width / 2, offset, 0xEEEEEE);
        }
    }
}
