package codechicken.nei.api;

import java.util.*;
import java.util.Map.Entry;

import codechicken.core.CommonUtils;
import codechicken.core.featurehack.GameDataManipulator;
import codechicken.core.inventory.ItemKey;
import codechicken.lib.lang.LangUtil;
import codechicken.nei.PopupInputHandler;
import codechicken.nei.InfiniteStackSizeHandler;
import codechicken.nei.InfiniteToolHandler;
import codechicken.nei.ItemMobSpawner;
import codechicken.nei.MultiItemRange;
import codechicken.nei.SpawnerRenderer;
import codechicken.nei.config.ArrayDumper;
import codechicken.nei.config.ItemPanelDumper;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.BrewingRecipeHandler;
import codechicken.nei.recipe.RecipeItemInputHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IShearable;

/**
 * This is an internal class for storing information about items, to be accessed by the API
 */
public class ItemInfo
{
    public static enum Layout {HEADER, BODY, FOOTER};
    public static final HashMap<Layout, ArrayList<IHighlightHandler>> highlightHandlers = new HashMap<Layout, ArrayList<IHighlightHandler>>();
    public static final HashMap<ItemKey, String> fallbackNames = new HashMap<ItemKey, String>();
    public static final HashSet<Integer> excludeIds = new HashSet<Integer>();
    public static final HashSet<Integer> nonUnlimitedIds = new HashSet<Integer>();
    public static final HashMap<Integer, ArrayList<int[]>> damageVariants = new HashMap<Integer, ArrayList<int[]>>();
    public static final ArrayList<int[]> defaultDamageRange = new ArrayList<int[]>();
    public static final HashMap<Integer, ArrayList<ItemStack>> itemcompounds = new HashMap<Integer, ArrayList<ItemStack>>();
    public static final LinkedList<IInfiniteItemHandler> infiniteHandlers = new LinkedList<IInfiniteItemHandler>();
    public static final HashMap<Integer, ArrayList<IHighlightHandler>> highlightIdentifiers = new HashMap<Integer, ArrayList<IHighlightHandler>>();
    public static final HashSet<Class<? extends Slot>> fastTransferExemptions = new HashSet<Class<? extends Slot>>();
    
    public static final String[] itemOwners = new String[Item.itemsList.length];

    static
    {
        for(Layout layout : Layout.values())
            ItemInfo.highlightHandlers.put(layout, new ArrayList<IHighlightHandler>());
    }
    
    public static boolean isHidden(int itemID)
    {
        return excludeIds.contains(Integer.valueOf(itemID));
    }
    
    public static ArrayList<int[]> getItemDamageVariants(int itemID)
    {
        ArrayList<int[]> damages = damageVariants.get(itemID);
        return damages == null ? defaultDamageRange : damages;
    }
        
    public static String getOverrideName(int itemID, int itemDamage)
    {
        ItemKey itemhash = new ItemKey(itemID, itemDamage);
        return fallbackNames.get(itemhash);
    }
       
    public static boolean canItemBeUnlimited(int itemID)
    {
        return !nonUnlimitedIds.contains(itemID);
    }    
    
    public static ArrayList<ItemStack> getItemCompounds(int itemID)
    {
        return itemcompounds.get(itemID);
    }
    
    public static void load(World world)
    {
        defaultDamageRange.add(new int[]{0,15});
        addVanillaBlockProperties();
        addDefaultDropDowns();
        searchItems();
        parseModItems();
        addMobSpawnerItem(world);
        addSpawnEggs();
        new BrewingRecipeHandler().searchPotions();
        addInfiniteHandlers();
        addInputHandlers();
        addIDDumps();
    }

