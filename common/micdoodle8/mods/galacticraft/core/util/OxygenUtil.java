package micdoodle8.mods.galacticraft.core.util;

import java.util.List;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor;
import micdoodle8.mods.galacticraft.api.item.IBreathableArmor.EnumGearType;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryPlayer;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;

public class OxygenUtil
{
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

        if (gui instanceof GuiChat)
        {
            return true;
        }

        return false;
    }

    public static boolean isAABBInBreathableAirBlock(Entity entity)
    {
        return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, new Vector3(entity.boundingBox.minX, entity.boundingBox.minY, entity.boundingBox.minZ), new Vector3(entity.boundingBox.maxX + 1, entity.boundingBox.maxY + 1, entity.boundingBox.maxZ + 1), false);
    }

    public static boolean isAABBInBreathableAirBlock(World world, Vector3 minVec, Vector3 maxVec, boolean testAllPoints)
    {
        final double avgX = (minVec.x + maxVec.x) / 2.0D;
        final double avgY = (minVec.y + maxVec.y) / 2.0D;
        final double avgZ = (minVec.z + maxVec.z) / 2.0D;

        final List l = world.loadedTileEntityList;

        for (final Object o : l)
        {
            if (o instanceof GCCoreTileEntityOxygenDistributor)
            {
                final GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor) o;

                if (!distributor.worldObj.isRemote)
                {
                    final double dist = distributor.getDistanceFromServer(avgX, avgY, avgZ);

                    if (dist < distributor.storedOxygen / 600.0D * (distributor.storedOxygen / 600.0D))
                    {
                        return true;
                    }
                }
            }
        }

        return OxygenUtil.isInOxygenBlock(world, AxisAlignedBB.getAABBPool().getAABB(minVec.x, minVec.y, minVec.z, maxVec.x, maxVec.y, maxVec.z).contract(0.001D, 0.001D, 0.001D));
    }

    public static boolean isInOxygenBlock(World world, AxisAlignedBB bb)
    {
        int i = MathHelper.floor_double(bb.minX);
        int j = MathHelper.floor_double(bb.maxX + 1.0D);
        int k = MathHelper.floor_double(bb.minY);
        int l = MathHelper.floor_double(bb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(bb.minZ);
        int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);

        if (world.checkChunksExist(i, k, i1, j, l, j1))
        {
            for (int k1 = i; k1 < j; ++k1)
            {
                for (int l1 = k; l1 < l; ++l1)
                {
                    for (int i2 = i1; i2 < j1; ++i2)
                    {
                        int j2 = world.getBlockId(k1, l1, i2);

                        if (j2 == GCCoreBlocks.breatheableAir.blockID)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static int getDrainSpacing(ItemStack tank)
    {
        if (tank == null)
        {
            return 0;
        }

        if (tank.getItem() instanceof GCCoreItemOxygenTank)
        {
            return 360;
        }

        return 0;
    }

    public static boolean hasValidOxygenSetup(GCCorePlayerMP player)
    {
        boolean missingComponent = false;

        if (((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(0) == null || !OxygenUtil.isItemValidForPlayerTankInv(0, ((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(0)))
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

        if (((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(1) == null || !OxygenUtil.isItemValidForPlayerTankInv(1, ((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(1)))
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

        if ((((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(2) == null || !OxygenUtil.isItemValidForPlayerTankInv(2, ((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(2))) && (((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(3) == null || !OxygenUtil.isItemValidForPlayerTankInv(3, ((GCCoreInventoryPlayer) player.inventory).tankItemInSlot(3))))
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
            return stack.getItem() instanceof GCCoreItemOxygenMask;
        case 1:
            return stack.getItem() instanceof GCCoreItemOxygenGear;
        case 2:
            return OxygenUtil.getDrainSpacing(stack) > 0;
        case 3:
            return OxygenUtil.getDrainSpacing(stack) > 0;
        }

        return false;
    }

    public static boolean isBlockGettingOxygen(World world, int par1, int par2, int par3)
    {
        return true; // TODO
        // return isBlockProvidingOxygenTo(world, par1, par2 - 1, par3, 0) ?
        // true : isBlockProvidingOxygenTo(world, par1, par2 + 1, par3, 1) ?
        // true : isBlockProvidingOxygenTo(world, par1, par2, par3 - 1, 2) ?
        // true : isBlockProvidingOxygenTo(world, par1, par2, par3 + 1, 3) ?
        // true : isBlockProvidingOxygenTo(world, par1 - 1, par2, par3, 4) ?
        // true : isBlockProvidingOxygenTo(world, par1 + 1, par2, par3, 5);
    }

    // public static boolean isBlockProvidingOxygenTo(World world, int par1, int
    // par2, int par3, int par4)
    // {
    // final TileEntity te = world.getBlockTileEntity(par1, par2, par3);
    //
    // if (te != null && te instanceof TileEntityOxygenTransmitter)
    // {
    // final TileEntityOxygenTransmitter teot = (TileEntityOxygenTransmitter)
    // te;
    //
    // if (teot.getOxygenInTransmitter() > 1.0D)
    // {
    // return true;
    // }
    // }
    //
    // return false;
    // }
}
