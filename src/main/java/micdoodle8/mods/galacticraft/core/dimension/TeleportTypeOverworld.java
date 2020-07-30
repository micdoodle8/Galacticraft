package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TeleportTypeOverworld implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return true;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerWorld world, ServerPlayerEntity player)
    {
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            return new Vector3D(stats.getCoordsTeleportedFromX(), 250.0F, stats.getCoordsTeleportedFromZ());
        }

        return null;
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerWorld world, Entity entity)
    {
        return new Vector3D((float) entity.getPosX(), 250.0F, (float) entity.getPosZ());
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerWorld world, ServerPlayerEntity player, Random rand)
    {
        final float x = (rand.nextFloat() * 2 - 1.0F) * 5.0F;
        final float z = (rand.nextFloat() * 2 - 1.0F) * 5.0F;

        return new Vector3D((float) player.getPosX() + x, 230.0F, (float) player.getPosZ() + z);
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, ServerPlayerEntity player, boolean ridingAutoRocket)
    {
    }

    @Override
    public void setupAdventureSpawn(ServerPlayerEntity player)
    {
        // TODO Auto-generated method stub

    }
}
