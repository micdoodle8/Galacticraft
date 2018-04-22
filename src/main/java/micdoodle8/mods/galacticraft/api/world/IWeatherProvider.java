package micdoodle8.mods.galacticraft.api.world;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IWeatherProvider
{
    @SideOnly(Side.CLIENT)
    Particle getParticle(WorldClient world, double x, double y, double z);

    void weatherSounds(int j, Minecraft mc, World world, BlockPos blockpos, double xx, double yy, double zz, Random random);

    int getSoundInterval(float f);
}
