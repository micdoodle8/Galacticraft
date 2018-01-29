package codechicken.lib.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by covers1624 on 3/27/2016.
 * TODO cleanup.
 */
public class ItemMultiType extends Item {

    private HashMap<Integer, String> names = new HashMap<Integer, String>();
    private HashMap<Integer, EnumRarity> rarityMap = new HashMap<Integer, EnumRarity>();
    private int nextVariant = 0;
    private boolean hasRegistered = false;
    private boolean registerToStackRegistry = false;

    /**
     * @param registryName The name to be registered with."
     */
    public ItemMultiType(CreativeTabs tab, String registryName) {
        setRegistryName(registryName);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(tab);
        setUnlocalizedName(new ResourceLocation(registryName).getResourcePath());
        setMaxStackSize(64);
    }

    @Override
    public ItemMultiType setUnlocalizedName(String unlocalizedName) {
        super.setUnlocalizedName(unlocalizedName);
        return this;
    }

    public ItemMultiType setUseStackRegistry() {
        registerToStackRegistry = true;
        return this;
    }

    public ItemStack registerSubItem(int meta, String name) {
        if (names.containsKey(meta)) {
            FMLLog.warning("[ItemMultiType.%s]: Variant %s with meta %s is already registered to %s with meta %s", getRegistryName(), name, meta, names.get(meta), meta);
        }
        names.put(meta, name);
        ItemStack stack = new ItemStack(this, 1, meta);
        incVariant();
        return stack;
    }

    public ItemStack registerSubItem(int meta, String name, EnumRarity rarity) {
        rarityMap.put(meta, rarity);
        return registerSubItem(meta, name);
    }

    public ItemStack registerSubItemOreDict(int meta, String name) {
        ItemStack stack = registerSubItem(meta, name);
        OreDictionary.registerOre(name, stack);
        if (registerToStackRegistry) {
            ItemStackRegistry.registerCustomItemStack(name, stack);
        }
        return stack;
    }

    public ItemStack registerSubItemOreDict(int meta, String name, EnumRarity rarity) {
        rarityMap.put(meta, rarity);
        return registerSubItemOreDict(meta, name);
    }

    public ItemStack registerSubItem(String name) {
        return registerSubItem(nextVariant, name);
    }

    public ItemStack registerSubItem(String name, EnumRarity rarity) {
        rarityMap.put(nextVariant, rarity);
        return registerSubItem(name);
    }

    public ItemStack registerSubItemOreDict(String name) {
        ItemStack stack = registerSubItem(nextVariant, name);
        OreDictionary.registerOre(name, stack);
        if (registerToStackRegistry) {
            ItemStackRegistry.registerCustomItemStack(name, stack);
        }
        return stack;
    }

    public ItemStack registerSubItemOreDict(String name, EnumRarity rarity) {
        rarityMap.put(nextVariant, rarity);
        return registerSubItemOreDict(name);
    }

    private void incVariant() {
        if (names.containsKey(nextVariant)) {
            nextVariant++;
            while (names.containsKey(nextVariant)) {
                nextVariant++;
            }
        }
    }

    @SuppressWarnings ("ConstantConditions")
    @SideOnly (Side.CLIENT)
    public void registerModelVariants() {
        if (!hasRegistered) {
            for (Map.Entry<Integer, String> entry : names.entrySet()) {
                ModelResourceLocation location = new ModelResourceLocation(getRegistryName(), "type=" + entry.getValue().toLowerCase());
                ModelLoader.setCustomModelResourceLocation(this, entry.getKey(), location);
            }
            hasRegistered = true;
        }
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Map.Entry<Integer, String> entry : names.entrySet()) {
            subItems.add(new ItemStack(itemIn, 1, entry.getKey()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        return getUnlocalizedName() + "." + names.get(meta);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        if (rarityMap.containsKey(stack.getItemDamage())) {
            return rarityMap.get(stack.getItemDamage());
        }
        return super.getRarity(stack);
    }
}
