package codechicken.nei;

import codechicken.core.featurehack.GameDataManipulator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ItemMobSpawner extends ItemBlock {
    private static Map<Integer, EntityLiving> entityHashMap = new HashMap<Integer, EntityLiving>();
    private static Map<Integer, String> IDtoNameMap = new HashMap<Integer, String>();
    public static int idPig;
    private static boolean loaded;
    private static ItemMobSpawner instance;

    public static void register() {
        GameDataManipulator.replaceItem(Block.getIdFromBlock(Blocks.mob_spawner), instance = new ItemMobSpawner());
    }

    public static void initRender() {
        SpawnerRenderer.load(instance);
    }

    public ItemMobSpawner() {
        super(Blocks.mob_spawner);
        setHasSubtypes(true);
    }

    /**
     * Called from BlockMobSpawner on the client via asm generated onBlockPlacedBy
     */
    public static void onBlockPlaced(World world, BlockPos pos, ItemStack stack) {
        if (!NEIClientConfig.hasSMPCounterPart()) {
            return;
        }

        TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner) world.getTileEntity(pos);
        if (tileentitymobspawner != null) {
            setDefaultTag(stack);
            String mobtype = IDtoNameMap.get(stack.getItemDamage());
            if (mobtype != null) {
                NEICPH.sendMobSpawnerID(pos.getX(), pos.getY(), pos.getZ(), mobtype);
                tileentitymobspawner.getSpawnerBaseLogic().setEntityName(mobtype);
            }
        }
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer par2EntityPlayer, List<String> list, boolean par4) {
        setDefaultTag(itemstack);
        int meta = itemstack.getItemDamage();
        if (meta == 0) {
            meta = idPig;
        }
        Entity e = getEntity(meta);
        list.add("\u00A7" + (e instanceof IMob ? "4" : "3") + IDtoNameMap.get(meta));
    }

    public static EntityLiving getEntity(int ID) {
        EntityLiving e = entityHashMap.get(ID);
        if (e == null) {
            World world = Minecraft.getMinecraft().theWorld;
            Class<? extends Entity> clazz = EntityList.idToClassMapping.get(ID);
            try {
                e = (EntityLiving) clazz.getConstructor(World.class).newInstance(world);
            } catch (Throwable t) {
                if (clazz == null) {
                    NEIClientConfig.logger.error("Null class for entity (" + ID + ", " + IDtoNameMap.get(ID));
                } else {
                    NEIClientConfig.logger.error("Error creating instance of entity: " + clazz.getName(), t);
                }
                e = getEntity(idPig);
            }
            entityHashMap.put(ID, e);
        }
        return e;
    }

    private static void setDefaultTag(ItemStack itemstack) {
        if (!IDtoNameMap.containsKey(itemstack.getItemDamage())) {
            itemstack.setItemDamage(idPig);
        }
    }

    public static void loadSpawners(World world) {
        if (loaded) {
            return;
        }
        loaded = true;
        HashMap<Class<? extends Entity>, String> classToStringMapping = (HashMap<Class<? extends Entity>, String>) EntityList.classToStringMapping;
        HashMap<Class<? extends Entity>, Integer> classToIDMapping = (HashMap<Class<? extends Entity>, Integer>) EntityList.classToIDMapping;
        for (Class<? extends Entity> entityClass : classToStringMapping.keySet()) {
            if (!EntityLiving.class.isAssignableFrom(entityClass)) {
                continue;
            }
            try {
                EntityLiving entityliving = (EntityLiving) entityClass.getConstructor(new Class[] { World.class }).newInstance(world);
                entityliving.isChild();

                int id = classToIDMapping.get(entityClass);
                String name = classToStringMapping.get(entityClass);

                if (name.equals("EnderDragon")) {
                    continue;
                }

                IDtoNameMap.put(id, name);

                if (name.equals("Pig")) {
                    idPig = id;
                }
            } catch (Throwable ignored) {
            }
        }

        for (Iterator<Entry<Integer, String>> it = IDtoNameMap.entrySet().iterator(); it.hasNext(); ) {
            Entry<Integer, String> e = it.next();
            if (getEntity(e.getKey()).getClass() == EntityPig.class && !e.getValue().equals("Pig")) {
                it.remove();
            }
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (!NEIClientConfig.hasSMPCounterPart()) {
            list.add(new ItemStack(item));
        } else {
            for (int i : IDtoNameMap.keySet()) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}
