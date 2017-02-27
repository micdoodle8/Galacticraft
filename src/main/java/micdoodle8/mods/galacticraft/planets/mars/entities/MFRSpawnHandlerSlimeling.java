package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.planets.mars.inventory.InventorySlimeling;
import net.minecraft.entity.EntityLivingBase;
import powercrystals.minefactoryreloaded.api.IMobSpawnHandler;

public class MFRSpawnHandlerSlimeling
{
	public class SpawnableSlimeling implements IMobSpawnHandler {

		@Override
		public Class<? extends EntityLivingBase> getMobClass()
		{
			return EntitySlimeling.class;
		}

		@Override
		public void onMobSpawn(EntityLivingBase entity)
		{
		}

		//Prevent dupe of slimeling inventory by exact copy spawns
		//(similar to what MFR does for vanilla donkeys) 
		@Override
		public void onMobExactSpawn(EntityLivingBase entity)
		{
			EntitySlimeling ent = (EntitySlimeling) entity;
			ent.slimelingInventory = new InventorySlimeling(ent);
		}
	}
}
