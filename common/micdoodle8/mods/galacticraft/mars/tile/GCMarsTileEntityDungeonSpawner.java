package micdoodle8.mods.galacticraft.mars.tile;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityCreeperBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

/**
 * GCMarsTileEntityDungeonSpawner.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsTileEntityDungeonSpawner extends GCCoreTileEntityDungeonSpawner
{
	public GCMarsTileEntityDungeonSpawner()
	{
		super(GCMarsEntityCreeperBoss.class);
	}

	@Override
	public List<Class<? extends EntityLiving>> getDisabledCreatures()
	{
		List<Class<? extends EntityLiving>> list = new ArrayList<Class<? extends EntityLiving>>();
		list.add(GCCoreEntitySkeleton.class);
		list.add(GCCoreEntityZombie.class);
		list.add(GCCoreEntitySpider.class);
		return list;
	}

	@Override
	public void playSpawnSound(Entity entity)
	{
		this.worldObj.playSoundAtEntity(entity, GalacticraftCore.ASSET_PREFIX + "ambience.scaryscape", 9.0F, 1.4F);
	}
}
