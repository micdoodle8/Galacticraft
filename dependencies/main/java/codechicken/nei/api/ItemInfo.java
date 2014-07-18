package codechicken.nei.api;

import codechicken.core.featurehack.GameDataManipulator;
import codechicken.nei.*;
import codechicken.nei.config.ArrayDumper;
import codechicken.nei.config.ItemPanelDumper;
import codechicken.nei.config.RegistryDumper;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.RecipeItemInputHandler;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * This is an internal class for storing information about items, to be accessed by the API
 */
public class ItemInfo
{
    public static enum Layout
    {
        HEADER, BODY, FOOTER
    }

    public static final HashMap<Layout, ArrayList<IHighlightHandler>> highlightHandlers = new HashMap<Layout, ArrayList<IHighlightHandler>>();
    public static final ItemStackMap<String> nameOverrides = new ItemStackMap<String>();
    public static final ItemStackSet hiddenItems = new ItemStackSet();
    public static final ItemStackSet finiteItems = new ItemStackSet();
    public static final ArrayListMultimap<Item, ItemStack> itemOverrides = ArrayListMultimap.create();

    public static final LinkedList<IInfiniteItemHandler> infiniteHandlers = new LinkedList<IInfiniteItemHandler>();
    public static final ArrayListMultimap<Block, IHighlightHandler> highlightIdentifiers = ArrayListMultimap.create();
    public static final HashSet<Class<? extends Slot>> fastTransferExemptions = new HashSet<Class<? extends Slot>>();

    public static final HashMap<Item, String> itemOwners = new HashMap<Item, String>();

    static {
        for (Layout layout : Layout.values())
            ItemInfo.highlightHandlers.put(layout, new ArrayList<IHighlightHandler>());
    }

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

    public static List<ItemStack> getItemOverrides(Item item) {
        return itemOverrides.get(item);
    }

    public static void load(World world) {
        addVanillaBlockProperties();
        addDefaultDropDowns();
        searchItems();
        parseModItems();
        addMobSpawnerItem(world);
        addSpawnEggs();
        addInfiniteHandlers();
        addInputHandlers();
        addIDDumps();
    }

    private static void addIDDumps() {
        API.addOption(new RegistryDumper<Item>("tools.dump.item")
        {
            @Override
            public String[] header() {
                return new String[]{"Name", "ID", "Has Block", "Mod", "Class"};
            }

            @Override
            public String[] dump(Item item, int id, String name) {
                return new String[]{
                        name,
                        Integer.toString(id),
                        Boolean.toString(Block.getBlockFromItem(item) != Blocks.air),
                        ItemInfo.itemOwners.get(item),
                        item.getClass().getCanonicalName()
                };
            }

            @Override
            public RegistryNamespaced registry() {
                return Item.itemRegistry;
            }
        });
        API.addOption(new RegistryDumper<Block>("tools.dump.block")
        {
            @Override
            public String[] header() {
                return new String[]{"Name", "ID", "Has Item", "Mod", "Class"};
            }

            @Override
            public String[] dump(Block item, int id, String name) {
                return new String[]{
                        name,
                        Integer.toString(id),
                        Boolean.toString(Item.getItemFromBlock(item) != null),
                        ItemInfo.itemOwners.get(item),
                        item.getClass().getCanonicalName()
                };
            }

            @Override
            public RegistryNamespaced registry() {
                return Block.blockRegistry;
            }
        });
        API.addOption(new ArrayDumper<Potion>("tools.dump.potion")
        {
            public String[] header() {
                return new String[]{"ID", "Unlocalised name", "Class"};
            }

            @Override
            public String[] dump(Potion potion, int id) {
                return new String[]{
                        Integer.toString(id),
                        potion.getName(),
                        potion.getClass().getCanonicalName()
                };
            }

            @Override
            public Potion[] array() {
                return Potion.potionTypes;
            }
        });
        API.addOption(new ArrayDumper<Enchantment>("tools.dump.enchantment")
        {
            public String[] header() {
                return new String[]{"ID", "Unlocalised name", "Type", "Min Level", "Max Level", "Class"};
            }

            @Override
            public String[] dump(Enchantment ench, int id) {
                return new String[]{
                        Integer.toString(id),
                        ench.getName(),
                        ench.type.toString(),
                        Integer.toString(ench.getMinLevel()),
                        Integer.toString(ench.getMaxLevel()),
                        ench.getClass().getCanonicalName()
                };
            }

            @Override
            public Enchantment[] array() {
                return Enchantment.enchantmentsList;
            }
        });
        API.addOption(new ArrayDumper<BiomeGenBase>("tools.dump.biome")
        {
            @Override
            public String[] header() {
                return new String[]{"ID", "Name", "Temperature", "Rainfall", "Spawn Chance", "Root Height", "Height Variation", "Types", "Class"};
            }

            @Override
            public String[] dump(BiomeGenBase biome, int id) {
                BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
                StringBuilder s_types = new StringBuilder();
                for (BiomeDictionary.Type t : types) {
                    if (s_types.length() > 0)
                        s_types.append(", ");
                    s_types.append(t.name());
                }

                return new String[]{
                        Integer.toString(id),
                        biome.biomeName,
                        Float.toString(biome.getFloatTemperature(0, 0, 0)),
                        Float.toString(biome.getFloatRainfall()),
                        Float.toString(biome.getSpawningChance()),
                        Float.toString(biome.rootHeight),
                        Float.toString(biome.heightVariation),
                        s_types.toString(),
                        biome.getClass().getCanonicalName()
                };
            }

            @Override
            public BiomeGenBase[] array() {
                return BiomeGenBase.getBiomeGenArray();
            }
        });
        API.addOption(new ItemPanelDumper("tools.dump.itempanel"));
    }

