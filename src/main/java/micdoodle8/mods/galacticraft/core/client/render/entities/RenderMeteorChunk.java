package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMeteorChunk extends Render<EntityMeteorChunk>
{
    private static final ResourceLocation meteorChunkTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/blocks/meteor_chunk.png");
    private static final ResourceLocation meteorChunkHotTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/blocks/meteor_chunk_hot.png");
    private final ModelMeteorChunk modelMeteor;

    public RenderMeteorChunk(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.1F;
        this.modelMeteor = new ModelMeteorChunk();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMeteorChunk entity)
    {
        if (entity.isHot())
        {
            return RenderMeteorChunk.meteorChunkHotTexture;
        }
        else
        {
            return RenderMeteorChunk.meteorChunkTexture;
        }
    }

    @Override
    public void doRender(EntityMeteorChunk entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        final float pitch = entity.rotationPitch;
        final float yaw = entity.rotationYaw;
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.scale(0.3F, 0.3F, 0.3F);
        GlStateManager.rotate(yaw, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(pitch, 0.0F, 0.0F, 1.0F);
        this.bindEntityTexture(entity);
        this.modelMeteor.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
