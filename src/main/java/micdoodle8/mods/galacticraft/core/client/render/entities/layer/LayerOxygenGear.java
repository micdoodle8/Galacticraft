//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.entity.PlayerRenderer;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.client.renderer.entity.model.PlayerModel;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class LayerOxygenGear extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
//{
//    private final PlayerRenderer playerRenderer;
//    public ModelRenderer[][] tubes = new ModelRenderer[2][7];
//
//    public LayerOxygenGear(PlayerRenderer playerRendererIn)
//    {
//        super(playerRendererIn);
//        this.playerRenderer = playerRendererIn;
//        float scaleFactor = 0.0F;
//        PlayerModel<AbstractClientPlayerEntity> modelPlayer = playerRendererIn.getEntityModel();
//
//        this.tubes[0][0] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[0][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[0][0].setRotationPoint(2F, 3F, 5.8F);
//        this.tubes[0][0].setTextureSize(128, 64);
//        this.tubes[0][0].mirror = true;
//        this.tubes[0][1] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[0][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[0][1].setRotationPoint(2F, 2F, 6.8F);
//        this.tubes[0][1].setTextureSize(128, 64);
//        this.tubes[0][1].mirror = true;
//        this.tubes[0][2] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[0][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[0][2].setRotationPoint(2F, 1F, 6.8F);
//        this.tubes[0][2].setTextureSize(128, 64);
//        this.tubes[0][2].mirror = true;
//        this.tubes[0][3] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[0][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[0][3].setRotationPoint(2F, 0F, 6.8F);
//        this.tubes[0][3].setTextureSize(128, 64);
//        this.tubes[0][3].mirror = true;
//        this.tubes[0][4] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[0][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[0][4].setRotationPoint(2F, -1F, 6.8F);
//        this.tubes[0][4].setTextureSize(128, 64);
//        this.tubes[0][4].mirror = true;
//        this.tubes[0][5] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[0][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[0][5].setRotationPoint(2F, -2F, 5.8F);
//        this.tubes[0][5].setTextureSize(128, 64);
//        this.tubes[0][5].mirror = true;
//        this.tubes[0][6] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[0][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[0][6].setRotationPoint(2F, -3F, 4.8F);
//        this.tubes[0][6].setTextureSize(128, 64);
//        this.tubes[0][6].mirror = true;
//
//        this.tubes[1][0] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[1][0].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[1][0].setRotationPoint(-2F, 3F, 5.8F);
//        this.tubes[1][0].setTextureSize(128, 64);
//        this.tubes[1][0].mirror = true;
//        this.tubes[1][1] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[1][1].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[1][1].setRotationPoint(-2F, 2F, 6.8F);
//        this.tubes[1][1].setTextureSize(128, 64);
//        this.tubes[1][1].mirror = true;
//        this.tubes[1][2] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[1][2].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[1][2].setRotationPoint(-2F, 1F, 6.8F);
//        this.tubes[1][2].setTextureSize(128, 64);
//        this.tubes[1][2].mirror = true;
//        this.tubes[1][3] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[1][3].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[1][3].setRotationPoint(-2F, 0F, 6.8F);
//        this.tubes[1][3].setTextureSize(128, 64);
//        this.tubes[1][3].mirror = true;
//        this.tubes[1][4] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[1][4].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[1][4].setRotationPoint(-2F, -1F, 6.8F);
//        this.tubes[1][4].setTextureSize(128, 64);
//        this.tubes[1][4].mirror = true;
//        this.tubes[1][5] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[1][5].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[1][5].setRotationPoint(-2F, -2F, 5.8F);
//        this.tubes[1][5].setTextureSize(128, 64);
//        this.tubes[1][5].mirror = true;
//        this.tubes[1][6] = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.tubes[1][6].addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scaleFactor);
//        this.tubes[1][6].setRotationPoint(-2F, -3F, 4.8F);
//        this.tubes[1][6].setTextureSize(128, 64);
//        this.tubes[1][6].mirror = true;
//    }
//
//    @Override
//    public void render(AbstractClientPlayerEntity player, float f5, float f6, float partialTicks, float f8, float f2, float f7, float scale)
//    {
//        if (!player.isInvisible())
//        {
//            PlayerGearData gearData = GalacticraftCore.proxy.getGearData(player);
//
//            if (gearData != null)
//            {
//                boolean wearingGear = gearData.getGear() == Constants.GEAR_ID_OXYGEN_GEAR;
//                Minecraft.getInstance().textureManager.bindTexture(RenderPlayerGC.PLAYER_TEXTURE);
//
//                if (wearingGear && !playerRenderer.getEntityModel().isSneak)
//                {
//                    for (int i = 0; i < 7; i++)
//                    {
//                        for (int k = 0; k < 2; k++)
//                        {
//                            ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedBody, this.tubes[k][i]);
//
//                            GlStateManager.enableRescaleNormal();
//                            GlStateManager.pushMatrix();
//                            GlStateManager.translatef(0.175F * (float) (k * 2 - 1), 0.0F, 0.0F);
//                            GlStateManager.translatef(0.0F, -0.0325F * (float) (i * 2 - 1), 0.0F);
//                            GlStateManager.translatef(0.0F, 0.0F, -0.0325F * (float) (Math.pow(i * 2 - 1, 2) * 0.05));
//                            GlStateManager.translatef(0.0F, 0.2F, 0.0F);
//                            GlStateManager.translatef(0.0F, 0.0F, 0.2F);
//
//                            this.tubes[k][i].render(scale);
//
//                            GlStateManager.popMatrix();
//                        }
//                    }
//                }
//            }
//        }
//        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public boolean shouldCombineTextures()
//    {
//        return true;
//    }
//}