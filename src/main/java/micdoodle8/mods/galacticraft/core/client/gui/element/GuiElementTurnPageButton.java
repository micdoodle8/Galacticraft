package micdoodle8.mods.galacticraft.core.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiElementTurnPageButton extends Widget
{
    private final boolean nextPage;
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/bookleft.png");

    public GuiElementTurnPageButton(int par2, int par3, boolean par4)
    {
        super(par2, par3, 23, 13, "");
        this.nextPage = par4;
    }

    @Override
    public void render(int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            boolean var4 = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(GuiElementTurnPageButton.BACKGROUND);
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

            this.blit(this.x, this.y, var5, var6, 23, 13);
        }
    }
}
