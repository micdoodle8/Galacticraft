package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.MoverType;

public interface IPlayerClient
{
    void move(ClientPlayerEntity player, MoverType type, double x, double y, double z);

    void onUpdate(ClientPlayerEntity player);

    void onLivingUpdatePre(ClientPlayerEntity player);

    void onLivingUpdatePost(ClientPlayerEntity player);

    float getBedOrientationInDegrees(ClientPlayerEntity player, float vanillaDegrees);

    boolean isEntityInsideOpaqueBlock(ClientPlayerEntity player, boolean vanillaInside);

    boolean wakeUpPlayer(ClientPlayerEntity player, boolean immediately, boolean updateWorldFlag, boolean setSpawn);

    void onBuild(int i, ClientPlayerEntity player);
}
