package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ParticleLaunchFlame extends Particle
{
    private float smokeParticleScale;
    private boolean spawnSmokeShort;
    private EntityLivingBase ridingEntity;

    public ParticleLaunchFlame(World par1World, Vector3 position, Vector3 motion, boolean launched, EntityLivingBase ridingEntity)
    {
        super(par1World, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        this.particleRed = 255F / 255F;
        this.particleGreen = 120F / 255F + this.rand.nextFloat() / 3;
        this.particleBlue = 55F / 255F;
        this.particleScale *= launched ? 4F : 0.1F;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = (int) (this.particleMaxAge * 1F);
        this.canCollide = true;
        this.spawnSmokeShort = launched;
        this.ridingEntity = ridingEntity;
    }

    @Override
    public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * var8;
        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            GalacticraftCore.proxy.spawnParticle(this.spawnSmokeShort ? "whiteSmokeLaunched" : "whiteSmokeIdle", new Vector3(this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ), new Vector3(this.motionX, this.motionY, this.motionZ), new Object[] {});
            GalacticraftCore.proxy.spawnParticle(this.spawnSmokeShort ? "whiteSmokeLargeLaunched" : "whiteSmokeLargeIdle", new Vector3(this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ), new Vector3(this.motionX, this.motionY, this.motionZ), new Object[] {});
            if (!this.spawnSmokeShort)
            {
                GalacticraftCore.proxy.spawnParticle("whiteSmokeIdle", new Vector3(this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ), new Vector3(this.motionX, this.motionY, this.motionZ), new Object[] {});
                GalacticraftCore.proxy.spawnParticle("whiteSmokeLargeIdle", new Vector3(this.posX, this.posY + this.rand.nextDouble() * 2, this.posZ), new Vector3(this.motionX, this.motionY, this.motionZ), new Object[] {});
            }
            this.setExpired();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
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

                    if (var5 instanceof EntityLivingBase && !var5.isDead && !var5.isBurning() && !var5.equals(this.ridingEntity))
                    {
                        var5.setFire(3);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_SET_ENTITY_FIRE, GCCoreUtil.getDimensionID(var5.world), new Object[] { var5.getEntityId() }));
                    }
                }
            }
        }
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

//    @Override
//    public float getBrightness(float par1)
//    {
//        return 1.0F;
//    }
}
