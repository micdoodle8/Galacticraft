package codechicken.nei.api;

import codechicken.nei.*;
import codechicken.nei.ItemList.ItemsLoadedCallback;
import codechicken.nei.api.ItemFilter.ItemFilterProvider;
import codechicken.nei.config.ArrayDumper;
import codechicken.nei.config.ItemPanelDumper;
import codechicken.nei.config.RegistryDumper;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.BrewingRecipeHandler;
import codechicken.nei.recipe.RecipeItemInputHandler;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.UniqueIdentifier;

import java.util.*;
import java.util.Map.Entry;

/**
 * This is an internal class for storing information about items, to be accessed by the API
 */
public class ItemInfo {
    public static enum Layout {
        HEADER, BODY, FOOTER
    }

    public static final ArrayListMultimap<Layout, IHighlightHandler> highlightHandlers = ArrayListMultimap.create();
    public static final ItemStackMap<String> nameOverrides = new ItemStackMap<String>();
    public static final ItemStackSet hiddenItems = new ItemStackSet();
    public static final ItemStackSet finiteItems = new ItemStackSet();
    public static final ArrayListMultimap<Item, ItemStack> itemOverrides = ArrayListMultimap.create();
    public static final ArrayListMultimap<Item, ItemStack> itemVariants = ArrayListMultimap.create();

    public static final LinkedList<IInfiniteItemHandler> infiniteHandlers = new LinkedList<IInfiniteItemHandler>();
    public static final ArrayListMultimap<Block, IHighlightHandler> highlightIdentifiers = ArrayListMultimap.create();
    public static final HashSet<Class<? extends Slot>> fastTransferExemptions = new HashSet<Class<? extends Slot>>();

    public static final HashMap<Item, String> itemOwners = new HashMap<Item, String>();

    //lookup optimisation
    public static final HashMap<ItemStack, String> itemSearchNames = new HashMap<ItemStack, String>();

    public static boolean isHidden(ItemStack stack) {
        return hiddenItems.contains(stack);
    }

    public static boolean isHidden(Item item) {
        return hiddenItems.containsAll(item);
    }

    public static String getNameOverride(ItemStack stack) {
        return nameOverrides.get(stack);
    }

    public static boolean canBeInfinite(ItemStack stack) {
        return !finiteItems.contains(stack);
    }

    /**
     * @deprecated Use field directly
     */
    @Deprecated
    public static List<ItemStack> getItemOverrides(Item item) {
        return itemOverrides.get(item);
    }

    public static void preInit() {
        ItemMobSpawner.register();
    }

    public static void init() {
        ItemMobSpawner.initRender();
    }

    public static void load(World world) {
        addVanillaBlockProperties();
        addDefaultDropDowns();
        searchItems();
        parseModItems();
        ItemMobSpawner.loadSpawners(world);
        addSpawnEggs();
        addInfiniteHandlers();
        addInputHandlers();
        addIDDumps();
        addHiddenItemFilter();
        addSearchOptimisation();
    }

    private static void addVanillaBlockProperties() {
        API.hideItem(new ItemStack(Blocks.farmland));
        API.hideItem(new ItemStack(Blocks.lit_furnace));
    }

    private static void addSearchOptimisation() {
        ItemList.loadCallbacks.add(new ItemsLoadedCallback() {
            @Override
            public void itemsLoaded() {
                itemSearchNames.clear();
            }
        });
    }

    private static void addHiddenItemFilter() {
        API.addItemFilter(new ItemFilterProvider() {
            @Override
            public ItemFilter getFilter() {
                return new ItemFilter() {
                    @Override
                    public boolean matches(ItemStack item) {
                        return !hiddenItems.contains(item);
                    }
                };
            }
        });
    }

