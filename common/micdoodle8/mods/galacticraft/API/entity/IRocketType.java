package micdoodle8.mods.galacticraft.api.entity;

import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase.EnumRocketType;

public interface IRocketType
{
    public EnumRocketType getType();

    public int getRocketTier();
}
