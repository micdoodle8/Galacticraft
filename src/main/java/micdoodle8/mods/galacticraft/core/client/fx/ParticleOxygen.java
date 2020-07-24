package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ParticleOxygen extends SpriteTexturedParticle
{
    private final float portalParticleScale;
    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;
    private static long tick = -1L;
    private static final Map<BlockPos, Integer> cacheLighting = new HashMap<>();

    public ParticleOxygen(World par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, float colR, float colG, float colB)
    {
        super(par1World, posX, posY, posZ, motX, motY, motZ);
        this.motionX = motX;
        this.motionY = motY;
        this.motionZ = motZ;
        this.portalPosX = this.posX = posX;
        this.portalPosY = this.posY = posY;
        this.portalPosZ = this.posZ = posZ;
        this.portalParticleScale = this.particleScale = 0.1F;
        this.particleRed = colR;
        this.particleGreen = colG;
        this.particleBlue = colB;
        this.maxAge = (int) (Math.random() * 10.0D) + 40;
        this.canCollide = false;
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        float var8 = (this.age + partialTicks) / this.maxAge;
        var8 = 1.0F - var8;
        var8 *= var8;
        var8 = 1.0F - var8;
        this.particleScale = this.portalParticleScale * var8;
        super.renderParticle(buffer, renderInfo, partialTicks);
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        long time = this.world.getDayTime();
        if (time > tick)
        {
            cacheLighting.clear();
            tick = time;
        }
        BlockPos blockpos = new BlockPos(this.posX, this.posY + 0.17, this.posZ);
        int var2;
        if (cacheLighting.containsKey(blockpos))
        {
            var2 = cacheLighting.get(blockpos);
        }
        else
        {
            var2 = this.world.isBlockLoaded(blockpos) ? this.world.getLight(blockpos) : 0;
            cacheLighting.put(blockpos, var2);
        }
        float var3 = (float) this.age / (float) this.maxAge;
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

    //    @Override
//    public float getBrightness(float par1)
//    {
//        final float var2 = super.getBrightness(par1);
//        float var3 = (float) this.particleAge / (float) this.maxAge;
//        var3 = var3 * var3 * var3 * var3;
//        return var2 * (1.0F - var3) + var3;
//    }

    @Override
    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float var1 = (float) this.age / (float) this.maxAge;
        final float var2 = var1;
        var1 = -var1 + var1 * var1 * 2.0F;
        var1 = 1.0F - var1;
        this.posX = this.portalPosX + this.motionX * var1;
        this.posY = this.portalPosY + this.motionY * var1 + (1.0F - var2);
        this.posZ = this.portalPosZ + this.motionZ * var1;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
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
            return new ParticleOxygen(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 0.7F, 0.7F, 1.0F);
        }
    }
}
