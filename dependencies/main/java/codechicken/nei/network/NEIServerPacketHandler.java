package codechicken.nei.network;

import codechicken.lib.gui.IGuiPacketSender;
import codechicken.core.inventory.ContainerExtended;
import codechicken.core.inventory.SlotDummy;
import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustom.IServerPacketHandler;
import codechicken.lib.util.ServerUtils;
import codechicken.nei.*;
import codechicken.nei.container.ContainerCreativeInv;
import codechicken.nei.container.ExtendedCreativeInv;
import codechicken.nei.util.LogHelper;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedList;
import java.util.Set;

public class NEIServerPacketHandler implements IServerPacketHandler {
    @Override
    public void handlePacket(PacketCustom packet, EntityPlayerMP sender, INetHandlerPlayServer netHandler) {
        if (!NEIServerConfig.authenticatePacket(sender, packet)) {
            return;
        }

        switch (packet.getType()) {
        case 1:
            handleGiveItem(sender, packet);
            break;
        case 4:
            NEIServerUtils.deleteAllItems(sender);
            break;
        case 5:
            setInventorySlot(sender, packet);
            break;
        case 6:
            NEIServerUtils.toggleMagnetMode(sender);
            break;
        case 7:
            NEIServerUtils.setHourForward(sender.worldObj, packet.readUByte(), true);
            break;
        case 8:
            NEIServerUtils.healPlayer(sender);
            break;
        case 9:
            NEIServerUtils.toggleRaining(sender.worldObj, true);
            break;
        case 10:
            sendLoginState(sender);
            break;
        case 11:
            sender.updateCraftingInventory(sender.openContainer, sender.openContainer.getInventory());
            break;
        case 12:
            handlePropertyChange(sender, packet);
            break;
        case 13:
            NEIServerUtils.setGamemode(sender, packet.readUByte());
            break;
        case 14:
            NEIServerUtils.cycleCreativeInv(sender, packet.readInt());
            break;
        case 15:
            handleMobSpawnerID(sender.worldObj, packet.readPos(), packet.readString());
            break;
        case 20:
            handleContainerPacket(sender, packet);
            break;
        case 21:
            openEnchantmentGui(sender);
            break;
        case 22:
            modifyEnchantment(sender, packet.readUByte(), packet.readUByte(), packet.readBoolean());
            break;
        case 23:
            processCreativeInv(sender, packet.readBoolean());
            break;
        case 24:
            openPotionGui(sender, packet);
            break;
        case 25:
            handleDummySlotSet(sender, packet);
            break;
        }
    }

    private void handleDummySlotSet(EntityPlayerMP sender, PacketCustom packet) {
        Slot slot = sender.openContainer.getSlot(packet.readShort());
        if (slot instanceof SlotDummy) {
            slot.putStack(packet.readItemStack());
        }
    }

    private void handleContainerPacket(EntityPlayerMP sender, PacketCustom packet) {
        if (sender.openContainer instanceof ContainerExtended) {
            ((ContainerExtended) sender.openContainer).handleInputPacket(packet);
        }
    }

