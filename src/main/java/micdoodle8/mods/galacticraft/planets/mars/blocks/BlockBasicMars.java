package micdoodle8.mods.galacticraft.planets.mars.blocks;

import com.google.common.base.Predicate;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityDungeonSpawnerMars;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockBasicMars extends Block implements IDetectableResource, IPlantableBlock, ITileEntityProvider, ITerraformableBlock
{
//    @SideOnly(Side.CLIENT)
//    private IIcon[] marsBlockIcons;

    //Metadata values:
    //0 copper ore, 1 tin ore, 2 desh ore, 3 iron ore
    //4 cobblestone, 5 top (surface rock), 6 middle, 7 dungeon brick
    //8 desh decoration block
    //9 Mars stone
    //10 dungeon spawner (invisible)
    
    public MapColor getMapColor(int meta)
    {
        switch (meta)
        {
        case 7:
            return MapColor.greenColor;
        case 5:
            return MapColor.dirtColor;
        default:
            return MapColor.redColor;
        }
    }

    public BlockBasicMars(String assetName)
    {
        super(Material.rock);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock().getMetaFromState(state) == 10)
        {
            return null;
        }

        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock().getMetaFromState(state) == 10)
        {
            return AxisAlignedBB.fromBounds(pos.getX() + 0.0D, pos.getY() + 0.0D, pos.getZ() + 0.0D, pos.getX() + 0.0D, pos.getY() + 0.0D, pos.getZ() + 0.0D);
        }

        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        IBlockState state = world.getBlockState(pos);
        int metadata = state.getBlock().getMetaFromState(state);

        if (metadata == 10)
        {
            return 10000.0F;
        }

        if (metadata == 7)
        {
            return 40.0F;
        }

        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.marsBlockIcons = new IIcon[11];
        this.marsBlockIcons[0] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "cobblestone");
        this.marsBlockIcons[1] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "decoration_desh");
        this.marsBlockIcons[2] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "middle");
        this.marsBlockIcons[3] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "brick");
        this.marsBlockIcons[4] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "top");
        this.marsBlockIcons[5] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "copper");
        this.marsBlockIcons[6] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "desh");
        this.marsBlockIcons[7] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "tin");
        this.marsBlockIcons[8] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "bottom");
        this.marsBlockIcons[9] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "iron");
        this.marsBlockIcons[10] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "blank");
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        final int meta = state.getBlock().getMetaFromState(state);

        if (meta == 7)
        {
            return 4.0F;
        }

        if (meta == 10)
        {
            return -1.0F;
        }

        return this.blockHardness;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (state.getBlock().getMetaFromState(state) == 10)
        {
            return new TileEntityDungeonSpawnerMars();
        }

        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return null;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        IBlockState state = world.getBlockState(pos);
        int meta = state.getBlock().getMetaFromState(state);
        if (meta == 10)
        {
            return false;
        }

        return super.canHarvestBlock(world, pos, player);
    }

    /*@Override
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
        case 0:
            return this.marsBlockIcons[5];
        case 1:
            return this.marsBlockIcons[7];
        case 2:
            return this.marsBlockIcons[6];
        case 3:
            return this.marsBlockIcons[9];
        case 4:
            return this.marsBlockIcons[0];
        case 5:
            return this.marsBlockIcons[4];
        case 6:
            return this.marsBlockIcons[2];
        case 7:
            return this.marsBlockIcons[3];
        case 8:
            return this.marsBlockIcons[1];
        case 9:
            return this.marsBlockIcons[8];
        case 10:
            return this.marsBlockIcons[10];
        }

        return this.marsBlockIcons[1];
    }*/

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        int meta = state.getBlock().getMetaFromState(state);
        if (meta == 2)
        {
            return MarsItems.marsItemBasic;
        }
        else if (meta == 10)
        {
            return Item.getItemFromBlock(Blocks.air);
        }

        return Item.getItemFromBlock(this);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int meta = state.getBlock().getMetaFromState(state);
        if (meta == 9)
        {
            return 4;
        }
        else if (meta == 2)
        {
            return 0;
        }
        else
        {
            return meta;
        }
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        int meta = state.getBlock().getMetaFromState(state);
        if (meta == 10)
        {
            return 0;
        }
        else if (meta == 2 && fortune >= 1)
        {
            return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
        }

        return 1;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        int var4;

        for (var4 = 0; var4 < 11; ++var4)
        {
            if (var4 != 10)
            {
                par3List.add(new ItemStack(par1, 1, var4));
            }
        }
    }

    @Override
    public boolean isValueable(IBlockState state)
    {
        switch (this.getMetaFromState(state))
        {
        case 0:
        case 1:
        case 2:
        case 3:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        return false;
    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(int metadata)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (rand.nextInt(10) == 0)
        {
            int metadata = state.getBlock().getMetaFromState(state);

            if (metadata == 7)
            {
                GalacticraftPlanets.spawnParticle("sludgeDrip", new Vector3(pos.getX() + rand.nextDouble(), pos.getY(), pos.getZ() + rand.nextDouble()), new Vector3(0, 0, 0));

                if (rand.nextInt(100) == 0)
                {
                    worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), GalacticraftCore.TEXTURE_PREFIX + "ambience.singledrip", 1, 0.8F + rand.nextFloat() / 5.0F, false);
                }
            }
        }
    }

    @Override
    public boolean isTerraformable(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().getMetaFromState(state) == 5 && !world.getBlockState(pos.up()).getBlock().isFullCube();
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return state.getBlock().getMetaFromState(state) < 10;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        IBlockState state = world.getBlockState(pos);
        int metadata = state.getBlock().getMetaFromState(state);
        if (metadata == 2)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
        }
        if (metadata == 9)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
        }
        if (metadata == 10)
        {
            return null;
        }

        return super.getPickBlock(target, world, pos, player);
    }
    
    @Override
    public boolean isReplaceableOreGen(World world, BlockPos pos, Predicate<IBlockState> target)
    {
        if (target != Blocks.stone) return false;
        IBlockState state = world.getBlockState(pos);
    	int meta = state.getBlock().getMetaFromState(state);
    	return (meta == 6 || meta == 9);
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return state.getBlock().getMetaFromState(state) == 10;
    }
}
