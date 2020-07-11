package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import net.minecraft.client.particle.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DripParticleGC extends SpriteTexturedParticle
{
    private final Fluid fluid;

    private DripParticleGC(World world, double x, double y, double z, Fluid fluid)
    {
        super(world, x, y, z);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.fluid = fluid;
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
        this.checkRespawn();
        if (!this.isExpired)
        {
            this.motionY -= this.particleGravity;
            this.move(this.motionX, this.motionY, this.motionZ);
            this.updateMotion();
            if (!this.isExpired)
            {
                this.motionX *= 0.98F;
                this.motionY *= 0.98F;
                this.motionZ *= 0.98F;
                BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
                IFluidState ifluidstate = this.world.getFluidState(blockpos);
                if (ifluidstate.getFluid() == this.fluid && this.posY < (double) ((float) blockpos.getY() + ifluidstate.getActualHeight(this.world, blockpos)))
                {
                    this.setExpired();
                }

            }
        }
    }

    protected void checkRespawn()
    {
        if (this.maxAge-- <= 0)
        {
            this.setExpired();
        }

    }

    protected void updateMotion()
    {
    }

    @OnlyIn(Dist.CLIENT)
    static class Dripping extends DripParticleGC
    {
        private final IParticleData data;

        private Dripping(World world, double x, double y, double z, Fluid fluid, IParticleData data)
        {
            super(world, x, y, z, fluid);
            this.data = data;
            this.particleGravity *= 0.02F;
            this.maxAge = 40;
        }

        @Override
        protected void checkRespawn()
        {
            if (this.maxAge-- <= 0)
            {
                this.setExpired();
                this.world.addParticle(this.data, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ);
            }

        }

        @Override
        protected void updateMotion()
        {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class DrippingOil extends DripParticleGC.Dripping
    {
        private DrippingOil(World world, double x, double y, double z, Fluid fluid, IParticleData data)
        {
            super(world, x, y, z, fluid, data);
        }

        @Override
        protected void checkRespawn()
        {
            this.particleRed = 1.0F;
            this.particleGreen = 16.0F / (float) (40 - this.maxAge + 16);
            this.particleBlue = 4.0F / (float) (40 - this.maxAge + 8);
            super.checkRespawn();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class DrippingOilFactory implements IParticleFactory<BasicParticleType>
    {
        protected final IAnimatedSprite spriteSet;

        public DrippingOilFactory(IAnimatedSprite sprite)
        {
            this.spriteSet = sprite;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            DripParticleGC.DrippingOil oilDrip = new DripParticleGC.DrippingOil(worldIn, x, y, z, GCFluids.OIL.getFluid(), GCParticles.OIL_DRIP);
            oilDrip.selectSpriteRandomly(this.spriteSet);
            return oilDrip;
        }
    }
}