package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreBlock extends Block
{
    /**
     * 0: COPPER BLOCK 1: ALUMINIUM BLOCK 2: TITANIUM BLOCK 3: DECO 1 4: DECO 2
     */
    Icon[][] iconBuffer;

    protected GCCoreBlock(int i)
    {
        super(i, Material.rock);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.iconBuffer = new Icon[5][3];
        this.iconBuffer[3][0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_2");
        this.iconBuffer[3][1] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_4");
        this.iconBuffer[3][2] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_1");
        this.iconBuffer[4][0] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_4");
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        switch (meta)
        {
        case 3:
            switch (side)
            {
            case 0:
                return this.iconBuffer[3][1];
            case 1:
                return this.iconBuffer[3][0];
            default:
                return this.iconBuffer[3][2];
            }
        case 4:
            return this.iconBuffer[4][0];
        default:
            return this.iconBuffer[4][0];
        }
    }

    @Override
    public int idDropped(int meta, Random random, int par3)
    {
        switch (meta)
        {
        default:
            return this.blockID;
        }
    }

    @Override
    public int damageDropped(int meta)
    {
        switch (meta)
        {
        default:
            return meta;
        }
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        switch (meta)
        {
        default:
            return 1;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 3; var4 < 5; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
