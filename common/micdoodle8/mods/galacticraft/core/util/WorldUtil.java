package micdoodle8.mods.galacticraft.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.API.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.API.entity.ISpaceship;
import micdoodle8.mods.galacticraft.API.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.API.world.ICelestialBody;
import micdoodle8.mods.galacticraft.API.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.world.IMapObject;
import micdoodle8.mods.galacticraft.API.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.API.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.planet.IUpdateable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListPlanets;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionListSpaceStations;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSpaceStationData;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
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
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

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

    public static List<IUpdateable> updateableObjects = new ArrayList<IUpdateable>();

    public static void updatePlanets()
    {
        final List<IUpdateable> planetList = new ArrayList<IUpdateable>();
        planetList.addAll(WorldUtil.updateableObjects);
        for (final IUpdateable planet : planetList)
        {
            planet.update();
        }
    }

    public static double getGravityForEntity(EntityLivingBase eLiving)
    {
        if (eLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) eLiving.worldObj.provider;

            if (eLiving instanceof EntityPlayer)
            {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && eLiving instanceof GCCorePlayerSP)
                {
                    return ((GCCorePlayerSP) eLiving).touchedGround ? 0.08D - customProvider.getGravity() : 0.08D;
                }
                else if (eLiving instanceof GCCorePlayerMP)
                {
                    return ((GCCorePlayerMP) eLiving).touchedGround ? 0.08D - customProvider.getGravity() : 0.08D;
                }
                else
                {
                    return 0.08D;
                }
            }
            else
            {
                return 0.08D - customProvider.getGravity();
            }
        }
        else
        {
            return 0.08D;
        }
    }

    public static double getItemGravity(EntityItem e)
    {
        if (e.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) e.worldObj.provider;
            return 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D;
        }
        else
        {
            return 0.03999999910593033D;
        }
    }

    public static double getItemGravity2(EntityItem e)
    {
        if (e.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            return 1.0D;
        }
        else
        {
            return 0.9800000190734863D;
        }
    }

    public static Vector3 getWorldColor(World world)
    {
        if (world.provider instanceof GCMoonWorldProvider)
        {
            float f1 = world.getCelestialAngle(1);
            float f2 = 1.0F - (MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            double d = 1.0 - f2 * f2 * 0.7;
            return new Vector3(d, d, d);
        }

        return new Vector3(1, 1, 1);
    }

    public static float getColorRed(World world)
    {
        return (float) WorldUtil.getWorldColor(world).x;
    }

    public static float getColorGreen(World world)
    {
        return (float) WorldUtil.getWorldColor(world).y;
    }

    public static float getColorBlue(World world)
    {
        return (float) WorldUtil.getWorldColor(world).z;
    }

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
        final TileEntityChest var7 = (TileEntityChest) var1.getBlockTileEntity(var3, var4, var5);

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
                    final String[] twoDimensions = par1String.split("\\$");

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
            if (WorldProvider.getProviderForDimension(id) != null)
            {
                if (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider && !(WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension) || WorldProvider.getProviderForDimension(id).dimensionId == 0)
                {
                    map.put(WorldProvider.getProviderForDimension(id).getDimensionName(), WorldProvider.getProviderForDimension(id).dimensionId);
                }
                else if (playerBase != null && WorldProvider.getProviderForDimension(id) instanceof IOrbitDimension)
                {
                    final GCCoreSpaceStationData data = GCCoreSpaceStationData.getStationData(playerBase.worldObj, id, playerBase);

                    if (!GCCoreConfigManager.spaceStationsRequirePermission || data.getAllowedPlayers().contains(playerBase.username.toLowerCase()) || data.getAllowedPlayers().contains(playerBase.username))
                    {
                        map.put(WorldProvider.getProviderForDimension(id).getDimensionName() + "$" + data.getOwner() + "$" + data.getSpaceStationName(), WorldProvider.getProviderForDimension(id).dimensionId);
                    }
                }
            }
        }

        for (int j = 0; j < GalacticraftRegistry.getCelestialBodies().size(); j++)
        {
            ICelestialBody object = GalacticraftRegistry.getCelestialBodies().get(j);

            if (!object.isReachable() && object.addToList())
            {
                map.put(object.getName() + "*", 0);
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
        final int var4 = (int) (par1 % 48000L);
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
        var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

    public static float calculateEarthAngleFromOtherPlanet(long par1, float par3)
    {
        final int var4 = (int) (par1 % 48000L);
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
        var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

    public static List getPlayersOnPlanet(IMapObject planet)
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
                            list.add(((EntityPlayer) world.getLoadedEntityList().get(j)).username);
                        }
                    }
                }
            }
        }

        return list;
    }

    private static List getExistingSpaceStationList(File var0)
    {
        final ArrayList var1 = new ArrayList();
        final File[] var2 = var0.listFiles();
        final int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            final File var5 = var2[var4];

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
            final Iterator var0 = WorldUtil.registeredSpaceStations.iterator();

            while (var0.hasNext())
            {
                final Integer var1 = (Integer) var0.next();
                DimensionManager.unregisterDimension(var1.intValue());
            }

            WorldUtil.registeredSpaceStations = null;
        }
    }

    public static void registerSpaceStations(File var0)
    {
        WorldUtil.registeredSpaceStations = WorldUtil.getExistingSpaceStationList(var0);
        final Iterator var1 = WorldUtil.registeredSpaceStations.iterator();

        while (var1.hasNext())
        {
            final Integer var2 = (Integer) var1.next();
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
            GCLog.info("Registered Dimension: " + planetID);
        }
    }

    public static void unregisterPlanets()
    {
        if (WorldUtil.registeredPlanets != null)
        {
            final Iterator var0 = WorldUtil.registeredPlanets.iterator();

            while (var0.hasNext())
            {
                final Integer var1 = (Integer) var0.next();
                DimensionManager.unregisterDimension(var1.intValue());
                GCLog.info("Unregistered Dimension: " + var1.intValue());
            }

            WorldUtil.registeredPlanets = null;
        }
    }

    public static Integer[] getArrayOfPossibleDimensions()
    {
        final ArrayList<Integer> temp = new ArrayList<Integer>();

        temp.add(0);

        for (final Integer i : WorldUtil.registeredPlanets)
        {
            temp.add(i);
        }

        for (final Integer i : WorldUtil.registeredSpaceStations)
        {
            temp.add(i);
        }

        final Integer[] finalArray = new Integer[temp.size()];

        int count = 0;

        for (final Integer integ : temp)
        {
            finalArray[count++] = integ;
        }

        return finalArray;
    }

    public static GCCoreSpaceStationData bindSpaceStationToNewDimension(World var0, GCCorePlayerMP player)
    {
        final int newID = DimensionManager.getNextFreeDimId();
        final GCCoreSpaceStationData data = WorldUtil.createSpaceStation(var0, newID, player);
        player.spaceStationDimensionID = newID;
        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 18, new Object[] { newID }));
        return data;
    }

    public static GCCoreSpaceStationData createSpaceStation(World var0, int par1, GCCorePlayerMP player)
    {
        WorldUtil.registeredSpaceStations.add(par1);
        DimensionManager.registerDimension(par1, GCCoreConfigManager.idDimensionOverworldOrbit);

        final MinecraftServer var2 = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (var2 != null)
        {
            final ArrayList<Integer> var1 = new ArrayList<Integer>();
            var1.add(par1);
            var2.getConfigurationManager().sendPacketToAllPlayers(GCCorePacketDimensionListSpaceStations.buildDimensionListPacket(var1));
        }

        final GCCoreSpaceStationData var3 = GCCoreSpaceStationData.getStationData(var0, par1, player);
        return var3;
    }

    private static MinecraftServer mcServer = null;

    public static void transferEntityToDimension(Entity entity, int dimensionID, WorldServer world)
    {
        WorldUtil.transferEntityToDimension(entity, dimensionID, world, true);
    }

    public static void transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, boolean transferInv)
    {
        if (!world.isRemote)
        {
            final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

            if (server != null)
            {
                final ArrayList array = new ArrayList();

                for (final int i : WorldUtil.registeredPlanets)
                {
                    array.add(i);
                }

                server.getConfigurationManager().sendPacketToAllPlayers(GCCorePacketDimensionListPlanets.buildDimensionListPacket(array));
            }

            if (WorldUtil.mcServer == null)
            {
                WorldUtil.mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
            }

            if (WorldUtil.mcServer != null)
            {
                final WorldServer var6 = WorldUtil.mcServer.worldServerForDimension(dimensionID);

                if (var6 == null)
                {
                    System.err.println("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
                }

                final ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(var6.provider.getClass());

                if (type != null)
                {
                    WorldUtil.teleportEntity(var6, entity, dimensionID, type, transferInv);
                }
            }
        }
    }

    private static Entity teleportEntity(World var0, Entity var1, int var2, ITeleportType type, boolean transferInv)
    {
        final Entity var6 = var1.ridingEntity;

        if (var1.ridingEntity != null && var1.ridingEntity instanceof ISpaceship)
        {
            var1.mountEntity(var1.ridingEntity);
        }

        final boolean var7 = var1.worldObj != var0;
        var1.worldObj.updateEntityWithOptionalForce(var1, false);
        GCCorePlayerMP var8 = null;

        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP) var1;
            var8.closeScreen();

            if (var7)
            {
                var8.dimension = var2;
                var8.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(var8.dimension, (byte) var8.worldObj.difficultySetting, var0.getWorldInfo().getTerrainType(), var0.getHeight(), var8.theItemInWorldManager.getGameType()));

                if (var0.provider instanceof GCCoreWorldProviderSpaceStation && WorldUtil.registeredSpaceStations.contains(var8))
                {
                    var8.playerNetServerHandler.sendPacketToPlayer(GCCorePacketSpaceStationData.buildSpaceStationDataPacket(var0, var0.provider.dimensionId, var8));
                }

                ((WorldServer) var1.worldObj).getPlayerManager().removePlayer(var8);
            }

            var8.setNotUsingPlanetGui();
        }

        if (var7)
        {
            WorldUtil.removeEntityFromWorld(var1.worldObj, var1);
        }

        if (var7)
        {
            if (var1 instanceof EntityPlayerMP)
            {
                var1.setLocationAndAngles(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).x, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).y, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).z, var1.rotationYaw, var1.rotationPitch);
                ((WorldServer) var0).theChunkProviderServer.loadChunk(var0.getChunkFromChunkCoords(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).getIntX(), type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).getIntZ()).getChunkCoordIntPair().chunkXPos, var0.getChunkFromChunkCoords(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).getIntX(), type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).getIntZ()).getChunkCoordIntPair().chunkZPos);
            }
        }

        if (var7)
        {
            if (var1 instanceof EntityPlayer)
            {
                var1.setPosition(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).x, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).y, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).z);
            }
        }

        if (var7)
        {
            if (!(var1 instanceof EntityPlayer))
            {
                final NBTTagCompound var11 = new NBTTagCompound();
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
                var1.setLocationAndAngles(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).x, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).y, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).z, var1.rotationYaw, var1.rotationPitch);
            }
        }

        var0.updateEntityWithOptionalForce(var1, false);

        if (var7)
        {
            if (var1 instanceof EntityPlayer)
            {
                var1.setLocationAndAngles(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).x, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).y, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).z, var1.rotationYaw, var1.rotationPitch);
            }
        }

        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP) var1;

            if (var7)
            {
                var8.mcServer.getConfigurationManager().func_72375_a(var8, (WorldServer) var0);
            }

            var8.playerNetServerHandler.setPlayerLocation(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).x, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).y, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).z, var1.rotationYaw, var1.rotationPitch);

            GCLog.info("Server attempting to transfer player " + var8.username + " to dimension " + var0.provider.dimensionId);
        }

        var0.updateEntityWithOptionalForce(var1, false);

        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP) var1;

            if (type.useParachute())
            {
                var8.setParachute(true);
            }
        }

        if (var1 instanceof GCCorePlayerMP && var7)
        {
            var8 = (GCCorePlayerMP) var1;
            var8.theItemInWorldManager.setWorld((WorldServer) var0);
            var8.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(var8, (WorldServer) var0);
            var8.mcServer.getConfigurationManager().syncPlayerInventory(var8);
            final Iterator var9 = var8.getActivePotionEffects().iterator();

            while (var9.hasNext())
            {
                final PotionEffect var10 = (PotionEffect) var9.next();
                var8.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(var8.entityId, var10));
            }

            var8.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(var8.experience, var8.experienceTotal, var8.experienceLevel));
        }

        if (var8 != null)
        {
            var1.setLocationAndAngles(type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).x, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).y, type.getPlayerSpawnLocation((WorldServer) var1.worldObj, (EntityPlayerMP) var1).z, var1.rotationYaw, var1.rotationPitch);
        }
        else
        {
            var1.setLocationAndAngles(type.getEntitySpawnLocation((WorldServer) var1.worldObj, var1).x, type.getEntitySpawnLocation((WorldServer) var1.worldObj, var1).y, type.getEntitySpawnLocation((WorldServer) var1.worldObj, var1).z, var1.rotationYaw, var1.rotationPitch);
        }

        if (var1 instanceof GCCorePlayerMP)
        {
            var8 = (GCCorePlayerMP) var1;

            if (var8.rocketStacks != null && var8.rocketStacks.length > 0)
            {
                for (int i = 0; i < var8.rocketStacks.length; i++)
                {
                    if (transferInv)
                    {
                        if (var8.rocketStacks[i] == null)
                        {
                            if (i == var8.rocketStacks.length - 1)
                            {
                                var8.rocketStacks[i] = new ItemStack(GCCoreItems.spaceship, 1, var8.rocketType);
                            }
                            else if (i == var8.rocketStacks.length - 2)
                            {
                                var8.rocketStacks[i] = new ItemStack(GCCoreBlocks.landingPad, 9, 0);
                            }
                            else if (i == var8.rocketStacks.length - 3)
                            {
                                var8.rocketStacks[i] = var8.fuelDamage > 0 && var8.fuelDamage <= GCCoreItems.fuelCanister.getMaxDamage() ? new ItemStack(GCCoreItems.fuelCanister, 1, var8.fuelDamage) : null;
                            }
                        }
                    }
                    else
                    {
                        var8.rocketStacks[i] = null;
                    }
                }
            }

            if (transferInv && var8.chestSpawnCooldown == 0)
            {
                var8.chestSpawnVector = type.getParaChestSpawnLocation((WorldServer) var1.worldObj, var8, new Random());
                var8.chestSpawnCooldown = 200;
            }
        }

        if (var1 != null && var6 != null)
        {
            if (var1 instanceof EntityPlayerMP)
            {
                var0.updateEntityWithOptionalForce(var1, true);
            }

            var1.mountEntity(var6);
        }

        if (var1 instanceof EntityPlayerMP)
        {
            GameRegistry.onPlayerChangedDimension((EntityPlayerMP) var1);

            type.onSpaceDimensionChanged(var0, (EntityPlayerMP) var1);
        }

        return var1;
    }

    private static void removeEntityFromWorld(World var0, Entity var1)
    {
        if (var1 instanceof EntityPlayer)
        {
            final EntityPlayer var2 = (EntityPlayer) var1;
            var2.closeScreen();
            var0.playerEntities.remove(var2);
            var0.updateAllPlayersSleepingFlag();
            final int var3 = var1.chunkCoordX;
            final int var4 = var1.chunkCoordZ;

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

    public static SpaceStationRecipe getSpaceStationRecipe(int planetID)
    {
        final Iterator i = GalacticraftRegistry.getSpaceStationData().keySet().iterator();

        while (i.hasNext())
        {
            final Integer type = (Integer) i.next();

            if (type != null && type == planetID)
            {
                return GalacticraftRegistry.getSpaceStationData().get(type).getRecipeForSpaceStation();
            }
        }

        return null;
    }
}
