package micdoodle8.mods.galacticraft.api.world;

import net.minecraft.entity.Entity;

public interface IZeroGDimension
{
    boolean inFreefall(Entity entity);

    void setInFreefall(Entity entity);
}
