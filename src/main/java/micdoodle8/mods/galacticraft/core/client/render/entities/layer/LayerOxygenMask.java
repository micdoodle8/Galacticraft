//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
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
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class LayerOxygenMask extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
//{
//    private final PlayerRenderer playerRenderer;
//    public ModelRenderer oxygenMask;
//
//    public LayerOxygenMask(PlayerRenderer playerRendererIn)
//    {
//        super(playerRendererIn);
//        this.playerRenderer = playerRendererIn;
//        float scaleFactor = 1.0F;
//        PlayerModel<AbstractClientPlayerEntity> modelPlayer = playerRendererIn.getEntityModel();
//
//        this.oxygenMask = new ModelRenderer(modelPlayer, 0, 0).setTextureSize(128, 64);
//        this.oxygenMask.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16, scaleFactor);
//        this.oxygenMask.setRotationPoint(0.0F, 0.0F, 0.0F);
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
//                boolean wearingMask = gearData.getMask() != GCPlayerHandler.GEAR_NOT_PRESENT;
//                Minecraft.getInstance().textureManager.bindTexture(RenderPlayerGC.OXYGEN_MASK_TEXTURE);
//
//                ClientUtil.copyModelAngles(this.playerRenderer.getEntityModel().bipedHeadwear, this.oxygenMask);
//                this.oxygenMask.rotationPointY = this.playerRenderer.getEntityModel().bipedHeadwear.rotationPointY * 8.0F;
//
//                GlStateManager.enableRescaleNormal();
//                GlStateManager.pushMatrix();
//                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
//
//                if (wearingMask)
//                {
//                    GL11.glPushMatrix();
//                    GL11.glScalef(1.05F, 1.05F, 1.05F);
//                    this.oxygenMask.render(scale);
//                    GL11.glScalef(1F, 1F, 1F);
//                    GL11.glPopMatrix();
//                }
//
//                GlStateManager.popMatrix();
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