package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPart
{
    World getWorld();

    boolean attackEntityFromPart(GCCoreEntityWormPart var1, DamageSource var2, int var3);
}
