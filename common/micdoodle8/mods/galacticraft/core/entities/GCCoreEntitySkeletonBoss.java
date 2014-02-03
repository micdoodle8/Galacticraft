package micdoodle8.mods.galacticraft.core.entities;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntitySkeletonBoss.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntitySkeletonBoss extends EntityMob implements IEntityBreathable, IBossDisplayData, IRangedAttackMob, IBoss
{
	protected long ticks = 0;
	private static final ItemStack defaultHeldItem = new ItemStack(Item.bow, 1);
	// private static final AttributeModifier skeleBossEnrage = new
	// AttributeModifier("Drinking speed penalty", 0.15D,
	// 0).func_111168_a(false);
	private GCCoreTileEntityDungeonSpawner spawner;

	public int throwTimer;
	public int postThrowDelay = 20;
	public Entity thrownEntity;
	public Entity targetEntity;
	public int deathTicks = 0;

	public int entitiesWithin;
	public int entitiesWithinLast;

	private Vector3 roomCoords;
	private Vector3 roomSize;

	public GCCoreEntitySkeletonBoss(World par1World)
	{
		super(par1World);
		this.setSize(1.5F, 4.0F);
		this.isImmuneToFire = true;
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new GCCoreEntityAIArrowAttack(this, 1.0D, 25, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(150.0F * GCCoreConfigManager.dungeonBossHealthMod);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25F);
	}

	public GCCoreEntitySkeletonBoss(World world, Vector3 vec)
	{
		this(world);
		this.setPosition(vec.x, vec.y, vec.z);
	}

	@Override
	public boolean isInWater()
	{
		return false;
	}

	@Override
	public boolean handleWaterMovement()
	{
		return false;
	}

	@Override
	public void updateRiderPosition()
	{
		if (this.riddenByEntity != null)
		{
			final double offsetX = Math.sin(this.rotationYaw * Math.PI / 180.0D);
			final double offsetZ = Math.cos(this.rotationYaw * Math.PI / 180.0D);
			final double offsetY = 2 * Math.cos((this.throwTimer + this.postThrowDelay) * 0.05F);

			this.riddenByEntity.setPosition(this.posX + offsetX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset() + offsetY, this.posZ + offsetZ);
		}
	}

	@Override
	public void knockBack(Entity par1Entity, float par2, double par3, double par5)
	{
		;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if (this.riddenByEntity == null && this.postThrowDelay == 0 && this.throwTimer == 0 && par1EntityPlayer.equals(this.targetEntity) && this.deathTicks == 0)
		{
			if (!this.worldObj.isRemote)
			{
				PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 40.0, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.PLAY_SOUND_BOSS_LAUGH, new Object[] { 0 }));
				par1EntityPlayer.mountEntity(this);
			}

			this.throwTimer = 40;
		}

		super.onCollideWithPlayer(par1EntityPlayer);
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
		return "";
	}

	@Override
	protected String getHurtSound()
	{
		this.playSound(GalacticraftCore.ASSET_PREFIX + "entity.bossliving", this.getSoundVolume(), this.getSoundPitch() + 6.0F);
		return "";
	}

	@Override
	protected String getDeathSound()
	{
		return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDeathUpdate()
	{
		++this.deathTicks;

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
				PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 40.0, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.PLAY_SOUND_EXPLODE, new Object[] { 0 }));
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
				PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 40.0, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.PLAY_SOUND_BOSS_DEATH, new Object[] { 0 }));
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
				if (tile instanceof GCCoreTileEntityTreasureChest)
				{
					final double d3 = tile.xCoord + 0.5D - this.posX;
					final double d4 = tile.yCoord + 0.5D - this.posY;
					final double d5 = tile.zCoord + 0.5D - this.posZ;
					final double dSq = d3 * d3 + d4 * d4 + d5 * d5;

					if (dSq < Math.pow(100.0D, 2))
					{
						if (!((GCCoreTileEntityTreasureChest) tile).locked)
						{
							((GCCoreTileEntityTreasureChest) tile).locked = true;
						}

						for (int k = 0; k < ((GCCoreTileEntityTreasureChest) tile).getSizeInventory(); k++)
						{
							((GCCoreTileEntityTreasureChest) tile).setInventorySlotContents(k, null);
						}

						ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);

						// Generate twice, since it's an extra special chest
						WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), (GCCoreTileEntityTreasureChest) tile, info.getCount(this.rand));
						WeightedRandomChestContent.generateChestContents(this.rand, info.getItems(this.rand), (GCCoreTileEntityTreasureChest) tile, info.getCount(this.rand));

						((GCCoreTileEntityTreasureChest) tile).setInventorySlotContents(this.rand.nextInt(((GCCoreTileEntityTreasureChest) tile).getSizeInventory()), this.getGuaranteedLoot(this.rand));

						break;
					}
				}
			}

			this.entityDropItem(new ItemStack(GCCoreItems.key.itemID, 1, 0), 0.5F);

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
	@SideOnly(Side.CLIENT)
	public ItemStack getHeldItem()
	{
		return GCCoreEntitySkeletonBoss.defaultHeldItem;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
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

		if (!this.worldObj.isRemote && this.getHealth() <= 150.0F * GCCoreConfigManager.dungeonBossHealthMod / 2)
		{
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
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

		// final Vector3 thisVec = new Vector3(this);
		// final List l =
		// this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
		// AxisAlignedBB.getBoundingBox(thisVec.x - 10, thisVec.y - 10,
		// thisVec.z - 10, thisVec.x + 10, thisVec.y + 10, thisVec.z + 10));
		//
		// for (final Entity e : (List<Entity>) l)
		// {
		// if (e instanceof GCCoreEntitySkeletonBoss)
		// {
		// if (((GCCoreEntitySkeletonBoss) e).getHealth() >= this.health)
		// {
		// ((GCCoreEntitySkeletonBoss) e).setDead();
		// }
		// }
		// }

		if (this.throwTimer > 0)
		{
			this.throwTimer--;
		}

		if (this.postThrowDelay > 0)
		{
			this.postThrowDelay--;
		}

		new Vector3(this);

		if (this.roomCoords != null && this.roomSize != null)
		{
			@SuppressWarnings("unchecked")
			List<Entity> entitiesWithin = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB(this.roomCoords.intX() - 1, this.roomCoords.intY() - 1, this.roomCoords.intZ() - 1, this.roomCoords.intX() + this.roomSize.intX(), this.roomCoords.intY() + this.roomSize.intY(), this.roomCoords.intZ() + this.roomSize.intZ()));

			this.entitiesWithin = entitiesWithin.size();

			if (this.entitiesWithin == 0 && this.entitiesWithinLast != 0)
			{
				@SuppressWarnings("unchecked")
				List<EntityPlayer> entitiesWithin2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB(this.roomCoords.intX() - 11, this.roomCoords.intY() - 11, this.roomCoords.intZ() - 11, this.roomCoords.intX() + this.roomSize.intX() + 10, this.roomCoords.intY() + this.roomSize.intY() + 10, this.roomCoords.intZ() + this.roomSize.intZ() + 10));

				for (EntityPlayer p : entitiesWithin2)
				{
					p.sendChatToPlayer(ChatMessageComponent.createFromText("Boss despawned, don't leave the boss room while fighting! Re-enter room to respawn boss."));
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

		if (this.riddenByEntity != null && this.throwTimer == 0)
		{
			this.postThrowDelay = 20;

			this.thrownEntity = this.riddenByEntity;

			if (!this.worldObj.isRemote)
			{
				this.riddenByEntity.mountEntity(null);
			}
		}

		if (this.thrownEntity != null && this.postThrowDelay == 18)
		{
			double d0 = this.posX - this.thrownEntity.posX;
			double d1;

			for (d1 = this.posZ - this.thrownEntity.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
			{
				d0 = (Math.random() - Math.random()) * 0.01D;
			}

			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 40.0, this.worldObj.provider.dimensionId, PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.PLAY_SOUND_BOW, new Object[] { 0 }));

			((EntityPlayer) this.thrownEntity).attackedAtYaw = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - this.rotationYaw;

			this.thrownEntity.isAirBorne = true;
			final float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
			final float f1 = 2.4F;
			this.thrownEntity.motionX /= 2.0D;
			this.thrownEntity.motionY /= 2.0D;
			this.thrownEntity.motionZ /= 2.0D;
			this.thrownEntity.motionX -= d0 / f * f1;
			this.thrownEntity.motionY += (double) f1 / 5;
			this.thrownEntity.motionZ -= d1 / f * f1;

			if (this.thrownEntity.motionY > 0.4000000059604645D)
			{
				this.thrownEntity.motionY = 0.4000000059604645D;
			}
		}

		// if (this.ticks % 5 == 0)
		// {
		// PacketManager.sendPacketToClients(this.getDescriptionPacket(),
		// this.worldObj, new Vector3(this), 100);
		// }

		super.onLivingUpdate();
	}

	// public Packet getDescriptionPacket()
	// {
	// return
	// GCCoreGCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES,
	// this, this.health);
	// }
	//
	// @Override
	// public void handlePacketData(INetworkManager network, int packetType,
	// Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput
	// dataStream)
	// {
	// try
	// {
	// if (this.worldObj.isRemote)
	// {
	// this.health = dataStream.readFloat();
	// }
	// }
	// catch (final Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void onDeath(DamageSource par1DamageSource)
	{
		super.onDeath(par1DamageSource);

		if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow && par1DamageSource.getEntity() instanceof EntityPlayer)
		{
			final EntityPlayer var2 = (EntityPlayer) par1DamageSource.getEntity();
			final double var3 = var2.posX - this.posX;
			final double var5 = var2.posZ - this.posZ;

			if (var3 * var3 + var5 * var5 >= 2500.0D)
			{
				var2.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
	}

	@Override
	protected int getDropItemId()
	{
		return Item.arrow.itemID;
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
			final ItemStack var2 = new ItemStack(Item.bow);
			EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5);
			this.entityDropItem(var2, 0.0F);
		}
		else
		{
			this.dropItem(Item.bow.itemID, 1);
		}
	}

	@Override
	public boolean canBreath()
	{
		return true;
	}

	public float getExperienceToSpawn()
	{
		return 50.0F;
	}

	public double getDistanceToSpawn()
	{
		return 40.0D;
	}

	public ItemStack getGuaranteedLoot(Random rand)
	{
		List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(1);
		return stackList.get(rand.nextInt(stackList.size()));
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
	public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f)
	{
		if (this.riddenByEntity != null)
		{
			return;
		}

		Entity var1;

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			var1 = new GCCoreEntityArrow(this.worldObj, this, entitylivingbase, 0.3F, 12.0F);
		}
		else
		{
			var1 = new EntityArrow(this.worldObj, this, entitylivingbase, 1.6F, 12.0F);
		}

		this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(var1);
	}

	@Override
	public void setRoom(Vector3 roomCoords, Vector3 roomSize)
	{
		this.roomCoords = roomCoords;
		this.roomSize = roomSize;
	}

	@Override
	public void onBossSpawned(GCCoreTileEntityDungeonSpawner spawner)
	{
		this.spawner = spawner;
	}
}
