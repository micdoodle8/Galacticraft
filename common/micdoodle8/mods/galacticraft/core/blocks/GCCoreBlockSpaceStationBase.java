package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySpaceStationBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockSpaceStationBase extends BlockContainer
{
    private Icon[] spaceStationIcons;

    public GCCoreBlockSpaceStationBase(int par1)
    {
        super(par1, Material.rock);
    }

    @Override
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
        return -1.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.spaceStationIcons = new Icon[2];
        this.spaceStationIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "space_station_top");
        this.spaceStationIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "space_station_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        switch (par5)
        {
        case 1:
            return this.spaceStationIcons[0];
        default:
            return this.spaceStationIcons[1];
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new GCCoreTileEntitySpaceStationBase();
    }
}
