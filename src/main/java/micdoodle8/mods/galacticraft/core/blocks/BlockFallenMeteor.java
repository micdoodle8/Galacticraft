package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFallenMeteor;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFallenMeteor extends Block implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public BlockFallenMeteor(String assetName)
    {
        super(Material.rock);
        this.setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
        this.setHardness(50.0F);
        this.setStepSound(Block.soundTypeStone);
        this.setUnlocalizedName(assetName);
    }

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

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        return 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return GCItems.meteoricIronRaw;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityFallenMeteor)
        {
            TileEntityFallenMeteor meteor = (TileEntityFallenMeteor) tile;

            if (meteor.getHeatLevel() <= 0)
            {
                return;
            }

            if (entityIn instanceof EntityLivingBase)
            {
                final EntityLivingBase livingEntity = (EntityLivingBase) entityIn;

                worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int var5 = 0; var5 < 8; ++var5)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + 0.2D + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                if (!livingEntity.isBurning())
                {
                    livingEntity.setFire(2);
                }

                double var9 = pos.getX() + 0.5F - livingEntity.posX;
                double var7;

                for (var7 = livingEntity.posZ - pos.getZ(); var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D)
                {
                    var9 = (Math.random() - Math.random()) * 0.01D;
                }

                livingEntity.knockBack(livingEntity, 1, var9, var7);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            this.tryToFall(worldIn, pos, state);
        }
    }

    private void tryToFall(World world, BlockPos pos, IBlockState state)
    {
        if (this.canFallBelow(world, pos.down()) && pos.getY() >= 0)
        {
            int prevHeatLevel = ((TileEntityFallenMeteor) world.getTileEntity(pos)).getHeatLevel();
            world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
            BlockPos blockpos1;

            for (blockpos1 = pos.down(); this.canFallBelow(world, blockpos1) && blockpos1.getY() > 0; blockpos1 = blockpos1.down()) {}

            if (blockpos1.getY() >= 0)
            {
                world.setBlockState(blockpos1.up(), state, 3);
                ((TileEntityFallenMeteor) world.getTileEntity(blockpos1.up())).setHeatLevel(prevHeatLevel);
            }
        }
    }

    private boolean canFallBelow(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();

        if (block.getMaterial() == Material.air)
        {
            return true;
        }
        else if (block == Blocks.fire)
        {
            return true;
        }
        else
        {
            return block.getMaterial() == Material.water ? true : block.getMaterial() == Material.lava;
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityFallenMeteor)
        {
            TileEntityFallenMeteor meteor = (TileEntityFallenMeteor) tile;

            Vector3 col = new Vector3(198, 108, 58);
            col.translate(200 - meteor.getScaledHeatLevel() * 200);
            col.x = Math.min(255, col.x);
            col.y = Math.min(255, col.y);
            col.z = Math.min(255, col.z);

            return ColorUtil.to32BitColor(255, (byte) col.x, (byte) col.y, (byte) col.z);
        }

        return super.colorMultiplier(worldIn, pos, renderPass);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityFallenMeteor();
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
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

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
