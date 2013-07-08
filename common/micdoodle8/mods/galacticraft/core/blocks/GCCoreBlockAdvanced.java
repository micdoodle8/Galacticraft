package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;

public abstract class GCCoreBlockAdvanced extends BlockAdvanced implements ITileEntityProvider
{
    public GCCoreBlockAdvanced(int par1, Material par3Material)
    {
        super(par1, par3Material);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int id = this.blockID;

        if (id == 0)
        {
            return null;
        }

        Item item = Item.itemsList[id];
        if (item == null)
        {
            return null;
        }

        return new ItemStack(id, 1, this.getDamageValue(world, x, y, z));
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }
}
