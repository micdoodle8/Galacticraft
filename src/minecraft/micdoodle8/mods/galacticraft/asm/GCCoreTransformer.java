package micdoodle8.mods.galacticraft.asm;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IClassTransformer;

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

		this.obfuscatedMap.put("playerControllerClass", "bds");
		this.unObfuscatedMap.put("playerControllerClass", "net/minecraft/client/multiplayer/PlayerControllerMP");
		this.obfuscatedMap.put("createClientPlayerMethod", "a");
		this.unObfuscatedMap.put("createClientPlayerMethod", "func_78754_a");
		this.obfuscatedMap.put("createClientPlayerDesc", "(Laab;)Lbdw;");
		this.unObfuscatedMap.put("createClientPlayerDesc", "(Lnet/minecraft/world/World;)Lnet/minecraft/client/entity/EntityClientPlayerMP;");
		this.obfuscatedMap.put("playerClient", "bdw");
		this.unObfuscatedMap.put("playerClient", "net/minecraft/client/entity/EntityClientPlayerMP");
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) 
	{
		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("confManagerClass")))
		{
			bytes = transform1(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("confManagerClass")))
		{
			bytes = transform1(name, bytes, this.obfuscatedMap);
		}

		if (name.replace('.', '/').equals(this.unObfuscatedMap.get("playerControllerClass")))
		{
			bytes = transform2(name, bytes, this.unObfuscatedMap);
		}
		else if (name.replace('.', '/').equals(this.obfuscatedMap.get("playerControllerClass")))
		{
			bytes = transform2(name, bytes, this.obfuscatedMap);
		}
		
		return bytes;
	}

	/**
	 *  replaces EntityPlayerMP initialization with GCCorePlayerMP ones
	 */
    public byte[] transform1(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			MethodNode methodnode = methods.next();
			
			if (methodnode.name.equals(map.get("createPlayerMethod")) && methodnode.desc.equals(map.get("createPlayerDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof TypeInsnNode)
	            	{
	            		TypeInsnNode nodeAt = (TypeInsnNode) list;
	            		
	            		if (nodeAt.getOpcode() != CHECKCAST && nodeAt.desc.contains(map.get("playerMP")))
	            		{
	            			TypeInsnNode overwriteNode = new TypeInsnNode(NEW, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP");
	            			
	            			methodnode.instructions.set(nodeAt, overwriteNode);
	            			
	            			FMLLog.info("Successfully set type insertion node with description \"NEW " + map.get("playerMP") + "\" to \"NEW micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		MethodInsnNode nodeAt = (MethodInsnNode) list;
	            		
	            		if (nodeAt.owner.contains(map.get("playerMP")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));

	            			FMLLog.info("Successfully set method insertion node with owner \"" + map.get("playerMP") + "\" to \"micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            }
			}

			if (methodnode.name.equals(map.get("respawnPlayerMethod")) && methodnode.desc.equals(map.get("respawnPlayerDesc")))
			{
				for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof TypeInsnNode)
	            	{
	            		TypeInsnNode nodeAt = (TypeInsnNode) list;
	            		
	            		if (nodeAt.getOpcode() != CHECKCAST && nodeAt.desc.contains(map.get("playerMP")))
	            		{
	            			TypeInsnNode overwriteNode = new TypeInsnNode(NEW, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP");
	            			
	            			methodnode.instructions.set(nodeAt, overwriteNode);
	            			
	            			FMLLog.info("Successfully set type insertion node with description \"NEW " + map.get("playerMP") + "\" to \"NEW micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		MethodInsnNode nodeAt = (MethodInsnNode) list;
	            		
	            		if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerMP")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));

	            			FMLLog.info("Successfully set method insertion node with owner \"" + map.get("playerMP") + "\" to \"micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP\" in method: " + methodnode.name);
	            		}
	            	}
	            }
			}
		}
    
        ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();
    
        return bytes;
    }

    public byte[] transform2(String name, byte[] bytes, HashMap<String, String> map)
    {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

		Iterator<MethodNode> methods = node.methods.iterator();
		
		while (methods.hasNext())
		{
			MethodNode methodnode = methods.next();
			
			if (methodnode.name.equals(map.get("createClientPlayerMethod")) && methodnode.desc.equals(map.get("createClientPlayerDesc")))
			{
	            for (int count = 0; count < methodnode.instructions.size(); count++)
	            {
	            	AbstractInsnNode list = methodnode.instructions.get(count);
	            	
	            	if (list instanceof TypeInsnNode)
	            	{
	            		TypeInsnNode nodeAt = (TypeInsnNode) list;
	            		
	            		if (nodeAt.desc.contains(map.get("playerClient")))
	            		{
	            			TypeInsnNode overwriteNode = new TypeInsnNode(NEW, "micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP");
	            			
	            			methodnode.instructions.set(nodeAt, overwriteNode);
	            			
	            			FMLLog.info("Successfully set NEW type insertion node with description \"NEW " + map.get("playerClient") + "\" to \"NEW micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP\"");
	            		}
	            	}
	            	else if (list instanceof MethodInsnNode)
	            	{
	            		MethodInsnNode nodeAt = (MethodInsnNode) list;

	            		if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerClient")))
	            		{
	            			methodnode.instructions.set(nodeAt, new MethodInsnNode(INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP", "<init>", "(Lnet/minecraft/client/Minecraft;Lnet/minecraft/world/World;Lnet/minecraft/util/Session;Lnet/minecraft/client/multiplayer/NetClientHandler;)V"));

	            			FMLLog.info("Successfully set INVOKESPECIAL method insertion node with owner \"" + map.get("playerClient") + "\" to \"micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP\"");
	            		}
	            	}
	            }
			}
		}
    
        ClassWriter writer = new ClassWriter(COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();
    
        return bytes;
    }
}