    private static void parseModItems() {
        //TODO GameRegistry.findUniqueIdentifierFor()
    }

    private static void addInputHandlers() {
        GuiContainerManager.addInputHandler(new RecipeItemInputHandler());
        GuiContainerManager.addInputHandler(new PopupInputHandler());
    }

    private static void addMobSpawnerItem(World world) {
        GameDataManipulator.replaceItem(Block.getIdFromBlock(Blocks.mob_spawner), new ItemMobSpawner(world));
    }

    private static void addInfiniteHandlers() {
        API.addInfiniteItemHandler(new InfiniteStackSizeHandler());
        API.addInfiniteItemHandler(new InfiniteToolHandler());
    }

    private static void addVanillaBlockProperties() {
        API.setOverrideName(new ItemStack(Blocks.flowing_water), "Water Source");
        API.setOverrideName(new ItemStack(Blocks.water), "Water Still");
        API.setOverrideName(new ItemStack(Blocks.flowing_lava), "Lava Source");
        API.setOverrideName(new ItemStack(Blocks.lava), "Lava Still");
        API.setOverrideName(new ItemStack(Blocks.end_portal), "End Portal");
        API.setOverrideName(new ItemStack(Blocks.end_portal_frame), "End Portal Frame");
        API.hideItem(new ItemStack(Blocks.double_stone_slab, 1, OreDictionary.WILDCARD_VALUE));
        API.hideItem(new ItemStack(Blocks.double_wooden_slab, 1, OreDictionary.WILDCARD_VALUE));
        API.hideItem(new ItemStack(Blocks.carrots));
        API.hideItem(new ItemStack(Blocks.potatoes));
        API.hideItem(new ItemStack(Blocks.cocoa));
    }

    private static void addDefaultDropDowns() {
        /*API.addSetRange("Blocks", new MultiItemRange("[0-32000]")
        {
            @Override
            public void addItemIfInRange(int item, int damage, NBTTagCompound compound) {
                if (item < Block.blocksList.length && (Block.blocksList[item] != null && Block.blocksList[item].blockMaterial != Material.air))
                    super.addItemIfInRange(item, damage, compound);
            }
        });
        API.addSetRange("Items", new MultiItemRange("[0-32000]")
        {
            @Override
            public void addItemIfInRange(int item, int damage, NBTTagCompound compound) {
                if (item >= Block.blocksList.length || Block.blocksList[item] == null || Block.blocksList[item].blockMaterial == Material.air)
                    super.addItemIfInRange(item, damage, compound);
            }
        });
        API.addSetRange("Blocks.MobSpawners", new MultiItemRange("[52]"));*/
    }

