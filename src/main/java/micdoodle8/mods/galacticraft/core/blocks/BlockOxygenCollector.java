package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockOxygenCollector extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Direction.Plane.HORIZONTAL);

    public BlockOxygenCollector(Properties builder)
    {
        super(builder);
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, Direction side, float hitX, float hitY, float hitZ)
    {
        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, BlockState state)
    {
        return new TileEntityOxygenCollector();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = Direction.getHorizontal(angle).getOpposite().getHorizontalIndex();
        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityOxygenCollector)
        {
            if (((TileEntityOxygenCollector) tile).lastOxygenCollected > 1)
            {
                for (int particleCount = 0; particleCount < 10; particleCount++)
                {
                    double x2 = pos.getX() + rand.nextFloat();
                    double y2 = pos.getY() + rand.nextFloat();
                    double z2 = pos.getZ() + rand.nextFloat();
                    double mX = 0.0D;
                    double mY = 0.0D;
                    double mZ = 0.0D;
                    int dir = rand.nextInt(2) * 2 - 1;
                    mX = (rand.nextFloat() - 0.5D) * 0.5D;
                    mY = (rand.nextFloat() - 0.5D) * 0.5D;
                    mZ = (rand.nextFloat() - 0.5D) * 0.5D;

                    final int meta = getMetaFromState(stateIn);

                    if (meta == 3 || meta == 2)
                    {
                        x2 = pos.getX() + 0.5D + 0.25D * dir;
                        mX = rand.nextFloat() * 2.0F * dir;
                    }
                    else
                    {
                        z2 = pos.getZ() + 0.5D + 0.25D * dir;
                        mZ = rand.nextFloat() * 2.0F * dir;
                    }

                    GalacticraftCore.proxy.spawnParticle("oxygen", new Vector3(x2, y2, z2), new Vector3(mX, mY, mZ), new Object[] { new Vector3(0.7D, 0.7D, 1.0D) });
                }
            }
        }
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
    public BlockState getStateFromMeta(int meta)
    {
        Direction enumfacing = Direction.getHorizontal(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
