package codechicken.lib.util;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by covers1624 on 3/27/2016.
 */
public class ArrayUtils {

    /**
     * Converts an String array to lowercase.
     *
     * @param array Array to convert.
     * @return Converted array.
     */
    public static String[] arrayToLowercase(String[] array) {
        String[] copy = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i].toLowerCase();
        }
        return copy;
    }

    /**
     * Converts and array of "key=value" to a map.
     *
     * @param array Array to convert.
     * @return Map of values.
     */
    public static Map<String, String> convertKeyValueArrayToMap(String[] array) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String entry : array) {
            String[] split = entry.split("=");
            map.put(split[0], split[1]);
        }
        return map;
    }

    /**
     * Prefixes a string array with the desired value.
     *
     * @param prefix The prefix to apply.
     * @param list   The list to apply the prefix to.
     * @return The list with the prefix applied.
     */
    public static List<String> prefixStringList(String prefix, List<String> list) {
        List<String> finalList = new ArrayList<String>();
        for (String string : list) {
            finalList.add(prefix + string);
        }
        return finalList;
    }

    /**
     * Checks if a map contains all keys passed in.
     *
     * @param map  Map to check.
     * @param keys Keys that must exist.
     * @param <T>  The type of data in the map key.
     * @return False if fail.
     */
    public static <T> boolean containsKeys(Map<T, ?> map, T... keys) {
        for (T object : keys) {
            if (!map.containsKey(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the value at the first null index in the array.
     *
     * @param array Array to add to.
     * @param value Value to add.
     * @param <T>   Type of value.
     */
    public static <T> void addToArrayFirstNull(T[] array, T value) {
        int nullIndex = -1;
        for (int i = 0; i < array.length; i++) {
            T v = array[i];
            if (v == null) {
                nullIndex = i;
                break;
            }
        }
        if (nullIndex == -1) {//We aren't able to expand it as we are using generics.
            throw new RuntimeException("Unable to add to array as it is full! Expand it before adding to it!");
        }
        array[nullIndex] = value;
    }

    /**
     * Adds all elements from the array that are not null to the list.
     *
     * @param array Array to grab from.
     * @param list  List to add to.
     * @param <T>   What we are dealing with.
     * @return The modified list.
     */
    public static <T> List<T> addAllNoNull(T[] array, List<T> list) {
        for (T value : array) {
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * Checks if the array is all null.
     *
     * @param array The array to check.
     * @param <T>   What we are dealing with.
     * @return True if the array only contains nulls.
     */
    public static <T> boolean isEmpty(T[] array) {
        for (T value : array) {
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts the elements in the array that are not null.
     *
     * @param array The array to check.
     * @param <T>   What we are dealing with.
     * @return The count of non-null objects in the array.
     */
    public static <T> int countNoNull(T[] array) {
        int counter = 0;
        for (T value : array) {
            if (value != null) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Fills the array with the specified value.
     * If the value is an instance of Copyable it will call copy.
     *
     * @param array Array to fill.
     * @param value Value to fill with.
     * @param <T>   What we are dealing with.
     */
    @SuppressWarnings ("unchecked")
    public static <T> void fillArray(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            T newValue = value;
            if (value instanceof Copyable) {
                newValue = ((Copyable<T>) value).copy();
            }
            array[i] = newValue;
        }
    }

    /**
     * Apples the Specified function to the entire array and returns a new List of the result.
     * The input to the function may be null.
     *
     * @param function The function to apply.
     * @param array    The array to apply the function to.
     * @param <I>      The Input generic.
     * @param <O>      The Output generic.
     * @return A list of the output of the specified function.
     */
    public static <I, O> List<O> applyArray(Function<I, O> function, I... array) {
        List<O> finalList = new ArrayList<O>();
        for (I i : array) {
            finalList.add(function.apply(i));
        }

        return finalList;
    }

    /**
     * Basically a wrapper for System.arraycopy with support for CCL Copyable's
     *
     * @param src     The source array.
     * @param srcPos  Starting position in the source array.
     * @param dst     The destination array.
     * @param destPos Starting position in the destination array.
     * @param length  The number of elements to copy.
     */
    public static void arrayCopy(Object src, int srcPos, Object dst, int destPos, int length) {
        System.arraycopy(src, srcPos, dst, destPos, length);
        if (dst instanceof Copyable[]) {
            Object[] oa = (Object[]) dst;
            Copyable<Object>[] c = (Copyable[]) dst;
            for (int i = destPos; i < destPos + length; i++) {
                if (c[i] != null) {
                    oa[i] = c[i].copy();
                }
            }
        }
    }

    /**
     * Returns the index of the first occurrence of the specified element in the array.
     * Will return -1 if the element is non existent in the array.
     *
     * @param array  The array to search.
     * @param object Element to find.
     * @param <T>    What we are dealing with.
     * @return The index in the array of the object.
     */
    public static <T> int indexOf(T[] array, T object) {
        if (object == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                if (object.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

}
