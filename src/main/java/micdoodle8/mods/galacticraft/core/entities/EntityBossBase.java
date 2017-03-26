package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;
import java.util.Random;

public abstract class EntityBossBase extends EntityMob implements IBoss
{
    private TileEntityDungeonSpawner spawner;
    public int deathTicks = 0;

    protected Vector3 roomCoords;
    protected Vector3 roomSize;

    public int entitiesWithin;
    public int entitiesWithinLast;

    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), getHealthBarColor(), BossInfo.Overlay.PROGRESS));

    public EntityBossBase(World world)
    {
        super(world);
    }

    public abstract int getChestTier();

    public abstract ItemStack getGuaranteedLoot(Random rand);

    public abstract void dropKey();

    public abstract BossInfo.Color getHealthBarColor();

    @Override
    protected void updateAITasks()
    {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        super.updateAITasks();
    }

    @Override
    protected void onDeathUpdate()
    {
        ++this.deathTicks;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            final float x = (this.rand.nextFloat() - 0.5F) * this.width;
            final float y = (this.rand.nextFloat() - 0.5F) * (this.height / 2.0F);
            final float z = (this.rand.nextFloat() - 0.5F) * this.width;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + x, this.posY + 2.0D + y, this.posZ + z, 0.0D, 0.0D, 0.0D);
        }

        int i;
        int j;

        if (!this.worldObj.isRemote)
        {
            if (this.deathTicks >= 180 && this.deathTicks % 5 == 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_PLAY_SOUND_EXPLODE, GCCoreUtil.getDimensionID(this.worldObj), new Object[] {}), new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(this.worldObj), this.posX, this.posY, this.posZ, 40.0D));
            }

            if (this.deathTicks > 150 && this.deathTicks % 5 == 0)
            {
                i = 30;

                while (i > 0)
                {
                    j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.worldObj.spawnEntity(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
                }
            }
        }

        if (this.deathTicks == 200 && !this.worldObj.isRemote)
        {
            i = 20;

            while (i > 0)
            {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.worldObj.spawnEntity(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }

            TileEntityTreasureChest chest = null;

            if (this.spawner != null && this.spawner.getChestPos() != null)
            {
                TileEntity chestTest = this.worldObj.getTileEntity(this.spawner.getChestPos());

                if (chestTest != null && chestTest instanceof TileEntityTreasureChest)
                {
                    chest = (TileEntityTreasureChest) chestTest;
                }
            }

            if (chest == null)
            {
                // Fallback to finding closest chest
                chest = TileEntityTreasureChest.findClosest(this, this.getChestTier());
            }

            if (chest != null)
            {
                double dist = this.getDistanceSq(chest.getPos().getX() + 0.5, chest.getPos().getY() + 0.5, chest.getPos().getZ() + 0.5);
                if (dist < 1000 * 1000)
                {
                    if (!chest.locked)
                    {
                        chest.locked = true;
                    }

                    for (int k = 0; k < chest.getSizeInventory(); k++)
                    {
                        chest.setInventorySlotContents(k, null);
                    }

                    chest.fillWithLoot(null);

//                    ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
//
//                    // Generate twice, since it's an extra special chest
//                    WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));
//                    WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));

                    ItemStack schematic = this.getGuaranteedLoot(this.rand);
                    int slot = this.rand.nextInt(chest.getSizeInventory());
                    chest.setInventorySlotContents(slot, schematic);
                }
            }

            this.dropKey();

            super.setDead();

            if (this.spawner != null)
            {
                this.spawner.isBossDefeated = true;
                this.spawner.boss = null;
                this.spawner.spawned = false;

                if (!this.worldObj.isRemote)
                {
                    this.spawner.lastKillTime = MinecraftServer.getCurrentTimeMillis();
                }
            }
        }
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.roomCoords != null && this.roomSize != null)
        {
            List<EntityPlayer> playersWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
                    new AxisAlignedBB(
                            this.roomCoords.intX(),
                            this.roomCoords.intY(),
                            this.roomCoords.intZ(),
                            this.roomCoords.intX() + this.roomSize.intX(),
                            this.roomCoords.intY() + this.roomSize.intY(),
                            this.roomCoords.intZ() + this.roomSize.intZ()));

            this.entitiesWithin = playersWithin.size();

            if (this.entitiesWithin == 0 && this.entitiesWithinLast != 0)
            {
                List<EntityPlayer> entitiesWithin2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.roomCoords.intX() - 11, this.roomCoords.intY() - 11, this.roomCoords.intZ() - 11, this.roomCoords.intX() + this.roomSize.intX() + 10, this.roomCoords.intY() + this.roomSize.intY() + 10, this.roomCoords.intZ() + this.roomSize.intZ() + 10));

                for (EntityPlayer p : entitiesWithin2)
                {
                    p.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.skeleton_boss.message")));
                }

                this.setDead();

                if (this.spawner != null)
                {
                    this.spawner.playerCheated = true;
                }

                return;
            }

            this.entitiesWithinLast = this.entitiesWithin;
        }

        super.onLivingUpdate();
    }

    @Override
    public void setDead()
    {
        if (this.spawner != null)
        {
            this.spawner.isBossDefeated = false;
            this.spawner.boss = null;
            this.spawner.spawned = false;
        }

        super.setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);

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
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
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
    public void setRoom(Vector3 roomCoords, Vector3 roomSize)
    {
        this.roomCoords = roomCoords;
        this.roomSize = roomSize;
    }

    @Override
    public void onBossSpawned(TileEntityDungeonSpawner spawner)
    {
        this.spawner = spawner;
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
