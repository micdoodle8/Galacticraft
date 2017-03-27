package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;

public interface IPlayerClient
{
    void move(EntityPlayerSP player, MoverType type, double x, double y, double z);

    void onUpdate(EntityPlayerSP player);

    void onLivingUpdatePre(EntityPlayerSP player);

    void onLivingUpdatePost(EntityPlayerSP player);

    float getBedOrientationInDegrees(EntityPlayerSP player, float vanillaDegrees);

    boolean isEntityInsideOpaqueBlock(EntityPlayerSP player, boolean vanillaInside);

    boolean wakeUpPlayer(EntityPlayerSP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn);

    void onBuild(int i, EntityPlayerSP player);
}
