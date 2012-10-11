package micdoodle8.mods.galacticraft;

import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AchievementMap;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StatBase;
import net.minecraft.src.StatList;

public class GCAchievement extends Achievement
{
    public final Achievement[] parentAchievements;
    
    public GCAchievement(int par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement[] par6Achievement)
    {
        super(par1, par2Str, par3, par4, par5ItemStack, par6Achievement[0]);
        this.parentAchievements = par6Achievement;
    }
}
