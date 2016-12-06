package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderMeteorChunk extends Render
{
    private static final ResourceLocation meteorChunkTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/meteor_chunk.png");
    private static final ResourceLocation meteorChunkHotTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/meteor_chunk_hot.png");

    private ModelMeteorChunk modelMeteor;

    public RenderMeteorChunk(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.1F;
        this.modelMeteor = new ModelMeteorChunk();
    }

    protected ResourceLocation func_110779_a(EntityMeteorChunk meteorChunk)
    {
        if (meteorChunk.isHot())
        {
            return RenderMeteorChunk.meteorChunkHotTexture;
        }
        else
        {
            return RenderMeteorChunk.meteorChunkTexture;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntityMeteorChunk) par1Entity);
    }

    public void renderMeteorChunk(EntityMeteorChunk entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.rotationPitch;
        final float var24b = entity.rotationYaw;
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glScalef(0.3F, 0.3F, 0.3F);
        GL11.glRotatef(var24b, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var24, 0.0F, 0.0F, 1.0F);

        this.bindEntityTexture(entity);
        this.modelMeteor = new ModelMeteorChunk();

        this.modelMeteor.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderMeteorChunk((EntityMeteorChunk) par1Entity, par2, par4, par6, par8, par9);
    }
}
