package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.SizedPiece;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public class DungeonConfigurationVenus extends DungeonConfiguration
{
    private IBlockState brickBlockFloor;

    public DungeonConfigurationVenus()
    {
    }

    public DungeonConfigurationVenus(IBlockState brickBlock, IBlockState brickBlockFloor, int yPosition, int hallwayLengthMin, int hallwayLengthMax, int hallwayHeight, int roomHeight, Class<? extends SizedPiece> bossRoom, Class<? extends SizedPiece> treasureRoom)
    {
        super(brickBlock, yPosition, hallwayLengthMin, hallwayLengthMax, hallwayHeight, roomHeight, bossRoom, treasureRoom);
        this.brickBlockFloor = brickBlockFloor;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setString("brickBlock", Block.blockRegistry.getNameForObject(this.brickBlockFloor.getBlock()).toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
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
    }

    public IBlockState getBrickBlockFloor()
    {
        return brickBlockFloor;
    }
}