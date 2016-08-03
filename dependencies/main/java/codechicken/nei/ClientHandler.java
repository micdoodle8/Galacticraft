package codechicken.nei;

import codechicken.core.ClientUtils;
import codechicken.core.GuiModListScroll;
import codechicken.lib.packet.PacketCustom;
import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

import java.util.ArrayList;
import java.util.Iterator;

public class ClientHandler {
    private static ClientHandler instance;

    private ArrayList<EntityItem> SMPmagneticItems = new ArrayList<EntityItem>();
    private World lastworld;
    private GuiScreen lastGui;

    public void addSMPMagneticItem(int i, World world) {
        Entity e = world.getEntityByID(i);
        if (e instanceof EntityItem) {
            SMPmagneticItems.add((EntityItem) e);
        }
    }

    private void updateMagnetMode(World world, EntityPlayerSP player) {
        if (!NEIClientConfig.getMagnetMode()) {
            return;
        }

        float distancexz = 16;
        float distancey = 8;
        double maxspeedxz = 0.5;
        double maxspeedy = 0.5;
        double speedxz = 0.05;
        double speedy = 0.07;

        for (Iterator<EntityItem> iterator = SMPmagneticItems.iterator(); iterator.hasNext(); ) {
            EntityItem item = iterator.next();

            if (item.cannotPickup()) {
                continue;
            }
            if (item.isDead) {
                iterator.remove();
            }
            if (!NEIClientUtils.canItemFitInInventory(player, item.getEntityItem())) {
                continue;
            }

            double dx = player.posX - item.posX;
            double dy = player.posY + player.getEyeHeight() - item.posY;
            double dz = player.posZ - item.posZ;
            double absxz = Math.sqrt(dx * dx + dz * dz);
            double absy = Math.abs(dy);
            if (absxz > distancexz || absy > distancey) {
                continue;
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

            if (absvxz < 0.2 && absxz < 0.2) {
                item.setDead();
            }

            item.setVelocity(vx, vy, vz);
        }
    }

    public static void preInit() {
        ItemInfo.preInit();
    }

    public static void init() {
        instance = new ClientHandler();

        GuiModListScroll.register("NotEnoughItems");
        PacketCustom.assignHandler(NEICPH.channel, new NEICPH());
        FMLCommonHandler.instance().bus().register(instance);
        MinecraftForge.EVENT_BUS.register(instance);

        ItemInfo.init();
        API.registerHighlightHandler(new DefaultHighlightHandler(), ItemInfo.Layout.HEADER);
        HUDRenderer.load();
        WorldOverlayRenderer.load();
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null) {
            if (loadWorld(mc.theWorld)) {
                NEIClientConfig.setHasSMPCounterPart(false);
                NEIClientConfig.setInternalEnabled(false);

                if (!Minecraft.getMinecraft().isSingleplayer())//wait for server to initiate in singleplayer
                {
                    NEIClientConfig.loadWorld("remote/" + ClientUtils.getServerIP().replace(':', '~'));
                }
            }

            if (!NEIClientConfig.isEnabled()) {
                return;
            }

            KeyManager.tickKeyStates();

            NEIController.updateUnlimitedItems(mc.thePlayer.inventory);
            if (mc.currentScreen == null) {
                NEIController.processCreativeCycling(mc.thePlayer.inventory);
            }

            updateMagnetMode(mc.theWorld, mc.thePlayer);
        }

        GuiScreen gui = mc.currentScreen;
        if (gui != lastGui) {
            if (gui instanceof GuiMainMenu) {
                lastworld = null;
            } else if (gui instanceof GuiSelectWorld) {
                NEIClientConfig.reloadSaves();
            }
        }
        lastGui = gui;
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase == Phase.END && NEIClientConfig.isEnabled()) {
            HUDRenderer.renderOverlay();
        }
    }

    @SubscribeEvent
    public void renderLastEvent(RenderWorldLastEvent event) {
        if (NEIClientConfig.isEnabled()) {
            WorldOverlayRenderer.render(event.partialTicks);
        }
    }

    public boolean loadWorld(World world) {
        if (world != lastworld) {
            SMPmagneticItems.clear();
            WorldOverlayRenderer.reset();
            lastworld = world;
            return true;
        }
        return false;
    }

    public static ClientHandler instance() {
        return instance;
    }

    public static RuntimeException throwCME(final String message) {
        final GuiScreen errorGui = new GuiErrorScreen(null, null) {
            @Override
            public void handleMouseInput() {
            }

            @Override
            public void handleKeyboardInput() {
            }

            @Override
            public void drawScreen(int par1, int par2, float par3) {
                drawDefaultBackground();
                String[] s_msg = message.split("\n");
                for (int i = 0; i < s_msg.length; ++i) {
                    drawCenteredString(fontRendererObj, s_msg[i], width / 2, height / 3 + 12 * i, 0xFFFFFFFF);
                }
            }
        };

        @SuppressWarnings("serial")
        CustomModLoadingErrorDisplayException e = new CustomModLoadingErrorDisplayException() {
            @Override
            public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {
                Minecraft.getMinecraft().displayGuiScreen(errorGui);
            }

            @Override
            public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime) {
            }
        };
        throw e;
    }
}
