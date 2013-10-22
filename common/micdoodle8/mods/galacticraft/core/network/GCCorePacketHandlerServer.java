package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerSchematic;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumClientPacket;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class GCCorePacketHandlerServer implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
    {
        if (packet == null)
        {
            FMLLog.severe("Packet received as null!");
            return;
        }

        if (packet.data == null)
        {
            FMLLog.severe("Packet data received as null! ID " + packet.getPacketId());
            return;
        }

        final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));

        final int packetType = PacketUtil.readPacketID(data);

        final EntityPlayerMP player = (EntityPlayerMP) p;

        final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player);

        if (packetType == 0)
        {
            final Class<?>[] decodeAs = { String.class };
            PacketUtil.readPacketData(data, decodeAs);

            player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        else if (packetType == 1)
        {
            final Class<?>[] decodeAs = { String.class };
            PacketUtil.readPacketData(data, decodeAs);

            player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte) player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
        }
        else if (packetType == 2)
        {
            final Class<?>[] decodeAs = { String.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (playerBase != null)
            {
                try
                {
                    final WorldProvider provider = WorldUtil.getProviderForName((String) packetReadout[0]);
                    final Integer dim = provider.dimensionId;
                    GCLog.info("Found matching world name for " + (String) packetReadout[0]);

                    if (playerBase.worldObj instanceof WorldServer)
                    {
                        final WorldServer world = (WorldServer) playerBase.worldObj;

                        if (provider instanceof IOrbitDimension)
                        {
                            WorldUtil.transferEntityToDimension(playerBase, dim, world);
                        }
                        else
                        {
                            WorldUtil.transferEntityToDimension(playerBase, dim, world);
                        }
                    }

                    playerBase.setTeleportCooldown(300);
                    final Object[] toSend = { player.username };
                    player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.CLOSE_GUI, toSend));
                }
                catch (final Exception e)
                {
                    GCLog.severe("Error occurred when attempting to transfer entity to dimension: " + (String) packetReadout[0]);
                    e.printStackTrace();
                }
            }
        }
        else if (packetType == 3)
        {
            if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) player.ridingEntity;

                if (ship.hasValidFuel())
                {
                    ItemStack stack2 = null;

                    if (playerBase != null)
                    {
                        stack2 = playerBase.getExtendedInventory().getStackInSlot(4);
                    }

                    if (stack2 != null && stack2.getItem() instanceof GCCoreItemParachute || playerBase != null && playerBase.getLaunchAttempts() > 0)
                    {
                        ship.ignite();
                        playerBase.setLaunchAttempts(0);
                    }
                    else if (playerBase.getChatCooldown() == 0 && playerBase.getLaunchAttempts() == 0)
                    {
                        player.sendChatToPlayer(ChatMessageComponent.createFromText("I don't have a parachute! If I press launch again, there's no going back!"));
                        playerBase.setChatCooldown(250);
                        playerBase.setLaunchAttempts(1);
                    }
                }
                else if (playerBase.getChatCooldown() == 0)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromText("I'll need to load in some rocket fuel first!"));
                    playerBase.setChatCooldown(250);
                }
            }
        }
        else if (packetType == 4)
        {
            final Class<?>[] decodeAs = { Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (player != null)
            {
                final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) packetReadout[0]);

                player.openGui(GalacticraftCore.instance, page.getGuiID(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
        }
        else if (packetType == 5)
        {
        }
        else if (packetType == 6)
        {
            final Class<?>[] decodeAs = { Integer.class };
            PacketUtil.readPacketData(data, decodeAs);

            player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        else if (packetType == 7)
        {
            final Class<?>[] decodeAs = { Float.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (player.ridingEntity instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

                if (ship != null)
                {
                    ship.rotationYaw = (Float) packetReadout[0];
                }
            }
        }
        else if (packetType == 8)
        {
            final Class<?>[] decodeAs = { Float.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (player.ridingEntity instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

                if (ship != null)
                {
                    ship.rotationPitch = (Float) packetReadout[0];
                }
            }
        }
        // else if (packetType == 9)
        // {
        // final Class<?>[] decodeAs = {Integer.class};
        // final Object[] packetReadout = PacketUtil.readPacketData(data,
        // decodeAs);
        //
        // if (player.ridingEntity instanceof GCCoreEntityControllable)
        // {
        // final GCCoreEntityControllable controllableEntity =
        // (GCCoreEntityControllable) player.ridingEntity;
        //
        // if (controllableEntity != null)
        // {
        // controllableEntity.keyPressed((Integer) packetReadout[0], player);
        // }
        // }
        // }
        else if (packetType == 10)
        {
            final Class<?>[] decodeAs = { Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            for (final Object object : player.worldObj.loadedEntityList)
            {
                if (object instanceof EntityLiving)
                {
                    final EntityLiving entity = (EntityLiving) object;

                    if (entity.entityId == (Integer) packetReadout[0] && entity.ridingEntity == null)
                    {
                        entity.setFire(3);
                    }
                }
            }
        }
        else if (packetType == 11)
        {
            final Class<?>[] decodeAs = { Integer.class, Integer.class, Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRefinery, player.worldObj, (Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);
        }
        else if (packetType == 12)
        {
            try
            {
                new GCCorePacketControllableEntity().handlePacket(data, new Object[] { player }, Side.SERVER);
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (packetType == 13)
        {
        }
        else if (packetType == 14)
        {
            try
            {
                new GCCorePacketEntityUpdate().handlePacket(data, new Object[] { player }, Side.SERVER);
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (packetType == 15)
        {
            final Class<?>[] decodeAs = { Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (playerBase.getSpaceStationDimensionID() == -1 || playerBase.getSpaceStationDimensionID() == 0)
            {
                WorldUtil.bindSpaceStationToNewDimension(playerBase.worldObj, playerBase);

                WorldUtil.getSpaceStationRecipe((Integer) packetReadout[0]).matches(playerBase, true);
            }
        }
        else if (packetType == 16)
        {
            final Container container = player.openContainer;

            if (container instanceof GCCoreContainerSchematic)
            {
                final GCCoreContainerSchematic schematicContainer = (GCCoreContainerSchematic) container;

                ItemStack stack = schematicContainer.craftMatrix.getStackInSlot(0);

                if (stack != null)
                {
                    final ISchematicPage page = SchematicRegistry.getMatchingRecipeForItemStack(stack);

                    if (page != null)
                    {
                        SchematicRegistry.unlockNewPage(playerBase, stack);

                        if (--stack.stackSize <= 0)
                        {
                            stack = null;
                        }

                        schematicContainer.craftMatrix.setInventorySlotContents(0, stack);
                        schematicContainer.craftMatrix.onInventoryChanged();

                        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.ADD_NEW_SCHEMATIC, new Object[] { page.getPageID() }));
                    }
                }
            }
        }
        else if (packetType == 17)
        {
            final Class<?>[] decodeAs = { Integer.class, Integer.class, Integer.class, Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            final TileEntity tileAt = player.worldObj.getBlockTileEntity((Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);

            if (tileAt instanceof IDisableableMachine)
            {
                final IDisableableMachine machine = (IDisableableMachine) tileAt;

                machine.setDisabled((Integer) packetReadout[3], !machine.getDisabled((Integer) packetReadout[3]));
            }
        }
        else if (packetType == 18)
        {
            final Class<?>[] decodeAs = { Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (playerBase.getChatCooldown() == 0)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText("I'll probably need a Tier " + packetReadout[0] + " Dungeon key to unlock this!"));
                playerBase.setChatCooldown(100);
            }
        }
        else if (packetType == 19)
        {
            final Class<?>[] decodeAs = { String.class, Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            final GCCoreSpaceStationData ssdata = GCCoreSpaceStationData.getStationData(playerBase.worldObj, (Integer) packetReadout[1], playerBase);

            if (ssdata != null && ssdata.getOwner().equalsIgnoreCase(player.username))
            {
                ssdata.setSpaceStationName((String) packetReadout[0]);
                ssdata.setDirty(true);
            }
        }
        else if (packetType == 20)
        {
            if (player.ridingEntity instanceof GCCoreEntityBuggy)
            {
                GCCoreUtil.openBuggyInv(player, (GCCoreEntityBuggy) player.ridingEntity, ((GCCoreEntityBuggy) player.ridingEntity).getType());
            }
        }
        else if (packetType == 21)
        {
            final Class<?>[] decodeAs = { Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
            Entity e = player.worldObj.getEntityByID((Integer) packetReadout[0]);

            if (e != null && e instanceof IInventorySettable)
            {
                player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketLanderUpdate.buildKeyPacket(e));
            }
        }
        else if (packetType == 22)
        {
            final Class<?>[] decodeAs = { Integer.class, Integer.class, Integer.class };
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
            TileEntity tile = player.worldObj.getBlockTileEntity((Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);

            if (tile != null && tile instanceof GCCoreTileEntityParachest)
            {
                new GCCorePacketParachestUpdate();
                player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketParachestUpdate.buildKeyPacket((GCCoreTileEntityParachest) tile));
            }
        }
        else if (packetType == 23)
        {
            player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiExtendedInventory, player.worldObj, 0, 0, 0);
        }
        else if (packetType == 24)
        {
            Class<?>[] decodeAs = { Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };
            Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            TileEntity tile = player.worldObj.getBlockTileEntity((Integer) packetReadout[1], (Integer) packetReadout[2], (Integer) packetReadout[3]);

            switch ((Integer) packetReadout[0])
            {
            case 0:
                if (tile instanceof GCCoreTileEntityAirLockController)
                {
                    GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile;
                    launchController.redstoneActivation = ((Integer) packetReadout[4]) == 1;
                }
                break;
            case 1:
                if (tile instanceof GCCoreTileEntityAirLockController)
                {
                    GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile;
                    launchController.playerDistanceActivation = ((Integer) packetReadout[4]) == 1;
                }
                break;
            case 2:
                if (tile instanceof GCCoreTileEntityAirLockController)
                {
                    GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile;
                    launchController.playerDistanceSelection = ((Integer) packetReadout[4]);
                }
                break;
            case 3:
                if (tile instanceof GCCoreTileEntityAirLockController)
                {
                    GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile;
                    launchController.playerNameMatches = ((Integer) packetReadout[4]) == 1;
                }
                break;
            case 4:
                if (tile instanceof GCCoreTileEntityAirLockController)
                {
                    GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile;
                    launchController.invertSelection = ((Integer) packetReadout[4]) == 1;
                }
                break;
            default:
                break;
            }
        }
        else if (packetType == 25)
        {
            Class<?>[] decodeAs = { Integer.class, Integer.class, Integer.class, Integer.class, String.class };
            Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            TileEntity tile = player.worldObj.getBlockTileEntity((Integer) packetReadout[1], (Integer) packetReadout[2], (Integer) packetReadout[3]);

            switch ((Integer) packetReadout[0])
            {
            case 0:
                if (tile instanceof GCCoreTileEntityAirLockController)
                {
                    GCCoreTileEntityAirLockController launchController = (GCCoreTileEntityAirLockController) tile;
                    launchController.playerToOpenFor = ((String) packetReadout[4]);
                }
                break;
            default:
                break;
            }
        }
    }
}