    private static void addIDDumps() {
        API.addOption(new RegistryDumper<Item>("tools.dump.item") {
            @Override
            public String[] header() {
                return new String[] { "Name", "ID", "Has Block", "Mod", "Class" };
            }

            @Override
            public String[] dump(Item item, int id, String name) {
                return new String[] { name, Integer.toString(id), Boolean.toString(Block.getBlockFromItem(item) != Blocks.air), ItemInfo.itemOwners.get(item), item.getClass().getCanonicalName() };
            }

            @Override
            public RegistryNamespaced registry() {
                return Item.itemRegistry;
            }
        });
        API.addOption(new RegistryDumper<Block>("tools.dump.block") {
            @Override
            public String[] header() {
                return new String[] { "Name", "ID", "Has Item", "Mod", "Class" };
            }

            @Override
            public String[] dump(Block item, int id, String name) {
                return new String[] { name, Integer.toString(id), Boolean.toString(Item.getItemFromBlock(item) != null), ItemInfo.itemOwners.get(item), item.getClass().getCanonicalName() };
            }

            @Override
            public RegistryNamespaced registry() {
                return Block.blockRegistry;
            }
        });
        API.addOption(new ArrayDumper<Potion>("tools.dump.potion") {
            public String[] header() {
                return new String[] { "ID", "Unlocalised name", "Class" };
            }

            @Override
            public String[] dump(Potion potion, int id) {
                return new String[] { Integer.toString(id), potion.getName(), potion.getClass().getCanonicalName() };
            }

            @Override
            public Potion[] array() {
                return Potion.potionTypes;
            }
        });
        API.addOption(new ArrayDumper<Enchantment>("tools.dump.enchantment") {
            public String[] header() {
                return new String[] { "ID", "Unlocalised name", "Type", "Min Level", "Max Level", "Class" };
            }

            @Override
            public String[] dump(Enchantment ench, int id) {
                return new String[] { Integer.toString(id), ench.getName(), ench.type.toString(), Integer.toString(ench.getMinLevel()), Integer.toString(ench.getMaxLevel()), ench.getClass().getCanonicalName() };
            }

            @Override
            public Enchantment[] array() {
                return Enchantment.enchantmentsList;
            }
        });
        API.addOption(new ArrayDumper<BiomeGenBase>("tools.dump.biome") {
            @Override
            public String[] header() {
                return new String[] { "ID", "Name", "Temperature", "Rainfall", "Spawn Chance", "Root Height", "Height Variation", "Types", "Class" };
            }

            @Override
            public String[] dump(BiomeGenBase biome, int id) {
                BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
                StringBuilder s_types = new StringBuilder();
                for (BiomeDictionary.Type t : types) {
                    if (s_types.length() > 0) {
                        s_types.append(", ");
                    }
                    s_types.append(t.name());
                }

                return new String[] { Integer.toString(id), biome.biomeName, Float.toString(biome.getFloatTemperature(BlockPos.ORIGIN)), Float.toString(biome.getFloatRainfall()), Float.toString(biome.getSpawningChance()), Float.toString(biome.minHeight), Float.toString(biome.maxHeight), s_types.toString(), biome.getClass().getCanonicalName() };
            }

            @Override
            public BiomeGenBase[] array() {
                return BiomeGenBase.getBiomeGenArray();
            }
        });
        API.addOption(new ItemPanelDumper("tools.dump.itempanel"));
        //TODO Fluid registry Dumper.
    }

    private static void parseModItems() {
        HashMap<String, ItemStackSet> modSubsets = new HashMap<String, ItemStackSet>();
        for (Item item : (Iterable<Item>) Item.itemRegistry) {
            UniqueIdentifier ident = GameRegistry.findUniqueIdentifierFor(item);
            if (ident == null) {
                NEIClientConfig.logger.error("Failed to find identifier for: " + item);
                continue;
            }
            String modId = GameRegistry.findUniqueIdentifierFor(item).modId;
            itemOwners.put(item, modId);
            ItemStackSet itemset = modSubsets.get(modId);
            if (itemset == null) {
                modSubsets.put(modId, itemset = new ItemStackSet());
            }
            itemset.with(item);
        }

        API.addSubset("Mod.Minecraft", modSubsets.remove("minecraft"));
        for (Entry<String, ItemStackSet> entry : modSubsets.entrySet()) {
            ModContainer mc = FMLCommonHandler.instance().findContainerFor(entry.getKey());
            if (mc == null) {
                NEIClientConfig.logger.error("Missing container for " + entry.getKey());
            } else {
                API.addSubset("Mod." + mc.getName(), entry.getValue());
            }
        }
    }

