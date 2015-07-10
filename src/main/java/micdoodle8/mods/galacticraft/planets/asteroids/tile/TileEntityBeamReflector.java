package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorage;

public class TileEntityBeamReflector extends TileEntityBeamOutput implements ILaserNode
{
    public Vector3 color = new Vector3(0, 1, 0);
    private EnergyStorage storage = new EnergyStorage(10, 1);

    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    @Override
    public Vector3 getInputPoint()
    {
        float distance = 0.15F;
        Vector3 deviation = new Vector3(Math.sin(Math.toRadians(this.yaw - 180)) * distance, 0, Math.cos(Math.toRadians(this.yaw - 180)) * distance);
        Vector3 headVec = new Vector3(this.getPos().getX() + 0.5, this.getPos().getY() + 1.13228 / 2.0, this.getPos().getZ() + 0.5);
        headVec.translate(deviation.clone().invert());
        return headVec;
    }

    @Override
    public Vector3 getOutputPoint(boolean offset)
    {
        return new Vector3(this.getPos().getX() + 0.5, this.getPos().getY() + 1.13228 / 2.0, this.getPos().getZ() + 0.5);
    }

    @Override
    public double getPacketRange()
    {
        return 24.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public Vector3 getColor()
    {
        return this.color;
    }

    @Override
    public boolean canConnectTo(ILaserNode laserNode)
    {
        return this.color.equals(laserNode.getColor());
    }

    @Override
    public float receiveEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        if (this.getTarget() != null)
        {
            if (from instanceof EnergySourceWireless)
            {
                if (((EnergySourceWireless) from).nodes.contains(this.getTarget()))
                {
                    return 0;
                }

                ((EnergySourceWireless) from).nodes.add(this);

                return this.getTarget().receiveEnergyGC(from, amount, simulate);
            }
            else
            {
                return 0;
            }
        }

        return this.storage.receiveEnergyGC(amount, simulate);
    }

    @Override
    public float extractEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        return 0;
    }

    @Override
    public boolean nodeAvailable(EnergySource from)
    {
        return from instanceof EnergySourceWireless;
    }

    @Override
    public float getEnergyStoredGC(EnergySource from)
    {
        return this.storage.getEnergyStoredGC();
    }

    @Override
    public float getMaxEnergyStoredGC(EnergySource from)
    {
        return this.storage.getCapacityGC();
    }

    @Override
    public void setTarget(ILaserNode target)
    {
        super.setTarget(target);
    }
}
