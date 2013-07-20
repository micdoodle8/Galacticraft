package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsBlockRock extends Block
{
    private Icon[] icons;
    public static String[] names = { "smallBoulder1", "smallBoulder2", "mediumBoulder1", "mediumBoulder2" };
    
    public GCMarsBlockRock(int i)
    {
        super(i, Material.rock);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.icons = new Icon[4];
        this.icons[0] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "boulder0");
        this.icons[1] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "boulder1");
        this.icons[2] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "boulder2");
        this.icons[3] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "boulder3");
        this.blockIcon = this.icons[0];
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        switch (metadata % 4)
        {
        case 0:
            return this.icons[0];
        case 1:
            return this.icons[1];
        case 2:
            return this.icons[2];
        case 3:
            return this.icons[3];
        }
        return this.blockIcon;
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftMars.proxy.getRockRenderID();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int idDropped(int meta, Random random, int par3)
    {
        return this.blockID;
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < GCMarsBlockRock.names.length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