    private void handleMobSpawnerID(World world, BlockPos coord, String mobtype) {
        TileEntity tile = world.getTileEntity(coord);
        if (tile instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner) tile).getSpawnerBaseLogic().setEntityName(mobtype);
            tile.markDirty();
            IBlockState state = world.getBlockState(coord);
            world.notifyBlockUpdate(coord, state, state, 4);
        }
    }

    private void handlePropertyChange(EntityPlayerMP sender, PacketCustom packet) {
        String name = packet.readString();
        if (NEIServerConfig.canPlayerPerformAction(sender.getName(), name)) {
            NEIServerConfig.disableAction(sender.dimension, name, packet.readBoolean());
        }
    }

    public static void processCreativeInv(EntityPlayerMP sender, boolean open) {
        if (open) {
            ServerUtils.openSMPContainer(sender, new ContainerCreativeInv(sender, new ExtendedCreativeInv(NEIServerConfig.forPlayer(sender.getName()), Side.SERVER)), new IGuiPacketSender() {
                @Override
                public void sendPacket(EntityPlayerMP player, int windowId) {
                    PacketCustom packet = new PacketCustom(channel, 23);
                    packet.writeBoolean(true);
                    packet.writeByte(windowId);
                    packet.sendToPlayer(player);
                }
            });
        } else {
            sender.closeContainer();
            PacketCustom packet = new PacketCustom(channel, 23);
            packet.writeBoolean(false);
            packet.sendToPlayer(sender);
        }
    }

    private void handleGiveItem(EntityPlayerMP player, PacketCustom packet) {
        NEIServerUtils.givePlayerItem(player, packet.readItemStack(), packet.readBoolean(), packet.readBoolean());
    }

    private void setInventorySlot(EntityPlayerMP player, PacketCustom packet) {
        boolean container = packet.readBoolean();
        int slot = packet.readShort();
        ItemStack item = packet.readItemStack();

        ItemStack old = NEIServerUtils.getSlotContents(player, slot, container);
        boolean deleting = item == null || old != null && NEIServerUtils.areStacksSameType(item, old) && item.stackSize < old.stackSize;
        if (NEIServerConfig.canPlayerPerformAction(player.getName(), deleting ? "delete" : "item")) {
            NEIServerUtils.setSlotContents(player, slot, item, container);
        }
    }

    @Deprecated
    private void modifyEnchantment(EntityPlayerMP player, int e, int lvl, boolean add) {
        ContainerEnchantmentModifier containerem = (ContainerEnchantmentModifier) player.openContainer;
        if (add) {
            containerem.addEnchantment(e, lvl);
        } else {
            containerem.removeEnchantment(e);
        }
    }

    private void openEnchantmentGui(EntityPlayerMP player) {
        ServerUtils.openSMPContainer(player, new ContainerEnchantmentModifier(player.inventory, player.worldObj), new IGuiPacketSender() {
            @Override
            public void sendPacket(EntityPlayerMP player, int windowId) {
                PacketCustom packet = new PacketCustom(channel, 21);
                packet.writeByte(windowId);
                packet.sendToPlayer(player);
            }
        });
    }

    private void openPotionGui(EntityPlayerMP player, PacketCustom packet) {
        InventoryBasic b = new InventoryBasic("potionStore", true, 9);
        for (int i = 0; i < b.getSizeInventory(); i++) {
            b.setInventorySlotContents(i, packet.readItemStack());
        }
        ServerUtils.openSMPContainer(player, new ContainerPotionCreator(player.inventory, b), new IGuiPacketSender() {
            @Override
            public void sendPacket(EntityPlayerMP player, int windowId) {
                PacketCustom packet = new PacketCustom(channel, 24);
                packet.writeByte(windowId);
                packet.sendToPlayer(player);
            }
        });
    }

    public static void sendActionDisabled(int dim, String name, boolean disable) {
        new PacketCustom(channel, 11).writeString(name).writeBoolean(disable).sendToDimension(dim);
    }

    public static void sendActionEnabled(EntityPlayerMP player, String name, boolean enable) {
        new PacketCustom(channel, 12).writeString(name).writeBoolean(enable).sendToPlayer(player);
    }

    private void sendLoginState(EntityPlayerMP player) {
        LinkedList<String> actions = new LinkedList<String>();
        LinkedList<String> disabled = new LinkedList<String>();
        LinkedList<String> enabled = new LinkedList<String>();
        LinkedList<ItemStack> bannedItems = new LinkedList<ItemStack>();
        PlayerSave playerSave = NEIServerConfig.forPlayer(player.getName());

        for (String name : NEIActions.nameActionMap.keySet()) {
            if (NEIServerConfig.canPlayerPerformAction(player.getName(), name)) {
                actions.add(name);
            }
            if (NEIServerConfig.isActionDisabled(player.dimension, name)) {
                disabled.add(name);
            }
            if (playerSave.isActionEnabled(name)) {
                enabled.add(name);
            }
        }
        for (ItemStackMap.Entry<Set<String>> entry : NEIServerConfig.bannedItems.entries()) {
            if (!NEIServerConfig.isPlayerInList(player.getName(), entry.value, true)) {
                bannedItems.add(entry.key);
            }
        }

        PacketCustom packet = new PacketCustom(channel, 10);

        packet.writeByte(actions.size());
        for (String s : actions) {
            packet.writeString(s);
        }

        packet.writeByte(disabled.size());
        for (String s : disabled) {
            packet.writeString(s);
        }

        packet.writeByte(enabled.size());
        for (String s : enabled) {
            packet.writeString(s);
        }

        packet.writeInt(bannedItems.size());
        for (ItemStack stack : bannedItems) {
            packet.writeItemStack(stack);
        }

        packet.sendToPlayer(player);
    }

    public static void sendHasServerSideTo(EntityPlayerMP player) {
        LogHelper.debug("Sending serverside check to: " + player.getName());
        PacketCustom packet = new PacketCustom(channel, 1);
        packet.writeByte(NEIActions.protocol);
        packet.writeString(player.worldObj.getWorldInfo().getWorldName());

        packet.sendToPlayer(player);
    }

    public static void sendAddMagneticItemTo(EntityPlayerMP player, EntityItem item) {
        PacketCustom packet = new PacketCustom(channel, 13);
        packet.writeInt(item.getEntityId());

        packet.sendToPlayer(player);
    }

    public static final String channel = "NEI";
}
