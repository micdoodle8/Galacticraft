package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;

public interface IPlayerClient
{
    void move(ClientPlayerEntity player, MoverType type, Vec3d pos);

    void onUpdate(ClientPlayerEntity player);

    void onTickPre(ClientPlayerEntity player);

    void onTickPost(ClientPlayerEntity player);

    Direction getBedDirection(ClientPlayerEntity player, Direction vanillaDegrees);

    boolean isEntityInsideOpaqueBlock(ClientPlayerEntity player, boolean vanillaInside);

//    boolean wakeUpPlayer(ClientPlayerEntity player, boolean immediately, boolean updateWorldFlag, boolean setSpawn); TODO Cryo chamber

    void onBuild(int i, ClientPlayerEntity player);
}
