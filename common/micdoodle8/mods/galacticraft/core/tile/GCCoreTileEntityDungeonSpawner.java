package micdoodle8.mods.galacticraft.core.tile;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.entities.IBoss;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * GCCoreTileEntityDungeonSpawner.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityDungeonSpawner extends GCCoreTileEntityAdvanced
{
	public Class<? extends IBoss> bossClass;
	public IBoss boss;
	public boolean spawned;
	public boolean isBossDefeated;
	public boolean playerInRange;
	public boolean lastPlayerInRange;
	public boolean playerCheated;
	private Vector3 roomCoords;
	private Vector3 roomSize;

	public GCCoreTileEntityDungeonSpawner()
	{
		this(GCCoreEntitySkeletonBoss.class);
	}

	public GCCoreTileEntityDungeonSpawner(Class<? extends IBoss> bossClass)
	{
		this.bossClass = bossClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			final Vector3 thisVec = new Vector3(this);
			final List<Entity> l = this.worldObj.getEntitiesWithinAABB(this.bossClass, AxisAlignedBB.getBoundingBox(thisVec.x - 15, thisVec.y - 15, thisVec.z - 15, thisVec.x + 15, thisVec.y + 15, thisVec.z + 15));

			for (final Entity e : l)
			{
				if (!e.isDead)
				{
					this.boss = (IBoss) e;
					this.boss.setRoom(this.roomCoords, this.roomSize);
					this.spawned = true;
					this.isBossDefeated = false;
				}
			}

			List<Entity> entitiesWithin = this.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getAABBPool().getAABB(this.roomCoords.intX() - 4, this.roomCoords.intY() - 4, this.roomCoords.intZ() - 4, this.roomCoords.intX() + this.roomSize.intX() + 3, this.roomCoords.intY() + this.roomSize.intY() + 3, this.roomCoords.intZ() + this.roomSize.intZ() + 3));

			for (Entity mob : entitiesWithin)
			{
				if (this.getDisabledCreatures().contains(mob.getClass()))
				{
					mob.setDead();
				}
			}

			if (this.boss == null && !this.isBossDefeated)
			{
				try
				{
					Constructor<?> c = this.bossClass.getConstructor(new Class[] { World.class });
					this.boss = (IBoss) c.newInstance(new Object[] { this.worldObj });
					((Entity) this.boss).setPosition(this.xCoord + 0.5, this.yCoord + 1.0, this.zCoord + 0.5);
					this.boss.setRoom(this.roomCoords, this.roomSize);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			entitiesWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB(this.roomCoords.intX() - 1, this.roomCoords.intY() - 1, this.roomCoords.intZ() - 1, this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ()));

			if (this.playerCheated)
			{
				if (!entitiesWithin.isEmpty())
				{
					this.isBossDefeated = false;
					this.spawned = false;
					this.lastPlayerInRange = false;
					this.playerCheated = false;
				}
			}

			this.playerInRange = !entitiesWithin.isEmpty();

			if (this.playerInRange && !this.lastPlayerInRange)
			{
				if (this.boss != null && !this.spawned)
				{
					if (this.boss instanceof Entity)
					{
						this.worldObj.spawnEntityInWorld((EntityLiving) this.boss);
						this.playSpawnSound((Entity) this.boss);
						this.spawned = true;
						this.boss.onBossSpawned(this);
						this.boss.setRoom(this.roomCoords, this.roomSize);
					}
				}
			}

			this.lastPlayerInRange = this.playerInRange;
		}
	}

	public void playSpawnSound(Entity entity)
	{
		;
	}

	public List<Class<? extends EntityLiving>> getDisabledCreatures()
	{
		List<Class<? extends EntityLiving>> list = new ArrayList<Class<? extends EntityLiving>>();
		list.add(GCCoreEntitySkeleton.class);
		list.add(GCCoreEntityCreeper.class);
		list.add(GCCoreEntityZombie.class);
		list.add(GCCoreEntitySpider.class);
		return list;
	}

	public void setRoom(Vector3 coords, Vector3 size)
	{
		this.roomCoords = coords;
		this.roomSize = size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.spawned = nbt.getBoolean("spawned");
		this.playerInRange = this.lastPlayerInRange = nbt.getBoolean("playerInRange");
		this.isBossDefeated = nbt.getBoolean("defeated");
		this.playerCheated = nbt.getBoolean("playerCheated");

		try
		{
			this.bossClass = (Class<? extends IBoss>) Class.forName(nbt.getString("bossClass"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.roomCoords = new Vector3();
		this.roomCoords.x = nbt.getDouble("roomCoordsX");
		this.roomCoords.y = nbt.getDouble("roomCoordsY");
		this.roomCoords.z = nbt.getDouble("roomCoordsZ");
		this.roomSize = new Vector3();
		this.roomSize.x = nbt.getDouble("roomSizeX");
		this.roomSize.y = nbt.getDouble("roomSizeY");
		this.roomSize.z = nbt.getDouble("roomSizeZ");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setBoolean("spawned", this.spawned);
		nbt.setBoolean("playerInRange", this.playerInRange);
		nbt.setBoolean("defeated", this.isBossDefeated);
		nbt.setBoolean("playerCheated", this.playerCheated);
		nbt.setString("bossClass", this.bossClass.getCanonicalName());

		if (this.roomCoords != null)
		{
			nbt.setDouble("roomCoordsX", this.roomCoords.x);
			nbt.setDouble("roomCoordsY", this.roomCoords.y);
			nbt.setDouble("roomCoordsZ", this.roomCoords.z);
			nbt.setDouble("roomSizeX", this.roomSize.x);
			nbt.setDouble("roomSizeY", this.roomSize.y);
			nbt.setDouble("roomSizeZ", this.roomSize.z);
		}
	}

	@Override
	public double getPacketRange()
	{
		return 0;
	}

	@Override
	public int getPacketCooldown()
	{
		return 0;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return false;
	}
}
