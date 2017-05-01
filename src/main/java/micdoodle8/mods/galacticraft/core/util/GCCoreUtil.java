package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.List;

public class GCCoreUtil
{
    public static int nextID = 0;
    private static boolean deobfuscated;

    static
    {
        try
        {
            deobfuscated = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean isDeobfuscated()
    {
        return deobfuscated;
    }

    public static void openBuggyInv(EntityPlayerMP player, IInventory buggyInv, int type)
    {
        player.getNextWindowId();
        player.closeContainer();
        int id = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { id, 0, 0 }), player);
        player.openContainer = new ContainerBuggy(player.inventory, buggyInv, type, player);
        player.openContainer.windowId = id;
        player.openContainer.onCraftGuiOpened(player);
    }

    public static void openParachestInv(EntityPlayerMP player, EntityLanderBase landerInv)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { windowId, 1, landerInv.getEntityId() }), player);
        player.openContainer = new ContainerParaChest(player.inventory, landerInv, player);
        player.openContainer.windowId = windowId;
        player.openContainer.onCraftGuiOpened(player);
    }

    public static int nextInternalID()
    {
        GCCoreUtil.nextID++;
        return GCCoreUtil.nextID - 1;
    }

    public static void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int back, int fore)
    {
        registerGalacticraftNonMobEntity(var0, var1, 80, 3, true);
        int nextEggID = getNextValidEggID();
        if (nextEggID < 65536)
        {
            EntityList.idToClassMapping.put(nextEggID, var0);
            EntityList.classToIDMapping.put(var0, nextEggID);
            EntityList.entityEggs.put(nextEggID, new EntityList.EntityEggInfo(nextEggID, back, fore));
        }
    }

    public static int getNextValidEggID()
    {
        int eggID = 255;

        //Non-global entity IDs - for egg ID purposes - can be greater than 255
        //The spawn egg will have this metadata.  Metadata up to 65535 is acceptable (see potions).

        do
        {
            eggID++;
        }
        while (EntityList.getClassFromID(eggID) != null);

        return eggID;
    }

    public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
        if (GCCoreUtil.getEffectiveSide() == Side.CLIENT)
        {
            LanguageRegistry.instance().addStringLocalization("entity.galacticraftcore." + var1 + ".name", "en_US", GCCoreUtil.translate("entity." + var1 + ".name"));
            LanguageRegistry.instance().addStringLocalization("entity.GalacticraftCore." + var1 + ".name", GCCoreUtil.translate("entity." + var1 + ".name"));
        }
        EntityRegistry.registerModEntity(var0, var1, nextInternalID(), GalacticraftCore.instance, trackingDistance, updateFreq, sendVel);
    }

    public static void registerGalacticraftItem(String key, Item item)
    {
        GalacticraftCore.itemList.put(key, new ItemStack(item));
    }

    public static void registerGalacticraftItem(String key, Item item, int metadata)
    {
        GalacticraftCore.itemList.put(key, new ItemStack(item, 1, metadata));
    }

    public static void registerGalacticraftItem(String key, ItemStack stack)
    {
        GalacticraftCore.itemList.put(key, stack);
    }

    public static void registerGalacticraftBlock(String key, Block block)
    {
        GalacticraftCore.blocksList.put(key, new ItemStack(block));
    }

    public static void registerGalacticraftBlock(String key, Block block, int metadata)
    {
        GalacticraftCore.blocksList.put(key, new ItemStack(block, 1, metadata));
    }

    public static void registerGalacticraftBlock(String key, ItemStack stack)
    {
        GalacticraftCore.blocksList.put(key, stack);
    }

    public static String translate(String key)
    {
        String result = StatCollector.translateToLocal(key);
        int comment = result.indexOf('#');
        String ret = (comment > 0) ? result.substring(0, comment).trim() : result;
        for (int i = 0; i < key.length(); ++i)
        {
            Character c = key.charAt(i);
            if (Character.isUpperCase(c))
            {
                System.err.println(ret);
            }
        }
        return ret;
    }

    public static List<String> translateWithSplit(String key)
    {
        String translated = translate(key);
        int comment = translated.indexOf('#');
        translated = (comment > 0) ? translated.substring(0, comment).trim() : translated;
        return Arrays.asList(translated.split("\\$"));
    }

    public static String translateWithFormat(String key, Object... values)
    {
        String result = StatCollector.translateToLocalFormatted(key, values);
        int comment = result.indexOf('#');
        String ret = (comment > 0) ? result.substring(0, comment).trim() : result;
        for (int i = 0; i < key.length(); ++i)
        {
            Character c = key.charAt(i);
            if (Character.isUpperCase(c))
            {
                System.err.println(ret);
            }
        }
        return ret;
    }

    public static void drawStringRightAligned(String string, int x, int y, int color, FontRenderer fontRendererObj)
    {
        fontRendererObj.drawString(string, x - fontRendererObj.getStringWidth(string), y, color);
    }

    public static void drawStringCentered(String string, int x, int y, int color, FontRenderer fontRendererObj)
    {
        fontRendererObj.drawString(string, x - fontRendererObj.getStringWidth(string) / 2, y, color);
    }

    public static String lowerCaseNoun(String string)
    {
        Language l = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage();
        if (l.getLanguageCode().equals("de_DE"))
        {
            return string;
        }
        return GCCoreUtil.translate(string).toLowerCase();
    }

    public static int getDimensionID(World world)
    {
        return world.provider.getDimensionId();
    }

    public static int getDimensionID(WorldProvider provider)
    {
        return provider.getDimensionId();
    }

    public static int getDimensionID(TileEntity tileEntity)
    {
        return tileEntity.getWorld().provider.getDimensionId();
    }

    public static void sendToAllDimensions(EnumSimplePacket packetType, Object[] data)
    {
        for (WorldServer world : MinecraftServer.getServer().worldServers)
        {
            int id = getDimensionID(world);
            GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(packetType, id, data), id);
        }
    }

    public static void sendToAllAround(PacketSimple packet, World world, int dimID, BlockPos pos, double radius)
    {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        double r2 = radius * radius;
        for (EntityPlayer playerMP : world.playerEntities)
        {
            if (playerMP.dimension == dimID)
            {
                final double dx = x - playerMP.posX;
                final double dy = y - playerMP.posY;
                final double dz = z - playerMP.posZ;

                if (dx * dx + dy * dy + dz * dz < r2)
                {
                    GalacticraftCore.packetPipeline.sendTo(packet, (EntityPlayerMP) playerMP);
                }
            }
        }
    }

