package micdoodle8.mods.galacticraft.core;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.client.gui.ISchematicPage;
import micdoodle8.mods.galacticraft.core.client.gui.ISchematicResultPage;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GCCoreSchematicRegistry 
{
	public static ArrayList<ISchematicPage> schematicRecipes = new ArrayList<ISchematicPage>();
	
	public static void registerSchematicRecipe(ISchematicPage page)
	{
		schematicRecipes.add(page);
	}
	
	public static ISchematicPage getMatchingRecipeForItemStack(ItemStack stack)
	{
		for (ISchematicPage schematic : GCCoreSchematicRegistry.schematicRecipes)
		{
			if (schematic.getRequiredItem().isItemEqual(stack))
			{
				return schematic;
			}
		}
		
		return null;
	}
	
	public static ISchematicPage getMatchingRecipeForID(int id)
	{
		for (ISchematicPage schematic : GCCoreSchematicRegistry.schematicRecipes)
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
		ISchematicPage schematic = GCCoreSchematicRegistry.getMatchingRecipeForItemStack(stack);
		
		if (schematic != null)
		{
			unlockSchematic(player, schematic);
			
			return schematic;
		}
		
		return null;
	}
	
	private static void unlockSchematic(GCCorePlayerMP player, ISchematicPage schematic)
	{
		player.unlockedSchematics.add(schematic);
		
		if (schematic.getResultScreen() instanceof ISchematicResultPage)
		{
			((ISchematicResultPage) schematic.getResultScreen()).setPageIndex(player.unlockedSchematics.size());
		}
	}

    public static NBTTagList writeToNBT(GCCorePlayerMP player, NBTTagList par1NBTTagList)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        
        for (ISchematicPage page : player.unlockedSchematics)
        {
        	nbttagcompound.setInteger("UnlockedPage", page.getPageID());
        	par1NBTTagList.appendTag(nbttagcompound);
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
            
            player.unlockedSchematics.add(GCCoreSchematicRegistry.getMatchingRecipeForID(j));
        }
    }
}
