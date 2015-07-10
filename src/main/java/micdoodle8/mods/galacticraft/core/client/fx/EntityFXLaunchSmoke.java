package micdoodle8.mods.galacticraft.core.client.fx;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityFXLaunchSmoke extends EntityFX
{
    float smokeParticleScale;

    public EntityFXLaunchSmoke(World par1World, Vector3 position, Vector3 motion, float size, boolean launched)
    {
        super(par1World, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.setSize(0.2F, 0.2F);
        this.motionX += motion.x;
        this.motionY += motion.y;
        this.motionZ += motion.z;
        this.particleAlpha = 1.0F;
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.30000001192092896D) + 0.6F;
        this.particleScale *= 0.75F;
        this.particleScale *= size * 3;
        this.smokeParticleScale = this.particleScale;

        if (launched)
        {
            this.particleMaxAge = (int) (this.particleMaxAge * size) + 10;
        }
        else
        {
            this.motionX += par1World.rand.nextDouble() / 2 - 0.25;
            this.motionY += par1World.rand.nextDouble() / 20;
            this.motionZ += par1World.rand.nextDouble() / 2 - 0.25;
            this.particleMaxAge = 30 + this.particleMaxAge;
        }

        this.noClip = false;
    }

    @Override
    public void func_180434_a(WorldRenderer worldRenderer, Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
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
        super.func_180434_a(worldRenderer, entity, f0, f1, f2, f3, f4, f5);

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
        this.motionY -= 0.002D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.99D;
        this.motionZ *= 0.99D;
    }
}
