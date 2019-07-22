package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TileEntityDungeonSpawner<E extends Entity> extends TileEntityAdvanced
{
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
        this(null);
    }

    public TileEntityDungeonSpawner(Class<E> bossClass)
    {
        super("tile.gcdungeonspawner.name");
        this.bossClass = bossClass;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public void update()
    {
        super.update();

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

            if (this.lastKillTime > 0 && MinecraftServer.getCurrentTimeMillis() - lastKillTime > 900000) // 15 minutes
            {
                this.lastKillTime = 0;
                this.isBossDefeated = false;
                //After 15 minutes a new boss is able to be spawned 
            }

            final List<E> l = this.world.getEntitiesWithinAABB(bossClass, this.range15);

            for (final Entity e : l)
            {
                if (!e.isDead)
                {
                    this.boss = (IBoss) e;
                    this.spawned = true;
                    this.isBossDefeated = false;
                    this.boss.onBossSpawned(this);
                }
            }

            List<EntityMob> entitiesWithin = this.world.getEntitiesWithinAABB(EntityMob.class, this.rangeBoundsPlus3);

            for (Entity mob : entitiesWithin)
            {
                if (this.getDisabledCreatures().contains(mob.getClass()))
                {
                    mob.setDead();
                }
            }

            List<EntityPlayer> playersWithin = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.rangeBounds);

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
                        if (this.boss instanceof EntityLiving)
                        {
                            EntityLiving bossLiving = (EntityLiving) this.boss;
                            bossLiving.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(bossLiving)), null);
                            this.world.spawnEntity(bossLiving);
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

    public List<Class<? extends EntityLiving>> getDisabledCreatures()
    {
        List<Class<? extends EntityLiving>> list = new ArrayList<Class<? extends EntityLiving>>();
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
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.playerInRange = this.lastPlayerInRange = nbt.getBoolean("playerInRange");
        this.isBossDefeated = nbt.getBoolean("defeated");

        try
        {
            this.bossClass = (Class<E>) Class.forName(nbt.getString("bossClass"));
        }
        catch (Exception e)
        {
            // This exception will be thrown when read is called from TileEntity.handleUpdateTag
            // but we only care if an exception is thrown on server side read
            if (!this.world.isRemote)
            {
                e.printStackTrace();
            }
        }

        this.roomCoords = new Vector3();
        this.roomCoords.x = nbt.getDouble("roomCoordsX");
        this.roomCoords.y = nbt.getDouble("roomCoordsY");
        this.roomCoords.z = nbt.getDouble("roomCoordsZ");
        this.roomSize = new Vector3();
        this.roomSize.x = nbt.getDouble("roomSizeX");
        this.roomSize.y = nbt.getDouble("roomSizeY");
        this.roomSize.z = nbt.getDouble("roomSizeZ");

        if (nbt.hasKey("lastKillTime"))
        {
            this.lastKillTime = nbt.getLong("lastKillTime");
        }
        else if (nbt.hasKey("lastKillTimeNew"))
        {
            long savedTime = nbt.getLong("lastKillTimeNew");
            this.lastKillTime = savedTime == 0 ? 0 : savedTime + MinecraftServer.getCurrentTimeMillis();
        }


        if (nbt.hasKey("chestPosNull") && !nbt.getBoolean("chestPosNull"))
        {
            this.chestPos = new BlockPos(nbt.getInteger("chestX"), nbt.getInteger("chestY"), nbt.getInteger("chestZ"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean("playerInRange", this.playerInRange);
        nbt.setBoolean("defeated", this.isBossDefeated);
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

        nbt.setLong("lastKillTimeNew", this.lastKillTime == 0 ? 0 : this.lastKillTime - MinecraftServer.getCurrentTimeMillis());

        nbt.setBoolean("chestPosNull", this.chestPos == null);
        if (this.chestPos != null)
        {
            nbt.setInteger("chestX", this.chestPos.getX());
            nbt.setInteger("chestY", this.chestPos.getY());
            nbt.setInteger("chestZ", this.chestPos.getZ());
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
