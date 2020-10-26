package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleSparks extends SpriteTexturedParticle
{
    float smokeParticleScale;
    private final IAnimatedSprite animatedSprite;

    public ParticleSparks(World par1World, double par2, double par4, double par6, double par8, double par12, IAnimatedSprite animatedSprite)
    {
        super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += par8;
        this.motionY += 0.06;
        this.motionZ += par12;
        this.particleRed = 255F / 255F;
        this.particleGreen = 255F / 255F;
        this.particleBlue = 0F / 255F + this.rand.nextFloat() / 6;
        this.particleScale *= 0.15F;
        this.particleScale *= 1.0F * 3;
        this.smokeParticleScale = this.particleScale;
        this.maxAge = (int) 50.0D;
        this.maxAge = (int) (this.maxAge * 1.0F);
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

        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.selectSpriteWithAge(this.animatedSprite);
        this.motionY -= 0.01D;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9100000262260437D;
        this.motionY *= 0.9100000262260437D;
        this.motionZ *= 0.9100000262260437D;
    }
}
