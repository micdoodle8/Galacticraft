package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;

public interface IPaintable
{
    int setColor(int color, PlayerEntity player, Direction side);
}
