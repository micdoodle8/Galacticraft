package micdoodle8.mods.galacticraft.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IOrbitDimension;
import micdoodle8.mods.galacticraft.API.ISpaceship;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreEnumTeleportType;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProvider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListPlanets;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListSpaceStations;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSpaceStationData;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet43Experience;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

public class WorldUtil
{
    public static Collection<Integer> registeredSpaceStations;
    public static Collection<Integer> registeredPlanets;
    public static Collection<String> registeredPlanetNames;
    public static List<ItemStack> useless = new ArrayList();
    public static List<ItemStack> common = new ArrayList();
    public static List<ItemStack> uncommon = new ArrayList();
    public static List<ItemStack> rare = new ArrayList();
    public static List<ItemStack> ultrarare = new ArrayList();

    public static boolean generateChestContents(World var1, Random var2, int var3, int var4, int var5)
    {
        boolean var6 = true;
        var1.setBlock(var3, var4, var5, Block.chest.blockID, 0, 3);
        int var7;

        for (var7 = 0; var7 < 4; ++var7)
        {
            var6 &= WorldUtil.addItemToChest(var1, var2, var3, var4, var5, WorldUtil.getCommonItem(var2));
        }

        for (var7 = 0; var7 < 2; ++var7)
        {
            var6 &= WorldUtil.addItemToChest(var1, var2, var3, var4, var5, WorldUtil.getUncommonItem(var2));
        }

        for (var7 = 0; var7 < 1; ++var7)
        {
            var6 &= WorldUtil.addItemToChest(var1, var2, var3, var4, var5, WorldUtil.getRareItem(var2));
        }

        return var6;
    }

    public static ItemStack getCommonItem(Random var1)
    {
        return var1.nextInt(4) == 0 ? WorldUtil.getRandomItemFromList(WorldUtil.useless, var1) : WorldUtil.getRandomItemFromList(WorldUtil.common, var1);
    }

    public static ItemStack getUncommonItem(Random var1)
    {
        return WorldUtil.getRandomItemFromList(WorldUtil.uncommon, var1);
    }

    public static ItemStack getRareItem(Random var1)
    {
        return var1.nextInt(4) == 0 ? WorldUtil.getRandomItemFromList(WorldUtil.ultrarare, var1) : WorldUtil.getRandomItemFromList(WorldUtil.rare, var1);
    }

    public static ItemStack getRandomItemFromList(List list, Random rand)
    {
    	return (ItemStack) list.get(rand.nextInt(list.size()));
    }

    protected static boolean addItemToChest(World var1, Random var2, int var3, int var4, int var5, ItemStack var6)
    {
        final TileEntityChest var7 = (TileEntityChest)var1.getBlockTileEntity(var3, var4, var5);

        if (var7 != null)
        {
            final int var8 = WorldUtil.findRandomInventorySlot(var7, var2);

            if (var8 != -1)
            {
                var7.setInventorySlotContents(var8, var6);
                return true;
            }
        }

        return false;
    }

    protected static int findRandomInventorySlot(TileEntityChest var1, Random var2)
    {
        for (int var3 = 0; var3 < 100; ++var3)
        {
            final int var4 = var2.nextInt(var1.getSizeInventory());

            if (var1.getStackInSlot(var4) == null)
            {
                return var4;
            }
        }

        return -1;
    }

	public static WorldProvider getProviderForName(String par1String)
	{
		final Integer[] var1 = WorldUtil.getArrayOfPossibleDimensions();

		for (final Integer element : var1)
		{
			if (WorldProvider.getProviderForDimension(element) != null && WorldProvider.getProviderForDimension(element).getDimensionName() != null)
			{
				if (par1String.contains("$"))
				{
					String[] twoDimensions = par1String.split("\\$");

					if (WorldProvider.getProviderForDimension(element).getDimensionName().equals(twoDimensions[0]))
					{
						return WorldProvider.getProviderForDimension(element);
					}
				}
				else if (WorldProvider.getProviderForDimension(element).getDimensionName().equals(par1String))
				{
					return WorldProvider.getProviderForDimension(element);
				}
			}
		}

		return null;
	}

