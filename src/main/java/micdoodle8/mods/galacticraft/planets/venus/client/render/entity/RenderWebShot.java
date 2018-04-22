package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import micdoodle8.mods.galacticraft.planets.venus.entities.EntityWebShot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWebShot extends Render<EntityWebShot>
{
    public RenderWebShot(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityWebShot entity, double x, double y, double z, float par8, float partialTicks)
    {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
        this.bindEntityTexture(entity);
        GlStateManager.rotate((entity.ticksExisted + partialTicks) * 50.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.translate(-0.5F, -1F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(Blocks.WEB.getDefaultState(), 1.0F);
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWebShot entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
