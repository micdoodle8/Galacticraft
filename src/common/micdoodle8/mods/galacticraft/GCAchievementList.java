package micdoodle8.mods.galacticraft;

import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.AchievementPage;

public class GCAchievementList 
{
	// regular page achievs:
	public static GCAchievement mineCopper;
	public static GCAchievement mineTitanium;
	public static GCAchievement mineAluminum;
	
	// galacticraft page achievs:
	public static GCAchievement buildSpaceship;
	public static GCAchievement travelToMars;
	
	public static AchievementPage achievmentPage;
	
	private static int baseID;
	
	public static void initAchievs()
	{
		baseID = GCConfigManager.idAchievBase;
		mineCopper = (GCAchievement) new GCAchievement(baseID, "MineCopper", 1, 2, new ItemStack(GCBlocks.blockOres, 1, 5), new Achievement[] {AchievementList.acquireIron}).registerAchievement();
		mineTitanium = (GCAchievement) new GCAchievement(baseID + 1, "MineTitanium", -2, 3, new ItemStack(GCBlocks.blockOres, 1, 7), new Achievement[] {AchievementList.diamonds}).registerAchievement();
		mineAluminum = (GCAchievement) new GCAchievement(baseID + 2, "MineAluminum", -1, 2, new ItemStack(GCBlocks.blockOres, 1, 3), new Achievement[] {AchievementList.diamonds}).registerAchievement();
		
		buildSpaceship = (GCAchievement) new GCAchievement(baseID + 3, "BuildSpaceship", 0, 0, new ItemStack(Block.workbench), new Achievement[] {mineCopper, mineTitanium, mineAluminum}).registerAchievement();
		travelToMars = (GCAchievement) new GCAchievement(baseID + 4, "TravelToMars", 2, 1, new ItemStack(GCItems.spaceship), new Achievement[] {buildSpaceship}).registerAchievement().setSpecial();
		
		achievmentPage = new AchievementPage("Galacticraft", buildSpaceship, travelToMars);
		
		AchievementPage.registerAchievementPage(achievmentPage);
	}
}
