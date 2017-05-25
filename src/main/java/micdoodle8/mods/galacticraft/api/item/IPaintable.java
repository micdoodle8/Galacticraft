package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public interface IPaintable
{
    int setColor(int color, EntityPlayer player, Side side);
}
