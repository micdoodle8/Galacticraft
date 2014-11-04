package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.EntityPlayerSP;

public interface IPlayerClient
{
    public void moveEntity(EntityPlayerSP player, double par1, double par3, double par5);

    public void onUpdate(EntityPlayerSP player);

    public void onLivingUpdatePre(EntityPlayerSP player);

    public void onLivingUpdatePost(EntityPlayerSP player);

    public float getBedOrientationInDegrees(EntityPlayerSP player, float vanillaDegrees);

    public boolean isEntityInsideOpaqueBlock(EntityPlayerSP player, boolean vanillaInside);

    public boolean wakeUpPlayer(EntityPlayerSP player, boolean par1, boolean par2, boolean par3);

	public void onBuild(int i, EntityPlayerSP player);
}
