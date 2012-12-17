package micdoodle8.mods.galacticraft.core;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryRocketBench;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class GCCoreUtil 
{
	public static void addCraftingRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			" YV",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', GCCoreItems.aluminumCanister,
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			"VY ",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', GCCoreItems.aluminumCanister,
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.heavyPlating, 2), new Object[] {
			"XYZ",
			"XYZ",
			"XYZ",
			'X', GCCoreItems.ingotTitanium,
			'Y', GCCoreItems.ingotCopper,
			'Z', GCCoreItems.ingotAluminum
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.rocketNoseCone, 1), new Object[] {
			" Y ",
			" X ",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', Block.torchRedstoneActive
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.rocketFins, 1), new Object[] {
			" Y ",
			"XYX",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', GCCoreItems.ingotAluminum
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9), new Object[] {
			"YYY",
			"XXX",
			'X', Block.blockSteel,
			'Y', GCCoreItems.ingotAluminum
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreBlocks.airDistributor, 1), new Object[] {
			"WXW",
			"YZY",
			"WXW",
			'W', GCCoreItems.ingotAluminum,
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', GCCoreItems.ingotTitanium
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreBlocks.blockAirCollector, 1), new Object[] {
			"WWW",
			"YXZ",
			"WVW",
			'W', GCCoreItems.oxygenConcentrator,
			'W', GCCoreItems.ingotTitanium,
			'X', GCCoreItems.aluminumCanister,
			'Y', GCCoreItems.airFan,
			'Z', GCCoreItems.airVent
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreBlocks.rocketBench, 1), new Object[] {
			"XXX",
			"YZY",
			"YYY",
			'X', GCCoreItems.ingotAluminum,
			'Y', Block.planks,
			'Z', Block.workbench
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 1), new Object[] {
			"XXX",
			"   ",
			"XXX",
			'X', Block.thinGlass
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.lightOxygenTankFull, 1), new Object[] {
			"Z",
			"X",
			"Y",
			'X', GCCoreItems.aluminumCanister,
			'Y', GCCoreItems.ingotCopper,
			'Z', new ItemStack(Block.cloth, 1, 5)
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.medOxygenTankFull, 1), new Object[] {
			"ZZ",
			"XX",
			"YY",
			'X', GCCoreItems.aluminumCanister,
			'Y', GCCoreItems.ingotAluminum,
			'Z', new ItemStack(Block.cloth, 1, 1)
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.heavyOxygenTankFull, 1), new Object[] {
			"ZZZ",
			"XXX",
			"YYY",
			'X', GCCoreItems.aluminumCanister,
			'Y', GCCoreItems.ingotTitanium,
			'Z', new ItemStack(Block.cloth, 1, 14)
		});
		// TODO sensor lens...
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] {
			"YYY",
			"Y Y",
			"XYX",
			'X', GCCoreItems.sensorLens,
			'Y', GCMoonItems.meteoricIronIngot
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] {
			"ZYZ",
			"YXY",
			"ZYZ",
			'X', Block.thinGlass,
			'Y', GCMoonItems.meteoricIronIngot,
			'Z', Item.redstone
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.aluminumCanister, 1), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', GCCoreItems.ingotAluminum
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.airVent, 1), new Object[] {
			"XX",
			"XX",
			'X', GCCoreItems.ingotAluminum
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.airFan, 1), new Object[] {
			"Z Z",
			" Y ",
			"ZXZ",
			'X', Item.redstone,
			'Y', GCCoreItems.ingotTitanium,
			'Z', GCCoreItems.ingotAluminum
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.oxygenMask, 1), new Object[] {
			"XXX",
			"XYX",
			"XXX",
			'X', Block.thinGlass,
			'Y', Item.helmetSteel
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] {
			"ZWZ",
			"WYW",
			"ZXZ",
			'W', GCCoreItems.ingotAluminum,
			'X', GCCoreItems.airVent,
			'Y', GCCoreItems.aluminumCanister,
			'Z', GCCoreItems.ingotTitanium
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumPickaxe, 1), new Object[] {
			"YYY",
			" X ",
			" X ",
			'Y', GCCoreItems.ingotTitanium,
			'X', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumAxe, 1), new Object[] {
			"YY ",
			"YX ",
			" X ",
			'Y', GCCoreItems.ingotTitanium,
			'X', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumAxe, 1), new Object[] {
			" YY",
			" XY",
			" X ",
			'Y', GCCoreItems.ingotTitanium,
			'X', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumHoe, 1), new Object[] {
			" YY",
			" X ",
			" X ",
			'Y', GCCoreItems.ingotTitanium,
			'X', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumHoe, 1), new Object[] {
			"YY ",
			" X ",
			" X ",
			'Y', GCCoreItems.ingotTitanium,
			'X', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumSpade, 1), new Object[] {
			" Y ",
			" X ",
			" X ",
			'Y', GCCoreItems.ingotTitanium,
			'X', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumSword, 1), new Object[] {
			" Y ",
			" Y ",
			" X ",
			'Y', GCCoreItems.ingotTitanium,
			'X', Item.stick
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumBoots, 1), new Object[] {
			"X X",
			"X X",
			'X', GCCoreItems.ingotTitanium
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumChestplate, 1), new Object[] {
			"X X",
			"XXX",
			"XXX",
			'X', GCCoreItems.ingotTitanium
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumLeggings, 1), new Object[] {
			"XXX",
			"X X",
			"X X",
			'X', GCCoreItems.ingotTitanium
		});
		GameRegistry.addRecipe(new ItemStack(GCCoreItems.titaniumHelmet, 1), new Object[] {
			"XXX",
			"X X",
			'X', GCCoreItems.ingotTitanium
		});
		GameRegistry.addShapelessRecipe(new ItemStack(GCCoreItems.titaniumHelmetBreathable, 1), new Object[] {
			GCCoreItems.titaniumHelmet, GCCoreItems.oxygenMask
		});
		GameRegistry.addShapelessRecipe(new ItemStack(GCCoreItems.sensorGlassesWithOxygenMask, 1), new Object[] {
			GCCoreItems.sensorGlasses, GCCoreItems.oxygenMask
		});
	}
	
	public static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 0, new ItemStack(GCCoreItems.ingotCopper), 0.1F);
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 1, new ItemStack(GCCoreItems.ingotAluminum), 0.3F);
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 2, new ItemStack(GCCoreItems.ingotTitanium), 1.0F);
	}
	
	public static Packet250CustomPayload createPacket(String channel, int packetID, Object[] input)
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream(bytes);
        try
        {
            data.write(packetID);

            if (input != null)
            {
                for (final Object obj : input)
                {
                    writeObjectToStream(obj, data);
                }
            }

        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        final Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;

        return packet;
    }

    public static Object[] readPacketData(DataInputStream data, Class[] packetDataTypes)
    {
        final List result = new ArrayList<Object>();

        try
        {
            for (final Class curClass : packetDataTypes)
            {
                result.add(readObjectFromStream(data, curClass));
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result.toArray();
    }
    
    private static void writeObjectToStream(Object obj, DataOutputStream data) throws IOException
    {
        final Class objClass = obj.getClass();

        if (objClass.equals(Boolean.class))
        {
            data.writeBoolean((Boolean) obj);
        }
        else if (objClass.equals(Byte.class))
        {
            data.writeByte((Byte) obj);
        }
        else if (objClass.equals(Integer.class))
        {
            data.writeInt((Integer) obj);
        }
        else if (objClass.equals(String.class))
        {
            data.writeUTF((String) obj);
        }
        else if (objClass.equals(Double.class))
        {
            data.writeDouble((Double) obj);
        }
        else if (objClass.equals(Float.class))
        {
            data.writeFloat((Float) obj);
        }
        else if (objClass.equals(Long.class))
        {
            data.writeLong((Long) obj);
        }
        else if (objClass.equals(Short.class))
        {
            data.writeShort((Short) obj);
        }
    }

    private static Object readObjectFromStream(DataInputStream data, Class curClass) throws IOException
    {
        if (curClass.equals(Boolean.class))
        {
            return data.readBoolean();
        }
        else if (curClass.equals(Byte.class))
        {
            return data.readByte();
        }
        else if (curClass.equals(Integer.class))
        {
            return data.readInt();
        }
        else if (curClass.equals(String.class))
        {
            return data.readUTF();
        }
        else if (curClass.equals(Double.class))
        {
            return data.readDouble();
        }
        else if (curClass.equals(Float.class))
        {
            return data.readFloat();
        }
        else if (curClass.equals(Long.class))
        {
            return data.readLong();
        }
        else if (curClass.equals(Short.class))
        {
            return data.readShort();
        }

        return null;
    }

    public static int readPacketID(DataInputStream data)
    {
        int result = -1;

        try
        {
            result = data.read();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
    
    public static int getDrainSpacing(ItemStack tank)
    {
		if (tank == null)
		{
			return 0;
		}
		
		if (tank.getItem().shiftedIndex == GCCoreItems.lightOxygenTankFull.shiftedIndex)
		{
			return 60;
		}
		else if (tank.getItem().shiftedIndex == GCCoreItems.medOxygenTankFull.shiftedIndex)
		{
			return 120;
		}
		else if (tank.getItem().shiftedIndex == GCCoreItems.heavyOxygenTankFull.shiftedIndex)
		{
			return 2000;
		}
		return 0;
    }
    
    public static GCCoreExplosion createNewExplosion(World world, Entity entity, double x, double y, double z, float size, boolean flaming)
    {
        final GCCoreExplosion explosion = new GCCoreExplosion(world, entity, x, y, z, size);
        explosion.isFlaming = flaming;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
    
    public static List<ItemStack> useless = new ArrayList();
    public static List<ItemStack> common = new ArrayList();
    public static List<ItemStack> uncommon = new ArrayList();
    public static List<ItemStack> rare = new ArrayList();
    public static List<ItemStack> ultrarare = new ArrayList();

    public static boolean generateChestContents(World var1, Random var2, int var3, int var4, int var5)
    {
        boolean var6 = true;
        var1.setBlockWithNotify(var3, var4, var5, Block.chest.blockID);
        int var7;

        for (var7 = 0; var7 < 4; ++var7)
        {
            var6 &= addItemToChest(var1, var2, var3, var4, var5, getCommonItem(var2));
        }

        for (var7 = 0; var7 < 2; ++var7)
        {
            var6 &= addItemToChest(var1, var2, var3, var4, var5, getUncommonItem(var2));
        }

        for (var7 = 0; var7 < 1; ++var7)
        {
            var6 &= addItemToChest(var1, var2, var3, var4, var5, getRareItem(var2));
        }

        return var6;
    }

    public static ItemStack getCommonItem(Random var1)
    {
        return var1.nextInt(4) == 0 ? getRandomItemFromList(useless, var1) : getRandomItemFromList(common, var1);
    }

    public static ItemStack getUncommonItem(Random var1)
    {
        return getRandomItemFromList(uncommon, var1);
    }

    public static ItemStack getRareItem(Random var1)
    {
        return var1.nextInt(4) == 0 ? getRandomItemFromList(ultrarare, var1) : getRandomItemFromList(rare, var1);
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
            final int var8 = findRandomInventorySlot(var7, var2);

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
		final Integer[] var1 = DimensionManager.getIDs();
		
		for (int i = 0; i < var1.length; i++)
		{
			if (DimensionManager.getProvider(var1[i]).getDimensionName() != null && DimensionManager.getProvider(var1[i]).getDimensionName().equals(par1String))
			{
				return DimensionManager.getProvider(var1[i]);
			}
		}
		
		return null;
	}
	
	public static int getAmountOfPossibleProviders(Integer[] ids)
	{
		int amount = 0;
		
		for (int i = 0; i < ids.length; i++)
		{
    		if (DimensionManager.getProvider(ids[i]) instanceof IGalacticraftWorldProvider || DimensionManager.getProvider(ids[i]).dimensionId == 0)
    		{
    			amount++;
    		}
		}
		
		return amount;
	}
	
	public static HashMap getArrayOfPossibleDimensions(Integer[] ids)
	{
		final HashMap map = new HashMap();
		
		for (int i = 0; i < ids.length; i++)
		{
    		if (DimensionManager.getProvider(ids[i]) != null && (DimensionManager.getProvider(ids[i]) instanceof IGalacticraftWorldProvider || DimensionManager.getProvider(ids[i]).dimensionId == 0))
    		{
    			map.put(DimensionManager.getProvider(ids[i]).getDimensionName(), DimensionManager.getProvider(ids[i]).dimensionId);
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
	
	public static ItemStack findMatchingSpaceshipRecipe(GCCoreInventoryRocketBench inventoryRocketBench)
	{
		final ItemStack[] slots = new ItemStack[18];
		
		for (int i = 0; i < 18; i++)
		{
			slots[i] = inventoryRocketBench.getStackInSlot(i + 1);
		}
		
		if (slots[0] != null && slots[1] != null && slots[2] != null && slots[3] != null && slots[4] != null && slots[5] != null && slots[6] != null && slots[7] != null && slots[8] != null && slots[9] != null && slots[10] != null && slots[11] != null && slots[12] != null && slots[13] != null)
		{
			if (slots[0].getItem().shiftedIndex == GCCoreItems.rocketNoseCone.shiftedIndex)
			{
				int platesInPlace = 0;
				
				for (int i = 1; i < 9; i++)
				{
					if (slots[i].getItem().shiftedIndex == GCCoreItems.heavyPlating.shiftedIndex)
					{
						platesInPlace++;
					}
				}
				
				if (platesInPlace == 8)
				{
					if (slots[9].getItem().shiftedIndex == GCCoreItems.rocketFins.shiftedIndex && slots[10].getItem().shiftedIndex == GCCoreItems.rocketFins.shiftedIndex)
					{
						if (slots[12].getItem().shiftedIndex == GCCoreItems.rocketFins.shiftedIndex && slots[13].getItem().shiftedIndex == GCCoreItems.rocketFins.shiftedIndex)
						{
							if (slots[11].getItem().shiftedIndex == GCCoreItems.rocketEngine.shiftedIndex)
							{
								return new ItemStack(GCCoreItems.spaceship);
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public static int convertTo32BitColor(int a, int r, int b, int g)
	{
        a = a << 24;
        r = r << 16;
        g = g << 8;
        
        return a | r | g | b;
	}
	
	public static List getPlayersOnPlanet(IMapPlanet planet)
	{
		final List list = new ArrayList();
		
		for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().worldServers.length; i++)
		{
			final World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[i];
			
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
}
