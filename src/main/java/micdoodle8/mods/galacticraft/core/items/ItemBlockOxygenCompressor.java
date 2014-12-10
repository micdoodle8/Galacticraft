package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenCompressor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockOxygenCompressor extends ItemBlockDesc
{
    public ItemBlockOxygenCompressor(Block block)
    {
        super(block);
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

        if (itemstack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
        {
            metadata = 1;
        }
        else if (itemstack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
        {
            metadata = 0;
        }

        return this.field_150939_a.getUnlocalizedName() + "." + metadata;
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.field_150939_a.getUnlocalizedName() + ".0";
    }
}
