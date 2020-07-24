//package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.planets.venus.entities.EntityWebShot;
//import net.minecraft.block.Blocks;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BlockRendererDispatcher;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderWebShot extends EntityRenderer<EntityWebShot>
//{
//    public RenderWebShot(EntityRendererManager renderManager)
//    {
//        super(renderManager);
//        this.shadowSize = 0.5F;
//    }
//
//    @Override
//    public void doRender(EntityWebShot entity, double x, double y, double z, float par8, float partialTicks)
//    {
//        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//        RenderHelper.disableStandardItemLighting();
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float) x, (float) y + 0.5F, (float) z);
//        this.bindEntityTexture(entity);
//        RenderSystem.rotatef((entity.ticksExisted + partialTicks) * 50.0F, 0.0F, 1.0F, 0.0F);
//        RenderSystem.scalef(0.5F, 0.5F, 0.5F);
//        RenderSystem.translatef(-0.5F, -1F, 0.5F);
//        blockrendererdispatcher.renderBlockBrightness(Blocks.COBWEB.getDefaultState(), 1.0F);
//        RenderSystem.translatef(0.0F, 0.0F, 1.0F);
//        RenderSystem.popMatrix();
//        RenderHelper.enableStandardItemLighting();
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityWebShot entity)
//    {
//        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//    }
//}
