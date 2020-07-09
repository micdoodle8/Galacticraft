package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TileEntityDungeonSpawner<E extends Entity> extends TileEntityAdvanced
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.bossSpawner)
    public static TileEntityType<TileEntityDungeonSpawner<?>> TYPE;

    public Class<E> bossClass;
    public IBoss boss;
    public boolean spawned;
    public boolean isBossDefeated;
    public boolean playerInRange;
    public boolean lastPlayerInRange;
    private Vector3 roomCoords;
    private Vector3 roomSize;
    public long lastKillTime;
    private BlockPos chestPos;
    private AxisAlignedBB range15 = null;
    private AxisAlignedBB rangeBounds = null;
    private AxisAlignedBB rangeBoundsPlus3 = null;
    private AxisAlignedBB rangeBoundsPlus11 = null;

    public TileEntityDungeonSpawner()
    {
        super(TYPE);
    }

    public TileEntityDungeonSpawner(TileEntityType<? extends TileEntityDungeonSpawner<E>> type, Class<E> bossClass)
    {
        super(type);
        this.bossClass = bossClass;
    }

    public TileEntityDungeonSpawner(Class<E> bossClass)
    {
        super(TYPE);
        this.bossClass = bossClass;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.roomCoords == null)
        {
            return;
        }

        if (!this.world.isRemote)
        {
            if (this.range15 == null)
            {
                final Vector3 thisVec = new Vector3(this);
                this.range15 = new AxisAlignedBB(thisVec.x - 15, thisVec.y - 15, thisVec.z - 15, thisVec.x + 15, thisVec.y + 15, thisVec.z + 15);
                this.rangeBounds = new AxisAlignedBB(this.roomCoords.intX(), this.roomCoords.intY(), this.roomCoords.intZ(), this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ());
                this.rangeBoundsPlus3 = this.rangeBounds.grow(3, 3, 3);
            }

            if (this.lastKillTime > 0 && Util.milliTime() - lastKillTime > 900000) // 15 minutes
            {
                this.lastKillTime = 0;
                this.isBossDefeated = false;
                //After 15 minutes a new boss is able to be spawned 
            }

            final List<E> l = this.world.getEntitiesWithinAABB(bossClass, this.range15);

            for (final Entity e : l)
            {
                if (e.isAlive())
                {
                    this.boss = (IBoss) e;
                    this.spawned = true;
                    this.isBossDefeated = false;
                    this.boss.onBossSpawned(this);
                }
            }

            List<MonsterEntity> entitiesWithin = this.world.getEntitiesWithinAABB(MonsterEntity.class, this.rangeBoundsPlus3);

            for (Entity mob : entitiesWithin)
            {
                if (this.getDisabledCreatures().contains(mob.getClass()))
                {
                    mob.remove();
                }
            }

            List<PlayerEntity> playersWithin = this.world.getEntitiesWithinAABB(PlayerEntity.class, this.rangeBounds);

            this.playerInRange = !playersWithin.isEmpty();

            if (this.playerInRange)
            {
                if (!this.lastPlayerInRange && !this.spawned)
                {
                    //Try to create a boss entity
                    if (this.boss == null && !this.isBossDefeated)
                    {
                        try
                        {
                            Constructor<?> c = this.bossClass.getConstructor(World.class);
                            this.boss = (IBoss) c.newInstance(this.world);
                            ((Entity) this.boss).setPosition(this.getPos().getX() + 0.5, this.getPos().getY() + 1.0, this.getPos().getZ() + 0.5);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    //Now spawn the boss
                    if (this.boss != null)
                    {
                        if (this.boss instanceof MobEntity)
                        {
                            MobEntity bossLiving = (MobEntity) this.boss;
                            bossLiving.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(bossLiving)), SpawnReason.SPAWNER, (ILivingEntityData)null, (CompoundNBT)null);
                            this.world.addEntity(bossLiving);
                            this.playSpawnSound(bossLiving);
                            this.spawned = true;
                        }
                    }
                }
            }

            this.lastPlayerInRange = this.playerInRange;
        }
    }

    public void playSpawnSound(Entity entity)
    {

    }

    public List<Class<? extends MobEntity>> getDisabledCreatures()
    {
        List<Class<? extends MobEntity>> list = new ArrayList<Class<? extends MobEntity>>();
        list.add(EntityEvolvedSkeleton.class);
        list.add(EntityEvolvedCreeper.class);
        list.add(EntityEvolvedZombie.class);
        list.add(EntityEvolvedSpider.class);
        return list;
    }

    public void setRoom(Vector3 coords, Vector3 size)
    {
        this.roomCoords = coords;
        this.roomSize = size;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        this.playerInRange = this.lastPlayerInRange = nbt.getBoolean("playerInRange");
        this.isBossDefeated = nbt.getBoolean("defeated");

        try
        {
            this.bossClass = (Class<E>) Class.forName(nbt.getString("bossClass"));
        }
        catch (Exception e)
        {
            // This exception will be thrown when read is called from TileEntity.handleUpdateTag
            // but we only care if an exception is thrown on server LogicalSide read
            if (!this.world.isRemote)
            {
                e.printStackTrace();
            }
        }

        this.roomCoords = new Vector3();
        this.roomCoords.x = nbt.getFloat("roomCoordsX");
        this.roomCoords.y = nbt.getFloat("roomCoordsY");
        this.roomCoords.z = nbt.getFloat("roomCoordsZ");
        this.roomSize = new Vector3();
        this.roomSize.x = nbt.getFloat("roomSizeX");
        this.roomSize.y = nbt.getFloat("roomSizeY");
        this.roomSize.z = nbt.getFloat("roomSizeZ");

        if (nbt.contains("lastKillTime"))
        {
            this.lastKillTime = nbt.getLong("lastKillTime");
        }
        else if (nbt.contains("lastKillTimeNew"))
        {
            long savedTime = nbt.getLong("lastKillTimeNew");
            this.lastKillTime = savedTime == 0 ? 0 : savedTime + Util.milliTime();
        }


        if (nbt.contains("chestPosNull") && !nbt.getBoolean("chestPosNull"))
        {
            this.chestPos = new BlockPos(nbt.getInt("chestX"), nbt.getInt("chestY"), nbt.getInt("chestZ"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        nbt.putBoolean("playerInRange", this.playerInRange);
        nbt.putBoolean("defeated", this.isBossDefeated);
        nbt.putString("bossClass", this.bossClass.getCanonicalName());

        if (this.roomCoords != null)
        {
            nbt.putDouble("roomCoordsX", this.roomCoords.x);
            nbt.putDouble("roomCoordsY", this.roomCoords.y);
            nbt.putDouble("roomCoordsZ", this.roomCoords.z);
            nbt.putDouble("roomSizeX", this.roomSize.x);
            nbt.putDouble("roomSizeY", this.roomSize.y);
            nbt.putDouble("roomSizeZ", this.roomSize.z);
        }

        nbt.putLong("lastKillTimeNew", this.lastKillTime == 0 ? 0 : this.lastKillTime - Util.milliTime());

        nbt.putBoolean("chestPosNull", this.chestPos == null);
        if (this.chestPos != null)
        {
            nbt.putInt("chestX", this.chestPos.getX());
            nbt.putInt("chestY", this.chestPos.getY());
            nbt.putInt("chestZ", this.chestPos.getZ());
        }
        return nbt;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
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

    public BlockPos getChestPos()
    {
        return chestPos;
    }

    public void setChestPos(BlockPos chestPos)
    {
        this.chestPos = chestPos;
    }
    
    public AxisAlignedBB getRangeBounds()
    {
        if (this.rangeBounds == null)
            this.rangeBounds = new AxisAlignedBB(this.roomCoords.intX(), this.roomCoords.intY(), this.roomCoords.intZ(), this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ());

        return this.rangeBounds;
    }

    public AxisAlignedBB getRangeBoundsPlus11()
    {
        if (this.rangeBoundsPlus11 == null)
            this.rangeBoundsPlus11 = this.getRangeBounds().grow(11, 11, 11);

        return this.rangeBoundsPlus11;
    }
}
