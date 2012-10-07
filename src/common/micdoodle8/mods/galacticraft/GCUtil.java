package micdoodle8.mods.galacticraft;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraftforge.common.DimensionManager;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCUtil 
{
	public static Packet250CustomPayload createPacket(String channel, int packetID, Object[] input)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        try
        {
            data.write(packetID);

            if (input != null)
            {
                for (Object obj : input)
                {
                    writeObjectToStream(obj, data);
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;

        return packet;
    }

    public static Object[] readPacketData(DataInputStream data, Class[] packetDataTypes)
    {
        List result = new ArrayList<Object>();

        try
        {
            for (Class curClass : packetDataTypes)
            {
                result.add(readObjectFromStream(data, curClass));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return result.toArray();
    }
    
    private static void writeObjectToStream(Object obj, DataOutputStream data) throws IOException
    {
        Class objClass = obj.getClass();

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
        catch (IOException e)
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
		
		if (tank.getItem().shiftedIndex == GCItems.lightOxygenTankFull.shiftedIndex)
		{
			return 60;
		}
		else if (tank.getItem().shiftedIndex == GCItems.medOxygenTankFull.shiftedIndex)
		{
			return 120;
		}
		else if (tank.getItem().shiftedIndex == GCItems.heavyOxygenTankFull.shiftedIndex)
		{
			return 2000;
		}
		return 0;
    }
    
    public static GCExplosion createNewExplosion(World world, Entity entity, double x, double y, double z, float size, boolean flaming)
    {
        GCExplosion explosion = new GCExplosion(world, entity, x, y, z, size);
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
        TileEntityChest var7 = (TileEntityChest)var1.getBlockTileEntity(var3, var4, var5);

        if (var7 != null)
        {
            int var8 = findRandomInventorySlot(var7, var2);

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
            int var4 = var2.nextInt(var1.getSizeInventory());

            if (var1.getStackInSlot(var4) == null)
            {
                return var4;
            }
        }

        return -1;
    }
    
    /**
     *  Checks if a block is in the structure bounding box, then sets the block
     * 
     * @param world The World to spawn the block in
     * @param i x-pos
     * @param j y-pos
     * @param k z-pos
     * @param ID block ID to set
     * @param meta block metadata, 0 if no metadata
     * @param notify notify adjacent blocks of change?
     * @param bb the bounding box to check
     * @param if the block set needs to be inside the walls
     * @return true if block was set, false if not
     */
	public static boolean checkAndSetBlock(World world, int i, int j, int k, int ID, int meta, boolean notify, StructureBoundingBox bb)
	{
		if (!notify)
		{
			if (bb.isVecInside(i, j, k))
			{
				world.setBlockAndMetadata(i, j, k, ID, meta);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if (bb.isVecInside(i, j, k))
			{
				world.setBlockAndMetadataWithNotify(i, j, k, ID, meta);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public static WorldProvider getProviderForName(String par1String)
	{
		Integer[] var1 = DimensionManager.getIDs();
		
		for (int i = 0; i < var1.length; i++)
		{
			if (DimensionManager.getProvider(var1[i]).getDimensionName() != null && DimensionManager.getProvider(var1[i]).getDimensionName() == par1String)
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
    		if (DimensionManager.getProvider(ids[i]) instanceof GCWorldProvider || DimensionManager.getProvider(ids[i]).dimensionId == 0)
    		{
    			amount++;
    		}
		}
		
		return amount;
	}
	
	public static HashMap getArrayOfPossibleDimensions(Integer[] ids)
	{
		HashMap map = new HashMap();
//		Integer[] array = new Integer[getAmountOfPossibleProviders(ids)];
		
		for (int i = 0; i < ids.length; i++)
		{
    		if (DimensionManager.getProvider(ids[i]) instanceof GCWorldProvider || DimensionManager.getProvider(ids[i]).dimensionId == 0)
    		{
    			map.put(DimensionManager.getProvider(ids[i]).getDimensionName(), DimensionManager.getProvider(ids[i]).dimensionId);
    		}
		}
		
		return map;
	}
	
	public static double getSpaceshipFailChance(EntityPlayer player)
	{
		Double level = Double.valueOf(player.experienceLevel);
		
		if (level <= 50.0D)
		{
			return 25.0D - (level / 2.0D);
		}
		else
		{
			return 0.0;
		}
	}
}
