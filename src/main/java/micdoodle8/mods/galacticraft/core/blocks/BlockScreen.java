package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockScreen extends BlockAdvanced implements ItemBlockDesc.IBlockShiftDesc, IPartialSealableBlock
{
	//Metadata: 0-5 = direction screen is facing;  bit 3 = part of multi-block screen
	protected BlockScreen(String assetName)
    {
        super(Material.circuits);
        this.setHardness(0.1F);
        this.setStepSound(Block.soundTypeGlass);
        this.setBlockTextureName("glass");
        this.setBlockName(assetName);
    }

	@Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection direction)
    {
    	return direction.ordinal() != world.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        int metadata = 0;

        int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 3;
            break;
        case 1:
            change = 4;
            break;
        case 2:
            change = 2;
            break;
        case 3:
            change = 5;
            break;
        }

        world.setBlockMetadataWithNotify(x, y, z, change, 3);
    }

    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        final int metadata = world.getBlockMetadata(x, y, z);
        final int facing = metadata & 7;
        int change = 0;
        
        switch (facing)
        {
        	case 0:
        		change = 1;
        		break;
        	case 1:
        		change = 3;
        		break;
        	case 2:
        		change = 5;
        		break;
        	case 3:
        		change = 4;
        		break;
        	case 4:
        		change = 2;
        		break;
        	case 5:
        		change = 0;       		
        }
        change += (8 & metadata);
        world.setBlockMetadataWithNotify(x, y, z, change, 2);

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityScreen)
        {
        	((TileEntityScreen) tile).breakScreen(facing);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityScreen();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityScreen)
        {
        	((TileEntityScreen) tile).changeChannel();
        	return true;
        }
    	return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbour)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityScreen)
        {
        	((TileEntityScreen) tile).refreshConnections(true);
        }
    }
    
    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

	@Override
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
    	return true;
	}
}
