package micdoodle8.mods.galacticraft.mars.entities;

import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.API.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.API.ISpaceship;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.world.World;

public class GCMarsEntitySpaceshipTier2 extends EntitySpaceshipBase implements ISpaceship
{
    protected ItemStack[] cargoItems = new ItemStack[36];

    public IUpdatePlayerListBox rocketSoundUpdater;

    private int type;

    public GCMarsEntitySpaceshipTier2(World par1World)
    {
    	super(par1World);
    }

    public GCMarsEntitySpaceshipTier2(World par1World, double par2, double par4, double par6, int type)
    {
        super(par1World);
        this.setPosition(par2, par4 + this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
        this.type = type;
    }

    public GCMarsEntitySpaceshipTier2(World par1World, double par2, double par4, double par6, boolean reversed, int type, ItemStack[] inv)
    {
        this(par1World, par2, par4, par6, type);
        this.cargoItems = inv;
    }

    @Override
	protected void entityInit()
    {
    	super.entityInit();
        this.dataWatcher.addObject(25, new Integer(0));
        this.setSpaceshipType(this.type);
    }

	@Override
	public void onUpdate()
    {
		super.onUpdate();

		if (this.getLaunched() == 1)
		{
			this.motionY += 0.005F;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
    }

	@Override
	public Entity[] getSpaceshipParts()
	{
		return null;
	}

	@Override
	public HashSet<Integer> getPossiblePlanets()
	{
		final HashSet<Integer> dimensions = new HashSet<Integer>();
		dimensions.add(0);
		dimensions.add(GCMoonConfigManager.dimensionIDMoon);
		dimensions.add(GCMarsConfigManager.dimensionIDMars);
		return dimensions;
	}

    public void setSpaceshipType(int par1)
    {
    	this.dataWatcher.updateObject(25, par1);
    }

    public int getSpaceshipType()
    {
    	return this.dataWatcher.getWatchableObjectInt(25);
    }

	@Override
	public int getYCoordToTeleport()
	{
		return 700;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	@Override
	public int getPreLaunchWait()
	{
		return 50;
	}

	@Override
	public List<ItemStack> getItemsDropped()
	{
		return null;
	}
}
