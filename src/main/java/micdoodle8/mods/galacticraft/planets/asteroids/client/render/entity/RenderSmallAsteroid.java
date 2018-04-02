package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockBasicAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockBasicAsteroids.EnumBlockBasic.ASTEROID_0;

public class RenderSmallAsteroid extends Render<EntitySmallAsteroid>
{
    public RenderSmallAsteroid(RenderManager manager)
    {
        super(manager);
    }

    @Override
    public void doRender(EntitySmallAsteroid asteroid, double x, double y, double z, float f, float partialTickTime)
    {
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y + 0.5F, (float) z);
        GL11.glRotatef(asteroid.rotationPitch, 1, 0, 0);
        GL11.glRotatef(asteroid.rotationYaw, 0, 1, 0);

        this.bindEntityTexture(asteroid);
        dispatcher.renderBlockBrightness(AsteroidBlocks.blockBasic.getDefaultState().withProperty(BlockBasicAsteroids.BASIC_TYPE, ASTEROID_0), 1.0F);

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySmallAsteroid entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