	public static int getAmountOfPossibleProviders(Integer[] ids)
	{
		int amount = 0;

		for (final Integer id : ids)
		{
    		if (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider || WorldProvider.getProviderForDimension(id).dimensionId == 0)
    		{
    			amount++;
    		}
		}

		return amount;
	}

	public static HashMap getArrayOfPossibleDimensions(Integer[] ids)
	{
		return WorldUtil.getArrayOfPossibleDimensions(ids, null);
	}

	public static HashMap getArrayOfPossibleDimensions(Integer[] ids, GCCorePlayerMP playerBase)
	{
		final HashMap map = new HashMap();

		for (final Integer id : ids)
		{
//			if (playerBase != null && WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension && playerBase.worldObj instanceof WorldServer)
//			{
//				((WorldServer)playerBase.worldObj).func_98180_V().func_98233_a("Found ID:" + id);
//				
//    			GCCoreSpaceStationData data = GCCoreSpaceStationData.getStationData(playerBase.worldObj, id, playerBase);
//
//    			for (String str : data.getAllowedPlayers())
//    			{
//    				((WorldServer)playerBase.worldObj).func_98180_V().func_98233_a(str);
//    			}
//    			
//				((WorldServer)playerBase.worldObj).func_98180_V().func_98233_a("" + playerBase.spaceStationDimensionID);
//				
//    			if (data != null && data.getAllowedPlayers().contains(playerBase.username.toLowerCase()))
//    			{
//    				((WorldServer)playerBase.worldObj).func_98180_V().func_98233_a((String) (WorldProvider.getProviderForDimension(id).getDimensionName() + "$" + ((IOrbitDimension) WorldProvider.getProviderForDimension(id)).getPlanetToOrbit()));
//    			}
//			}
			
			if (WorldProvider.getProviderForDimension(id) != null)
			{
	    		if (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider && !(WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension) || WorldProvider.getProviderForDimension(id).dimensionId == 0)
	    		{
	    			map.put(WorldProvider.getProviderForDimension(id).getDimensionName(), WorldProvider.getProviderForDimension(id).dimensionId);
	    		}
	    		else if (playerBase != null && WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension)
	    		{
	    			GCCoreSpaceStationData data = GCCoreSpaceStationData.getStationData(playerBase.worldObj, id, playerBase);

	    			if (!GCCoreConfigManager.spaceStationsRequirePermission || data.getAllowedPlayers().contains(playerBase.username.toLowerCase()))
	    			{
	    				map.put(WorldProvider.getProviderForDimension(id).getDimensionName() + "$" + data.getOwner(), WorldProvider.getProviderForDimension(id).dimensionId);
	    			}
	    		}
			}
		}

		for (int j = 0; j < GalacticraftCore.subMods.size(); j++)
		{
			if (!GalacticraftCore.subMods.get(j).reachableDestination())
			{
				map.put(GalacticraftCore.subMods.get(j).getDimensionName() + "*", 0);
			}
		}

		return map;
	}

	public static double getSpaceshipFailChance(EntityPlayer player)
	{
		final Double level = Double.valueOf(player.experienceLevel);

		if (level <= 50.0D)
		{
			return 12.5D - level / 4.0D;
		}
		else
		{
			return 0.0;
		}
	}

