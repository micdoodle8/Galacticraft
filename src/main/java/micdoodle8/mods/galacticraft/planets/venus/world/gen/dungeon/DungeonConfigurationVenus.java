package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

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
        tagCompound.setString("brickBlock", Block.REGISTRY.getNameForObject(this.brickBlockFloor.getBlock()).toString());

        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(CompoundNBT tagCompound)
    {
        try
        {
            this.brickBlockFloor = Block.getBlockFromName(tagCompound.getString("brickBlock")).getStateFromMeta(tagCompound.getInteger("brickBlockMeta"));
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
}
