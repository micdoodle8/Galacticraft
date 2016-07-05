package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockWalkway;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockWalkway extends ItemBlockDesc
{
    public ItemBlockWalkway(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean advanced)
    {
        if (itemStack.getItemDamage() == BlockWalkway.EnumWalkwayType.WALKWAY_WIRE.getMeta())
        {
            list.add(EnumColor.AQUA + GCBlocks.aluminumWire.getLocalizedName());
        }
        else if (itemStack.getItemDamage() == BlockWalkway.EnumWalkwayType.WALKWAY_PIPE.getMeta())
        {
            list.add(EnumColor.AQUA + GCBlocks.oxygenPipe.getLocalizedName());
        }

        super.addInformation(itemStack, entityPlayer, list, advanced);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return this.getBlock().getUnlocalizedName() + "." + "todo";
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.getBlock().getUnlocalizedName() + ".0";
    }
}
