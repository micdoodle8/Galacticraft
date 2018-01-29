package codechicken.nei.api;

import codechicken.lib.item.filtering.IItemFilter;
import codechicken.lib.item.filtering.IItemFilterProvider;
import codechicken.nei.*;
import codechicken.nei.ItemList.ItemsLoadedCallback;
import codechicken.nei.config.ItemPanelDumper;
import codechicken.nei.config.dumps.FluidRegistryDumper;
import codechicken.nei.config.dumps.ForgeRegistryDumper;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.BrewingRecipeHandler;
import codechicken.nei.recipe.RecipeItemInputHandler;
import codechicken.nei.recipe.potion.PotionRecipeHelper;
import codechicken.nei.thirdparty.colossalchests.ColossalChestsConfig;
import codechicken.nei.util.LogHelper;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.*;
import java.util.Map.Entry;

/**
 * This is an internal class for storing information about items, to be accessed by the API
 */
public class ItemInfo {
    public enum Layout {
        HEADER,
        BODY,
        FOOTER
    }

    @Deprecated
    public static final ArrayListMultimap<Layout, IHighlightHandler> highlightHandlers = ArrayListMultimap.create();
    public static final ItemStackMap<String> nameOverrides = new ItemStackMap<String>();
    public static final ItemStackSet hiddenItems = new ItemStackSet();
    public static final ItemStackSet finiteItems = new ItemStackSet();
    public static final ArrayListMultimap<Item, ItemStack> itemOverrides = ArrayListMultimap.create();
    public static final ArrayListMultimap<Item, ItemStack> itemVariants = ArrayListMultimap.create();

