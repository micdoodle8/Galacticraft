package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityFXSmokeSmall extends EntityFX
{
    float smokeParticleScale;

    public EntityFXSmokeSmall(World par1World, Vector3 position, Vector3 motion)
    {
        super(par1World, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.01D;
        this.motionY *= 0.01D;
        this.motionZ *= 0.01D;
        this.setSize(0.05F, 0.05F);
        this.motionX += motion.x;
        this.motionY += motion.y;
        this.motionZ += motion.z;
        this.particleAlpha = 0.8F;
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.2D) + 0.7F;
        this.particleScale *= 0.3F;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = 110;
        this.noClip = false;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
    {
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.particleAge + f0) / this.particleMaxAge * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * var8;
        super.renderParticle(worldRenderer, entity, f0, f1, f2, f3, f4, f5);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.03D;
            this.motionZ *= 1.03D;
        }

        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;
    }
}
