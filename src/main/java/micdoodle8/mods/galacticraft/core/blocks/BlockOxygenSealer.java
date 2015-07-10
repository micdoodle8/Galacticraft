package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOxygenSealer extends BlockAdvancedTile implements ItemBlockDesc.IBlockShiftDesc
{
    /*private IIcon iconMachineSide;
    private IIcon iconSealer;
    private IIcon iconInput;
    private IIcon iconOutput;*/

    public BlockOxygenSealer(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeStone);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
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
        this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconSealer = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_sealer");
        this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_oxygen_input");
        this.iconOutput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");
    }*/

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int change = 0;

        // Re-orient the block
        switch (getMetaFromState(world.getBlockState(pos)))
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

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBaseUniversalElectrical)
        {
            ((TileBaseUniversalElectrical) te).updateFacing();
        }

        world.setBlockState(pos, getStateFromMeta(change), 3);
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    /*@Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == 1)
        {
            return this.iconSealer;
        }
        else if (side == metadata + 2)
        {
            return this.iconOutput;
        }
        else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
        {
            return this.iconInput;
        }
        else
        {
            return this.iconMachineSide;
        }
    }*/

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
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

        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityOxygenSealer();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        // This is unnecessary as it will be picked up by
        // OxygenPressureProtocol.onEdgeBlockUpdated anyhow
        // Also don't want to clear all the breatheableAir if there are still
        // working sealers in the space
        /*
		 * TileEntity tile = world.getTileEntity(x, y, z);
		 * 
		 * if (tile instanceof GCCoreTileEntityOxygenSealer) {
		 * GCCoreTileEntityOxygenSealer sealer = (GCCoreTileEntityOxygenSealer)
		 * tile;
		 * 
		 * if (sealer.threadSeal != null && sealer.threadSeal.sealed) { for
		 * (BlockVec3 checkedVec : sealer.threadSeal.checked) { int blockID =
		 * checkedVec.getBlockID(world);
		 * 
		 * if (blockID == GCCoreBlocks.breatheableAir) {
		 * world.setBlock(checkedVec.x, checkedVec.y, checkedVec.z, 0, 0, 2); }
		 * } } }
		 */

        super.breakBlock(worldIn, pos, state);
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
