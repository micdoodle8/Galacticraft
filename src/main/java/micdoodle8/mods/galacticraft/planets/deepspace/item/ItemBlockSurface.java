package micdoodle8.mods.galacticraft.planets.deepspace.item;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockSurface;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSurface extends ItemBlock
{
    public ItemBlockSurface(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String name = BlockSurface.EnumBlockSurface.values()[itemstack.getItemDamage()].getName();

        return this.getBlock().getUnlocalizedName() + "." + name;
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.getBlock().getUnlocalizedName() + ".0";
    }
}
