package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.API.AdvancedAchievement;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
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
		GCMarsAchievementList.baseID = 2853;
		GCMarsAchievementList.buildSpaceship = (AdvancedAchievement) new AdvancedAchievement(GCMarsAchievementList.baseID + 1, "BuildSpaceship", 0, 0, new ItemStack(Block.workbench), new Achievement[] {null}).registerAchievement();
		GCMarsAchievementList.travelToMars = (AdvancedAchievement) new AdvancedAchievement(GCMarsAchievementList.baseID + 2, "TravelToMars", 2, 1, new ItemStack(GCCoreItems.spaceship), new Achievement[] {GCMarsAchievementList.buildSpaceship}).registerAchievement().setSpecial();
		
		GCMarsAchievementList.achievmentPage = new AchievementPage("Galacticraft Mars", GCMarsAchievementList.buildSpaceship, GCMarsAchievementList.travelToMars);
		
		AchievementPage.registerAchievementPage(GCMarsAchievementList.achievmentPage);
	}
}