    private static void addIDDumps()
    {
        API.addOption(new ArrayDumper<Item>("tools.dump.item")
        {
            @Override
            public String[] header()
            {
                return new String[]{"ID", "Block/Item", "Mod", "Unlocalised name", "Class"};
            }
            
            @Override
            public String[] dump(int i, Item item)
            {
                Block block = i < Block.blocksList.length ? Block.blocksList[i] : null;
                return new String[]{
                        Integer.toString(i),
                        block != null ? "Block" : "Item",
                        ItemInfo.itemOwners[i],
                        block != null ? block.getUnlocalizedName() : item.getUnlocalizedName(),
                        (block != null ? block : item).getClass().getCanonicalName()
                    };
            }
            
            @Override
            public Item[] array()
            {
                return Item.itemsList;
            }
        });
        API.addOption(new ArrayDumper<Potion>("tools.dump.potion")
        {
            public String[] header()
            {
                return new String[]{"ID", "Unlocalised name", "Class"};
            }
            
            @Override
            public String[] dump(int i, Potion potion)
            {
                return new String[]{
                        Integer.toString(i),
                        potion.getName(),
                        potion.getClass().getCanonicalName()
                    };
            }
            
            @Override
            public Potion[] array()
            {
                return Potion.potionTypes;
            }
        });
        API.addOption(new ArrayDumper<Enchantment>("tools.dump.enchantment")
        {
            public String[] header()
            {
                return new String[]{"ID", "Unlocalised name", "Type", "Min Level", "Max Level", "Class"};
            }
            
            @Override
            public String[] dump(int i, Enchantment ench)
            {
                return new String[]{
                        Integer.toString(i),
                        ench.getName(),
                        ench.type.toString(),
                        Integer.toString(ench.getMinLevel()),
                        Integer.toString(ench.getMaxLevel()),
                        ench.getClass().getCanonicalName()
                    };
            }
            
            @Override
            public Enchantment[] array()
            {
                return Enchantment.enchantmentsList;
            }
        });
        API.addOption(new ArrayDumper<BiomeGenBase>("tools.dump.biome")
        {
            @Override
            public String[] header()
            {
                return new String[]{"ID", "Name", "Temperature", "Rainfall", "Spawn Chance", "Min Height", "Max Height", "Types", "Class"};
            }
            
            @Override
            public String[] dump(int i, BiomeGenBase biome)
            {
                BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
                StringBuilder s_types = new StringBuilder();
                for(BiomeDictionary.Type t : types)
                {
                    if(s_types.length() > 0)
                        s_types.append(", ");
                    s_types.append(t.name());
                }
                
                return new String[]{
                        Integer.toString(i),
                        biome.biomeName,
                        Float.toString(biome.getFloatTemperature()),
                        Float.toString(biome.getFloatRainfall()),
                        Float.toString(biome.getSpawningChance()),
                        Float.toString(biome.minHeight),
                        Float.toString(biome.maxHeight),
                        s_types.toString(),
                        biome.getClass().getCanonicalName()
                    };
            }
            
            @Override
            public BiomeGenBase[] array()
            {
                return BiomeGenBase.biomeList;
            }
        });
        API.addOption(new ItemPanelDumper("tools.dump.itempanel"));
    }

    private static void parseModItems()
    {
        NBTTagList itemDataList = new NBTTagList();
        GameData.writeItemData(itemDataList);
        
        HashMap<String, MultiItemRange> modRangeMap = new HashMap<String, MultiItemRange>();
        
        for(int i = 0; i < itemDataList.tagCount(); i++)
        {
            ItemData itemData = new ItemData((NBTTagCompound) itemDataList.tagAt(i));
            MultiItemRange itemRange = modRangeMap.get(itemData.getModId());
            if(itemRange == null)
                modRangeMap.put(itemData.getModId(), itemRange = new MultiItemRange());
            itemOwners[itemData.getItemId()] = itemData.getModId();
            itemRange.add(itemData.getItemId());
        }
        
        for(Entry<String, MultiItemRange> entry : modRangeMap.entrySet())
        {
            String modID = entry.getKey();
            ModContainer mod = CommonUtils.findModContainer(modID);
            String modname = mod == null ? modID : mod.getName();
            API.addSetRange("Mod."+modname, entry.getValue());
        }
    }

    private static void addInputHandlers()
    {
        GuiContainerManager.addInputHandler(new RecipeItemInputHandler());
        GuiContainerManager.addInputHandler(new PopupInputHandler());
    }

    private static void addMobSpawnerItem(final World world)
    {
        Item.itemsList[Block.mobSpawner.blockID] = null; 
        GameDataManipulator.createHiddenItem(new Runnable()
        {
            @SuppressWarnings("unused")
            @Override
            public void run()
            {
                new ItemMobSpawner(world);
            }
        });
        MinecraftForgeClient.registerItemRenderer(Block.mobSpawner.blockID, new SpawnerRenderer());
    }

    private static void addInfiniteHandlers()
    {
        API.addInfiniteItemHandler(new InfiniteStackSizeHandler());
        API.addInfiniteItemHandler(new InfiniteToolHandler());
    }

