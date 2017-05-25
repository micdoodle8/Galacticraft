package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class EntityFXLaunchFlame extends EntityFX
{
    private float smokeParticleScale;
    private boolean spawnSmokeShort;
    private EntityLivingBase ridingEntity;

    public EntityFXLaunchFlame(World par1World, Vector3 position, Vector3 motion, boolean launched, EntityLivingBase ridingEntity)
    {
        super(par1World, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += motion.x;
        this.motionY += motion.y;
        this.motionZ += motion.z;
        this.particleRed = 255F / 255F;
        this.particleGreen = 120F / 255F + this.rand.nextFloat() / 3;
        this.particleBlue = 55F / 255F;
        this.particleScale *= 2F;
        this.particleScale *= 1F * 2;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = (int) (this.particleMaxAge * 1F);
        this.noClip = false;
        this.spawnSmokeShort = launched;
        this.ridingEntity = ridingEntity;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
    {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.particleAge + f0) / this.particleMaxAge * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * var8;
        super.renderParticle(worldRenderer, entity, f0, f1, f2, f3, f4, f5);
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
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY += 0.001D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        this.particleGreen += 0.01F;

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        final List<?> var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D));

        if (var3 != null)
        {
            for (int var4 = 0; var4 < var3.size(); ++var4)
            {
                final Entity var5 = (Entity) var3.get(var4);

                if (var5 instanceof EntityLivingBase)
                {
                    if (!var5.isDead && !var5.isBurning() && !var5.equals(this.ridingEntity))
                    {
                        var5.setFire(3);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_SET_ENTITY_FIRE, GCCoreUtil.getDimensionID(var5.worldObj), new Object[] { var5.getEntityId() }));
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

    @Override
    public float getBrightness(float par1)
    {
        return 1.0F;
    }
}
