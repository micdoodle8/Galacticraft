package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTransmitter;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityHydrogenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHydrogenPipe extends BlockTransmitter implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
//    private IIcon pipeIcon;

    public BlockHydrogenPipe(String assetName)
    {
        super(Material.glass);
        this.setHardness(0.3F);
        this.setStepSound(Block.soundTypeGlass);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        worldIn.notifyLightSet(pos);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
    {
        BlockVec3 thisVec = new BlockVec3(x, y, z).modifyPositionFromSide(ForgeDirection.getOrientation(par5));
        final Block blockAt = thisVec.getBlock(par1IBlockAccess);

        if (blockAt == MarsBlocks.hydrogenPipe)
        {
            return this.pipeIcon;
        }

        return this.pipeIcon;
    }*/

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.pipeIcon = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "pipe_hydrogen");

        this.blockIcon = this.pipeIcon;
    }*/

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
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
    public TileEntity createNewTileEntity(World world, int meta)
    {
    	return new TileEntityHydrogenPipe(); 
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.HYDROGEN;
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
