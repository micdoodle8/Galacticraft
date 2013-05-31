package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDetector;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockOxygenDetector extends BlockContainer
{
    private Icon iconSide;
    private Icon iconTop;

    protected GCCoreBlockOxygenDetector(int id)
    {
        super(id, Material.iron);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.iconTop = par1IconRegister.registerIcon("galacticraftcore:machine_blank");
        this.iconSide = par1IconRegister.registerIcon("galacticraftcore:detector_side");
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return this.iconTop;
        } else
        {
            return this.iconSide;
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new GCCoreTileEntityOxygenDetector();
    }

    public void updateOxygenState(World par1World, int x, int y, int z, boolean valid)
    {
        if (valid)
        {
            par1World.setBlockMetadataWithNotify(x, y, z, 1, 3);
        } else
        {
            par1World.setBlockMetadataWithNotify(x, y, z, 0, 3);
        }
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1 ? 15 : 0;
    }
}
