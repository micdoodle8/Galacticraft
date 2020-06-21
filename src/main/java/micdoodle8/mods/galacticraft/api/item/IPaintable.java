package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.LogicalSide;

public interface IPaintable
{
    int setColor(int color, PlayerEntity player);
}
