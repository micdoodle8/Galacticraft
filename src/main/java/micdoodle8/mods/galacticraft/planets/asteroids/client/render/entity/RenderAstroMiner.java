package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class RenderAstroMiner extends Render
{
    private RenderBlocks blockRenderer = new RenderBlocks();
    private float spin;
    private float lastPartTime;

    private ResourceLocation modelTexture;
    protected IModelCustom modelObj;

    private final NoiseModule wobbleX;
    private final NoiseModule wobbleY;
    private final NoiseModule wobbleZ;
    private final NoiseModule wobbleXX;
    private final NoiseModule wobbleYY;
    private final NoiseModule wobbleZZ;

    public RenderAstroMiner()
    {
    	this.modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMiner.obj"));
        this.modelTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/astroMiner.png");
        this.shadowSize = 2F;
        
        Random rand = new Random();
        this.wobbleX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleX.amplitude = 0.5F;
        this.wobbleX.frequencyX = 0.025F;

        this.wobbleY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleY.amplitude = 0.6F;
        this.wobbleY.frequencyX = 0.025F;

        this.wobbleZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZ.amplitude = 0.1F;
        this.wobbleZ.frequencyX = 0.025F;

        this.wobbleXX = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleXX.amplitude = 0.1F;
        this.wobbleXX.frequencyX = 0.8F;

        this.wobbleYY = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleYY.amplitude = 0.15F;
        this.wobbleYY.frequencyX = 0.8F;

        this.wobbleZZ = new Gradient(rand.nextLong(), 2, 1);
        this.wobbleZZ.amplitude = 0.04F;
        this.wobbleZZ.frequencyX = 0.8F;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float partialTickTime)
    {
        EntityAstroMiner astroMiner = (EntityAstroMiner) entity;
        float time = astroMiner.ticksExisted + partialTickTime;
        float wx = this.wobbleX.getNoise(time) + this.wobbleXX.getNoise(time);        
        float wy = this.wobbleY.getNoise(time) + this.wobbleYY.getNoise(time);
        float wz = this.wobbleZ.getNoise(time) + this.wobbleZZ.getNoise(time);
        
        float partTime = partialTickTime - lastPartTime;
        lastPartTime = partialTickTime;
        while (partTime < 0) partTime += 1F;

        GL11.glPushMatrix();
        final float rotPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTickTime;
        final float rotYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTickTime;

        /*
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        RenderGlobal.drawOutlinedBoundingBox(asteroid.boundingBox, 16777215);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
         */
        GL11.glTranslatef((float)x, (float)y + 0.75F, (float)z);
        if (rotPitch != 0F)
        {
            GL11.glTranslatef(-0.65F, -0.65F, 0);
        	GL11.glRotatef(rotPitch, 0, 0, -1);
            GL11.glTranslatef(0.65F, 0.65F, 0);
        }
        
//        else if (rotPitch > 0F)
//        {
//            GL11.glTranslatef(-0.65F, -0.65F, 0);
//        	GL11.glRotatef(rotPitch, 0, 0, 1);
//            GL11.glTranslatef(0.65F, 0.65F, 0);
//        }
        GL11.glRotatef(rotYaw + 180F, 0, 1, 0);
        GL11.glTranslatef(0F, -0.65F, 0F);
        GL11.glScalef(0.06F, 0.06F, 0.06F);
        GL11.glTranslatef(wx, wy, wz);

        this.bindEntityTexture(astroMiner);
//        this.blockRenderer.renderBlockAsItem(Blocks.coal_block, 0, 1.0F);
//        GL11.glTranslatef(-0.6F, 0F, 0F);
//        spin += 2F * partTime;
//        if (spin >= 360F) spin-=360F;
//        GL11.glRotatef(spin, 1, 0, 0);
//        GL11.glScalef(0.5F, 1F, 1F);
//        this.blockRenderer.renderBlockAsItem(Blocks.diamond_block, 0, 1.0F);
        this.modelObj.renderAll();
        
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.modelTexture;
    }
}
