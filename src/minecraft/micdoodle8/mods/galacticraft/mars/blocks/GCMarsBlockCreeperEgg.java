package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.Random;

import net.minecraft.block.BlockDragonEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCMarsBlockCreeperEgg extends BlockDragonEgg
{
    public GCMarsBlockCreeperEgg(int par1, int par2)
    {
        super(par1, par2);
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
        return 27;
    }

    @Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {

    }

    @Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        return false;
    }

    @Override
	public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {

    }

    @Override
	@SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }

    @Override
	public String getTextureFile()
    {
		return "/micdoodle8/mods/galacticraft/mars/client/blocks/mars.png";
    }
}
