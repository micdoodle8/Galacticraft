package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElementTurnPageButton extends GuiButton
{
    private final boolean nextPage;
    private static final ResourceLocation background = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/bookleft.png");

    public GuiElementTurnPageButton(int par1, int par2, int par3, boolean par4)
    {
        super(par1, par2, par3, 23, 13, "");
        this.nextPage = par4;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.visible)
        {
            boolean var4 = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            par1Minecraft.getTextureManager().bindTexture(GuiElementTurnPageButton.background);
            int var5 = 0;
            int var6 = 192;

            if (var4)
            {
                var5 += 23;
            }

            if (!this.nextPage)
            {
                var6 += 13;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, var5, var6, 23, 13);
        }
    }
}
