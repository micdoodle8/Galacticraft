package codechicken.nei;

import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.*;

import static codechicken.nei.NEIServerUtils.*;

public class FastTransferManager
{
    /**
     * Based on the general assumption that we want to fill top to bottom, left to right
     */
    public static class SlotPositionComparator implements Comparator<Integer>
    {
        Container container;

        public SlotPositionComparator(Container c) {
            container = c;
        }

        @Override
        public int compare(Integer arg0, Integer arg1) {
            Slot slot1 = container.getSlot(arg0);
            Slot slot2 = container.getSlot(arg1);

            if (slot2.yDisplayPosition != slot1.yDisplayPosition)
                return slot1.yDisplayPosition - slot2.yDisplayPosition;
            return slot1.xDisplayPosition - slot2.xDisplayPosition;
        }
    }

    public LinkedList<LinkedList<Integer>> slotZones = new LinkedList<LinkedList<Integer>>();
    public HashMap<Integer, Integer> slotZoneMap = new HashMap<Integer, Integer>();

    private void generateSlotMap(Container container, ItemStack stack) {
        stack = stack.copy();
        stack.stackSize = 1;

        for (int slotNo = 0; slotNo < container.inventorySlots.size(); slotNo++) {
            if (slotZoneMap.containsKey(slotNo) || !container.getSlot(slotNo).isItemValid(stack))
                continue;

            HashSet<Integer> connectedSlots = new HashSet<Integer>();
            findConnectedSlots(container, slotNo, connectedSlots);

            LinkedList<Integer> zoneSlots = new LinkedList<Integer>(connectedSlots);
            Collections.sort(zoneSlots, new SlotPositionComparator(container));
            slotZones.add(zoneSlots);

            for (int i : zoneSlots) {
                slotZoneMap.put(i, slotZones.size() - 1);
            }
        }
    }

    private void findConnectedSlots(Container container, int slotNo, HashSet<Integer> connectedSlots) {
        connectedSlots.add(slotNo);
        Slot slot = container.getSlot(slotNo);
        final int threshold = 18;

        for (int i = 0; i < container.inventorySlots.size(); i++) {
            if (connectedSlots.contains(i))
                continue;

            Slot slot1 = container.getSlot(i);
            if (Math.abs(slot.xDisplayPosition - slot1.xDisplayPosition) <= threshold && Math.abs(slot.yDisplayPosition - slot1.yDisplayPosition) <= threshold) {
                findConnectedSlots(container, i, connectedSlots);
            }
        }
    }

    public static int findSlotWithItem(Container container, ItemStack teststack) {
        for (int slotNo = 0; slotNo < container.inventorySlots.size(); slotNo++) {
            ItemStack stack = container.getSlot(slotNo).getStack();
            if (stack != null && areStacksSameType(stack, teststack))
                return slotNo;
        }
        return -1;
    }

    public static void clearSlots(Container container) {
        for (int slotNo = 0; slotNo < container.inventorySlots.size(); slotNo++)
            ((Slot) container.inventorySlots.get(slotNo)).putStack(null);
    }

    public void performMassTransfer(GuiContainer window, int fromSlot, int toSlot, ItemStack heldStack) {
        generateSlotMap(window.inventorySlots, heldStack);

        Integer fromZone = slotZoneMap.get(fromSlot);
        Integer toZone = slotZoneMap.get(toSlot);

        if (fromZone == null || toZone == null || fromZone.equals(toZone))
            return;

        if (NEIClientUtils.getHeldItem() != null && !areStacksSameType(heldStack, NEIClientUtils.getHeldItem()))
            return;

        if (!fillZoneWithHeldItem(window, toZone))
            return;

        for (int transferFrom : slotZones.get(fromZone)) {
            ItemStack transferStack = window.inventorySlots.getSlot(transferFrom).getStack();

            if (!areStacksSameType(heldStack, transferStack))
                continue;

            clickSlot(window, transferFrom);
            if (!fillZoneWithHeldItem(window, toZone)) {
                clickSlot(window, transferFrom);
                return;
            }
        }
    }

    /**
     * @return The slot that one item from the source slot will end up in apon shift clicking, -1 if none.
     */
    public int findShiftClickDestinationSlot(Container container, int fromSlot) {
        LinkedList<ItemStack> save = saveContainer(container);

        Slot slot = container.getSlot(fromSlot);
        ItemStack stack = slot.getStack();
        if (stack == null)
            return -1;

        stack.stackSize = 1;
        slot.putStack(stack.copy());

        LinkedList<ItemStack> compareBefore = saveContainer(container);
        container.slotClick(fromSlot, 0, 1, NEIClientUtils.mc().thePlayer);
        LinkedList<ItemStack> compareAfter = saveContainer(container);

        try {
            //if(compareAfter.get(fromSlot) != null)//transfer failed
            //return -1;

            for (int i = 0; i < compareBefore.size(); i++) {
                if (i == fromSlot)
                    continue;

                ItemStack before = compareBefore.get(i);
                ItemStack after = compareAfter.get(i);

                if (!areStacksIdentical(before, after) && after != null)
                    if (before == null ? areStacksSameType(stack, after) ://transfered into this empty slot
                            areStacksSameType(stack, after) && after.stackSize - before.stackSize > 0)//it added to this stack
                        return i;
            }

            return -1;
        } finally {
            restoreContainer(container, save);
        }
    }

    public LinkedList<ItemStack> saveContainer(Container container) {
        LinkedList<ItemStack> stacks = new LinkedList<ItemStack>();
        for (int i = 0; i < container.inventorySlots.size(); i++)
            stacks.add(copyStack(container.getSlot(i).getStack()));

        return stacks;
    }

