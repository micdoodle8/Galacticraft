package micdoodle8.mods.galacticraft.core.dimension;

import java.util.Random;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.api.vector.Vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class GCCoreSpaceTeleportType implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
    {
        return new Vector3(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity player)
    {
        return new Vector3(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand)
    {
        return null;
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        if (GCCoreConfigManager.spaceStationsRequirePermission && !newWorld.isRemote)
        {
            player.sendChatToPlayer(ChatMessageComponent.createFromText(EnumColor.YELLOW + "Type " + EnumColor.AQUA + "/ssinvite <playername> " + EnumColor.YELLOW + "to allow another player to enter this space station!"));
        }
    }
}
