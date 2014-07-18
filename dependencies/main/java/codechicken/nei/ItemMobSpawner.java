package codechicken.nei;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemMobSpawner extends ItemBlock
{
    private static Map<Integer, EntityLiving> entityHashMap;
    private static Map<Integer, String> IDtoNameMap;
    public static int idPig;
    private static boolean loaded;

    public ItemMobSpawner() {
        super(Blocks.mob_spawner);

        hasSubtypes = true;
        MinecraftForgeClient.registerItemRenderer(this, new SpawnerRenderer());

        entityHashMap = new HashMap<Integer, EntityLiving>();
        IDtoNameMap = new HashMap<Integer, String>();
    }

    /**
     * These are ASM translated from BlockMobSpawner
     */
    public static int placedX;
    public static int placedY;
    public static int placedZ;

    @Override
    public IIcon getIconFromDamage(int par1) {
        return Blocks.mob_spawner.getBlockTextureFromSide(0);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
        if (super.onItemUse(itemstack, entityplayer, world, x, y, z, par7, par8, par9, par10) && world.isRemote) {
            TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner) world.getTileEntity(placedX, placedY, placedZ);
            if (tileentitymobspawner != null) {
                setDefaultTag(itemstack);
                String mobtype = IDtoNameMap.get(itemstack.getItemDamage());
                if (mobtype != null) {
                    NEICPH.sendMobSpawnerID(placedX, placedY, placedZ, mobtype);
                    tileentitymobspawner.func_145881_a().setEntityName(mobtype);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
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
            World world = NEIClientUtils.mc().theWorld;
            loadSpawners(world);
            Class<?> clazz = (Class<?>) EntityList.IDtoClassMapping.get(ID);
            try {
                e = (EntityLiving) clazz.getConstructor(new Class[]{World.class}).newInstance(world);
            } catch (Throwable t) {
                NEIClientConfig.logger.error("Error creating instance of entity: " + clazz.getName(), t);
                e = getEntity(idPig);
            }
            entityHashMap.put(ID, e);
        }
        return e;
    }

    private void setDefaultTag(ItemStack itemstack) {
        if (!IDtoNameMap.containsKey(itemstack.getItemDamage()))
            itemstack.setItemDamage(idPig);
    }

    public static void loadSpawners(World world) {
        if (loaded) return;
        loaded = true;
        HashMap<Class<Entity>, String> classToStringMapping = (HashMap<Class<Entity>, String>) EntityList.classToStringMapping;
        HashMap<Class<Entity>, Integer> classToIDMapping = (HashMap<Class<Entity>, Integer>) EntityList.classToIDMapping;
        for (Class<Entity> eclass : classToStringMapping.keySet()) {
            if (!EntityLiving.class.isAssignableFrom(eclass))
                continue;
            try {
                EntityLiving entityliving = (EntityLiving) eclass.getConstructor(new Class[]{World.class}).newInstance(world);
                entityliving.isChild();

                int id = classToIDMapping.get(eclass);
                String name = classToStringMapping.get(eclass);

                if (name.equals("EnderDragon"))
                    continue;

                IDtoNameMap.put(id, name);

                if (name.equals("Pig"))
                    idPig = id;
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i : IDtoNameMap.keySet())
            list.add(new ItemStack(item, 1, i));
    }
}
