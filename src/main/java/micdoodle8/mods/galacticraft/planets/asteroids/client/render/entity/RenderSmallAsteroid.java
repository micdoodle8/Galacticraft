//package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;
//
//import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
//import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BlockRendererDispatcher;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//public class RenderSmallAsteroid extends EntityRenderer<EntitySmallAsteroid>
//{
//    public RenderSmallAsteroid(EntityRendererManager manager)
//    {
//        super(manager);
//    }
//
//    @Override
//    public void doRender(EntitySmallAsteroid asteroid, double x, double y, double z, float f, float partialTickTime)
//    {
//        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//        RenderSystem.disableRescaleNormal();
//
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float) x, (float) y + 0.5F, (float) z);
//        RenderSystem.rotatef(asteroid.rotationPitch, 1, 0, 0);
//        RenderSystem.rotatef(asteroid.rotationYaw, 0, 1, 0);
//
//        this.bindEntityTexture(asteroid);
//        dispatcher.renderBlockBrightness(AsteroidBlocks.rock0.getDefaultState(), 1.0F);
//
//        RenderSystem.popMatrix();
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntitySmallAsteroid entity)
//    {
//        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//    }
//}
