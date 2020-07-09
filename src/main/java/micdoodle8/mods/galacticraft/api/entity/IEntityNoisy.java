package micdoodle8.mods.galacticraft.api.entity;


import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Implement into entities that make a sound all the time, like rockets
 */
public interface IEntityNoisy
{
    @OnlyIn(Dist.CLIENT)
    TickableSound getSoundUpdater();

    @OnlyIn(Dist.CLIENT)
    ISound setSoundUpdater(ClientPlayerEntity player);
}
