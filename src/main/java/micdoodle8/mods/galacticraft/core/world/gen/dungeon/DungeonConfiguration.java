package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class DungeonConfiguration implements IFeatureConfig
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
        Minecraft.getInstance().mouseHelper.ungrabMouse();
        tagCompound.putString("brickBlock", this.brickBlock.getBlock().getRegistryName().toString());
//        tagCompound.putInt("brickBlockMeta", this.brickBlock.getBlock().getMetaFromState(this.brickBlock));
        tagCompound.putInt("yPosition", this.yPosition);
        tagCompound.putInt("hallwayLengthMin", this.hallwayLengthMin);
        tagCompound.putInt("hallwayLengthMax", this.hallwayLengthMax);
        tagCompound.putInt("hallwayHeight", this.hallwayHeight);
        tagCompound.putInt("roomHeight", this.roomHeight);
        tagCompound.putString("bossRoom", this.bossRoom.getName());
        tagCompound.putString("treasureRoom", this.treasureRoom.getName());
        Minecraft.getInstance().mouseHelper.grabMouse(); // TODO Remove
        return tagCompound;
    }

    public void readFromNBT(CompoundNBT tagCompound)
    {
        try
        {
            Minecraft.getInstance().mouseHelper.ungrabMouse();
            this.brickBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tagCompound.getString("brickBlock"))).getDefaultState();
            this.yPosition = tagCompound.getInt("yPosition");
            this.hallwayLengthMin = tagCompound.getInt("hallwayLengthMin");
            this.hallwayLengthMax = tagCompound.getInt("hallwayLengthMax");
            this.hallwayHeight = tagCompound.getInt("hallwayHeight");
            this.roomHeight = tagCompound.getInt("roomHeight");
            this.bossRoom = Class.forName(tagCompound.getString("bossRoom"));
            this.treasureRoom = Class.forName(tagCompound.getString("treasureRoom"));
            Minecraft.getInstance().mouseHelper.grabMouse(); // TODO Remove
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

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops)
    {
        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        builder.put(ops.createString("brickBlock"), ops.createString(this.brickBlock.getBlock().getRegistryName().toString()));
        builder.put(ops.createString("yPosition"), ops.createInt(this.yPosition));
        builder.put(ops.createString("hallwayLengthMin"), ops.createInt(this.hallwayLengthMin));
        builder.put(ops.createString("hallwayLengthMax"), ops.createInt(this.hallwayLengthMax));
        builder.put(ops.createString("hallwayHeight"), ops.createInt(this.hallwayHeight));
        builder.put(ops.createString("roomHeight"), ops.createInt(this.roomHeight));
        builder.put(ops.createString("bossRoom"), ops.createString(this.bossRoom.getName()));
        builder.put(ops.createString("treasureRoom"), ops.createString(this.treasureRoom.getName()));
        return new Dynamic<>(ops, ops.createMap(builder.build()));
    }

    public static <T> DungeonConfiguration deserialize(Dynamic<T> ops)
    {
        BlockState state = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ops.get("brickBlock").asString().get())).getDefaultState();
        try
        {
            return new DungeonConfiguration(state, ops.get("yPosition").asInt(0),
                    ops.get("yPosition").asInt(0),
                    ops.get("hallwayLengthMin").asInt(0),
                    ops.get("hallwayLengthMax").asInt(0),
                    ops.get("hallwayHeight").asInt(0),
                    Class.forName(ops.get("bossRoom").asString().get()),
                    Class.forName(ops.get("treasureRoom").asString().get()));
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
