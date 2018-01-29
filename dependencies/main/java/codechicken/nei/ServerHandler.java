package codechicken.nei;

import codechicken.lib.packet.PacketCustom;
import codechicken.nei.network.NEIClientPacketHandler;
import codechicken.nei.network.NEIServerPacketHandler;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import java.util.List;

public class ServerHandler {
    private static ServerHandler instance;

    public static void init() {
        instance = new ServerHandler();

        PacketCustom.assignHandler(NEIClientPacketHandler.channel, new NEIServerPacketHandler());
        //FMLCommonHandler.instance().bus().register(instance);
        MinecraftForge.EVENT_BUS.register(instance);

        Item.getItemFromBlock(Blocks.MOB_SPAWNER).setHasSubtypes(true);
        NEIActions.init();
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.WorldTickEvent event) {
        if (event.phase == Phase.START && !event.world.isRemote &&
                NEIServerConfig.dimTags.containsKey(event.world.provider.getDimension()))//fake worlds that don't call Load
        {
            processDisabledProperties(event.world);
        }
    }

    @SubscribeEvent
    public void loadEvent(WorldEvent.Load event) {
        if (!event.getWorld().isRemote) {
            NEIServerConfig.load(event.getWorld());
        }
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == Phase.START && event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            PlayerSave save = NEIServerConfig.forPlayer(player.getName());
            if (save == null) {
                return;
            }
            updateMagneticPlayer(player, save);
            save.updateOpChange();
            save.save();
        }
    }

    private void processDisabledProperties(World world) {
        NEIServerUtils.advanceDisabledTimes(world);
        if (NEIServerUtils.isRaining(world) && NEIServerConfig.isActionDisabled(world.provider.getDimension(), "rain")) {
            NEIServerUtils.toggleRaining(world, false);
        }
    }

    private void updateMagneticPlayer(EntityPlayerMP player, PlayerSave save) {
        if (!save.isActionEnabled("magnet") || player.isDead) {
            return;
        }

        float distancexz = 16;
        float distancey = 8;
        double maxspeedxz = 0.5;
        double maxspeedy = 0.5;
        double speedxz = 0.05;
        double speedy = 0.07;
        List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, player.getEntityBoundingBox().expand(distancexz, distancey, distancexz));
        for (EntityItem item : items) {
            if (item.cannotPickup()) {
                continue;
            }
            if (!NEIServerUtils.canItemFitInInventory(player, item.getEntityItem())) {
                continue;
            }
            if (save.magneticItems.add(item)) {
                NEIServerPacketHandler.sendAddMagneticItemTo(player, item);
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
            if (absvy > 1) {
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
        NEIServerPacketHandler.sendHasServerSideTo((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void logoutEvent(PlayerLoggedOutEvent event) {
        NEIServerConfig.unloadPlayer(event.player);
    }

    @SubscribeEvent
    public void dimChangeEvent(PlayerChangedDimensionEvent event) {
        NEIServerConfig.forPlayer(event.player.getName()).onWorldReload();
    }

    @SubscribeEvent
    public void loginEvent(PlayerRespawnEvent event) {
        NEIServerConfig.forPlayer(event.player.getName()).onWorldReload();
    }
}
