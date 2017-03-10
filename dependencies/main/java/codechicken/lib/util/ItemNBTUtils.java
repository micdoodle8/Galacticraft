package codechicken.lib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

/**
 * Created by covers1624 on 6/30/2016.
 */
public class ItemNBTUtils {

    /**
     * Checks if an ItemStack has an NBTTag.
     *
     * @param stack Stack to check.
     * @return If tag is in existence.
     */
    public static boolean hasTag(ItemStack stack) {
        return stack.hasTagCompound();
    }

    /**
     * Sets the tag on an ItemStack.
     *
     * @param stack       Stack to set tag on.
     * @param tagCompound Tag to set on item.
     */
    public static void setTag(ItemStack stack, NBTTagCompound tagCompound) {
        stack.setTagCompound(tagCompound);
    }

    /**
     * Gets the NBTTag associated with an ItemStack.
     *
     * @param stack Stack to get tag from.
     * @return Tag from the ItemStack.
     */
    public static NBTTagCompound getTag(ItemStack stack) {
        return stack.getTagCompound();
    }

    /**
     * Checks if an NBTTag exists on an item and if not it will create a new one.
     *
     * @param stack Stack to check.
     * @return The Tag on the item.
     */
    public static NBTTagCompound validateTagExists(ItemStack stack) {
        if (!hasTag(stack)) {
            setTag(stack, new NBTTagCompound());
        }
        return getTag(stack);
    }

    /**
     * Checks if an ItemStack has an NBTTag on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to check.
     * @param key   Key to check for.
     * @return False if key does not exist or if the tag does not exist.
     */
    public static boolean hasKey(ItemStack stack, String key) {
        return hasTag(stack) && getTag(stack).hasKey(key);
    }

    /**
     * Checks if an ItemStack has an NBTTag of a specific type and name.
     *
     * @param stack   Stack to check.
     * @param key     Key to check for.
     * @param nbtType Primitive NBT Type.
     * @return False if key does not exist or if the tag does not exist.
     */
    public static boolean hasKey(ItemStack stack, String key, int nbtType) {
        return hasTag(stack) && getTag(stack).hasKey(key, nbtType);
    }

    /**
     * Removes a key from the ItemStacks NBTTag.
     *
     * @param stack Stack to edit.
     * @param key   Key to remove.
     */
    public static void removeTag(ItemStack stack, String key) {
        if (hasTag(stack)) {
            getTag(stack).removeTag(key);
        }
    }

    //region Setters

    /**
     * Sets a byte on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param b     Value.
     */
    public static void setByte(ItemStack stack, String key, byte b) {
        validateTagExists(stack);
        getTag(stack).setByte(key, b);
    }

    /**
     * Sets a short on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param s     Value.
     */
    public static void setShort(ItemStack stack, String key, short s) {
        validateTagExists(stack);
        getTag(stack).setShort(key, s);
    }

    /**
     * Sets a int on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param i     Value.
     */
    public static void setInteger(ItemStack stack, String key, int i) {
        validateTagExists(stack);
        getTag(stack).setInteger(key, i);
    }

    /**
     * Sets a long on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param l     Value.
     */
    public static void setLong(ItemStack stack, String key, long l) {
        validateTagExists(stack);
        getTag(stack).setLong(key, l);
    }

    /**
     * Sets a UUID on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param uuid  Value.
     */
    public static void setUUID(ItemStack stack, String key, UUID uuid) {
        validateTagExists(stack);
        getTag(stack).setUniqueId(key, uuid);
    }

    /**
     * Sets a float on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param f     Value.
     */
    public static void setFloat(ItemStack stack, String key, float f) {
        validateTagExists(stack);
        getTag(stack).setFloat(key, f);
    }

    /**
     * Sets a double on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param d     Value.
     */
    public static void setDouble(ItemStack stack, String key, double d) {
        validateTagExists(stack);
        getTag(stack).setDouble(key, d);
    }

    /**
     * Sets a String on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param s     Value.
     */
    public static void setString(ItemStack stack, String key, String s) {
        validateTagExists(stack);
        getTag(stack).setString(key, s);
    }

    /**
     * Sets a byte array on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param b     Value.
     */
    public static void setByteArray(ItemStack stack, String key, byte[] b) {
        validateTagExists(stack);
        getTag(stack).setByteArray(key, b);
    }

    /**
     * Sets a int array on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param i     Value.
     */
    public static void setIntArray(ItemStack stack, String key, int[] i) {
        validateTagExists(stack);
        getTag(stack).setIntArray(key, i);
    }

    /**
     * Sets a boolean on an ItemStack with the specified Key and Value.
     *
     * @param stack Stack to set.
     * @param key   Key.
     * @param b     Value.
     */
    public static void setBoolean(ItemStack stack, String key, boolean b) {
        validateTagExists(stack);
        getTag(stack).setBoolean(key, b);
    }

    //endregion

    //region Getters

    /**
     * Gets a byte from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static byte getByte(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getByte(key);
    }

    /**
     * Gets a short from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static short getShort(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getShort(key);
    }

    /**
     * Gets a int from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static int getInteger(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getInteger(key);
    }

    /**
     * Gets a long from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static long getLong(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getLong(key);
    }

    /**
     * Gets a UUID from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static UUID getUUID(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getUniqueId(key);
    }

    /**
     * Gets a float from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static float getFloat(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getFloat(key);
    }

    /**
     * Gets a double from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static double getDouble(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getDouble(key);
    }

    /**
     * Gets a String from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static String getString(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getString(key);
    }

    /**
     * Gets a byte array from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static byte[] getByteArray(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getByteArray(key);
    }

    /**
     * Gets a int array from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static int[] getIntArray(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getIntArray(key);
    }

    /**
     * Gets a boolean from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static boolean getBoolean(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getBoolean(key);
    }

    /**
     * Gets a NBTTagCompound from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @return Value.
     */
    public static NBTTagCompound getCompoundTag(ItemStack stack, String key) {
        validateTagExists(stack);
        return getTag(stack).getCompoundTag(key);
    }

    /**
     * Gets a NBTTagList from an ItemStacks NBTTag.
     *
     * @param stack Stack key exists on.
     * @param key   Key for the value.
     * @param type  Primitive NBT Type the List should be made up of.
     * @return Value.
     */
    public static NBTTagList getTagList(ItemStack stack, String key, int type) {
        validateTagExists(stack);
        return getTag(stack).getTagList(key, type);
    }

    //endregion

}
