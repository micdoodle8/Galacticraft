package micdoodle8.mods.galacticraft.API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SchematicRegistry 
{
	public static ArrayList<ISchematicPage> schematicRecipes = new ArrayList<ISchematicPage>();
	
	public static void registerSchematicRecipe(ISchematicPage page)
	{
		if (!schematicRecipes.contains(page))
		{
			schematicRecipes.add(page);
		}
	}
	
	public static ISchematicPage getMatchingRecipeForItemStack(ItemStack stack)
	{
		for (ISchematicPage schematic : SchematicRegistry.schematicRecipes)
		{
			ItemStack requiredItem = schematic.getRequiredItem();
			
			if (requiredItem != null && stack != null && requiredItem.isItemEqual(stack))
			{
				return schematic;
			}
		}
		
		return null;
	}
	
	public static ISchematicPage getMatchingRecipeForID(int id)
	{
		for (ISchematicPage schematic : SchematicRegistry.schematicRecipes)
		{
			if (schematic.getPageID() == id)
			{
				return schematic;
			}
		}
		
		return null;
	}
	
	public static void addUnlockedPage(GCCorePlayerMP player, ISchematicPage page)
	{
		if (!player.unlockedSchematics.contains(page))
		{
			player.unlockedSchematics.add(page);
		}
	}
	
	public static ISchematicPage unlockNewPage(GCCorePlayerMP player, ItemStack stack)
	{
		if (stack != null)
		{
			ISchematicPage schematic = SchematicRegistry.getMatchingRecipeForItemStack(stack);
			
			if (schematic != null)
			{
				addUnlockedPage(player, schematic);
				
				return schematic;
			}
		}
		
		return null;
	}

    public static NBTTagList writeToNBT(GCCorePlayerMP player, NBTTagList par1NBTTagList)
    {
		Collections.sort(player.unlockedSchematics);
        
        for (ISchematicPage page : player.unlockedSchematics)
        {
        	if (page != null)
        	{
                NBTTagCompound nbttagcompound = new NBTTagCompound();
            	nbttagcompound.setInteger("UnlockedPage", page.getPageID());
            	par1NBTTagList.appendTag(nbttagcompound);
        	}
        }
        
        return par1NBTTagList;
    }

    public static void readFromNBT(GCCorePlayerMP player, NBTTagList par1NBTTagList)
    {
    	player.unlockedSchematics = new ArrayList<ISchematicPage>();

        for (int i = 0; i < par1NBTTagList.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)par1NBTTagList.tagAt(i);

            int j = nbttagcompound.getInteger("UnlockedPage");
            
            addUnlockedPage(player, SchematicRegistry.getMatchingRecipeForID(j));
        }
		
		Collections.sort(player.unlockedSchematics);
    }

    @SideOnly(Side.CLIENT)
    private static ISchematicPage getNextSchematic(int currentIndex)
    {
    	HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();
    	
    	for (int i = 0; i < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size(); i++)
    	{
    		idList.put(i, PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(i).getPageID());
    	}
    	
    	SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
    	Iterator iterator = keys.iterator();
    	
    	for (int count = 0; count < keys.size(); count++)
    	{
    		int i = (Integer) iterator.next();
    		ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));
    		
    		if (page.getPageID() == currentIndex)
    		{
        		if (count + 1 < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size())
        		{
        			return PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(count + 1);
        		}
        		else
        		{
        			return null;
        		}
    		}
    	}
    	
    	return null;
    }

    @SideOnly(Side.CLIENT)
    private static ISchematicPage getLastSchematic(int currentIndex)
    {
    	HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();
    	
    	for (int i = 0; i < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size(); i++)
    	{
    		idList.put(i, PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(i).getPageID());
    	}
    	
    	SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
    	Iterator iterator = keys.iterator();
    	
    	for (int count = 0; count < keys.size(); count++)
    	{
    		int i = (Integer) iterator.next();
    		ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));
    		
    		if (page.getPageID() == currentIndex)
    		{
        		if (count - 1 >= 0)
        		{
        			return PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(count - 1);
        		}
        		else
        		{
        			return null;
        		}
    		}
    	}
    	
    	return null;
    }
    
    @SideOnly(Side.CLIENT)
    public static void flipToNextPage(int currentIndex)
    {
    	ISchematicPage page = SchematicRegistry.getNextSchematic(currentIndex);
    	
    	if (page != null)
    	{
    		int nextID = page.getPageID();
        	int nextGuiID = page.getGuiID();
        	
            final Object[] toSend = {nextID};
            FMLClientHandler.instance().getClient().currentScreen = null;

            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
            FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, nextGuiID, FMLClientHandler.instance().getClient().thePlayer.worldObj, (int)FMLClientHandler.instance().getClient().thePlayer.posX, (int)FMLClientHandler.instance().getClient().thePlayer.posY, (int)FMLClientHandler.instance().getClient().thePlayer.posZ);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void flipToLastPage(int currentIndex)
    {
    	ISchematicPage page = SchematicRegistry.getLastSchematic(currentIndex);
    	
    	if (page != null)
    	{
    		int nextID = page.getPageID();
        	int nextGuiID = page.getGuiID();
        	
            final Object[] toSend = {nextID};
            FMLClientHandler.instance().getClient().currentScreen = null;

            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
            FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, nextGuiID, FMLClientHandler.instance().getClient().thePlayer.worldObj, (int)FMLClientHandler.instance().getClient().thePlayer.posX, (int)FMLClientHandler.instance().getClient().thePlayer.posY, (int)FMLClientHandler.instance().getClient().thePlayer.posZ);
        }
    }
}
