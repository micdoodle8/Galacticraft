package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.Constants;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Nullable;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class GCCoreUtil
{
    public static int nextID = 0;
    private static boolean deobfuscated;
    private static String lastLang = "";
    public static boolean langDisable;

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
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.world), new Object[] { id, 0, 0 }), player);
        player.openContainer = new ContainerBuggy(player.inventory, buggyInv, type, player);
        player.openContainer.windowId = id;
        player.openContainer.addListener(player);
    }

    public static void openParachestInv(EntityPlayerMP player, EntityLanderBase landerInv)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.world), new Object[] { windowId, 1, landerInv.getEntityId() }), player);
        player.openContainer = new ContainerParaChest(player.inventory, landerInv, player);
        player.openContainer.windowId = windowId;
        player.openContainer.addListener(player);
    }

    public static int nextInternalID()
    {
        GCCoreUtil.nextID++;
        return GCCoreUtil.nextID - 1;
    }

    public static void registerGalacticraftCreature(Class<? extends Entity> clazz, String name, int back, int fore)
    {
        registerGalacticraftNonMobEntity(clazz, name, 80, 3, true);
        int nextEggID = getNextValidID();
        if (nextEggID < 65536)
        {
            ResourceLocation resourcelocation = new ResourceLocation(Constants.MOD_ID_CORE, name);
//            name = Constants.MOD_ID_CORE + "." + name;
            EntityList.ENTITY_EGGS.put(resourcelocation, new EntityList.EntityEggInfo(resourcelocation, back, fore));
        }
    }

    public static int getNextValidID()
    {
        int eggID = 255;

        //Non-global entity IDs - for egg ID purposes - can be greater than 255
        //The spawn egg will have this metadata.  Metadata up to 65535 is acceptable (see potions).

        do
        {
            eggID++;
        }
        while (net.minecraftforge.fml.common.registry.GameData.getEntityRegistry().getObjectById(eggID) != null);

        return eggID;
    }

    public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
        ResourceLocation registryName = new ResourceLocation(Constants.MOD_ID_CORE, var1);
        EntityRegistry.registerModEntity(registryName, var0, var1, nextInternalID(), GalacticraftCore.instance, trackingDistance, updateFreq, sendVel);
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
        String result = I18n.translateToLocal(key);
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
        String result = I18n.translateToLocalFormatted(key, values);
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

    @Nullable
    public static InputStream supplementEntityKeys(InputStream inputstream, String assetprefix) throws IOException
    {
        ArrayList<String> langLines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
        String line;
        String supplemented = "entity." + assetprefix.toLowerCase() + ".";
        
        //TODO:  We could also load en_US here and have any language keys not in the other lang set to the en_US value
        
        while((line = br.readLine()) != null)
        {
            line = line.trim();
            if (!line.isEmpty())
            {
                langLines.add(line);
                if (line.substring(0, 7).equals("entity."))
                {
                    langLines.add(supplemented + line.substring(7));
                }
            }
        }
        
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputstream, Charsets.UTF_8.newEncoder()));
        for (String outLine : langLines)
            writer.write(outLine + "\n");
        writer.close();

        return new ByteArrayInputStream(outputstream.toByteArray());
    }

    public static void loadLanguage(String langIdentifier, String assetPrefix, File source)
    {
        if (!lastLang.equals(langIdentifier))
        {
            langDisable = false;
        }
        if (langDisable) return;
        String langFile = "assets/" + assetPrefix + "/lang/" + langIdentifier + ".lang";
        InputStream stream = null;
        ZipFile zip = null;
        try
        {
            if (source.isDirectory() && (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
            {
                stream = new FileInputStream(new File(source.toURI().resolve(langFile).getPath()));
            }
            else
            {
                zip = new ZipFile(source);
                ZipEntry entry = zip.getEntry(langFile);
                if(entry == null) throw new FileNotFoundException();
                stream = zip.getInputStream(entry);
            }
            LanguageMap.inject(GCCoreUtil.supplementEntityKeys(stream, assetPrefix));
        }
        catch(FileNotFoundException fnf)
        {
            langDisable = true;
        }
        catch(Exception ignore) { }
        finally
        {
            if (stream != null) IOUtils.closeQuietly(stream);
            try
            {
                if (zip != null) zip.close();
            }
            catch (IOException ignore) {}
        }
    }

    public static int getDimensionID(World world)
    {
        return world.provider.getDimension();
    }

    public static int getDimensionID(WorldProvider provider)
    {
        return provider.getDimension();
    }

    public static int getDimensionID(TileEntity tileEntity)
    {
        return tileEntity.getWorld().provider.getDimension();
    }

    public static void sendToAllDimensions(EnumSimplePacket packetType, Object[] data)
    {
        for (WorldServer world : FMLCommonHandler.instance().getMinecraftServerInstance().worlds)
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
}