//    public static void sortBlock(Block block, int meta, StackSorted beforeStack)
//    {
//        StackSorted newStack = new StackSorted(Item.getItemFromBlock(block), meta);
//
//        // Remove duplicates
//        for (Iterator<StackSorted> it = GalacticraftCore.itemOrderListBlocks.iterator(); it.hasNext();)
//        {
//            StackSorted stack = it.next();
//            if (stack.equals(newStack))
//            {
//                it.remove();
//            }
//        }
//
//        if (beforeStack == null)
//        {
//            GalacticraftCore.itemOrderListBlocks.add(newStack);
//        }
//        else
//        {
//            for (int i = 0; i < GalacticraftCore.itemOrderListBlocks.size(); ++i)
//            {
//                if (GalacticraftCore.itemOrderListBlocks.get(i).equals(beforeStack))
//                {
//                    GalacticraftCore.itemOrderListBlocks.add(i + 1, newStack);
//                    return;
//                }
//            }
//
//            throw new RuntimeException("Could not find block to insert before: " + beforeStack);
//        }
//    }
//
//    public static void sortItem(Item item, int meta, StackSorted beforeStack)
//    {
//        StackSorted newStack = new StackSorted(item, meta);
//
//        // Remove duplicates
//        for (Iterator<StackSorted> it = GalacticraftCore.itemOrderListBlocks.iterator(); it.hasNext();)
//        {
//            StackSorted stack = it.next();
//            if (stack.equals(newStack))
//            {
//                it.remove();
//            }
//        }
//
//        if (beforeStack == null)
//        {
//            GalacticraftCore.itemOrderListItems.add(newStack);
//        }
//        else
//        {
//            for (int i = 0; i < GalacticraftCore.itemOrderListItems.size(); ++i)
//            {
//                if (GalacticraftCore.itemOrderListItems.get(i).equals(beforeStack))
//                {
//                    GalacticraftCore.itemOrderListItems.add(i + 1, newStack);
//                    break;
//                }
//            }
//        }
//    }

    /**
     * Custom getEffectiveSide method, covering more cases than FMLCommonHandler
     */
    public static Side getEffectiveSide()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER || Thread.currentThread().getName().startsWith("Netty Epoll Server IO"))
        {
            return Side.SERVER;
        }

        return Side.CLIENT;
    }
}
