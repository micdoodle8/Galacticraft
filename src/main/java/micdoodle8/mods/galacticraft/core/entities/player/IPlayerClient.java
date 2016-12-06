package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.EntityPlayerSP;

public interface IPlayerClient
{
    void moveEntity(EntityPlayerSP player, double par1, double par3, double par5);

    void onUpdate(EntityPlayerSP player);

    void onLivingUpdatePre(EntityPlayerSP player);

    void onLivingUpdatePost(EntityPlayerSP player);

    float getBedOrientationInDegrees(EntityPlayerSP player, float vanillaDegrees);

    boolean isEntityInsideOpaqueBlock(EntityPlayerSP player, boolean vanillaInside);

    boolean wakeUpPlayer(EntityPlayerSP player, boolean par1, boolean par2, boolean par3);

    void onBuild(int i, EntityPlayerSP player);
}
