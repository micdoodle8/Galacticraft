package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCopperWire;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockConductor;

/**
 * A copper wire block that can change its collision bounds based on the
 * connection.
 * 
 * @author Calclavia, Aidancbrady
 */
public class GCCoreBlockCopperWire extends BlockConductor
{
    public GCCoreBlockCopperWire(int id, String assetName)
    {
        super(id, Material.cloth);
        this.setStepSound(Block.soundClothFootstep);
        this.setResistance(0.2F);
        this.setHardness(0.1F);
        this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
        Block.setBurnProperties(this.blockID, 30, 60);
        this.setHardness(0.1F);
        this.setTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + this.getUnlocalizedName().replace("tile.", ""));
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new GCCoreTileEntityCopperWire();
    }
}
