package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;

public class MusicTickerGC extends MusicTicker
{
    public MusicTickerGC(Minecraft mc)
    {
        super(mc);
    }

    @Override
    public void update()
    {
        MusicTicker.MusicType musictype = this.mc.getAmbientMusicType();
        if (FMLClientHandler.instance().getWorldClient() != null && FMLClientHandler.instance().getWorldClient().provider instanceof IGalacticraftWorldProvider)
        {
            musictype = ClientProxyCore.MUSIC_TYPE_MARS;
        }

        if (this.currentMusic != null)
        {
            if (!musictype.getMusicLocation().equals(this.currentMusic.getSoundLocation()))
            {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, musictype.getMinDelay() / 2);
            }

            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic))
            {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musictype.getMinDelay(), musictype.getMaxDelay()), this.timeUntilNextMusic);
            }
        }

        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
        {
            this.func_181558_a(musictype);
        }
    }
}
