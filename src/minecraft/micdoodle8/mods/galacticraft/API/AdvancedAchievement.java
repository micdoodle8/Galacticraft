package micdoodle8.mods.galacticraft.API;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class AdvancedAchievement extends Achievement
{
    public final Achievement[] parentAchievements;
    
    public AdvancedAchievement(int par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement[] par6Achievement)
    {
        super(par1, par2Str, par3, par4, par5ItemStack, par6Achievement[0]);
        this.parentAchievements = par6Achievement;
    }
}
