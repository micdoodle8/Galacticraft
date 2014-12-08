package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import java.util.List;

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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBeamReceiver extends BlockTileGC implements ItemBlockDesc.IBlockShiftDesc
{
    public BlockBeamReceiver(String assetName)
    {
        super(Material.iron);
        this.setBlockName(assetName);
        this.setBlockTextureName("stone");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int oldMeta = world.getBlockMetadata(x, y, z);
        int meta = this.getMetadataFromAngle(world, x, y, z, ForgeDirection.getOrientation(oldMeta).getOpposite().ordinal());

        if (meta == -1)
        {
            world.func_147480_a(x, y, z, true);
        }

        if (meta != oldMeta)
        {
            world.setBlockMetadataWithNotify(x, y, z, meta, 3);
            TileEntity thisTile = world.getTileEntity(x, y, z);
            if (thisTile instanceof TileEntityBeamReceiver)
            {
            	TileEntityBeamReceiver thisReceiver = (TileEntityBeamReceiver) thisTile; 
                thisReceiver.setFacing(ForgeDirection.getOrientation(meta));
                thisReceiver.invalidateReflector();
                thisReceiver.initiateReflector();
            }
        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        TileEntity thisTile = world.getTileEntity(x, y, z);
        if (thisTile instanceof TileEntityBeamReceiver)
        	((TileEntityBeamReceiver)thisTile).setFacing(ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z)));
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);

        if (meta != -1)
        {
            ForgeDirection dir = ForgeDirection.getOrientation(meta);

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
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
    }

    private int getMetadataFromAngle(World world, int x, int y, int z, int side)
    {
        ForgeDirection direction = ForgeDirection.getOrientation(side).getOpposite();

        TileEntity tileAt = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);

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

        for (ForgeDirection adjacentDir : ForgeDirection.VALID_DIRECTIONS)
        {
            if (adjacentDir == direction) continue;
        	tileAt = world.getTileEntity(x + adjacentDir.offsetX, y + adjacentDir.offsetY, z + adjacentDir.offsetZ);

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
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {
        return this.getMetadataFromAngle(world, x, y, z, side);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        if (this.getMetadataFromAngle(world, x, y, z, side) != -1)
        {
            return true;
        }

        if (world.isRemote && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
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
    public int damageDropped(int metadata)
    {
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
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
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileEntityBeamReceiver)
        {
            return ((TileEntityBeamReceiver) tile).onMachineActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
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
