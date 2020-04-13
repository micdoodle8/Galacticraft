package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.event.ZeroGravityEvent;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.client.EventHandlerClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.List;

public class GCEntityClientPlayerMP extends EntityPlayerSP
{
    private boolean lastIsFlying;
    private boolean sneakLast;
    private int lastLandingTicks;
    private boolean checkedCape = false;
    private ResourceLocation galacticraftCape = null;

    public GCEntityClientPlayerMP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatisticsManager statFileWriter, RecipeBook book)
    {
        super(mcIn, worldIn, netHandler, statFileWriter, book);
    }

    @Override
    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn)
    {
        if (!ClientProxyCore.playerClientHandler.wakeUpPlayer(this, immediately, updateWorldFlag, setSpawn))
        {
            super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return ClientProxyCore.playerClientHandler.isEntityInsideOpaqueBlock(this, super.isEntityInsideOpaqueBlock());
    }

    @Override
    public boolean isServerWorld()
    {
        return true;
    }
    
    
    @Override
    public void onLivingUpdate()
    {
        ClientProxyCore.playerClientHandler.onLivingUpdatePre(this);
        try {
            if (this.world.provider instanceof IZeroGDimension)
            {
                //  from: EntityPlayerSP
                if (this.sprintingTicksLeft > 0)
                {
                    --this.sprintingTicksLeft;

                    if (this.sprintingTicksLeft == 0)
                    {
                        this.setSprinting(false);
                    }
                }

                if (this.sprintToggleTimer > 0)
                {
                    --this.sprintToggleTimer;
                }

                this.prevTimeInPortal = this.timeInPortal;

                if (this.inPortal)
                {
                    if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame())
                    {
                        this.mc.displayGuiScreen((GuiScreen)null);
                    }

                    if (this.timeInPortal == 0.0F)
                    {
                        this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRIGGER, this.rand.nextFloat() * 0.4F + 0.8F));
                    }

                    this.timeInPortal += 0.0125F;

                    if (this.timeInPortal >= 1.0F)
                    {
                        this.timeInPortal = 1.0F;
                    }

                    this.inPortal = false;
                }
                else if (this.isPotionActive(MobEffects.NAUSEA) && this.getActivePotionEffect(MobEffects.NAUSEA).getDuration() > 60)
                {
                    this.timeInPortal += 0.006666667F;

                    if (this.timeInPortal > 1.0F)
                    {
                        this.timeInPortal = 1.0F;
                    }
                }
                else
                {
                    if (this.timeInPortal > 0.0F)
                    {
                        this.timeInPortal -= 0.05F;
                    }

                    if (this.timeInPortal < 0.0F)
                    {
                        this.timeInPortal = 0.0F;
                    }
                }

                if (this.timeUntilPortal > 0)
                {
                    --this.timeUntilPortal;
                }

                boolean flag1 = this.movementInput.sneak;
                float sprintlevel = 0.8F;
                boolean flag2 = this.movementInput.moveForward >= sprintlevel;
                this.movementInput.updatePlayerMoveState();

                if (this.isHandActive() && !this.isRiding())
                {
                    this.movementInput.moveStrafe *= 0.2F;
                    this.movementInput.moveForward *= 0.2F;
                    this.sprintToggleTimer = 0;
                }

                //CUSTOM-------------------
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
                if (stats.getLandingTicks() > 0)
                {
                    this.movementInput.moveStrafe *= 0.5F;
                    this.movementInput.moveForward *= 0.5F;
                }
                //-----------END CUSTOM
                    
                this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
                this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
                this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
                this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
                boolean flag4 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

                if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= 0.8F && !this.isSprinting() && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS))
                {
                    if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown())
                    {
                        this.sprintToggleTimer = 7;
                    }
                    else
                    {
                        this.setSprinting(true);
                    }
                }

                if (!this.isSprinting() && this.movementInput.moveForward >= sprintlevel && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown())
                {
                    this.setSprinting(true);
                }

                if (this.isSprinting() && (this.movementInput.moveForward < sprintlevel || this.collidedHorizontally || !flag4))
                {
                    this.setSprinting(false);
                }

                //Omit flying toggles - flying will be controlled by freefall status

                if (this.capabilities.isFlying && this.isCurrentViewEntity())
                {
                    if (this.movementInput.sneak)
                    {
                        this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
                    }

                    if (this.movementInput.jump)
                    {
                        this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
                    }
                }

                //Omit horse jumping - no horse jumping in space!

                // -from: EntityPlayer

                //Omit fly toggle timer
                
                if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("naturalRegeneration"))
                {
                    if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0)
                    {
                        this.heal(1.0F);
                    }

                    if (this.foodStats.needFood() && this.ticksExisted % 10 == 0)
                    {
                        this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
                    }
                }

                this.inventory.decrementAnimations();
                this.prevCameraYaw = this.cameraYaw;

                //  from: EntityLivingBase
                if (this.newPosRotationIncrements > 0)
                {
                    double d0 = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
                    double d1 = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
                    double d2 = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
                    double d3 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
                    this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.newPosRotationIncrements);
                    this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                    --this.newPosRotationIncrements;
                    this.setPosition(d0, d1, d2);
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                }
                else if (!this.isServerWorld())
                {
                    this.motionX *= 0.98D;
                    this.motionY *= 0.98D;
                    this.motionZ *= 0.98D;
                }

                if (Math.abs(this.motionX) < 0.005D)
                {
                    this.motionX = 0.0D;
                }

                if (Math.abs(this.motionY) < 0.005D)
                {
                    this.motionY = 0.0D;
                }

                if (Math.abs(this.motionZ) < 0.005D)
                {
                    this.motionZ = 0.0D;
                }

                this.world.profiler.startSection("ai");

                if (this.isMovementBlocked())
                {
                    this.isJumping = false;
                    this.moveStrafing = 0.0F;
                    this.moveForward = 0.0F;
                    this.randomYawVelocity = 0.0F;
                }
                else
                    this.updateEntityActionState();
                
                this.world.profiler.endSection();
                this.world.profiler.startSection("travel");
                this.moveStrafing *= 0.98F;
                this.moveForward *= 0.98F;
                this.randomYawVelocity *= 0.9F;

                // CUSTOM--------------
                AxisAlignedBB aABB = this.getEntityBoundingBox();
                if ((aABB.minY % 1D) == 0.5D) this.setEntityBoundingBox(aABB.offset(0D, 0.00001D, 0D));
                //-----------END CUSTOM
                
                //NOTE: No Elytra movement from this.updateElytra() in a zero G dimension
                this.travel(this.moveStrafing, this.moveVertical, this.moveForward);
                this.world.profiler.endSection();
                this.world.profiler.startSection("push");

                if (!this.world.isRemote)
                {
                    this.collideWithNearbyEntities();
                }

                this.world.profiler.endSection();

                // -from: EntityPlayer
                
                //Omit IAttributeInstance - seems relevant only on server
                
                //Omit        this.jumpMovementFactor = this.speedInAir;
                //(no bounding in space)
                
                float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                float f1 = (float)(Math.atan(-this.motionY * 0.20000000298023224D) * 15.0D);

                if (f > 0.1F)
                {
                    f = 0.1F;
                }

                if (!this.onGround || this.getHealth() <= 0.0F)
                {
                    f = 0.0F;
                }

                if (this.onGround || this.getHealth() <= 0.0F)
                {
                    f1 = 0.0F;
                }

                this.cameraYaw += (f - this.cameraYaw) * 0.4F;
                this.cameraPitch += (f1 - this.cameraPitch) * 0.8F;

                if (this.getHealth() > 0.0F && !this.isSpectator())
                {
                    AxisAlignedBB axisalignedbb = null;

                    if (this.getRidingEntity() != null && !this.getRidingEntity().isDead)
                    {
                        axisalignedbb = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox()).grow(1.0D, 0.0D, 1.0D);
                    }
                    else
                    {
                        axisalignedbb = this.getEntityBoundingBox().grow(1.0D, 0.5D, 1.0D);
                    }

                    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);

                    for (int i = 0; i < list.size(); ++i)
                    {
                        Entity entity = (Entity)list.get(i);

                        if (!entity.isDead)
                        {
                            entity.onCollideWithPlayer(this);
                        }
                    }
                }

                //  from: EntityPlayerSP
                //(modified CUSTOM)
                if (this.lastIsFlying != this.capabilities.isFlying)
                {
                    this.lastIsFlying = this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                }
            }
            else
            {
                super.onLivingUpdate();
            }
        }
        catch (RuntimeException e)
        {
            FMLLog.severe("A mod has crashed while Minecraft was doing a normal player tick update.  See details below.  GCEntityClientPlayerMP is in this because that is the player class name when Galacticraft is installed.  This is =*NOT*= a bug in Galacticraft, please report it to the mod indicated by the first lines of the crash report.");
            throw (e);
        }
        ClientProxyCore.playerClientHandler.onLivingUpdatePost(this);
    }

    @Override
    public void move(MoverType type, double x, double y, double z)
    {
        super.move(type, x, y, z);
        ClientProxyCore.playerClientHandler.move(this, type, x, y, z);
    }

    @Override
    public void onUpdate()
    {
        ClientProxyCore.playerClientHandler.onUpdate(this);
        super.onUpdate();
    }

    @Override
    public boolean isSneaking()
    {
        if (this.world.provider instanceof IZeroGDimension)
        {
            ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.SneakOverride(this);
            MinecraftForge.EVENT_BUS.post(zeroGEvent);
            if (zeroGEvent.isCanceled())
            {
                return super.isSneaking();
            }

            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
            if (stats.getLandingTicks() > 0)
            {
                if (this.lastLandingTicks == 0)
                    this.lastLandingTicks = stats.getLandingTicks();

                this.sneakLast = stats.getLandingTicks() < this.lastLandingTicks;
                return sneakLast;
            }
            else
                this.lastLandingTicks = 0;
            if (stats.getFreefallHandler().pjumpticks > 0)
            {
                this.sneakLast = true;
                return true;
            }
            if (EventHandlerClient.sneakRenderOverride)
            {
                if (this.movementInput != null && this.movementInput.sneak != this.sneakLast)
                { 
                    return false;
                }
                //                if (stats.freefallHandler.testFreefall(this)) return false;
                if (stats.isInFreefall() || stats.getFreefallHandler().onWall)
                {
                    this.sneakLast = false;
                    return false;
                }
            }
            this.sneakLast = this.movementInput == null ? false : this.movementInput.sneak;
        }
        else
        {
            this.sneakLast = false;
            if (EventHandlerClient.sneakRenderOverride && this.onGround && this.inventory.getCurrentItem() != null && this.inventory.getCurrentItem().getItem() instanceof IHoldableItem && !(this.getRidingEntity() instanceof ICameraZoomEntity))
            {
                IHoldableItem holdableItem = (IHoldableItem) this.inventory.getCurrentItem().getItem();

                if (holdableItem.shouldCrouch(this))
                {
                    return true;
                }
            }
        }
        return super.isSneaking();
    }
    
    @Override
    public float getEyeHeight()
    {
        float f = eyeHeight;

        if (this.isPlayerSleeping())
        {
            return 0.2F;
        }

        float ySize = 0.0F;
        if (this.world.provider instanceof IZeroGDimension)
        {
            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
            if (stats.getLandingTicks() > 0)
            {
                ySize = stats.getLandingYOffset()[stats.getLandingTicks()];
                if (this.movementInput.sneak && ySize < 0.08F)
                {
                    ySize = 0.08F;
                }
            }
            else if (stats.getFreefallHandler().pjumpticks > 0)
            {
                ySize = 0.01F * stats.getFreefallHandler().pjumpticks;
            }
            else if (this.isSneaking() && !stats.isInFreefall() && !stats.getFreefallHandler().onWall)
            {
                ySize = 0.08F;
            }
        }
        else if (this.isSneaking() && this.movementInput != null && this.movementInput.sneak)
        {
            ySize = 0.08F;
        }

        return f - ySize;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getBedOrientationInDegrees()
    {
        return ClientProxyCore.playerClientHandler.getBedOrientationInDegrees(this, super.getBedOrientationInDegrees());
    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void setVelocity(double xx, double yy, double zz)
//    {
//    	if (this.world.provider instanceof WorldProviderOrbit)
//    	{
//    		((WorldProviderOrbit)this.world.provider).setVelocityClient(this, xx, yy, zz);
//    	}
//    	super.setVelocity(xx, yy, zz);
//    }
//

    /*@Override
    public void setInPortal()
    {
    	if (!(this.world.provider instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */

    @Override
    public ResourceLocation getLocationCape()
    {
        if (this.getRidingEntity() instanceof EntitySpaceshipBase)
        {
            // Don't draw any cape if riding a rocket (the cape renders outside the rocket model!)
            return null;
        }
        
        ResourceLocation vanillaCape = super.getLocationCape();

        if (!this.checkedCape)
        {
            NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
            this.galacticraftCape = ClientProxyCore.capeMap.get(networkplayerinfo.getGameProfile().getId().toString().replace("-", ""));
            this.checkedCape = true;
        }

        if ((ConfigManagerCore.overrideCapes || vanillaCape == null) && galacticraftCape != null)
        {
            return galacticraftCape;
        }

        return vanillaCape;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        double height = this.posY + (double)this.getEyeHeight();
        if (height > 255D) height = 255D;
        BlockPos blockpos = new BlockPos(this.posX, height, this.posZ);
        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
    }
}
