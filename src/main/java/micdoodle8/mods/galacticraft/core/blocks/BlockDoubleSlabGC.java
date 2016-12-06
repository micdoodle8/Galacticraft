package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockDoubleSlabGC extends BlockSlabGC
{
    public BlockDoubleSlabGC(String name, Material material)
    {
        super(material);
        this.setUnlocalizedName(name);
    }

    @Override
    public boolean isDouble()
    {
        return true;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return null;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 2;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state) & 7;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)) & 7);
    }
}