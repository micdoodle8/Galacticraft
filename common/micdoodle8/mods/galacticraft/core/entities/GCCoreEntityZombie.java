package micdoodle8.mods.galacticraft.core.entities;

import java.util.Calendar;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntityZombie.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityZombie extends EntityZombie implements IEntityBreathable
{
	private int conversionTime = 0;

	public GCCoreEntityZombie(World par1World)
	{
		super(par1World);
		this.tasks.taskEntries.clear();
		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIBreakDoor(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.36F, false));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, 0.36F, true));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.36F));
		this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, 0.36F, false));
		this.tasks.addTask(6, new EntityAIWander(this, 0.36F));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.96F);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(16.0D);
	}

	@Override
	public void fall(float var1)
	{
		;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	@Override
	public int getTotalArmorValue()
	{
		int i = super.getTotalArmorValue() + 2;

		if (i > 20)
		{
			i = 20;
		}

		return i;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	@Override
	public boolean isChild()
	{
		return this.getDataWatcher().getWatchableObjectByte(12) == 1;
	}

	/**
	 * Set whether this zombie is a child.
	 */
	@Override
	public void setChild(boolean par1)
	{
		this.getDataWatcher().updateObject(12, Byte.valueOf((byte) 1));
	}

	/**
	 * Return whether this zombie is a villager.
	 */
	@Override
	public boolean isVillager()
	{
		return this.getDataWatcher().getWatchableObjectByte(13) == 1;
	}

	/**
	 * Set whether this zombie is a villager.
	 */
	@Override
	public void setVillager(boolean par1)
	{
		this.getDataWatcher().updateObject(13, Byte.valueOf((byte) (par1 ? 1 : 0)));
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
		{
			final float f = this.getBrightness(1.0F);
			float rand = this.rand.nextFloat() * 30.0F;

			if (f > 0.5F && rand < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
			{
				boolean flag = true;
				final ItemStack itemstack = this.getCurrentItemOrArmor(4);

				if (itemstack != null)
				{
					if (itemstack.isItemStackDamageable())
					{
						itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

						if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
						{
							this.renderBrokenItemStack(itemstack);
							this.setCurrentItemOrArmor(4, (ItemStack) null);
						}
					}

					flag = false;
				}

				if (flag)
				{
					this.setFire(8);
				}
			}
		}

		super.onLivingUpdate();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if (!this.worldObj.isRemote && this.isConverting())
		{
			final int i = this.getConversionTimeBoost();
			this.conversionTime -= i;

			if (this.conversionTime <= 0)
			{
				this.convertToVillager();
			}
		}

		super.onUpdate();
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		final boolean flag = super.attackEntityAsMob(par1Entity);

		if (flag && this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < this.worldObj.difficultySetting * 0.3F)
		{
			par1Entity.setFire(2 * this.worldObj.difficultySetting);
		}

		return flag;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.zombie.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.zombie.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.zombie.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	@Override
	protected void playStepSound(int par1, int par2, int par3, int par4)
	{
		this.playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected int getDropItemId()
	{
		return Item.rottenFlesh.itemID;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	protected void dropRareDrop(int par1)
	{
		switch (this.rand.nextInt(3))
		{
		case 0:
			this.dropItem(Item.ingotIron.itemID, 1);
			break;
		case 1:
			this.dropItem(Item.carrot.itemID, 1);
			break;
		case 2:
			this.dropItem(Item.potato.itemID, 1);
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	@Override
	protected void addRandomArmor()
	{
		super.addRandomArmor();

		if (this.rand.nextFloat() < (this.worldObj.difficultySetting == 3 ? 0.05F : 0.01F))
		{
			final int i = this.rand.nextInt(3);

			if (i == 0)
			{
				this.setCurrentItemOrArmor(0, new ItemStack(Item.swordIron));
			}
			else
			{
				this.setCurrentItemOrArmor(0, new ItemStack(Item.shovelIron));
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);

		if (this.isChild())
		{
			par1NBTTagCompound.setBoolean("IsBaby", true);
		}

		if (this.isVillager())
		{
			par1NBTTagCompound.setBoolean("IsVillager", true);
		}

		par1NBTTagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.getBoolean("IsBaby"))
		{
			this.setChild(true);
		}

		if (par1NBTTagCompound.getBoolean("IsVillager"))
		{
			this.setVillager(true);
		}

		if (par1NBTTagCompound.hasKey("ConversionTime") && par1NBTTagCompound.getInteger("ConversionTime") > -1)
		{
			this.startConversion(par1NBTTagCompound.getInteger("ConversionTime"));
		}
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	@Override
	public void onKillEntity(EntityLivingBase par1EntityLiving)
	{
		super.onKillEntity(par1EntityLiving);

		if (this.worldObj.difficultySetting >= 2 && par1EntityLiving instanceof EntityVillager)
		{
			if (this.worldObj.difficultySetting == 2 && this.rand.nextBoolean())
			{
				return;
			}

			final EntityZombie entityzombie = new EntityZombie(this.worldObj);
			entityzombie.copyLocationAndAnglesFrom(par1EntityLiving);
			this.worldObj.removeEntity(par1EntityLiving);
			entityzombie.onSpawnWithEgg((EntityLivingData) null);
			entityzombie.setVillager(true);

			if (par1EntityLiving.isChild())
			{
				entityzombie.setChild(true);
			}

			this.worldObj.spawnEntityInWorld(entityzombie);
			this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
		}
	}

	@Override
	public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData)
	{
		par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
		float f = this.worldObj.getLocationTensionFactor(this.posX, this.posY, this.posZ);
		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

		if (this.worldObj.rand.nextFloat() < 0.05F)
		{
			this.setVillager(true);
		}

		this.addRandomArmor();
		this.enchantEquipment();

		if (this.getCurrentItemOrArmor(4) == null)
		{
			Calendar calendar = this.worldObj.getCurrentDate();

			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
			{
				this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Block.pumpkinLantern : Block.pumpkin));
				this.equipmentDropChances[4] = 0.0F;
			}
		}

		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
		this.getEntityAttribute(SharedMonsterAttributes.followRange).removeModifier(new AttributeModifier("Random zombie-spawn bonus", this.rand.nextDouble() * 1.5D, 2));

		if (this.rand.nextFloat() < f * 0.05F)
		{
			this.getEntityAttribute(EntityZombie.field_110186_bp).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
		}

		return par1EntityLivingData;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		final ItemStack itemstack = par1EntityPlayer.getCurrentEquippedItem();

		if (itemstack != null && itemstack.getItem() == Item.appleGold && itemstack.getItemDamage() == 0 && this.isVillager() && this.isPotionActive(Potion.weakness))
		{
			if (!par1EntityPlayer.capabilities.isCreativeMode)
			{
				--itemstack.stackSize;
			}

			if (itemstack.stackSize <= 0)
			{
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack) null);
			}

			if (!this.worldObj.isRemote)
			{
				this.startConversion(this.rand.nextInt(2401) + 3600);
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Starts converting this zombie into a villager. The zombie converts into a
	 * villager after the specified time in ticks.
	 */
	@Override
	protected void startConversion(int par1)
	{
		this.conversionTime = par1;
		this.getDataWatcher().updateObject(14, Byte.valueOf((byte) 1));
		this.removePotionEffect(Potion.weakness.id);
		this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, par1, Math.min(this.worldObj.difficultySetting - 1, 0)));
		this.worldObj.setEntityState(this, (byte) 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte par1)
	{
		if (par1 == 16)
		{
			this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
		}
		else
		{
			super.handleHealthUpdate(par1);
		}
	}

	/**
	 * Returns whether this zombie is in the process of converting to a villager
	 */
	@Override
	public boolean isConverting()
	{
		return this.getDataWatcher().getWatchableObjectByte(14) == 1;
	}

	/**
	 * Convert this zombie into a villager.
	 */
	@Override
	protected void convertToVillager()
	{
		final EntityVillager entityvillager = new EntityVillager(this.worldObj);
		entityvillager.copyLocationAndAnglesFrom(this);
		entityvillager.onSpawnWithEgg((EntityLivingData) null);
		entityvillager.func_82187_q();

		if (this.isChild())
		{
			entityvillager.setGrowingAge(-24000);
		}

		this.worldObj.removeEntity(this);
		this.worldObj.spawnEntityInWorld(entityvillager);
		entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
		this.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1017, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
	}

	/**
	 * Return the amount of time decremented from conversionTime every tick.
	 */
	@Override
	protected int getConversionTimeBoost()
	{
		int i = 1;

		if (this.rand.nextFloat() < 0.01F)
		{
			int j = 0;

			for (int k = (int) this.posX - 4; k < (int) this.posX + 4 && j < 14; ++k)
			{
				for (int l = (int) this.posY - 4; l < (int) this.posY + 4 && j < 14; ++l)
				{
					for (int i1 = (int) this.posZ - 4; i1 < (int) this.posZ + 4 && j < 14; ++i1)
					{
						final int j1 = this.worldObj.getBlockId(k, l, i1);

						if (j1 == Block.fenceIron.blockID || j1 == Block.bed.blockID)
						{
							if (this.rand.nextFloat() < 0.3F)
							{
								++i;
							}

							++j;
						}
					}
				}
			}
		}

		return i;
	}

	@Override
	public boolean canBreath()
	{
		return true;
	}
}
