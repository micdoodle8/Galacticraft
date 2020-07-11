//package micdoodle8.mods.galacticraft.core.client.model;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.entity.player.ClientPlayerEntity;
//import net.minecraft.client.renderer.entity.model.PlayerModel;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import com.mojang.blaze3d.platform.GlStateManager;
//import net.minecraft.util.ResourceLocation;
//
//public class ModelPlayerGC extends PlayerModel<ClientPlayerEntity>
//{
//
//    public ModelPlayerGC(float var1, boolean smallArms)
//    {
//        super(var1, smallArms);
//
//        if (smallArms)
//        {
//            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
//            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, var1);
//            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
//            this.bipedRightArm = new ModelRenderer(this, 40, 16);
//            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, var1);
//            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
//        }
//        else
//        {
//            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
//            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, var1);
//            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
//            this.bipedRightArm = new ModelRenderer(this, 40, 16);
//            this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, var1);
//            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
//        }
//    }
//
//    @Override
//    public void render(ClientPlayerEntity entityIn, float f1, float f2, float f3, float f4, float f5, float scale)
//    {
//        GlStateManager.pushMatrix();
//        super.render(entityIn, f1, f2, f3, f4, f5, scale);
//        GlStateManager.popMatrix();
//    }
//}
