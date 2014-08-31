package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.client.FMLClientHandler;
import mekanism.api.gas.IGasTransmitter;
import mekanism.api.gas.ITubeConnection;
import mekanism.api.transmitters.TransmissionType;
import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor.EnumGearType;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class OxygenUtil
{
	private static HashSet<BlockVec3> checked;

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
        double y = entity.posY + entity.getEyeHeight();
        double x = entity.posX;
        double z = entity.posZ;
        
        double sx = entity.boundingBox.maxX - entity.boundingBox.minX;
        double sy = entity.boundingBox.maxY - entity.boundingBox.minY;
        double sz = entity.boundingBox.maxZ - entity.boundingBox.minZ;
        
        //A good first estimate of head size is that it's the smallest of the entity's 3 dimensions (e.g. front to back, for Steve)
        double smin = Math.min(sx,  Math.min(sy, sz)) / 2;

		return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, AxisAlignedBB.getBoundingBox(x - smin, y - smin, z - smin, x + smin, y + smin, z + smin));
	}

	@SuppressWarnings("rawtypes")
	public static boolean isAABBInBreathableAirBlock(World world, AxisAlignedBB bb)
	{
		if (!world.isRemote)
		{
		
		final double avgX = (bb.minX + bb.maxX) / 2.0D;
		final double avgY = (bb.minY + bb.maxY) / 2.0D;
		final double avgZ = (bb.minZ + bb.maxZ) / 2.0D;

		final List l = world.loadedTileEntityList;

		for (final Object o : l)
		{
			if (o instanceof TileEntityOxygenDistributor)
			{
				final TileEntityOxygenDistributor distributor = (TileEntityOxygenDistributor) o;

				if (distributor.oxygenBubble != null)
				{
					final double dist = distributor.getDistanceFromServer(avgX, avgY, avgZ);
					double r = distributor.oxygenBubble.getSize();

					if (dist < r * r)
					{
						return true;
					}
				}
			}
		}

		}
		return OxygenUtil.isInOxygenBlock(world, bb.copy().contract(0.001D, 0.001D, 0.001D));
	}

	public static boolean isInOxygenBlock(World world, AxisAlignedBB bb)
	{
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ);

		OxygenUtil.checked = new HashSet();
		if (world.checkChunksExist(i, k, i1, j, l, j1))
		{
			for (int x = i; x <= j; ++x)
			{
				for (int y = k; y <= l; ++y)
				{
					for (int z = i1; z <= j1; ++z)
					{
						Block block = world.getBlock(x, y, z);
						if (OxygenUtil.testContactWithBreathableAir(world, block, x, y, z, 0))
							return true;
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
	public static boolean checkTorchHasOxygen(World world, Block block, int x, int y, int z)
	{	
		OxygenUtil.checked = new HashSet();
		BlockVec3 vec = new BlockVec3(x, y, z);
		for (int side = 0; side < 6; side++)
		{
			BlockVec3 sidevec = vec.newVecSide(side);
			Block newblock = sidevec.getBlockID_noChunkLoad(world);
			if (OxygenUtil.testContactWithBreathableAir(world, newblock, sidevec.x, sidevec.y, sidevec.z, 1)) return true;
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
	public static boolean testContactWithBreathableAir(World world, Block block, int x, int y, int z, int limitCount)
	{
		BlockVec3 vec = new BlockVec3(x, y, z);
		checked.add(vec);
		if (block == GCBlocks.breatheableAir || block == GCBlocks.brightBreatheableAir)
		{
			return true;
		}
		
		if (block == Blocks.air || block == GCBlocks.brightAir)
		{
			return false;							
		}

		//Test for non-sided permeable or solid blocks first
		boolean permeableFlag = false;
		if (!(block instanceof BlockLeavesBase))
		{
			if (block.isOpaqueCube())
			{
				if  (block instanceof BlockGravel || block.getMaterial() == Material.cloth || block instanceof BlockSponge)
					permeableFlag = true;
				else
					return false;			
			}
			else if (block instanceof BlockGlass || block instanceof BlockStainedGlass)
			{
				return false;
			}
			else if (block instanceof BlockLiquid)
			{
				return false;
			}
			else if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(block))
			{
				ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
				if (metaList.contains(Integer.valueOf(-1)) || metaList.contains(world.getBlockMetadata(x, y, z)))
				{
					return false;
				}
			}
		}
		else permeableFlag = true;

		//Testing a non-air, permeable block (for example a torch or a ladder)
		if (limitCount < 5)
		{
			for (int side = 0; side < 6; side++)
				if (permeableFlag || OxygenUtil.canBlockPassAirOnSide(world, block, vec, side))
				{
					BlockVec3 sidevec = vec.newVecSide(side);
					if (!checked.contains(sidevec))
					{
						Block newblock = sidevec.getBlockID_noChunkLoad(world);
						if (OxygenUtil.testContactWithBreathableAir(world, newblock, sidevec.x, sidevec.y, sidevec.z, limitCount+1)) return true;
					}
				}
		}
		
		return false;
	}
	//TODO - performance, could add a 'safe' version of this code (inside world borders)

	//TODO - add more performance increase, these sided checks could be done once only
	private static boolean canBlockPassAirOnSide(World world, Block block, BlockVec3 vec, int side)
	{
		if (block instanceof IPartialSealableBlock)
		{
			return !((IPartialSealableBlock) block).isSealed(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side));
		}

		//Half slab seals on the top side or the bottom side according to its metadata
		if (block instanceof BlockSlab)
		{
			return !(side == 0 && (vec.getBlockMetadata(world) & 8) == 8 || side == 1 && (vec.getBlockMetadata(world) & 8) == 0);
		}

		//Farmland etc only seals on the solid underside
		if (block instanceof BlockFarmland || block instanceof BlockEnchantmentTable || block instanceof BlockLiquid)
		{
			return side != 1;
		}

		if (block instanceof BlockPistonBase)
		{
			BlockPistonBase piston = (BlockPistonBase) block;
			int meta = vec.getBlockMetadata(world);
			if (BlockPistonBase.isExtended(meta))
			{
				int facing = BlockPistonBase.getPistonOrientation(meta);
				return side != facing;
			}
			return false;
		}

		return !block.isSideSolid(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side ^ 1));
	}
	
	public static int getDrainSpacing(ItemStack tank, ItemStack tank2)
	{
		boolean tank1Valid = tank != null && tank.getItem() instanceof ItemOxygenTank && tank.getMaxDamage() - tank.getItemDamage() > 0;
		boolean tank2Valid = tank2 != null && tank2.getItem() instanceof ItemOxygenTank && tank2.getMaxDamage() - tank2.getItemDamage() > 0;

		if (!tank1Valid && !tank2Valid)
		{
			return 0;
		}

		if (tank1Valid && !tank2Valid || !tank1Valid && tank2Valid)
		{
			return 9;
		}

		return 18;
	}

	public static boolean hasValidOxygenSetup(EntityPlayerMP player)
	{
		boolean missingComponent = false;

        GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(player);

		if (stats.extendedInventory.getStackInSlot(0) == null || !OxygenUtil.isItemValidForPlayerTankInv(0, stats.extendedInventory.getStackInSlot(0)))
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

		if (stats.extendedInventory.getStackInSlot(1) == null || !OxygenUtil.isItemValidForPlayerTankInv(1, stats.extendedInventory.getStackInSlot(1)))
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

		if ((stats.extendedInventory.getStackInSlot(2) == null || !OxygenUtil.isItemValidForPlayerTankInv(2, stats.extendedInventory.getStackInSlot(2))) && (stats.extendedInventory.getStackInSlot(3) == null || !OxygenUtil.isItemValidForPlayerTankInv(3, stats.extendedInventory.getStackInSlot(3))))
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
			return stack.getItem() instanceof ItemOxygenTank;
		case 3:
			return stack.getItem() instanceof ItemOxygenTank;
		}

		return false;
	}
	
	public static TileEntity[] getAdjacentOxygenConnections(TileEntity tile)
	{
		TileEntity[] adjacentConnections = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];

		boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();

		BlockVec3 thisVec = new BlockVec3(tile);
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorldObj(), direction);

			if (tileEntity instanceof IConnector)
			{
				if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.OXYGEN))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			else if (isMekLoaded)
			{
				if (tileEntity instanceof ITubeConnection && (!(tileEntity instanceof IGasTransmitter) || TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, tileEntity)))
				{
					if (((ITubeConnection) tileEntity).canTubeConnect(direction))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
					}
				}
			}
		}

		return adjacentConnections;
	}
}
