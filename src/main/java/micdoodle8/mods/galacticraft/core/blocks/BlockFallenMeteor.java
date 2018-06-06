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
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFallenMeteor extends Block implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.15, 0.05, 0.15, 0.85, 0.75, 0.85);
    
    public BlockFallenMeteor(String assetName)
    {
        super(Material.ROCK);
        this.setHardness(40.0F);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        return 1 + (int) (par2Random.nextFloat() + 0.75F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return GCItems.meteoricIronRaw;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
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

                worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

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
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack)
    {
        world.setBlockState(pos, this.getDefaultState(), 3);
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityFallenMeteor)
        {
            ((TileEntityFallenMeteor) tile).setHeatLevel(0);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
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
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
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

        if (block.getMaterial(world.getBlockState(pos)) == Material.AIR)
        {
            return true;
        }
        else if (block == Blocks.FIRE)
        {
            return true;
        }
        else
        {
            return block.getMaterial(world.getBlockState(pos)) == Material.WATER ? true : block.getMaterial(world.getBlockState(pos)) == Material.LAVA;
        }
    }

    public static int colorMultiplier(IBlockAccess worldIn, BlockPos pos)
    {
        if (worldIn != null && pos != null)
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
        }

        return 16777215;
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
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos)
    {
        float hardness = this.getBlockHardness(state, world, pos);
        if (hardness < 0.0F)
        {
            return 0.0F;
        }

        int power = canHarvestBlock(this, player, state);
        if (power > 0)
        {
            return power * player.getDigSpeed(state, pos) / hardness / 30F;
        }
        else
        {
            return player.getDigSpeed(state, pos) / hardness / 30F;
        }
    }

    public int canHarvestBlock(Block block, EntityPlayer player, IBlockState state)
    {
        ItemStack stack = player.inventory.getCurrentItem();
        String tool = block.getHarvestTool(state);
        if (stack.isEmpty() || tool == null)
        {
            return player.canHarvestBlock(state) ? 1 : 0;
        }

        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state) - block.getHarvestLevel(state) + 1;
        if (toolLevel < 1)
        {
            return player.canHarvestBlock(state) ? 1 : 0;
        }

        return toolLevel;
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

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        if (state.getBlock() != this) return 0;
        
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, 3, 7);
    }
}
