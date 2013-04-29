package micdoodle8.mods.galacticraft.API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSchematicList;
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
		if (!SchematicRegistry.schematicRecipes.contains(page))
		{
			SchematicRegistry.schematicRecipes.add(page);
		}
	}

	public static ISchematicPage getMatchingRecipeForItemStack(ItemStack stack)
	{
		for (final ISchematicPage schematic : SchematicRegistry.schematicRecipes)
		{
			final ItemStack requiredItem = schematic.getRequiredItem();

			if (requiredItem != null && stack != null && requiredItem.isItemEqual(stack))
			{
				return schematic;
			}
		}

		return null;
	}

	public static ISchematicPage getMatchingRecipeForID(int id)
	{
		for (final ISchematicPage schematic : SchematicRegistry.schematicRecipes)
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
			Collections.sort(player.unlockedSchematics);
			
			if (player != null && player.playerNetServerHandler != null)
			{
				player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketSchematicList.buildSchematicListPacket(player.unlockedSchematics));
			}
		}
	}

	public static ISchematicPage unlockNewPage(GCCorePlayerMP player, ItemStack stack)
	{
		if (stack != null)
		{
			final ISchematicPage schematic = SchematicRegistry.getMatchingRecipeForItemStack(stack);

			if (schematic != null)
			{
				SchematicRegistry.addUnlockedPage(player, schematic);

				return schematic;
			}
		}

		return null;
	}

    public static NBTTagList writeToNBT(GCCorePlayerMP player, NBTTagList par1NBTTagList)
    {
		Collections.sort(player.unlockedSchematics);

        for (final ISchematicPage page : player.unlockedSchematics)
        {
        	if (page != null)
        	{
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
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
            final NBTTagCompound nbttagcompound = (NBTTagCompound)par1NBTTagList.tagAt(i);

            final int j = nbttagcompound.getInteger("UnlockedPage");

            SchematicRegistry.addUnlockedPage(player, SchematicRegistry.getMatchingRecipeForID(j));
        }

		Collections.sort(player.unlockedSchematics);
    }

    @SideOnly(Side.CLIENT)
    private static ISchematicPage getNextSchematic(int currentIndex)
    {
    	final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();

    	for (int i = 0; i < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size(); i++)
    	{
    		idList.put(i, PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(i).getPageID());
    	}

    	final SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
    	final Iterator iterator = keys.iterator();

    	for (int count = 0; count < keys.size(); count++)
    	{
    		final int i = (Integer) iterator.next();
    		final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));

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
    	final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();

    	for (int i = 0; i < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size(); i++)
    	{
    		idList.put(i, PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(i).getPageID());
    	}

    	final SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
    	final Iterator iterator = keys.iterator();

    	for (int count = 0; count < keys.size(); count++)
    	{
    		final int i = (Integer) iterator.next();
    		final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));

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
    	final ISchematicPage page = SchematicRegistry.getNextSchematic(currentIndex);

    	if (page != null)
    	{
    		final int nextID = page.getPageID();
        	final int nextGuiID = page.getGuiID();

            final Object[] toSend = {nextID};
            FMLClientHandler.instance().getClient().currentScreen = null;

            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
            FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, nextGuiID, FMLClientHandler.instance().getClient().thePlayer.worldObj, (int)FMLClientHandler.instance().getClient().thePlayer.posX, (int)FMLClientHandler.instance().getClient().thePlayer.posY, (int)FMLClientHandler.instance().getClient().thePlayer.posZ);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void flipToLastPage(int currentIndex)
    {
    	final ISchematicPage page = SchematicRegistry.getLastSchematic(currentIndex);

    	if (page != null)
    	{
    		final int nextID = page.getPageID();
        	final int nextGuiID = page.getGuiID();

            final Object[] toSend = {nextID};
            FMLClientHandler.instance().getClient().currentScreen = null;

            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, toSend));
            FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, nextGuiID, FMLClientHandler.instance().getClient().thePlayer.worldObj, (int)FMLClientHandler.instance().getClient().thePlayer.posX, (int)FMLClientHandler.instance().getClient().thePlayer.posY, (int)FMLClientHandler.instance().getClient().thePlayer.posZ);
        }
    }
}
