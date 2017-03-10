package codechicken.nei.config;

import codechicken.core.gui.GuiCCButton;
import codechicken.lib.texture.TextureUtils;
import net.minecraft.client.renderer.GlStateManager;

public class PatreonButton extends GuiCCButton {
    public PatreonButton(int x, int y, int width, int height) {
        super(x, y, width, height, null);
    }

    @Override
    public void drawButtonTex(int mousex, int mousey) {
        super.drawButtonTex(mousex, mousey);

        GlStateManager.color(1, 1, 1, 1);
        TextureUtils.changeTexture("nei:textures/patreonNeon.png");
        int texh = height - 4;
        int texw = texh * 4;
        drawModalRectWithCustomSizedTexture(x + width / 2 - texw / 2, y + height / 2 - texh / 2, 0, 0, texw, texh, texw, texh);
    }
}
