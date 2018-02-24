package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor.EnumGearType;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterOxygenInfinite;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashSet;

public class OxygenUtil
{
    private static HashSet<BlockPos> checked;

    @SideOnly(Side.CLIENT)
    public static boolean shouldDisplayTankGui(GuiScreen gui)
    {
        if (FMLClientHandler.instance().getClient().gameSettings.hideGUI)
        {
            return false;
        }

        if (gui == null)
        {
            return true;
        }

        if (gui instanceof GuiInventory)
        {
            return false;
        }

        return gui instanceof GuiChat;

    }

    public static boolean isAABBInBreathableAirBlock(EntityLivingBase entity)
    {
        return isAABBInBreathableAirBlock(entity, false);
    }

    public static boolean isAABBInBreathableAirBlock(EntityLivingBase entity, boolean testThermal)
    {
        double y = entity.posY + entity.getEyeHeight();
        double x = entity.posX;
        double z = entity.posZ;

        double sx = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
        double sy = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY;
        double sz = entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ;

        //A good first estimate of head size is that it's the smallest of the entity's 3 dimensions (e.g. front to back, for Steve)
        double smin = Math.min(sx, Math.min(sy, sz)) / 2;
        double offsetXZ = 0.0;

        // If entity is within air lock wall, check adjacent blocks for oxygen
        // The value is equal to the max distance from an adjacent oxygen block to the edge of a sealer wall
        if (entity.worldObj.getBlockState(entity.getPosition()).getBlock() == GCBlocks.airLockSeal)
        {
            offsetXZ = 0.75F;
        }

        return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, AxisAlignedBB.fromBounds(x - smin - offsetXZ, y - smin, z - smin - offsetXZ, x + smin + offsetXZ, y + smin, z + smin + offsetXZ), testThermal);
    }

    public static boolean isAABBInBreathableAirBlock(World world, AxisAlignedBB bb)
    {
        return isAABBInBreathableAirBlock(world, bb, false);
    }

    public static boolean isAABBInBreathableAirBlock(World world, AxisAlignedBB bb, boolean testThermal)
    {
        final double avgX = (bb.minX + bb.maxX) / 2.0D;
        final double avgY = (bb.minY + bb.maxY) / 2.0D;
        final double avgZ = (bb.minZ + bb.maxZ) / 2.0D;

        if (testThermal)
        {
            return OxygenUtil.isInOxygenAndThermalBlock(world, bb);
        }

        if (OxygenUtil.inOxygenBubble(world, avgX, avgY, avgZ))
        {
            return true;
        }

        return OxygenUtil.isInOxygenBlock(world, bb);
    }

    public static boolean isInOxygenBlock(World world, AxisAlignedBB bb)
    {
        int xm = MathHelper.floor_double(bb.minX + 0.001D);
        int xx = MathHelper.floor_double(bb.maxX - 0.001D);
        int ym = MathHelper.floor_double(bb.minY + 0.001D);
        int yy = MathHelper.floor_double(bb.maxY - 0.001D);
        int zm = MathHelper.floor_double(bb.minZ + 0.001D);
        int zz = MathHelper.floor_double(bb.maxZ - 0.001D);

        OxygenUtil.checked = new HashSet<>();
        if (world.isAreaLoaded(new BlockPos(xm, ym, zm), new BlockPos(xx, yy, zz)))
        {
            for (int x = xm; x <= xx; ++x)
            {
                for (int z = zm; z <= zz; ++z)
                {
                    for (int y = ym; y <= yy; ++y)
                    {
                        BlockPos pos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(pos).getBlock();
                        if (OxygenUtil.testContactWithBreathableAir(world, block, pos, 0) >= 0)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isInOxygenAndThermalBlock(World world, AxisAlignedBB bb)
    {
        int i = MathHelper.floor_double(bb.minX + 0.001D);
        int j = MathHelper.floor_double(bb.maxX - 0.001D);
        int k = MathHelper.floor_double(bb.minY + 0.001D);
        int l = MathHelper.floor_double(bb.maxY - 0.001D);
        int i1 = MathHelper.floor_double(bb.minZ + 0.001D);
        int j1 = MathHelper.floor_double(bb.maxZ - 0.001D);

        OxygenUtil.checked = new HashSet<>();
        if (world.isAreaLoaded(new BlockPos(i, k, i1), new BlockPos(j, l, j1)))
        {
            for (int x = i; x <= j; ++x)
            {
                for (int y = k; y <= l; ++y)
                {
                    for (int z = i1; z <= j1; ++z)
                    {
                        BlockPos pos = new BlockPos(x, y, z);
                        Block block = world.getBlockState(pos).getBlock();
                        if (OxygenUtil.testContactWithBreathableAir(world, block, pos, 0) == 1) // Thermal air has metadata 1
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /*
     * A simplified version of the breathable air check which checks
     * all 6 sides of the given block (because a torch can pass air on all sides)
     * Used in BlockUnlitTorch.
     */
    public static boolean checkTorchHasOxygen(World world, BlockPos pos)
    {
        if (OxygenUtil.inOxygenBubble(world, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D))
        {
            return true;
        }
        OxygenUtil.checked = new HashSet<>();
        BlockVec3 vec = new BlockVec3(pos);
        for (int side = 0; side < 6; side++)
        {
            BlockVec3 sidevec = vec.newVecSide(side);
            Block newblock = sidevec.getBlockID_noChunkLoad(world);
            if (OxygenUtil.testContactWithBreathableAir(world, newblock, sidevec.toBlockPos(), 1) >= 0)
            {
                return true;
            }
        }
        return false;
    }

    /*
     * Test whether the given block at (x,y,z) coordinates is either:
     * - breathable air (returns true)
     * - solid, or air which is not breathable (returns false)
     * - an air-permeable block, for example a torch, in which case test the surrounding
     * air-reachable blocks (up to 5 blocks away) and return true if breathable air is found
     * in one of them, or false if not.
     */
    private static synchronized int testContactWithBreathableAir(World world, Block block, BlockPos pos, int limitCount)
    {
        checked.add(pos);
        if (block == GCBlocks.breatheableAir || block == GCBlocks.brightBreatheableAir)
        {
            return block.getMetaFromState(world.getBlockState(pos));
        }

        if (block == null || block.getMaterial() == Material.air)
        {
            return -1;
        }

        //Test for non-sided permeable or solid blocks first
        boolean permeableFlag = false;
        if (!(block instanceof BlockLeavesBase))
        {
            if (block.isOpaqueCube())
            {
                if (block instanceof BlockGravel || block.getMaterial() == Material.cloth || block instanceof BlockSponge)
                {
                    permeableFlag = true;
                }
                else
                {
                    return -1;
                }
            }
            else if (block instanceof BlockGlass || block instanceof BlockStainedGlass)
            {
                return -1;
            }
            else if (block instanceof BlockLiquid)
            {
                return -1;
            }
            else if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(block))
            {
                ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
                IBlockState state = world.getBlockState(pos);
                if (metaList.contains(-1) || metaList.contains(state.getBlock().getMetaFromState(state)))
                {
                    return -1;
                }
            }
        }
        else
        {
            permeableFlag = true;
        }

        //Testing a non-air, permeable block (for example a torch or a ladder)
        if (limitCount < 5)
        {
            for (EnumFacing side : EnumFacing.VALUES)
            {
                if (permeableFlag || OxygenUtil.canBlockPassAirOnSide(world, block, pos, side))
                {
                    BlockPos sidevec = pos.add(side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ());
                    if (!checked.contains(sidevec))
                    {
                        Block newblock = world.getBlockState(sidevec).getBlock();
                        int adjResult = OxygenUtil.testContactWithBreathableAir(world, newblock, sidevec, limitCount + 1);
                        if (adjResult >= 0)
                        {
                            return adjResult;
                        }
                    }
                }
            }
        }

        return -1;
    }
    //TODO - performance, could add a 'safe' version of this code (inside world borders)

    //TODO - add more performance increase, these sided checks could be done once only
    private static boolean canBlockPassAirOnSide(World world, Block block, BlockPos vec, EnumFacing side)
    {
        if (block instanceof IPartialSealableBlock)
        {
            return !((IPartialSealableBlock) block).isSealed(world, vec, side);
        }

        //Half slab seals on the top side or the bottom side according to its metadata
        if (block instanceof BlockSlab)
        {
            IBlockState state = world.getBlockState(vec);
            int meta = state.getBlock().getMetaFromState(state);
            return !(side == EnumFacing.DOWN && (meta & 8) == 8 || side == EnumFacing.UP && (meta & 8) == 0);
        }

        //Farmland etc only seals on the solid underside
        if (block instanceof BlockFarmland || block instanceof BlockEnchantmentTable || block instanceof BlockLiquid)
        {
            return side != EnumFacing.UP;
        }

        if (block instanceof BlockPistonBase)
        {
            IBlockState state = world.getBlockState(vec);
            if (((Boolean) state.getValue(BlockPistonBase.EXTENDED)).booleanValue())
            {
                int meta0 = state.getBlock().getMetaFromState(state);
                EnumFacing facing = BlockPistonBase.getFacing(meta0);
                return side != facing;
            }
            return false;
        }

        return !block.isSideSolid(world, vec, EnumFacing.getFront(side.getIndex() ^ 1));
    }

    public static int getDrainSpacing(ItemStack tank, ItemStack tank2)
    {
        boolean tank1Valid = tank != null && tank.getItem() instanceof ItemOxygenTank && tank.getMaxDamage() - tank.getItemDamage() > 0;
        boolean tank2Valid = tank2 != null && tank2.getItem() instanceof ItemOxygenTank && tank2.getMaxDamage() - tank2.getItemDamage() > 0;

        if (!tank1Valid && !tank2Valid)
        {
            return 0;
        }

        return 9;
    }

    public static boolean hasValidOxygenSetup(EntityPlayerMP player)
    {
        boolean missingComponent = false;

        GCPlayerStats stats = GCPlayerStats.get(player);

        if (stats.getExtendedInventory().getStackInSlot(0) == null || !OxygenUtil.isItemValidForPlayerTankInv(0, stats.getExtendedInventory().getStackInSlot(0)))
        {
            boolean handled = false;

            for (final ItemStack armorStack : player.inventory.armorInventory)
            {
                if (armorStack != null && armorStack.getItem() instanceof IBreathableArmor)
                {
                    final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

                    if (breathableArmor.handleGearType(EnumGearType.HELMET))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.HELMET))
                        {
                            handled = true;
                        }
                    }
                }
            }

            if (!handled)
            {
                missingComponent = true;
            }
        }

        if (stats.getExtendedInventory().getStackInSlot(1) == null || !OxygenUtil.isItemValidForPlayerTankInv(1, stats.getExtendedInventory().getStackInSlot(1)))
        {
            boolean handled = false;

            for (final ItemStack armorStack : player.inventory.armorInventory)
            {
                if (armorStack != null && armorStack.getItem() instanceof IBreathableArmor)
                {
                    final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

                    if (breathableArmor.handleGearType(EnumGearType.GEAR))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.GEAR))
                        {
                            handled = true;
                        }
                    }
                }
            }

            if (!handled)
            {
                missingComponent = true;
            }
        }

        if ((stats.getExtendedInventory().getStackInSlot(2) == null || !OxygenUtil.isItemValidForPlayerTankInv(2, stats.getExtendedInventory().getStackInSlot(2))) && (stats.getExtendedInventory().getStackInSlot(3) == null || !OxygenUtil.isItemValidForPlayerTankInv(3, stats.getExtendedInventory().getStackInSlot(3))))
        {
            boolean handled = false;

            for (final ItemStack armorStack : player.inventory.armorInventory)
            {
                if (armorStack != null && armorStack.getItem() instanceof IBreathableArmor)
                {
                    final IBreathableArmor breathableArmor = (IBreathableArmor) armorStack.getItem();

                    if (breathableArmor.handleGearType(EnumGearType.TANK1))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.TANK1))
                        {
                            handled = true;
                        }
                    }

                    if (breathableArmor.handleGearType(EnumGearType.TANK2))
                    {
                        if (breathableArmor.canBreathe(armorStack, player, EnumGearType.TANK2))
                        {
                            handled = true;
                        }
                    }
                }
            }

            if (!handled)
            {
                missingComponent = true;
            }
        }

        return !missingComponent;
    }

    public static boolean isItemValidForPlayerTankInv(int slotIndex, ItemStack stack)
    {
        switch (slotIndex)
        {
        case 0:
            return stack.getItem() instanceof ItemOxygenMask;
        case 1:
            return stack.getItem() instanceof ItemOxygenGear;
        case 2:
        case 3:
            return stack.getItem() instanceof ItemOxygenTank || stack.getItem() instanceof ItemCanisterOxygenInfinite;
        }

        return false;
    }

    public static TileEntity[] getAdjacentFluidConnections(TileEntity tile)
    {
        return getAdjacentFluidConnections(tile, false);
    }

    public static TileEntity[] getAdjacentFluidConnections(TileEntity tile, boolean ignoreConnect)
    {
        TileEntity[] adjacentConnections = new TileEntity[EnumFacing.VALUES.length];

        boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();

        BlockVec3 thisVec = new BlockVec3(tile);
        for (EnumFacing direction : EnumFacing.VALUES)
        {
            TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorld(), direction);

            if (tileEntity instanceof IFluidHandler)
            {
                if (ignoreConnect || !(tileEntity instanceof IConnector) || ((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.FLUID))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
            }
//            else if (isMekLoaded)
//            {
//                if (tileEntity instanceof ITubeConnection && (!(tileEntity instanceof IGasTransmitter) || TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, tileEntity)))
//                {
//                    if (((ITubeConnection) tileEntity).canTubeConnect(direction))
//                    {
//                        adjacentConnections[direction.ordinal()] = tileEntity;
//                    }
//                }
//            }
        }

        return adjacentConnections;
    }

    public static boolean noAtmosphericCombustion(WorldProvider provider)
    {
        if (provider instanceof IGalacticraftWorldProvider)
        {
            return (!((IGalacticraftWorldProvider) provider).isGasPresent(EnumAtmosphericGas.OXYGEN) && !((IGalacticraftWorldProvider) provider).hasBreathableAtmosphere());
        }

        return false;
    }

    public static boolean inOxygenBubble(World worldObj, double avgX, double avgY, double avgZ)
    {
        int dimID = GCCoreUtil.getDimensionID(worldObj);
        for (final BlockVec3Dim blockVec : TileEntityOxygenDistributor.loadedTiles)
        {
            if (blockVec != null && blockVec.dim == dimID)
            {
                TileEntity tile = blockVec.getTileEntity();
                if (tile instanceof TileEntityOxygenDistributor)
                {
                    if (((TileEntityOxygenDistributor) tile).inBubble(avgX, avgY, avgZ))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
