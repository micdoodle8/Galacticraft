package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GCBlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.blocks.BlockSolar;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSolar;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
//import micdoodle8.mods.galacticraft.planets.venus.dimension.WorldProviderVenus;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public abstract class TileEntitySolar extends TileBaseUniversalElectricalSource implements IMultiBlock, IDisableableMachine, IInventoryDefaults, ISidedInventory, IConnector, INamedContainerProvider
{
    public static class TileEntitySolarT1 extends TileEntitySolar
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.solarPanel)
        public static TileEntityType<TileEntitySolarT1> TYPE;

        public TileEntitySolarT1()
        {
            super(TYPE);
            this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
            this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
            this.setTierGC(1);
//            this.initialised = true;
            this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        }

        @Override
        public ITextComponent getDisplayName()
        {
            return new TranslationTextComponent("container.solar_basic");
        }
    }

    public static class TileEntitySolarT2 extends TileEntitySolar
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCBlockNames.solarPanelAdvanced)
        public static TileEntityType<TileEntitySolarT2> TYPE;

        public TileEntitySolarT2()
        {
            super(TYPE);
            this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
            this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
            this.storage.setCapacity(30000);
            this.setTierGC(2);
//            this.initialised = true;
            this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        }

        @Override
        public ITextComponent getDisplayName()
        {
            return new TranslationTextComponent("container.solar_advanced");
        }
    }

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int solarStrength = 0;
    public float targetAngle;
    public float currentAngle;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int disableCooldown = 0;
    public static final int MAX_GENERATE_WATTS = 200;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int generateWatts = 0;

    //    protected boolean initialised = false;
    private boolean initialisedMulti = false;
    private AxisAlignedBB renderAABB;

    public TileEntitySolar(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void tick()
    {
//        if (!this.initialised)
//        {
//            int metadata = this.getBlockMetadata();
//            if (metadata >= BlockSolar.ADVANCED_METADATA)
//            {
//                this.storage.setCapacity(30000);
//                this.setTierGC(2);
//            }
//            this.initialised = true;
//        }

        if (!this.initialisedMulti)
        {
            this.initialisedMulti = this.initialiseMultiTiles(this.getPos(), this.world);
        }

        if (!this.world.isRemote)
        {
            this.receiveEnergyGC(null, this.generateWatts, false);
        }

        super.tick();

        if (!this.world.isRemote)
        {
            this.recharge(this.getInventory().get(0));

            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            if (!this.getDisabled(0) && this.ticks % 20 == 0)
            {
                this.solarStrength = 0;

                if (this.world.isDaytime() && (this.world.getDimension() instanceof IGalacticraftDimension || !this.world.isRaining() && !this.world.isThundering()))
                {
                    double distance = 100.0D;
                    double sinA = -Math.sin((this.currentAngle - 77.5D) / Constants.RADIANS_TO_DEGREES_D);
                    double cosA = Math.abs(Math.cos((this.currentAngle - 77.5D) / Constants.RADIANS_TO_DEGREES_D));

                    for (int x = -1; x <= 1; x++)
                    {
                        for (int z = -1; z <= 1; z++)
                        {
                            if (this.tierGC == 1)
                            {
                                if (this.world.canBlockSeeSky(this.getPos().add(x, 2, z)))
                                {
                                    boolean valid = true;

                                    for (int y = this.getPos().getY() + 3; y < 256; y++)
                                    {
                                        BlockPos atPos = new BlockPos(this.getPos().getX() + x, y, this.getPos().getZ() + z);
                                        BlockState state = this.world.getBlockState(atPos);

                                        if (state.getBlock().isOpaqueCube(state, world, atPos))
                                        {
                                            valid = false;
                                            break;
                                        }
                                    }

                                    if (valid)
                                    {
                                        this.solarStrength++;
                                    }
                                }
                            }
                            else
                            {
                                boolean valid = true;

                                BlockVec3 blockVec = new BlockVec3(this).translate(x, 3, z);
                                for (double d = 0.0D; d < distance; d++)
                                {
                                    BlockVec3 blockAt = blockVec.clone().translate((int) (d * sinA), (int) (d * cosA), 0);
                                    BlockState state = blockAt.getBlockState(this.world);

                                    if (state == null)
                                    {
                                        break;
                                    }

                                    if (state.getBlock().isOpaqueCube(state, world, blockAt.toBlockPos()))
                                    {
                                        valid = false;
                                        break;
                                    }
                                }

                                if (valid)
                                {
                                    this.solarStrength++;
                                }
                            }
                        }
                    }
                }
            }
        }

        float angle = this.world.getCelestialAngle(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
        float celestialAngle = (this.world.getCelestialAngle(1.0F) + angle) * 360.0F;
//        if (!(this.world.getDimension() instanceof DimensionSpaceStation)) celestialAngle += 12.5F; TODO Space stations
//        if (GalacticraftCore.isPlanetsLoaded && this.world.getDimension() instanceof WorldProviderVenus) celestialAngle = 180F - celestialAngle; TODO planets
        celestialAngle %= 360;
        boolean isDaytime = this.world.isDaytime() && (celestialAngle < 180.5F || celestialAngle > 359.5F)/* || this.world.getDimension() instanceof DimensionSpaceStation  TODO Space stations*/;

        if (this.tierGC == 1)
        {
            if (!isDaytime || this.world.isRaining() || this.world.isThundering())
            {
                this.targetAngle = 77.5F + 180.0F;
            }
            else
            {
                this.targetAngle = 77.5F;
            }
        }
        else
        {
            if (!isDaytime || this.world.isRaining() || this.world.isThundering())
            {
                this.targetAngle = 77.5F + 180F;
            }
            else if (celestialAngle > 27.5F && celestialAngle < 152.5F)
            {
                float difference = this.targetAngle - celestialAngle + 12.5F;

                this.targetAngle -= difference / 20.0F;
            }
            else if (celestialAngle <= 27.5F || celestialAngle > 270F)
            {
                this.targetAngle = 15F;
            }
            else if (celestialAngle >= 152.5F)
            {
                this.targetAngle = 140F;
            }
        }

        float difference = this.targetAngle - this.currentAngle;

        this.currentAngle += difference / 20.0F;

        if (!this.world.isRemote)
        {
            int generated = this.getGenerate();
            if (generated > 0)
            {
                this.generateWatts = Math.min(Math.max(generated, 0), TileEntitySolar.MAX_GENERATE_WATTS);
            }
            else
            {
                this.generateWatts = 0;
            }
        }

        this.produce();
    }

    protected boolean initialiseMultiTiles(BlockPos pos, World world)
    {
        //Client can create its own fake blocks and tiles - no need for networking in 1.8+
        if (world.isRemote)
        {
            this.onCreate(world, pos);
        }

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            TileEntity tile = world.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityFake)
            {
                ((TileEntityFake) tile).mainBlockPosition = pos;
            }
            else
            {
                result = false;
            }
        }
        return result;
    }

    public int getGenerate()
    {
        if (this.getDisabled(0))
        {
            return 0;
        }

        float angle = this.world.getCelestialAngle(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
        float celestialAngle = (this.world.getCelestialAngle(1.0F) + angle) * 360.0F;
//        if (!(this.world.getDimension() instanceof DimensionSpaceStation)) celestialAngle += 12.5F; TODO Space stations

//        if (GalacticraftCore.isPlanetsLoaded && this.world.getDimension() instanceof WorldProviderVenus) celestialAngle = 180F - celestialAngle; TODO Planets
        celestialAngle %= 360F;

        float difference = (180.0F - Math.abs((this.currentAngle + 12.5F) % 180F - celestialAngle)) / 180.0F;

        return MathHelper.floor(0.01F * difference * difference * (this.solarStrength * (Math.abs(difference) * 500.0F)) * this.getSolarBoost());
    }

    public float getSolarBoost()
    {
        float result = (float) (this.world.getDimension() instanceof ISolarLevel ? ((ISolarLevel) this.world.getDimension()).getSolarEnergyMultiplier() : 1.0F);
//        if (GalacticraftCore.isPlanetsLoaded && this.world.getDimension() instanceof WorldProviderVenus)
//        {
//            if (this.pos.getY() > 90)
//            {
//                result += (this.pos.getY() - 90) / 1000F;   //Small improvement on Venus at higher altitudes
//            }
//        } TODO Planets
        return result;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity entityPlayer)
    {
        return ActionResultType.PASS; // TODO
//        return this.getBlockType().onBlockActivated(this.world, this.getPos(), this.world.getBlockState(getPos()), entityPlayer, EnumFacing.DOWN, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        if (positions.size() > 0)
        {
            ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions.get(0), placedPosition, EnumBlockMultiType.SOLAR_PANEL_0);
            positions.remove(0);
        }
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return (this.getTierGC() == 1) ? EnumBlockMultiType.SOLAR_PANEL_1 : EnumBlockMultiType.SOLAR_PANEL_0;
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.world.getHeight() - 1;
        int y = placedPosition.getY() + 1;
        if (y > buildHeight)
        {
            return;
        }
        positions.add(new BlockPos(placedPosition.getX(), y, placedPosition.getZ()));

        y++;
        if (y > buildHeight)
        {
            return;
        }
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            BlockState stateAt = this.world.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock)
            {
                EnumBlockMultiType type = stateAt.get(BlockMulti.MULTI_TYPE);
                if ((type == EnumBlockMultiType.SOLAR_PANEL_0 || type == EnumBlockMultiType.SOLAR_PANEL_1))
                {
                    if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
                    {
                        Minecraft.getInstance().particles.addBlockDestroyEffects(pos, GCBlocks.solarPanel.getDefaultState());
                    }

                    this.world.removeBlock(pos, false);
                }
            }
        }

        this.world.destroyBlock(getPos(), true);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.storage.setCapacity(nbt.getFloat("maxEnergy"));
        this.currentAngle = nbt.getFloat("currentAngle");
        this.targetAngle = nbt.getFloat("targetAngle");
        this.setDisabled(0, nbt.getBoolean("disabled"));
        this.disableCooldown = nbt.getInt("disabledCooldown");

//        this.initialised = false;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putFloat("maxEnergy", this.getMaxEnergyStoredGC());
        nbt.putFloat("currentAngle", this.currentAngle);
        nbt.putFloat("targetAngle", this.targetAngle);
        nbt.putInt("disabledCooldown", this.disableCooldown);
        nbt.putBoolean("disabled", this.getDisabled(0));

        return nbt;
    }

	/*@Override
    public float getRequest(EnumFacing direction)
	{
		return 0;
	}
	*/

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockSolar)
        {
            return state.get(BlockSolar.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.of(getFront());
    }

    @Override
    public Direction getElectricOutputDirection()
    {
        return getFront();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 4, getPos().getZ() + 2);
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 20;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStoredGC() * i / this.getMaxEnergyStoredGC());
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        return slotID == 0;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem());
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricOutputDirection();
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerSolar(containerId, playerInv, this);
    }
}
