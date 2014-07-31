package micdoodle8.mods.galacticraft.core.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import micdoodle8.mods.miccore.MicdoodleTransformer;
import micdoodle8.mods.miccore.MicdoodleTransformer.MethodObfuscationEntry;
import micdoodle8.mods.miccore.MicdoodleTransformer.ObfuscationEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class VersionUtil
{
    private static DefaultArtifactVersion mcVersion = null;
    private static boolean deobfuscated = true;
    private static HashMap<String, MicdoodleTransformer.ObfuscationEntry> nodemap = Maps.newHashMap();
    private static HashMap<Integer, Object> reflectionCache = Maps.newHashMap();

    private static final String KEY_CLASS_COMPRESSED_STREAM_TOOLS = "compressedStreamTools";
    private static final String KEY_CLASS_NBT_SIZE_TRACKER = "nbtSizeTracker";
    private static final String KEY_CLASS_YGG_CONVERTER = "preYggdrasilConverter";
    private static final String KEY_CLASS_TEXTURE_UTIL = "textureUtil";
    private static final String KEY_CLASS_COMMAND_BASE = "commandBase";
    private static final String KEY_CLASS_SCALED_RES = "scaledResolution";

    private static final String KEY_METHOD_SET_OWNER = "setOwner";
    private static final String KEY_METHOD_GET_OWNER = "getOwnerName";
    private static final String KEY_METHOD_CONVERT_UUID = "yggdrasilConvert";
    private static final String KEY_METHOD_DECOMPRESS_NBT = "decompress";
    private static final String KEY_METHOD_SET_MIPMAP = "setMipMap";
    private static final String KEY_METHOD_NOTIFY_ADMINS = "notifyAdmins";
    private static final String KEY_METHOD_PLAYER_FOR_NAME = "getPlayerForUsername";
    private static final String KEY_METHOD_PLAYER_IS_OPPED = "isPlayerOpped";

    static
    {
        mcVersion = new DefaultArtifactVersion((String) FMLInjectionData.data()[4]);

        try
        {
            final URLClassLoader loader = new LaunchClassLoader(((URLClassLoader) GalacticraftCore.instance.getClass().getClassLoader()).getURLs());
            URL classResource = loader.findResource(String.valueOf("net.minecraft.world.World").replace('.', '/').concat(".class"));
            deobfuscated = classResource != null;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

        if (mcVersionMatches("1.7.10"))
        {
            nodemap.put(KEY_CLASS_COMPRESSED_STREAM_TOOLS, new ObfuscationEntry("net/minecraft/nbt/CompressedStreamTools", "du"));
            nodemap.put(KEY_CLASS_NBT_SIZE_TRACKER, new ObfuscationEntry("net/minecraft/nbt/NBTSizeTracker", "ds"));
            nodemap.put(KEY_CLASS_YGG_CONVERTER, new ObfuscationEntry("net/minecraft/server/management/PreYggdrasilConverter", "nz"));
            nodemap.put(KEY_CLASS_TEXTURE_UTIL, new ObfuscationEntry("net/minecraft/client/renderer/texture/TextureUtil", "bqi"));
            nodemap.put(KEY_CLASS_COMMAND_BASE, new ObfuscationEntry("net/minecraft/command/CommandBase", "y"));
            nodemap.put(KEY_CLASS_SCALED_RES, new ObfuscationEntry("net/minecraft/client/gui/ScaledResolution", "bca"));

            // Method descriptions are empty, since they are not needed for reflection.
            nodemap.put(KEY_METHOD_SET_OWNER, new MethodObfuscationEntry("func_152115_b", "b", ""));
            nodemap.put(KEY_METHOD_GET_OWNER, new MethodObfuscationEntry("func_152113_b", "b", ""));
            nodemap.put(KEY_METHOD_CONVERT_UUID, new MethodObfuscationEntry("func_152719_a", "a", ""));
            nodemap.put(KEY_METHOD_DECOMPRESS_NBT, new MethodObfuscationEntry("func_152457_a", "a", ""));
            nodemap.put(KEY_METHOD_SET_MIPMAP, new MethodObfuscationEntry("func_152777_a", "a", ""));
            nodemap.put(KEY_METHOD_NOTIFY_ADMINS, new MethodObfuscationEntry("func_152373_a", "a", ""));
            nodemap.put(KEY_METHOD_PLAYER_FOR_NAME, new MethodObfuscationEntry("func_152612_a", "a", ""));
            nodemap.put(KEY_METHOD_PLAYER_IS_OPPED, new MethodObfuscationEntry("func_152596_g", "g", ""));
        }
        else if (mcVersionMatches("1.7.2"))
        {
            nodemap.put(KEY_CLASS_COMPRESSED_STREAM_TOOLS, new ObfuscationEntry("net/minecraft/nbt/CompressedStreamTools", "du"));
            nodemap.put(KEY_CLASS_NBT_SIZE_TRACKER, new ObfuscationEntry("", "")); // Not part of 1.7.2
            nodemap.put(KEY_CLASS_YGG_CONVERTER, new ObfuscationEntry("", "")); // Not part of 1.7.2
            nodemap.put(KEY_CLASS_TEXTURE_UTIL, new ObfuscationEntry("net/minecraft/client/renderer/texture/TextureUtil", "bqi"));
            nodemap.put(KEY_CLASS_COMMAND_BASE, new ObfuscationEntry("net/minecraft/command/CommandBase", "y"));
            nodemap.put(KEY_CLASS_SCALED_RES, new ObfuscationEntry("net/minecraft/client/gui/ScaledResolution", "bca"));

            nodemap.put(KEY_METHOD_SET_OWNER, new MethodObfuscationEntry("setOwner", "b", ""));
            nodemap.put(KEY_METHOD_GET_OWNER, new MethodObfuscationEntry("getOwnerName", "b", ""));
            nodemap.put(KEY_METHOD_CONVERT_UUID, new MethodObfuscationEntry("", "", "")); // Not part of 1.7.2
            nodemap.put(KEY_METHOD_DECOMPRESS_NBT, new MethodObfuscationEntry("decompress", "a", ""));
            nodemap.put(KEY_METHOD_SET_MIPMAP, new MethodObfuscationEntry("func_147950_a", "a", ""));
            nodemap.put(KEY_METHOD_NOTIFY_ADMINS, new MethodObfuscationEntry("notifyAdmins", "a", ""));
            nodemap.put(KEY_METHOD_PLAYER_FOR_NAME, new MethodObfuscationEntry("getPlayerForUsername", "e", ""));
            nodemap.put(KEY_METHOD_PLAYER_IS_OPPED, new MethodObfuscationEntry("isPlayerOpped", "d", ""));
        }
    }

    public static boolean mcVersionMatches(String version)
    {
        return VersionParser.parseRange("[" + version + "]").containsVersion(mcVersion);
    }

    public static void setSlimelingOwner(EntitySlimeling slimeling, String ownerName)
    {
        try
        {
            Method m = (Method)reflectionCache.get(0);

            if (m == null)
            {
                m = slimeling.getClass().getMethod(getNameDynamic(KEY_METHOD_SET_OWNER), new Class[] { String.class });
                reflectionCache.put(0, m);
            }

            m.invoke(slimeling, ownerName);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    public static String getSlimelingOwner(EntitySlimeling slimeling)
    {
        try
        {
            Method m = (Method)reflectionCache.get(1);

            if (m == null)
            {
                m = slimeling.getClass().getMethod(getNameDynamic(KEY_METHOD_GET_OWNER));
                reflectionCache.put(1, m);
            }

            return (String)m.invoke(slimeling);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return "";
    }

    public static void readSlimelingEggFromNBT(TileEntitySlimelingEgg egg, NBTTagCompound nbt)
    {
        try
        {
            String s = "";
            if (nbt.hasKey("OwnerUUID", 8))
            {
                s = nbt.getString("OwnerUUID");
            }
            else
            {
                Class<?> c = (Class)reflectionCache.get(2);

                if (c == null)
                {
                    c = Class.forName(getNameDynamic(KEY_CLASS_YGG_CONVERTER).replace('/', '.'));
                    reflectionCache.put(2, c);
                }

                Method m = (Method)reflectionCache.get(3);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_CONVERT_UUID), new Class[] { String.class });
                    reflectionCache.put(3, m);
                }

                String s1 = nbt.getString("Owner");
                    s = (String)m.invoke(null, s1);
            }

            if (s.length() > 0)
            {
                egg.lastTouchedPlayerUUID = s;
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    public static NBTTagCompound decompressNBT(byte[] compressedNBT)
    {
        try
        {
            Class<?> c = (Class<?>)reflectionCache.get(4);
            Class<?> c0 = (Class<?>)reflectionCache.get(5);

            if (c == null)
            {
                c = Class.forName(getNameDynamic(KEY_CLASS_COMPRESSED_STREAM_TOOLS).replace('/', '.'));
                reflectionCache.put(4, c);
            }

            if (c0 == null)
            {
                c0 = Class.forName(getNameDynamic(KEY_CLASS_NBT_SIZE_TRACKER).replace('/', '.'));
                reflectionCache.put(5, c0);
            }

            if (mcVersionMatches("1.7.10"))
            {
                Method m = (Method)reflectionCache.get(6);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_DECOMPRESS_NBT), new Class[] { byte[].class, c0 });
                    reflectionCache.put(6, m);
                }

                return (NBTTagCompound)m.invoke(null, compressedNBT, c0.getConstructor(new Class[] { Long.class }).newInstance(2097152L));
            }
            else if (mcVersionMatches("1.7.2"))
            {
                Method m = (Method)reflectionCache.get(6);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_DECOMPRESS_NBT), new Class[] { byte[].class });
                    reflectionCache.put(6, m);
                }

                return (NBTTagCompound)m.invoke(null, compressedNBT);
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return null;
    }

    public static void setMipMap(boolean b0, boolean b1)
    {
        try
        {
            Class<?> c = (Class<?>)reflectionCache.get(7);

            if (c == null)
            {
                c = Class.forName(getNameDynamic(KEY_CLASS_TEXTURE_UTIL).replace('/', '.'));
                reflectionCache.put(7, c);
            }

            if (mcVersionMatches("1.7.10"))
            {
                Method m = (Method)reflectionCache.get(8);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_SET_MIPMAP), new Class[] { boolean.class, boolean.class, float.class });
                    reflectionCache.put(8, m);
                }

                m.invoke(null, b0, b1, 1.0F);
            }
            else if (mcVersionMatches("1.7.2"))
            {
                Method m = (Method)reflectionCache.get(8);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_SET_MIPMAP), new Class[] { boolean.class, boolean.class });
                    reflectionCache.put(8, m);
                }

                m.invoke(null, b0, b1);
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    public static void notifyAdmins(ICommandSender sender, ICommand command, String name, Object... objects)
    {
        try
        {
            Class<?> c = (Class<?>)reflectionCache.get(9);

            if (c == null)
            {
                c = Class.forName(getNameDynamic(KEY_CLASS_COMMAND_BASE).replace('/', '.'));
                reflectionCache.put(9, c);
            }

            if (mcVersionMatches("1.7.10"))
            {
                Method m = (Method)reflectionCache.get(10);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_NOTIFY_ADMINS), new Class[] { ICommandSender.class, ICommand.class, String.class, Object[].class });
                    reflectionCache.put(10, m);
                }

                m.invoke(null, sender, command, name, objects);
            }
            else if (mcVersionMatches("1.7.2"))
            {
                Method m = (Method)reflectionCache.get(10);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_NOTIFY_ADMINS), new Class[] { ICommandSender.class, String.class, Object[].class });
                    reflectionCache.put(10, m);
                }

                m.invoke(null, sender, name, objects);
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    public static EntityPlayerMP getPlayerForUsername(MinecraftServer server, String username)
    {
        try
        {
            Class<?> c = (Class<?>)reflectionCache.get(11);

            if (c == null)
            {
                c = server.getConfigurationManager().getClass();
                reflectionCache.put(11, c);
            }

            Method m = (Method)reflectionCache.get(12);

            if (m == null)
            {
                m = c.getMethod(getNameDynamic(KEY_METHOD_PLAYER_FOR_NAME), new Class[] { String.class });
                reflectionCache.put(12, m);
            }

            return (EntityPlayerMP)m.invoke(server.getConfigurationManager(), username);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return null;
    }

    public static boolean isPlayerOpped(EntityPlayerMP player)
    {
        try
        {
            Class<?> c = (Class<?>)reflectionCache.get(13);

            if (c == null)
            {
                c = player.mcServer.getConfigurationManager().getClass();
                reflectionCache.put(13, c);
            }

            if (mcVersionMatches("1.7.10"))
            {
                Method m = (Method)reflectionCache.get(14);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_PLAYER_IS_OPPED), new Class[] { GameProfile.class });
                    reflectionCache.put(14, m);
                }

                return (Boolean)m.invoke(player.mcServer.getConfigurationManager(), player.getGameProfile());
            }
            else if (mcVersionMatches("1.7.2"))
            {
                Method m = (Method)reflectionCache.get(14);

                if (m == null)
                {
                    m = c.getMethod(getNameDynamic(KEY_METHOD_PLAYER_IS_OPPED), new Class[] { String.class });
                    reflectionCache.put(14, m);
                }

                return (Boolean)m.invoke(player.mcServer.getConfigurationManager(), player.getGameProfile().getName());
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static ScaledResolution getScaledRes(Minecraft mc, int width, int height)
    {
        try
        {
            Class<?> c = (Class<?>)reflectionCache.get(15);

            if (c == null)
            {
                c = Class.forName(getNameDynamic(KEY_CLASS_SCALED_RES).replace('/', '.'));
                reflectionCache.put(15, c);
            }

            if (mcVersionMatches("1.7.10"))
            {
                Constructor m = (Constructor)reflectionCache.get(16);

                if (m == null)
                {
                    m = c.getConstructor(new Class[]{Minecraft.class, int.class, int.class});
                    reflectionCache.put(16, m);
                }

                return (ScaledResolution)m.newInstance(mc, width, height);
            }
            else if (mcVersionMatches("1.7.2"))
            {
                Constructor m = (Constructor)reflectionCache.get(16);

                if (m == null)
                {
                    m = c.getConstructor(new Class[]{ GameSettings.class, int.class, int.class});
                    reflectionCache.put(16, m);
                }

                return (ScaledResolution)m.newInstance(mc.gameSettings, width, height);
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return null;
    }

    private static String getName(String keyName)
    {
        return nodemap.get(keyName).name;
    }

    private static String getObfName(String keyName)
    {
        return nodemap.get(keyName).obfuscatedName;
    }

    private static String getNameDynamic(String keyName)
    {
        try
        {
            if (deobfuscated)
            {
                return getName(keyName);
            }
            else
            {
                return getObfName(keyName);
            }
        }
        catch (NullPointerException e)
        {
            System.err.println("Could not find key: " + keyName);
            throw e;
        }
    }
}
