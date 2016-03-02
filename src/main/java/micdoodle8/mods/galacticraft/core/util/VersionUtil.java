package micdoodle8.mods.galacticraft.core.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import micdoodle8.mods.miccore.MicdoodleTransformer;
import micdoodle8.mods.miccore.MicdoodleTransformer.MethodObfuscationEntry;
import micdoodle8.mods.miccore.MicdoodleTransformer.ObfuscationEntry;
import micdoodle8.mods.miccore.MicdoodleTransformer.FieldObfuscationEntry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VersionUtil
{
    private static DefaultArtifactVersion mcVersion = null;
    public static boolean mcVersion1_7_2 = false;
    public static boolean mcVersion1_7_10 = false;
    private static boolean deobfuscated = true;
    private static HashMap<String, MicdoodleTransformer.ObfuscationEntry> nodemap = Maps.newHashMap();
    private static HashMap<Integer, Object> reflectionCache = Maps.newHashMap();
    //Note: in reflectionCache, currently positions 3, 5, 7, 11, 13, 15, 17 are unused and 21 onwards are also free.

    private static final String KEY_CLASS_COMPRESSED_STREAM_TOOLS = "compressedStreamTools";
    private static final String KEY_CLASS_NBT_SIZE_TRACKER = "nbtSizeTracker";
    private static final String KEY_CLASS_YGG_CONVERTER = "preYggdrasilConverter";
    private static final String KEY_CLASS_TEXTURE_UTIL = "textureUtil";
    private static final String KEY_CLASS_COMMAND_BASE = "commandBase";
    private static final String KEY_CLASS_SCALED_RES = "scaledResolution";
    private static final String KEY_CLASS_RENDER_PLAYER = "renderPlayer";
    private static final String KEY_CLASS_ENTITYLIST = "entityList";

    private static final String KEY_METHOD_SET_OWNER = "setOwner";
    private static final String KEY_METHOD_GET_OWNER = "getOwnerName";
    private static final String KEY_METHOD_CONVERT_UUID = "yggdrasilConvert";
    private static final String KEY_METHOD_DECOMPRESS_NBT = "decompress";
    private static final String KEY_METHOD_SET_MIPMAP = "setMipMap";
    private static final String KEY_METHOD_NOTIFY_ADMINS = "notifyAdmins";
    private static final String KEY_METHOD_PLAYER_FOR_NAME = "getPlayerForUsername";
    private static final String KEY_METHOD_PLAYER_IS_OPPED = "isPlayerOpped";
    private static final String KEY_METHOD_PLAYER_TEXTURE = "getEntityTexture";

    //Used in GCPlayerHandler etc
    public static final String KEY_FIELD_FLOATINGTICKCOUNT = "floatingTickCount";
    public static final String KEY_FIELD_BIOMEINDEXLAYER = "biomeIndexLayer";
	public static final String KEY_FIELD_MUSICTICKER = "mcMusicTicker";

	public static final String KEY_FIELD_CAMERA_ZOOM = "cameraZoom";
	public static final String KEY_FIELD_CAMERA_YAW = "cameraYaw";
	public static final String KEY_FIELD_CAMERA_PITCH = "cameraPitch";
	public static final String KEY_FIELD_CLASSTOIDMAPPING = "classToIDMapping";
	public static final String KEY_FIELD_CHUNKCACHE_WORLDOBJ = "chunkCacheWorldObj";

	public static final String KEY_METHOD_ORIENT_CAMERA = "orientCamera";
	public static Block sand;

    static
    {
        mcVersion = new DefaultArtifactVersion((String) FMLInjectionData.data()[4]);
        mcVersion1_7_2 = VersionUtil.mcVersionMatches("1.7.2");
        mcVersion1_7_10 = VersionUtil.mcVersionMatches("1.7.10");

        try
        {
            deobfuscated = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

        if (mcVersion1_7_10)
        {
//            nodemap.put(KEY_CLASS_COMPRESSED_STREAM_TOOLS, new ObfuscationEntry("net/minecraft/nbt/CompressedStreamTools", "du"));
//            nodemap.put(KEY_CLASS_NBT_SIZE_TRACKER, new ObfuscationEntry("net/minecraft/nbt/NBTSizeTracker", "ds"));
//            nodemap.put(KEY_CLASS_YGG_CONVERTER, new ObfuscationEntry("net/minecraft/server/management/PreYggdrasilConverter", "nz"));
//            nodemap.put(KEY_CLASS_TEXTURE_UTIL, new ObfuscationEntry("net/minecraft/client/renderer/texture/TextureUtil", "bqi"));
//            nodemap.put(KEY_CLASS_COMMAND_BASE, new ObfuscationEntry("net/minecraft/command/CommandBase", "y"));
//            nodemap.put(KEY_CLASS_SCALED_RES, new ObfuscationEntry("net/minecraft/client/gui/ScaledResolution", "bca"));
            nodemap.put(KEY_CLASS_COMPRESSED_STREAM_TOOLS, new ObfuscationEntry("net/minecraft/nbt/CompressedStreamTools"));
            nodemap.put(KEY_CLASS_NBT_SIZE_TRACKER, new ObfuscationEntry("net/minecraft/nbt/NBTSizeTracker"));
            nodemap.put(KEY_CLASS_YGG_CONVERTER, new ObfuscationEntry("net/minecraft/server/management/PreYggdrasilConverter"));
            nodemap.put(KEY_CLASS_TEXTURE_UTIL, new ObfuscationEntry("net/minecraft/client/renderer/texture/TextureUtil"));
            nodemap.put(KEY_CLASS_COMMAND_BASE, new ObfuscationEntry("net/minecraft/command/CommandBase"));
            nodemap.put(KEY_CLASS_SCALED_RES, new ObfuscationEntry("net/minecraft/client/gui/ScaledResolution"));
            nodemap.put(KEY_CLASS_RENDER_PLAYER, new ObfuscationEntry("net/minecraft/client/renderer/entity/RenderPlayer"));
            nodemap.put(KEY_CLASS_ENTITYLIST, new ObfuscationEntry("net/minecraft/entity/EntityList"));

            // Method descriptions are empty, since they are not needed for reflection.
            nodemap.put(KEY_METHOD_SET_OWNER, new MethodObfuscationEntry("func_152115_b", "func_152115_b", ""));
            nodemap.put(KEY_METHOD_GET_OWNER, new MethodObfuscationEntry("func_152113_b", "func_152113_b", ""));
            nodemap.put(KEY_METHOD_CONVERT_UUID, new MethodObfuscationEntry("func_152719_a", "func_152719_a", ""));
            nodemap.put(KEY_METHOD_DECOMPRESS_NBT, new MethodObfuscationEntry("func_152457_a", "func_152457_a", ""));
            nodemap.put(KEY_METHOD_SET_MIPMAP, new MethodObfuscationEntry("func_152777_a", "func_152777_a", ""));
            nodemap.put(KEY_METHOD_NOTIFY_ADMINS, new MethodObfuscationEntry("func_152373_a", "func_152373_a", ""));
            nodemap.put(KEY_METHOD_PLAYER_FOR_NAME, new MethodObfuscationEntry("func_152612_a", "func_152612_a", ""));
            nodemap.put(KEY_METHOD_PLAYER_IS_OPPED, new MethodObfuscationEntry("func_152596_g", "func_152596_g", ""));
            nodemap.put(KEY_METHOD_PLAYER_TEXTURE, new MethodObfuscationEntry("getEntityTexture", "func_110775_a", ""));
            sand = Blocks.sand;
        }
        else if (mcVersion1_7_2)
        {
//            nodemap.put(KEY_CLASS_COMPRESSED_STREAM_TOOLS, new ObfuscationEntry("net/minecraft/nbt/CompressedStreamTools", "dr"));
//            nodemap.put(KEY_CLASS_NBT_SIZE_TRACKER, new ObfuscationEntry("", "")); // Not part of 1.7.2
//            nodemap.put(KEY_CLASS_YGG_CONVERTER, new ObfuscationEntry("", "")); // Not part of 1.7.2
//            nodemap.put(KEY_CLASS_TEXTURE_UTIL, new ObfuscationEntry("net/minecraft/client/renderer/texture/TextureUtil", "bqa"));
//            nodemap.put(KEY_CLASS_COMMAND_BASE, new ObfuscationEntry("net/minecraft/command/CommandBase", "y"));
//            nodemap.put(KEY_CLASS_SCALED_RES, new ObfuscationEntry("net/minecraft/client/gui/ScaledResolution", "bam"));
            nodemap.put(KEY_CLASS_COMPRESSED_STREAM_TOOLS, new ObfuscationEntry("net/minecraft/nbt/CompressedStreamTools"));
            nodemap.put(KEY_CLASS_NBT_SIZE_TRACKER, new ObfuscationEntry("", "")); // Not part of 1.7.2
            nodemap.put(KEY_CLASS_YGG_CONVERTER, new ObfuscationEntry("", "")); // Not part of 1.7.2
            nodemap.put(KEY_CLASS_TEXTURE_UTIL, new ObfuscationEntry("net/minecraft/client/renderer/texture/TextureUtil"));
            nodemap.put(KEY_CLASS_COMMAND_BASE, new ObfuscationEntry("net/minecraft/command/CommandBase"));
            nodemap.put(KEY_CLASS_SCALED_RES, new ObfuscationEntry("net/minecraft/client/gui/ScaledResolution"));
            nodemap.put(KEY_CLASS_RENDER_PLAYER, new ObfuscationEntry("net/minecraft/client/renderer/entity/RenderPlayer"));
            nodemap.put(KEY_CLASS_ENTITYLIST, new ObfuscationEntry("net/minecraft/entity/EntityList"));

            nodemap.put(KEY_METHOD_SET_OWNER, new MethodObfuscationEntry("setOwner", "func_70910_a", ""));
            nodemap.put(KEY_METHOD_GET_OWNER, new MethodObfuscationEntry("getOwnerName", "func_70905_p", ""));
            nodemap.put(KEY_METHOD_CONVERT_UUID, new MethodObfuscationEntry("", "", "")); // Not part of 1.7.2
            nodemap.put(KEY_METHOD_DECOMPRESS_NBT, new MethodObfuscationEntry("decompress", "func_74792_a", ""));
            nodemap.put(KEY_METHOD_SET_MIPMAP, new MethodObfuscationEntry("func_147950_a", "func_147950_a", ""));
            nodemap.put(KEY_METHOD_NOTIFY_ADMINS, new MethodObfuscationEntry("notifyAdmins", "func_71522_a", ""));
            nodemap.put(KEY_METHOD_PLAYER_FOR_NAME, new MethodObfuscationEntry("getPlayerForUsername", "func_72361_f", ""));
            nodemap.put(KEY_METHOD_PLAYER_IS_OPPED, new MethodObfuscationEntry("isPlayerOpped", "func_72353_e", ""));
            nodemap.put(KEY_METHOD_PLAYER_TEXTURE, new MethodObfuscationEntry("getEntityTexture", "func_110775_a", ""));
            
            try {
				Field sandField = Blocks.class.getField(deobfuscated ? "sand" : "field_150354_m");
				sand = (Block) sandField.get(null);
			} catch (Exception e) { e.printStackTrace(); }
        }

        //Same for both versions
        nodemap.put(KEY_FIELD_FLOATINGTICKCOUNT, new ObfuscationEntry("floatingTickCount", "field_147365_f"));
        nodemap.put(KEY_FIELD_BIOMEINDEXLAYER, new ObfuscationEntry("biomeIndexLayer", "field_76945_e"));
        nodemap.put(KEY_FIELD_MUSICTICKER, new ObfuscationEntry("mcMusicTicker", "field_147126_aw"));

        nodemap.put(KEY_FIELD_CAMERA_ZOOM, new FieldObfuscationEntry("cameraZoom", "field_78503_V"));
        nodemap.put(KEY_FIELD_CAMERA_YAW, new FieldObfuscationEntry("cameraYaw", "field_78502_W"));
        nodemap.put(KEY_FIELD_CAMERA_PITCH, new FieldObfuscationEntry("cameraPitch", "field_78509_X"));
        nodemap.put(KEY_FIELD_CLASSTOIDMAPPING, new FieldObfuscationEntry("classToIDMapping", "field_75624_e"));
        nodemap.put(KEY_FIELD_CHUNKCACHE_WORLDOBJ, new FieldObfuscationEntry("worldObj", "field_72815_e"));
        
        nodemap.put(KEY_METHOD_ORIENT_CAMERA, new MethodObfuscationEntry("orientCamera", "func_78467_g", ""));
    }

    public static boolean mcVersionMatches(String version)
    {
        return VersionParser.parseRange("[" + version + "]").containsVersion(mcVersion);
    }

    @Optional.Method(modid = Constants.MOD_ID_PLANETS)
    public static void setSlimelingOwner(EntitySlimeling slimeling, String ownerName)
    {
        try
        {
            Method m = (Method) reflectionCache.get(0);

            if (m == null)
            {
                m = slimeling.getClass().getSuperclass().getMethod(getNameDynamic(KEY_METHOD_SET_OWNER), new Class[] { String.class });
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
            Method m = (Method) reflectionCache.get(1);

            if (m == null)
            {
                m = slimeling.getClass().getMethod(getNameDynamic(KEY_METHOD_GET_OWNER));
                reflectionCache.put(1, m);
            }

            return (String) m.invoke(slimeling);
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
                if (mcVersion1_7_10)
                {
                    Method m = (Method) reflectionCache.get(2);

                    if (m == null)
                    {
                        Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_YGG_CONVERTER).replace('/', '.'));
                        m = c.getMethod(getNameDynamic(KEY_METHOD_CONVERT_UUID), new Class[] { String.class });
                        reflectionCache.put(2, m);
                    }

                    String s1 = nbt.getString("Owner");
                    s = (String) m.invoke(null, s1);
                }
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
            if (mcVersion1_7_10)
            {
                Class<?> c0 = (Class<?>) reflectionCache.get(4);
                Method m = (Method) reflectionCache.get(6);

                if (c0 == null)
                {
                    c0 = Class.forName(getNameDynamic(KEY_CLASS_NBT_SIZE_TRACKER).replace('/', '.'));
                    reflectionCache.put(4, c0);
                }

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_COMPRESSED_STREAM_TOOLS).replace('/', '.'));
                    m = c.getMethod(getNameDynamic(KEY_METHOD_DECOMPRESS_NBT), byte[].class, c0);
                    reflectionCache.put(6, m);
                }

                Object nbtSizeTracker = c0.getConstructor(long.class).newInstance(2097152L);
                return (NBTTagCompound) m.invoke(null, compressedNBT, nbtSizeTracker);
            }
            else if (mcVersion1_7_2)
            {
                Method m = (Method) reflectionCache.get(6);

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_COMPRESSED_STREAM_TOOLS).replace('/', '.'));
                    m = c.getMethod(getNameDynamic(KEY_METHOD_DECOMPRESS_NBT), byte[].class);
                    reflectionCache.put(6, m);
                }

                return (NBTTagCompound) m.invoke(null, compressedNBT);
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
            if (mcVersion1_7_10)
            {
                Method m = (Method) reflectionCache.get(8);

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_TEXTURE_UTIL).replace('/', '.'));
                    m = c.getMethod(getNameDynamic(KEY_METHOD_SET_MIPMAP), new Class[] { boolean.class, boolean.class, float.class });
                    reflectionCache.put(8, m);
                }

                m.invoke(null, b0, b1, 1.0F);
            }
            else if (mcVersion1_7_2)
            {
                Method m = (Method) reflectionCache.get(8);

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_TEXTURE_UTIL).replace('/', '.'));
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
            if (mcVersion1_7_10)
            {
                Method m = (Method) reflectionCache.get(10);

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_COMMAND_BASE).replace('/', '.'));
                    m = c.getMethod(getNameDynamic(KEY_METHOD_NOTIFY_ADMINS), new Class[] { ICommandSender.class, ICommand.class, String.class, Object[].class });
                    reflectionCache.put(10, m);
                }

                m.invoke(null, sender, command, name, objects);
            }
            else if (mcVersion1_7_2)
            {
                Method m = (Method) reflectionCache.get(10);

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_COMMAND_BASE).replace('/', '.'));
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
            Method m = (Method) reflectionCache.get(12);

            if (m == null)
            {
                Class<?> c = server.getConfigurationManager().getClass();
                m = c.getMethod(getNameDynamic(KEY_METHOD_PLAYER_FOR_NAME), new Class[] { String.class });
                reflectionCache.put(12, m);
            }

            return (EntityPlayerMP) m.invoke(server.getConfigurationManager(), username);
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
            if (mcVersion1_7_10)
            {
                Method m = (Method) reflectionCache.get(14);

                if (m == null)
                {
                    Class<?> c = player.mcServer.getConfigurationManager().getClass();
                    m = c.getMethod(getNameDynamic(KEY_METHOD_PLAYER_IS_OPPED), new Class[] { GameProfile.class });
                    reflectionCache.put(14, m);
                }

                return (Boolean) m.invoke(player.mcServer.getConfigurationManager(), player.getGameProfile());
            }
            else if (mcVersion1_7_2)
            {
                Method m = (Method) reflectionCache.get(14);

                if (m == null)
                {
                    Class<?> c = player.mcServer.getConfigurationManager().getClass();
                    m = c.getMethod(getNameDynamic(KEY_METHOD_PLAYER_IS_OPPED), new Class[] { String.class });
                    reflectionCache.put(14, m);
                }

                return (Boolean) m.invoke(player.mcServer.getConfigurationManager(), player.getGameProfile().getName());
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
            if (mcVersion1_7_10)
            {
                Constructor m = (Constructor) reflectionCache.get(16);

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_SCALED_RES).replace('/', '.'));
                    m = c.getConstructor(new Class[] { Minecraft.class, int.class, int.class });
                    reflectionCache.put(16, m);
                }

                return (ScaledResolution) m.newInstance(mc, width, height);
            }
            else if (mcVersion1_7_2)
            {
                Constructor m = (Constructor) reflectionCache.get(16);

                if (m == null)
                {
                    Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_SCALED_RES).replace('/', '.'));
                    m = c.getConstructor(new Class[] { GameSettings.class, int.class, int.class });
                    reflectionCache.put(16, m);
                }

                return (ScaledResolution) m.newInstance(mc.gameSettings, width, height);
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return null;
    }

    public static Method getPlayerTextureMethod()
    {
        try
        {
            Method m = (Method) reflectionCache.get(18);

            if (m == null)
            {
                Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_RENDER_PLAYER).replace('/', '.'));
                m = c.getMethod(getNameDynamic(KEY_METHOD_PLAYER_TEXTURE), new Class[] { AbstractClientPlayer.class });
                m.setAccessible(true);
                reflectionCache.put(18, m);
            }

            return m;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return null;
    }

    public static void putClassToIDMapping(Class mobClazz, int id)
    {
        //Achieves this, with private field:
        //    EntityList.classToIDMapping.put(mobClazz, id);
    	try
        {
            Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_ENTITYLIST).replace('/', '.'));
            Field f = c.getDeclaredField(getNameDynamic(KEY_FIELD_CLASSTOIDMAPPING));
            f.setAccessible(true);
            Map classToIDMapping = (Map) f.get(null);
            classToIDMapping.put(mobClazz, id);

            return;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return;
    }

    public static int getClassToIDMapping(Class mobClazz)
    {
        //Achieves this, with private field:
        //    EntityList.classToIDMapping.put(mobClazz, id);
    	try
        {
            Class<?> c = Class.forName(getNameDynamic(KEY_CLASS_ENTITYLIST).replace('/', '.'));
            Field f = c.getDeclaredField(getNameDynamic(KEY_FIELD_CLASSTOIDMAPPING));
            f.setAccessible(true);
            Map classToIDMapping = (Map) f.get(null);
            Integer i = (Integer) classToIDMapping.get(mobClazz);
            
            return i != null ? i : 0;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return 0;
    }

    private static String getName(String keyName)
    {
        return nodemap.get(keyName).name;
    }

    private static String getObfName(String keyName)
    {
        return nodemap.get(keyName).obfuscatedName;
    }

    public static String getNameDynamic(String keyName)
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

	public static GameProfile constructGameProfile(UUID uuid, String strName)
	{
        try
        {
			Class<?> c = (Class<?>) reflectionCache.get(19);
	        if (c == null)
	        {
	            c = Class.forName("com.mojang.authlib.GameProfile");
	            reflectionCache.put(19, c);
	        }
			
			if (mcVersion1_7_10)
	        {
				return (GameProfile) c.getConstructor(UUID.class, String.class).newInstance(uuid, strName);
	        }
	        
			if (mcVersion1_7_2)
	        {
	        	return (GameProfile) c.getConstructor(String.class, String.class).newInstance(uuid.toString().replaceAll("-", ""), strName);
	        }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        return null;
	}

	public static World getWorld(IBlockAccess world)
	{
        if (world instanceof World)
        	return (World) world;
        
        if (world instanceof ChunkCache)
		try
        {
			Field f = (Field) reflectionCache.get(20);
	        if (f == null)
	        {
	            Class c = Class.forName("net.minecraft.world.ChunkCache");
	            f = c.getDeclaredField(getNameDynamic(KEY_FIELD_CHUNKCACHE_WORLDOBJ));
	            f.setAccessible(true);
	            reflectionCache.put(20, f);
	        }
	        
	        return (World) f.get(world);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
		
		return null;
	}
	
	public static ItemStack createStack(Block block, int meta)
	{
		try
        {
			Method m = (Method) reflectionCache.get(3);
	        if (m == null)
	        {
	            Class c = Class.forName("net.minecraft.block.Block");
	            Method mm[] = c.getDeclaredMethods();
	            for (Method testMethod : mm)
	            {
	            	if (testMethod.getName().equals("func_149644_j"))
	            	{
	            		m = testMethod;
	            		break;
	            	}
	            }
	            m.setAccessible(true);
	            reflectionCache.put(3, m);
	        }
	        
	        return (ItemStack) m.invoke(block,  meta);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
		
		return null;	
	}
}
