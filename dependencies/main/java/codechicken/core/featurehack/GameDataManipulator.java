package codechicken.core.featurehack;

import codechicken.lib.asm.ObfMapping;
import net.minecraft.item.Item;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.RegistrySimple;

import java.lang.reflect.Field;
import java.util.Map;

public class GameDataManipulator
{
    public static Field f_registryObjects;
    public static Field f_underlyingIntegerMap;

    static
    {
        try {
            f_registryObjects = RegistrySimple.class.getDeclaredField(
                    new ObfMapping("net/minecraft/util/RegistrySimple", "field_82596_a", "Ljava/util/Map;").toRuntime().s_name);
            f_registryObjects.setAccessible(true);
            f_underlyingIntegerMap = RegistryNamespaced.class.getDeclaredField(
                    new ObfMapping("net/minecraft/util/RegistryNamespaced", "field_148759_a", "Lnet/minecraft/util/ObjectIntIdentityMap;").toRuntime().s_name);
            f_underlyingIntegerMap.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void replaceItem(int id, Item item) {
        try {
            String name = Item.itemRegistry.getNameForObject(Item.getItemById(id));
            ((Map)f_registryObjects.get(Item.itemRegistry)).put(name, item);
            ((ObjectIntIdentityMap)f_underlyingIntegerMap.get(Item.itemRegistry)).func_148746_a(item, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
