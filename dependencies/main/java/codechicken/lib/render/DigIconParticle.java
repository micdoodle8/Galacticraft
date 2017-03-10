package codechicken.lib.render;

import codechicken.lib.render.particle.CustomParticleHandler;
import codechicken.lib.vec.Cuboid6;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class DigIconParticle extends Particle {

    public DigIconParticle(World world, double x, double y, double z, double dx, double dy, double dz, TextureAtlasSprite icon) {
        super(world, x, y, z, dx, dy, dz);
        particleTexture = icon;
        particleGravity = 1;
        particleRed = particleGreen = particleBlue = 0.6F;
        particleScale /= 2.0F;
    }

    public static DigIconParticle newLandingParticle(World world, double x, double y, double z, double dx, double dy, double dz, TextureAtlasSprite icon) {
        DigIconParticle particle = new DigIconParticle(world, x, y, z, dx, dy, dz, icon);
        particle.motionX = dx;
        particle.motionY = dy;
        particle.motionZ = dz;
        return particle;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    public void setScale(float scale) {
        particleScale = scale;
    }

    public float getScale() {
        return particleScale;
    }

    public void setMaxAge(int age) {
        particleMaxAge = age;
    }

    public int getMaxAge() {
        return particleMaxAge;
    }

    /**
     * copy pasted from ParticleBreaking
     * //TODO Doc.
     */
    @Override
    public void renderParticle(VertexBuffer vertexBuffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float f = ((float) this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
        float f1 = f + 0.015609375F;
        float f2 = ((float) this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
        float f3 = f2 + 0.015609375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleTexture != null) {
            f = this.particleTexture.getInterpolatedU((double) (this.particleTextureJitterX / 4.0F * 16.0F));
            f1 = this.particleTexture.getInterpolatedU((double) ((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
            f2 = this.particleTexture.getInterpolatedV((double) (this.particleTextureJitterY / 4.0F * 16.0F));
            f3 = this.particleTexture.getInterpolatedV((double) ((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
        }

        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        vertexBuffer.pos((double) (f5 - rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4), (double) (f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double) f, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
        vertexBuffer.pos((double) (f5 - rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4), (double) (f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double) f, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
        vertexBuffer.pos((double) (f5 + rotationX * f4 + rotationXY * f4), (double) (f6 + rotationZ * f4), (double) (f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double) f1, (double) f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
        vertexBuffer.pos((double) (f5 + rotationX * f4 - rotationXY * f4), (double) (f6 - rotationZ * f4), (double) (f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double) f1, (double) f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
    }

    @Deprecated//Use CustomParticleHandler.addBlockHitEffects This will be removed in 1.11
    public static void addBlockHitEffects(World world, Cuboid6 bounds, int side, TextureAtlasSprite icon, ParticleManager effectRenderer) {
        CustomParticleHandler.addBlockHitEffects(world, bounds, EnumFacing.VALUES[side], icon, effectRenderer);
    }

    @Deprecated//Use CustomParticleHandler.addBlockDestroyEffects This will be removed in 1.11
    public static void addBlockDestroyEffects(World world, Cuboid6 bounds, TextureAtlasSprite[] icons, ParticleManager effectRenderer) {
        CustomParticleHandler.addBlockDestroyEffects(world, bounds, icons, effectRenderer);
    }
}
