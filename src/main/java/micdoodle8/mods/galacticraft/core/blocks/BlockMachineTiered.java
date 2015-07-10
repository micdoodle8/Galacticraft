package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class BlockMachineTiered extends BlockTileGC implements ItemBlockDesc.IBlockShiftDesc
{
    public static final int STORAGE_MODULE_METADATA = 0;
    public static final int ELECTRIC_FURNACE_METADATA = 4;

    /*private IIcon iconMachineSide;
    private IIcon iconInput;
    private IIcon iconOutput;
    private IIcon iconTier2;
    private IIcon iconMachineSideT2;
    private IIcon iconInputT2;
    private IIcon iconOutputT2;

    private IIcon[] iconEnergyStorageModule;
    private IIcon[] iconEnergyStorageModuleT2;
    private IIcon iconElectricFurnace;
    private IIcon iconElectricFurnaceT2;*/

    public BlockMachineTiered(String assetName)
    {
        super(GCBlocks.machine);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + "machine");
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
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine");
        this.iconInput = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");
        this.iconOutput = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_output");
        this.iconMachineSide = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_side");

        this.iconEnergyStorageModule = new IIcon[17];

        for (int i = 0; i < this.iconEnergyStorageModule.length; i++)
        {
            this.iconEnergyStorageModule[i] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "energyStorageModule_" + i);
        }

        this.iconElectricFurnace = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "electricFurnace");

        if (GalacticraftCore.isPlanetsLoaded)
        {
            this.iconTier2 = iconRegister.registerIcon("galacticraftasteroids:machine");
            this.iconInputT2 = iconRegister.registerIcon("galacticraftasteroids:machine_input");
            this.iconOutputT2 = iconRegister.registerIcon("galacticraftasteroids:machine_output");
            this.iconMachineSideT2 = iconRegister.registerIcon("galacticraftasteroids:machine_side");
            this.iconEnergyStorageModuleT2 = new IIcon[17];
            for (int i = 0; i < this.iconEnergyStorageModule.length; i++)
            {
                this.iconEnergyStorageModuleT2[i] = iconRegister.registerIcon("galacticraftasteroids:energyStorageModule_" + i);
            }
            this.iconElectricFurnaceT2 = iconRegister.registerIcon("galacticraftasteroids:electricFurnace");
        }
        else
        {
            this.iconTier2 = iconRegister.registerIcon("void");
            this.iconInputT2 = iconRegister.registerIcon("void");
            this.iconOutputT2 = iconRegister.registerIcon("void");
            this.iconMachineSideT2 = iconRegister.registerIcon("void");
            this.iconEnergyStorageModuleT2 = new IIcon[17];
            for (int i = 0; i < this.iconEnergyStorageModule.length; i++)
            {
                this.iconEnergyStorageModuleT2[i] = iconRegister.registerIcon("void");
            }
            this.iconElectricFurnaceT2 = iconRegister.registerIcon("void");
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        int type = metadata & 4;
        int metaside = (metadata & 3) + 2;

        if (type == BlockMachineTiered.STORAGE_MODULE_METADATA)
        {
            if (side == 0 || side == 1)
            {
                if (metadata >= 8)
                {
                    return this.iconTier2;
                }
                return this.blockIcon;
            }

            // If it is the front side
            if (side == metaside)
            {
                if (metadata >= 8)
                {
                    return this.iconOutputT2;
                }
                return this.iconOutput;
            }
            // If it is the back side
            else if (side == (metaside ^ 1))
            {
                if (metadata >= 8)
                {
                    return this.iconInputT2;
                }
                return this.iconInput;
            }

            TileEntity tile = world.getTileEntity(x, y, z);

            int level = 0;
            if (tile instanceof TileEntityEnergyStorageModule)
            {
                level = ((TileEntityEnergyStorageModule) tile).scaledEnergyLevel;
            }

            if (metadata >= 8)
            {
                return this.iconEnergyStorageModuleT2[level];
            }
            return this.iconEnergyStorageModule[level];
        }

        return this.getIcon(side, metadata);
    }

    @Override
    public IIcon getIcon(int side, int metadata)
    {
        int metaside = (metadata & 3) + 2;

        if (side == 0 || side == 1)
        {
            if (metadata >= 8)
            {
                return this.iconTier2;
            }
            return this.blockIcon;
        }

        if ((metadata & 4) == BlockMachineTiered.ELECTRIC_FURNACE_METADATA)
        {
            // If it is the front side
            if (side == metaside)
            {
                if (metadata >= 8)
                {
                    return this.iconInputT2;
                }
                return this.iconInput;
            }
            // If it is the back side
            else if (metaside == 2 && side == 4 || metaside == 3 && side == 5 || metaside == 4 && side == 3 || metaside == 5 && side == 2)
            {
                if (metadata >= 8)
                {
                    return this.iconElectricFurnaceT2;
                }
                return this.iconElectricFurnace;
            }
        }
        else
        {
            // If it is the front side
            if (side == metaside)
            {
                if (metadata >= 8)
                {
                    return this.iconOutputT2;
                }
                return this.iconOutput;
            }
            // If it is the back side
            else if (side == (metaside ^ 1))
            {
                if (metadata >= 8)
                {
                    return this.iconInputT2;
                }
                return this.iconInput;
            }

            if (metadata >= 8)
            {
                return this.iconEnergyStorageModuleT2[16];
            }
            return this.iconEnergyStorageModule[16];
        }

        if (metadata >= 8)
        {
            return this.iconMachineSideT2;
        }
        return this.iconMachineSide;
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

        worldIn.setBlockState(pos, getStateFromMeta((metadata & 12) + change), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));
        int original = metadata & 3;
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

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBaseUniversalElectrical)
        {
            ((TileBaseUniversalElectrical) te).updateFacing();
        }

        world.setBlockState(pos, getStateFromMeta((metadata & 12) + change), 3);
        return true;
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int metadata = getMetaFromState(state);
        int tier = metadata / 8 + 1;

        if ((metadata & 4) == BlockMachineTiered.ELECTRIC_FURNACE_METADATA)
        {
            return new TileEntityElectricFurnace(tier);
        }
        else
        {
            return new TileEntityEnergyStorageModule(tier);
        }
    }

    public ItemStack getEnergyStorageModule()
    {
        return new ItemStack(this, 1, BlockMachineTiered.STORAGE_MODULE_METADATA);
    }

    public ItemStack getEnergyStorageCluster()
    {
        return new ItemStack(this, 1, 8 + BlockMachineTiered.STORAGE_MODULE_METADATA);
    }

    public ItemStack getElectricFurnace()
    {
        return new ItemStack(this, 1, BlockMachineTiered.ELECTRIC_FURNACE_METADATA);
    }

    public ItemStack getElectricArcFurnace()
    {
        return new ItemStack(this, 1, 8 + BlockMachineTiered.ELECTRIC_FURNACE_METADATA);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(this.getEnergyStorageModule());
        par3List.add(this.getElectricFurnace());
        if (GalacticraftCore.isPlanetsLoaded)
        {
            par3List.add(this.getEnergyStorageCluster());
            par3List.add(this.getElectricArcFurnace());
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state) & 12;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
    {
        int metadata = this.getDamageValue(world, pos);

        return new ItemStack(this, 1, metadata);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        int tier = (meta >= 8 ? 2 : 1);
        switch (meta & 4)
        {
        case ELECTRIC_FURNACE_METADATA:
            return GCCoreUtil.translate("tile.electricFurnaceTier" + tier + ".description");
        case STORAGE_MODULE_METADATA:
            return GCCoreUtil.translate("tile.energyStorageModuleTier" + tier + ".description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
