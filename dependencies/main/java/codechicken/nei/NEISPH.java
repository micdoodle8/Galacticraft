package codechicken.nei;

import codechicken.core.CommonUtils;
import codechicken.core.IGuiPacketSender;
import codechicken.core.ServerUtils;
import codechicken.core.inventory.ContainerExtended;
import codechicken.core.inventory.SlotDummy;
import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustom.IServerPacketHandler;
import codechicken.lib.vec.BlockCoord;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.Set;

public class NEISPH implements IServerPacketHandler
{
    @Override
    public void handlePacket(PacketCustom packet, EntityPlayerMP sender, INetHandlerPlayServer netHandler) {
        if (!NEIServerConfig.authenticatePacket(sender, packet))
            return;

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
                sender.sendContainerAndContentsToPlayer(sender.openContainer, sender.openContainer.getInventory());
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
                handleMobSpawnerID(sender.worldObj, packet.readCoord(), packet.readString());
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
        int slotNumber = packet.readShort();
        ItemStack stack = packet.readItemStack(true);

        Slot slot = sender.openContainer.getSlot(slotNumber);
        if (slot instanceof SlotDummy)
            slot.putStack(stack);
    }

    private void handleContainerPacket(EntityPlayerMP sender, PacketCustom packet) {
        if (sender.openContainer instanceof ContainerExtended)
            ((ContainerExtended) sender.openContainer).handleInputPacket(packet);
    }

    private void handleMobSpawnerID(World world, BlockCoord coord, String mobtype) {
        TileEntity tile = world.getTileEntity(coord.x, coord.y, coord.z);
        if (tile instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner) tile).func_145881_a().setEntityName(mobtype);
            tile.markDirty();
            world.markBlockForUpdate(coord.x, coord.y, coord.z);
        }
    }

    private void handlePropertyChange(EntityPlayerMP sender, PacketCustom packet) {
        String name = packet.readString();
        if (NEIServerConfig.canPlayerPerformAction(sender.getCommandSenderName(), name))
            NEIServerConfig.disableAction(sender.dimension, name, packet.readBoolean());
    }

    private void processCreativeInv(EntityPlayerMP sender, boolean open) {
        if (open) {
            ServerUtils.openSMPContainer(sender, new ContainerCreativeInv(sender, new ExtendedCreativeInv(NEIServerConfig.forPlayer(sender.getCommandSenderName()), Side.SERVER)), new IGuiPacketSender()
            {
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
        NEIServerUtils.givePlayerItem(player, packet.readItemStack(true), packet.readBoolean(), packet.readBoolean());
    }

    private void setInventorySlot(EntityPlayerMP player, PacketCustom packet) {
        boolean container = packet.readBoolean();
        int slot = packet.readShort();
        ItemStack item = packet.readItemStack();

        ItemStack old = NEIServerUtils.getSlotContents(player, slot, container);
        boolean deleting = item == null || old != null && NEIServerUtils.areStacksSameType(item, old) && item.stackSize < old.stackSize;
        if (NEIServerConfig.canPlayerPerformAction(player.getCommandSenderName(), deleting ? "delete" : "item"))
            NEIServerUtils.setSlotContents(player, slot, item, container);
    }

    private void modifyEnchantment(EntityPlayerMP player, int e, int lvl, boolean add) {
        ContainerEnchantmentModifier containerem = (ContainerEnchantmentModifier) player.openContainer;
        if (add) {
            containerem.addEnchantment(e, lvl);
        } else {
            containerem.removeEnchantment(e);
        }
    }

    private void openEnchantmentGui(EntityPlayerMP player) {
        ServerUtils.openSMPContainer(player, new ContainerEnchantmentModifier(player.inventory, player.worldObj, 0, 0, 0), new IGuiPacketSender()
        {
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
        for (int i = 0; i < b.getSizeInventory(); i++)
            b.setInventorySlotContents(i, packet.readItemStack());
        ServerUtils.openSMPContainer(player, new ContainerPotionCreator(player.inventory, b), new IGuiPacketSender()
        {
            @Override
            public void sendPacket(EntityPlayerMP player, int windowId) {
                PacketCustom packet = new PacketCustom(channel, 24);
                packet.writeByte(windowId);
                packet.sendToPlayer(player);
            }
        });
    }

    public static void sendActionDisabled(int dim, String name, boolean disable) {
        new PacketCustom(channel, 11)
                .writeString(name)
                .writeBoolean(disable)
                .sendToDimension(dim);
    }

    public static void sendActionEnabled(EntityPlayerMP player, String name, boolean enable) {
        new PacketCustom(channel, 12)
                .writeString(name)
                .writeBoolean(enable)
                .sendToPlayer(player);
    }

    private void sendLoginState(EntityPlayerMP player) {
        LinkedList<String> actions = new LinkedList<String>();
        LinkedList<String> disabled = new LinkedList<String>();
        LinkedList<String> enabled = new LinkedList<String>();
        LinkedList<ItemStack> bannedItems = new LinkedList<ItemStack>();
        PlayerSave playerSave = NEIServerConfig.forPlayer(player.getCommandSenderName());

        for (String name : NEIActions.nameActionMap.keySet()) {
            if (NEIServerConfig.canPlayerPerformAction(player.getCommandSenderName(), name))
                actions.add(name);
            if (NEIServerConfig.isActionDisabled(player.dimension, name))
                disabled.add(name);
            if (playerSave.isActionEnabled(name))
                enabled.add(name);
        }
        for (ItemStackMap.Entry<Set<String>> entry : NEIServerConfig.bannedItems.entries())
            if (!NEIServerConfig.isPlayerInList(player.getCommandSenderName(), entry.value, true))
                bannedItems.add(entry.key);

        PacketCustom packet = new PacketCustom(channel, 10);

        packet.writeByte(actions.size());
        for (String s : actions)
            packet.writeString(s);

        packet.writeByte(disabled.size());
        for (String s : disabled)
            packet.writeString(s);

        packet.writeByte(enabled.size());
        for (String s : enabled)
            packet.writeString(s);

        packet.writeInt(bannedItems.size());
        for (ItemStack stack : bannedItems)
            packet.writeItemStack(stack);

        packet.sendToPlayer(player);
    }

    public static void sendHasServerSideTo(EntityPlayerMP player) {
        NEIServerConfig.logger.debug("Sending serverside check to: " + player.getCommandSenderName());
        PacketCustom packet = new PacketCustom(channel, 1);
        packet.writeByte(NEIActions.protocol);
        packet.writeString(CommonUtils.getWorldName(player.worldObj));

        packet.sendToPlayer(player);
    }

    public static void sendAddMagneticItemTo(EntityPlayerMP player, EntityItem item) {
        PacketCustom packet = new PacketCustom(channel, 13);
        packet.writeInt(item.getEntityId());

        packet.sendToPlayer(player);
    }

    public static final String channel = "NEI";
}
