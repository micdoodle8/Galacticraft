package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ParticleLaunchFlameUnlaunched extends ParticleLaunchFlame
{
    public ParticleLaunchFlameUnlaunched(World par1World, Vector3 position, Vector3 motion, EntityParticleData particleData, IAnimatedSprite sprite)
    {
        super(par1World, position, motion, false, particleData, sprite);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<EntityParticleData>
    {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle makeParticle(EntityParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            return new ParticleLaunchFlameUnlaunched(worldIn, new Vector3(x, y, z), new Vector3(xSpeed, ySpeed, zSpeed), typeIn, this.spriteSet);
        }
    }
}
