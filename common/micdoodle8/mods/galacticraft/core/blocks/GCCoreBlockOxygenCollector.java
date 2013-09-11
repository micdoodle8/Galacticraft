package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockOxygenCollector extends GCCoreBlockAdvanced
{
    @SideOnly(Side.CLIENT)
    private Icon[] collectorIcons;

    private Icon iconMachineSide;
    private Icon iconInput;
    private Icon iconOutput;

    public GCCoreBlockOxygenCollector(int id, String assetName)
    {
        super(id, Material.rock);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundStoneFootstep);
        this.setTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_collector_fan");
        this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_oxygen_input");
        this.iconOutput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_power_input");
    }

    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        final int metadata = par1World.getBlockMetadata(x, y, z);
        final int original = metadata;

        int change = 0;

        // Re-orient the block
        switch (original)
        {
        case 0:
            change = 3;
            break;
        case 3:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        }

        par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        entityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiAirCollector, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new GCCoreTileEntityOxygenCollector();
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == metadata + 2)
        {
            return this.iconOutput;
        }
        else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
        {
            return this.iconInput;
        }

        return this.iconMachineSide;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        final int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 3;
            break;
        case 1:
            change = 1;
            break;
        case 2:
            change = 2;
            break;
        case 3:
            change = 0;
            break;
        }

        world.setBlockMetadataWithNotify(x, y, z, change, 3);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.getBlockTileEntity(par2, par3, par4) instanceof GCCoreTileEntityOxygenCollector)
        {
            if (((GCCoreTileEntityOxygenCollector) par1World.getBlockTileEntity(par2, par3, par4)).getPower() > 1)
            {
                for (int var6 = 0; var6 < 10; ++var6)
                {
                    double var7 = par2 + par5Random.nextFloat();
                    final double var9 = par3 + par5Random.nextFloat();
                    double var11 = par4 + par5Random.nextFloat();
                    double var13 = 0.0D;
                    double var15 = 0.0D;
                    double var17 = 0.0D;
                    final int var19 = par5Random.nextInt(2) * 2 - 1;
                    var13 = (par5Random.nextFloat() - 0.5D) * 0.5D;
                    var15 = (par5Random.nextFloat() - 0.5D) * 0.5D;
                    var17 = (par5Random.nextFloat() - 0.5D) * 0.5D;

                    final int var2 = par1World.getBlockMetadata(par2, par3, par4);

                    if (var2 == 3 || var2 == 2)
                    {
                        var7 = par2 + 0.5D + 0.25D * var19;
                        var13 = par5Random.nextFloat() * 2.0F * var19;
                    }
                    else
                    {
                        var11 = par4 + 0.5D + 0.25D * var19;
                        var17 = par5Random.nextFloat() * 2.0F * var19;
                    }

                    GalacticraftCore.proxy.spawnParticle("oxygen", var7, var9, var11, var13, var15, var17, 0.7D, 0.7D, 1.0D, false);
                }
            }
        }
    }
}
