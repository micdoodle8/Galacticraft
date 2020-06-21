package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

public class TileEntityFallenMeteor extends TileEntityAdvanced
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.fallenMeteor)
    public static TileEntityType<TileEntityFallenMeteor> TYPE;

    public static final int MAX_HEAT_LEVEL = 5000;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int heatLevel = TileEntityFallenMeteor.MAX_HEAT_LEVEL;
    private boolean sentOnePacket = false;

    public TileEntityFallenMeteor()
    {
        super(TYPE);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote && this.heatLevel > 0)
        {
            this.heatLevel--;
        }
    }

    public int getHeatLevel()
    {
        return this.heatLevel;
    }

    public void setHeatLevel(int heatLevel)
    {
        this.heatLevel = heatLevel;
    }

    public float getScaledHeatLevel()
    {
        return (float) this.heatLevel / TileEntityFallenMeteor.MAX_HEAT_LEVEL;
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.world.isRemote)
        {
//            this.world.notifyLightSet(this.getPos()); TODO Lighting
            world.getChunkProvider().getLightManager().checkBlock(this.getPos());
        }
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        this.sentOnePacket = true;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.heatLevel = nbt.getInt("MeteorHeatLevel");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("MeteorHeatLevel", this.heatLevel);
        return nbt;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public double getPacketRange()
    {
        return 50;
    }

    @Override
    public int getPacketCooldown()
    {
        return this.sentOnePacket ? 100 : 1;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }
}
