package micdoodle8.mods.galacticraft.core.client.fx;

import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityFXEntityOxygen extends EntityFX
{
    private final float portalParticleScale;
    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;
    private static long tick = -1L;
    private static Map<BlockPos, Integer> cacheLighting = new HashMap<>();

    public EntityFXEntityOxygen(World par1World, Vector3 position, Vector3 motion, Vector3 color)
    {
        super(par1World, position.x, position.y, position.z, motion.x, motion.y, motion.z);
        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        this.portalPosX = this.posX = position.x;
        this.portalPosY = this.posY = position.y;
        this.portalPosZ = this.posZ = position.z;
        this.portalParticleScale = this.particleScale = 0.1F;
        this.particleRed = color.floatX();
        this.particleGreen = color.floatY();
        this.particleBlue = color.floatZ();
        this.particleMaxAge = (int) (Math.random() * 10.0D) + 40;
        this.noClip = true;
        this.setParticleTextureIndex((int) (Math.random() * 8.0D));
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
    {
        float var8 = (this.particleAge + f0) / this.particleMaxAge;
        var8 = 1.0F - var8;
        var8 *= var8;
        var8 = 1.0F - var8;
        this.particleScale = this.portalParticleScale * var8;
        super.renderParticle(worldRenderer, entity, f0, f1, f2, f3, f4, f5);
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        long time = this.worldObj.getWorldTime(); 
        if (time > tick)
        {
            cacheLighting.clear();
            tick = time;
        }
        BlockPos blockpos = new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
        int var2;
        if (cacheLighting.containsKey(blockpos))
        {
            var2 = cacheLighting.get(blockpos);
        }
        else
        {
            var2 = this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
            cacheLighting.put(blockpos, var2);
        }
        float var3 = (float) this.particleAge / (float) this.particleMaxAge;
        var3 *= var3;
        var3 *= var3;
        final int var4 = var2 & 255;
        int var5 = var2 >> 16 & 255;
        var5 += (int) (var3 * 15.0F * 16.0F);

        if (var5 > 240)
        {
            var5 = 240;
        }

        return var4 | var5 << 16;
    }

    /**
     * Gets how bright this entity is.
     */
    @Override
    public float getBrightness(float par1)
    {
        final float var2 = super.getBrightness(par1);
        float var3 = (float) this.particleAge / (float) this.particleMaxAge;
        var3 = var3 * var3 * var3 * var3;
        return var2 * (1.0F - var3) + var3;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float var1 = (float) this.particleAge / (float) this.particleMaxAge;
        final float var2 = var1;
        var1 = -var1 + var1 * var1 * 2.0F;
        var1 = 1.0F - var1;
        this.posX = this.portalPosX + this.motionX * var1;
        this.posY = this.portalPosY + this.motionY * var1 + (1.0F - var2);
        this.posZ = this.portalPosZ + this.motionZ * var1;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }
    }
}
