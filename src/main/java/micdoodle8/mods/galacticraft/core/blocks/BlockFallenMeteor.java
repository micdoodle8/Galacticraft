package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFallenMeteor;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFallenMeteor extends Block implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
    public BlockFallenMeteor(String assetName)
    {
        super(Material.rock);
        this.setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
        this.setHardness(50.0F);
        this.setStepSound(Block.soundTypeStone);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "fallen_meteor");
    }*/

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
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
            this.tryToFall(worldIn, pos);
        }
    }

    private void tryToFall(World worldIn, BlockPos pos)
    {
        float yPos = pos.getY() - 1;
        if (BlockFallenMeteor.canFallBelow(worldIn, pos.offset(EnumFacing.DOWN)) && pos.getY() >= 0)
        {
            int prevHeatLevel = ((TileEntityFallenMeteor) worldIn.getTileEntity(pos)).getHeatLevel();
            worldIn.setBlockToAir(pos);

            while (BlockFallenMeteor.canFallBelow(worldIn, new BlockPos(pos.getX(), yPos, pos.getZ())) && yPos > 0)
            {
                --yPos;
            }

            if (yPos > 0)
            {
                worldIn.setBlockState(pos, this.getDefaultState(), 3);
                ((TileEntityFallenMeteor) worldIn.getTileEntity(pos)).setHeatLevel(prevHeatLevel);
            }
        }
    }

    public static boolean canFallBelow(World worldIn, BlockPos pos)
    {
        final Block var4 = worldIn.getBlockState(pos).getBlock();

        if (var4.getMaterial() == Material.air)
        {
            return true;
        }
        else if (var4 == Blocks.fire)
        {
            return true;
        }
        else
        {
            final Material var5 = var4.getMaterial();
            return var5 == Material.water || var5 == Material.lava;
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
    public boolean canSilkHarvest()
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
