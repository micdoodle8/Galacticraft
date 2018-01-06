package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
public class TileEntityScreenRenderer extends TileEntitySpecialRenderer<TileEntityScreen>
{
    public static final ResourceLocation blockTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/blocks/screen_side.png");
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    private float yPlane = 0.91F;
    float frame = 0.098F;

    @Override
    public void render(TileEntityScreen screen, double par2, double par4, double par6, float partialTickTime, int par9, float alpha)
    {
        GL11.glPushMatrix();
        // Texture file
        this.renderEngine.bindTexture(TileEntityScreenRenderer.blockTexture);
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);

        int meta = screen.getBlockMetadata();
        boolean screenData = (meta >= 8);
        meta &= 7;

        switch (meta)
        {
        case 0:
            GL11.glRotatef(180, 1, 0, 0);
            GL11.glTranslatef(0, -1.0F, -1.0F);
            break;
        case 1:
            break;
        case 2:
            GL11.glTranslatef(0.0F, 0.0F, -0.87F);
            GL11.glRotatef(90, 1.0F, 0, 0);
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            break;
        case 3:
            GL11.glTranslatef(0.0F, 0.0F, 0.87F);
            GL11.glRotatef(90, -1.0F, 0, 0);
            GL11.glTranslatef(1.0F, -1.0F, 1.0F);
            GL11.glRotatef(180, 0, -1.0F, 0);
            break;
        case 4:
            GL11.glTranslatef(-0.87F, 0.0F, 0.0F);
            GL11.glRotatef(90, 0, 0, -1.0F);
            GL11.glTranslatef(-1.0F, 0.0F, 1.0F);
            GL11.glRotatef(90, 0, 1.0F, 0);
            break;
        case 5:
            GL11.glTranslatef(0.87F, 0.0F, 0.0F);
            GL11.glRotatef(90, 0, 0, 1.0F);
            GL11.glTranslatef(1.0F, -1.0F, 0.0F);
            GL11.glRotatef(90, 0, -1.0F, 0);
            break;
        default:
            break;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glTranslatef(-screen.screenOffsetx, this.yPlane, -screen.screenOffsetz);
        GL11.glRotatef(90, 1F, 0F, 0F);
        boolean cornerblock = false;
        if (screen.connectionsLeft == 0 || screen.connectionsRight == 0)
        {
            cornerblock = (screen.connectionsUp == 0 || screen.connectionsDown == 0);
        }
        int totalLR = screen.connectionsLeft + screen.connectionsRight;
        int totalUD = screen.connectionsUp + screen.connectionsDown;
        if (totalLR > 1 && totalUD > 1 && !cornerblock)
        {
            //centre block
            if (screen.connectionsLeft == screen.connectionsRight - (totalLR | 1))
            {
                if (screen.connectionsUp == screen.connectionsDown - (totalUD | 1))
                {
                    cornerblock = true;
                }
            }
        }
        GL11.glRotatef(180, 0, 1, 0);
        GL11.glTranslatef(-screen.screen.getScaleX(), 0.0F, 0.0F);
        screen.screen.drawScreen(screen.imageType, partialTickTime + screen.getWorld().getWorldTime(), cornerblock);

        GL11.glPopMatrix();
    }
}
