package codechicken.lib.util;

import codechicken.lib.vec.Vector3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

/**
 * Created by covers1624 on 17/10/2016.
 */
//TODO Nuke.
public class EntityUtils {

    public static int entityId = 0;

    public static int nextEntityId() {
        return entityId++;
    }

    public static boolean teleportEntityTo(EntityLivingBase entity, Vector3 location) {
        EnderTeleportEvent event = new EnderTeleportEvent(entity, location.x, location.y, location.z, 0);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        SoundUtils.playSoundAt(entity, SoundCategory.BLOCKS, SoundEvents.ENTITY_ENDERMEN_TELEPORT);
        return true;
    }

}
