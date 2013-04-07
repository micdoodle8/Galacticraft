package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import micdoodle8.mods.galacticraft.API.IOrbitDimension;
import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.API.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreEnumTeleportType;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerSchematic;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerCommon;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.tileentity.TileEntity;
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
        final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));

        final int packetType = PacketUtil.readPacketID(data);

        final EntityPlayerMP player = (EntityPlayerMP)p;

    	final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player);

        if (packetType == 0)
        {
            final Class[] decodeAs = {String.class};
            PacketUtil.readPacketData(data, decodeAs);

            player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
        else if (packetType == 1)
        {
            final Class[] decodeAs = {String.class};
            PacketUtil.readPacketData(data, decodeAs);

            player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
        }
        else if (packetType == 2)
        {
            final Class[] decodeAs = {String.class};
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (playerBase != null)
            {
            	try
            	{
                	WorldProvider provider = WorldUtil.getProviderForName((String)packetReadout[0]);
    	    		final Integer dim = provider.dimensionId;
            		FMLLog.info("Found matching world name for " + (String)packetReadout[0]);
    	    		
            		if (playerBase.worldObj instanceof WorldServer)
            		{
            			WorldServer world = (WorldServer) playerBase.worldObj;
            			
        	    		if (provider instanceof IOrbitDimension)
        	    		{
    	            		WorldUtil.transferEntityToDimension(playerBase, dim, world, GCCoreEnumTeleportType.TOORBIT);
        	    		}
        	    		else
        	    		{
    	            		WorldUtil.transferEntityToDimension(playerBase, dim, world, GCCoreEnumTeleportType.TOPLANET);
        	    		}
            		}
    	    		
    	    		playerBase.teleportCooldown = 300;
    	    		final Object[] toSend = {player.username};
    	    		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 12, toSend));
            	}
            	catch (Exception e)
            	{
            		FMLLog.severe("Not matching world names found for " + (String)packetReadout[0]);
            		e.printStackTrace();
            	}
            }
        }
        else if (packetType == 3)
        {
            if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof GCCoreEntitySpaceship)
            {
            	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;

            	final ItemStack stack = ship.getStackInSlot(0);

            	if (ship.hasFuelTank())
            	{
            		ItemStack stack2 = null;

            		if (playerBase != null && playerBase.playerTankInventory != null)
            		{
            			stack2 = playerBase.playerTankInventory.getStackInSlot(4);
            		}

    				if (stack2 != null && stack2.getItem() instanceof GCCoreItemParachute || player != null && playerBase.launchAttempts > 0)
    				{
                    	ship.ignite();
                    	playerBase.launchAttempts = 0;
    				}
                	else if (GCCoreTickHandlerCommon.chatCooldown == 0 && playerBase.launchAttempts == 0)
                	{
                		player.sendChatToPlayer("I don't have a parachute! If I press launch again, there's no going back!");
                		GCCoreTickHandlerCommon.chatCooldown = 250;
                		playerBase.launchAttempts = 1;
                	}
    			}
            	else if (GCCoreTickHandlerCommon.chatCooldown == 0)
            	{
            		player.sendChatToPlayer("I'll need to load in some rocket fuel first!");
            		GCCoreTickHandlerCommon.chatCooldown = 250;
            	}
            }
        }
        else if (packetType == 4)
        {
            final Class[] decodeAs = {Integer.class};
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

        	if (player != null)
        	{
        		ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) packetReadout[0]);

        		player.openGui(GalacticraftCore.instance, page.getGuiID(), player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        	}
        }
        else if (packetType == 5)
        {
        }
        else if (packetType == 6)
        {
            final Class[] decodeAs = {Integer.class};
            PacketUtil.readPacketData(data, decodeAs);

            if (player.ridingEntity instanceof GCCoreEntitySpaceship)
            {
            	player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            }
        }
        else if (packetType == 7)
        {
            final Class[] decodeAs = {Float.class};
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (player.ridingEntity instanceof GCCoreEntitySpaceship)
            {
            	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;

            	if (ship != null)
            	{
            		ship.rotationYaw = (Float) packetReadout[0];
            	}
            }
        }
        else if (packetType == 8)
        {
            final Class[] decodeAs = {Float.class};
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            if (player.ridingEntity instanceof GCCoreEntitySpaceship)
            {
            	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;

            	if (ship != null)
            	{
            		ship.rotationPitch = (Float) packetReadout[0];
            	}
            }
        }
