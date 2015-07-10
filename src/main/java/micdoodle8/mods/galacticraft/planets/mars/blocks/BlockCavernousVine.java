package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCavernousVine extends Block implements IShearable, ItemBlockDesc.IBlockShiftDesc
{
//    @SideOnly(Side.CLIENT)
//    private IIcon[] vineIcons;

    public BlockCavernousVine(String assetName)
    {
        super(Material.vine);
        this.setLightLevel(1.0F);
        this.setTickRandomly(true);
        this.setStepSound(soundTypeGrass);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (world.setBlockToAir(pos))
        {
            int y2 = pos.getY() - 1;
            while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == this)
            {
                world.setBlockToAir(new BlockPos(pos.getX(), y2, pos.getZ()));
                y2--;
            }

            return true;
        }

        return false;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        Block blockAbove = worldIn.getBlockState(pos.up()).getBlock();
        return (blockAbove == this || blockAbove.getMaterial().isSolid());
    }

	@Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

		if (!this.canBlockStay(worldIn, pos))
		{
            worldIn.setBlockToAir(pos);
		}
	}

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase)
        {
            if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).capabilities.isFlying)
            {
                return;
            }

            entityIn.motionY = 0.06F;
            entityIn.rotationYaw += 0.4F;

            if (!((EntityLivingBase) entityIn).getActivePotionEffects().contains(Potion.poison))
            {
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(Potion.poison.id, 5, 20, false, true));
            }
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.vineIcons = new IIcon[3];

        for (int i = 0; i < 3; i++)
        {
            this.vineIcons[i] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "vine_" + i);
        }
    }*/

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
        return this.getVineLight(world, pos);
    }

    /*@Override
    public IIcon getIcon(int side, int meta)
    {
        if (meta < 3)
        {
            return this.vineIcons[meta];
        }

        return super.getIcon(side, meta);
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftPlanets.getBlockRenderID(this);
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
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing)
    {
        return facing == EnumFacing.DOWN && this.isBlockSolid(world, pos.up(), facing);
    }

    public int getVineLength(IBlockAccess world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.vine)
        {
            vineCount++;
            y2++;
        }

        return vineCount;
    }

    public int getVineLight(IBlockAccess world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.vine)
        {
            vineCount += 4;
            y2--;
        }

        return Math.max(19 - vineCount, 0);
    }

    @Override
    public int tickRate(World par1World)
    {
        return 50;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            for (int y2 = pos.getY() - 1; y2 >= pos.getY() - 2; y2--)
            {
                BlockPos pos1 = new BlockPos(pos.getX(), y2, pos.getZ());
                Block blockID = worldIn.getBlockState(pos1).getBlock();

                if (!blockID.isAir(worldIn, pos1))
                {
                    return;
                }
            }

            worldIn.setBlockState(pos.down(), this.getStateFromMeta(this.getVineLength(worldIn, pos) % 3), 2);
            worldIn.checkLight(pos);
        }

    }

//    @Override
//    public void onBlockAdded(World world, int x, int y, int z)
//    {
//        if (!world.isRemote)
//        {
//            // world.scheduleBlockUpdate(x, y, z, this,
//            // this.tickRate(world) + world.rand.nextInt(10));
//        }
//    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.air);
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, 0));
        return ret;
    }

    @Override
    public boolean isLadder(IBlockAccess world, BlockPos pos, EntityLivingBase entity)
    {
        return true;
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
