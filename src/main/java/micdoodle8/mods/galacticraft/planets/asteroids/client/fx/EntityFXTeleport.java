package micdoodle8.mods.galacticraft.planets.asteroids.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;

@SideOnly(Side.CLIENT)
public class EntityFXTeleport extends Particle
{
    private float portalParticleScale;
    private double portalPosX;
    private double portalPosY;
    private double portalPosZ;
    private WeakReference<TileEntityShortRangeTelepad> telepad;
    private boolean direction;

    public EntityFXTeleport(World par1World, Vector3 position, Vector3 motion, TileEntityShortRangeTelepad telepad, boolean direction)
    {
        super(par1World, position.x, position.y, position.z, motion.x, motion.y, motion.z);
        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;
        this.portalPosX = this.posX = position.x;
        this.portalPosY = this.posY = position.y;
        this.portalPosZ = this.posZ = position.z;
        this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
        this.particleMaxAge = (int) (Math.random() * 10.0D) + 40;
        this.canCollide = false;
        this.canCollide = false;
        this.setParticleTextureIndex((int) (Math.random() * 8.0D));
        this.telepad = new WeakReference<TileEntityShortRangeTelepad>(telepad);
        this.direction = direction;
    }

    @Override
    public void renderParticle(BufferBuilder worldRenderer, Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (this.particleAge + f0) / this.particleMaxAge;
        f6 = 1.0F - f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        this.particleScale = this.portalParticleScale * f6;
        super.renderParticle(worldRenderer, entity, f0, f1, f2, f3, f4, f5);
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        int i = super.getBrightnessForRender(par1);
        float f1 = (float) this.particleAge / (float) this.particleMaxAge;
        f1 *= f1;
        f1 *= f1;
        int j = i & 255;
        int k = i >> 16 & 255;
        k += (int) (f1 * 15.0F * 16.0F);

        if (k > 240)
        {
            k = 240;
        }

        return j | k << 16;
    }

//    @Override
//    public float getBrightness(float par1)
//    {
//        float f1 = super.getBrightness(par1);
//        float f2 = (float) this.particleAge / (float) this.particleMaxAge;
//        f2 = f2 * f2 * f2 * f2;
//        return f1 * (1.0F - f2) + f2;
//    }

    @Override
    public void onUpdate()
    {
        TileEntityShortRangeTelepad telepad1 = this.telepad.get();

        if (telepad1 != null)
        {
            Vector3 color = telepad1.getParticleColor(this.rand, this.direction);
            this.particleRed = color.floatX();
            this.particleGreen = color.floatY();
            this.particleBlue = color.floatZ();
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float) this.particleAge / (float) this.particleMaxAge;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        this.posX = this.portalPosX + this.motionX * f;
        this.posY = this.portalPosY + this.motionY * f + (1.0F - f1);
        this.posZ = this.portalPosZ + this.motionZ * f;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
    }
}