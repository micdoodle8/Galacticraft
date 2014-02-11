package micdoodle8.mods.miccore;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.FMLInjectionData;

public class MicdoodleTransformer implements net.minecraft.launchwrapper.IClassTransformer
{
    HashMap<String, String> nodemap = new HashMap<String, String>();
    private boolean deobfuscated = true;
    private boolean optifinePresent;

    @SuppressWarnings("resource")
	public MicdoodleTransformer()
    {
        try
        {
            final URLClassLoader loader = new LaunchClassLoader(((URLClassLoader) this.getClass().getClassLoader()).getURLs());
            URL classResource = loader.findResource(String.valueOf("net.minecraft.world.World").replace('.', '/').concat(".class"));
            if (classResource == null)
            {
                this.deobfuscated = false;
            }
            else
            {
                this.deobfuscated = true;
            }

            classResource = loader.findResource(String.valueOf("CustomColorizer").replace('.', '/').concat(".class"));
            if (classResource == null)
            {
                this.optifinePresent = false;
            }
            else
            {
                this.optifinePresent = true;
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

        if (this.deobfuscated)
        {
            this.nodemap.put("respawnPlayerMethod", "respawnPlayer");
            this.nodemap.put("worldClass", "net/minecraft/world/World");
            this.nodemap.put("playerMP", "net/minecraft/entity/player/EntityPlayerMP");
            this.nodemap.put("netLoginHandler", "net/minecraft/network/NetLoginHandler");
            this.nodemap.put("confManagerClass", "net/minecraft/server/management/ServerConfigurationManager");
            this.nodemap.put("createPlayerMethod", "createPlayerForUser");
            this.nodemap.put("gameProfileClass", "com/mojang/authlib/GameProfile");
            // CHANGED:
            this.nodemap.put("createPlayerDesc", "(L" + this.nodemap.get("gameProfileClass") + ";)L" + this.nodemap.get("playerMP") + ";");
            this.nodemap.put("respawnPlayerDesc", "(L" + this.nodemap.get("playerMP") + ";IZ)L" + this.nodemap.get("playerMP") + ";");
            this.nodemap.put("itemInWorldManagerClass", "net/minecraft/src/ItemInWorldManager");

            this.nodemap.put("attemptLoginMethodBukkit", "");
            this.nodemap.put("attemptLoginDescBukkit", "");

            this.nodemap.put("playerControllerClass", "net/minecraft/client/multiplayer/PlayerControllerMP");
            this.nodemap.put("playerClient", "net/minecraft/client/entity/EntityClientPlayerMP");
            this.nodemap.put("netClientHandler", "net/minecraft/client/multiplayer/NetClientHandler");
            this.nodemap.put("createClientPlayerMethod", "func_78754_a");
            this.nodemap.put("createClientPlayerDesc", "(L" + this.nodemap.get("worldClass") + ";)L" + this.nodemap.get("playerClient") + ";");

            this.nodemap.put("entityLivingClass", "net/minecraft/entity/EntityLivingBase");
            this.nodemap.put("moveEntityMethod", "moveEntityWithHeading");
            this.nodemap.put("moveEntityDesc", "(FF)V");

            this.nodemap.put("entityItemClass", "net/minecraft/entity/item/EntityItem");
            this.nodemap.put("onUpdateMethod", "onUpdate");
            this.nodemap.put("onUpdateDesc", "()V");

            this.nodemap.put("entityRendererClass", "net/minecraft/client/renderer/EntityRenderer");
            this.nodemap.put("updateLightmapMethod", "updateLightmap");
            this.nodemap.put("updateLightmapDesc", "(F)V");

            this.nodemap.put("player", "net/minecraft/entity/player/EntityPlayer");
            this.nodemap.put("containerPlayer", "net/minecraft/inventory/ContainerPlayer");
            this.nodemap.put("invPlayerClass", "net/minecraft/entity/player/InventoryPlayer");

            this.nodemap.put("minecraft", "net/minecraft/client/Minecraft");
            this.nodemap.put("session", "net/minecraft/util/Session");
            this.nodemap.put("guiPlayer", "net/minecraft/client/gui/inventory/GuiInventory");
            this.nodemap.put("thePlayer", "thePlayer");
            this.nodemap.put("displayGui", "displayGuiScreen");
            this.nodemap.put("guiScreen", "net/minecraft/src/GuiScreen");
            this.nodemap.put("displayGuiDesc", "(L" + this.nodemap.get("guiScreen") + ";)V");
            this.nodemap.put("runTick", "runTick");
            this.nodemap.put("runTickDesc", "()V");
            this.nodemap.put("clickMiddleMouseButton", "clickMiddleMouseButton");
            this.nodemap.put("clickMiddleMouseButtonDesc", "()V");

            this.nodemap.put("itemRendererClass", "net/minecraft/client/renderer/ItemRenderer");
            this.nodemap.put("renderOverlaysMethod", "renderOverlays");
            this.nodemap.put("renderOverlaysDesc", "(F)V");

            this.nodemap.put("updateFogColorMethod", "updateFogColor");
            this.nodemap.put("updateFogColorDesc", "(F)V");
            this.nodemap.put("getFogColorMethod", "getFogColor");
            this.nodemap.put("getSkyColorMethod", "getSkyColor");
            this.nodemap.put("vecClass", "net/minecraft/util/Vec3");
            this.nodemap.put("entityClass", "net/minecraft/entity/Entity");
            this.nodemap.put("getFogColorDesc", "(F)L" + this.nodemap.get("vecClass") + ";");
            this.nodemap.put("getSkyColorDesc", "(L" + this.nodemap.get("entityClass") + ";F)L" + this.nodemap.get("vecClass") + ";");

            this.nodemap.put("guiSleepClass", "net/minecraft/client/gui/GuiSleepMP");
            this.nodemap.put("wakeEntityMethod", "wakeEntity");
            this.nodemap.put("wakeEntityDesc", "()V");

            this.nodemap.put("orientCameraDesc", "(L" + this.nodemap.get("minecraft") + ";L" + this.nodemap.get("entityLivingClass") + ";)V");
            
            this.nodemap.put("blockClass", "net/minecraft/block/Block");
            this.nodemap.put("breakBlockMethod", "breakBlock");
            this.nodemap.put("breakBlockDesc", "(L" + this.nodemap.get("worldClass") + ";IIIII)V");
        }
        else
        {
            final String mcVersion = (String) FMLInjectionData.data()[4];

            if (VersionParser.parseRange("[1.6.4]").containsVersion(new DefaultArtifactVersion(mcVersion)))
            {
                this.nodemap.put("worldClass", "abw");

                this.nodemap.put("playerMP", "jv");
                this.nodemap.put("netLoginHandler", "jy");
                this.nodemap.put("confManagerClass", "hn");
                this.nodemap.put("createPlayerMethod", "a");
                this.nodemap.put("createPlayerDesc", "(Ljava/lang/String;)L" + this.nodemap.get("playerMP") + ";");
                this.nodemap.put("respawnPlayerMethod", "a");
                this.nodemap.put("respawnPlayerDesc", "(L" + this.nodemap.get("playerMP") + ";IZ)L" + this.nodemap.get("playerMP") + ";");
                this.nodemap.put("itemInWorldManagerClass", "jw");

                this.nodemap.put("attemptLoginMethodBukkit", "attemptLogin");
                this.nodemap.put("attemptLoginDescBukkit", "(L" + this.nodemap.get("netLoginHandler") + ";Ljava/lang/String;Ljava/lang/String;)L" + this.nodemap.get("playerMP") + ";");

                this.nodemap.put("playerControllerClass", "bdc");
                this.nodemap.put("playerClient", "bdi");
                this.nodemap.put("netClientHandler", "bcw");
                this.nodemap.put("createClientPlayerMethod", "a");
                this.nodemap.put("createClientPlayerDesc", "(L" + this.nodemap.get("worldClass") + ";)L" + this.nodemap.get("playerClient") + ";");

                this.nodemap.put("entityLivingClass", "of");
                this.nodemap.put("moveEntityMethod", "e");
                this.nodemap.put("moveEntityDesc", "(FF)V");

                this.nodemap.put("entityItemClass", "ss");
                this.nodemap.put("onUpdateMethod", "l_");
                this.nodemap.put("onUpdateDesc", "()V");

                this.nodemap.put("entityRendererClass", "bfe");
                this.nodemap.put("updateLightmapMethod", "h");
                this.nodemap.put("updateLightmapDesc", "(F)V");

                this.nodemap.put("player", "uf");
                this.nodemap.put("containerPlayer", "vv");
                this.nodemap.put("invPlayerClass", "ud");

                this.nodemap.put("minecraft", "atv");
                this.nodemap.put("session", "aus");
                this.nodemap.put("guiPlayer", "axv");
                this.nodemap.put("thePlayer", "h");
                this.nodemap.put("displayGui", "a");
                this.nodemap.put("guiScreen", "awe");
                this.nodemap.put("displayGuiDesc", "(L" + this.nodemap.get("guiScreen") + ";)V");
                this.nodemap.put("runTick", "k");
                this.nodemap.put("runTickDesc", "()V");
                this.nodemap.put("clickMiddleMouseButton", "W");
                this.nodemap.put("clickMiddleMouseButtonDesc", "()V");

                this.nodemap.put("itemRendererClass", "bfj");
                this.nodemap.put("renderOverlaysMethod", "b");
                this.nodemap.put("renderOverlaysDesc", "(F)V");

                this.nodemap.put("updateFogColorMethod", "i");
                this.nodemap.put("updateFogColorDesc", "(F)V");
                this.nodemap.put("getFogColorMethod", "f");
                this.nodemap.put("getSkyColorMethod", "a");
                this.nodemap.put("vecClass", "atc");
                this.nodemap.put("entityClass", "nn");
                this.nodemap.put("getFogColorDesc", "(F)L" + this.nodemap.get("vecClass") + ";");
                this.nodemap.put("getSkyColorDesc", "(L" + this.nodemap.get("entityClass") + ";F)L" + this.nodemap.get("vecClass") + ";");

                this.nodemap.put("guiSleepClass", "avm");
                this.nodemap.put("wakeEntityMethod", "g");
                this.nodemap.put("wakeEntityDesc", "()V");

                this.nodemap.put("orientCameraDesc", "(L" + this.nodemap.get("minecraft") + ";L" + this.nodemap.get("entityLivingClass") + ";)V");
            }
            else if (VersionParser.parseRange("[1.6.2]").containsVersion(new DefaultArtifactVersion(mcVersion)))
            {
                this.nodemap.put("worldClass", "abv");

                this.nodemap.put("playerMP", "ju");
                this.nodemap.put("netLoginHandler", "jx");
                this.nodemap.put("confManagerClass", "hm");
                this.nodemap.put("createPlayerMethod", "a");
                this.nodemap.put("createPlayerDesc", "(Ljava/lang/String;)L" + this.nodemap.get("playerMP") + ";");
                this.nodemap.put("respawnPlayerMethod", "a");
                this.nodemap.put("respawnPlayerDesc", "(L" + this.nodemap.get("playerMP") + ";IZ)L" + this.nodemap.get("playerMP") + ";");
                this.nodemap.put("itemInWorldManagerClass", "jv");

                this.nodemap.put("attemptLoginMethodBukkit", "attemptLogin");
                this.nodemap.put("attemptLoginDescBukkit", "(L" + this.nodemap.get("netLoginHandler") + ";Ljava/lang/String;Ljava/lang/String;)L" + this.nodemap.get("playerMP") + ";");

                this.nodemap.put("playerControllerClass", "bcz");
                this.nodemap.put("playerClient", "bdf");
                this.nodemap.put("netClientHandler", "bct");
                this.nodemap.put("createClientPlayerMethod", "a");
                this.nodemap.put("createClientPlayerDesc", "(L" + this.nodemap.get("worldClass") + ";)L" + this.nodemap.get("playerClient") + ";");

                this.nodemap.put("entityLivingClass", "oe");
                this.nodemap.put("moveEntityMethod", "e");
                this.nodemap.put("moveEntityDesc", "(FF)V");

                this.nodemap.put("entityItemClass", "sr");
                this.nodemap.put("onUpdateMethod", "l_");
                this.nodemap.put("onUpdateDesc", "()V");

                this.nodemap.put("entityRendererClass", "bfb");
                this.nodemap.put("updateLightmapMethod", "h");
                this.nodemap.put("updateLightmapDesc", "(F)V");

                this.nodemap.put("player", "ue");
                this.nodemap.put("containerPlayer", "vu");
                this.nodemap.put("invPlayerClass", "uc");

                this.nodemap.put("minecraft", "ats");
                this.nodemap.put("session", "aup");
                this.nodemap.put("guiPlayer", "axs");
                this.nodemap.put("thePlayer", "g");
                this.nodemap.put("displayGui", "a");
                this.nodemap.put("guiScreen", "avv");
                this.nodemap.put("displayGuiDesc", "(L" + this.nodemap.get("guiScreen") + ";)V");
                this.nodemap.put("runTick", "k");
                this.nodemap.put("runTickDesc", "()V");
                this.nodemap.put("clickMiddleMouseButton", "W");
                this.nodemap.put("clickMiddleMouseButtonDesc", "()V");

                this.nodemap.put("itemRendererClass", "bfg");
                this.nodemap.put("renderOverlaysMethod", "b");
                this.nodemap.put("renderOverlaysDesc", "(F)V");

                this.nodemap.put("updateFogColorMethod", "i");
                this.nodemap.put("updateFogColorDesc", "(F)V");
                this.nodemap.put("getFogColorMethod", "f");
                this.nodemap.put("getSkyColorMethod", "a");
                this.nodemap.put("vecClass", "asz");
                this.nodemap.put("entityClass", "nm");
                this.nodemap.put("getFogColorDesc", "(F)L" + this.nodemap.get("vecClass") + ";");
                this.nodemap.put("getSkyColorDesc", "(L" + this.nodemap.get("entityClass") + ";F)L" + this.nodemap.get("vecClass") + ";");

                this.nodemap.put("guiSleepClass", "avj");
                this.nodemap.put("wakeEntityMethod", "g");
                this.nodemap.put("wakeEntityDesc", "()V");

                this.nodemap.put("orientCameraDesc", "(L" + this.nodemap.get("minecraft") + ";L" + this.nodemap.get("entityLivingClass") + ";)V");
            }
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (name.replace('.', '/').equals(this.nodemap.get("confManagerClass")))
        {
            bytes = this.transform1(name, bytes, this.nodemap);
        }
        else if (name.replace('.', '/').equals(this.nodemap.get("playerControllerClass")))
        {
            bytes = this.transform2(name, bytes, this.nodemap);
        }
        else if (name.replace('.', '/').equals(this.nodemap.get("entityLivingClass")))
        {
            bytes = this.transform3(name, bytes, this.nodemap);
        }
        else if (name.replace('.', '/').equals(this.nodemap.get("entityItemClass")))
        {
            bytes = this.transform4(name, bytes, this.nodemap);
        }
        else if (name.replace('.', '/').equals(this.nodemap.get("entityRendererClass")))
        {
            bytes = this.transform5(name, bytes, this.nodemap);
        }
        else if (name.replace('.', '/').equals(this.nodemap.get("itemRendererClass")))
        {
            bytes = this.transform14(name, bytes, this.nodemap);
        }
        else if (name.replace('.', '/').equals(this.nodemap.get("guiSleepClass")))
        {
            bytes = this.transform7(name, bytes, this.nodemap);
        }
        else if (name.equals("net.minecraftforge.client.ForgeHooksClient"))
        {
            bytes = this.transform8(name, bytes, this.nodemap);
        }
        
        if (name.contains("galacticraft"))
        {
            bytes = this.transform9(name, bytes, this.nodemap);
        }

        return bytes;
    }

    /**
     * replaces EntityPlayerMP initialization with GCCorePlayerMP ones
     */
    public byte[] transform1(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 6;
        int injectionCount = 0;

        final Iterator<MethodNode> methods = node.methods.iterator();

        while (methods.hasNext())
        {
            final MethodNode methodnode = methods.next();

        	FMLLog.info("" + methodnode.name + " " + map.get("createPlayerMethod") + " " + methodnode.desc + " " + map.get("createPlayerDesc"));
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
                            final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerMP");

                            methodnode.instructions.set(nodeAt, overwriteNode);
                            System.out.println("1");
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.owner.contains(map.get("playerMP")) && nodeAt.getOpcode() == Opcodes.INVOKESPECIAL)
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));
                            System.out.println("2");
                            injectionCount++;
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
                            final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerMP");

                            methodnode.instructions.set(nodeAt, overwriteNode);
                            System.out.println("3");
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerMP")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));

                            System.out.println("4");
                            injectionCount++;
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
                            final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerMP");

                            methodnode.instructions.set(nodeAt, overwriteNode);
                            System.out.println("5");
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerMP")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;L" + map.get("worldClass") + ";Ljava/lang/String;L" + map.get("itemInWorldManagerClass") + ";)V"));

