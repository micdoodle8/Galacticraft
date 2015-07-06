package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockBasicMoon extends BlockAdvancedTile implements IDetectableResource, IPlantableBlock, ITerraformableBlock
{
    // CopperMoon: 0, TinMoon: 1, CheeseStone: 2
    // Moon dirt: 3;  Moon rock: 4;  Moon topsoil: 5-13 (6-13 have GC2 footprints);  Moon dungeon brick: 14;  Moon boss spawner: 15;

    public static final PropertyEnum BASIC_TYPE_MOON = PropertyEnum.create("basicTypeMoon", EnumBlockBasicMoon.class);

    private enum EnumBlockBasicMoon
    {
        ORE_COPPER_MOON(0),
        ORE_TIN_MOON(1),
        ORE_CHEESE_MOON(2),
        MOON_DIRT(3),
        MOON_STONE(4),
        MOON_TURF_0(5),
        MOON_TURF_1(6),
        MOON_TURF_2(7),
        MOON_TURF_3(8),
        MOON_TURF_4(9),
        MOON_TURF_5(10),
        MOON_TURF_6(11),
        MOON_TURF_7(12),
        MOON_TURF_8(13),
        MOON_DUNGEON_BRICK(15),
        MOON_BOSS_SPAWNER(16);

        private final int meta;

        private EnumBlockBasicMoon(int meta)
        {
            this.meta = meta;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumBlockBasicMoon byMetadata(int meta)
        {
            for (EnumBlockBasicMoon value : values())
            {
                if (value.getMeta() == meta)
                {
                    return value;
                }
            }

            return null;
        }
    }

    /*@SideOnly(Side.CLIENT)
    private IIcon[] moonBlockIcons;*/

    public BlockBasicMoon()
    {
        super(Material.rock);
        this.blockHardness = 1.5F;
        this.blockResistance = 2.5F;
        /*this.setUnlocalizedName("moonBlock");*/
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        if (getMetaFromState(state) == 15)
        {
            return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }

        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos)
    {
        if (getMetaFromState(world.getBlockState(pos)) == 15)
        {
            return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }

        return super.getSelectedBoundingBox(world, pos);
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, BlockPos pos)
    {
        if (getMetaFromState(world.getBlockState(pos)) == 15)
        {
            return false;
        }
        else
        {
            return super.isNormalCube(world, pos);
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.moonBlockIcons = new IIcon[17];
        this.moonBlockIcons[0] = par1IconRegister.registerIcon("galacticraftmoon:top");
        this.moonBlockIcons[1] = par1IconRegister.registerIcon("galacticraftmoon:brick");
        this.moonBlockIcons[2] = par1IconRegister.registerIcon("galacticraftmoon:middle");
        this.moonBlockIcons[3] = par1IconRegister.registerIcon("galacticraftmoon:top_side");
        this.moonBlockIcons[4] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_1");
        this.moonBlockIcons[5] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_2");
        this.moonBlockIcons[6] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_3");
        this.moonBlockIcons[7] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_4");
        this.moonBlockIcons[8] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_5");
        this.moonBlockIcons[9] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_6");
        this.moonBlockIcons[10] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_7");
        this.moonBlockIcons[11] = par1IconRegister.registerIcon("galacticraftmoon:grass_step_8");
        this.moonBlockIcons[12] = par1IconRegister.registerIcon("galacticraftmoon:moonore_copper");
        this.moonBlockIcons[13] = par1IconRegister.registerIcon("galacticraftmoon:moonore_tin");
        this.moonBlockIcons[14] = par1IconRegister.registerIcon("galacticraftmoon:moonore_cheese");
        this.moonBlockIcons[15] = par1IconRegister.registerIcon("galacticraftmoon:bottom");
        this.moonBlockIcons[16] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "blank");
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
    	int metadata = getMetaFromState(world.getBlockState(pos));

    	if (metadata == 15)
        {
            return 10000.0F;
        }
    	else if (metadata == 14)
        {
            return 40.0F;
        }
    	else if (metadata == 4)
        {
            return 6.0F;
        }
    	else if (metadata < 3)
        {
            return 3.0F;
        }

        return this.blockResistance / 5.0F;
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        final int meta = getMetaFromState(worldIn.getBlockState(pos));

        if (meta == 3 || meta >= 5 && meta <= 13)
        {
            return 0.5F;
        }

        if (meta == 14)
        {
            return 4.0F;
        }

        if (meta > 13)
        {
            return -1F;
        }

        if (meta < 2)
        {
            return 5.0F;
        }

        if (meta == 2)
        {
            return 3.0F;
        }

        return this.blockHardness;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        int meta = getMetaFromState(world.getBlockState(pos));
        if (meta == 3 || meta >= 5 && meta <= 13)
        {
            return true;
        }

        return super.canHarvestBlock(world, pos, player);
    }

    /*@SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        if (meta >= 5 && meta <= 13)
        {
            if (side == 1)
            {
                switch (meta - 5)
                {
                case 0:
                    return this.moonBlockIcons[0];
                case 1:
                    return this.moonBlockIcons[4];
                case 2:
                    return this.moonBlockIcons[5];
                case 3:
                    return this.moonBlockIcons[6];
                case 4:
                    return this.moonBlockIcons[7];
                case 5:
                    return this.moonBlockIcons[8];
                case 6:
                    return this.moonBlockIcons[9];
                case 7:
                    return this.moonBlockIcons[10];
                case 8:
                    return this.moonBlockIcons[11];
                }
            }
            else if (side == 0)
            {
                return this.moonBlockIcons[2];
            }
            else
            {
                return this.moonBlockIcons[3];
            }
        }
        else
        {
            switch (meta)
            {
            case 0:
                return this.moonBlockIcons[12];
            case 1:
                return this.moonBlockIcons[13];
            case 2:
                return this.moonBlockIcons[14];
            case 3:
                return this.moonBlockIcons[2];
            case 4:
                return this.moonBlockIcons[15];
            case 14:
                return this.moonBlockIcons[1];
            case 15:
                return this.moonBlockIcons[16];
            default:
                return this.moonBlockIcons[16];
            }
        }

        return null;
    }*/

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        switch (getMetaFromState(state))
        {
        case 2:
            return GCItems.cheeseCurd;
        case 15:
            return Item.getItemFromBlock(Blocks.air);
        default:
            return Item.getItemFromBlock(this);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int meta = getMetaFromState(state);
        if (meta >= 5 && meta <= 13)
        {
            return 5;
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
        int meta = getMetaFromState(state);
        switch (meta)
        {
        case 2:
            if (fortune >= 1)
            {
                return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
            }
            return 1;
        case 15:
            return 0;
        default:
            return 1;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        int var4;

        for (var4 = 0; var4 < 6; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }

        for (var4 = 14; var4 < 15; var4++)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (getMetaFromState(state) == 15)
        {
            return new TileEntityDungeonSpawner();
        }

        return null;
    }

    @Override
    public boolean isValueable(int metadata)
    {
        switch (metadata)
        {
        case 0:
            return true;
        case 1:
            return true;
        case 2:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        final int metadata = getMetaFromState(world.getBlockState(pos));

        if (metadata < 5 && metadata > 13)
        {
            return false;
        }

        plantable.getPlant(world, pos.offset(EnumFacing.UP));

        return plantable instanceof BlockFlower;

    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(int metadata)
    {
        return metadata >= 5 && metadata <= 13;

    }

    @Override
    public boolean isTerraformable(World world, BlockPos pos)
    {
        int meta = getMetaFromState(world.getBlockState(pos));

        if (meta >= 5 && meta <= 13)
        {
            return world.getBlockState(pos.offset(EnumFacing.UP)).getBlock() instanceof BlockAir;
        }

        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));
        if (metadata == 2)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
        }
        if (metadata == 15)
        {
            return null;
        }

        return super.getPickBlock(target, world, pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote && getMetaFromState(state) == 5)
        {
            Map<Long, List<Footprint>> footprintChunkMap = TickHandlerServer.serverFootprintMap.get(worldIn.provider.getDimensionId());

            if (footprintChunkMap != null)
            {
                long chunkKey = ChunkCoordIntPair.chunkXZ2Int(pos.getX() >> 4, pos.getZ() >> 4);
                List<Footprint> footprintList = footprintChunkMap.get(chunkKey);

                if (footprintList != null && !footprintList.isEmpty())
                {
                    List<Footprint> toRemove = new ArrayList<Footprint>();

                    for (Footprint footprint : footprintList)
                    {
                        if (footprint.position.x > pos.getX() && footprint.position.x < pos.getX() + 1 &&
                                footprint.position.z > pos.getZ() && footprint.position.z < pos.getZ() + 1)
                        {
                            toRemove.add(footprint);
                        }
                    }

                    if (!toRemove.isEmpty())
                    {
                        footprintList.removeAll(toRemove);
                        footprintChunkMap.put(chunkKey, footprintList);
                        TickHandlerServer.serverFootprintMap.put(worldIn.provider.getDimensionId(), footprintChunkMap);
                        TickHandlerServer.footprintRefreshList.add(new NetworkRegistry.TargetPoint(worldIn.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), 50));
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isReplaceableOreGen(World world, BlockPos pos, com.google.common.base.Predicate<IBlockState> target)
    {
        if (target != Blocks.stone) return false;
    	int meta = getMetaFromState(world.getBlockState(pos));
    	return (meta == 3 || meta == 4);
    }
}