	public static float calculateMarsAngleFromOtherPlanet(long par1, float par3)
	{
        final int var4 = (int)(par1 % 48000L);
        float var5 = (var4 + par3) / 48000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        final float var6 = var5;
        var5 = 1.0F - (float)((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
	}

	public static float calculateEarthAngleFromOtherPlanet(long par1, float par3)
	{
        final int var4 = (int)(par1 % 48000L);
        float var5 = (var4 + par3) / 48000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        final float var6 = var5;
        var5 = 1.0F - (float)((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
	}

	public static List getPlayersOnPlanet(IMapPlanet planet)
	{
		final List list = new ArrayList();

		for (final WorldServer world : DimensionManager.getWorlds())
		{
			if (world != null && world.provider instanceof IGalacticraftWorldProvider)
			{
				if (planet.getSlotRenderer().getPlanetName().toLowerCase().equals(world.provider.getDimensionName().toLowerCase()))
				{
					for (int j = 0; j < world.getLoadedEntityList().size(); j++)
					{
						if (world.getLoadedEntityList().get(j) != null && world.getLoadedEntityList().get(j) instanceof EntityPlayer)
						{
							list.add(((EntityPlayer)world.getLoadedEntityList().get(j)).username);
						}
					}
				}
			}
		}

		return list;
	}
	
//    public static void travelToDimension(Entity entity, WorldServer world, int par1)
//    {
//        if (!entity.worldObj.isRemote && !entity.isDead)
//        {
//        	entity.worldObj.theProfiler.startSection("changeDimension");
//            MinecraftServer minecraftserver = MinecraftServer.getServer();
//            int j = entity.dimension;
//            WorldServer worldserver = minecraftserver.worldServerForDimension(-1);
//            WorldServer worldserver1 = minecraftserver.worldServerForDimension(0);
//            entity.dimension = par1;
//            entity.worldObj.removeEntity(entity);
//            entity.isDead = false;
//            entity.worldObj.theProfiler.startSection("reposition");
//            
//    		for (int i = 0; i < world.customTeleporters.size(); i++)
//    		{
//    			if (world.customTeleporters.get(i) instanceof GCCoreTeleporter)
//    			{
//                    transferEntityToWorld(entity, j, worldserver, worldserver1, world.customTeleporters.get(i));
//    			}
//    		}
//    		
//            entity.worldObj.theProfiler.endStartSection("reloading");
//            Entity entity2 = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);
//
//            if (entity2 != null)
//            {
//            	entity2.copyDataFrom(entity, true);
//                worldserver1.spawnEntityInWorld(entity2);
//            }
//
//            entity.isDead = true;
//            entity.worldObj.theProfiler.endSection();
//            worldserver.resetUpdateEntityTick();
//            worldserver1.resetUpdateEntityTick();
//            entity.worldObj.theProfiler.endSection();
//        }
//    }
//
//    public static void transferEntityToWorld(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer, Teleporter teleporter)
//    {
//        WorldProvider pOld = par3WorldServer.provider;
//        WorldProvider pNew = par4WorldServer.provider;
//        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
//        double d0 = par1Entity.posX * moveFactor;
//        double d1 = par1Entity.posZ * moveFactor;
//        double d3 = par1Entity.posX;
//        double d4 = par1Entity.posY;
//        double d5 = par1Entity.posZ;
//        float f = par1Entity.rotationYaw;
//        par3WorldServer.theProfiler.startSection("moving");
//
//        if (par1Entity.dimension == 1)
//        {
//            ChunkCoordinates chunkcoordinates;
//
//            if (par2 == 1)
//            {
//                chunkcoordinates = par4WorldServer.getSpawnPoint();
//            }
//            else
//            {
//                chunkcoordinates = par4WorldServer.getEntrancePortalLocation();
//            }
//
//            d0 = (double)chunkcoordinates.posX;
//            par1Entity.posY = (double)chunkcoordinates.posY;
//            d1 = (double)chunkcoordinates.posZ;
//            par1Entity.setLocationAndAngles(d0, par1Entity.posY, d1, 90.0F, 0.0F);
//
//            if (par1Entity.isEntityAlive())
//            {
//                par3WorldServer.updateEntityWithOptionalForce(par1Entity, false);
//            }
//        }
//
//        par3WorldServer.theProfiler.endSection();
//
//        if (par2 != 1)
//        {
//            par3WorldServer.theProfiler.startSection("placing");
//            d0 = (double)MathHelper.clamp_int((int)d0, -29999872, 29999872);
//            d1 = (double)MathHelper.clamp_int((int)d1, -29999872, 29999872);
//
//            if (par1Entity.isEntityAlive())
//            {
//                par4WorldServer.spawnEntityInWorld(par1Entity);
//                par1Entity.setLocationAndAngles(d0, par1Entity.posY, d1, par1Entity.rotationYaw, par1Entity.rotationPitch);
//                par4WorldServer.updateEntityWithOptionalForce(par1Entity, false);
//                teleporter.placeInPortal(par1Entity, d3, d4, d5, f);
//            }
//
//            par3WorldServer.theProfiler.endSection();
//        }
//
//        par1Entity.setWorld(par4WorldServer);
//        
//        if (par1Entity instanceof IInterplanetaryObject)
//        {
//        	((IInterplanetaryObject) par1Entity).onPlanetChanged();
//        }
//    }

    private static List getExistingSpaceStationList(File var0)
    {
        ArrayList var1 = new ArrayList();
        File[] var2 = var0.listFiles();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            File var5 = var2[var4];

            if (var5.getName().contains("spacestation_"))
            {
                String var6 = var5.getName();
                var6 = var6.substring(13, var6.length() - 4);
                var1.add(Integer.valueOf(Integer.parseInt(var6)));
            }
        }

        return var1;
    }

    public static void unregisterSpaceStations()
    {
        if (WorldUtil.registeredSpaceStations != null)
        {
            Iterator var0 = WorldUtil.registeredSpaceStations.iterator();

            while (var0.hasNext())
            {
                Integer var1 = (Integer)var0.next();
                DimensionManager.unregisterDimension(var1.intValue());
            }

            WorldUtil.registeredSpaceStations = null;
        }
    }

    public static void registerSpaceStations(File var0)
    {
    	WorldUtil.registeredSpaceStations = WorldUtil.getExistingSpaceStationList(var0);
        Iterator var1 = WorldUtil.registeredSpaceStations.iterator();

        while (var1.hasNext())
        {
            Integer var2 = (Integer)var1.next();
            DimensionManager.registerDimension(var2.intValue(), GCCoreConfigManager.idDimensionOverworldOrbit);
        }
    }

    /**
     * Call this on FMLServerStartingEvent to add a hotloadable planet ID
     */
    public static void registerPlanet(int planetID, boolean isStatic)
    {
    	if (WorldUtil.registeredPlanets == null)
    	{
    		WorldUtil.registeredPlanets = new ArrayList<Integer>();
    	}
    	
		WorldUtil.registeredPlanets.add(planetID);

    	if (isStatic)
    	{
    		DimensionManager.registerDimension(planetID, planetID);
            FMLLog.info("Registered Dimension: " + planetID);
    	}
    }

    public static void unregisterPlanets()
    {
        if (WorldUtil.registeredPlanets != null)
        {
            Iterator var0 = WorldUtil.registeredPlanets.iterator();

            while (var0.hasNext())
            {
                Integer var1 = (Integer)var0.next();
                DimensionManager.unregisterDimension(var1.intValue());
                FMLLog.info("Unregistered Dimension: " + var1.intValue());
            }

            WorldUtil.registeredPlanets = null;
        }
    }
    
    public static Integer[] getArrayOfPossibleDimensions()
    {
    	ArrayList<Integer> temp = new ArrayList<Integer>();
    	
		temp.add(0);
    	
    	for (Integer i : WorldUtil.registeredPlanets)
    	{
    		temp.add(i);
    	}
    	
    	for (Integer i : WorldUtil.registeredSpaceStations)
    	{
    		temp.add(i);
    	}
    	
    	Integer[] finalArray = new Integer[temp.size()];
    	
    	int count = 0;
    	
    	for (Integer integ : temp)
    	{
    		finalArray[count++] = integ;
    	}
    	
    	return finalArray;
    }
    
    public static GCCoreSpaceStationData bindSpaceStationToNewDimension(World var0, GCCorePlayerMP player)
    {
    	int newID = DimensionManager.getNextFreeDimId();
    	GCCoreSpaceStationData data = WorldUtil.createSpaceStation(var0, newID, player);
    	player.spaceStationDimensionID = newID;
    	Object[] toSend = {newID};
    	player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 18, toSend));
    	return data;
    }
    
    public static GCCoreSpaceStationData createSpaceStation(World var0, int par1, GCCorePlayerMP player)
    {
    	WorldUtil.registeredSpaceStations.add(par1);
        DimensionManager.registerDimension(par1, GCCoreConfigManager.idDimensionOverworldOrbit);
    	
        MinecraftServer var2 = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (var2 != null)
        {
            ArrayList var1 = new ArrayList();
            var1.add(par1);
            var2.getConfigurationManager().sendPacketToAllPlayers(GCCorePacketDimensionListSpaceStations.buildDimensionListPacket(var1));
        }

        GCCoreSpaceStationData var3 = GCCoreSpaceStationData.getStationData(var0, par1, player);
        return var3;
    }

    private static MinecraftServer mcServer = null;
    
    public static void transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, GCCoreEnumTeleportType type)
    {
        if (!world.isRemote)
        {
            if (WorldUtil.mcServer == null)
            {
                WorldUtil.mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
            }

            if (WorldUtil.mcServer != null)
            {
                WorldServer var6 = WorldUtil.mcServer.worldServerForDimension(dimensionID);

                if (var6 == null)
                {
                    System.err.println("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
                }

                WorldUtil.teleportEntity(var6, entity, dimensionID, type);
            }
        }
    }

    private static Entity teleportEntity(World var0, Entity var1, int var2, GCCoreEnumTeleportType type)
    {
        Entity var6 = var1.ridingEntity;

        if (var1.ridingEntity != null && var1.ridingEntity instanceof ISpaceship)
        {
            var1.mountEntity(var1.ridingEntity);
//            teleportEntity(var0, var1, var2, type);
        }

        boolean var7 = var1.worldObj != var0;
        var1.worldObj.updateEntityWithOptionalForce(var1, false);
        GCCorePlayerMP var8 = null;

        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP)var1;
            var8.closeScreen();

            if (var7)
            {
                var8.dimension = var2;
                var8.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(var8.dimension, (byte)var8.worldObj.difficultySetting, var0.getWorldInfo().getTerrainType(), var0.getHeight(), var8.theItemInWorldManager.getGameType()));

                if (var0.provider instanceof GCCoreWorldProvider && WorldUtil.registeredSpaceStations.contains(var8))
                {
                	var8.playerNetServerHandler.sendPacketToPlayer(GCCorePacketSpaceStationData.buildSpaceStationDataPacket(var0, var0.provider.dimensionId, var8));
                }

                ((WorldServer)var1.worldObj).getPlayerManager().removePlayer(var8);
            }
            
