package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.API.IDungeonBoss;
import micdoodle8.mods.galacticraft.API.IDungeonBossSpawner;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class GCCoreTileEntityDungeonSpawner extends TileEntityAdvanced implements IDungeonBossSpawner
{
	private IDungeonBoss boss;
	private boolean spawned = false;
	private boolean isBossDefeated = false;
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!this.worldObj.isRemote)
		{
			if (this.worldObj != null && this.boss == null && !isBossDefeated)
			{
				this.boss = new GCCoreEntitySkeletonBoss(this.worldObj, this, new Vector3(this).add(new Vector3(0.0D, 1.0D, 0.0D)));
			}
			else if (this.boss != null && !spawned)
			{
				Vector3 vec = new Vector3(this);
				EntityPlayer closestPlayer = this.worldObj.getClosestPlayer(vec.x, vec.y, vec.z, this.boss.getDistanceToSpawn());
			
				if (closestPlayer != null && this.boss instanceof Entity)
				{
					this.worldObj.spawnEntityInWorld((Entity)this.boss);
					this.spawned = true;
					this.boss.onBossSpawned(this);
				}
			}
		}
	}

	@Override
	public void setBossDefeated(boolean defeated) 
	{
		this.isBossDefeated = defeated;
	}

	@Override
	public boolean getBossDefeated() 
	{
		return this.isBossDefeated;
	}

    public void readFromNBT(NBTTagCompound nbt)
    {
    	super.readFromNBT(nbt);
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    }
}
