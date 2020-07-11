//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeletonBoss;
//import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedSkeletonBoss;
//import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.client.renderer.model.ItemCameraTransforms;
//import net.minecraft.item.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class LayerHeldItemEvolvedSkeletonBoss extends LayerRenderer<EntitySkeletonBoss, ModelEvolvedSkeletonBoss>
//{
//    private final RenderEvolvedSkeletonBoss renderer;
//    private final ItemStack heldItem = new ItemStack(Items.BOW);
//
//    public LayerHeldItemEvolvedSkeletonBoss(RenderEvolvedSkeletonBoss renderer)
//    {
//        super(renderer);
//        this.renderer = renderer;
//    }
//
//    @Override
//    public void render(EntitySkeletonBoss entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
//    {
//        if (entity.throwTimer + entity.postThrowDelay == 0)
//        {
//            this.renderHeldItem(entity, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
//            this.renderHeldItem(entity, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
//        }
//    }
//
//    @Override
//    public boolean shouldCombineTextures()
//    {
//        return false;
//    }
//
//    private void renderHeldItem(EntitySkeletonBoss entity, ItemCameraTransforms.TransformType type)
//    {
//        GlStateManager.pushMatrix();
//        this.renderer.getEntityModel().postRenderArm(0.0625F, type);
//
//        if (type == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
//        {
//            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
//            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
//            GlStateManager.translatef(0.05F, 0.125F, -1.525F);
//        }
//        else
//        {
//            GlStateManager.translatef(0.025F, 1.525F, -0.125F);
//        }
//
//        Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entity, heldItem, type, false);
//        GlStateManager.popMatrix();
//    }
//}