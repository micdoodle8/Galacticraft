package codechicken.nei;

import codechicken.core.CommonUtils;
import codechicken.lib.packet.PacketCustom;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.List;

public class ServerHandler
{
    private static ServerHandler instance;

    public static void load() {
        instance = new ServerHandler();

        PacketCustom.assignHandler(NEICPH.channel, new NEISPH());
        FMLCommonHandler.instance().bus().register(instance);
        MinecraftForge.EVENT_BUS.register(instance);

        NEIActions.init();
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.WorldTickEvent event) {
        if (event.phase == Phase.START)
            processDisabledProperties(event.world);
    }

    @SubscribeEvent
    public void loadEvent(WorldEvent.Load event) {
        if(!event.world.isRemote)
            NEIServerConfig.load(event.world);
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == Phase.START && event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            PlayerSave save = NEIServerConfig.forPlayer(player.getCommandSenderName());
            if (save == null)
                return;
            updateMagneticPlayer(player, save);
            save.updateOpChange(player);
            save.save();
        }
    }

    private void processDisabledProperties(World world) {
        NEIServerUtils.advanceDisabledTimes(world);
        if (NEIServerUtils.isRaining(world) && NEIServerConfig.isActionDisabled(CommonUtils.getDimension(world), "rain"))
            NEIServerUtils.toggleRaining(world, false);
    }

    private void updateMagneticPlayer(EntityPlayerMP player, PlayerSave save) {
        if (!save.isActionEnabled("magnet") || player.isDead)
            return;

        float distancexz = 16;
        float distancey = 8;
        double maxspeedxz = 0.5;
        double maxspeedy = 0.5;
        double speedxz = 0.05;
        double speedy = 0.07;
        List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(distancexz, distancey, distancexz));
        for (EntityItem item : items) {
            if (item.delayBeforeCanPickup > 0) continue;
            if (!NEIServerUtils.canItemFitInInventory(player, item.getEntityItem())) continue;
            if (item.delayBeforeCanPickup == 0) {
                NEISPH.sendAddMagneticItemTo(player, item);
            }

            double dx = player.posX - item.posX;
            double dy = player.posY + player.getEyeHeight() - item.posY;
            double dz = player.posZ - item.posZ;
            double absxz = Math.sqrt(dx * dx + dz * dz);
            double absy = Math.abs(dy);
            if (absxz > distancexz) {
                continue;
            }
            if (absxz < 1) {
                item.onCollideWithPlayer(player);
            }

            if (absxz > 1) {
                dx /= absxz;
                dz /= absxz;
            }

            if (absy > 1) {
                dy /= absy;
            }

            double vx = item.motionX + speedxz * dx;
            double vy = item.motionY + speedy * dy;
            double vz = item.motionZ + speedxz * dz;

            double absvxz = Math.sqrt(vx * vx + vz * vz);
            double absvy = Math.abs(vy);

            double rationspeedxz = absvxz / maxspeedxz;
            if (rationspeedxz > 1) {
                vx /= rationspeedxz;
                vz /= rationspeedxz;
            }

            double rationspeedy = absvy / maxspeedy;
            if (rationspeedy > 1) {
                vy /= rationspeedy;
            }

            item.motionX = vx;
            item.motionY = vy;
            item.motionZ = vz;
        }
    }

    @SubscribeEvent
    public void loginEvent(PlayerLoggedInEvent event) {
        NEIServerConfig.loadPlayer(event.player);
        NEISPH.sendHasServerSideTo((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void logoutEvent(PlayerLoggedOutEvent event) {
        NEIServerConfig.unloadPlayer(event.player);
    }

    @SubscribeEvent
    public void dimChangeEvent(PlayerChangedDimensionEvent event) {
        NEISPH.sendHasServerSideTo((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void loginEvent(PlayerRespawnEvent event) {
        NEISPH.sendHasServerSideTo((EntityPlayerMP) event.player);
    }
}
