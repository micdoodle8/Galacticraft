package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TeleportTypeOrbit implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerWorld world, ServerPlayerEntity player)
    {
        return new Vector3D(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerWorld world, Entity player)
    {
        return new Vector3D(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerWorld world, ServerPlayerEntity player, Random rand)
    {
        return new Vector3D(-8.5, 90.0, -1.5);
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