                            System.out.println("6");
                            injectionCount++;
                        }
                    }
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    public byte[] transform2(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 2;
        int injectionCount = 0;

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
                            final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerSP");

                            methodnode.instructions.set(nodeAt, overwriteNode);
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerClient")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/player/GCCorePlayerSP", "<init>", "(L" + map.get("minecraft") + ";L" + map.get("worldClass") + ";L" + map.get("session") + ";L" + map.get("netClientHandler") + ";)V"));
                            injectionCount++;
                        }
                    }
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    public byte[] transform3(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 1;
        int injectionCount = 0;

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
                            final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getGravityForEntity", "(L" + map.get("entityLivingClass") + ";)D");

                            methodnode.instructions.insertBefore(nodeAt, beforeNode);
                            methodnode.instructions.set(nodeAt, overwriteNode);
                            injectionCount++;
                        }
                    }
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    public byte[] transform4(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 2;
        int injectionCount = 0;

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
                            injectionCount++;
                        }

                        if (nodeAt.cst.equals(Double.valueOf(0.9800000190734863D)))
                        {
                            final VarInsnNode beforeNode = new VarInsnNode(Opcodes.ALOAD, 0);
                            final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getItemGravity2", "(L" + map.get("entityItemClass") + ";)D");

                            methodnode.instructions.insertBefore(nodeAt, beforeNode);
                            methodnode.instructions.set(nodeAt, overwriteNode);
                            injectionCount++;
                        }
                    }
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    public byte[] transform5(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 3;
        int injectionCount = 0;

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
                            final InsnList nodesToAdd = new InsnList();

                            nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 11));
                            nodesToAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getColorRed", "(L" + map.get("worldClass") + ";)F"));
                            nodesToAdd.add(new InsnNode(Opcodes.FMUL));
                            nodesToAdd.add(new VarInsnNode(Opcodes.FSTORE, 11));

                            nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 12));
                            nodesToAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getColorGreen", "(L" + map.get("worldClass") + ";)F"));
                            nodesToAdd.add(new InsnNode(Opcodes.FMUL));
                            nodesToAdd.add(new VarInsnNode(Opcodes.FSTORE, 12));

                            nodesToAdd.add(new VarInsnNode(Opcodes.FLOAD, 13));
                            nodesToAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            nodesToAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getColorBlue", "(L" + map.get("worldClass") + ";)F"));
                            nodesToAdd.add(new InsnNode(Opcodes.FMUL));
                            nodesToAdd.add(new VarInsnNode(Opcodes.FSTORE, 13));

                            methodnode.instructions.insertBefore(nodeAt, nodesToAdd);
                            injectionCount++;
                            break;
                        }
                    }
                }
            }
            else if (methodnode.name.equals(map.get("updateFogColorMethod")) && methodnode.desc.equals(map.get("updateFogColorDesc")))
            {
                for (int count = 0; count < methodnode.instructions.size(); count++)
                {
                    final AbstractInsnNode list = methodnode.instructions.get(count);

                    if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (!this.optifinePresent && nodeAt.name.equals(map.get("getFogColorMethod")) && nodeAt.desc.equals(map.get("getFogColorDesc")))
                        {
                            InsnList toAdd = new InsnList();

                            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getFogColorHook", "(L" + map.get("worldClass") + ";)L" + map.get("vecClass") + ";"));
                            toAdd.add(new VarInsnNode(Opcodes.ASTORE, 9));

                            methodnode.instructions.insertBefore(methodnode.instructions.get(count + 2), toAdd);
                            injectionCount++;
                        }
                        else if (nodeAt.name.equals(map.get("getSkyColorMethod")) && nodeAt.desc.equals(map.get("getSkyColorDesc")))
                        {
                            InsnList toAdd = new InsnList();

                            toAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getSkyColorHook", "(L" + map.get("worldClass") + ";)L" + map.get("vecClass") + ";"));
                            toAdd.add(new VarInsnNode(Opcodes.ASTORE, 5));

                            methodnode.instructions.insertBefore(methodnode.instructions.get(count + 2), toAdd);
                            injectionCount++;
                        }
                    }
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    public byte[] transform7(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 1;
        int injectionCount = 0;

        final Iterator<MethodNode> methods = node.methods.iterator();

        while (methods.hasNext())
        {
            final MethodNode methodnode = methods.next();

            if (methodnode.name.equals(map.get("wakeEntityMethod")) && methodnode.desc.equals(map.get("wakeEntityDesc")))
            {
                methodnode.instructions.insertBefore(methodnode.instructions.get(methodnode.instructions.size() - 3), new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/miccore/MicdoodlePlugin", "onSleepCancelled", "()V"));
                injectionCount++;
                break;
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    public byte[] transform8(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 1;
        int injectionCount = 0;

        final Iterator<MethodNode> methods = node.methods.iterator();

        while (methods.hasNext())
        {
            final MethodNode methodnode = methods.next();

            if (methodnode.name.equals("orientBedCamera") && methodnode.desc.equals(map.get("orientCameraDesc")))
            {
                methodnode.instructions.insertBefore(methodnode.instructions.get(0), new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/miccore/MicdoodlePlugin", "orientCamera", "()V"));
                injectionCount++;
                break;
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    @SuppressWarnings("unchecked")
	public byte[] transform9(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int injectionCount = 0;

        final Iterator<MethodNode> methods = node.methods.iterator();
        List<String> ignoredMods = new ArrayList<String>();

        while (methods.hasNext())
        {
            MethodNode methodnode = methods.next();

            methodLabel:
            if (methodnode.visibleAnnotations != null && methodnode.visibleAnnotations.size() > 0)
            {
                final Iterator<AnnotationNode> annotations = methodnode.visibleAnnotations.iterator();

                while (annotations.hasNext())
                {
                    AnnotationNode annotation = annotations.next();

                    if (annotation.desc.equals("Lmicdoodle8/mods/galacticraft/core/GCCoreAnnotations$RuntimeInterface;"))
                    {
                    	List<String> desiredInterfaces = new ArrayList<String>();
                    	String modID = "";
                    	
                    	for (int i = 0; i < annotation.values.size(); i++)
                    	{
                    		Object value = annotation.values.get(i);
                    		
                    		if (value.equals("clazz"))
                    		{
                    			desiredInterfaces.add(String.valueOf(annotation.values.get(i + 1)));
                    		}
                    		else if (value.equals("modID"))
                    		{
                    			modID = String.valueOf(annotation.values.get(i + 1));
                    		}
                    		else if (value.equals("altClasses"))
                    		{
                    			desiredInterfaces.addAll((ArrayList<String>) annotation.values.get(i + 1));
                    		}
                    	}
                    	
                    	if (!ignoredMods.contains(modID))
                    	{
                        	boolean modFound = Loader.isModLoaded(modID);
                        	
                        	if (modFound)
                        	{
                        		for (String inter : desiredInterfaces)
                        		{
                                    try
                                    {
                                    	Class.forName(inter);
                                    }
                                    catch (ClassNotFoundException e)
                                    {
                                    	System.out.println("Galacticraft ignored missing interface \"" + inter + "\" from mod \"" + modID + "\".");
                                    	continue;
                                    }
                                    
                                    inter = inter.replace(".", "/");

                                    if (!node.interfaces.contains(inter))
                                    {
                                    	System.out.println("Galacticraft added interface \"" + inter + "\" dynamically from \"" + modID + "\" to class \"" + node.name + "\".");
                                        node.interfaces.add(inter);
                                        injectionCount++;
                                    }
                                    
                                    break;
                        		}
                        	}
                        	else
                        	{
                        		ignoredMods.add(modID);
                            	System.out.println("Galacticraft ignored dynamic interface insertion since \"" + modID + "\" was not found.");
                        	}
                    	}
                    	
                    	break methodLabel;
                    }
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        if (injectionCount > 0)
        {
            System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + ")");
        }

        return bytes;
    }

    public byte[] transform14(String name, byte[] bytes, HashMap<String, String> map)
    {
        final ClassNode node = new ClassNode();
        final ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        int operationCount = 1;
        int injectionCount = 0;

        final Iterator<MethodNode> methods = node.methods.iterator();

        findmethod:
        while (methods.hasNext())
        {
            final MethodNode methodnode = methods.next();

            if (methodnode.name.equals(map.get("renderOverlaysMethod")) && methodnode.desc.equals(map.get("renderOverlaysDesc")))
            {
                for (int count = 0; count < methodnode.instructions.size(); count++)
                {
                    final AbstractInsnNode glEnable = methodnode.instructions.get(count);

                    if (glEnable instanceof MethodInsnNode && ((MethodInsnNode) glEnable).name.equals("glEnable"))
                    {
                        InsnList toAdd = new InsnList();

                        toAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));
                        toAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/client/ClientProxyCore", "renderLiquidOverlays", "(F)V"));

                        methodnode.instructions.insertBefore(glEnable, toAdd);
                        injectionCount++;
                        break findmethod;
                    }
                }
            }
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }
}
