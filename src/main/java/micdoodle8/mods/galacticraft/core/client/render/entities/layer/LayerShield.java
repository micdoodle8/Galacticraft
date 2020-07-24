//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
//import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.renderer.entity.PlayerRenderer;
//import net.minecraft.client.renderer.entity.layers.LayerRenderer;
//import net.minecraft.client.renderer.entity.model.PlayerModel;
//import org.lwjgl.opengl.GL11;
//
//public class LayerShield extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
//{
//    private final PlayerRenderer renderer;
//    private PlayerModel shieldModel;
//
//    public LayerShield(PlayerRenderer playerRendererIn)
//    {
//        super(playerRendererIn);
//        this.renderer = playerRendererIn;
//        this.initModel();
//    }
//
//    @Override
//    public boolean shouldCombineTextures()
//    {
//        return false;
//    }
//
//    @Override
//    public void render(AbstractClientPlayerEntity player, float f2, float f3, float partialTicks, float f5, float f6, float f7, float scale)
//    {
//        if (!player.isInvisible())
//        {
//            PlayerGearData gearData = GalacticraftCore.proxy.getGearData(player);
//
//            if (gearData != null)
//            {
//                if (gearData.getShieldController() != GCPlayerHandler.GEAR_NOT_PRESENT)
//                {
//                    this.shieldModel.setVisible(false);
//                    this.shieldModel.bipedRightLeg.showModel = true;
//                    this.shieldModel.bipedLeftLeg.showModel = true;
//                    this.shieldModel.bipedBody.showModel = true;
//                    this.shieldModel.bipedRightArm.showModel = true;
//                    this.shieldModel.bipedLeftArm.showModel = true;
//                    this.shieldModel.bipedHead.showModel = true;
//                    this.shieldModel.bipedHeadwear.showModel = true;
//                    this.shieldModel.setModelAttributes(this.renderer.getEntityModel());
//                    this.shieldModel.setLivingAnimations(player, f2, f3, partialTicks);
//                    this.renderer.bindTexture(RenderPlayerGC.thermalPaddingTexture0);
//                    RenderSystem.matrixMode(5890);
//                    RenderSystem.loadIdentity();
//                    float f = (float) Math.sin((player.ticksExisted + partialTicks) / 20.0F) * 5.0F;
//                    RenderSystem.translatef(0.0F, f * 0.01F, 0.0F);
//                    RenderSystem.matrixMode(5888);
//
//                    RenderSystem.disableLighting();
//                    Minecraft.getInstance().textureManager.bindTexture(RenderPlayerGC.heatShieldTexture);
//                    RenderSystem.enableBlend();
//                    float sTime = (float) ((1.0F - Math.sin((player.ticksExisted + partialTicks) / 10.0F)) * 0.1F + 0.0F);
//
//                    float r = 0.9F * sTime;
//                    float g = 0.2F * sTime;
//                    float b = 0.9F * sTime;
//
//                    RenderSystem.depthMask(false);
//                    RenderSystem.color4f(r, g, b, 0.2F);
//                    RenderSystem.blendFunc(1, 1);
//                    this.shieldModel.render(player, f2, f3, f5, f6, f7, scale);
//                    RenderSystem.matrixMode(5890);
//                    RenderSystem.loadIdentity();
//                    RenderSystem.matrixMode(5888);
//                    RenderSystem.color4f(1, 1, 1, 1);
//                    RenderSystem.disableBlend();
//                    RenderSystem.enableAlphaTest();
//                    RenderSystem.enableLighting();
//                    RenderSystem.depthMask(true);
//                }
//            }
//        }
//    }
//
//    private void initModel()
//    {
//        this.shieldModel = new PlayerModel(1.5F, false);
//    }
//}
