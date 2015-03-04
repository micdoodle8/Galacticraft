package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.MathHelper;

public class MusicTickerGC extends MusicTicker
{
    public MusicTickerGC(Minecraft mc)
    {
        super(mc);
    }

    public void update()
    {
        MusicTicker.MusicType musictype = this.field_147677_b.func_147109_W();

        if (FMLClientHandler.instance().getWorldClient() != null && FMLClientHandler.instance().getWorldClient().provider instanceof IGalacticraftWorldProvider)
        {
            musictype = ClientProxyCore.MUSIC_TYPE_MARS;
        }

        if (this.field_147678_c != null)
        {
            if (!musictype.getMusicTickerLocation().equals(this.field_147678_c.getPositionedSoundLocation()))
            {
                this.field_147677_b.getSoundHandler().stopSound(this.field_147678_c);
                this.field_147676_d = MathHelper.getRandomIntegerInRange(this.field_147679_a, 0, musictype.func_148634_b() / 2);
            }

            if (!this.field_147677_b.getSoundHandler().isSoundPlaying(this.field_147678_c))
            {
                this.field_147678_c = null;
                this.field_147676_d = Math.min(MathHelper.getRandomIntegerInRange(this.field_147679_a, musictype.func_148634_b(), musictype.func_148633_c()), this.field_147676_d);
            }
        }

        if (this.field_147678_c == null && this.field_147676_d-- <= 0)
        {
            this.field_147678_c = PositionedSoundRecord.func_147673_a(musictype.getMusicTickerLocation());
            this.field_147677_b.getSoundHandler().playSound(this.field_147678_c);
            this.field_147676_d = Integer.MAX_VALUE;
        }
    }
}