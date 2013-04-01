package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GCCoreGuiSchematicPages 
{
	public static ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();

	public static ArrayList<ISchematicPage> schematicRecipes = new ArrayList<ISchematicPage>();
	
	public static void addUnlockedPage(ISchematicPage page)
	{
		if (!unlockedSchematics.contains(page))
		{
			unlockedSchematics.add(page);
		}
	}
	
	public static void registerSchematicRecipe(ItemStack stack, ISchematicPage page)
	{
		schematicRecipes.add(page);
	}
	
	public static ISchematicPage getMatchingRecipeForID(int id)
	{
		for (ISchematicPage schematic : schematicRecipes)
		{
			if (schematic.getPageID() == id)
			{
				return schematic;
			}
		}
		
		return null;
	}
	
	public static GuiScreen getMatchingScreenForItemStack(ItemStack stack)
	{
		for (ISchematicPage schematic : schematicRecipes)
		{
			if (schematic.getRequiredItem().isItemEqual(stack))
			{
				return schematic.getResultScreen();
			}
		}
		
		return null;
	}

    public NBTTagList writeToNBT(NBTTagList par1NBTTagList)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        
        for (ISchematicPage page : unlockedSchematics)
        {
        	nbttagcompound.setInteger("UnlockedPage", page.getPageID());
        	par1NBTTagList.appendTag(nbttagcompound);
        }
        
        return par1NBTTagList;
    }

    public void readFromNBT(NBTTagList par1NBTTagList)
    {
    	unlockedSchematics = new ArrayList<ISchematicPage>();

        for (int i = 0; i < par1NBTTagList.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)par1NBTTagList.tagAt(i);

            int j = nbttagcompound.getInteger("UnlockedPage");
            
            this.unlockedSchematics.add(this.getMatchingRecipeForID(j));
        }
    }
}
