package micdoodle8.mods.galacticraft.planets.asteroids.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.fx.BlockPosParticleData;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.ref.WeakReference;

@OnlyIn(Dist.CLIENT)
public class ParticleTelepad extends SpriteTexturedParticle
{
    private final float portalParticleScale;
    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;
    private final WeakReference<TileEntityShortRangeTelepad> telepad;
    private final boolean direction;

    public ParticleTelepad(World par1World, double x, double y, double z, double mX, double mY, double mZ, TileEntityShortRangeTelepad telepad, boolean direction)
    {
        super(par1World, x, y, z, mX, mY, mZ);
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.portalPosX = this.posX = x;
        this.portalPosY = this.posY = y;
        this.portalPosZ = this.posZ = z;
        this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
        this.maxAge = (int) (Math.random() * 10.0D) + 40;
        this.canCollide = false;
        this.canCollide = false;
//        this.setParticleTextureIndex((int) (Math.random() * 8.0D));
        this.telepad = new WeakReference<>(telepad);
        this.direction = direction;
//        float f = rand.nextFloat() * 0.6F + 0.4F;
//        float teleportTimeScaled = Math.min(1.0F, telepad.teleportTime / (float) TileEntityShortRangeTelepad.MAX_TELEPORT_TIME);
//        this.particleRed = f * 0.3F;
//        this.particleGreen = f * (0.3F + (teleportTimeScaled * 0.7F));
//        this.particleBlue = f * (1.0F - (teleportTimeScaled * 0.7F));
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        float f6 = (this.age + partialTicks) / this.maxAge;
        f6 = 1.0F - f6;
        f6 *= f6;
        f6 = 1.0F - f6;
        this.particleScale = this.portalParticleScale * f6;
        super.renderParticle(buffer, renderInfo, partialTicks);
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        int i = super.getBrightnessForRender(par1);
        float f1 = (float) this.age / (float) this.maxAge;
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
    public void tick()
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
        float f = (float) this.age / (float) this.maxAge;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        this.posX = this.portalPosX + this.motionX * f;
        this.posY = this.portalPosY + this.motionY * f + (1.0F - f1);
        this.posZ = this.portalPosZ + this.motionZ * f;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DownFactory implements IParticleFactory<BlockPosParticleData>
    {
        private final IAnimatedSprite spriteSet;

        public DownFactory(IAnimatedSprite spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BlockPosParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BlockPos pos = typeIn.getBlockPos();
            return new ParticleTelepad(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, (TileEntityShortRangeTelepad) worldIn.getTileEntity(pos), true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class UpFactory implements IParticleFactory<BlockPosParticleData>
    {
        private final IAnimatedSprite spriteSet;

        public UpFactory(IAnimatedSprite spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(BlockPosParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BlockPos pos = typeIn.getBlockPos();
            return new ParticleTelepad(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, (TileEntityShortRangeTelepad) worldIn.getTileEntity(pos), false);
        }
    }
}