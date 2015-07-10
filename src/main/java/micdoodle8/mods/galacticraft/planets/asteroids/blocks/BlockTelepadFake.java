package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTelepadFake;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockTelepadFake extends BlockAdvancedTile implements ITileEntityProvider
{
    public BlockTelepadFake(String assetName)
    {
        super(GCBlocks.machine);
        this.setStepSound(Block.soundTypeMetal);
//        this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
//        this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + "launch_pad");
        this.setResistance(1000000000000000.0F);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        int meta = state.getBlock().getMetaFromState(state);

        if (meta == 0)
        {
            this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (meta == 1)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        int meta = state.getBlock().getMetaFromState(state);

        if (meta == 0)
        {
            this.setBlockBounds(0.0F, 0.55F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        else if (meta == 1)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        else
        {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public boolean canDropFromExplosion(Explosion par1Explosion)
    {
        return false;
    }

    public void makeFakeBlock(World worldObj, BlockPos pos, BlockPos mainBlock, IBlockState state)
    {
        worldObj.setBlockState(pos, state, 3);
        ((TileEntityTelepadFake) worldObj.getTileEntity(pos)).setMainBlock(mainBlock);
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity != null)
        {
            BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                return worldIn.getBlockState(pos).getBlock().getBlockHardness(worldIn, pos);
            }
        }

        return this.blockHardness;
    }

//    @Override
//    public Block setBlockTextureName(String name)
//    {
//        this.textureName = name;
//        return this;
//    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            ((TileEntityTelepadFake) tileEntity).onBlockRemoval();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityTelepadFake tileEntity = (TileEntityTelepadFake) worldIn.getTileEntity(pos);
        return tileEntity.onActivated(playerIn);
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int meta)
    {
        return new TileEntityTelepadFake();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            Block mainBlockID = world.getBlockState(mainBlockPosition).getBlock();

            if (Blocks.air != mainBlockID)
            {
                return mainBlockID.getPickBlock(target, world, mainBlockPosition, player);
            }
        }

        return null;
    }

    @Override
    public EnumFacing getBedDirection(IBlockAccess world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            return world.getBlockState(pos).getBlock().getBedDirection(world, mainBlockPosition);
        }

        return (EnumFacing)getActualState(world.getBlockState(pos), world, pos).getValue(BlockDirectional.FACING);
    }

    @Override
    public boolean isBed(IBlockAccess world, BlockPos pos, Entity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            return world.getBlockState(pos).getBlock().isBed(world, mainBlockPosition, player);
        }

        return super.isBed(world, pos, player);
    }

    @Override
    public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null)
        {
            world.getBlockState(pos).getBlock().setBedOccupied(world, mainBlockPosition, player, occupied);
        }
        else
        {
            super.setBedOccupied(world, pos, player, occupied);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        IBlockState state = worldObj.getBlockState(target.getBlockPos());
        if (state.getBlock().getMetaFromState(state) == 6)
        {
            return true;
        }

        TileEntity tileEntity = worldObj.getTileEntity(target.getBlockPos());

        if (tileEntity instanceof TileEntityTelepadFake)
        {
            BlockPos mainBlockPosition = ((TileEntityTelepadFake) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null)
            {
                effectRenderer.addBlockHitEffects(mainBlockPosition, target);
            }
        }

        return super.addHitEffects(worldObj, target, effectRenderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().getMetaFromState(state) == 6)
        {
            return true;
        }

        return super.addDestroyEffects(world, pos, effectRenderer);
    }
}