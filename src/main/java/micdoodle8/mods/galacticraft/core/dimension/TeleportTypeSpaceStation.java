package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TeleportTypeSpaceStation implements ITeleportType
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
        return null;
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, ServerPlayerEntity player, boolean ridingAutoRocket)
    {
        if (ConfigManagerCore.spaceStationsRequirePermission.get() && !newWorld.isRemote)
        {
            player.sendMessage(new StringTextComponent(EnumColor.YELLOW + GCCoreUtil.translate("gui.spacestation.type_command") + " " + EnumColor.AQUA + "/ssinvite " + GCCoreUtil.translate("gui.spacestation.playe") + " " + EnumColor.YELLOW + GCCoreUtil.translate("gui.spacestation.to_allow_entry")));
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayerEntity player)
    {
        // TODO Auto-generated method stub

    }
}
