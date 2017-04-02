package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import java.util.Random;

import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseDeck.EnumDeckType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public class BaseConfiguration
{
    private final static int HANGAR_AIRLOCK_HEIGHT = 5;
    private final static int HANGAR_AIRLOCK_WIDTH = 7;
    private int deckType;
    private boolean hangar;
    private IBlockState wallBlock;
    private int yPosition;
//    private int layers;
//    private int roomDepth;
    private int roomHeight;
    private int roomsNo;

    public BaseConfiguration()
    {
    }

    public BaseConfiguration(int yPosition, Random rand)
    {
        this.yPosition = yPosition;
        this.deckType = rand.nextInt(BaseDeck.EnumDeckType.values().length);
        this.hangar = true;
        this.roomHeight = BaseDeck.EnumDeckType.values()[this.deckType].height;
        this.wallBlock = BaseDeck.EnumDeckType.values()[this.deckType].wall;
        this.roomsNo = this.deckType == 3 ? 1 : rand.nextInt(3) + 3;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("yPosition", this.yPosition);
        tagCompound.setInteger("deckType", this.deckType);
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        try
        {
            this.yPosition = tagCompound.getInteger("yPosition");
            this.deckType = tagCompound.getInteger("deckType");
            this.roomHeight = BaseDeck.EnumDeckType.values()[this.deckType].height;
            this.wallBlock = BaseDeck.EnumDeckType.values()[this.deckType].wall;
        }
        catch (Exception e)
        {
            System.err.println("Failed to read dungeon configuration from NBT");
            e.printStackTrace();
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

    public EnumDeckType getDeckType()
    {
        return BaseDeck.EnumDeckType.values()[this.deckType];
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
        return hangar ? 1 : roomsNo;
    }

    public int getCorridorLength()
    {
        if (getRoomsNo() == 1)
            return BaseDeck.ROOMSMALL;

        if (getRoomsNo() == 2)
            return BaseDeck.ROOMSMALL + BaseDeck.ROOMLARGE;

        return getRoomsNo() * BaseDeck.ROOMSMALL + 2 * (BaseDeck.ROOMLARGE - BaseDeck.ROOMSMALL);
    }
}
