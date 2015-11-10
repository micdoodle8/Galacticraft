package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.VersionUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockSlimelingEgg extends Block implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
//    private IIcon[] icons;
    public static String[] names = { "redEgg", "blueEgg", "yellowEgg" };

    public BlockSlimelingEgg(String assetName)
    {
        super(Material.rock);
        this.setBlockBounds(0.17F, 0.0F, 0.11F, 0.83F, 0.70F, 0.89F);
        this.setUnlocalizedName(assetName);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[6];
        this.icons[0] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "redEgg_0");
        this.icons[1] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "blueEgg_0");
        this.icons[2] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "yellowEgg_0");
        this.icons[3] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "redEgg_1");
        this.icons[4] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "blueEgg_1");
        this.icons[5] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "yellowEgg_1");
        this.blockIcon = this.icons[0];
    }*/

    @Override
    public boolean isFullCube()
    {
        return false;
    }

//    @Override
//    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
//    {
//        Block block = par1World.getBlock(par2, par3 - 1, par4);
//        return block.isSideSolid(par1World, par2, par3, par4, ForgeDirection.UP);
//    }
    
    private boolean beginHatch(World world, BlockPos pos, EntityPlayer player)
    {
        IBlockState state = world.getBlockState(pos);
        int l = state.getBlock().getMetaFromState(state);

        if (l < 3)
        {
            world.setBlockState(pos, state.getBlock().getStateFromMeta(l + 3), 2);

            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntitySlimelingEgg)
            {
                ((TileEntitySlimelingEgg) tile).timeToHatch = world.rand.nextInt(50) + 20;
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerUUID = VersionUtil.mcVersionMatches("1.7.2") ? player.getName() : player.getUniqueID().toString();
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerName = player.getName();
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        ItemStack currentStack = player.getCurrentEquippedItem();
    	if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
	    {
	        return world.setBlockToAir(pos);
	    }
	    else if (player.capabilities.isCreativeMode)
	    {
	        return world.setBlockToAir(pos);
	    }
	    else 
	    {
	    	beginHatch(world, pos, player);
	    	return false;
	    }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	return beginHatch(worldIn, pos, playerIn);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
    {
        ItemStack currentStack = player.getCurrentEquippedItem();

        if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
        {
            player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
            player.addExhaustion(0.025F);
            this.dropBlockAsItem(worldIn, pos, state.getBlock().getStateFromMeta(state.getBlock().getMetaFromState(state) % 3), 0);
            if (currentStack.getItem() == MarsItems.deshPickaxe && EnchantmentHelper.getSilkTouchModifier(player))
            {
                ItemStack itemstack = new ItemStack(MarsItems.deshPickSlime, 1, currentStack.getItemDamage());
                if (currentStack.getTagCompound() != null)
                {
                    itemstack.setTagCompound((NBTTagCompound) currentStack.getTagCompound().copy());
                }
                player.setCurrentItemOrArmor(0, itemstack);
            }
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return this.icons[metadata % 6];
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

//    @Override
//    public Item getItemDropped(int meta, Random random, int par3)
//    {
//        return Item.getItemFromBlock(this);
//    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

//    @Override
//    public int quantityDropped(int meta, int fortune, Random random)
//    {
//        return 1;
//    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < BlockSlimelingEgg.names.length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntitySlimelingEgg();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        IBlockState state = world.getBlockState(pos);
        int metadata = state.getBlock().getMetaFromState(state);

        if (metadata == 3)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 0);
        }
        if (metadata == 4)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 1);
        }
        if (metadata == 5)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 2);
        }
        return super.getPickBlock(target, world, pos, player);
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