    public static final LinkedList<IInfiniteItemHandler> infiniteHandlers = new LinkedList<IInfiniteItemHandler>();
    @Deprecated
    public static final ArrayListMultimap<Block, IHighlightHandler> highlightIdentifiers = ArrayListMultimap.create();
    public static final HashSet<Class<? extends Slot>> fastTransferExemptions = new HashSet<Class<? extends Slot>>();
    public static final HashSet<Class<? extends GuiContainer>> fastTransferContainerExemptions = new HashSet<Class<? extends GuiContainer>>();

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
        ColossalChestsConfig.init();
        PotionRecipeHelper.init();
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
        API.hideItem(new ItemStack(Blocks.FARMLAND));
        API.hideItem(new ItemStack(Blocks.LIT_FURNACE));
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
        API.addItemFilter(new IItemFilterProvider() {
            @Override
            public IItemFilter getFilter() {
                return new IItemFilter() {
                    @Override
                    public boolean matches(ItemStack item) {
                        return !hiddenItems.contains(item);
                    }
                };
            }
        });
    }

    private static void addIDDumps() {
        API.addOption(new ForgeRegistryDumper<Item>("tools.dump.item") {
            @Override
            public IForgeRegistry<Item> registry() {
                return ForgeRegistries.ITEMS;
            }

            @Override
            public String[] dump(Item obj, ResourceLocation registryName) {
                int id = Item.getIdFromItem(obj);
                boolean hasBlock = Block.getBlockFromItem(obj) != null && Block.getBlockFromItem(obj) != Blocks.AIR;
                return new String[] { registryName.toString(), Integer.toString(id), Boolean.toString(hasBlock), obj.getClass().getCanonicalName() };
            }

            @Override
            public String[] header() {
                return new String[] { "Registry Name", "ID", "Has Block", "Class" };
            }
        });
        API.addOption(new ForgeRegistryDumper<Block>("tools.dump.block") {
            @Override
            public IForgeRegistry<Block> registry() {
                return ForgeRegistries.BLOCKS;
            }

            @Override
            public String[] dump(Block obj, ResourceLocation registryName) {
                int id = Block.getIdFromBlock(obj);
                boolean hasBlock = Item.getItemFromBlock(obj) != null; //&& Block.getBlockFromItem(obj) != Blocks.AIR;
                return new String[] { registryName.toString(), Integer.toString(id), Boolean.toString(hasBlock), obj.getClass().getCanonicalName() };
            }

            @Override
            public String[] header() {
                return new String[] { "Registry Name", "ID", "Has Item", "Class" };
            }
        });
        API.addOption(new FluidRegistryDumper());
        API.addOption(new ForgeRegistryDumper<Potion>("tools.dump.potion") {
            @Override
            public IForgeRegistry<Potion> registry() {
                return ForgeRegistries.POTIONS;
            }

            @Override
            public String[] dump(Potion obj, ResourceLocation registryName) {
                int id = Potion.getIdFromPotion(obj);
                return new String[] { registryName.toString(), Integer.toString(id), Boolean.toString(obj.isBadEffect()), Boolean.toString(obj.isBeneficial()), obj.getClass().getCanonicalName() };
            }

            @Override
            public String[] header() {
                return new String[] { "Registry Name", "ID", "Is bad Effect", "Is beneficial", "Class" };
            }
        });
        API.addOption(new ForgeRegistryDumper<Biome>("tools.dump.biome") {
            @Override
            public IForgeRegistry<Biome> registry() {
                return ForgeRegistries.BIOMES;
            }

            @Override
            public String[] dump(Biome obj, ResourceLocation registryName) {
                int id = Biome.getIdForBiome(obj);

                BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(obj);
                StringBuilder s_types = new StringBuilder();
                for (BiomeDictionary.Type t : types) {
                    if (s_types.length() > 0) {
                        s_types.append(", ");
                    }
                    s_types.append(t.name());
                }
                return new String[] { registryName.toString(), Integer.toString(id), obj.getBiomeName(), Float.toString(obj.getFloatTemperature(BlockPos.ORIGIN)), Float.toString(obj.getRainfall()), Float.toString(obj.getSpawningChance()), Float.toString(obj.getBaseHeight()), Float.toString(obj.getHeightVariation()), s_types.toString(), obj.getClass().getCanonicalName() };
            }

            @Override
            public String[] header() {
                return new String[] { "Registry Name", "ID", "Name", "Temperature", "Rainfall", "Spawn Chance", "Root Height", "Height Variation", "Types", "Class" };
            }
        });
        API.addOption(new ForgeRegistryDumper<SoundEvent>("tools.dump.sound_event") {
            @Override
            public IForgeRegistry<SoundEvent> registry() {
                return ForgeRegistries.SOUND_EVENTS;
            }

            @Override
            public String[] dump(SoundEvent obj, ResourceLocation registryName) {
                int id = SoundEvent.REGISTRY.getIDForObject(obj);
                return new String[] { registryName.toString(), Integer.toString(id), obj.getClass().getCanonicalName() };
            }

            @Override
            public String[] header() {
                return new String[] { "Registry Name", "ID", "Class" };
            }
        });
        API.addOption(new ForgeRegistryDumper<Enchantment>("tools.dump.enchantment") {
            @Override
            public IForgeRegistry<Enchantment> registry() {
                return null;
            }

            @Override
            public String[] dump(Enchantment obj, ResourceLocation registryName) {
                int id = Enchantment.getEnchantmentID(obj);
                StringBuilder s_slots = new StringBuilder();
                for (EntityEquipmentSlot slot : obj.applicableEquipmentTypes) {
                    if (s_slots.length() > 0) {
                        s_slots.append(", ");
                    }
                    s_slots.append(slot.getName());
                }
                return new String[] { registryName.toString(), Integer.toString(id), obj.getName(), obj.type.toString(), Integer.toString(obj.getMinLevel()), Integer.toString(obj.getMinLevel()), obj.getRarity().name(), s_slots.toString(), obj.getClass().getCanonicalName() };
            }

            @Override
            public String[] header() {
                return new String[] { "Registry Name", "ID", "Name", "Type", "Min Level", "Max Level", "Rarity", "Slots", "Class" };
            }
        });
        API.addOption(new ItemPanelDumper("tools.dump.itempanel"));
    }

    private static void parseModItems() {
        HashMap<String, ItemStackSet> modSubsets = new HashMap<String, ItemStackSet>();
        for (Item item : Item.REGISTRY) {
            ResourceLocation ident = Item.REGISTRY.getNameForObject(item);
            if (ident == null) {
                LogHelper.error("Failed to find identifier for: " + item);
                continue;
            }
            String modId = ident.getResourceDomain();
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
                LogHelper.error("Missing container for " + entry.getKey());
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
        API.addSubset("Items", new IItemFilter() {
            @Override
            public boolean matches(ItemStack item) {
                return Block.getBlockFromItem(item.getItem()) == Blocks.AIR;
            }
        });
        API.addSubset("Blocks", new IItemFilter() {
            @Override
            public boolean matches(ItemStack item) {
                return Block.getBlockFromItem(item.getItem()) != Blocks.AIR;
            }
        });
        API.addSubset("Blocks.MobSpawners", ItemStackSet.of(Blocks.MOB_SPAWNER));
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

        ArrayList<ItemStackSet> creativeTabRanges = new ArrayList<ItemStackSet>(CreativeTabs.CREATIVE_TAB_ARRAY.length);
        List<ItemStack> stackList = new LinkedList<ItemStack>();

        for (Item item : Item.REGISTRY) {
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

            //TODO EntityEquipmentSlot
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
                        case HEAD:
                            helmets.with(item);
                            break;
                        case CHEST:
                            chest.with(item);
                            break;
                        case LEGS:
                            legs.with(item);
                            break;
                        case FEET:
                            boots.with(item);
                            break;
                    }
                } else if (item == Items.ARROW || item == Items.BOW) {
                    ranged.with(item);
                } else if (item == Items.FISHING_ROD || item == Items.FLINT_AND_STEEL || item == Items.SHEARS) {
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
                    if (PotionHelper.isReagent(stack)) {
                        BrewingRecipeHandler.ingredients.add(stack);
                        potioningredients.add(stack);
                    }
                }

            } catch (Exception e) {
                LogHelper.errorError("Error loading brewing ingredients for: " + item, e);
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

        for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
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

    //TODO
    private static void addSpawnEggs() {
        //addEntityEgg(EntitySnowman.class, 0xEEFFFF, 0xffa221);
        //addEntityEgg(EntityIronGolem.class, 0xC5C2C1, 0xffe1cc);
    }

    private static void addEntityEgg(Class<? extends Entity> entity, int i, int j) {
        String id = EntityList.getEntityStringFromClass(entity);
        EntityList.ENTITY_EGGS.put(id, new EntityEggInfo(id, i, j));
    }

    @Deprecated
    public static ArrayList<ItemStack> getIdentifierItems(World world, EntityPlayer player, RayTraceResult hit) {
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

        ItemStack pick = block.getPickBlock(state, hit, world, pos, player);
        if (pick != null) {
            items.add(pick);
        }

        try {
            items.addAll(block.getDrops(world, pos, state, 0));
        } catch (Exception ignored) {
        }
        if (block instanceof IShearable) {
            IShearable shearable = (IShearable) block;
            if (shearable.isShearable(new ItemStack(Items.SHEARS), world, pos)) {
                items.addAll(shearable.onSheared(new ItemStack(Items.SHEARS), world, pos, 0));
            }
        }

        if (items.size() == 0) {
            items.add(0, new ItemStack(block, 1, block.getMetaFromState(state)));
        }

        return items;
    }

    @Deprecated
    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layouts) {
        for (ItemInfo.Layout layout : layouts) {
            ItemInfo.highlightHandlers.get(layout).add(handler);
        }
    }

    @Deprecated
    public static List<String> getText(ItemStack itemStack, World world, EntityPlayer player, RayTraceResult rayTraceResult) {
        List<String> retString = new ArrayList<String>();

        for (ItemInfo.Layout layout : ItemInfo.Layout.values()) {
            for (IHighlightHandler handler : ItemInfo.highlightHandlers.get(layout)) {
                retString = handler.handleTextData(itemStack, world, player, rayTraceResult, retString, layout);
            }
        }

        return retString;
    }

    public static String getSearchName(ItemStack stack) {
        String s = itemSearchNames.get(stack);
        if (s == null) {
            s = TextFormatting.getTextWithoutFormattingCodes(GuiContainerManager.concatenatedDisplayName(stack, true).toLowerCase());
            itemSearchNames.put(stack, s);
        }
        return s;
    }
}
