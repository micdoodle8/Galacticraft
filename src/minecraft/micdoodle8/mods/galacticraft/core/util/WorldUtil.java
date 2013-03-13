package micdoodle8.mods.galacticraft.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IInterplanetaryObject;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreTeleporter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class WorldUtil
{
    public static List<ItemStack> useless = new ArrayList();
    public static List<ItemStack> common = new ArrayList();
    public static List<ItemStack> uncommon = new ArrayList();
    public static List<ItemStack> rare = new ArrayList();
    public static List<ItemStack> ultrarare = new ArrayList();

    public static boolean generateChestContents(World var1, Random var2, int var3, int var4, int var5)
    {
        boolean var6 = true;
        var1.setBlockAndMetadataWithNotify(var3, var4, var5, Block.chest.blockID, 0, 3);
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
		final Integer[] var1 = DimensionManager.getStaticDimensionIDs();

		for (final Integer element : var1)
		{
			if (WorldProvider.getProviderForDimension(element) != null && WorldProvider.getProviderForDimension(element).getDimensionName() != null && WorldProvider.getProviderForDimension(element).getDimensionName().equals(par1String))
			{
				return WorldProvider.getProviderForDimension(element);
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
		final HashMap map = new HashMap();

		for (final Integer id : ids)
		{
    		if (WorldProvider.getProviderForDimension(id) != null && (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider || WorldProvider.getProviderForDimension(id).dimensionId == 0))
    		{
    			map.put(WorldProvider.getProviderForDimension(id).getDimensionName(), WorldProvider.getProviderForDimension(id).dimensionId);
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
	
    public static void travelToDimension(Entity entity, WorldServer world, int par1)
    {
        if (!entity.worldObj.isRemote && !entity.isDead)
        {
        	entity.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int j = entity.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(-1);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(0);
            entity.dimension = par1;
            entity.worldObj.removeEntity(entity);
            entity.isDead = false;
            entity.worldObj.theProfiler.startSection("reposition");
            
    		for (int i = 0; i < world.customTeleporters.size(); i++)
    		{
    			if (world.customTeleporters.get(i) instanceof GCCoreTeleporter)
    			{
                    transferEntityToWorld(entity, j, worldserver, worldserver1, world.customTeleporters.get(i));
    			}
    		}
    		
            entity.worldObj.theProfiler.endStartSection("reloading");
            Entity entity2 = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);

            if (entity2 != null)
            {
            	entity2.copyDataFrom(entity, true);
                worldserver1.spawnEntityInWorld(entity2);
            }

            entity.isDead = true;
            entity.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            entity.worldObj.theProfiler.endSection();
        }
    }

    public static void transferEntityToWorld(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer, Teleporter teleporter)
    {
        WorldProvider pOld = par3WorldServer.provider;
        WorldProvider pNew = par4WorldServer.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double d0 = par1Entity.posX * moveFactor;
        double d1 = par1Entity.posZ * moveFactor;
        double d3 = par1Entity.posX;
        double d4 = par1Entity.posY;
        double d5 = par1Entity.posZ;
        float f = par1Entity.rotationYaw;
        par3WorldServer.theProfiler.startSection("moving");

        if (par1Entity.dimension == 1)
        {
            ChunkCoordinates chunkcoordinates;

            if (par2 == 1)
            {
                chunkcoordinates = par4WorldServer.getSpawnPoint();
            }
            else
            {
                chunkcoordinates = par4WorldServer.getEntrancePortalLocation();
            }

            d0 = (double)chunkcoordinates.posX;
            par1Entity.posY = (double)chunkcoordinates.posY;
            d1 = (double)chunkcoordinates.posZ;
            par1Entity.setLocationAndAngles(d0, par1Entity.posY, d1, 90.0F, 0.0F);

            if (par1Entity.isEntityAlive())
            {
                par3WorldServer.updateEntityWithOptionalForce(par1Entity, false);
            }
        }

        par3WorldServer.theProfiler.endSection();

        if (par2 != 1)
        {
            par3WorldServer.theProfiler.startSection("placing");
            d0 = (double)MathHelper.clamp_int((int)d0, -29999872, 29999872);
            d1 = (double)MathHelper.clamp_int((int)d1, -29999872, 29999872);

            if (par1Entity.isEntityAlive())
            {
                par4WorldServer.spawnEntityInWorld(par1Entity);
                par1Entity.setLocationAndAngles(d0, par1Entity.posY, d1, par1Entity.rotationYaw, par1Entity.rotationPitch);
                par4WorldServer.updateEntityWithOptionalForce(par1Entity, false);
                teleporter.placeInPortal(par1Entity, d3, d4, d5, f);
            }

            par3WorldServer.theProfiler.endSection();
        }

        par1Entity.setWorld(par4WorldServer);
        
        if (par1Entity instanceof IInterplanetaryObject)
        {
        	((IInterplanetaryObject) par1Entity).onPlanetChanged();
        }
    }
}