    private static void addVanillaBlockProperties()
    {
        API.setOverrideName(Block.waterMoving.blockID, 0, "Water Source");
        API.setMaxDamageException(Block.waterMoving.blockID, 0);
        API.setOverrideName(Block.waterStill.blockID, 0, "Water Still");
        API.setMaxDamageException(Block.waterStill.blockID, 0);
        API.setOverrideName(Block.lavaMoving.blockID, 0, "Lava Source");
        API.setMaxDamageException(Block.lavaMoving.blockID, 0);
        API.setOverrideName(Block.lavaStill.blockID, 0, "Lava Still");
        API.setMaxDamageException(Block.lavaStill.blockID, 0);
        API.setOverrideName(Block.silverfish.blockID, 0, "Silverfish Stone");
        API.setOverrideName(Block.endPortal.blockID, 0, "End Portal");
        API.setOverrideName(Block.endPortalFrame.blockID, 0, "End Portal Frame");
        API.hideItem(Block.pistonExtension.blockID);
        API.hideItem(Block.pistonMoving.blockID);
        API.hideItem(Block.melonStem.blockID);
        API.hideItem(Block.pumpkinStem.blockID);
        API.hideItem(Block.bed.blockID);
        API.hideItem(Block.redstoneWire.blockID);
        API.hideItem(Block.crops.blockID);
        API.hideItem(Block.signPost.blockID);
        API.hideItem(Block.doorWood.blockID);
        API.hideItem(Block.signWall.blockID);
        API.hideItem(Block.doorIron.blockID);
        API.hideItem(Block.oreRedstoneGlowing.blockID);
        API.hideItem(Block.torchRedstoneIdle.blockID);
        API.hideItem(Block.reed.blockID);
        API.hideItem(Block.redstoneRepeaterIdle.blockID);
        API.hideItem(Block.redstoneRepeaterActive.blockID);
        API.hideItem(Block.cauldron.blockID);
        API.hideItem(Block.netherStalk.blockID);
        API.hideItem(Block.brewingStand.blockID);
        API.hideItem(Block.furnaceBurning.blockID);
        API.hideItem(Block.redstoneLampActive.blockID);
        API.hideItem(Block.flowerPot.blockID);
        API.hideItem(Block.carrot.blockID);
        API.hideItem(Block.potato.blockID);
        API.hideItem(Block.skull.blockID);
        API.hideItem(Block.tripWire.blockID);
    }

    private static void addDefaultDropDowns()
    {
        API.addSetRange("Blocks", new MultiItemRange("[0-32000]")
        {
            @Override
            public void addItemIfInRange(int item, int damage, NBTTagCompound compound)
            {
                if(item < Block.blocksList.length && (Block.blocksList[item] != null && Block.blocksList[item].blockMaterial != Material.air))
                    super.addItemIfInRange(item, damage, compound);
            }
        });
        API.addSetRange("Items", new MultiItemRange("[0-32000]")
        {
            @Override
            public void addItemIfInRange(int item, int damage, NBTTagCompound compound)
            {
                if(item >= Block.blocksList.length || Block.blocksList[item] == null || Block.blocksList[item].blockMaterial == Material.air)
                    super.addItemIfInRange(item, damage, compound);
            }
        });
        API.addSetRange("Blocks.MobSpawners", new MultiItemRange("[52]"));
    }
    
