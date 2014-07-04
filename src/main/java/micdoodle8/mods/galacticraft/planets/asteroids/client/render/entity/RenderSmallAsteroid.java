package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSmallAsteroid extends Render
{
	private RenderBlocks blockRenderer = new RenderBlocks();
	private static final ResourceLocation asteroidTexture0 = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/blocks/asteroid0.png");
	private static final ResourceLocation asteroidTexture1 = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/blocks/asteroid1.png");
	private static final ResourceLocation asteroidTexture2 = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/blocks/asteroid2.png");

	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTickTime)
	{
		EntitySmallAsteroid asteroid = (EntitySmallAsteroid) entity;

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glRotatef(asteroid.rotationPitch, 1, 0, 0);
		GL11.glRotatef(asteroid.rotationYaw, 0, 1, 0);

		this.bindEntityTexture(asteroid);
		this.blockRenderer.renderBlockAsItem(AsteroidBlocks.blockBasic, 0, 1.0F);

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationBlocksTexture;
	}
}
