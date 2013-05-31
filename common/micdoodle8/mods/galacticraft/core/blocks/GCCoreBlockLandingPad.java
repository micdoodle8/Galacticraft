package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPadSingle;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreBlockLandingPad extends GCCoreBlockAdvanced
{
    private Icon[] icons = new Icon[2];

    public GCCoreBlockLandingPad(int i)
    {
        super(i, Material.iron);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < 2; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.icons[0] = par1IconRegister.registerIcon("galacticraftcore:launch_pad");
        this.icons[1] = par1IconRegister.registerIcon("galacticraftcore:buggy_fueler_blank");
        this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:launch_pad");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        switch (par2)
        {
        case 0:
            return this.icons[0];
        case 1:
            return this.icons[1];
        }

        return this.blockIcon;
    }

    @Override
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        final int id = GCCoreBlocks.landingPad.blockID;

        if (par1World.getBlockId(par2 + 1, par3, par4) == id && par1World.getBlockId(par2 + 2, par3, par4) == id && par1World.getBlockId(par2 + 3, par3, par4) == id)
        {
            return false;
        }

        if (par1World.getBlockId(par2 - 1, par3, par4) == id && par1World.getBlockId(par2 - 2, par3, par4) == id && par1World.getBlockId(par2 - 3, par3, par4) == id)
        {
            return false;
        }

        if (par1World.getBlockId(par2, par3, par4 + 1) == id && par1World.getBlockId(par2, par3, par4 + 2) == id && par1World.getBlockId(par2, par3, par4 + 3) == id)
        {
            return false;
        }

        if (par1World.getBlockId(par2, par3, par4 - 1) == id && par1World.getBlockId(par2, par3, par4 - 2) == id && par1World.getBlockId(par2, par3, par4 - 3) == id)
        {
            return false;
        }

        if (par1World.getBlockId(par2, par3 - 1, par4) == GCCoreBlocks.landingPad.blockID && par5 == 1)
        {
            return false;
        } else
        {
            return this.canPlaceBlockAt(par1World, par2, par3, par4);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch (metadata)
        {
        case 0:
            return new GCCoreTileEntityLandingPadSingle();
        default:
            return new GCCoreTileEntityBuggyFuelerSingle();
        }
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
}