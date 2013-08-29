package micdoodle8.mods.galacticraft.mars.tile;

import java.util.ArrayList;
import java.util.List;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityCreeperBoss;
import net.minecraft.entity.EntityLiving;

public class GCMarsTileEntityDungeonSpawner extends GCCoreTileEntityDungeonSpawner
{
    public GCMarsTileEntityDungeonSpawner()
    {
        super(GCMarsEntityCreeperBoss.class);
    }
    
    public List<Class<? extends EntityLiving>> getDisabledCreatures()
    {
        List<Class<? extends EntityLiving>> list = new ArrayList<Class<? extends EntityLiving>>();
        list.add(GCCoreEntitySkeleton.class);
        list.add(GCCoreEntityZombie.class);
        list.add(GCCoreEntitySpider.class);
        return list;
    }
}
