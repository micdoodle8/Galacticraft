package micdoodle8.mods.galacticraft.core.tile;

import java.util.List;

import micdoodle8.mods.galacticraft.API.IDungeonBoss;
import micdoodle8.mods.galacticraft.API.IDungeonBossSpawner;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityDungeonSpawner extends TileEntityAdvanced implements IDungeonBossSpawner
{
	private IDungeonBoss boss;
	private boolean spawned = false;
	private boolean isBossDefeated = false;
	private boolean playerInRange;
	private boolean lastPlayerInRange;
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!this.worldObj.isRemote)
		{
	    	Vector3 thisVec = new Vector3(this);
	    	List l = this.worldObj.getEntitiesWithinAABB(GCCoreEntitySkeletonBoss.class, AxisAlignedBB.getBoundingBox(thisVec.x - 15, thisVec.y - 15, thisVec.z - 15, thisVec.x + 15, thisVec.y + 15, thisVec.z + 15));
			
	    	for (Entity e : (List<Entity>)l)
	    	{
	    		if (e instanceof GCCoreEntitySkeletonBoss)
	    		{
	    			if (!e.isDead)
	    			{
		    			this.boss = (IDungeonBoss) e;
		    			this.setBossSpawned(true);
		    			this.setBossDefeated(false);
	    			}
	    		}
	    	}
	    	
			if (this.boss == null && !this.getBossDefeated())
			{
				this.setBoss(new GCCoreEntitySkeletonBoss(this.worldObj, new Vector3(this).add(new Vector3(0.0D, 1.0D, 0.0D))));
			}
			
			EntityPlayer closestPlayer = null;
	    	
			Vector3 vec = new Vector3(this);
			closestPlayer = this.worldObj.getClosestPlayer(vec.x, vec.y, vec.z, 40.0D);
			
			this.playerInRange = closestPlayer != null;

			if (this.playerInRange && !this.lastPlayerInRange)
			{
				if (this.getBoss() != null && !this.getBossSpawned())
				{
					if (closestPlayer != null && this.boss instanceof Entity)
					{
						this.worldObj.spawnEntityInWorld((Entity)this.boss);
						this.setBossSpawned(true);
						this.boss.onBossSpawned(this);
					}
				}
			}

			if (this.getBossDefeated() && closestPlayer == null)
			{
				this.setBossDefeated(false);
			}
	    	
			this.lastPlayerInRange = this.playerInRange;
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

    	this.setBossSpawned(nbt.getBoolean("spawned"));
    	this.playerInRange = this.lastPlayerInRange = nbt.getBoolean("playerInRange");
    	this.setBossDefeated(nbt.getBoolean("defeated"));
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
    	super.writeToNBT(nbt);
    	
    	nbt.setBoolean("spawned", this.getBossSpawned());
    	nbt.setBoolean("playerInRange", this.playerInRange);
    	nbt.setBoolean("defeated", this.getBossDefeated());
    }

	@Override
	public void setBossSpawned(boolean spawned) 
	{
		this.spawned = spawned;
	}

	@Override
	public boolean getBossSpawned() 
	{
		return this.spawned;
	}

	@Override
	public void setBoss(IDungeonBoss boss) 
	{
		this.boss = boss;
	}

	@Override
	public IDungeonBoss getBoss() 
	{
		return this.boss;
	}
}
