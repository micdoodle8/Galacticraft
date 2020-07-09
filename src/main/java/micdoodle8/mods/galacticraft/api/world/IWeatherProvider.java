package micdoodle8.mods.galacticraft.api.world;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IWeatherProvider
{
    @OnlyIn(Dist.CLIENT)
    IParticleData getParticle(ClientWorld world, double x, double y, double z);

    void weatherSounds(int j, Minecraft mc, World world, BlockPos blockpos, double xx, double yy, double zz, Random random);

    int getSoundInterval(float f);
}
