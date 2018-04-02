package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockBasicMoon;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

public class PlayerClient implements IPlayerClient
{
    private boolean saveSneak;
	private double downMot2;
	public static boolean startup;

    @Override
    public void move(EntityPlayerSP player, MoverType type, double x, double y, double z)
    {
        this.updateFeet(player, x, z);
    }

    @Override
    public boolean wakeUpPlayer(EntityPlayerSP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn)
    {
        return this.wakeUpPlayer(player, immediately, updateWorldFlag, setSpawn, false);
    }

    @Override
    public void onUpdate(EntityPlayerSP player)
    {
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);
        stats.setTick(stats.getTick() + 1);

        if (stats.isUsingParachute() && !player.capabilities.isFlying && !player.handleWaterMovement())
        {
            player.motionY = -0.5D;
            player.motionX *= 0.5F;
            player.motionZ *= 0.5F;
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock(EntityPlayerSP player, boolean vanillaInside)
    {
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);
        if (vanillaInside && stats.isInFreefall())
        {
            stats.setInFreefall(false);
            return false;
        }
        return !(player.getRidingEntity() instanceof EntityLanderBase) && vanillaInside;
    }

    @Override
    public void onLivingUpdatePre(EntityPlayerSP player)
    {
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        if (stats.getPlatformControlled() || player.world.provider instanceof IGalacticraftWorldProvider)
        {
            if (!startup)
            {
                stats.setInFreefallLast(stats.isInFreefall());
                stats.setInFreefall(stats.getPlatformControlled() || stats.getFreefallHandler().testFreefall(player));
                startup = true;
            }
            if (stats.getPlatformControlled() || player.world.provider instanceof IZeroGDimension)
            {
                stats.setInFreefallLast(stats.isInFreefall());
                stats.setInFreefall(stats.getPlatformControlled() || stats.getFreefallHandler().testFreefall(player));
                this.downMot2 = stats.getDownMotionLast();
                stats.setDownMotionLast(player.motionY);
                stats.getFreefallHandler().preVanillaMotion(player);
                if (stats.getPlatformControlled())
                {
                    player.motionY = stats.getPlatformVelocity(player.posY);
                    player.motionX = 0D;
                    player.motionZ = 0D;
                }
            }
        }

//        if (player.boundingBox != null && stats.boundingBoxBefore == null)
//        {
//            GCLog.debug("Changed player BB from " + player.boundingBox.minY);
//            stats.boundingBoxBefore = player.boundingBox;
//            player.boundingBox.setBounds(stats.boundingBoxBefore.minX + 0.4, stats.boundingBoxBefore.minY + 0.9, stats.boundingBoxBefore.minZ + 0.4, stats.boundingBoxBefore.maxX - 0.4, stats.boundingBoxBefore.maxY - 0.9, stats.boundingBoxBefore.maxZ - 0.4);
//            GCLog.debug("Changed player BB to " + player.boundingBox.minY);
//        }
//        else if (player.boundingBox != null && stats.boundingBoxBefore != null)
//        {
//            player.boundingBox.setBB(stats.boundingBoxBefore);
//            GCLog.debug("Changed player BB to " + player.boundingBox.minY);
//        }
    }
    
    public void cancelLimbSwing(EntityPlayerSP player)
    {
        player.limbSwing -= player.limbSwingAmount;
        player.limbSwingAmount = player.prevLimbSwingAmount;
        float adjust = Math.min(Math.abs(player.limbSwing), Math.abs(player.limbSwingAmount) / 3);
        if (player.limbSwing < 0)
        {
            player.limbSwing += adjust;
        }
        else if (player.limbSwing > 0)
        {
            player.limbSwing -= adjust;
        }
        player.limbSwingAmount *= 0.9;
    }

    @Override
    public void onLivingUpdatePost(EntityPlayerSP player)
    {
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);
        boolean ridingThirdPersonEntity = player.getRidingEntity() instanceof ICameraZoomEntity && ((ICameraZoomEntity) player.getRidingEntity()).defaultThirdPerson();

