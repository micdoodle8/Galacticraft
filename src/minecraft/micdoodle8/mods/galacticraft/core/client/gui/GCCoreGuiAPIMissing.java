package micdoodle8.mods.galacticraft.core.client.gui;

import java.io.File;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.core.exception.MissingAPIException;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.src.ServerPlayerAPI;
import cpw.mods.fml.common.DuplicateModsFoundException;
import cpw.mods.fml.common.ModContainer;

public class GCCoreGuiAPIMissing extends GuiErrorScreen
{
    private MissingAPIException apiMissing;

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
        int offset = Math.max(85 - apiMissing.missingAPIs.size() * 10, 10);
        this.drawCenteredString(this.fontRenderer, "Galacticraft has found a problem with your minecraft installation", this.width / 2, offset, 0xFFFFFF);
        offset+=10;
        this.drawCenteredString(this.fontRenderer, "You are missing a required API", this.width / 2, offset, 0xFFFFFF);
        offset+=10;
        this.drawCenteredString(this.fontRenderer, "Class Name : API", this.width / 2, offset, 0xFFFFFF);
        offset+=5;
        for (String s : apiMissing.missingAPIs)
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
