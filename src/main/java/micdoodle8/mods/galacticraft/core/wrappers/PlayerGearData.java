package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerGearData
{
    private final EntityPlayer player;
    private int mask;
    private int gear;
    private int leftTank;
    private int rightTank;
    private int[] thermalPadding;
    private ResourceLocation parachute;
    private int frequencyModule;

    public PlayerGearData(EntityPlayer player)
    {
        this(player, -1, -1, -1, -1, -1, new int[] { -1, -1, -1, -1 });
    }

    public PlayerGearData(EntityPlayer player, int mask, int gear, int leftTank, int rightTank, int frequencyModule, int[] thermalPadding)
    {
        this.player = player;
        this.mask = mask;
        this.gear = gear;
        this.leftTank = leftTank;
        this.rightTank = rightTank;
        this.frequencyModule = frequencyModule;
        this.thermalPadding = thermalPadding;
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

    public int getThermalPadding(int slot)
    {
        if (slot >= 0 && slot < this.thermalPadding.length)
        {
            return this.thermalPadding[slot];
        }

        return -1;
    }

    public void setThermalPadding(int slot, int thermalPadding)
    {
        if (slot >= 0 && slot < this.thermalPadding.length)
        {
            this.thermalPadding[slot] = thermalPadding;
        }
    }

    @Override
    public int hashCode()
    {
        return this.player.getGameProfile().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PlayerGearData)
        {
            return ((PlayerGearData) obj).player.getGameProfile().getName().equals(this.player.getGameProfile().getName());
        }

        return false;
    }
}
