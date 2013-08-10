package micdoodle8.mods.galacticraft.mars.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class GCMarsItemTrowel extends ItemTool
{
    public static final Block[] blocksEffectiveAgainst = new Block[] { GCMarsBlocks.rock };

    public GCMarsItemTrowel(int id, EnumToolMaterial toolMaterial)
    {
        super(id, 1.0F, toolMaterial, blocksEffectiveAgainst);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }
    
    public boolean canHarvestBlock(Block par1Block)
    {
        return par1Block != null && par1Block.blockID == GCMarsBlocks.rock.blockID;
    }
    
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
        return par2Block != null && this.canHarvestBlock(par2Block) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(par1ItemStack, par2Block);
    }
}
