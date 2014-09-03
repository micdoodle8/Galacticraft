package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import java.util.ArrayList;
import java.util.List;

public class TileEntityDungeonSpawnerMars extends TileEntityDungeonSpawner
{
    public TileEntityDungeonSpawnerMars()
    {
        super(EntityCreeperBoss.class);
    }

    @Override
    public List<Class<? extends EntityLiving>> getDisabledCreatures()
    {
        List<Class<? extends EntityLiving>> list = new ArrayList<Class<? extends EntityLiving>>();
        list.add(EntityEvolvedSkeleton.class);
        list.add(EntityEvolvedZombie.class);
        list.add(EntityEvolvedSpider.class);
        return list;
    }

    @Override
    public void playSpawnSound(Entity entity)
    {
        this.worldObj.playSoundAtEntity(entity, GalacticraftCore.TEXTURE_PREFIX + "ambience.scaryscape", 9.0F, 1.4F);
    }
}