    private static void searchItems()
    {
        MultiItemRange tools = new MultiItemRange();
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
        for(CreativeTabs tab : CreativeTabs.creativeTabArray)
            creativeTabRanges[tab.getTabIndex()] = new MultiItemRange();
        
        for(Item item : Item.itemsList)
        {
            if(item == null)
                continue;
            
            CreativeTabs itemTab = item.getCreativeTab();
            if(itemTab != null)
                creativeTabRanges[itemTab.getTabIndex()].add(item);
            
            if(item.isDamageable())
            {
                tools.add(item);
                if(item instanceof ItemPickaxe)
                {
                    picks.add(item);
                }
                else if(item instanceof ItemSpade)
                {
                    shovels.add(item);
                }
                else if(item instanceof ItemAxe)
                {
                    axes.add(item);
                }
                else if(item instanceof ItemHoe)
                {
                    hoes.add(item);
                }
                else if(item instanceof ItemSword)
                {
                    swords.add(item);
                }
                else if(item instanceof ItemArmor)
                {
                    switch(((ItemArmor) item).armorType)
                    {
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
                }
                else if(item == Item.arrow || item == Item.bow)
                {
                    ranged.add(item);
                }
                else if(item == Item.fishingRod || item == Item.flintAndSteel || item == Item.shears)
                {
                    other.add(item);
                }
            }
            
            if(item instanceof ItemFood)
            {
                food.add(item);
            }
            
            if(item.isPotionIngredient())
            {
                BrewingRecipeHandler.ingredientIDs.add(item.itemID);
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
        
        for(CreativeTabs tab : CreativeTabs.creativeTabArray)
        {
            if(creativeTabRanges[tab.getTabIndex()].ranges.size() > 0)
                API.addSetRange("CreativeTabs."+LangUtil.translateG(tab.getTranslatedTabLabel()), creativeTabRanges[tab.getTabIndex()]);
        }
    }

    private static void addSpawnEggs()
    {
        ArrayList<Integer> damages = new ArrayList<Integer>();
        try
        {
            HashMap<Class<Entity>, Integer> classToIDMapping = (HashMap<Class<Entity>, Integer>) EntityList.classToIDMapping;
            damages.add(classToIDMapping.get(EntityMooshroom.class));
            damages.add(classToIDMapping.get(EntitySkeleton.class));
            damages.add(classToIDMapping.get(EntityCreeper.class));
            damages.add(classToIDMapping.get(EntitySlime.class));
            damages.add(classToIDMapping.get(EntityZombie.class));
            damages.add(classToIDMapping.get(EntitySpider.class));
            damages.add(classToIDMapping.get(EntityChicken.class));
            damages.add(classToIDMapping.get(EntityCaveSpider.class));
            damages.add(classToIDMapping.get(EntityCow.class));
            damages.add(classToIDMapping.get(EntityEnderman.class));
            damages.add(classToIDMapping.get(EntityWolf.class));
            damages.add(classToIDMapping.get(EntityPigZombie.class));
            damages.add(classToIDMapping.get(EntitySquid.class));
            damages.add(classToIDMapping.get(EntityGhast.class));
            damages.add(classToIDMapping.get(EntityMagmaCube.class));
            damages.add(classToIDMapping.get(EntitySheep.class));
            damages.add(classToIDMapping.get(EntityBlaze.class));
            damages.add(classToIDMapping.get(EntityVillager.class));
            damages.add(classToIDMapping.get(EntitySilverfish.class));
            damages.add(classToIDMapping.get(EntityPig.class));
            damages.add(classToIDMapping.get(EntityOcelot.class));

            addEntityEgg(damages, classToIDMapping.get(EntitySnowman.class), 0xEEFFFF, 0xffa221);
            addEntityEgg(damages, classToIDMapping.get(EntityIronGolem.class), 0xC5C2C1, 0xffe1cc);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        API.setItemDamageVariants(Item.monsterPlacer.itemID, damages);
    }

    private static void addEntityEgg(ArrayList<Integer> damages, Integer ID, int i, int j)
    {
        damages.add(ID);
        EntityList.entityEggs.put(ID, new EntityEggInfo(ID, i, j));
    }
    
    public static ArrayList<ItemStack> getIdentifierItems(World world, EntityPlayer player, MovingObjectPosition hit)
    {
        int x = hit.blockX;
        int y = hit.blockY;
        int z = hit.blockZ;
        Block mouseoverBlock = Block.blocksList[world.getBlockId(x, y, z)];
        
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        
        ArrayList<IHighlightHandler> handlers = new ArrayList<IHighlightHandler>();
        if(highlightIdentifiers.get(0) != null)
            handlers.addAll(highlightIdentifiers.get(0));
        if(highlightIdentifiers.get(mouseoverBlock.blockID) != null)
                handlers.addAll(highlightIdentifiers.get(mouseoverBlock.blockID));
        for(IHighlightHandler ident : handlers)
        {
            ItemStack item = ident.identifyHighlight(world, player, hit);
            if(item != null)
                items.add(item);
        }
        
        if(items.size() > 0)
            return items;

        ItemStack pick = mouseoverBlock.getPickBlock(hit, world, x, y, z);
        if(pick != null)
            items.add(pick);
        
        try
        {
            items.addAll(mouseoverBlock.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0));
        }
        catch(Exception e){}
        if(mouseoverBlock instanceof IShearable)
        {
            IShearable shearable = (IShearable)mouseoverBlock;
            if(shearable.isShearable(new ItemStack(Item.shears), world, x, y, z))
            {
                items.addAll(shearable.onSheared(new ItemStack(Item.shears), world, x, y, z, 0));
            }
        }
        
        if(items.size() == 0)
            items.add(0, new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z)));
        
        return items;
    }
    
    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layouts){
        for (ItemInfo.Layout layout: layouts)
            ItemInfo.highlightHandlers.get(layout).add(handler);
    }
    
    public static List<String> getText(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop){
        List<String> retString = new ArrayList<String>();

        for (ItemInfo.Layout layout : ItemInfo.Layout.values())
            for (IHighlightHandler handler : ItemInfo.highlightHandlers.get(layout))
                retString = handler.handleTextData(itemStack, world, player, mop, retString, layout);
        
        return retString;
    }
}
