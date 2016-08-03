package codechicken.lib.data;

import com.google.common.base.Charsets;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class MCDataIO {
    /**
     * PacketBuffer.readVarIntFromBuffer
     */
    public static int readVarInt(MCDataInput in) {
        int i = 0;
        int j = 0;
        byte b0;

        do {
            b0 = in.readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public static int readVarShort(MCDataInput in) {
        int low = in.readUShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = in.readUByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    public static String readString(MCDataInput in) {
        return new String(in.readArray(in.readVarInt()), Charsets.UTF_8);
    }

    public static ItemStack readItemStack(MCDataInput in) {
        ItemStack item = null;
        short itemID = in.readShort();

        if (itemID >= 0) {
            int stackSize = in.readVarInt();
            short damage = in.readShort();
            item = new ItemStack(Item.getItemById(itemID), stackSize, damage);
            item.setTagCompound(in.readNBTTagCompound());
        }

        return item;
    }

    public static FluidStack readFluidStack(MCDataInput in) {
        FluidStack fluid = null;
        short fluidID = in.readShort();

        if (fluidID >= 0) {
            fluid = new FluidStack(FluidRegistry.getFluid(fluidID), in.readVarInt(), in.readNBTTagCompound());
        }

        return fluid;
    }

    /**
     * PacketBuffer.writeVarIntToBuffer
     */
    public static void writeVarInt(MCDataOutput out, int i) {
        while ((i & 0x80) != 0) {
            out.writeByte(i & 0x7F | 0x80);
            i >>>= 7;
        }

        out.writeByte(i);
    }

    /**
     * ByteBufUtils.readVarShort
     */
    public static void writeVarShort(MCDataOutput out, int s) {
        int low = s & 0x7FFF;
        int high = (s & 0x7F8000) >> 15;
        if (high != 0) {
            low |= 0x8000;
        }
        out.writeShort(low);
        if (high != 0) {
            out.writeByte(high);
        }
    }

    /**
     * PacketBuffer.writeString
     */
    public static void writeString(MCDataOutput out, String string) {
        byte[] abyte = string.getBytes(Charsets.UTF_8);
        if (abyte.length > 32767) {
            throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + 32767 + ")");
        }

        out.writeVarInt(abyte.length);
        out.writeArray(abyte);
    }

    /**
     * Supports large stacks by writing stackSize as a varInt
     */
    public static void writeItemStack(MCDataOutput out, ItemStack stack) {
        if (stack == null) {
            out.writeShort(-1);
        } else {
            out.writeShort(Item.getIdFromItem(stack.getItem()));
            out.writeVarInt(stack.stackSize);
            out.writeShort(stack.getItemDamage());
            out.writeNBTTagCompound(stack.getItem().getShareTag() ? stack.getTagCompound() : null);
        }
    }

    public static void writeFluidStack(MCDataOutput out, FluidStack fluid) {
        if (fluid == null) {
            out.writeShort(-1);
        } else {
            out.writeShort(fluid.getFluid().getID());
            out.writeVarInt(fluid.amount);
            out.writeNBTTagCompound(fluid.tag);
        }
    }
}
