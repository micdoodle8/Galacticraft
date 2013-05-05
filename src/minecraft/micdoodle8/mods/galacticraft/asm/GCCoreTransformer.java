package micdoodle8.mods.galacticraft.asm;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FMUL;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.PUTSTATIC;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;

import micdoodle8.mods.galacticraft.core.GCLog;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IClassTransformer;
import cpw.mods.fml.relauncher.RelaunchClassLoader;

public class GCCoreTransformer implements IClassTransformer
{
	HashMap<String, String> obfuscatedMap = new HashMap<String, String>();
	HashMap<String, String> unObfuscatedMap = new HashMap<String, String>();

	public GCCoreTransformer()
	{
		this.obfuscatedMap.put("confManagerClass", "gu");
		this.unObfuscatedMap.put("confManagerClass", "net/minecraft/server/management/ServerConfigurationManager");
		this.obfuscatedMap.put("createPlayerMethod", "a");
		this.unObfuscatedMap.put("createPlayerMethod", "createPlayerForUser");
		this.obfuscatedMap.put("createPlayerDesc", "(Ljava/lang/String;)Ljc;");
		this.unObfuscatedMap.put("createPlayerDesc", "(Ljava/lang/String;)Lnet/minecraft/entity/player/EntityPlayerMP;");
		this.obfuscatedMap.put("respawnPlayerMethod", "a");
		this.unObfuscatedMap.put("respawnPlayerMethod", "respawnPlayer");
		this.obfuscatedMap.put("respawnPlayerDesc", "(Ljc;IZ)Ljc;");
		this.unObfuscatedMap.put("respawnPlayerDesc", "(Lnet/minecraft/entity/player/EntityPlayerMP;IZ)Lnet/minecraft/entity/player/EntityPlayerMP;");
		this.obfuscatedMap.put("playerMP", "jc");
		this.unObfuscatedMap.put("playerMP", "net/minecraft/entity/player/EntityPlayerMP");

		this.obfuscatedMap.put("attemptLoginMethodBukkit", "attemptLogin");
		this.obfuscatedMap.put("attemptLoginDescBukkit", "(Ljf;Ljava/lang/String;Ljava/lang/String;)Ljc;");
		this.unObfuscatedMap.put("attemptLoginMethodBukkit", "dontfindthis");
		this.unObfuscatedMap.put("attemptLoginDescBukkit", "dontfindthis");

		this.obfuscatedMap.put("playerControllerClass", "bds");
		this.unObfuscatedMap.put("playerControllerClass", "net/minecraft/client/multiplayer/PlayerControllerMP");
		this.obfuscatedMap.put("createClientPlayerMethod", "a");
		this.unObfuscatedMap.put("createClientPlayerMethod", "func_78754_a");
		this.obfuscatedMap.put("createClientPlayerDesc", "(Laab;)Lbdw;");
		this.unObfuscatedMap.put("createClientPlayerDesc", "(Lnet/minecraft/world/World;)Lnet/minecraft/client/entity/EntityClientPlayerMP;");
		this.obfuscatedMap.put("playerClient", "bdw");
		this.unObfuscatedMap.put("playerClient", "net/minecraft/client/entity/EntityClientPlayerMP");

		this.obfuscatedMap.put("entityLivingClass", "ng");
		this.unObfuscatedMap.put("entityLivingClass", "net/minecraft/entity/EntityLiving");
		this.obfuscatedMap.put("moveEntityMethod", "e");
		this.unObfuscatedMap.put("moveEntityMethod", "moveEntityWithHeading");
		this.obfuscatedMap.put("moveEntityDesc", "(FF)V");
		this.unObfuscatedMap.put("moveEntityDesc", "(FF)V");
		this.obfuscatedMap.put("entityLiving", "ng");
		this.unObfuscatedMap.put("entityLiving", "net/minecraft/entity/EntityLiving");

		this.obfuscatedMap.put("entityItemClass", "rh");
		this.unObfuscatedMap.put("entityItemClass", "net/minecraft/entity/item/EntityItem");
		this.obfuscatedMap.put("onUpdateMethod", "l_");
		this.unObfuscatedMap.put("onUpdateMethod", "onUpdate");
		this.obfuscatedMap.put("onUpdateDesc", "()V");
		this.unObfuscatedMap.put("onUpdateDesc", "()V");

		this.obfuscatedMap.put("entityRendererClass", "bfr");
		this.unObfuscatedMap.put("entityRendererClass", "net/minecraft/client/renderer/EntityRenderer");
		this.obfuscatedMap.put("updateLightmapMethod", "h");
		this.unObfuscatedMap.put("updateLightmapMethod", "updateLightmap");
		this.obfuscatedMap.put("updateLightmapDesc", "(F)V");
		this.unObfuscatedMap.put("updateLightmapDesc", "(F)V");
		this.obfuscatedMap.put("worldClass", "aab");
		this.unObfuscatedMap.put("worldClass", "net/minecraft/world/World");
		
		this.obfuscatedMap.put("player", "sq");
		this.unObfuscatedMap.put("player", "net/minecraft/entity/player/EntityPlayer");
		this.obfuscatedMap.put("invPlayer", "bK");
		this.unObfuscatedMap.put("invPlayer", "inventory");
		this.obfuscatedMap.put("containerPlayer", "tz");
		this.unObfuscatedMap.put("containerPlayer", "net/minecraft/inventory/ContainerPlayer");
		this.obfuscatedMap.put("invPlayerClass", "so");
		this.unObfuscatedMap.put("invPlayerClass", "net/minecraft/entity/player/InventoryPlayer");
		
		this.obfuscatedMap.put("minecraft", "net/minecraft/client/Minecraft");
		this.unObfuscatedMap.put("minecraft", "net/minecraft/client/Minecraft");
		this.obfuscatedMap.put("guiPlayer", "azg");
		this.unObfuscatedMap.put("guiPlayer", "net/minecraft/client/gui/inventory/GuiInventory");
		this.obfuscatedMap.put("thePlayer", "g");
		this.unObfuscatedMap.put("thePlayer", "thePlayer");
		this.obfuscatedMap.put("displayGui", "a");
		this.unObfuscatedMap.put("displayGui", "displayGuiScreen");
		this.obfuscatedMap.put("displayGuiDesc", "(Laxr;)V");
		this.unObfuscatedMap.put("displayGuiDesc", "(Lnet/minecraft/src/GuiScreen;)V");
		this.obfuscatedMap.put("runTick", "l");
		this.unObfuscatedMap.put("runTick", "runTick");
		this.obfuscatedMap.put("runTickDesc", "()V");
		this.unObfuscatedMap.put("runTickDesc", "()V");
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("confManagerClass")))
		{
			bytes = this.transform1(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("confManagerClass")))
		{
			bytes = this.transform1(name, bytes, this.obfuscatedMap);
		}

		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("playerControllerClass")))
		{
			bytes = this.transform2(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("playerControllerClass")))
		{
			bytes = this.transform2(name, bytes, this.obfuscatedMap);
		}

		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("entityLivingClass")))
		{
			bytes = this.transform3(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("entityLivingClass")))
		{
			bytes = this.transform3(name, bytes, this.obfuscatedMap);
		}

		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("entityItemClass")))
		{
			bytes = this.transform4(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("entityItemClass")))
		{
			bytes = this.transform4(name, bytes, this.obfuscatedMap);
		}

		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("entityRendererClass")))
		{
			bytes = this.transform5(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("entityRendererClass")))
		{
			bytes = this.transform5(name, bytes, this.obfuscatedMap);
		}
		
		boolean deobfuscated = false;
		
        try
        {
        	@SuppressWarnings("resource")
			URLClassLoader loader = new RelaunchClassLoader(((URLClassLoader) getClass().getClassLoader()).getURLs());
            URL classResource = loader.findResource(String.valueOf("net.minecraft.world.World").replace('.', '/').concat(".class"));
            if (classResource == null)
            {
            	deobfuscated = false;
            }
            else
            {
                deobfuscated = true;
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
        {
        }

		if (deobfuscated && name.replace('.', '/').equals(this.unObfuscatedMap.get("minecraft")))
		{
			bytes = this.transform6(name, bytes, this.unObfuscatedMap);
		}
		else if (!deobfuscated && name.replace('.', '/').equals(this.obfuscatedMap.get("minecraft")))
		{
			bytes = this.transform6(name, bytes, this.obfuscatedMap);
		}

		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("player")))
		{
			bytes = this.transform7(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("player")))
		{
			bytes = this.transform7(name, bytes, this.obfuscatedMap);
		}
		
		if (deobfuscated && name.equals("invtweaks.InvTweaksObfuscation"))
		{
			bytes = this.transform8(name, bytes, unObfuscatedMap);
		}
		else if (!deobfuscated && name.equals("invtweaks.InvTweaksObfuscation"))
		{
			bytes = this.transform8(name, bytes, obfuscatedMap);
		}
		
		if (deobfuscated && name.equals("invtweaks.InvTweaksContainerManager"))
		{
			bytes = this.transform9(name, bytes, unObfuscatedMap);
		}
		else if (!deobfuscated && name.equals("invtweaks.InvTweaksContainerManager"))
		{
			bytes = this.transform9(name, bytes, obfuscatedMap);
		}
		
		if (deobfuscated && name.equals("codechicken.nei.NEICPH"))
		{
			bytes = this.transform10(name, bytes, unObfuscatedMap);
		}
		else if (!deobfuscated && name.equals("codechicken.nei.NEICPH"))
		{
			bytes = this.transform10(name, bytes, obfuscatedMap);
		}
		
		if (deobfuscated && name.equals("codechicken.nei.ContainerCreativeInv"))
		{
			bytes = this.transform11(name, bytes, unObfuscatedMap);
		}
		else if (!deobfuscated && name.equals("codechicken.nei.ContainerCreativeInv"))
		{
			bytes = this.transform11(name, bytes, obfuscatedMap);
		}
		
		if (deobfuscated && name.equals("mithion.arsmagica.guis.GuiIngameArsMagica"))
		{
			bytes = this.transform12(name, bytes, unObfuscatedMap);
		}
		else if (!deobfuscated && name.equals("mithion.arsmagica.guis.GuiIngameArsMagica"))
		{
			bytes = this.transform12(name, bytes, obfuscatedMap);
		}
		
		bytes = this.transform13(name, bytes, obfuscatedMap);

		return bytes;
	}

	/**
	 *  replaces EntityPlayerMP initialization with GCCorePlayerMP ones
	 */
    public byte[] transform1(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		final Iterator<MethodNode> methods = node.methods.iterator();

		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals(map.get("createPlayerMethod")) && methodnode.desc.equals(map.get("createPlayerDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);

	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.getOpcode() != Opcodes.CHECKCAST && nodeAt.desc.contains(map.get("playerMP")))
	            		{
	            			final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP");

	            			methodnode.instructions.set(nodeAt, overwriteNode);

	            			GCLog.info("Successfully set type insertion node with description \"NEW " + map.get("playerMP") + "\" to \"NEW micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.owner.contains(map.get("playerMP")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));

	            			GCLog.info("Successfully set method insertion node with owner \"" + map.get("playerMP") + "\" to \"micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            }
			}

			if (methodnode.name.equals(map.get("respawnPlayerMethod")) && methodnode.desc.equals(map.get("respawnPlayerDesc")))
			{
				for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);

	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.getOpcode() != Opcodes.CHECKCAST && nodeAt.desc.contains(map.get("playerMP")))
	            		{
	            			final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP");

	            			methodnode.instructions.set(nodeAt, overwriteNode);

	            			GCLog.info("Successfully set type insertion node with description \"NEW " + map.get("playerMP") + "\" to \"NEW micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerMP")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));

	            			GCLog.info("Successfully set method insertion node with owner \"" + map.get("playerMP") + "\" to \"micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            }
			}

			if (methodnode.name.equals(map.get("attemptLoginMethodBukkit")) && methodnode.desc.equals(map.get("attemptLoginDescBukkit")))
			{
				for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);

	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.NEW && nodeAt.desc.contains(map.get("playerMP")))
	            		{
	            			final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP");

	            			methodnode.instructions.set(nodeAt, overwriteNode);

	            			GCLog.info("[BUKKIT] Successfully set type insertion node with description \"NEW " + map.get("playerMP") + "\" to \"NEW micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerMP")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Laab;Ljava/lang/String;Ljd;)V"));

	            			GCLog.info("[BUKKIT] Successfully set method insertion node with owner \"" + map.get("playerMP") + "\" to \"micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }

    public byte[] transform2(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		final Iterator<MethodNode> methods = node.methods.iterator();

		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals(map.get("createClientPlayerMethod")) && methodnode.desc.equals(map.get("createClientPlayerDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);

	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.desc.contains(map.get("playerClient")))
	            		{
	            			final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP");

	            			methodnode.instructions.set(nodeAt, overwriteNode);

	            			GCLog.info("Successfully set NEW type insertion node with description \"NEW " + map.get("playerClient") + "\" to \"NEW micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP\"");
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerClient")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP", "<init>", "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/world/World;Lnet/minecraft/util/Session;Lnet/minecraft/client/multiplayer/NetClientHandler;)V"));

	            			GCLog.info("Successfully set INVOKESPECIAL method insertion node with owner \"" + map.get("playerClient") + "\" to \"micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP\"");
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }

    public byte[] transform3(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		final Iterator<MethodNode> methods = node.methods.iterator();

		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals(map.get("moveEntityMethod")) && methodnode.desc.equals(map.get("moveEntityDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);

	            	if (list instanceof LdcInsnNode)
	            	{
	            		final LdcInsnNode nodeAt = (LdcInsnNode) list;

	            		if (nodeAt.cst.equals(Double.valueOf(0.08D)))
	            		{
	            			final VarInsnNode beforeNode = new VarInsnNode(Opcodes.ALOAD, 0);
	            			final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getGravityForEntity", "(L" + map.get("entityLiving") + ";)D");

	            			methodnode.instructions.insertBefore(nodeAt, beforeNode);
	            			methodnode.instructions.set(nodeAt, overwriteNode);

	            			GCLog.info("Successfully set INVOKESTATIC type insertion node with name \"micdoodle8/mods/galacticraft/core/util/WorldUtil\"");
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }

    public byte[] transform4(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		final Iterator<MethodNode> methods = node.methods.iterator();

		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals(map.get("onUpdateMethod")) && methodnode.desc.equals(map.get("onUpdateDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);

	            	if (list instanceof LdcInsnNode)
	            	{
	            		final LdcInsnNode nodeAt = (LdcInsnNode) list;

	            		if (nodeAt.cst.equals(Double.valueOf(0.03999999910593033D)))
	            		{
	            			final VarInsnNode beforeNode = new VarInsnNode(Opcodes.ALOAD, 0);
	            			final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getItemGravity", "(L" + map.get("entityItemClass") + ";)D");

	            			methodnode.instructions.insertBefore(nodeAt, beforeNode);
	            			methodnode.instructions.set(nodeAt, overwriteNode);

	            			GCLog.info("Successfully set INVOKESTATIC type insertion node with name \"micdoodle8/mods/galacticraft/core/util/WorldUtil\"");
	            		}

	            		if (nodeAt.cst.equals(Double.valueOf(0.9800000190734863D)))
	            		{
	            			final VarInsnNode beforeNode = new VarInsnNode(Opcodes.ALOAD, 0);
	            			final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getItemGravity2", "(L" + map.get("entityItemClass") + ";)D");

	            			methodnode.instructions.insertBefore(nodeAt, beforeNode);
	            			methodnode.instructions.set(nodeAt, overwriteNode);

	            			GCLog.info("Successfully set INVOKESTATIC type insertion node with name \"micdoodle8/mods/galacticraft/core/util/WorldUtil\"");
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }

    public byte[] transform5(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		final Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals(map.get("updateLightmapMethod")) && methodnode.desc.equals(map.get("updateLightmapDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);

	            	if (list instanceof IntInsnNode)
	            	{
	            		final IntInsnNode nodeAt = (IntInsnNode) list;
	            		
	            		if (nodeAt.operand == 255)
	            		{
	            			InsnList nodesToAdd = new InsnList();

	            			nodesToAdd.add(new VarInsnNode(FLOAD, 11));
	            			nodesToAdd.add(new VarInsnNode(ALOAD, 2));
	            			nodesToAdd.add(new MethodInsnNode(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getColorRed", "(L" + map.get("worldClass") + ";)F"));
	            			nodesToAdd.add(new InsnNode(FMUL));
	            			nodesToAdd.add(new VarInsnNode(FSTORE, 11));

	            			nodesToAdd.add(new VarInsnNode(FLOAD, 12));
	            			nodesToAdd.add(new VarInsnNode(ALOAD, 2));
	            			nodesToAdd.add(new MethodInsnNode(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getColorGreen", "(L" + map.get("worldClass") + ";)F"));
	            			nodesToAdd.add(new InsnNode(FMUL));
	            			nodesToAdd.add(new VarInsnNode(FSTORE, 12));

	            			nodesToAdd.add(new VarInsnNode(FLOAD, 13));
	            			nodesToAdd.add(new VarInsnNode(ALOAD, 2));
	            			nodesToAdd.add(new MethodInsnNode(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getColorBlue", "(L" + map.get("worldClass") + ";)F"));
	            			nodesToAdd.add(new InsnNode(FMUL));
	            			nodesToAdd.add(new VarInsnNode(FSTORE, 13));
	            			
	            			methodnode.instructions.insertBefore(nodeAt, nodesToAdd);
	            			break;
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }

    public byte[] transform6(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals(map.get("runTick")) && methodnode.desc.equals(map.get("runTickDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.NEW && nodeAt.desc.equals(map.get("guiPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory"));
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("guiPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory", "<init>", "(L" + map.get("player") + ";)V"));
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }

    public byte[] transform7(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals("<init>"))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.NEW && nodeAt.desc.equals(map.get("containerPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/inventory/GCCoreContainerPlayer"));
	            		}
	            		else if (nodeAt.getOpcode() == Opcodes.NEW && nodeAt.desc.equals(map.get("invPlayerClass")))
	            		{
	            			methodnode.instructions.set(nodeAt, new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/inventory/GCCoreInventoryPlayer"));
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("containerPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/inventory/GCCoreContainerPlayer", "<init>", "(L" + map.get("invPlayerClass") + ";ZL" + map.get("player") + ";)V"));
	            		}
	            		else if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("invPlayerClass")))
	            		{
	        				methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/inventory/GCCoreInventoryPlayer", "<init>", "(L" + map.get("player") + ";)V"));
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
    
    public byte[] transform8(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals("<init>"))
			{
        		InsnList toAdd = new InsnList();
        		
        		toAdd.add(new IntInsnNode(BIPUSH, 51));
        		toAdd.add(new FieldInsnNode(PUTSTATIC, "invtweaks/InvTweaksObfuscation", "CREATIVE_MAIN_INVENTORY_SIZE", "I"));
        		methodnode.instructions.insertBefore(methodnode.instructions.getFirst(), toAdd);
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
    
    public byte[] transform9(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();

		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals("<init>"))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof IincInsnNode)
	            	{
	            		final IincInsnNode nodeAt = (IincInsnNode) list;

	            		if (nodeAt.incr == -1)
	            		{
	            			methodnode.instructions.set(nodeAt, new IincInsnNode(nodeAt.var, -6));
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
    
    public byte[] transform10(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals("handlePacket"))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.NEW && nodeAt.desc.equals(map.get("guiPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory"));
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("guiPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory", "<init>", "(L" + map.get("player") + ";)V"));
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
    
    public byte[] transform11(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals("<init>"))
			{
	            for (int count = 1; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);
	            	final AbstractInsnNode prevList = methodnode.instructions.get(count - 1);
	            	
	            	if (list instanceof InsnNode && prevList instanceof MethodInsnNode)
	            	{
	            		InsnNode insn = (InsnNode) list;
	            		
	            		if (insn.getOpcode() == Opcodes.ICONST_1)
	            		{
	            			methodnode.instructions.set(insn, new IntInsnNode(Opcodes.BIPUSH, 6));
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
    
    public byte[] transform12(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		String manaStr = "Current Mana: ";
		
		int wOffset = 0;
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals("renderManaAmount"))
			{
	            for (int count = 0; count < methodnode.instructions.size() - 1; count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);
	            	final AbstractInsnNode prevList = methodnode.instructions.get(count + 1);
	            	
	            	if (list instanceof LdcInsnNode)
	            	{
	            		LdcInsnNode insn = (LdcInsnNode) list;
	            		
	            		if (insn.cst.equals("Current Mana: "))
	            		{
	            			methodnode.instructions.set(insn, new LdcInsnNode("Mana:"));
	            		}
	            	}
	            	else if (list instanceof InsnNode && prevList instanceof VarInsnNode)
	            	{
	            		InsnNode insn = (InsnNode) list;
	            		VarInsnNode insn2 = (VarInsnNode) prevList;
	            		
	            		if (insn.getOpcode() == Opcodes.ICONST_0 && insn2.getOpcode() == Opcodes.ISTORE)
	            		{
	            			methodnode.instructions.set(insn, new IntInsnNode(BIPUSH, 80));
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
    
    public byte[] transform13(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			final MethodNode methodnode = methods.next();

			if (methodnode.name.equals("openInventoryGui"))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	final AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof TypeInsnNode)
	            	{
	            		final TypeInsnNode nodeAt = (TypeInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.NEW && nodeAt.desc.equals(map.get("guiPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory"));
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		final MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("guiPlayer")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory", "<init>", "(L" + map.get("player") + ";)V"));
	            		}
	            	}
	            }
			}
		}

        final ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
}
