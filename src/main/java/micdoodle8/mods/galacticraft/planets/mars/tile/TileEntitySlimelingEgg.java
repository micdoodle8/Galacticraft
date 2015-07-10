package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.core.util.VersionUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySlimelingEgg extends TileEntity
{
    public int timeToHatch = -1;
    public String lastTouchedPlayerUUID = "";
    public String lastTouchedPlayerName = "";

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (this.timeToHatch > 0)
            {
                this.timeToHatch--;
            }
            else if (this.timeToHatch == 0 && lastTouchedPlayerUUID != null && lastTouchedPlayerUUID.length() > 0)
            {
                int metadata = this.worldObj.getBlockMetadata(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()) % 3;

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

                EntitySlimeling slimeling = new EntitySlimeling(this.worldObj, colorRed, colorGreen, colorBlue);

                slimeling.setPosition(this.getPos().getX() + 0.5, this.getPos().getY() + 1.0, this.getPos().getZ() + 0.5);
                VersionUtil.setSlimelingOwner(slimeling, this.lastTouchedPlayerUUID);
                slimeling.setOwnerUsername(this.lastTouchedPlayerName);

                if (!this.worldObj.isRemote)
                {
                    this.worldObj.spawnEntityInWorld(slimeling);
                }

                slimeling.setTamed(true);
                slimeling.setPathToEntity((PathEntity) null);
                slimeling.setAttackTarget((EntityLivingBase) null);
                slimeling.setHealth(20.0F);

                this.worldObj.setBlockToAir(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.timeToHatch = nbt.getInteger("TimeToHatch");
        VersionUtil.readSlimelingEggFromNBT(this, nbt);
        this.lastTouchedPlayerName = nbt.getString("OwnerUsername");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("TimeToHatch", this.timeToHatch);
        nbt.setString("OwnerUUID", this.lastTouchedPlayerUUID);
        nbt.setString("OwnerUsername", this.lastTouchedPlayerName);
    }
}
