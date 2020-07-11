//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.Constants;
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
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.model.obj.OBJModel;
//
//@OnlyIn(Dist.CLIENT)
//public class LayerFrequencyModule extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
//{
//    private final PlayerRenderer playerRenderer;
//    public static OBJModel.OBJBakedModel moduleModel;
//    public static OBJModel.OBJBakedModel radarModel;
//
//    public LayerFrequencyModule(PlayerRenderer playerRendererIn)
//    {
//        super(playerRendererIn);
//        this.playerRenderer = playerRendererIn;
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
//                boolean wearingModule = gearData.getFrequencyModule() != GCPlayerHandler.GEAR_NOT_PRESENT;
//                boolean wearingHelmet = gearData.getMask() != GCPlayerHandler.GEAR_NOT_PRESENT;
//                Minecraft.getInstance().textureManager.bindTexture(RenderPlayerGC.PLAYER_TEXTURE);
//
//                if (wearingModule)
//                {
//                    GlStateManager.enableRescaleNormal();
//                    GlStateManager.pushMatrix();
//                    Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//
//                    GlStateManager.rotatef(180, 1, 0, 0);
//                    GlStateManager.pushMatrix();
//                    GlStateManager.rotatef(this.playerRenderer.getEntityModel().bipedHeadwear.rotateAngleY * -Constants.RADIANS_TO_DEGREES, 0, 1, 0);
//                    GlStateManager.rotatef(this.playerRenderer.getEntityModel().bipedHeadwear.rotateAngleX * Constants.RADIANS_TO_DEGREES, 1, 0, 0);
//                    GlStateManager.scalef(0.3F, 0.3F, 0.3F);
//
//                    if (wearingHelmet)
//                    {
//                        GlStateManager.translatef(-1.1F, player.isSneaking() ? 0.35F : 1.2F, 0);
//                    }
//                    else
//                    {
//                        GlStateManager.translatef(-0.9F, player.isSneaking() ? 0.1F : 0.9F, 0);
//                    }
//
//                    ClientUtil.drawBakedModel(moduleModel);
//                    GlStateManager.translatef(0.0F, 1.3F, 0.0F);
//                    GlStateManager.rotatef((float) (Math.sin(player.ticksExisted * 0.05) * 50.0F), 1, 0, 0);
//                    GlStateManager.rotatef((float) (Math.cos(player.ticksExisted * 0.1) * 50.0F), 0, 1, 0);
//                    ClientUtil.drawBakedModel(radarModel);
//                    GlStateManager.popMatrix();
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
