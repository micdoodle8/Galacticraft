package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ParticleLaunchFlame extends SpriteTexturedParticle
{
    private final IAnimatedSprite animatedSprite;
    private final float smokeParticleScale;
    private final boolean spawnSmokeShort;
    private final UUID ridingEntity;

    public ParticleLaunchFlame(World par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, boolean launched, EntityParticleData particleData, IAnimatedSprite animatedSprite)
    {
        super(par1World, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.motionX = motX;
        this.motionY = motY;
        this.motionZ = motZ;
        this.particleRed = 255F / 255F;
        this.particleGreen = 120F / 255F + this.rand.nextFloat() / 3;
        this.particleBlue = 55F / 255F;
        this.particleScale *= launched ? 4F : 0.1F;
        this.smokeParticleScale = this.particleScale;
        this.maxAge = (int) (this.maxAge * 1F);
        this.canCollide = true;
        this.spawnSmokeShort = launched;
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
            this.world.addParticle(this.spawnSmokeShort ? GCParticles.WHITE_SMOKE_LAUNCHED : GCParticles.WHITE_SMOKE_IDLE, this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ, this.motionX, this.motionY, this.motionZ);
            this.world.addParticle(this.spawnSmokeShort ? GCParticles.WHITE_SMOKE_LAUNCHED_LARGE : GCParticles.WHITE_SMOKE_IDLE_LARGE, this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ, this.motionX, this.motionY, this.motionZ);
            if (!this.spawnSmokeShort)
            {
                this.world.addParticle(GCParticles.WHITE_SMOKE_IDLE, this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ, this.motionX, this.motionY, this.motionZ);
                this.world.addParticle(GCParticles.WHITE_SMOKE_IDLE_LARGE, this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ, this.motionX, this.motionY, this.motionZ);
            }
            this.setExpired();
        }
        else
        {
            this.selectSpriteWithAge(animatedSprite);
            this.motionY += 0.001D;
            this.move(this.motionX, this.motionY, this.motionZ);

            this.particleGreen += 0.01F;

            if (this.posY == this.prevPosY)
            {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

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

        @Nullable
        @Override
        public Particle makeParticle(EntityParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleLaunchFlame(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, true, typeIn, this.spriteSet);
        }
    }
}
