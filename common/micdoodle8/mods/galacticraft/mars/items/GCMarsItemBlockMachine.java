package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import basiccomponents.common.block.BlockBasicMachine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsItemBlockMachine extends ItemBlock
{
    public GCMarsItemBlockMachine(int id)
    {
        super(id);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int metadata = 0;

        if (itemstack.getItemDamage() >= BlockBasicMachine.ELECTRIC_FURNACE_METADATA)
        {
            metadata = 2;
        }
        else if (itemstack.getItemDamage() >= BlockBasicMachine.BATTERY_BOX_METADATA)
        {
            metadata = 1;
        }

        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + metadata;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName()
    {
        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + ".0";
    }
}
