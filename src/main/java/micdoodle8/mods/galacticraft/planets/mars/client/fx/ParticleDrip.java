package micdoodle8.mods.galacticraft.planets.mars.client.fx;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleDrip extends Particle
{
    private int bobTimer;

    public ParticleDrip(World world, double x, double y, double z)
    {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.setParticleTextureIndex(113);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.bobTimer = 40;
        this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = this.motionY = this.motionZ = 0.0D;
    }

    @Override
    public void onUpdate()
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
            this.setParticleTextureIndex(113);
        }
        else
        {
            this.setParticleTextureIndex(112);
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.particleMaxAge-- <= 0)
        {
            this.setExpired();
        }

        if (this.onGround)
        {
            this.setExpired();
        }

        BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState state = this.world.getBlockState(pos);
        Material material = state.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = 0.0D;

            if (state.getBlock() instanceof BlockLiquid)
            {
                d0 = BlockLiquid.getLiquidHeightPercent(((Integer) state.getValue(BlockLiquid.LEVEL)).intValue());
            }

            double d1 = MathHelper.floor(this.posY) + 1 - d0;

            if (this.posY < d1)
            {
                this.setExpired();
            }
        }
    }
}