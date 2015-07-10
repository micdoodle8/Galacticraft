package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockBeamReceiver extends BlockTileGC implements ItemBlockDesc.IBlockShiftDesc
{
    public BlockBeamReceiver(String assetName)
    {
        super(Material.iron);
        this.setUnlocalizedName(assetName);
        this.setStepSound(Block.soundTypeMetal);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        int oldMeta = getMetaFromState(worldIn.getBlockState(pos));
        int meta = this.getMetadataFromAngle(worldIn, pos, EnumFacing.getFront(oldMeta).getOpposite());

        if (meta == -1)
        {
            worldIn.destroyBlock(pos, true);
        }

        if (meta != oldMeta)
        {
            worldIn.setBlockState(pos, getStateFromMeta(meta), 3);
            TileEntity thisTile = worldIn.getTileEntity(pos);
            if (thisTile instanceof TileEntityBeamReceiver)
            {
            	TileEntityBeamReceiver thisReceiver = (TileEntityBeamReceiver) thisTile; 
                thisReceiver.setFacing(EnumFacing.getFront(meta));
                thisReceiver.invalidateReflector();
                thisReceiver.initiateReflector();
            }
        }

        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileEntity thisTile = world.getTileEntity(pos);
        if (thisTile instanceof TileEntityBeamReceiver)
        	((TileEntityBeamReceiver)thisTile).setFacing(EnumFacing.getFront(getMetaFromState(state)));
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        int meta = getMetaFromState(world.getBlockState(pos));

        if (meta != -1)
        {
            EnumFacing dir = EnumFacing.getFront(meta);

            switch (dir)
            {
            case UP:
                this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 1.0F, 0.7F);
                break;
            case DOWN:
                this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.42F, 0.8F);
                break;
            case EAST:
                this.setBlockBounds(0.58F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
                break;
            case WEST:
                this.setBlockBounds(0.0F, 0.2F, 0.2F, 0.42F, 0.8F, 0.8F);
                break;
            case NORTH:
                this.setBlockBounds(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.42F);
                break;
            case SOUTH:
                this.setBlockBounds(0.2F, 0.2F, 0.58F, 0.8F, 0.8F, 1.0F);
                break;
            default:
                break;
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    private int getMetadataFromAngle(World world, BlockPos pos, EnumFacing side)
    {
        EnumFacing direction = side.getOpposite();

        TileEntity tileAt = world.getTileEntity(pos.add(direction.getFrontOffsetX(), direction.getFrontOffsetY(), direction.getFrontOffsetZ()));

        if (tileAt instanceof EnergyStorageTile)
        {
            if (((EnergyStorageTile) tileAt).getModeFromDirection(direction.getOpposite()) != null)
            {
                return direction.ordinal();
            }
            else
            {
                return -1;
            }
        }
        
        if (EnergyUtil.otherModCanReceive(tileAt, direction.getOpposite()))
        	return direction.ordinal();

        for (EnumFacing adjacentDir : EnumFacing.values())
        {
            if (adjacentDir == direction) continue;
        	tileAt = world.getTileEntity(pos.add(adjacentDir.getFrontOffsetX(), adjacentDir.getFrontOffsetY(), adjacentDir.getFrontOffsetZ()));

            if (tileAt instanceof EnergyStorageTile && ((EnergyStorageTile) tileAt).getModeFromDirection(adjacentDir.getOpposite()) != null)
            {
                return adjacentDir.ordinal();
            }
            
            if (EnergyUtil.otherModCanReceive(tileAt, adjacentDir.getOpposite()))
            	return adjacentDir.ordinal();
        }

        return -1;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(this.getMetadataFromAngle(worldIn, pos, facing));
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        if (this.getMetadataFromAngle(worldIn, pos, side) != -1)
        {
            return true;
        }

        if (worldIn.isRemote)
        {
            this.sendIncorrectSideMessage();
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    private void sendIncorrectSideMessage()
    {
        FMLClientHandler.instance().getClient().thePlayer.addChatMessage(new ChatComponentText(EnumColor.RED + GCCoreUtil.translate("gui.receiver.cannotAttach")));
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
        return -1;
    }

    @Override
    public int damageDropped(IBlockState metadata)
    {
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState metadata)
    {
        return new TileEntityBeamReceiver();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityBeamReceiver)
        {
            return ((TileEntityBeamReceiver) tile).onMachineActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        }

        return false;
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
}
