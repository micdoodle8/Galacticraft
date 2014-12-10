package micdoodle8.mods.galacticraft.planets.asteroids.client.fx;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;

@SideOnly(Side.CLIENT)
public class EntityFXTeleport extends EntityFX
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
        this.noClip = true;
        this.setParticleTextureIndex((int) (Math.random() * 8.0D));
        this.telepad = new WeakReference<TileEntityShortRangeTelepad>(telepad);
        this.direction = direction;
    }

    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        float f6 = (this.particleAge + par2) / this.particleMaxAge;
        f6 = 1.0F - f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        this.particleScale = this.portalParticleScale * f6;
        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
    }

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

    public float getBrightness(float par1)
    {
        float f1 = super.getBrightness(par1);
        float f2 = (float) this.particleAge / (float) this.particleMaxAge;
        f2 = f2 * f2 * f2 * f2;
        return f1 * (1.0F - f2) + f2;
    }

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
            this.setDead();
        }
    }
}