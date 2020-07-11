//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.renderer.entity.LivingRenderer;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.client.renderer.entity.model.BipedModel;
//import net.minecraft.client.renderer.entity.model.PlayerModel;
//import net.minecraft.client.renderer.model.ItemCameraTransforms;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.HandSide;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class LayerHeldItemGC extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
//{
//    protected final LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> livingEntityRenderer;
//
//    public LayerHeldItemGC(LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> livingEntityRendererIn)
//    {
//        super(livingEntityRendererIn);
//        this.livingEntityRenderer = livingEntityRendererIn;
//    }
//
//    @Override
//    public void render(AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
//    {
//        if (player.getRidingEntity() instanceof ICameraZoomEntity)
//        {
//            return;
//        }
//        boolean flag = player.getPrimaryHand() == HandSide.RIGHT;
//        ItemStack itemstack = flag ? player.getHeldItemOffhand() : player.getHeldItemMainhand();
//        ItemStack itemstack1 = flag ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
//
//        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
//        {
//            GlStateManager.pushMatrix();
//
//            if (this.livingEntityRenderer.getEntityModel().isChild)
//            {
//                float f = 0.5F;
//                GlStateManager.translatef(0.0F, 0.75F, 0.0F);
//                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
//            }
//
//            this.renderHeldItem(player, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT);
//            this.renderHeldItem(player, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT);
//            GlStateManager.popMatrix();
//        }
//    }
//
//    private void renderHeldItem(LivingEntity p_188358_1_, ItemStack p_188358_2_, ItemCameraTransforms.TransformType p_188358_3_, HandSide handSide)
//    {
//        if (!p_188358_2_.isEmpty())
//        {
//            GlStateManager.pushMatrix();
//
//            if (p_188358_1_.isSneaking())
//            {
//                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
//            }
//            // Forge: moved this call down, fixes incorrect offset while sneaking.
//            ((BipedModel<AbstractClientPlayerEntity>) this.livingEntityRenderer.getEntityModel()).postRenderArm(0.0625F, handSide);
//            GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
//            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
//            boolean flag = handSide == HandSide.LEFT;
//            GlStateManager.translatef((float) (flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
//            Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
//            GlStateManager.popMatrix();
//        }
//    }
//
//    @Override
//    public boolean shouldCombineTextures()
//    {
//        return false;
//    }
//}
