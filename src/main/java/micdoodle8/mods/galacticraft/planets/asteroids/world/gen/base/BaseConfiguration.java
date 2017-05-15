package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import java.util.Random;

import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseDeck.EnumBaseType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public class BaseConfiguration
{
    private final static int HANGAR_AIRLOCK_HEIGHT = 6;
    private final static int HANGAR_AIRLOCK_WIDTH = 7;
    private int yPosition;
    private int baseType;
    private boolean hangar;
    private int roomHeight;
    private int roomDepth;
    private IBlockState wallBlock;
    private int roomsNo;

    public BaseConfiguration()
    {
    }

    public BaseConfiguration(int yPosition, Random rand)
    {
        BaseDeck.EnumBaseType[] types = BaseDeck.EnumBaseType.values();
        this.yPosition = yPosition;
        this.baseType = rand.nextInt(types.length);
        this.hangar = true;  //For testing!
        this.roomHeight = types[this.baseType].height;
        this.roomDepth = this.hangar ? 7 : rand.nextInt(3) + 5;
        this.wallBlock = types[this.baseType].wall;
        this.roomsNo = rand.nextInt(3) + 2;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("yPos", this.yPosition);
        tagCompound.setInteger("dT", this.baseType + (this.hangar ? 16 : 0));
        tagCompound.setInteger("rmD", this.roomDepth);
        tagCompound.setInteger("rmN", this.roomsNo);
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        try
        {
            this.yPosition = tagCompound.getInteger("yPos");
            this.baseType = tagCompound.getInteger("dT");
            this.hangar = false;
            if (this.baseType >= 16)
            {
                this.hangar = true;
                this.baseType -= 16;
            }
            this.roomDepth = tagCompound.getInteger("rmD");
            this.roomsNo = tagCompound.getInteger("rmN");
            this.roomHeight = BaseDeck.EnumBaseType.values()[this.baseType].height;
            this.wallBlock = BaseDeck.EnumBaseType.values()[this.baseType].wall;
        }
        catch (Exception e)
        {
            System.err.println("Failed to read Abandoned Base configuration from NBT");
            System.err.println(tagCompound.toString());
        }
    }

    public int getYPosition()
    {
        return this.yPosition;
    }

    public IBlockState getWallBlock()
    {
        return this.wallBlock;
    }

    public int getRoomHeight()
    {
        return hangar ? HANGAR_AIRLOCK_HEIGHT : this.roomHeight;
    }

    public int getRoomDepth()
    {
        return this.roomDepth;
    }

    public EnumBaseType getDeckType()
    {
        return BaseDeck.EnumBaseType.values()[this.baseType];
    }

    public boolean isHangarDeck()
    {
        return hangar;
    }

    public int getCorridorWidth()
    {
        return hangar ? HANGAR_AIRLOCK_WIDTH : this.getDeckType().width;
    }

    public int getRoomsNo()
    {
        return hangar ? 2 : roomsNo;
    }

    public int getCorridorLength()
    {
        if (getRoomsNo() == 1)
            return BaseDeck.ROOMLARGE;

        if (getRoomsNo() == 2)
            return BaseDeck.ROOMLARGE + BaseDeck.ROOMLARGE;

        return getRoomsNo() * BaseDeck.ROOMSMALL + 2 * (BaseDeck.ROOMLARGE - BaseDeck.ROOMSMALL);
    }
}
