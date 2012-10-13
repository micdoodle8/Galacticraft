package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.API.AdvancedAchievement;
import micdoodle8.mods.galacticraft.core.GCCoreItems;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.AchievementPage;

public class GCMarsAchievementList 
{
	// galacticraft page achievs:
	public static AdvancedAchievement buildSpaceship;
	public static AdvancedAchievement travelToMars;
	
	public static AchievementPage achievmentPage;
	
	private static int baseID;
	
	public static void initAchievs()
	{
		baseID = 2853;
		buildSpaceship = (AdvancedAchievement) new AdvancedAchievement(baseID + 1, "BuildSpaceship", 0, 0, new ItemStack(Block.workbench), new Achievement[] {null}).registerAchievement();
		travelToMars = (AdvancedAchievement) new AdvancedAchievement(baseID + 2, "TravelToMars", 2, 1, new ItemStack(GCCoreItems.spaceship), new Achievement[] {buildSpaceship}).registerAchievement().setSpecial();
		
		achievmentPage = new AchievementPage("Galacticraft Mars", buildSpaceship, travelToMars);
		
		AchievementPage.registerAchievementPage(achievmentPage);
	}
}
