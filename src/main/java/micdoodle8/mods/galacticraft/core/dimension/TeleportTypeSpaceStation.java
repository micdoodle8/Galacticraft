package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class TeleportTypeSpaceStation implements ITeleportType
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
        if (ConfigManagerCore.spaceStationsRequirePermission && !newWorld.isRemote)
        {
            player.sendMessage(new TextComponentString(EnumColor.YELLOW + GCCoreUtil.translate("gui.spacestation.type_command") + " " + EnumColor.AQUA + "/ssinvite " + GCCoreUtil.translate("gui.spacestation.playername") + " " + EnumColor.YELLOW + GCCoreUtil.translate("gui.spacestation.to_allow_entry")));
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub

    }
}
