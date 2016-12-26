package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.Random;

public class TileEntitySpout extends TileEntity implements ITickable
{
    private final Random rand = new Random(System.currentTimeMillis());

    @Override
    public void update()
    {
        if (this.worldObj.isRemote)
        {
            if (rand.nextInt(100) == 0)
            {
                double posX = (double)pos.getX() + 0.45 + rand.nextDouble() * 0.1;
                double posY = (double)pos.getY() + 1.0;
                double posZ = (double)pos.getZ() + 0.45 + rand.nextDouble() * 0.1;
                for (int i = 0; i < 14 + rand.nextInt(4); ++i)
                {
                    GalacticraftPlanets.spawnParticle("acidVapor", new Vector3(posX, posY, posZ), new Vector3(rand.nextDouble() * 0.5 - 0.25, rand.nextDouble() * 0.5 + 0.5, rand.nextDouble() * 0.5 - 0.25));
                }
            }
        }
    }
}
