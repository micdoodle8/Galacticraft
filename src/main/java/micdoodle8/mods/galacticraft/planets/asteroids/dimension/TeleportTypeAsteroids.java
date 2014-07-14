package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public class TeleportTypeAsteroids implements ITeleportType
{
	@Override
	public boolean useParachute()
	{
		return false;
	}

	@Override
	public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
	{
		if (player instanceof GCEntityPlayerMP)
		{
			return new Vector3(((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromX, 310, ((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromZ);
		}

		return null;
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
	{
		return new Vector3(entity.posX, ConfigManagerCore.disableLander ? 250.0 : 900.0, entity.posZ);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand)
	{
		return null;
	}

	@Override
	public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
	{
        if (!ridingAutoRocket && player instanceof GCEntityPlayerMP && ((GCEntityPlayerMP) player).getPlayerStats().teleportCooldown <= 0)
        {
            GCEntityPlayerMP gcPlayer = (GCEntityPlayerMP) player;

            if (gcPlayer.capabilities.isFlying)
            {
                gcPlayer.capabilities.isFlying = false;
            }

            EntityEntryPod entryPod = new EntityEntryPod(newWorld);
            entryPod.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);
            entryPod.waitForPlayer = true;
            entryPod.riddenByEntity = player;
            player.ridingEntity = entryPod;

            entryPod.containedItems = new ItemStack[gcPlayer.getPlayerStats().rocketStacks.length + 1];
            entryPod.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, gcPlayer.getPlayerStats().fuelLevel));

            for (int i = 0; i < gcPlayer.getPlayerStats().rocketStacks.length; i++)
            {
                entryPod.containedItems[i] = gcPlayer.getPlayerStats().rocketStacks[i];
            }

            if (!newWorld.isRemote)
            {
                newWorld.spawnEntityInWorld(entryPod);
            }

            gcPlayer.getPlayerStats().teleportCooldown = 10;
        }
	}
}
