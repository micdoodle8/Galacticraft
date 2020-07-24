package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.UUID;

public class TileEntitySlimelingEgg extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.slimelingEgg)
    public static TileEntityType<TileEntitySlimelingEgg> TYPE;

    public int timeToHatch = -1;
    public String lastTouchedPlayerUUID = "";

    public TileEntitySlimelingEgg()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote)
        {
            if (this.timeToHatch > 0)
            {
                this.timeToHatch--;
            }
            else if (this.timeToHatch == 0 && lastTouchedPlayerUUID != null && lastTouchedPlayerUUID.length() > 0)
            {
                BlockState state = this.world.getBlockState(this.getPos());
                BlockSlimelingEgg.EnumEggColor color = state.get(BlockSlimelingEgg.EGG_COLOR);

                float colorRed = 0.0F;
                float colorGreen = 0.0F;
                float colorBlue = 0.0F;

                switch (color)
                {
                    case RED:
                        colorRed = 1.0F;
                        break;
                    case BLUE:
                        colorBlue = 1.0F;
                        break;
                    case YELLOW:
                        colorRed = 1.0F;
                        colorGreen = 1.0F;
                        break;
                }

                EntitySlimeling slimeling = EntitySlimeling.createEntitySlimeling(this.world, colorRed, colorGreen, colorBlue);

                slimeling.setPosition(this.getPos().getX() + 0.5, this.getPos().getY() + 1.0, this.getPos().getZ() + 0.5);
                slimeling.setOwnerId(UUID.fromString(this.lastTouchedPlayerUUID));

                if (!this.world.isRemote)
                {
                    this.world.addEntity(slimeling);
                }

                slimeling.setTamed(true);
                slimeling.getNavigator().clearPath();
                slimeling.setAttackTarget(null);
                slimeling.setHealth(20.0F);

                this.world.removeBlock(this.getPos(), false);
            }
        }
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.timeToHatch = nbt.getInt("TimeToHatch");

        String uuid = nbt.getString("OwnerUUID");

        if (uuid.length() > 0)
        {
            lastTouchedPlayerUUID = uuid;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("TimeToHatch", this.timeToHatch);
        nbt.putString("OwnerUUID", this.lastTouchedPlayerUUID);
        return nbt;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
