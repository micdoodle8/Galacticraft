package micdoodle8.mods.galacticraft.mars.tile;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
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
public class GCMarsTileEntityDungeonSpawner extends TileEntityDungeonSpawner
{
	public GCMarsTileEntityDungeonSpawner()
	{
		super(GCMarsEntityCreeperBoss.class);
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
		this.worldObj.playSoundAtEntity(entity, GalacticraftCore.ASSET_PREFIX + "ambience.scaryscape", 9.0F, 1.4F);
	}
}
