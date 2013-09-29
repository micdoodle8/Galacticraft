package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine2;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemBlockMachine extends ItemBlock
{
    public GCCoreItemBlockMachine(int id)
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
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int metadata = 0;
        
        if (this.getBlockID() == GCCoreBlocks.blockMachineBase.blockID)
        {
            if (itemstack.getItemDamage() >= GCCoreBlockMachine.COMPRESSOR_METADATA)
            {
                metadata = 3;
            }
            else if (itemstack.getItemDamage() >= GCCoreBlockMachine.ELECTRIC_FURNACE_METADATA)
            {
                metadata = 2;
            }
            else if (itemstack.getItemDamage() >= GCCoreBlockMachine.BATTERY_BOX_METADATA)
            {
                metadata = 1;
            }
        }
        else
        {
            if (itemstack.getItemDamage() >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
            {
                metadata = 4;
            }
        }

        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + metadata;
    }

    @Override
    public String getUnlocalizedName()
    {
        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + ".0";
    }
}
