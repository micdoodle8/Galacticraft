package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSkeletonBoss extends RenderLiving<EntitySkeletonBoss>
{
    private static final ResourceLocation skeletonBossTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/skeletonboss.png");

    public RenderEvolvedSkeletonBoss(RenderManager manager)
    {
        super(manager, new ModelEvolvedSkeletonBoss(), 0.9F);
        this.addLayer(new LayerHeldItemEvolvedSkeletonBoss(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySkeletonBoss entity)
    {
        return RenderEvolvedSkeletonBoss.skeletonBossTexture;
    }

    @Override
    protected void preRenderCallback(EntitySkeletonBoss entity, float partialTicks)
    {
        GlStateManager.scale(1.2F, 1.2F, 1.2F);
        GlStateManager.rotate((float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTicks), 0.0F, 1.0F, 0.0F);
    }
}
