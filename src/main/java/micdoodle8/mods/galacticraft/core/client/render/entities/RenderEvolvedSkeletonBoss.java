//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeletonBoss;
//import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedSkeletonBoss;
//import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.entity.MobRenderer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderEvolvedSkeletonBoss extends MobRenderer<EntitySkeletonBoss, ModelEvolvedSkeletonBoss>
//{
//    private static final ResourceLocation skeletonBossTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/skeletonboss.png");
//
//    public RenderEvolvedSkeletonBoss(EntityRendererManager manager)
//    {
//        super(manager, new ModelEvolvedSkeletonBoss(), 0.9F);
//        this.addLayer(new LayerHeldItemEvolvedSkeletonBoss(this));
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntitySkeletonBoss entity)
//    {
//        return RenderEvolvedSkeletonBoss.skeletonBossTexture;
//    }
//
//    @Override
//    protected void preRenderCallback(EntitySkeletonBoss entity, float partialTicks)
//    {
//        RenderSystem.scalef(1.2F, 1.2F, 1.2F);
//        RenderSystem.rotatef((float) (Math.pow(entity.deathTicks, 2) / 5.0F + (Math.pow(entity.deathTicks, 2) / 5.0F - Math.pow(entity.deathTicks - 1, 2) / 5.0F) * partialTicks), 0.0F, 1.0F, 0.0F);
//    }
//}
