package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseDeck.EnumBaseType;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseRoom.EnumRoomType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public class BaseConfiguration implements IFeatureConfig
{
    private final static int HANGAR_AIRLOCK_HEIGHT = 6;
    private final static int HANGAR_AIRLOCK_WIDTH = 7;
    private int yPosition;
    private int baseType;
    private boolean hangar;
    private int roomHeight;
    private int roomDepth;
    private BlockState wallBlock;
    private int roomsNo;
    private int[] randomRoomTypes;
    private final EnumRoomType[] roomTypes = EnumRoomType.values();

    public BaseConfiguration(int yPosition, int baseType, int roomDepth, int roomsNo)
    {
        this.yPosition = yPosition;
        this.baseType = baseType;
        this.roomDepth = roomDepth;
        this.roomsNo = roomsNo;
    }

    public BaseConfiguration()
    {
    }

    public BaseConfiguration(int yPosition, Random rand)
    {
        BaseDeck.EnumBaseType[] types = BaseDeck.EnumBaseType.values();
        this.yPosition = yPosition - 2 + rand.nextInt(5);
        this.baseType = rand.nextInt(types.length);
        this.hangar = rand.nextInt(3) == 0;
        this.roomHeight = types[this.baseType].height;
        this.roomDepth = this.hangar ? 7 : rand.nextInt(3) + 5;
        this.wallBlock = types[this.baseType].wall;
        this.roomsNo = rand.nextInt(2) + 2;
        this.createRandomRoomList(rand);
    }

    private void createRandomRoomList(Random rand)
    {
        int range = this.roomTypes.length;
        int size = this.hangar ? 8 : this.roomsNo * 6;
        this.randomRoomTypes = new int[size];
        for (int i = 0; i < size; i++)
        {
            this.randomRoomTypes[i] = i % range;
        }
        int index, temp;
        for (int i = size - 1; i > 0; i--)
        {
            index = rand.nextInt(i + 1);
            if (i == index)
            {
                continue;
            }
            temp = this.randomRoomTypes[index];
            this.randomRoomTypes[index] = this.randomRoomTypes[i];
            this.randomRoomTypes[i] = temp;
        }

        //Make sure there's a Cargo Loader on lower tier (50/50 chance this causes one other room to be missed completely, that's OK!)
        if (this.hangar)
        {
            boolean storeFound = false;
            for (int i = 0; i < 4; i++)
            {
                if (this.randomRoomTypes[i] == EnumRoomType.STORE.ordinal())
                {
                    storeFound = true;
                    break;
                }
            }
            if (!storeFound)
            {
                this.randomRoomTypes[rand.nextInt(4)] = EnumRoomType.STORE.ordinal();
            }
        }
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops)
    {
        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        builder.put(ops.createString("yPos"), ops.createInt(this.yPosition));
        builder.put(ops.createString("dT"), ops.createInt(this.baseType + (this.hangar ? 16 : 0)));
        builder.put(ops.createString("rmD"), ops.createInt(this.roomDepth));
        builder.put(ops.createString("rmN"), ops.createInt(this.roomsNo));
        return new Dynamic<>(ops, ops.createMap(builder.build()));
    }

    public static <T> BaseConfiguration deserialize(Dynamic<T> ops)
    {
        BaseConfiguration config = new BaseConfiguration(ops.get("yPos").asInt(0),
                ops.get("dT").asInt(0),
                ops.get("rmD").asInt(0),
                ops.get("rmN").asInt(0));
        if (config.baseType >= 16)
        {
            config.hangar = true;
            config.baseType -= 16;
        }
        return config;
    }

    public void writeToNBT(CompoundNBT tagCompound)
    {
        tagCompound.putInt("yPos", this.yPosition);
        tagCompound.putInt("dT", this.baseType + (this.hangar ? 16 : 0));
        tagCompound.putInt("rmD", this.roomDepth);
        tagCompound.putInt("rmN", this.roomsNo);
    }

    public void readFromNBT(CompoundNBT tagCompound)
    {
        try
        {
            this.yPosition = tagCompound.getInt("yPos");
            this.baseType = tagCompound.getInt("dT");
            this.hangar = false;
            if (this.baseType >= 16)
            {
                this.hangar = true;
                this.baseType -= 16;
            }
            this.roomDepth = tagCompound.getInt("rmD");
            this.roomsNo = tagCompound.getInt("rmN");
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

    public BlockState getWallBlock()
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
        {
            return BaseDeck.ROOMLARGE;
        }

        if (getRoomsNo() == 2)
        {
            return BaseDeck.ROOMLARGE + BaseDeck.ROOMLARGE;
        }

        return getRoomsNo() * BaseDeck.ROOMSMALL + 2 * (BaseDeck.ROOMLARGE - BaseDeck.ROOMSMALL);
    }

    public EnumRoomType getRandomRoom(int i)
    {
        return roomTypes[this.randomRoomTypes[i % this.randomRoomTypes.length]];
    }
}
