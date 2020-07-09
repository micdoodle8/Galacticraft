package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceWireless;
import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorage;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityBeamReflector extends TileEntityBeamOutput implements ILaserNode
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + AsteroidBlockNames.beamReflector)
    public static TileEntityType<TileEntityBeamReflector> TYPE;

    public Vector3 color = new Vector3(0, 1, 0);
    private final EnergyStorage storage = new EnergyStorage(10, 1);

//    @Override
//    public void tick()
//    {
//        super.tick();
//    }

    public TileEntityBeamReflector()
    {
        super(TYPE);
    }

    @Override
    public Vector3 getInputPoint()
    {
        float distance = 0.15F;
        Vector3 deviation = new Vector3((float) Math.sin(Math.toRadians(this.yaw - 180)) * distance, 0, (float) Math.cos(Math.toRadians(this.yaw - 180)) * distance);
        Vector3 headVec = new Vector3((float) this.getPos().getX() + 0.5F, (float) this.getPos().getY() + 1.13228F / 2.0F, (float) this.getPos().getZ() + 0.5F);
        headVec.translate(deviation.clone().invert());
        return headVec;
    }

    @Override
    public Vector3 getOutputPoint(boolean offset)
    {
        return new Vector3(this.getPos().getX() + 0.5F, this.getPos().getY() + 1.13228F / 2.0F, this.getPos().getZ() + 0.5F);
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

    private AxisAlignedBB renderAABB;

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 2, 1));
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_SHORT;
    }
}
