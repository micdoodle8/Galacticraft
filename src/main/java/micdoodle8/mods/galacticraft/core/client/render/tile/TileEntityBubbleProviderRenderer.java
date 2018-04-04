package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@Deprecated
public class TileEntityBubbleProviderRenderer<E extends TileEntity & IBubbleProvider> extends TileEntitySpecialRenderer<E>
{
    private static IBakedModel sphere;

    private final float colorRed;
    private final float colorGreen;
    private final float colorBlue;

    public TileEntityBubbleProviderRenderer(float colorRed, float colorGreen, float colorBlue)
    {
        this.colorRed = colorRed;
        this.colorGreen = colorGreen;
        this.colorBlue = colorBlue;
    }

    private static void updateModels()
    {
        if (sphere == null)
        {
            try
            {
                sphere = ClientUtil.modelFromOBJ(new ResourceLocation(Constants.ASSET_PREFIX, "sphere.obj"), ImmutableList.of("Sphere"));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void render(E provider, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (!provider.getBubbleVisible())
        {
            return;
        }

        updateModels();

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.0F, (float) z + 0.5F);

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(this.colorRed / 2.0F, this.colorGreen / 2.0F, this.colorBlue / 2.0F, 1.0F);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDepthMask(false);
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GL11.glScalef(provider.getBubbleSize(), provider.getBubbleSize(), provider.getBubbleSize());

        int color = ColorUtil.to32BitColor(30, (int)(this.colorBlue / 2.0F * 255), (int)(this.colorGreen / 2.0F * 255), (int)(this.colorRed / 2.0F * 255));
        ClientUtil.drawBakedModelColored(sphere, color);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glDepthMask(true);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_CULL_FACE);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
