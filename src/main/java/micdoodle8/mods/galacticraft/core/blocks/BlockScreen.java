package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockScreen extends BlockAdvanced implements ItemBlockDesc.IBlockShiftDesc, IPartialSealableBlock
{
    /*private IIcon iconFront;
    private IIcon iconSide;*/
	
	//Metadata: 0-5 = direction of screen back;  bit 3 = reserved for future use
	protected BlockScreen(String assetName)
    {
        super(Material.circuits);
        this.setHardness(0.1F);
        this.setStepSound(Block.soundTypeGlass);
        //this.setBlockTextureName("glass");
        this.setUnlocalizedName(assetName);
    }

	@Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing direction)
    {
    	return direction.ordinal() != getMetaFromState(world.getBlockState(pos));
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.iconFront = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "screenFront");
        this.iconSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "screenSide");
    }
    
    @Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == (metadata & 7))
        {
            return this.iconSide;
        }

        return this.iconFront;
    }*/

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = 0;

        int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
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

        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        final int metadata = getMetaFromState(world.getBlockState(pos));
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
        world.setBlockState(pos, getStateFromMeta(change), 2);

        TileEntity tile = world.getTileEntity(pos);
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
    public boolean onMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityScreen)
        {
        	((TileEntityScreen) tile).changeChannel();
        	return true;
        }
    	return false;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
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
	public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
	{
    	return true;
	}

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        final int metadata = getMetaFromState(worldIn.getBlockState(pos)) & 7;
        float boundsFront = 0.094F;
        float boundsBack = 1.0F - boundsFront;

        switch (metadata)
        {
        case 0:
        	this.setBlockBounds(0F, 0F, 0F, 1.0F, boundsBack, 1.0F);        	
        	break;
        case 1:
        	this.setBlockBounds(0F, boundsFront, 0F, 1.0F, 1.0F, 1.0F);
        	break;
        case 2:
        	this.setBlockBounds(0F, 0F, 0F, 1.0F, 1.0F, boundsBack);        	
        	break;
        case 3:
        	this.setBlockBounds(0F, 0F, boundsFront, 1.0F, 1.0F, 1.0F);        	
        	break;
        case 4:
        	this.setBlockBounds(0F, 0F, 0F, boundsBack, 1.0F, 1.0F);        	
        	break;
        case 5:
        	this.setBlockBounds(boundsFront, 0F, 0F,  1.0F, 1.0F, 1.0F);        	
        	break;
        }

        return super.collisionRayTrace(worldIn, pos, start, end);
    }
}