//        else if (packetType == 9)
//        {
//            final Class[] decodeAs = {Integer.class};
//            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
//
//            if (player.ridingEntity instanceof GCCoreEntityControllable)
//            {
//            	final GCCoreEntityControllable controllableEntity = (GCCoreEntityControllable) player.ridingEntity;
//
//            	if (controllableEntity != null)
//            	{
//            		controllableEntity.keyPressed((Integer) packetReadout[0], player);
//            	}
//            }
//        }
        else if (packetType == 10)
        {
            final Class[] decodeAs = {Integer.class};
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
            final Class[] decodeAs = {Integer.class, Integer.class, Integer.class};
            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

            player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRefinery, player.worldObj, (Integer)packetReadout[0], (Integer)packetReadout[1], (Integer)packetReadout[2]);
        }
        else if (packetType == 12)
        {
            try
            {
	    		new GCCorePacketControllableEntity().handlePacket(data, new Object[] {player}, Side.SERVER);
            }
            catch(Exception e)
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
	    		new GCCorePacketEntityUpdate().handlePacket(data, new Object[] {player}, Side.SERVER);
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
        }
        else if (packetType == 15)
        {
        	if (playerBase.spaceStationDimensionID == -1 || playerBase.spaceStationDimensionID == 0)
        	{
            	WorldUtil.bindSpaceStationToNewDimension(playerBase.worldObj, playerBase);
            	
            	for (ItemStack stack : RecipeUtil.getStandardSpaceStationRequirements())
            	{
            		int amountToRemove = stack.stackSize;
            		
            		for (ItemStack stack2 : playerBase.inventory.mainInventory)
            		{
            			if (stack != null && stack2 != null && stack.itemID == stack2.itemID && stack.getItemDamage() == stack2.getItemDamage())
            			{
            				if (stack2.stackSize > amountToRemove)
            				{
            					stack2.stackSize -= amountToRemove;
            					break;
            				}
            				else if (stack2.stackSize <= amountToRemove)
            				{
            					amountToRemove -= stack2.stackSize;
            					stack2.stackSize = 0;
            				}
            			}
            		}
            	}
        	}
        }
        else if (packetType == 16)
        {
        	Container container = player.openContainer;
        	
        	if (container instanceof GCCoreContainerSchematic)
        	{
        		GCCoreContainerSchematic schematicContainer = (GCCoreContainerSchematic) container;
        		
        		ItemStack stack = schematicContainer.craftMatrix.getStackInSlot(0);
        		
        		if (stack != null)
        		{
            		ISchematicPage page = SchematicRegistry.getMatchingRecipeForItemStack(stack);
            		
            		if (page != null)
            		{
            			SchematicRegistry.unlockNewPage(playerBase, stack);
            			
            			if (--stack.stackSize <= 0)
            			{
            				stack = null;
            			}

            			schematicContainer.craftMatrix.setInventorySlotContents(0, stack);
            			schematicContainer.craftMatrix.onInventoryChanged();
            			
        	    		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 20, new Object[] {page.getPageID()}));
            		}
        		}
        	}
        }
//        else if (packetType == ) TODO
//        {
//            final Class[] decodeAs = {Integer.class, Float.class, Double.class, Double.class, Double.class};
//            final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
//            
//            for (final Object object : player.worldObj.loadedEntityList)
//            {
//            	if (object instanceof GCCoreEntityLander)
//            	{
//            		final GCCoreEntityLander entity = (GCCoreEntityLander) object;
//
//            		if (entity.entityId == (Integer) packetReadout[0])
//            		{
//            			entity.worldObj.createExplosion(entity, (Double) packetReadout[2], (Double) packetReadout[3], (Double) packetReadout[4], (Float) packetReadout[1], false);
//            			
//            			entity.setDead();
//            		}
//            	}
//            }
//        }
        else if (packetType == 17)
      	{
        	final Class[] decodeAs = {Integer.class, Integer.class, Integer.class};
          	final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
          	
          	TileEntity tileAt = player.worldObj.getBlockTileEntity((Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);
      	
          	if (tileAt instanceof GCCoreTileEntityOxygenSealer)
          	{
          		GCCoreTileEntityOxygenSealer sealer = (GCCoreTileEntityOxygenSealer) tileAt;
          		
          		boolean disabledBefore = sealer.disabled;
          		
          		sealer.disabled = !disabledBefore;
          	}
      	}
    }
}