    public void restoreContainer(Container container, LinkedList<ItemStack> items) {
        for (int i = 0; i < container.inventorySlots.size(); i++) {
            container.getSlot(i).putStack(items.get(i));
        }

        container.slotClick(-999, 0, 0, NEIClientUtils.mc().thePlayer);
    }

    public void transferItem(GuiContainer window, int fromSlot) {
        int toSlot = findShiftClickDestinationSlot(window.inventorySlots, fromSlot);
        if (toSlot == -1)
            return;

        Slot from = window.inventorySlots.getSlot(fromSlot);

        if (from.isItemValid(from.getStack()))
            moveOneItem(window, fromSlot, toSlot);
        else//slots that you can't put stuff in
            moveOutputSet(window, fromSlot, toSlot);
    }

    public void moveOutputSet(GuiContainer window, int fromSlot, int toSlot) {
        if (NEIClientUtils.getHeldItem() != null)
            return;

        clickSlot(window, fromSlot);//pickup fromSlot
        if (NEIClientUtils.getHeldItem() == null)//maybe this container does auto transfers. No need to pick up the final item
            return;
        clickSlot(window, toSlot);//place one in toSlot
    }

    public void moveOneItem(GuiContainer window, int fromSlot, int toSlot) {
        clickSlot(window, fromSlot);//pickup fromSlot
        clickSlot(window, toSlot, 1);//place one in toSlot
        clickSlot(window, fromSlot);//place down in fromSlot
    }

    public void retrieveItem(GuiContainer window, int toSlot) {
        Slot slot = window.inventorySlots.getSlot(toSlot);
        ItemStack slotStack = slot.getStack();
        if (slotStack == null ||
                slotStack.stackSize == slot.getSlotStackLimit() ||
                slotStack.stackSize == slotStack.getMaxStackSize())
            return;

        generateSlotMap(window.inventorySlots, slotStack);

        Integer destZone = slotZoneMap.get(toSlot);
        if (destZone == null)//slots that don't accept
            return;

        int firstZoneSlot = findShiftClickDestinationSlot(window.inventorySlots, toSlot);
        int firstZone = -1;
        if (firstZoneSlot != -1) {
            Integer integer = slotZoneMap.get(firstZoneSlot);
            if (integer != null) {
                firstZone = integer;
                if (retrieveItemFromZone(window, firstZone, toSlot))
                    return;
            }
        }

        for (int zone = 0; zone < slotZones.size(); zone++) {
            if (zone == destZone || zone == firstZone)
                continue;

            if (retrieveItemFromZone(window, zone, toSlot))
                return;
        }

        retrieveItemFromZone(window, destZone, toSlot);
    }

    private boolean retrieveItemFromZone(GuiContainer window, int zone, int toSlot) {
        ItemStack stack = window.inventorySlots.getSlot(toSlot).getStack();
        for (int i : slotZones.get(zone)) {
            if (i == toSlot)
                continue;

            Slot slot = window.inventorySlots.getSlot(i);
            ItemStack stack1 = slot.getStack();

            if (areStacksSameType(stack, stack1) &&
                    stack1.stackSize != slot.getSlotStackLimit() && //get from full stacks on second pass
                    stack1.stackSize != stack1.getMaxStackSize()) {
                moveOneItem(window, i, toSlot);
                return true;
            }
        }

        for (int i : slotZones.get(zone)) {
            if (i == toSlot)
                continue;

            Slot slot = window.inventorySlots.getSlot(i);
            ItemStack stack1 = slot.getStack();

            if (areStacksSameType(stack, stack1)) {
                moveOneItem(window, i, toSlot);
                return true;
            }
        }
        return false;
    }

    public static void clickSlot(GuiContainer window, int slotIndex) {
        clickSlot(window, slotIndex, 0);
    }

    public static void clickSlot(GuiContainer window, int slotIndex, int button) {
        clickSlot(window, slotIndex, button, 0);
    }

    public static void clickSlot(GuiContainer window, int slotIndex, int button, int modifier) {
        GuiContainerManager.getManager(window).handleSlotClick(slotIndex, button, modifier);
    }

    private boolean fillZoneWithHeldItem(GuiContainer window, int zoneIndex) {
        for (int transferTo : slotZones.get(zoneIndex)) {
            ItemStack held = NEIClientUtils.getHeldItem();

            if (held == null)
                break;

            ItemStack inToSlot = window.inventorySlots.getSlot(transferTo).getStack();

            if (!areStacksSameType(inToSlot, held))
                continue;

            clickSlot(window, transferTo);
        }

        for (int transferTo : slotZones.get(zoneIndex))//repeat on empty slots
        {
            ItemStack held = NEIClientUtils.getHeldItem();

            if (held == null)
                break;

            ItemStack inToSlot = window.inventorySlots.getSlot(transferTo).getStack();

            if (inToSlot != null)
                continue;

            clickSlot(window, transferTo);
        }

        return NEIClientUtils.getHeldItem() == null;
    }

    public void throwAll(GuiContainer window, int pickedUpFromSlot) {
        ItemStack held = NEIClientUtils.getHeldItem();
        if (held == null)
            return;

        clickSlot(window, -999);

        generateSlotMap(window.inventorySlots, held);
        for (int slotIndex : slotZones.get(slotZoneMap.get(pickedUpFromSlot))) {
            Slot slot = window.inventorySlots.getSlot(slotIndex);
            if (areStacksSameType(held, slot.getStack())) {
                clickSlot(window, slotIndex);
                clickSlot(window, -999);
            }
        }
    }
}