    private static void addInputHandlers() {
        GuiContainerManager.addInputHandler(new RecipeItemInputHandler());
        GuiContainerManager.addInputHandler(new PopupInputHandler());
    }

    private static void addInfiniteHandlers() {
        API.addInfiniteItemHandler(new InfiniteStackSizeHandler());
        API.addInfiniteItemHandler(new InfiniteToolHandler());
    }

    private static void addDefaultDropDowns() {
        API.addSubset("Items", new ItemFilter() {
            @Override
            public boolean matches(ItemStack item) {
                return Block.getBlockFromItem(item.getItem()) == Blocks.air;
            }
        });
        API.addSubset("Blocks", new ItemFilter() {
            @Override
            public boolean matches(ItemStack item) {
                return Block.getBlockFromItem(item.getItem()) != Blocks.air;
            }
        });
        API.addSubset("Blocks.MobSpawners", ItemStackSet.of(Blocks.mob_spawner));
    }

    private static void searchItems() {
        ItemStackSet tools = new ItemStackSet();
        ItemStackSet picks = new ItemStackSet();
        ItemStackSet shovels = new ItemStackSet();
        ItemStackSet axes = new ItemStackSet();
        ItemStackSet hoes = new ItemStackSet();
        ItemStackSet swords = new ItemStackSet();
        ItemStackSet chest = new ItemStackSet();
        ItemStackSet helmets = new ItemStackSet();
        ItemStackSet legs = new ItemStackSet();
        ItemStackSet boots = new ItemStackSet();
        ItemStackSet other = new ItemStackSet();
        ItemStackSet ranged = new ItemStackSet();
        ItemStackSet food = new ItemStackSet();
        ItemStackSet potioningredients = new ItemStackSet();

        ArrayList<ItemStackSet> creativeTabRanges = new ArrayList<ItemStackSet>(CreativeTabs.creativeTabArray.length);
        List<ItemStack> stackList = new LinkedList<ItemStack>();

        for (Item item : (Iterable<Item>) Item.itemRegistry) {
            if (item == null) {
                continue;
            }

            for (CreativeTabs itemTab : item.getCreativeTabs()) {
                if (itemTab != null) {
                    while (itemTab.getTabIndex() >= creativeTabRanges.size()) {
                        creativeTabRanges.add(null);
                    }
                    ItemStackSet set = creativeTabRanges.get(itemTab.getTabIndex());
                    if (set == null) {
                        creativeTabRanges.set(itemTab.getTabIndex(), set = new ItemStackSet());
                    }
                    stackList.clear();
                    item.getSubItems(item, itemTab, stackList);
                    for (ItemStack stack : stackList) {
                        set.add(stack);
                    }
                }
            }

            if (item.isDamageable()) {
                tools.with(item);
                if (item instanceof ItemPickaxe) {
                    picks.with(item);
                } else if (item instanceof ItemSpade) {
                    shovels.with(item);
                } else if (item instanceof ItemAxe) {
                    axes.with(item);
                } else if (item instanceof ItemHoe) {
                    hoes.with(item);
                } else if (item instanceof ItemSword) {
                    swords.with(item);
                } else if (item instanceof ItemArmor) {
                    switch (((ItemArmor) item).armorType) {
                    case 0:
                        helmets.with(item);
                        break;
                    case 1:
                        chest.with(item);
                        break;
                    case 2:
                        legs.with(item);
                        break;
                    case 3:
                        boots.with(item);
                        break;
                    }
                } else if (item == Items.arrow || item == Items.bow) {
                    ranged.with(item);
                } else if (item == Items.fishing_rod || item == Items.flint_and_steel || item == Items.shears) {
                    other.with(item);
                }
            }

            if (item instanceof ItemFood) {
                food.with(item);
            }

            try {
                LinkedList<ItemStack> subItems = new LinkedList<ItemStack>();
                item.getSubItems(item, null, subItems);
                for (ItemStack stack : subItems) {
                    if (item.isPotionIngredient(stack)) {
                        BrewingRecipeHandler.ingredients.add(stack);
                        potioningredients.add(stack);
                    }
                }

            } catch (Exception e) {
                NEIClientConfig.logger.error("Error loading brewing ingredients for: " + item, e);
            }
        }
        API.addSubset("Items.Tools.Pickaxes", picks);
        API.addSubset("Items.Tools.Shovels", shovels);
        API.addSubset("Items.Tools.Axes", axes);
        API.addSubset("Items.Tools.Hoes", hoes);
        API.addSubset("Items.Tools.Other", other);
        API.addSubset("Items.Weapons.Swords", swords);
        API.addSubset("Items.Weapons.Ranged", ranged);
        API.addSubset("Items.Armor.ChestPlates", chest);
        API.addSubset("Items.Armor.Leggings", legs);
        API.addSubset("Items.Armor.Helmets", helmets);
        API.addSubset("Items.Armor.Boots", boots);
        API.addSubset("Items.Food", food);
        API.addSubset("Items.Potions.Ingredients", potioningredients);

        for (CreativeTabs tab : CreativeTabs.creativeTabArray) {
            if (tab.getTabIndex() >= creativeTabRanges.size()) {
                continue;
            }
            ItemStackSet set = creativeTabRanges.get(tab.getTabIndex());
            if (set != null && !set.isEmpty()) {
                API.addSubset("CreativeTabs." + I18n.format(tab.getTranslatedTabLabel()), set);
            }
        }

        BrewingRecipeHandler.searchPotions();
    }