        if (stats.getPlatformControlled() || player.world.provider instanceof IZeroGDimension)
        {
            stats.getFreefallHandler().postVanillaMotion(player);

            if (stats.isInFreefall() || ridingThirdPersonEntity)
            {
                this.cancelLimbSwing(player);
            }
            else
            {
		    	if (stats.isInFreefallLast() && this.downMot2 < -0.008D)
                {
		    		stats.setLandingTicks(5 - (int)(Math.min(this.downMot2, stats.getDownMotionLast()) * 40));
		    		if (stats.getLandingTicks() > stats.getMaxLandingticks())
		    		{
	                    if (stats.getLandingTicks() > stats.getMaxLandingticks() + 4)
	                    {
	                        stats.getFreefallHandler().pjumpticks = stats.getLandingTicks() - stats.getMaxLandingticks() - 5;
                        }
		    		    stats.setLandingTicks(stats.getMaxLandingticks());
		    		}
		    		float dYmax = 0.3F * stats.getLandingTicks() / stats.getMaxLandingticks();
		    		float factor = 1F;
		    		for (int i = 0; i <= stats.getLandingTicks(); i++)
		    		{
    	                stats.getLandingYOffset()[i] = dYmax * MathHelper.sin(i * 3.1415926F / stats.getLandingTicks()) * factor;
    	                factor *= 0.97F;
                    }
		    	}
	        }

	        if (stats.getLandingTicks() > 0)
	        {
	            stats.setLandingTicks(stats.getLandingTicks() - 1);
                player.limbSwing *= 0.8F;
                player.limbSwingAmount = 0F;
            }
        }
        else
        {
            stats.setInFreefall(false);
            if (ridingThirdPersonEntity)
            {
                this.cancelLimbSwing(player);
            }
        }

        if (ridingThirdPersonEntity && !stats.isLastRidingCameraZoomEntity())
        {
            if(!ConfigManagerCore.disableVehicleCameraChanges)
                FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;
        }

        if (player.getRidingEntity() instanceof ICameraZoomEntity)
        {
            if(!ConfigManagerCore.disableVehicleCameraChanges)
            {
                stats.setLastZoomed(true);
                TickHandlerClient.zoom(((ICameraZoomEntity) player.getRidingEntity()).getCameraZoom());
            }
        }
        else if (stats.isLastZoomed())
        {
        	if(!ConfigManagerCore.disableVehicleCameraChanges)
            {
	            stats.setLastZoomed(false);
	            TickHandlerClient.zoom(4.0F);
            }
        }

        stats.setLastRidingCameraZoomEntity(ridingThirdPersonEntity);

        if (stats.isUsingParachute())
        {
            player.fallDistance = 0.0F;
        }

        PlayerGearData gearData = GalacticraftCore.proxy.getGearData(player);

        stats.setUsingParachute(false);

        if (gearData != null)
        {
            stats.setUsingParachute(gearData.getParachute() != null);
            if(!GalacticraftCore.isHeightConflictingModInstalled)
            {
                if (gearData.getMask() != GCPlayerHandler.GEAR_NOT_PRESENT)
                {
                	player.height = 1.9375F;
                }
                else
                {
                	player.height = 1.8F;
                }
                AxisAlignedBB bounds = player.getEntityBoundingBox();
                player.setEntityBoundingBox(new AxisAlignedBB(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.minY + (double) player.height, bounds.maxZ));
            }
        }

        if (stats.isUsingParachute() && player.onGround)
        {
            stats.setUsingParachute(false);
            stats.setLastUsingParachute(false);
            FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = stats.getThirdPersonView();
        }

