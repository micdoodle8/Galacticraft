package micdoodle8.mods.galacticraft.asm;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import cpw.mods.fml.relauncher.IClassTransformer;
import cpw.mods.fml.relauncher.RelaunchClassLoader;

public class GCCoreTransformer implements IClassTransformer
{
    HashMap<String, String> nodemap = new HashMap<String, String>();
    private boolean deobfuscated = true;

    public GCCoreTransformer()
    {
        try
        {
            @SuppressWarnings("resource")
            final URLClassLoader loader = new RelaunchClassLoader(((URLClassLoader) this.getClass().getClassLoader()).getURLs());
            final URL classResource = loader.findResource(String.valueOf("net.minecraft.world.World").replace('.', '/').concat(".class"));
            if (classResource == null)
            {
                this.deobfuscated = false;
            }
            else
            {
                this.deobfuscated = true;
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
            this.nodemap.put("createPlayerDesc", "(Ljava/lang/String;)L" + this.nodemap.get("playerMP") + ";");
            this.nodemap.put("respawnPlayerDesc", "(L" + this.nodemap.get("playerMP") + ";IZ)L" + this.nodemap.get("playerMP") + ";");

            this.nodemap.put("attemptLoginMethodBukkit", "");
            this.nodemap.put("attemptLoginDescBukkit", "");

            this.nodemap.put("playerControllerClass", "net/minecraft/client/multiplayer/PlayerControllerMP");
            this.nodemap.put("playerClient", "net/minecraft/client/entity/EntityClientPlayerMP");
            this.nodemap.put("netClientHandler", "net/minecraft/client/multiplayer/NetClientHandler");
            this.nodemap.put("createClientPlayerMethod", "func_78754_a");
            this.nodemap.put("createClientPlayerDesc", "(L" + this.nodemap.get("worldClass") + ";)L" + this.nodemap.get("playerClient") + ";");

            this.nodemap.put("entityLivingClass", "net/minecraft/entity/EntityLiving");
            this.nodemap.put("moveEntityMethod", "moveEntityWithHeading");
            this.nodemap.put("moveEntityDesc", "(FF)V");
            this.nodemap.put("entityLiving", "net/minecraft/entity/EntityLiving");

            this.nodemap.put("entityItemClass", "net/minecraft/entity/item/EntityItem");
            this.nodemap.put("onUpdateMethod", "onUpdate");
            this.nodemap.put("onUpdateDesc", "()V");

            this.nodemap.put("entityRendererClass", "net/minecraft/client/renderer/EntityRenderer");
            this.nodemap.put("updateLightmapMethod", "updateLightmap");
            this.nodemap.put("updateLightmapDesc", "(F)V");

            this.nodemap.put("player", "net/minecraft/entity/player/EntityPlayer");
            this.nodemap.put("invPlayer", "inventory");
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
        }
        else
        {
            this.nodemap.put("worldClass", "aab");

            this.nodemap.put("playerMP", "jc");
            this.nodemap.put("netLoginHandler", "jf");
            this.nodemap.put("confManagerClass", "gu");
            this.nodemap.put("createPlayerMethod", "a");
            this.nodemap.put("createPlayerDesc", "(Ljava/lang/String;)L" + this.nodemap.get("playerMP") + ";");
            this.nodemap.put("respawnPlayerMethod", "a");
            this.nodemap.put("respawnPlayerDesc", "(L" + this.nodemap.get("playerMP") + ";IZ)L" + this.nodemap.get("playerMP") + ";");

            this.nodemap.put("attemptLoginMethodBukkit", "attemptLogin");
            this.nodemap.put("attemptLoginDescBukkit", "(L" + this.nodemap.get("netLoginHandler") + ";Ljava/lang/String;Ljava/lang/String;)L" + this.nodemap.get("playerMP") + ";");

            this.nodemap.put("playerControllerClass", "bdr");
            this.nodemap.put("playerClient", "bdv");
            this.nodemap.put("netClientHandler", "bdk");
            this.nodemap.put("createClientPlayerMethod", "a");
            this.nodemap.put("createClientPlayerDesc", "(L" + this.nodemap.get("worldClass") + ";)L" + this.nodemap.get("playerClient") + ";");

            this.nodemap.put("entityLivingClass", "ng");
            this.nodemap.put("moveEntityMethod", "e");
            this.nodemap.put("moveEntityDesc", "(FF)V");
            this.nodemap.put("entityLiving", "ng");

            this.nodemap.put("entityItemClass", "rh");
            this.nodemap.put("onUpdateMethod", "l_");
            this.nodemap.put("onUpdateDesc", "()V");

            this.nodemap.put("entityRendererClass", "bfq");
            this.nodemap.put("updateLightmapMethod", "h");
            this.nodemap.put("updateLightmapDesc", "(F)V");

            this.nodemap.put("player", "sq");
            this.nodemap.put("invPlayer", "bK");
            this.nodemap.put("containerPlayer", "tz");
            this.nodemap.put("invPlayerClass", "so");

            this.nodemap.put("minecraft", "net/minecraft/client/Minecraft");
            this.nodemap.put("session", "awf");
            this.nodemap.put("guiPlayer", "azg");
            this.nodemap.put("thePlayer", "g");
            this.nodemap.put("displayGui", "a");
            this.nodemap.put("guiScreen", "axr");
            this.nodemap.put("displayGuiDesc", "(L" + this.nodemap.get("guiScreen") + ";)V");
            this.nodemap.put("runTick", "l");
            this.nodemap.put("runTickDesc", "()V");
            this.nodemap.put("clickMiddleMouseButton", "O");
            this.nodemap.put("clickMiddleMouseButtonDesc", "()V");
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
        else if (name.replace('.', '/').equals(this.nodemap.get("minecraft")))
        {
            bytes = this.transform6(name, bytes, this.nodemap);
        }
        else if (name.replace('.', '/').equals(this.nodemap.get("player")))
        {
            bytes = this.transform7(name, bytes, this.nodemap);
        }
        else if (this.deobfuscated && name.equals("codechicken.nei.NEICPH"))
        {
            bytes = this.transform10(name, bytes, this.nodemap);
        }
        else if (name.equals("codechicken.nei.ContainerCreativeInv"))
        {
            bytes = this.transform11(name, bytes, this.nodemap);
        }
        else if (name.equals("mithion.arsmagica.guis.GuiIngameArsMagica"))
        {
            bytes = this.transform12(name, bytes, this.nodemap);
        }
        else if (name.equals("mods.tinker.tconstruct.client.TProxyClient"))
        {
            bytes = this.transform13(name, bytes, this.nodemap);
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
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.owner.contains(map.get("playerMP")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));
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
                            final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP");

                            methodnode.instructions.set(nodeAt, overwriteNode);
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerMP")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/item/ItemInWorldManager;)V"));
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
                            final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP");

                            methodnode.instructions.set(nodeAt, overwriteNode);
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerMP")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/entities/GCCorePlayerMP", "<init>", "(Lnet/minecraft/server/MinecraftServer;Laab;Ljava/lang/String;Ljd;)V"));
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
                            final TypeInsnNode overwriteNode = new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP");

                            methodnode.instructions.set(nodeAt, overwriteNode);
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.name.equals("<init>") && nodeAt.owner.equals(map.get("playerClient")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/GCCorePlayerSP", "<init>", "(L" + map.get("minecraft") + ";L" + map.get("worldClass") + ";L" + map.get("session") + ";L" + map.get("netClientHandler") + ";)V"));
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
                            final MethodInsnNode overwriteNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "getGravityForEntity", "(L" + map.get("entityLiving") + ";)D");

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
        
        int operationCount = 1;
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
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();

        System.out.println("Galacticraft successfully injected bytecode into: " + node.name + " (" + injectionCount + " / " + operationCount + ")");

        return bytes;
    }

    public byte[] transform6(String name, byte[] bytes, HashMap<String, String> map)
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
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("guiPlayer")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory", "<init>", "(L" + map.get("player") + ";)V"));
                            injectionCount++;
                        }
                    }
                }
            }
            else if (methodnode.name.equals(map.get("clickMiddleMouseButton")) && methodnode.desc.equals(map.get("clickMiddleMouseButtonDesc")))
            {
                for (int count = 0; count < methodnode.instructions.size(); count++)
                {
                    final AbstractInsnNode list = methodnode.instructions.get(count);

                    if (list instanceof IntInsnNode)
                    {
                        IntInsnNode nodeAt = (IntInsnNode) list;

                        if (nodeAt.operand == 9)
                        {
                            methodnode.instructions.set(nodeAt, new IntInsnNode(Opcodes.BIPUSH, 14));
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
        
        int operationCount = 4;
        int injectionCount = 0;

        final Iterator<MethodNode> methods = node.methods.iterator();

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
                            injectionCount++;
                        }
                        else if (nodeAt.getOpcode() == Opcodes.NEW && nodeAt.desc.equals(map.get("invPlayerClass")))
                        {
                            methodnode.instructions.set(nodeAt, new TypeInsnNode(Opcodes.NEW, "micdoodle8/mods/galacticraft/core/inventory/GCCoreInventoryPlayer"));
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("containerPlayer")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/inventory/GCCoreContainerPlayer", "<init>", "(L" + map.get("invPlayerClass") + ";ZL" + map.get("player") + ";)V"));
                            injectionCount++;
                        }
                        else if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("invPlayerClass")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/inventory/GCCoreInventoryPlayer", "<init>", "(L" + map.get("player") + ";)V"));
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

    public byte[] transform10(String name, byte[] bytes, HashMap<String, String> map)
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
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("guiPlayer")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory", "<init>", "(L" + map.get("player") + ";)V"));
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

    public byte[] transform11(String name, byte[] bytes, HashMap<String, String> map)
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

            if (methodnode.name.equals("<init>"))
            {
                for (int count = 1; count < methodnode.instructions.size(); count++)
                {
                    final AbstractInsnNode list = methodnode.instructions.get(count);
                    final AbstractInsnNode prevList = methodnode.instructions.get(count - 1);

                    if (list instanceof InsnNode && prevList instanceof MethodInsnNode)
                    {
                        final InsnNode insn = (InsnNode) list;

                        if (insn.getOpcode() == Opcodes.ICONST_1)
                        {
                            methodnode.instructions.set(insn, new IntInsnNode(Opcodes.BIPUSH, 6));
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

    public byte[] transform12(String name, byte[] bytes, HashMap<String, String> map)
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

            if (methodnode.name.equals("renderManaAmount"))
            {
                for (int count = 0; count < methodnode.instructions.size() - 1; count++)
                {
                    final AbstractInsnNode list = methodnode.instructions.get(count);
                    final AbstractInsnNode prevList = methodnode.instructions.get(count + 1);

                    if (list instanceof LdcInsnNode)
                    {
                        final LdcInsnNode insn = (LdcInsnNode) list;

                        if (insn.cst.equals("Current Mana: "))
                        {
                            methodnode.instructions.set(insn, new LdcInsnNode("Mana:"));
                            injectionCount++;
                        }
                    }
                    else if (list instanceof InsnNode && prevList instanceof VarInsnNode)
                    {
                        final InsnNode insn = (InsnNode) list;
                        final VarInsnNode insn2 = (VarInsnNode) prevList;

                        if (insn.getOpcode() == Opcodes.ICONST_0 && insn2.getOpcode() == Opcodes.ISTORE)
                        {
                            methodnode.instructions.set(insn, new IntInsnNode(Opcodes.BIPUSH, 80));
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

    public byte[] transform13(String name, byte[] bytes, HashMap<String, String> map)
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
                            injectionCount++;
                        }
                    }
                    else if (list instanceof MethodInsnNode)
                    {
                        final MethodInsnNode nodeAt = (MethodInsnNode) list;

                        if (nodeAt.getOpcode() == Opcodes.INVOKESPECIAL && nodeAt.owner.equals(map.get("guiPlayer")))
                        {
                            methodnode.instructions.set(nodeAt, new MethodInsnNode(Opcodes.INVOKESPECIAL, "micdoodle8/mods/galacticraft/core/client/gui/GCCoreGuiInventory", "<init>", "(L" + map.get("player") + ";)V"));
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
}