    private static void addSpawnEggs() {
        addEntityEgg(EntitySnowman.class, 0xEEFFFF, 0xffa221);
        addEntityEgg(EntityIronGolem.class, 0xC5C2C1, 0xffe1cc);
    }

    private static void addEntityEgg(Class<?> entity, int i, int j) {
        int id = (Integer) EntityList.classToIDMapping.get(entity);
        EntityList.entityEggs.put(id, new EntityEggInfo(id, i, j));
    }

    public static ArrayList<ItemStack> getIdentifierItems(World world, EntityPlayer player, MovingObjectPosition hit) {
        BlockPos pos = hit.getBlockPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        ArrayList<IHighlightHandler> handlers = new ArrayList<IHighlightHandler>();
        if (highlightIdentifiers.containsKey(null)) {
            handlers.addAll(highlightIdentifiers.get(null));
        }
        if (highlightIdentifiers.containsKey(block)) {
            handlers.addAll(highlightIdentifiers.get(block));
        }
        for (IHighlightHandler ident : handlers) {
            ItemStack item = ident.identifyHighlight(world, player, hit);
            if (item != null) {
                items.add(item);
            }
        }

        if (items.size() > 0) {
            return items;
        }

        ItemStack pick = block.getPickBlock(hit, world, pos);
        if (pick != null) {
            items.add(pick);
        }

        try {
            items.addAll(block.getDrops(world, pos, state, 0));
        } catch (Exception ignored) {
        }
        if (block instanceof IShearable) {
            IShearable shearable = (IShearable) block;
            if (shearable.isShearable(new ItemStack(Items.shears), world, pos)) {
                items.addAll(shearable.onSheared(new ItemStack(Items.shears), world, pos, 0));
            }
        }

        if (items.size() == 0) {
            items.add(0, new ItemStack(block, 1, block.getMetaFromState(state)));
        }

        return items;
    }

    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layouts) {
        for (ItemInfo.Layout layout : layouts) {
            ItemInfo.highlightHandlers.get(layout).add(handler);
        }
    }

    public static List<String> getText(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop) {
        List<String> retString = new ArrayList<String>();

        for (ItemInfo.Layout layout : ItemInfo.Layout.values()) {
            for (IHighlightHandler handler : ItemInfo.highlightHandlers.get(layout)) {
                retString = handler.handleTextData(itemStack, world, player, mop, retString, layout);
            }
        }

        return retString;
    }

    public static String getSearchName(ItemStack stack) {
        String s = itemSearchNames.get(stack);
        if (s == null) {
            s = EnumChatFormatting.getTextWithoutFormattingCodes(GuiContainerManager.concatenatedDisplayName(stack, true).toLowerCase());
            itemSearchNames.put(stack, s);
        }
        return s;
    }
}