        if (!stats.isLastUsingParachute() && stats.isUsingParachute())
        {
            FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(GCSounds.parachute, SoundCategory.PLAYERS, 0.95F + player.getRNG().nextFloat() * 0.1F, 1.0F, (float) player.posX, (float) player.posY, (float) player.posZ));
        }

        stats.setLastUsingParachute(stats.isUsingParachute());
        stats.setLastOnGround(player.onGround);
    }

    @Override
    public float getBedOrientationInDegrees(EntityPlayerSP player, float vanillaDegrees)
    {
        if (player.bedLocation != null)
        {
            if (player.world.getTileEntity(player.bedLocation) instanceof TileEntityAdvanced)
            {
//                int j = player.world.getBlock(x, y, z).getBedDirection(player.world, x, y, z);
                IBlockState state = player.world.getBlockState(player.bedLocation);
                switch (state.getBlock().getMetaFromState(state) - 4)
                {
                case 0:
                    return 90.0F;
                case 1:
                    return 270.0F;
                case 2:
                    return 180.0F;
                case 3:
                    return 0.0F;
                }
            }
            else
            {
                return vanillaDegrees;
            }
        }

        return vanillaDegrees;
    }

    private void updateFeet(EntityPlayerSP player, double motionX, double motionZ)
    {
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);
        double motionSqrd = motionX * motionX + motionZ * motionZ;

        // If the player is on the moon, not airbourne and not riding anything
        if (motionSqrd > 0.001 && player.world != null && player.world.provider instanceof WorldProviderMoon && player.getRidingEntity() == null && !player.capabilities.isFlying)
        {
            int iPosX = MathHelper.floor(player.posX);
            int iPosY = MathHelper.floor(player.posY - 0.05);
            int iPosZ = MathHelper.floor(player.posZ);
            BlockPos pos1 = new BlockPos(iPosX, iPosY, iPosZ);
            IBlockState state = player.world.getBlockState(pos1);

            // If the block below is the moon block
            if (state.getBlock() == GCBlocks.blockMoon)
            {
                // And is the correct metadata (moon turf)
                if (state.getValue(BlockBasicMoon.BASIC_TYPE_MOON) == BlockBasicMoon.EnumBlockBasicMoon.MOON_TURF)
                {
                    // If it has been long enough since the last step
                    if (stats.getDistanceSinceLastStep() > 0.35)
                    {
                        Vector3 pos = new Vector3(player);
                        // Set the footprint position to the block below and add random number to stop z-fighting
                        pos.y = MathHelper.floor(player.posY) + player.getRNG().nextFloat() / 100.0F;

                        // Adjust footprint to left or right depending on step count
                        switch (stats.getLastStep())
                        {
                        case 0:
                            pos.translate(new Vector3(Math.sin(Math.toRadians(-player.rotationYaw + 90)) * 0.25, 0, Math.cos(Math.toRadians(-player.rotationYaw + 90)) * 0.25));
                            break;
                        case 1:
                            pos.translate(new Vector3(Math.sin(Math.toRadians(-player.rotationYaw - 90)) * 0.25, 0, Math.cos(Math.toRadians(-player.rotationYaw - 90)) * 0.25));
                            break;
                        }

                        pos = WorldUtil.getFootprintPosition(player.world, player.rotationYaw - 180, pos, new BlockVec3(player));

                        long chunkKey = ChunkPos.asLong(pos.intX() >> 4, pos.intZ() >> 4);
                        int lightmapVal = player.world.getCombinedLight(new BlockPos(pos.intX(), pos.intY(), pos.intZ()), 0);
                        FootprintRenderer.addFootprint(chunkKey, GCCoreUtil.getDimensionID(player.world), pos, player.rotationYaw, player.getName(), lightmapVal);

                        // Increment and cap step counter at 1
                        stats.setLastStep((stats.getLastStep() + 1) % 2);
                        stats.setDistanceSinceLastStep(0);
                    }
                    else
                    {
                        stats.setDistanceSinceLastStep(stats.getDistanceSinceLastStep() + motionSqrd);
                    }
                }
            }
        }
    }

    public boolean wakeUpPlayer(EntityPlayerSP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn, boolean bypass)
    {
        BlockPos c = player.bedLocation;

        if (c != null)
        {
            EventWakePlayer event = new EventWakePlayer(player, c, immediately, updateWorldFlag, setSpawn, bypass);
            MinecraftForge.EVENT_BUS.post(event);

            if (bypass || event.result == null || event.result == EntityPlayer.SleepResult.OK)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onBuild(int i, EntityPlayerSP player)
    {
        // 0 : opened GC inventory tab
        // 1,2,3 : Compressor, CF, Standard Wrench
        // 4,5,6 : Fuel loader, Launchpad, NASA Workbench
        // 7: oil found 8: placed rocket

        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);
        int flag = stats.getBuildFlags();
        if (flag == -1)
        {
            flag = 0;
        }
        int repeatCount = flag >> 9;
        if (repeatCount <= 3)
        {
            repeatCount++;
        }
        if ((flag & 1 << i) > 0)
        {
            return;
        }
        flag |= 1 << i;
        stats.setBuildFlags((flag & 511) + (repeatCount << 9));
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_BUILDFLAGS_UPDATE, GCCoreUtil.getDimensionID(player.world), new Object[] { stats.getBuildFlags() }));
        switch (i)
        {
        case 0:
        case 1:
        case 2:
        case 3:
            player.sendMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.help1") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/1" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/1" + "\"}}]"));
            player.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.message.help1a") + EnumColor.AQUA + " /gchelp"));
            break;
        case 4:
        case 5:
        case 6:
            player.sendMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.help2") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/2" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/2" + "\"}}]"));
            break;
        case 7:
            player.sendMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.help3") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/oil" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/oil" + "\"}}]"));
            break;
        case 8:
            player.sendMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.prelaunch") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/pre" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/pre" + "\"}}]"));
            break;
        }
    }
}
