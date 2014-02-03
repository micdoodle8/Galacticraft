package micdoodle8.mods.galacticraft.core.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreBookPage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.w3c.dom.Document;

import cpw.mods.fml.common.FMLLog;

/**
 * GCCoreManualUtil.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreManualUtil
{
	private static Map<String, ItemStack> manualIcons = new HashMap<String, ItemStack>();
	private static Map<String, ItemStack[]> recipeIcons = new HashMap<String, ItemStack[]>();
	private static ItemStack defaultStack = new ItemStack(Item.appleRed);

	public static Document readManual(String manualLocation, DocumentBuilderFactory docBuilderFactory)
	{
		try
		{
			InputStream stream = GalacticraftCore.class.getResourceAsStream(manualLocation);
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			return doc;
		}
		catch (Exception e)
		{
			FMLLog.severe("Encountered problem while trying to read Galacticraft manual file...");
			e.printStackTrace();
		}

		return null;
	}

	public static void registerIcon(String iconName, ItemStack stack)
	{
		GCCoreManualUtil.manualIcons.put(iconName, stack);
	}

	public static ItemStack getIcon(String iconName)
	{
		ItemStack preferredStack = GCCoreManualUtil.manualIcons.get(iconName);
		return preferredStack != null ? preferredStack : GCCoreManualUtil.defaultStack;
	}

	public static void registerManualRecipe(String name, ItemStack output, ItemStack... input)
	{
		ItemStack[] recipe = new ItemStack[10];
		recipe[0] = output;
		System.arraycopy(input, 0, recipe, 1, 9);
		GCCoreManualUtil.recipeIcons.put(name, recipe);
	}

	public static Map<String, Class<? extends GCCoreBookPage>> pageClasses = new HashMap<String, Class<? extends GCCoreBookPage>>();

	public static void registerManualPage(String type, Class<? extends GCCoreBookPage> clazz)
	{
		GCCoreManualUtil.pageClasses.put(type, clazz);
	}

	public static Class<? extends GCCoreBookPage> getPageClass(String type)
	{
		return GCCoreManualUtil.pageClasses.get(type);
	}

	public static ItemStack[] getRecipeIcons(String name)
	{
		return GCCoreManualUtil.recipeIcons.get(name);
	}
}
