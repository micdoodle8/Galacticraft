package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineFrame extends Block
{
//    @SideOnly(Side.CLIENT)
//    private IIcon[] blockIcons;

    public BlockMachineFrame(String assetName)
    {
        super(Material.rock);
        this.blockHardness = 3.0F;
        this.setUnlocalizedName(assetName);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcons = new IIcon[1];
        this.blockIcons[0] = par1IconRegister.registerIcon(GalacticraftPlanets.TEXTURE_PREFIX + "machineframe");
        this.blockIcon = this.blockIcons[0];
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        if (meta < 0 || meta >= this.blockIcons.length)
        {
            return this.blockIcon;
        }

        return this.blockIcons[meta];
    }*/

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

//    @SuppressWarnings({ "unchecked", "rawtypes" })
//    @SideOnly(Side.CLIENT)
//    @Override
//    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
//    {
//        int var4;
//
//        for (var4 = 0; var4 < this.blockIcons.length; ++var4)
//        {
//            par3List.add(new ItemStack(par1, 1, var4));
//        }
//    }
}
