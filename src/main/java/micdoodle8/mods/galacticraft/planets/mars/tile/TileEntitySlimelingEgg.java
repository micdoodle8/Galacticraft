package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TileEntitySlimelingEgg extends TileEntity implements ITickable
{
    public int timeToHatch = -1;
    public String lastTouchedPlayerUUID = "";
    public String lastTouchedPlayerName = "";

    @Override
    public void update()
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
                int metadata = state.getBlock().getMetaFromState(state) % 3;

                float colorRed = 0.0F;
                float colorGreen = 0.0F;
                float colorBlue = 0.0F;

                switch (metadata)
                {
                case 0:
                    colorRed = 1.0F;
                    break;
                case 1:
                    colorBlue = 1.0F;
                    break;
                case 2:
                    colorRed = 1.0F;
                    colorGreen = 1.0F;
                    break;
                }

                EntitySlimeling slimeling = new EntitySlimeling(this.world, colorRed, colorGreen, colorBlue);

                slimeling.setPosition(this.getPos().getX() + 0.5, this.getPos().getY() + 1.0, this.getPos().getZ() + 0.5);
                slimeling.setOwnerId(UUID.fromString(this.lastTouchedPlayerUUID));
                slimeling.setOwnerUsername(this.lastTouchedPlayerName);

                if (!this.world.isRemote)
                {
                    this.world.addEntity(slimeling);
                }

                slimeling.setTamed(true);
                slimeling.getNavigator().clearPath();
                slimeling.setAttackTarget((LivingEntity) null);
                slimeling.setHealth(20.0F);

                this.world.setBlockToAir(this.getPos());
            }
        }
    }

    @Override
    public void readFromNBT(CompoundNBT nbt)
    {
        super.readFromNBT(nbt);
        this.timeToHatch = nbt.getInt("TimeToHatch");

        String uuid;
        if (nbt.contains("OwnerUUID", 8))
        {
            uuid = nbt.getString("OwnerUUID");
        }
        else
        {
            uuid = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.world.getMinecraftServer(), nbt.getString("Owner"));
        }

        if (uuid.length() > 0)
        {
            lastTouchedPlayerUUID = uuid;
        }

        this.lastTouchedPlayerName = nbt.getString("OwnerUsername");
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt)
    {
        super.writeToNBT(nbt);
        nbt.putInt("TimeToHatch", this.timeToHatch);
        nbt.setString("OwnerUUID", this.lastTouchedPlayerUUID);
        nbt.setString("OwnerUsername", this.lastTouchedPlayerName);
        return nbt;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
