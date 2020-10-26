package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ParticleLaunchSmoke extends LaunchParticle
{
    float smokeParticleScale;
    private final IAnimatedSprite animatedSprite;

    public ParticleLaunchSmoke(World par1World, double posX, double posY, double posZ, double motX, double motY, double motZ, float size, boolean launched, IAnimatedSprite animatedSprite)
    {
        super(par1World, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.setSize(0.2F, 0.2F);
        this.motionX += motX;
        this.motionY += motY;
        this.motionZ += motZ;
        this.particleAlpha = 1.0F;
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.30000001192092896D) + 0.6F;
        this.particleScale *= 0.75F;
        this.particleScale *= size * 3;
        this.smokeParticleScale = this.particleScale;
        this.animatedSprite = animatedSprite;

        if (launched)
        {
            this.maxAge = (int) (this.maxAge * size) + 10;
        }
        else
        {
            this.motionX += par1World.rand.nextDouble() / 2 - 0.25;
            this.motionY += par1World.rand.nextDouble() / 20;
            this.motionZ += par1World.rand.nextDouble() / 2 - 0.25;
            this.maxAge = 30 + this.maxAge;
        }

        this.canCollide = true;
        this.selectSpriteWithAge(animatedSprite);
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.age + partialTicks) / this.maxAge * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * var8;
        super.renderParticle(buffer, renderInfo, partialTicks);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    @Override
    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        }

        this.selectSpriteWithAge(this.animatedSprite);
        this.move(this.motionX, this.motionY, this.motionZ);

//        if (this.posY == this.prevPosY)
//        {
////            this.motionX *= 1.0001D;
////            this.motionZ *= 1.0001D;
//        }
//        else
//        {
//            this.motionX *= 0.99D;
//            this.motionZ *= 0.99D;
//        }
    }
}
