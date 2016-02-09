package micdoodle8.mods.galacticraft.planets.mars.entities;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.EntityAIArrowAttack;
import micdoodle8.mods.galacticraft.core.entities.IBoss;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

import java.util.List;
import java.util.Random;

public class EntityCreeperBoss extends EntityMob implements IEntityBreathable, IBossDisplayData, IRangedAttackMob, IBoss
{
    protected long ticks = 0;
    private TileEntityDungeonSpawner spawner;

    public int headsRemaining = 3;
    public Entity targetEntity;
    public int deathTicks = 0;

    public int entitiesWithin;
    public int entitiesWithinLast;

    private Vector3 roomCoords;
    private Vector3 roomSize;

    public EntityCreeperBoss(World par1World)
    {
        super(par1World);
        this.setSize(2.0F, 7.0F);
        this.isImmuneToFire = true;
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 25, 20.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {
        if (damageSource.getDamageType().equals("fireball"))
        {
            if (this.isEntityInvulnerable())
            {
                return false;
            }
            else if (super.attackEntityFrom(damageSource, damage))
            {
                Entity entity = damageSource.getEntity();

                if (this.riddenByEntity != entity && this.ridingEntity != entity)
                {
                    if (entity != this)
                    {
                        this.entityToAttack = entity;
                    }

                    return true;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0F * ConfigManagerCore.dungeonBossHealthMod);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.05F);
    }

    public EntityCreeperBoss(World world, Vector3 vec)
    {
        this(world);
        this.setPosition(vec.x, vec.y, vec.z);
    }

    @Override
    public void knockBack(Entity par1Entity, float par2, double par3, double par5)
    {
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected String getLivingSound()
    {
        return null;
    }

    @Override
    protected String getHurtSound()
    {
        this.playSound(GalacticraftCore.TEXTURE_PREFIX + "entity.bossliving", this.getSoundVolume(), this.getSoundPitch() + 6.0F);
        return null;
    }

    @Override
    protected String getDeathSound()
    {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onDeathUpdate()
    {
        ++this.deathTicks;

        this.headsRemaining = 0;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            final float f = (this.rand.nextFloat() - 0.5F) * 1.5F;
            final float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F;
            final float f2 = (this.rand.nextFloat() - 0.5F) * 1.5F;
            this.worldObj.spawnParticle("hugeexplosion", this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        int i;
        int j;

        if (!this.worldObj.isRemote)
        {
            if (this.deathTicks >= 180 && this.deathTicks % 5 == 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_EXPLODE, new Object[] { }), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 40.0D));
                //				PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 40.0, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.PLAY_SOUND_EXPLODE, new Object[] { 0 }));
            }

            if (this.deathTicks > 150 && this.deathTicks % 5 == 0)
            {
                i = 30;

                while (i > 0)
                {
                    j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
                }
            }

            if (this.deathTicks == 1)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, new Object[] {}), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 40.0D));
                //				PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 40.0, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.PLAY_SOUND_BOSS_DEATH, new Object[] { 0 }));
            }
        }

        this.moveEntity(0.0D, 0.10000000149011612D, 0.0D);
        this.renderYawOffset = this.rotationYaw += 20.0F;

        if (this.deathTicks == 200 && !this.worldObj.isRemote)
        {
            i = 20;

            while (i > 0)
            {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }

            for (final TileEntity tile : (List<TileEntity>) this.worldObj.loadedTileEntityList)
            {
                if (tile instanceof TileEntityTreasureChestMars)
                {
                	final double d3 = tile.xCoord + 0.5D - this.posX;
                    final double d4 = tile.yCoord + 0.5D - this.posY;
                    final double d5 = tile.zCoord + 0.5D - this.posZ;
                    final double dSq = d3 * d3 + d4 * d4 + d5 * d5;
                    TileEntityTreasureChestMars chest = (TileEntityTreasureChestMars) tile; 

                    if (dSq < 10000)
                    {
                    	if (!chest.locked)
                        {
                            chest.locked = true;
                        }

                        for (int k = 0; k < chest.getSizeInventory(); k++)
                        {
                            chest.setInventorySlotContents(k, null);
                        }

                        ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);

                        // Generate three times, since it's an extra extra special chest
                        WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));
                        WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));
                        WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), chest, info.getCount(this.rand));

                        chest.setInventorySlotContents(this.rand.nextInt(chest.getSizeInventory()), this.getGuaranteedLoot(this.rand));

                        break;
                    }
                }
            }

            this.entityDropItem(new ItemStack(MarsItems.key, 1, 0), 0.5F);

            super.setDead();

            if (this.spawner != null)
            {
                this.spawner.isBossDefeated = true;
                this.spawner.boss = null;
                this.spawner.spawned = false;
            }
        }
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
    public void onLivingUpdate()
    {
        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 1;
        }

        this.ticks++;

        if (this.getHealth() <= 0)
        {
            this.headsRemaining = 0;
        }
        else if (this.getHealth() <= this.getMaxHealth() / 3.0)
        {
            this.headsRemaining = 1;
        }
        else if (this.getHealth() <= 2 * (this.getMaxHealth() / 3.0))
        {
            this.headsRemaining = 2;
        }

        final EntityPlayer player = this.worldObj.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0);

        if (player != null && !player.equals(this.targetEntity))
        {
            if (this.getDistanceSqToEntity(player) < 400.0D)
            {
                this.getNavigator().getPathToEntityLiving(player);
                this.targetEntity = player;
            }
        }
        else
        {
            this.targetEntity = null;
        }

        new Vector3(this);

        if (this.roomCoords != null && this.roomSize != null)
        {
            @SuppressWarnings("unchecked")
            List<Entity> entitiesWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.roomCoords.intX() - 1, this.roomCoords.intY() - 1, this.roomCoords.intZ() - 1, this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ()));

            this.entitiesWithin = entitiesWithin.size();

            if (this.entitiesWithin == 0 && this.entitiesWithinLast != 0)
            {
                @SuppressWarnings("unchecked")
                List<EntityPlayer> entitiesWithin2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.roomCoords.intX() - 11, this.roomCoords.intY() - 11, this.roomCoords.intZ() - 11, this.roomCoords.intX() + this.roomSize.intX() + 10, this.roomCoords.intY() + this.roomSize.intY() + 10, this.roomCoords.intZ() + this.roomSize.intZ() + 10));

                for (EntityPlayer p : entitiesWithin2)
                {
                    p.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.skeletonBoss.message")));
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
    protected Item getDropItem()
    {
        return Items.arrow;
    }

    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
    }

    @Override
    public EntityItem entityDropItem(ItemStack par1ItemStack, float par2)
    {
        final EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + par2, this.posZ, par1ItemStack);
        entityitem.motionY = -2.0D;
        entityitem.delayBeforeCanPickup = 10;
        if (this.captureDrops)
        {
            this.capturedDrops.add(entityitem);
        }
        else
        {
            this.worldObj.spawnEntityInWorld(entityitem);
        }
        return entityitem;
    }

    @Override
    protected void dropRareDrop(int par1)
    {
        if (par1 > 0)
        {
            final ItemStack var2 = new ItemStack(Items.bow);
            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5);
            this.entityDropItem(var2, 0.0F);
        }
        else
        {
            this.dropItem(Items.bow, 1);
        }
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(2);
        int range = 2;
        //If player seems to have Tier 3 rocket already then add Astro Miner to the loot
        final EntityPlayer player = this.worldObj.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0);
        if (player != null)
        {
	        GCPlayerStats stats = GCPlayerStats.get((EntityPlayerMP) player);
	        if (stats != null)
	        {
		        for (ISchematicPage page : stats.unlockedSchematics)
		        {
		        	if (page.getPageID() == ConfigManagerAsteroids.idSchematicRocketT3)
		        	{
		        		range = 3;
		        		break;
		        	}
		        }
		        if (stats.rocketItem == AsteroidsItems.tier3Rocket)
		        	range = 3;
	        }
	        if (range == 2)
	        {
	        	SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(player.getGameProfile().getName());
	        	if (race != null && race.getCelestialBodyStatusList().containsKey(AsteroidsModule.planetAsteroids))
	        		range = 3;        	
	        }
        }
        return stackList.get(rand.nextInt(range)).copy();
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

    private void func_82216_a(int par1, EntityLivingBase par2EntityLivingBase)
    {
        this.func_82209_a(par1, par2EntityLivingBase.posX, par2EntityLivingBase.posY + par2EntityLivingBase.getEyeHeight() * 0.5D, par2EntityLivingBase.posZ);
    }

    private void func_82209_a(int par1, double par2, double par4, double par6)
    {
        this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1014, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
        double d3 = this.func_82214_u(par1);
        double d4 = this.func_82208_v(par1);
        double d5 = this.func_82213_w(par1);
        double d6 = par2 - d3;
        double d7 = par4 - d4;
        double d8 = par6 - d5;
        EntityProjectileTNT entitywitherskull = new EntityProjectileTNT(this.worldObj, this, d6 * 0.5D, d7 * 0.5D, d8 * 0.5D);

        entitywitherskull.posY = d4;
        entitywitherskull.posX = d3;
        entitywitherskull.posZ = d5;
        this.worldObj.spawnEntityInWorld(entitywitherskull);
    }

    private double func_82214_u(int par1)
    {
        if (par1 <= 0)
        {
            return this.posX;
        }
        else
        {
            float f = (this.renderYawOffset + 180 * (par1 - 1)) / 180.0F * (float) Math.PI;
            float f1 = MathHelper.cos(f);
            return this.posX + f1 * 1.3D;
        }
    }

    private double func_82208_v(int par1)
    {
        return par1 <= 0 ? this.posY + 6.0D : this.posY + 4.2D;
    }

    private double func_82213_w(int par1)
    {
        if (par1 <= 0)
        {
            return this.posZ;
        }
        else
        {
            float f = (this.renderYawOffset + 180 * (par1 - 1)) / 180.0F * (float) Math.PI;
            float f1 = MathHelper.sin(f);
            return this.posZ + f1 * 1.3D;
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f)
    {
        this.func_82216_a(0, entitylivingbase);
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
}
