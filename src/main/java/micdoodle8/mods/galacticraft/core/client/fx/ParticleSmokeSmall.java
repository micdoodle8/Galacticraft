package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ParticleSmokeSmall extends SpriteTexturedParticle
{
    float smokeParticleScale;
    private final IAnimatedSprite animatedSprite;

    public ParticleSmokeSmall(World par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, IAnimatedSprite animatedSprite)
    {
        super(par1World, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.01D;
        this.motionY *= 0.01D;
        this.motionZ *= 0.01D;
        this.setSize(0.05F, 0.05F);
        this.motionX += motX;
        this.motionY += motY;
        this.motionZ += motZ;
        this.particleAlpha = 0.8F;
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.2D) + 0.7F;
        this.particleScale *= 0.3F;
        this.smokeParticleScale = this.particleScale;
        this.maxAge = 110;
        this.canCollide = true;
        this.animatedSprite = animatedSprite;
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.age + partialTicks) / this.maxAge * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * var8;
        super.renderParticle(buffer, renderInfo, partialTicks);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    @Override
    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

        this.selectSpriteWithAge(animatedSprite);
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.03D;
            this.motionZ *= 1.03D;
        }

        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;
        this.selectSpriteWithAge(animatedSprite);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            ParticleSmokeSmall particleSmokeSmall = new ParticleSmokeSmall(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particleSmokeSmall.selectSpriteRandomly(this.spriteSet);
            return particleSmokeSmall;
        }
    }
}
