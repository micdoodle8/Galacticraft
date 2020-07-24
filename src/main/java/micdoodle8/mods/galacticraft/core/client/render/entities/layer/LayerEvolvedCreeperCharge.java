//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedCreeper;
//import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedCreeper;
//import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class LayerEvolvedCreeperCharge extends LayerRenderer<EntityEvolvedCreeper, ModelEvolvedCreeper>
//{
//    private final RenderEvolvedCreeper render;
//    private final ModelEvolvedCreeper creeperModel = new ModelEvolvedCreeper(2.0F);
//
//    public LayerEvolvedCreeperCharge(RenderEvolvedCreeper render)
//    {
//        super(render);
//        this.render = render;
//    }
//
//    @Override
//    public void render(EntityEvolvedCreeper entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
//    {
//        if (entity.getPowered())
//        {
//            boolean flag = entity.isInvisible();
//            RenderSystem.depthMask(!flag);
//            this.render.bindTexture(new ResourceLocation("textures/entity/creeper/creeper_armor.png"));
//            RenderSystem.matrixMode(5890);
//            RenderSystem.loadIdentity();
//            float f = entity.ticksExisted + partialTicks;
//            RenderSystem.translatef(f * 0.01F, f * 0.01F, 0.0F);
//            RenderSystem.matrixMode(5888);
//            RenderSystem.enableBlend();
//            float f1 = 0.5F;
//            RenderSystem.color4f(f1, f1, f1, 1.0F);
//            RenderSystem.disableLighting();
//            RenderSystem.blendFunc(1, 1);
//            this.creeperModel.setModelAttributes(this.render.getEntityModel());
//            this.creeperModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//            RenderSystem.matrixMode(5890);
//            RenderSystem.loadIdentity();
//            RenderSystem.matrixMode(5888);
//            RenderSystem.enableLighting();
//            RenderSystem.disableBlend();
//            RenderSystem.depthMask(flag);
//        }
//    }
//
//    @Override
//    public boolean shouldCombineTextures()
//    {
//        return false;
//    }
//}