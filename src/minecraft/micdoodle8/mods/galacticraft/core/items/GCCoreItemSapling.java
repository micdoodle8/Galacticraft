package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import mekanism.api.EnumColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemSapling extends ItemBlock
{
    public GCCoreItemSapling(int par1)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
	public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getIconFromDamage(int par1)
    {
        return Block.sapling.getBlockTextureFromSideAndMetadata(0, par1);
    }

    @Override
	public String getItemNameIS(ItemStack par1ItemStack)
    {
        int var2 = par1ItemStack.getItemDamage();

        if (var2 < 0 || var2 >= BlockSapling.WOOD_TYPES.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + BlockSapling.WOOD_TYPES[var2];
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	par3List.add("\u00a7cRequires water blocks to grow");
    }
}
