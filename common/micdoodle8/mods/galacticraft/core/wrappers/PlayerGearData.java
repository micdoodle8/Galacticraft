package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * PlayerGearData.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class PlayerGearData
{
	private final EntityPlayer player;
	private int mask;
	private int gear;
	private int leftTank;
	private int rightTank;
	private ResourceLocation parachute;
	private int frequencyModule;

	public PlayerGearData(EntityPlayer player)
	{
		this(player, -1, -1, -1, -1, null, -1);
	}

	public PlayerGearData(EntityPlayer player, int mask, int gear, int leftTank, int rightTank, ResourceLocation parachute, int frequencyModule)
	{
		this.player = player;
		this.mask = mask;
		this.gear = gear;
		this.leftTank = leftTank;
		this.rightTank = rightTank;
		this.parachute = parachute;
		this.frequencyModule = frequencyModule;
	}

	public int getMask()
	{
		return this.mask;
	}

	public void setMask(int mask)
	{
		this.mask = mask;
	}

	public int getGear()
	{
		return this.gear;
	}

	public void setGear(int gear)
	{
		this.gear = gear;
	}

	public int getLeftTank()
	{
		return this.leftTank;
	}

	public void setLeftTank(int leftTank)
	{
		this.leftTank = leftTank;
	}

	public int getRightTank()
	{
		return this.rightTank;
	}

	public void setRightTank(int rightTank)
	{
		this.rightTank = rightTank;
	}

	public EntityPlayer getPlayer()
	{
		return this.player;
	}

	public ResourceLocation getParachute()
	{
		return this.parachute;
	}

	public void setParachute(ResourceLocation parachute)
	{
		this.parachute = parachute;
	}

	public int getFrequencyModule()
	{
		return this.frequencyModule;
	}

	public void setFrequencyModule(int frequencyModule)
	{
		this.frequencyModule = frequencyModule;
	}

	@Override
	public int hashCode()
	{
		return this.player.username.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof PlayerGearData)
		{
			return ((PlayerGearData) obj).player.username.equals(this.player.username);
		}

		return false;
	}
}
