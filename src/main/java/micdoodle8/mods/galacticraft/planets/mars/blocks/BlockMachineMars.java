package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.world.IChunkLoader;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockMachineMars extends BlockTileGC implements ItemBlockDesc.IBlockShiftDesc
{
    public static final int TERRAFORMER_METADATA = 0;
    public static final int CRYOGENIC_CHAMBER_METADATA = 4;
    public static final int LAUNCH_CONTROLLER_METADATA = 8;

//    private IIcon iconMachineSide;
//    private IIcon iconInput;
//
//    private IIcon iconTerraformer;
//    private IIcon iconLaunchController;
//    private IIcon iconCryochamber;

    public BlockMachineMars(String assetName)
    {
        super(GCBlocks.machine);
		this.setStepSound(soundTypeMetal);
        this.setUnlocalizedName(assetName);
    }

    /*@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");

        this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconTerraformer = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "terraformer_0");
        this.iconLaunchController = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "launchController");
        this.iconCryochamber = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "cryoDummy");
    }*/

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return this.blockIcon;
        }

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            metadata -= BlockMachineMars.LAUNCH_CONTROLLER_METADATA;

            // If it is the front side
            if (side == metadata + 2)
            {
                return this.iconInput;
            }
            else if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
            {
                return this.iconMachineSide;
            }
            else
            {
                return this.iconLaunchController;
            }
        }
        else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            return this.iconCryochamber;
        }
        else
        {
            if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
            {
                return this.iconMachineSide;
            }
            else if (side == ForgeDirection.getOrientation(metadata + 2).ordinal())
            {
                return this.iconInput;
            }
            else
            {
                return this.iconTerraformer;
            }
        }
    }*/

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        case 3:
            change = 3;
            break;
        }

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
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

            worldIn.setBlockState(pos, getStateFromMeta(BlockMachineMars.LAUNCH_CONTROLLER_METADATA + change), 3);
        }
        else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            if (!this.canPlaceChamberAt(worldIn, pos, placer))
            {
                if (placer instanceof EntityPlayer)
                {
                    if (!worldIn.isRemote)
                    {
                        ((EntityPlayer) placer).addChatMessage(new ChatComponentText(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                    }

                    worldIn.setBlockToAir(pos);
                    ((EntityPlayer) placer).inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(MarsBlocks.machine), 1, BlockMachineMars.CRYOGENIC_CHAMBER_METADATA));
                    return;
                }
            }
            else
            {
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

                worldIn.setBlockState(pos, getStateFromMeta(BlockMachineMars.CRYOGENIC_CHAMBER_METADATA + change), 3);
            }
        }
        else
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachineMars.TERRAFORMER_METADATA + change), 3);
        }

        TileEntity var8 = worldIn.getTileEntity(pos);

        if (var8 instanceof IMultiBlock)
        {
            ((IMultiBlock) var8).onCreate(pos);
        }

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            for (int dX = -2; dX < 3; dX++)
            {
                for (int dZ = -2; dZ < 3; dZ++)
                {
                    BlockPos pos1 = pos.add(dX, 0, dZ);
                    final Block id = worldIn.getBlockState(pos1).getBlock();

                    if (id == GCBlocks.landingPadFull)
                    {
                        worldIn.markBlockForUpdate(pos1);
                    }
                }
            }
        }

        if (var8 instanceof IChunkLoader && !var8.getWorld().isRemote && ConfigManagerMars.launchControllerChunkLoad && placer instanceof EntityPlayer)
        {
            ((IChunkLoader) var8).setOwnerName(((EntityPlayer) placer).getGameProfile().getName());
            ((IChunkLoader) var8).onTicketLoaded(ForgeChunkManager.requestTicket(GalacticraftCore.instance, var8.getWorld(), Type.NORMAL), true);
        }
        else if (var8 instanceof TileEntityLaunchController && placer instanceof EntityPlayer)
        {
            ((TileEntityLaunchController) var8).setOwnerName(((EntityPlayer) placer).getGameProfile().getName());
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        int metadata = getMetaFromState(state);
        int original = metadata;

        int change = 0;

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            original -= BlockMachineMars.LAUNCH_CONTROLLER_METADATA;
        }
        else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            return false;
        }

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

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            change += BlockMachineMars.LAUNCH_CONTROLLER_METADATA;
        }

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA || metadata < BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileBaseUniversalElectrical)
            {
                ((TileBaseUniversalElectrical) te).updateFacing();
            }
        }

        world.setBlockState(pos, getStateFromMeta(change), 3);
        return true;
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int metadata = getMetaFromState(worldIn.getBlockState(pos));

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            ((IMultiBlock) worldIn.getTileEntity(pos)).onActivated(playerIn);
            return true;
        }
        else
        {
            playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
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
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int metadata = getMetaFromState(state);
        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            return new TileEntityLaunchController();
        }
        if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            return new TileEntityCryogenicChamber();
        }
        else
        {
            return new TileEntityTerraformer();
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);

        if (getMetaFromState(worldIn.getBlockState(pos)) >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            for (int dX = -2; dX < 3; dX++)
            {
                for (int dZ = -2; dZ < 3; dZ++)
                {
                    BlockPos pos1 = pos.add(dX, 0, dZ);
                    final Block id = worldIn.getBlockState(pos1).getBlock();

                    if (id == GCBlocks.landingPadFull)
                    {
                        worldIn.markBlockForUpdate(pos1);
                    }
                }
            }
        }
    }

    public ItemStack getTerraformer()
    {
        return new ItemStack(this, 1, BlockMachineMars.TERRAFORMER_METADATA);
    }

    public ItemStack getChamber()
    {
        return new ItemStack(this, 1, BlockMachineMars.CRYOGENIC_CHAMBER_METADATA);
    }

    public ItemStack getLaunchController()
    {
        return new ItemStack(this, 1, BlockMachineMars.LAUNCH_CONTROLLER_METADATA);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(this.getTerraformer());
        par3List.add(this.getChamber());
        par3List.add(this.getLaunchController());
    }

    private boolean canPlaceChamberAt(World world, BlockPos pos, EntityLivingBase player)
    {
        for (int y = 0; y < 3; y++)
        {
            Block blockAt = world.getBlockState(pos.add(0, y, 0)).getBlock();
            int metaAt = getMetaFromState(world.getBlockState(pos));

            if (y == 0 && blockAt == MarsBlocks.machine && metaAt >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA && metaAt < BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
            {
                continue;
            }
            if (!blockAt.getMaterial().isReplaceable())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int metadata = getMetaFromState(state);
        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            return BlockMachineMars.LAUNCH_CONTROLLER_METADATA;
        }
        else if (metadata >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
        {
            return BlockMachineMars.CRYOGENIC_CHAMBER_METADATA;
        }
        else
        {
            return BlockMachineMars.TERRAFORMER_METADATA;
        }
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftPlanets.getBlockRenderID(this);
    }

    @Override
    public boolean isBed(IBlockAccess world, BlockPos pos, Entity player)
    {
        return getMetaFromState(world.getBlockState(pos)) >= BlockMachineMars.CRYOGENIC_CHAMBER_METADATA;
    }

    @Override
    public BlockPos getBedSpawnPosition(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return pos.up();
    }

    @Override
    public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityCryogenicChamber)
        {
            ((TileEntityCryogenicChamber) tile).isOccupied = true;
        }
    }

