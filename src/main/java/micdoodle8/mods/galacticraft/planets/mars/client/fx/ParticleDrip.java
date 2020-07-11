package micdoodle8.mods.galacticraft.planets.mars.client.fx;

import micdoodle8.mods.galacticraft.core.client.fx.BlockPosParticleData;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleDrip extends SpriteTexturedParticle
{
    private final IAnimatedSprite animatedSprite;
    private int bobTimer;

    public ParticleDrip(World world, double x, double y, double z, IAnimatedSprite animatedSprite)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = this.motionY = this.motionZ = 0.0D;
//        this.setParticleTextureIndex(113);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.bobTimer = 40;
        this.maxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.animatedSprite = animatedSprite;
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
        this.particleRed = 0.0F;
        this.particleGreen = 0.2F;
        this.particleBlue = 0.1F;
        this.motionY -= this.particleGravity;

        if (this.bobTimer-- > 0)
        {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
//            this.setParticleTextureIndex(113);
        }
        else
        {
//            this.setParticleTextureIndex(112);
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.maxAge-- <= 0)
        {
            this.setExpired();
        }

        if (this.onGround)
        {
            this.setExpired();
        }

        BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
        BlockState state = this.world.getBlockState(pos);
        Material material = state.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = 0.0D;

            if (state.getBlock() instanceof FlowingFluidBlock)
            {
                d0 = ((FlowingFluidBlock) state.getBlock()).getFluid().getHeight(((FlowingFluidBlock) state.getBlock()).getFluidState(state));
            }

            double d1 = MathHelper.floor(this.posY) + 1 - d0;

            if (this.posY < d1)
            {
                this.setExpired();
            }
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
            return new ParticleDrip(worldIn, x, y, z, this.spriteSet);
        }
    }
}