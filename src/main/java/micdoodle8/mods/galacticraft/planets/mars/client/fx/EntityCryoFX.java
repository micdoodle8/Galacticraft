package micdoodle8.mods.galacticraft.planets.mars.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityCryoFX extends SpriteTexturedParticle
{
    private final IAnimatedSprite animatedSprite;
    float scaleStart;

    public EntityCryoFX(World worldIn, double x, double y, double z, double mX, double mY, double mZ, IAnimatedSprite animatedSprite)
    {
        super(worldIn, x, y, z, mX, mY, mZ);
        float f = 2.5F;
//        this.motionX *= 0.0;
//        this.motionY *= 0.10000000149011612;
//        this.motionZ *= 0.0;
//        this.motionX += motion.x;
//        this.motionY += motion.y;
//        this.motionZ += motion.z;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float) (Math.random() * 0.30000001192092896D);
        this.particleRed *= 0.8F;
        this.particleGreen *= 0.8F;
        this.particleScale *= 0.25F;
        this.particleScale *= f;
        this.scaleStart = this.particleScale;
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.3D));
        this.maxAge = (int) ((float) this.maxAge * f);
        this.canCollide = false;
        this.animatedSprite = animatedSprite;
        this.selectSpriteWithAge(animatedSprite);
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        float f = ((float) this.age + partialTicks) / (float) this.maxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.scaleStart * f;
        super.renderParticle(buffer, renderInfo, partialTicks);
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
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

//        this.setParticleTextureIndex(7 - this.age * 8 / this.maxAge);
        this.selectSpriteWithAge(this.animatedSprite);
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
        PlayerEntity entityplayer = this.world.getClosestPlayer(this.posX, posY, posZ, 2.0D, false);

        if (entityplayer != null && this.posY > entityplayer.getBoundingBox().minY)
        {
            this.posY += (entityplayer.getBoundingBox().minY - this.posY) * 0.2D;
            this.motionY += (entityplayer.getMotion().y - this.motionY) * 0.2D;
            this.setPosition(this.posX, this.posY, this.posZ);
        }

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
            return new EntityCryoFX(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
