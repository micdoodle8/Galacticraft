package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class DungeonConfiguration
{
    private BlockState brickBlock;
    private int yPosition;
    private int hallwayLengthMin;
    private int hallwayLengthMax;
    private int hallwayHeight;
    private int roomHeight;
    private Class<?> bossRoom;
    private Class<?> treasureRoom;

    public DungeonConfiguration()
    {
    }

    public DungeonConfiguration(BlockState brickBlock, int yPosition, int hallwayLengthMin, int hallwayLengthMax, int hallwayHeight, int roomHeight, Class<?> bossRoom, Class<?> treasureRoom)
    {
        this.brickBlock = brickBlock;
        this.yPosition = yPosition;
        this.hallwayLengthMin = hallwayLengthMin;
        this.hallwayLengthMax = hallwayLengthMax;
        this.hallwayHeight = hallwayHeight;
        this.roomHeight = roomHeight;
        this.bossRoom = bossRoom;
        this.treasureRoom = treasureRoom;
    }

    public CompoundNBT writeToNBT(CompoundNBT tagCompound)
    {
        tagCompound.putString("brickBlock", this.brickBlock.getBlock().getRegistryName().toString());
//        tagCompound.putInt("brickBlockMeta", this.brickBlock.getBlock().getMetaFromState(this.brickBlock));
        tagCompound.putInt("yPosition", this.yPosition);
        tagCompound.putInt("hallwayLengthMin", this.hallwayLengthMin);
        tagCompound.putInt("hallwayLengthMax", this.hallwayLengthMax);
        tagCompound.putInt("hallwayHeight", this.hallwayHeight);
        tagCompound.putInt("roomHeight", this.roomHeight);
        tagCompound.putString("bossRoom", this.bossRoom.getName());
        tagCompound.putString("treasureRoom", this.treasureRoom.getName());
        return tagCompound;
    }

    public void readFromNBT(CompoundNBT tagCompound)
    {
        try
        {
            this.brickBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tagCompound.getString("brickBlock"))).getDefaultState();
            this.yPosition = tagCompound.getInt("yPosition");
            this.hallwayLengthMin = tagCompound.getInt("hallwayLengthMin");
            this.hallwayLengthMax = tagCompound.getInt("hallwayLengthMax");
            this.hallwayHeight = tagCompound.getInt("hallwayHeight");
            this.roomHeight = tagCompound.getInt("roomHeight");
            this.bossRoom = Class.forName(tagCompound.getString("bossRoom"));
            this.treasureRoom = Class.forName(tagCompound.getString("treasureRoom"));
        }
        catch (Exception e)
        {
            System.err.println("Failed to read dungeon configuration from NBT");
            e.printStackTrace();
        }
    }

    public BlockState getBrickBlock()
    {
        return brickBlock;
    }

    public int getYPosition()
    {
        return yPosition;
    }

    public int getHallwayLengthMin()
    {
        return hallwayLengthMin;
    }

    public int getHallwayLengthMax()
    {
        return hallwayLengthMax;
    }

    public int getHallwayHeight()
    {
        return hallwayHeight;
    }

    public int getRoomHeight()
    {
        return roomHeight;
    }

    public Class<?> getBossRoom()
    {
        return bossRoom;
    }

    public Class<?> getTreasureRoom()
    {
        return treasureRoom;
    }
}
