package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
import micdoodle8.mods.galacticraft.core.entities.player.CapabilityStatsHandler;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC.OrientCameraEvent;
import micdoodle8.mods.galacticraft.core.event.EventLandingPadRemoval;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars.EnumMachineType;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.WorldGenEggs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

public class EventHandlerMars
{
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event)
    {
        if (event.getSource().damageType.equals("slimeling") && event.getSource() instanceof EntityDamageSource)
        {
            EntityDamageSource source = (EntityDamageSource) event.getSource();

            if (source.getEntity() instanceof EntitySlimeling && !source.getEntity().worldObj.isRemote)
            {
                ((EntitySlimeling) source.getEntity()).kills++;
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event)
    {
        if (!event.getEntity().isEntityInvulnerable(event.getSource()) && !event.getEntity().worldObj.isRemote && event.getEntityLiving().getHealth() <= 0.0F && !(event.getSource().isFireDamage() && event.getEntityLiving().isPotionActive(MobEffects.FIRE_RESISTANCE)))
        {
            Entity entity = event.getSource().getEntity();

            if (entity instanceof EntitySlimeling)
            {
                EntitySlimeling entitywolf = (EntitySlimeling) entity;

                if (entitywolf.isTamed())
                {
//                    event.entityLiving.recentlyHit = 100;
//                    event.entityLiving.attackingPlayer = null; TODO
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerWakeUp(EventWakePlayer event)
    {
        BlockPos c = event.getEntityPlayer().bedLocation;
        IBlockState state = event.getEntityPlayer().getEntityWorld().getBlockState(c);
        Block blockID = state.getBlock();

        if (blockID == MarsBlocks.machine && state.getValue(BlockMachineMars.TYPE) == EnumMachineType.CRYOGENIC_CHAMBER)
        {
            if (!event.flag1 && event.flag2 && event.flag3)
            {
                event.result = EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;

                if (event.getEntityPlayer().worldObj.isRemote && event.bypassed && event.getEntityPlayer() instanceof EntityPlayerSP)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_WAKE_PLAYER, event.getEntityPlayer().worldObj.provider.getDimension(), new Object[] {}));
                }
            }
            else if (!event.flag1 && !event.flag2 && event.flag3)
            {
                if (!event.getEntityPlayer().worldObj.isRemote)
                {
                    event.getEntityPlayer().heal(5.0F);
                    event.getEntityPlayer().getCapability(CapabilityStatsHandler.GC_STATS_CAPABILITY, null).setCryogenicChamberCooldown(6000);

                    for (WorldServer worldServer : event.getEntity().getServer().worldServers)
                    {
                        worldServer.setWorldTime(0);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerRotate(RenderPlayerGC.RotatePlayerEvent event)
    {
        BlockPos blockPos = event.getEntityPlayer().bedLocation;
        IBlockState state = event.getEntityPlayer().worldObj.getBlockState(blockPos);
        if (state.getBlock() == GCBlocks.fakeBlock && state.getValue(BlockMulti.MULTI_TYPE) == BlockMulti.EnumBlockMultiType.CRYO_CHAMBER)
        {
            TileEntity tile = event.getEntityPlayer().worldObj.getTileEntity(blockPos);
            if (tile instanceof TileEntityMulti)
            {
                state = event.getEntityPlayer().worldObj.getBlockState(((TileEntityMulti) tile).mainBlockPosition);
            }
        }

        if (state.getBlock() == MarsBlocks.machine && state.getValue(BlockMachineMars.TYPE) == BlockMachineMars.EnumMachineType.CRYOGENIC_CHAMBER)
        {
            event.shouldRotate = true;
        	event.vanillaOverride = true;
        }
    }

    private WorldGenerator eggGenerator;

    @SubscribeEvent
    public void onPlanetDecorated(GCCoreEventPopulate.Post event)
    {
        if (this.eggGenerator == null)
        {
            this.eggGenerator = new WorldGenEggs(MarsBlocks.rock);
        }

        if (event.worldObj.provider instanceof WorldProviderMars)
        {
            int eggsPerChunk = 2;
            BlockPos blockpos;

            for (int eggCount = 0; eggCount < eggsPerChunk; ++eggCount)
            {
                blockpos = event.pos.add(event.rand.nextInt(16), event.rand.nextInt(128), event.rand.nextInt(16));
                this.eggGenerator.generate(event.worldObj, event.rand, blockpos);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void orientCamera(OrientCameraEvent event)
    {
        EntityPlayer entity = Minecraft.getMinecraft().thePlayer;

        if (entity != null)
        {
            int x = MathHelper.floor_double(entity.posX);
            int y = MathHelper.floor_double(entity.posY);
            int z = MathHelper.floor_double(entity.posZ);
            TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(x, y - 1, z));

            if (tile instanceof TileEntityMulti)
            {
                tile = ((TileEntityMulti) tile).getMainBlockTile();
            }

            if (tile instanceof TileEntityCryogenicChamber)
            {
                entity.rotationPitch = 0;

                switch (tile.getBlockMetadata() & 3)
                {
                case 0:
                    GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(-0.4F, -0.5F, 4.1F);
                    GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
                    entity.rotationYaw = 0;
                    entity.rotationYawHead = 320;
                    break;
                case 1:
                    GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0, -0.5F, 4.1F);
                    GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
                    entity.rotationYaw = 0;
                    entity.rotationYawHead = 45;
                    break;
                case 2:
                    GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0, -0.5F, 4.1F);
                    GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                    entity.rotationYaw = 0;
                    entity.rotationYawHead = 45;
                    break;
                case 3:
                    GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, -0.5F, 4.1F);
                    entity.rotationYaw = 0;
                    entity.rotationYawHead = 335;
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onLandingPadRemoved(EventLandingPadRemoval event)
    {
        TileEntity tile = event.world.getTileEntity(event.pos);

        if (tile instanceof IFuelDock)
        {
            IFuelDock dock = (IFuelDock) tile;

            for (ILandingPadAttachable connectedTile : dock.getConnectedTiles())
            {
                if (connectedTile instanceof TileEntityLaunchController)
                {
                    final TileEntityLaunchController launchController = (TileEntityLaunchController) connectedTile;
                    if (launchController.getEnergyStoredGC() > 0.0F && launchController.launchPadRemovalDisabled && !launchController.getDisabled(0))
                {
                    	event.allow = false;
                    	return;
                    }
                    break;
                }
            }
        }
    }
}
