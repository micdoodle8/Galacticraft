package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import micdoodle8.mods.galacticraft.planets.mars.entities.EntityProjectileTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProjectileTNT extends Render<EntityProjectileTNT>
{
    public RenderProjectileTNT(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityProjectileTNT entity, double x, double y, double z, float par8, float partialTicks)
    {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);

        float f2 = (1.0F - ((float) entity.ticksExisted - partialTicks + 1.0F) / 100.0F) * 0.1F;
        this.bindEntityTexture(entity);
        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(Blocks.TNT.getDefaultState(), entity.getBrightness());
        GlStateManager.translate(0.0F, 0.0F, 1.0F);

        if (entity.ticksExisted % 2 == 0)
        {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            blockrendererdispatcher.renderBlockBrightness(Blocks.TNT.getDefaultState(), 0.2F);
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, par8, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityProjectileTNT entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
