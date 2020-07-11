//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.renderer.entity.PlayerRenderer;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.client.renderer.entity.model.PlayerModel;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class LayerOxygenTanks extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
//{
//    private final PlayerRenderer playerRenderer;
//    public ModelRenderer[] greenOxygenTanks = new ModelRenderer[2];
//    public ModelRenderer[] orangeOxygenTanks = new ModelRenderer[2];
//    public ModelRenderer[] redOxygenTanks = new ModelRenderer[2];
//
//    public LayerOxygenTanks(PlayerRenderer playerRendererIn)
//    {
//        super(playerRendererIn);
//        this.playerRenderer = playerRendererIn;
//        float scaleFactor = 0.0F;
//        PlayerModel<AbstractClientPlayerEntity> modelPlayer = playerRendererIn.getEntityModel();
//
//        this.greenOxygenTanks[0] = new ModelRenderer(modelPlayer, 4, 0).setTextureSize(128, 64);
//        this.greenOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
//        this.greenOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
//        this.greenOxygenTanks[0].mirror = true;
//        this.greenOxygenTanks[1] = new ModelRenderer(modelPlayer, 4, 0).setTextureSize(128, 64);
//        this.greenOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
//        this.greenOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
//        this.greenOxygenTanks[1].mirror = true;
//
//        this.orangeOxygenTanks[0] = new ModelRenderer(modelPlayer, 16, 0).setTextureSize(128, 64);
//        this.orangeOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
//        this.orangeOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
//        this.orangeOxygenTanks[0].mirror = true;
//        this.orangeOxygenTanks[1] = new ModelRenderer(modelPlayer, 16, 0).setTextureSize(128, 64);
//        this.orangeOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
//        this.orangeOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
//        this.orangeOxygenTanks[1].mirror = true;
//
//        this.redOxygenTanks[0] = new ModelRenderer(modelPlayer, 28, 0).setTextureSize(128, 64);
//        this.redOxygenTanks[0].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
//        this.redOxygenTanks[0].setRotationPoint(2F, 2F, 3.8F);
//        this.redOxygenTanks[0].mirror = true;
//        this.redOxygenTanks[1] = new ModelRenderer(modelPlayer, 28, 0).setTextureSize(128, 64);
//        this.redOxygenTanks[1].addBox(-1.5F, 0F, -1.5F, 3, 7, 3, scaleFactor);
//        this.redOxygenTanks[1].setRotationPoint(-2F, 2F, 3.8F);
//        this.redOxygenTanks[1].mirror = true;
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
//                boolean wearingLeftTankGreen = gearData.getLeftTank() == Constants.GEAR_ID_OXYGEN_TANK_LIGHT;
//                boolean wearingLeftTankOrange = gearData.getLeftTank() == Constants.GEAR_ID_OXYGEN_TANK_MEDIUM;
//                boolean wearingLeftTankRed = gearData.getLeftTank() == Constants.GEAR_ID_OXYGEN_TANK_HEAVY || gearData.getLeftTank() == Constants.GEAR_ID_OXYGEN_TANK_INFINITE;
//                boolean wearingRightTankGreen = gearData.getRightTank() == Constants.GEAR_ID_OXYGEN_TANK_LIGHT;
//                boolean wearingRightTankOrange = gearData.getRightTank() == Constants.GEAR_ID_OXYGEN_TANK_MEDIUM;
//                boolean wearingRightTankRed = gearData.getRightTank() == Constants.GEAR_ID_OXYGEN_TANK_HEAVY || gearData.getRightTank() == Constants.GEAR_ID_OXYGEN_TANK_INFINITE;
//
//                Minecraft.getInstance().textureManager.bindTexture(RenderPlayerGC.PLAYER_TEXTURE);
//
//                ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedBody, this.greenOxygenTanks[0]);
//                ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedBody, this.greenOxygenTanks[1]);
//                ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedBody, this.orangeOxygenTanks[0]);
//                ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedBody, this.orangeOxygenTanks[1]);
//                ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedBody, this.redOxygenTanks[0]);
//                ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedBody, this.redOxygenTanks[1]);
//
//                if (playerRenderer.getEntityModel().isSneak)
//                {
//                    this.greenOxygenTanks[0].rotationPointY = 2.0F;
//                    this.greenOxygenTanks[1].rotationPointY = 2.0F;
//                    this.greenOxygenTanks[0].rotationPointZ = 1.6F;
//                    this.greenOxygenTanks[1].rotationPointZ = 1.6F;
//
//                    this.orangeOxygenTanks[0].rotationPointY = 2.0F;
//                    this.orangeOxygenTanks[1].rotationPointY = 2.0F;
//                    this.orangeOxygenTanks[0].rotationPointZ = 1.6F;
//                    this.orangeOxygenTanks[1].rotationPointZ = 1.6F;
//
//                    this.redOxygenTanks[0].rotationPointY = 2.0F;
//                    this.redOxygenTanks[1].rotationPointY = 2.0F;
//                    this.redOxygenTanks[0].rotationPointZ = 1.6F;
//                    this.redOxygenTanks[1].rotationPointZ = 1.6F;
//                }
//                else
//                {
//                    this.greenOxygenTanks[0].rotationPointY = 0.5F;
//                    this.greenOxygenTanks[1].rotationPointY = 0.5F;
//                    this.greenOxygenTanks[0].rotationPointZ = 0.5F;
//                    this.greenOxygenTanks[1].rotationPointZ = 0.5F;
//
//                    this.orangeOxygenTanks[0].rotationPointY = 0.5F;
//                    this.orangeOxygenTanks[1].rotationPointY = 0.5F;
//                    this.orangeOxygenTanks[0].rotationPointZ = 0.5F;
//                    this.orangeOxygenTanks[1].rotationPointZ = 0.5F;
//
//                    this.redOxygenTanks[0].rotationPointY = 0.5F;
//                    this.redOxygenTanks[1].rotationPointY = 0.5F;
//                    this.redOxygenTanks[0].rotationPointZ = 0.5F;
//                    this.redOxygenTanks[1].rotationPointZ = 0.5F;
//                }
//
//                for (int i = 0; i < 2; ++i)
//                {
//                    GlStateManager.enableRescaleNormal();
//                    GlStateManager.pushMatrix();
//                    GlStateManager.translatef(0.175F, 0.0F, 0.0F);
//                    GlStateManager.translatef(0.0F, 0.2F, 0.0F);
//                    GlStateManager.translatef(0.0F, 0.0F, 0.2F);
//
//                    if (wearingLeftTankRed)
//                    {
//                        this.redOxygenTanks[0].render(scale);
//                    }
//
//                    if (wearingLeftTankOrange)
//                    {
//                        this.orangeOxygenTanks[0].render(scale);
//                    }
//
//                    if (wearingLeftTankGreen)
//                    {
//                        this.greenOxygenTanks[0].render(scale);
//                    }
//
//                    GlStateManager.popMatrix();
//
//                    GlStateManager.pushMatrix();
//                    GlStateManager.translatef(-0.175F, 0.0F, 0.0F);
//                    GlStateManager.translatef(0.0F, 0.2F, 0.0F);
//                    GlStateManager.translatef(0.0F, 0.0F, 0.2F);
//
//                    if (wearingRightTankRed)
//                    {
//                        this.redOxygenTanks[1].render(scale);
//                    }
//
//                    if (wearingRightTankOrange)
//                    {
//                        this.orangeOxygenTanks[1].render(scale);
//                    }
//
//                    if (wearingRightTankGreen)
//                    {
//                        this.greenOxygenTanks[1].render(scale);
//                    }
//                    GlStateManager.color3f(1.0F, 1.0F, 1.0F);
//                    GlStateManager.popMatrix();
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean shouldCombineTextures()
//    {
//        return true;
//    }
//}
