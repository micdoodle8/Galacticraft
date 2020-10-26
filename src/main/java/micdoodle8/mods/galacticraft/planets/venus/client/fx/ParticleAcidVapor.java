package micdoodle8.mods.galacticraft.planets.venus.client.fx;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleAcidVapor extends SpriteTexturedParticle
{
    private final IAnimatedSprite animatedSprite;
    private final float smokeParticleScale;

    public ParticleAcidVapor(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, IAnimatedSprite animatedSprite)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += motionX / 10.0F;
        this.motionY += motionY;
        this.motionZ += motionZ / 10.0F;
        this.particleRed = (float) (Math.random() * 0.10000001192092896D + 0.8);
        this.particleGreen = particleRed;
        this.particleBlue = (float) (Math.random() * 0.10000001192092896D);
        this.particleAlpha = 1.0F;
//        this.particleScale *= 0.75F;
        this.particleScale *= 2.5F;
        this.smokeParticleScale = this.particleScale;
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.maxAge = (int) ((float) this.maxAge * 2.5F);
        this.canCollide = true;
        this.animatedSprite = animatedSprite;
        this.selectSpriteWithAge(animatedSprite);
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        GlStateManager.disableLighting();
        float f = (float) Math.pow(this.age / 11.0, 2.0F);
        f = Math.max(f, 0.1F);
        this.particleAlpha = this.particleAlpha * 0.994F;
        this.particleScale = this.smokeParticleScale * f;
        super.renderParticle(buffer, renderInfo, partialTicks);
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
        this.motionY += 0.004D;
        this.move(this.motionX, this.motionY, this.motionZ);

        this.motionY *= 0.9899999785423279D;
        this.motionX *= 1.001D;
        this.motionZ *= 1.001D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
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
            return new ParticleAcidVapor(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}