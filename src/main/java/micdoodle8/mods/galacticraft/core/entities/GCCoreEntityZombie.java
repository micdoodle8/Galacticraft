package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
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
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

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
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.96F);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
	}

	@Override
	public boolean canBreath()
	{
		return true;
	}
}
