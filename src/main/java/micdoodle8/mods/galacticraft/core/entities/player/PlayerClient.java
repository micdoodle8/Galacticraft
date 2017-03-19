package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderZeroGravity;
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
    public void moveEntity(EntityPlayerSP player, double par1, double par3, double par5)
    {
        this.updateFeet(player, par1, par5);
    }

    @Override
    public boolean wakeUpPlayer(EntityPlayerSP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn)
    {
        return this.wakeUpPlayer(player, immediately, updateWorldFlag, setSpawn, false);
    }

    @Override
    public void onUpdate(EntityPlayerSP player)
    {
        IStatsClientCapability stats = player.getCapability(CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY, null);
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
        IStatsClientCapability stats = player.getCapability(CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY, null);
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
        IStatsClientCapability stats = player.getCapability(CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY, null);

        if (player.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            if (!startup)
            {
                stats.setInFreefallLast(stats.isInFreefall());
                stats.setInFreefall(stats.getFreefallHandler().testFreefall(player));
                startup = true;
            }
            if (player.worldObj.provider instanceof WorldProviderZeroGravity)
            {
                stats.setInFreefallLast(stats.isInFreefall());
                stats.setInFreefall(stats.getFreefallHandler().testFreefall(player));
                this.downMot2 = stats.getDownMotionLast();
                stats.setDownMotionLast(player.motionY);
                stats.getFreefallHandler().preVanillaMotion(player);
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

    @Override
    public void onLivingUpdatePost(EntityPlayerSP player)
    {
        IStatsClientCapability stats = player.getCapability(CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY, null);

        if (player.worldObj.provider instanceof WorldProviderZeroGravity)
        {
            stats.getFreefallHandler().postVanillaMotion(player);

            if (stats.isInFreefall())
            {
                //No limb swing
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
            else
            {
                if (stats.isInFreefallLast() && this.downMot2 < -0.01D)
                {
                    stats.setLandingTicks(2 - (int) (Math.min(this.downMot2, stats.getDownMotionLast()) * 75));
                    if (stats.getLandingTicks() > 6)
                    {
                        stats.setLandingTicks(6);
                    }
                }
            }

            if (stats.getLandingTicks() > 0)
            {
                stats.setLandingTicks(stats.getLandingTicks() - 1);
            }
        }
        else
        {
            stats.setInFreefall(false);
        }

        boolean ridingThirdPersonEntity = player.getRidingEntity() instanceof ICameraZoomEntity && ((ICameraZoomEntity) player.getRidingEntity()).defaultThirdPerson();

        if (ridingThirdPersonEntity && !stats.isLastRidingCameraZoomEntity())
        {
            if(!ConfigManagerCore.disableVehicleCameraChanges)
                FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;
        }

        if (player.getRidingEntity() != null && player.getRidingEntity() instanceof ICameraZoomEntity)
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

        PlayerGearData gearData = ModelPlayerGC.getGearData(player);

        stats.setUsingParachute(false);

        if (gearData != null)
        {
            stats.setUsingParachute(gearData.getParachute() != null);
            if(!GalacticraftCore.isHeightConflictingModInstalled)
            {
                if (gearData.getMask() >= 0)
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
            if (player.worldObj.getTileEntity(player.bedLocation) instanceof TileEntityAdvanced)
            {
//                int j = player.worldObj.getBlock(x, y, z).getBedDirection(player.worldObj, x, y, z);
                IBlockState state = player.worldObj.getBlockState(player.bedLocation);
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
        IStatsClientCapability stats = player.getCapability(CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY, null);
        double motionSqrd = motionX * motionX + motionZ * motionZ;

        // If the player is on the moon, not airbourne and not riding anything
        if (motionSqrd > 0.001 && player.worldObj != null && player.worldObj.provider instanceof WorldProviderMoon && player.getRidingEntity() == null && !player.capabilities.isFlying)
        {
            int iPosX = (int) Math.floor(player.posX);
            int iPosY = (int) Math.floor(player.posY - 1);
            int iPosZ = (int) Math.floor(player.posZ);
            BlockPos pos1 = new BlockPos(iPosX, iPosY, iPosZ);
            IBlockState state = player.worldObj.getBlockState(pos1);

            // If the block below is the moon block
            if (state.getBlock() == GCBlocks.blockMoon)
            {
                // And is the correct metadata (moon turf)
                if (state.getBlock().getMetaFromState(state) == 5)
                {
                    // If it has been long enough since the last step
                    if (stats.getDistanceSinceLastStep() > 0.35)
                    {
                        Vector3 pos = new Vector3(player);
                        // Set the footprint position to the block below and add random number to stop z-fighting
                        pos.y = MathHelper.floor_double(player.posY) + player.getRNG().nextFloat() / 100.0F;

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

                        pos = WorldUtil.getFootprintPosition(player.worldObj, player.rotationYaw - 180, pos, new BlockVec3(player));

                        long chunkKey = ChunkPos.asLong(pos.intX() >> 4, pos.intZ() >> 4);
                        FootprintRenderer.addFootprint(chunkKey, player.worldObj.provider.getDimension(), pos, player.rotationYaw, player.getName());

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

        IStatsClientCapability stats = player.getCapability(CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY, null);
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
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_BUILDFLAGS_UPDATE, player.worldObj.provider.getDimension(), new Object[] { stats.getBuildFlags() }));
        switch (i)
        {
        case 0:
        case 1:
        case 2:
        case 3:
            player.addChatMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.help1") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/1" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/1" + "\"}}]"));
            player.addChatMessage(new TextComponentString(GCCoreUtil.translate("gui.message.help1a") + EnumColor.AQUA + " /gchelp"));
            break;
        case 4:
        case 5:
        case 6:
            player.addChatMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.help2") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/2" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/2" + "\"}}]"));
            break;
        case 7:
            player.addChatMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.help3") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/oil" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/oil" + "\"}}]"));
            break;
        case 8:
            player.addChatMessage(ITextComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.prelaunch") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki." + Constants.PREFIX + "com/wiki/pre" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\"" + GCCoreUtil.translate("gui.message.clicklink") + "\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki." + Constants.PREFIX + "com/wiki/pre" + "\"}}]"));
            break;
        }
    }
}