//    public static BlockPos getNearestEmptyBlockPos(World par0World, int par1, int par2, int par3, int par4)
//    {
//        for (int k1 = 0; k1 <= 1; ++k1)
//        {
//            int l1 = par1 - 1;
//            int i2 = par3 - 1;
//            int j2 = l1 + 2;
//            int k2 = i2 + 2;
//
//            for (int l2 = l1; l2 <= j2; ++l2)
//            {
//                for (int i3 = i2; i3 <= k2; ++i3)
//                {
//                    if (World.doesBlockHaveSolidTopSurface(par0World, l2, par2 - 1, i3) && !par0World.getBlock(l2, par2, i3).getMaterial().isOpaque() && !par0World.getBlock(l2, par2 + 1, i3).getMaterial().isOpaque())
//                    {
//                        if (par4 <= 0)
//                        {
//                            return new BlockPos(l2, par2, i3);
//                        }
//
//                        --par4;
//                    }
//                }
//            }
//        }
//
//        return null;
//    }

    @Override
    public EnumFacing getBedDirection(IBlockAccess world, BlockPos pos)
    {
        return EnumFacing.DOWN;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case CRYOGENIC_CHAMBER_METADATA:
            return GCCoreUtil.translate("tile.cryoChamber.description");
        case LAUNCH_CONTROLLER_METADATA:
            return GCCoreUtil.translate("tile.launchController.description");
        case TERRAFORMER_METADATA:
            return GCCoreUtil.translate("tile.terraformer.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
