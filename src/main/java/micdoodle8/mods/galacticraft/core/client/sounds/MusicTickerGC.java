package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.math.MathHelper;

public class MusicTickerGC extends MusicTicker
{
    public MusicTickerGC(Minecraft client)
    {
        super(client);
    }

    @Override
    public void tick()
    {
        MusicTicker.MusicType musictype = this.client.getAmbientMusicType();
        if (Minecraft.getInstance().world != null && Minecraft.getInstance().world.dimension instanceof IGalacticraftDimension)
        {
            musictype = ClientProxyCore.MUSIC_TYPE_MARS;
        }

        if (this.currentMusic != null)
        {
            if (!musictype.getSound().getName().equals(this.currentMusic.getSoundLocation()))
            {
                this.client.getSoundHandler().stop(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.nextInt(this.random, 0, musictype.getMinDelay() / 2);
            }

            if (!this.client.getSoundHandler().isPlaying(this.currentMusic))
            {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.nextInt(this.random, musictype.getMinDelay(), musictype.getMaxDelay()), this.timeUntilNextMusic);
            }
        }

        this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, musictype.getMaxDelay());

        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
        {
            this.play(musictype);
        }
    }
}
