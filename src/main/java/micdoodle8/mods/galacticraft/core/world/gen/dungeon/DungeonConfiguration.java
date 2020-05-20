package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

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
        tagCompound.setString("brickBlock", Block.REGISTRY.getNameForObject(this.brickBlock.getBlock()).toString());
        tagCompound.setInteger("brickBlockMeta", this.brickBlock.getBlock().getMetaFromState(this.brickBlock));
        tagCompound.setInteger("yPosition", this.yPosition);
        tagCompound.setInteger("hallwayLengthMin", this.hallwayLengthMin);
        tagCompound.setInteger("hallwayLengthMax", this.hallwayLengthMax);
        tagCompound.setInteger("hallwayHeight", this.hallwayHeight);
        tagCompound.setInteger("roomHeight", this.roomHeight);
        tagCompound.setString("bossRoom", this.bossRoom.getName());
        tagCompound.setString("treasureRoom", this.treasureRoom.getName());
        return tagCompound;
    }

    public void readFromNBT(CompoundNBT tagCompound)
    {
        try
        {
            this.brickBlock = Block.getBlockFromName(tagCompound.getString("brickBlock")).getStateFromMeta(tagCompound.getInteger("brickBlockMeta"));
            this.yPosition = tagCompound.getInteger("yPosition");
            this.hallwayLengthMin = tagCompound.getInteger("hallwayLengthMin");
            this.hallwayLengthMax = tagCompound.getInteger("hallwayLengthMax");
            this.hallwayHeight = tagCompound.getInteger("hallwayHeight");
            this.roomHeight = tagCompound.getInteger("roomHeight");
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
