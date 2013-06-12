package micdoodle8.mods.galacticraft.core.tile;

import java.util.List;
import micdoodle8.mods.galacticraft.API.IDungeonBoss;
import micdoodle8.mods.galacticraft.API.IDungeonBossSpawner;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class GCCoreTileEntityDungeonSpawner extends TileEntityAdvanced implements IDungeonBossSpawner
{
    private IDungeonBoss boss;
    private boolean spawned;
    private boolean isBossDefeated;
    private boolean playerInRange;
    private boolean lastPlayerInRange;
    private boolean playerCheated;
    private Vector3 roomCoords;
    private Vector3 roomSize;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            final Vector3 thisVec = new Vector3(this);
            final List<Entity> l = this.worldObj.getEntitiesWithinAABB(GCCoreEntitySkeletonBoss.class, AxisAlignedBB.getBoundingBox(thisVec.x - 15, thisVec.y - 15, thisVec.z - 15, thisVec.x + 15, thisVec.y + 15, thisVec.z + 15));

            for (final Entity e : l)
            {
                if (e instanceof GCCoreEntitySkeletonBoss)
                {
                    if (!e.isDead)
                    {
                        this.boss = (IDungeonBoss) e;
                        ((GCCoreEntitySkeletonBoss) this.boss).setRoom(this.roomCoords, this.roomSize);
                        this.setBossSpawned(true);
                        this.setBossDefeated(false);
                    }
                }
            }

            List<Entity> entitiesWithin = this.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getAABBPool().getAABB(this.roomCoords.intX() - 4, this.roomCoords.intY() - 4, this.roomCoords.intZ() - 4, this.roomCoords.intX() + this.roomSize.intX() + 3, this.roomCoords.intY() + this.roomSize.intY() + 3, this.roomCoords.intZ() + this.roomSize.intZ() + 3));

            for (Entity mob : entitiesWithin)
            {
                if (mob instanceof GCCoreEntitySkeleton || mob instanceof GCCoreEntityZombie || mob instanceof GCCoreEntitySpider || mob instanceof GCCoreEntityCreeper)
                {
                    mob.setDead();
                }
            }

            if (this.boss == null && !this.getBossDefeated())
            {
                this.setBoss(new GCCoreEntitySkeletonBoss(this.worldObj, new Vector3(this).add(new Vector3(0.0D, 1.0D, 0.0D))));
                ((GCCoreEntitySkeletonBoss) this.boss).setRoom(this.roomCoords, this.roomSize);
            }

            entitiesWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB(this.roomCoords.intX() - 1, this.roomCoords.intY() - 1, this.roomCoords.intZ() - 1, this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ()));

            if (this.playerCheated)
            {
                if (!entitiesWithin.isEmpty())
                {
                    this.setBossSpawned(false);
                    this.setBossDefeated(false);
                    this.lastPlayerInRange = false;
                    this.playerCheated = false;
                }
            }

            this.playerInRange = !entitiesWithin.isEmpty();

            if (this.playerInRange && !this.lastPlayerInRange)
            {
                if (this.getBoss() != null && !this.getBossSpawned())
                {
                    if (this.boss instanceof Entity)
                    {
                        this.worldObj.spawnEntityInWorld((Entity) this.boss);
                        this.setBossSpawned(true);
                        this.boss.onBossSpawned(this);
                        ((GCCoreEntitySkeletonBoss) this.boss).setRoom(this.roomCoords, this.roomSize);
                    }
                }
            }

            this.lastPlayerInRange = this.playerInRange;
        }
    }

    public void setRoom(Vector3 coords, Vector3 size)
    {
        this.roomCoords = coords;
        this.roomSize = size;
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

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.setBossSpawned(nbt.getBoolean("spawned"));
        this.playerInRange = this.lastPlayerInRange = nbt.getBoolean("playerInRange");
        this.setBossDefeated(nbt.getBoolean("defeated"));
        this.playerCheated = nbt.getBoolean("playerCheated");
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

        nbt.setBoolean("spawned", this.getBossSpawned());
        nbt.setBoolean("playerInRange", this.playerInRange);
        nbt.setBoolean("defeated", this.getBossDefeated());
        nbt.setBoolean("playerCheated", this.playerCheated);

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

    @Override
    public void setPlayerCheated()
    {
        this.playerCheated = true;
    }
}
