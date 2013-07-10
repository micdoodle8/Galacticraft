package micdoodle8.mods.galacticraft.mars.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumSet;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpaceship;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSoundUpdaterSpaceship;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.CommonProxyMars;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelSpaceshipTier2;
import micdoodle8.mods.galacticraft.mars.client.render.block.GCMarsBlockRendererVine;
import micdoodle8.mods.galacticraft.mars.client.render.entity.GCMarsRenderSludgeling;
import micdoodle8.mods.galacticraft.mars.client.render.item.GCMarsItemRendererSpaceshipT2;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsWorldProvider;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityRocketT2;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySludgeling;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class ClientProxyMars extends CommonProxyMars
{
    private static int vineRenderID;
    public static long getFirstBootTime;
    public static long getCurrentTime;
    private final Random rand = new Random();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new GCMarsSounds());
        ClientProxyMars.getFirstBootTime = System.currentTimeMillis();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
        NetworkRegistry.instance().registerChannel(new ClientPacketHandler(), "GalacticraftMars", Side.CLIENT);
        ClientProxyMars.vineRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCMarsBlockRendererVine(ClientProxyMars.vineRenderID));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    @Override
    public void registerRenderInformation()
    {
        RenderingRegistry.registerEntityRenderingHandler(GCMarsEntitySludgeling.class, new GCMarsRenderSludgeling());
        RenderingRegistry.registerEntityRenderingHandler(GCMarsEntityRocketT2.class, new GCCoreRenderSpaceship(new GCMarsModelSpaceshipTier2(), GalacticraftMars.TEXTURE_DOMAIN, "rocketT2"));
        RenderingRegistry.addNewArmourRendererPrefix("desh");
        MinecraftForgeClient.registerItemRenderer(GCMarsItems.spaceship.itemID, new GCMarsItemRendererSpaceshipT2(new GCMarsEntityRocketT2(FMLClientHandler.instance().getClient().theWorld), new GCMarsModelSpaceshipTier2(), new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/rocketT2.png")));
    }

    @Override
    public int getVineRenderID()
    {
        return ClientProxyMars.vineRenderID;
    }

    @Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
        final Minecraft var14 = FMLClientHandler.instance().getClient();

        if (var14 != null && var14.renderViewEntity != null && var14.effectRenderer != null)
        {
            final double var15 = var14.renderViewEntity.posX - var2;
            final double var17 = var14.renderViewEntity.posY - var4;
            final double var19 = var14.renderViewEntity.posZ - var6;
            Object var21 = null;
            final double var22 = 64.0D;

            if (var15 * var15 + var17 * var17 + var19 * var19 < var22 * var22)
            {
                if (var1.equals("sludgeDrip"))
                {
                    var21 = new GCMarsEntityDropParticleFX(var14.theWorld, var2, var4, var6, GCMarsBlocks.bacterialSludge);
                }
            }

            if (var21 != null)
            {
                ((EntityFX) var21).prevPosX = ((EntityFX) var21).posX;
                ((EntityFX) var21).prevPosY = ((EntityFX) var21).posY;
                ((EntityFX) var21).prevPosZ = ((EntityFX) var21).posZ;
                var14.effectRenderer.addEffect((EntityFX) var21);
            }
        }
    }

    public class ClientPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            final int packetType = PacketUtil.readPacketID(data);
            if (packetType == 0)
            {

            }
        }
    }

    public static boolean handleBacterialMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCMarsBlocks.bacterialSludge);
    }

    public static boolean handleLavaMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
    }

    public static boolean handleWaterMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.water);
    }

    public static boolean handleLiquidMovement(EntityPlayer player)
    {
        return ClientProxyMars.handleBacterialMovement(player) || ClientProxyMars.handleLavaMovement(player) || ClientProxyMars.handleWaterMovement(player);
    }

    public static class TickHandlerClient implements ITickHandler
    {
        @Override
        public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
            ClientProxyMars.getCurrentTime = System.currentTimeMillis();

            final Minecraft minecraft = FMLClientHandler.instance().getClient();

            final WorldClient world = minecraft.theWorld;

            if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
                if (world != null)
                {
                    if (world.provider instanceof GCMarsWorldProvider)
                    {
                        if (world.provider.getSkyRenderer() == null)
                        {
                            world.provider.setSkyRenderer(new GCMarsSkyProvider());
                        }
                    }

                    for (int i = 0; i < world.loadedEntityList.size(); i++)
                    {
                        final Entity e = (Entity) world.loadedEntityList.get(i);

                        if (e != null)
                        {
                            if (e instanceof GCMarsEntityRocketT2)
                            {
                                final GCMarsEntityRocketT2 eship = (GCMarsEntityRocketT2) e;

                                if (eship.rocketSoundUpdater == null)
                                {
                                    eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void tickEnd(EnumSet<TickType> type, Object... tickData)
        {
        }

        @Override
        public String getLabel()
        {
            return "Galacticraft Mars Client";
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.CLIENT);
        }
    }
}
