package micdoodle8.mods.galacticraft.api.item;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.entity.player.PlayerEntity;

public interface IHoldableItemCustom extends IHoldableItem
{
    Vector3 getLeftHandRotation(PlayerEntity player);

    Vector3 getRightHandRotation(PlayerEntity player);
}
