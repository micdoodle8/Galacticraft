package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockWalkway;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

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
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (itemStack.getItemDamage() == BlockWalkway.EnumWalkwayType.WALKWAY_WIRE.getMeta())
        {
            tooltip.add(EnumColor.AQUA + GCCoreUtil.translate("tile.aluminum_wire.alu_wire.name"));
        }
        else if (itemStack.getItemDamage() == BlockWalkway.EnumWalkwayType.WALKWAY_PIPE.getMeta())
        {
            tooltip.add(EnumColor.AQUA + GCCoreUtil.translate(GCBlocks.oxygenPipe.getUnlocalizedName() + ".name"));
        }

        super.addInformation(itemStack, worldIn, tooltip, flagIn);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String name = BlockWalkway.EnumWalkwayType.values()[itemstack.getItemDamage()].getName();
        return this.getBlock().getUnlocalizedName() + "." + name;
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.getBlock().getUnlocalizedName() + ".0";
    }
}
