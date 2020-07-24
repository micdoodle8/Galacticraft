//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityDish;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import net.minecraft.client.renderer.model.IBakedModel;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.client.FMLClientHandler;
//
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//
//public class TileEntityDishRenderer extends TileEntityRenderer<TileEntityDish>
//{
//    private static final ResourceLocation textureSupport = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/telesupport.png");
//    private static final ResourceLocation textureFork = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/telefork.png");
//    private static final ResourceLocation textureDish = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/teledish.png");
//    private static IBakedModel modelSupport;
//    private static IBakedModel modelFork;
//    private static IBakedModel modelDish;
//    private TextureManager renderEngine = Minecraft.getInstance().renderEngine;
//
//    private void updateModels()
//    {
//        if (modelDish == null)
//        {
//            try
//            {
//                modelDish = ClientUtil.modelFromOBJ(new ResourceLocation(Constants.MOD_ID_CORE, "teledish.obj"));
//                modelFork = ClientUtil.modelFromOBJ(new ResourceLocation(Constants.MOD_ID_CORE, "telefork.obj"));
//                modelSupport = ClientUtil.modelFromOBJ(new ResourceLocation(Constants.MOD_ID_CORE, "telesupport.obj"));
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void render(TileEntityDish tile, double par2, double par4, double par6, float partialTickTime, int par9, float alpha)
//    {
//        this.updateModels();
//        TileEntityDish dish = (TileEntityDish) tile;
//        float hour = dish.rotation(partialTickTime) % 360F;
//        float declination = dish.elevation(partialTickTime) % 360F;
//
//        final PlayerEntity player = Minecraft.getInstance().player;
//
//        RenderSystem.pushMatrix();
//        RenderSystem.enableRescaleNormal();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.translatef((float) par2, (float) par4, (float) par6);
//        RenderSystem.translatef(0.5F, 1.0F, 0.5F);
//        RenderSystem.scalef(1.6F, 1.25F, 1.6F);
//
//        this.textureManager.bindTexture(textureSupport);
//        ClientUtil.drawBakedModel(modelSupport);
//        RenderSystem.scalef(1.25F, 1.6F, 1.25F);
//        RenderSystem.translatef(0F, 2.88F * 0.15F / 1.6F, 0F);
//        RenderSystem.scalef(0.85F, 0.85F, 0.85F);
//        RenderSystem.rotatef(hour, 0, -1, 0);
//        this.textureManager.bindTexture(textureFork);
//        ClientUtil.drawBakedModel(modelFork);
//
////        float celestialAngle = (dish.getWorldObj().getCelestialAngle(1.0F) - 0.784690560F) * 360.0F;
////        float celestialAngle2 = dish.getWorldObj().getCelestialAngle(1.0F) * 360.0F;
//
//        RenderSystem.translatef(0.0F, 2.3F, 0.0F);
//        RenderSystem.rotatef(declination, 1.0F, 0.0F, 0.0F);
//        RenderSystem.translatef(0.0F, -2.3F, 0.0F);
//
//        this.textureManager.bindTexture(textureDish);
//        ClientUtil.drawBakedModel(modelDish);
//
//        RenderSystem.disableRescaleNormal();
//        RenderSystem.popMatrix();
//    }
//}