    //TODO
    private static void searchItems() {
        /*MultiItemRange tools = new MultiItemRange();
        MultiItemRange picks = new MultiItemRange();
        MultiItemRange shovels = new MultiItemRange();
        MultiItemRange axes = new MultiItemRange();
        MultiItemRange hoes = new MultiItemRange();
        MultiItemRange swords = new MultiItemRange();
        MultiItemRange chest = new MultiItemRange();
        MultiItemRange helmets = new MultiItemRange();
        MultiItemRange legs = new MultiItemRange();
        MultiItemRange boots = new MultiItemRange();
        MultiItemRange other = new MultiItemRange();
        MultiItemRange ranged = new MultiItemRange();
        MultiItemRange food = new MultiItemRange();
        MultiItemRange potioningredients = new MultiItemRange();

        MultiItemRange[] creativeTabRanges = new MultiItemRange[CreativeTabs.creativeTabArray.length];
        for (CreativeTabs tab : CreativeTabs.creativeTabArray)
            creativeTabRanges[tab.getTabIndex()] = new MultiItemRange();

        for (Item item : (Iterable<Item>) Item.itemRegistry) {
            if (item == null)
                continue;

            CreativeTabs itemTab = item.getCreativeTab();
            if (itemTab != null)
                creativeTabRanges[itemTab.getTabIndex()].add(item);

            if (item.isDamageable()) {
                tools.add(item);
                if (item instanceof ItemPickaxe) {
                    picks.add(item);
                } else if (item instanceof ItemSpade) {
                    shovels.add(item);
                } else if (item instanceof ItemAxe) {
                    axes.add(item);
                } else if (item instanceof ItemHoe) {
                    hoes.add(item);
                } else if (item instanceof ItemSword) {
                    swords.add(item);
                } else if (item instanceof ItemArmor) {
                    switch (((ItemArmor) item).armorType) {
                        case 0:
                            helmets.add(item);
                            break;
                        case 1:
                            chest.add(item);
                            break;
                        case 2:
                            legs.add(item);
                            break;
                        case 3:
                            boots.add(item);
                            break;
                    }
                } else if (item == Items.arrow || item == Items.bow) {
                    ranged.add(item);
                } else if (item == Items.fishing_rod || item == Items.flint_and_steel || item == Items.shears) {
                    other.add(item);
                }
            }

            if (item instanceof ItemFood) {
                food.add(item);
            }

            if (item.isPotionIngredient()) {
                BrewingRecipeHandler.ingredients.add(item);
                potioningredients.add(item);
            }
        }
        API.addSetRange("Items.Tools.Pickaxes", picks);
        API.addSetRange("Items.Tools.Shovels", shovels);
        API.addSetRange("Items.Tools.Axes", axes);
        API.addSetRange("Items.Tools.Hoes", hoes);
        API.addSetRange("Items.Tools.Other", other);
        API.addSetRange("Items.Weapons.Swords", swords);
        API.addSetRange("Items.Weapons.Ranged", ranged);
        API.addSetRange("Items.Armor.ChestPlates", chest);
        API.addSetRange("Items.Armor.Leggings", legs);
        API.addSetRange("Items.Armor.Helmets", helmets);
        API.addSetRange("Items.Armor.Boots", boots);
        API.addSetRange("Items.Food", food);
        API.addSetRange("Items.Potions.Ingredients", potioningredients);

        for (CreativeTabs tab : CreativeTabs.creativeTabArray) {
            if (creativeTabRanges[tab.getTabIndex()].ranges.size() > 0)
                API.addSetRange("CreativeTabs." + LangUtil.translateG(tab.getTranslatedTabLabel()), creativeTabRanges[tab.getTabIndex()]);
        }*/
    }

    private static void addSpawnEggs()
    {
        addEntityEgg(EntitySnowman.class, 0xEEFFFF, 0xffa221);
        addEntityEgg(EntityIronGolem.class, 0xC5C2C1, 0xffe1cc);
    }

    private static void addEntityEgg(Class<?> entity, int i, int j) {
        int id = (Integer)EntityList.classToIDMapping.get(entity);
        EntityList.entityEggs.put(id, new EntityEggInfo(id, i, j));
    }

    public static ArrayList<ItemStack> getIdentifierItems(World world, EntityPlayer player, MovingObjectPosition hit) {
        int x = hit.blockX;
        int y = hit.blockY;
        int z = hit.blockZ;
        Block mouseoverBlock = world.getBlock(x, y, z);

        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        ArrayList<IHighlightHandler> handlers = new ArrayList<IHighlightHandler>();
        if (highlightIdentifiers.containsKey(null))
            handlers.addAll(highlightIdentifiers.get(null));
        if (highlightIdentifiers.containsKey(mouseoverBlock))
            handlers.addAll(highlightIdentifiers.get(mouseoverBlock));
        for (IHighlightHandler ident : handlers) {
            ItemStack item = ident.identifyHighlight(world, player, hit);
            if (item != null)
                items.add(item);
        }

        if (items.size() > 0)
            return items;

        ItemStack pick = mouseoverBlock.getPickBlock(hit, world, x, y, z);
        if (pick != null)
            items.add(pick);

        try {
            items.addAll(mouseoverBlock.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0));
        } catch (Exception ignored) {}
        if (mouseoverBlock instanceof IShearable) {
            IShearable shearable = (IShearable) mouseoverBlock;
            if (shearable.isShearable(new ItemStack(Items.shears), world, x, y, z))
                items.addAll(shearable.onSheared(new ItemStack(Items.shears), world, x, y, z, 0));
        }

        if (items.size() == 0)
            items.add(0, new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z)));

        return items;
    }

    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layouts) {
        for (ItemInfo.Layout layout : layouts)
            ItemInfo.highlightHandlers.get(layout).add(handler);
    }

    public static List<String> getText(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop) {
        List<String> retString = new ArrayList<String>();

        for (ItemInfo.Layout layout : ItemInfo.Layout.values())
            for (IHighlightHandler handler : ItemInfo.highlightHandlers.get(layout))
                retString = handler.handleTextData(itemStack, world, player, mop, retString, layout);

        return retString;
    }
}
