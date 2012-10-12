package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GCAdvancedAchievement;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.AchievementPage;

public class GCMarsAchievementList 
{
	// regular page achievs:
	public static GCAdvancedAchievement mineCopper;
	public static GCAdvancedAchievement mineTitanium;
	public static GCAdvancedAchievement mineAluminum;
	
	// galacticraft page achievs:
	public static GCAdvancedAchievement buildSpaceship;
	public static GCAdvancedAchievement travelToMars;
	
	public static AchievementPage achievmentPage;
	
	private static int baseID;
	
	public static void initAchievs()
	{
		baseID = GCMarsConfigManager.idAchievBase;
		mineCopper = (GCAdvancedAchievement) new GCAdvancedAchievement(baseID, "MineCopper", 1, 2, new ItemStack(GCMarsBlocks.blockOres, 1, 5), new Achievement[] {AchievementList.acquireIron}).registerAchievement();
		mineTitanium = (GCAdvancedAchievement) new GCAdvancedAchievement(baseID + 1, "MineTitanium", -2, 3, new ItemStack(GCMarsBlocks.blockOres, 1, 7), new Achievement[] {AchievementList.diamonds}).registerAchievement();
		mineAluminum = (GCAdvancedAchievement) new GCAdvancedAchievement(baseID + 2, "MineAluminum", -1, 2, new ItemStack(GCMarsBlocks.blockOres, 1, 3), new Achievement[] {AchievementList.diamonds}).registerAchievement();
		
		buildSpaceship = (GCAdvancedAchievement) new GCAdvancedAchievement(baseID + 3, "BuildSpaceship", 0, 0, new ItemStack(Block.workbench), new Achievement[] {mineCopper, mineTitanium, mineAluminum}).registerAchievement();
		travelToMars = (GCAdvancedAchievement) new GCAdvancedAchievement(baseID + 4, "TravelToMars", 2, 1, new ItemStack(GCMarsItems.spaceship), new Achievement[] {buildSpaceship}).registerAchievement().setSpecial();
		
		achievmentPage = new AchievementPage("Galacticraft", buildSpaceship, travelToMars);
		
		AchievementPage.registerAchievementPage(achievmentPage);
	}
}
