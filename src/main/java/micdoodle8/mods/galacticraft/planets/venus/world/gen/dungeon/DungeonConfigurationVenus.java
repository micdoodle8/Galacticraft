package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class DungeonConfigurationVenus extends DungeonConfiguration
{
    private BlockState brickBlockFloor;

    public DungeonConfigurationVenus()
    {
    }

    public DungeonConfigurationVenus(BlockState brickBlock, BlockState brickBlockFloor, int yPosition, int hallwayLengthMin, int hallwayLengthMax, int hallwayHeight, int roomHeight, Class<?> bossRoom, Class<?> treasureRoom)
    {
        super(brickBlock, yPosition, hallwayLengthMin, hallwayLengthMax, hallwayHeight, roomHeight, bossRoom, treasureRoom);
        this.brickBlockFloor = brickBlockFloor;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT tagCompound)
    {
        tagCompound.putString("brickBlockFloor", this.brickBlockFloor.getBlock().getRegistryName().toString());

        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(CompoundNBT tagCompound)
    {
        try
        {
            this.brickBlockFloor = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tagCompound.getString("brickBlockFloor"))).getDefaultState();
        }
        catch (Exception e)
        {
            System.err.println("Failed to read dungeon configuration from NBT");
            e.printStackTrace();
        }

        super.readFromNBT(tagCompound);
    }

    public BlockState getBrickBlockFloor()
    {
        return brickBlockFloor;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops)
    {
        ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
        Dynamic<T> s = super.serialize(ops);
        Map<Dynamic<T>, Dynamic<T>> mapValues = s.getMapValues().orElse(null);
        for (Map.Entry<Dynamic<T>, Dynamic<T>> e : mapValues.entrySet())
        {
            builder.put(e.getKey().getValue(), e.getValue().getValue());
        }
        builder.put(ops.createString("brickBlockFloor"), ops.createString(this.brickBlockFloor.getBlock().getRegistryName().toString()));
        return new Dynamic<>(ops, ops.createMap(builder.build()));
    }

    public static <T> DungeonConfigurationVenus deserialize(Dynamic<T> ops)
    {
        BlockState state = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ops.get("brickBlock").asString().get())).getDefaultState();
        BlockState stateFloor = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ops.get("brickBlockFloor").asString().get())).getDefaultState();
        try
        {
            return new DungeonConfigurationVenus(state, stateFloor, ops.get("yPosition").asInt(0),
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