            var8.setNotUsingPlanetGui();
        }

        if (var7)
        {
            WorldUtil.removeEntityFromWorld(var1.worldObj, var1);
        }

        if (var7)
        {
            if (var1 instanceof EntityPlayer)
            {
                switch (type)
                {
                case TOORBIT:
                    var1.setLocationAndAngles(0.5D, 65.0D, 0.5D, var1.rotationYaw, var1.rotationPitch);
                    ((WorldServer)var0).theChunkProviderServer.loadChunk(0, 0);
                    break;
                case TOPLANET:
                    var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                    ((WorldServer)var0).theChunkProviderServer.loadChunk(var0.getChunkFromChunkCoords(MathHelper.floor_double(var8.coordsTeleportedFromX), MathHelper.floor_double(var8.coordsTeleportedFromZ)).getChunkCoordIntPair().chunkXPos, var0.getChunkFromBlockCoords(MathHelper.floor_double(var8.coordsTeleportedFromX), MathHelper.floor_double(var8.coordsTeleportedFromZ)).getChunkCoordIntPair().chunkZPos);
                    break;
                case TOOVERWORLD:
                    var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                    ((WorldServer)var0).theChunkProviderServer.loadChunk(var0.getChunkFromChunkCoords(MathHelper.floor_double(var8.coordsTeleportedFromX), MathHelper.floor_double(var8.coordsTeleportedFromZ)).getChunkCoordIntPair().chunkXPos, var0.getChunkFromBlockCoords(MathHelper.floor_double(var8.coordsTeleportedFromX), MathHelper.floor_double(var8.coordsTeleportedFromZ)).getChunkCoordIntPair().chunkZPos);
                    break;
                }
            }
        }

        if (var7)
        {
            if (var1 instanceof EntityPlayer)
            {
                switch (type)
                {
                case TOORBIT:
                    var1.setPosition(0.5D, 65.0D, 0.5D);
                    break;
                case TOPLANET:
                    var1.setPosition(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ);
                    break;
                case TOOVERWORLD:
                    var1.setPosition(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ);
                    break;
                }
            }
        }

        if (var7)
        {
            if (!(var1 instanceof EntityPlayer))
            {
                NBTTagCompound var11 = new NBTTagCompound();
                var1.isDead = false;
                var1.addEntityID(var11);
                var1.isDead = true;
                var1 = EntityList.createEntityFromNBT(var11, var0);

                if (var1 == null)
                {
                    return null;
                }
            }

            var0.spawnEntityInWorld(var1);
            var1.setWorld(var0);
        }
        
        if (var7)
        {
            if (var1 instanceof EntityPlayer)
            {
                switch (type)
                {
                case TOORBIT:
                    var1.setLocationAndAngles(0.5D, 65.0D, 0.5D, var1.rotationYaw, var1.rotationPitch);
                    break;
                case TOPLANET:
                    var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                    break;
                case TOOVERWORLD:
                    var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                    break;
                }
            }
        }
        
        var0.updateEntityWithOptionalForce(var1, false);
        
        if (var7)
        {
            if (var1 instanceof EntityPlayer)
            {
                switch (type)
                {
                case TOORBIT:
                    var1.setLocationAndAngles(0.5D, 65.0D, 0.5D, var1.rotationYaw, var1.rotationPitch);
                    break;
                case TOPLANET:
                    var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                    break;
                case TOOVERWORLD:
                    var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                    break;
                }
            }
        }

        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP)var1;

            if (var7)
            {
                var8.mcServer.getConfigurationManager().func_72375_a(var8, (WorldServer)var0);
            }

            switch (type)
            {
            case TOORBIT:
            	var8.playerNetServerHandler.setPlayerLocation(0.5D, 65.0D, 0.5D, var1.rotationYaw, var1.rotationPitch);
                break;
            case TOPLANET:
            	var8.playerNetServerHandler.setPlayerLocation(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                break;
            case TOOVERWORLD:
            	var8.playerNetServerHandler.setPlayerLocation(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                break;
            }
            
            FMLLog.info("Server attempting to transfer player " + var8.username + " to dimension " + var0.provider.dimensionId);
        }

        var0.updateEntityWithOptionalForce(var1, false);
        
        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP)var1;
            
            switch (type)
            {
            case TOORBIT:
                break;
            default:
            	var8.setParachute(true);
                break;
            }
        }

        if (var1 instanceof GCCorePlayerMP && var7)
        {
            var8 = (GCCorePlayerMP)var1;
            var8.theItemInWorldManager.setWorld((WorldServer)var0);
            var8.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(var8, (WorldServer)var0);
            var8.mcServer.getConfigurationManager().syncPlayerInventory(var8);
            Iterator var9 = var8.getActivePotionEffects().iterator();

            while (var9.hasNext())
            {
                PotionEffect var10 = (PotionEffect)var9.next();
                var8.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(var8.entityId, var10));
            }

            var8.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(var8.experience, var8.experienceTotal, var8.experienceLevel));
        }
        
        if (var8 != null)
        {
            switch (type)
            {
            case TOORBIT:
            	var1.setLocationAndAngles(0.5D, 65.0D, 0.5D, var1.rotationYaw, var1.rotationPitch);
                break;
            case TOPLANET:
            	var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                break;
            case TOOVERWORLD:
            	var1.setLocationAndAngles(var8.coordsTeleportedFromX, 250.0D, var8.coordsTeleportedFromZ, var1.rotationYaw, var1.rotationPitch);
                break;
            }
        }
        else
        {
            switch (type)
            {
            case TOORBIT:
            	var1.setLocationAndAngles(0.5D, 65.0D, 0.5D, var1.rotationYaw, var1.rotationPitch);
                break;
            case TOPLANET:
            	var1.setLocationAndAngles(var1.posX, 250.0D, var1.posZ, var1.rotationYaw, var1.rotationPitch);
                break;
            case TOOVERWORLD:
            	var1.setLocationAndAngles(var1.posX, 250.0D, var1.posZ, var1.rotationYaw, var1.rotationPitch);
                break;
            }
        }
        
        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP)var1;
            double spawnChestHeight = 250.0D;

            switch (type)
            {
            case TOORBIT:
            	spawnChestHeight = 90.0D;
                break;
            default:
            	spawnChestHeight = 250.0D;
                break;
            }
            
          	for (int i = 0; i < 28; i++)
          	{
          		if (var8.rocketStacks[i] == null)
          		{
          			switch (i)
          			{
          			case 0:
          				var8.rocketStacks[i] = new ItemStack(GCCoreItems.rocketFuelBucket, 1, var8.fuelDamage);
          				break;
          			case 25:
          				var8.rocketStacks[i] = type.equals(GCCoreEnumTeleportType.TOORBIT) ? null : new ItemStack(GCCoreBlocks.landingPad, 9, 0);
          				break;
          			case 26:
          				var8.rocketStacks[i] = new ItemStack(GCCoreItems.spaceship, 1, var8.rocketType);
          				break;
          			}
          		}
          	}

          	if (var8.chestSpawnCooldown == 0)
          	{
              	final GCCoreEntityParaChest chest = new GCCoreEntityParaChest(var0, var8.rocketStacks);

              	double x = (var8.worldObj.rand.nextInt(2) - 1) * 3.0D;
              	double z = (var8.worldObj.rand.nextInt(2) - 1) * 3.0D;
              	
                switch (type)
                {
                case TOORBIT:
              		chest.setPosition(-8.5D, spawnChestHeight, -1.5D);
                    break;
                default:
              		chest.setPosition(var8.posX + x, spawnChestHeight, var8.posZ + z);
                    break;
                }

              	if (!var0.isRemote)
              	{
              		var0.spawnEntityInWorld(chest);
              	}

              	var8.chestSpawnCooldown = 200;
          	}
        }
    	
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (server != null)
        {
            ArrayList array = new ArrayList();
            array.add(var2);
            server.getConfigurationManager().sendPacketToAllPlayers(GCCorePacketDimensionListPlanets.buildDimensionListPacket(array));
        }
        
        if (var1 != null && var6 != null)
        {
            if (var1 instanceof EntityPlayerMP)
            {
                var0.updateEntityWithOptionalForce(var1, true);
            }

            var1.mountEntity(var6);
        }

        return var1;
    }

    private static void removeEntityFromWorld(World var0, Entity var1)
    {
        if (var1 instanceof EntityPlayer)
        {
            EntityPlayer var2 = (EntityPlayer)var1;
            var2.closeScreen();
            var0.playerEntities.remove(var2);
            var0.updateAllPlayersSleepingFlag();
            int var3 = var1.chunkCoordX;
            int var4 = var1.chunkCoordZ;

            if (var1.addedToChunk && var0.getChunkProvider().chunkExists(var3, var4))
            {
                var0.getChunkFromChunkCoords(var3, var4).removeEntity(var1);
                var0.getChunkFromChunkCoords(var3, var4).isModified = true;
            }

            var0.loadedEntityList.remove(var1);
            var0.releaseEntitySkin(var1);
        }

        var1.isDead = false;
    }
}
