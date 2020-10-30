package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ParticleLanderFlame extends SpriteTexturedParticle
{
    private final IAnimatedSprite animatedSprite;
    private final float smokeParticleScale;
    private final UUID ridingEntity;

    public ParticleLanderFlame(World world, double x, double y, double z, double mX, double mY, double mZ, EntityParticleData particleData, IAnimatedSprite animatedSprite)
    {
        super(world, x, y, z, mX, mY, mZ);
        this.motionX *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += mX;
        this.motionY = mY;
        this.motionZ += mZ;
        this.particleRed = 200F / 255F;
        this.particleGreen = 200F / 255F;
        this.particleBlue = 200F / 255F + this.rand.nextFloat() / 3;
        this.particleScale *= 8F * 1.0F;
        this.smokeParticleScale = this.particleScale;
        this.maxAge = (int) 5.0D;
        this.canCollide = true;
        this.ridingEntity = particleData.getEntityUUID();
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

        this.selectSpriteWithAge(this.animatedSprite);
        this.move(this.motionX, this.motionY, this.motionZ);

        this.particleGreen -= 0.09F;
        this.particleRed -= 0.09F;

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.particleScale *= 0.9599999785423279D;

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.world.rand.nextInt(5) == 1)
        {
            final List<?> var3 = this.world.getEntitiesWithinAABB(Entity.class, this.getBoundingBox().grow(1.0D, 0.5D, 1.0D));

            if (var3 != null)
            {
                for (int var4 = 0; var4 < var3.size(); ++var4)
                {
                    final Entity var5 = (Entity) var3.get(var4);

                    if (var5 instanceof LivingEntity && var5.isAlive() && !var5.isBurning() && !var5.getUniqueID().equals(this.ridingEntity))
                    {
                        var5.setFire(3);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_SET_ENTITY_FIRE, GCCoreUtil.getDimensionType(var5.world), new Object[]{var5.getEntityId()}));
                    }
                }
            }
        }
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        return Constants.PACKED_LIGHT_FULL_BRIGHT;
    }

//    @Override
//    public float getBrightness(float par1)
//    {
//        return 1.0F;
//    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<EntityParticleData>
    {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(EntityParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleLanderFlame(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn, this.spriteSet);
        }
    }
}
