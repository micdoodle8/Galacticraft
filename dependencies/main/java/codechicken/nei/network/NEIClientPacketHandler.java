package codechicken.nei.network;

import codechicken.lib.util.ClientUtils;
import codechicken.lib.inventory.InventoryUtils;
import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustom.IClientPacketHandler;
import codechicken.nei.*;
import codechicken.nei.container.ContainerCreativeInv;
import codechicken.nei.container.ExtendedCreativeInv;
import codechicken.nei.util.LogHelper;
import codechicken.nei.util.NEIClientUtils;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class NEIClientPacketHandler implements IClientPacketHandler {
    public static final String channel = "NEI";

    @Override
    public void handlePacket(PacketCustom packet, Minecraft mc, INetHandlerPlayClient netHandler) {
        switch (packet.getType()) {
        case 1:
            handleSMPCheck(packet.readUByte(), packet.readString(), mc.theWorld);
            break;
        case 10:
            handleLoginState(packet);
            break;
        case 11:
            handleActionDisabled(packet);
            break;
        case 12:
            handleActionEnabled(packet);
            break;
        case 13:
            ClientHandler.instance().addSMPMagneticItem(packet.readInt(), mc.theWorld);
            break;
        case 14:
            handleGamemode(mc, packet.readUByte());
            break;
        case 21:
            ClientUtils.openSMPGui(packet.readUByte(), new GuiEnchantmentModifier(mc.thePlayer.inventory, mc.theWorld));
            break;
        case 23:
            if (packet.readBoolean()) {
                ClientUtils.openSMPGui(packet.readUByte(), new GuiExtendedCreativeInv(new ContainerCreativeInv(mc.thePlayer, new ExtendedCreativeInv(null, Side.CLIENT))));
            } else {
                mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
            }
            break;
        case 24:
            ClientUtils.openSMPGui(packet.readUByte(), new GuiPotionCreator(mc.thePlayer.inventory));
            break;
        }
    }

    private void handleGamemode(Minecraft mc, int mode) {
        mc.playerController.setGameType(NEIServerUtils.getGameType(mode));
    }

    private void handleActionEnabled(PacketCustom packet) {
        String name = packet.readString();
        if (packet.readBoolean()) {
            NEIClientConfig.enabledActions.add(name);
        } else {
            NEIClientConfig.enabledActions.remove(name);
        }
    }

    private void handleActionDisabled(PacketCustom packet) {
        String name = packet.readString();
        if (packet.readBoolean()) {
            NEIClientConfig.disabledActions.add(name);
        } else {
            NEIClientConfig.disabledActions.remove(name);
        }
    }

    private void handleLoginState(PacketCustom packet) {
        NEIClientConfig.permissableActions.clear();
        int num = packet.readUByte();
        for (int i = 0; i < num; i++) {
            NEIClientConfig.permissableActions.add(packet.readString());
        }

        NEIClientConfig.disabledActions.clear();
        num = packet.readUByte();
        for (int i = 0; i < num; i++) {
            NEIClientConfig.disabledActions.add(packet.readString());
        }

        NEIClientConfig.enabledActions.clear();
        num = packet.readUByte();
        for (int i = 0; i < num; i++) {
            NEIClientConfig.enabledActions.add(packet.readString());
        }

        NEIClientConfig.bannedBlocks.clear();
        num = packet.readInt();
        for (int i = 0; i < num; i++) {
            NEIClientConfig.bannedBlocks.add(packet.readItemStack());
        }

        if (NEIClientUtils.getGuiContainer() != null) {
            LayoutManager.instance().refresh(NEIClientUtils.getGuiContainer());
        }
    }

    private void handleSMPCheck(int serverprotocol, String worldName, World world) {
        if (serverprotocol > NEIActions.protocol) {
            NEIClientUtils.printChatMessage(new TextComponentTranslation("nei.chat.mismatch.client"));
        } else if (serverprotocol < NEIActions.protocol) {
            NEIClientUtils.printChatMessage(new TextComponentTranslation("nei.chat.mismatch.server"));
        } else {
            try {
                ClientHandler.instance().loadWorld(world);
                NEIClientConfig.setHasSMPCounterPart(true);
                NEIClientConfig.loadWorld(getSaveName(worldName));
                sendRequestLoginInfo();
            } catch (Exception e) {
                LogHelper.errorError("Error handling SMP Check", e);
            }
        }
    }

    private static String getSaveName(String worldName) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            return "local/" + ClientUtils.getWorldSaveName();
        }

        return "remote/" + ClientUtils.getServerIP().replace(':', '~') + "/" + worldName;
    }

    public static void sendGiveItem(ItemStack spawnstack, boolean infinite, boolean doSpawn) {
        PacketCustom packet = new PacketCustom(channel, 1);
        packet.writeItemStack(spawnstack);
        packet.writeBoolean(infinite);
        packet.writeBoolean(doSpawn);
        packet.sendToServer();
    }

    public static void sendDeleteAllItems() {
        PacketCustom packet = new PacketCustom(channel, 4);
        packet.sendToServer();
    }

    public static void sendStateLoad(ItemStack[] state) {
        sendDeleteAllItems();
        for (int slot = 0; slot < state.length; slot++) {
            ItemStack item = state[slot];
            if (item == null) {
                continue;
            }
            sendSetSlot(slot, item, false);
        }

        PacketCustom packet = new PacketCustom(channel, 11);
        packet.sendToServer();
    }

    public static void sendSetSlot(int slot, ItemStack stack, boolean container) {
        PacketCustom packet = new PacketCustom(channel, 5);
        packet.writeBoolean(container);
        packet.writeShort(slot);
        packet.writeItemStack(stack);
        packet.sendToServer();
    }

    private static void sendRequestLoginInfo() {
        PacketCustom packet = new PacketCustom(channel, 10);
        packet.sendToServer();
    }

    public static void sendToggleMagnetMode() {
        PacketCustom packet = new PacketCustom(channel, 6);
        packet.sendToServer();
    }

    public static void sendSetTime(int hour) {
        PacketCustom packet = new PacketCustom(channel, 7);
        packet.writeByte(hour);
        packet.sendToServer();
    }

    public static void sendHeal() {
        PacketCustom packet = new PacketCustom(channel, 8);
        packet.sendToServer();
    }

    public static void sendToggleRain() {
        PacketCustom packet = new PacketCustom(channel, 9);
        packet.sendToServer();
    }

    public static void sendOpenEnchantmentWindow() {
        PacketCustom packet = new PacketCustom(channel, 21);
        packet.sendToServer();
    }

    @Deprecated
    public static void sendModifyEnchantment(int enchID, int level, boolean add) {
        PacketCustom packet = new PacketCustom(channel, 22);
        packet.writeByte(enchID);
        packet.writeByte(level);
        packet.writeBoolean(add);
        packet.sendToServer();
    }

    public static void sendSetPropertyDisabled(String name, boolean enable) {
        PacketCustom packet = new PacketCustom(channel, 12);
        packet.writeString(name);
        packet.writeBoolean(enable);
        packet.sendToServer();
    }

    public static void sendGamemode(int mode) {
        new PacketCustom(channel, 13).writeByte(mode).sendToServer();
    }

    public static void sendCreativeInv(boolean open) {
        PacketCustom packet = new PacketCustom(channel, 23);
        packet.writeBoolean(open);
        packet.sendToServer();
    }

    public static void sendCreativeScroll(int steps) {
        PacketCustom packet = new PacketCustom(channel, 14);
        packet.writeInt(steps);
        packet.sendToServer();
    }

    public static void sendMobSpawnerID(int x, int y, int z, String mobtype) {
        PacketCustom packet = new PacketCustom(channel, 15);
        packet.writePos(new BlockPos(x, y, z));
        packet.writeString(mobtype);
        packet.sendToServer();
    }

    public static PacketCustom createContainerPacket() {
        return new PacketCustom(channel, 20);
    }

    public static void sendOpenPotionWindow() {
        ItemStack[] potionStore = new ItemStack[9];
        InventoryUtils.readItemStacksFromTag(potionStore, NEIClientConfig.global.nbt.getCompoundTag("potionStore").getTagList("items", 10));
        PacketCustom packet = new PacketCustom(channel, 24);
        for (ItemStack stack : potionStore) {
            packet.writeItemStack(stack);
        }
        packet.sendToServer();
    }

    public static void sendDummySlotSet(int slotNumber, ItemStack stack) {
        PacketCustom packet = new PacketCustom(channel, 25);
        packet.writeShort(slotNumber);
        packet.writeItemStack(stack);
        packet.sendToServer();
    }
}
