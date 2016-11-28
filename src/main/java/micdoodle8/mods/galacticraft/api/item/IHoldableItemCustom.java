package micdoodle8.mods.galacticraft.api.item;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.entity.player.EntityPlayer;

public interface IHoldableItemCustom extends IHoldableItem
{
    public Vector3 getLeftHandRotation(EntityPlayer player);

    public Vector3 getRightHandRotation(EntityPlayer player);
}
