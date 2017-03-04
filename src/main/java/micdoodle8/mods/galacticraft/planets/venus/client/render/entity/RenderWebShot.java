package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import micdoodle8.mods.galacticraft.planets.venus.entities.EntityWebShot;
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
import org.lwjgl.opengl.GL11;

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
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);

        float f2 = (1.0F - ((float) entity.ticksExisted - partialTicks + 1.0F) / 100.0F) * 0.1F;
        this.bindEntityTexture(entity);
        GL11.glRotatef((entity.ticksExisted + partialTicks) * 50.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.translate(-0.5F, -1F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(Blocks.web.getDefaultState(), 1.0F);
        GlStateManager.translate(0.0F, 0.0F, 1.0F);

        GlStateManager.popMatrix();
//        super.doRender(entity, x, y, z, par8, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWebShot entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
