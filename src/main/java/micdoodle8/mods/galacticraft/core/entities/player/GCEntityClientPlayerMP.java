package micdoodle8.mods.galacticraft.core.entities.player;

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
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
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

    public GCEntityClientPlayerMP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatisticsManager statFileWriter)
    {
        super(mcIn, worldIn, netHandler, statFileWriter);
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
            if (this.worldObj.provider instanceof IZeroGDimension)
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

                if (this.isSprinting() && (this.movementInput.moveForward < sprintlevel || this.isCollidedHorizontally || !flag4))
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
                
                if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getBoolean("naturalRegeneration"))
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

                this.worldObj.theProfiler.startSection("ai");

                if (this.isMovementBlocked())
                {
                    this.isJumping = false;
                    this.moveStrafing = 0.0F;
                    this.moveForward = 0.0F;
                    this.randomYawVelocity = 0.0F;
                }
                else
                    this.updateEntityActionState();
                
                this.worldObj.theProfiler.endSection();
                this.worldObj.theProfiler.startSection("travel");
                this.moveStrafing *= 0.98F;
                this.moveForward *= 0.98F;
                this.randomYawVelocity *= 0.9F;

                // CUSTOM--------------
                AxisAlignedBB aABB = this.getEntityBoundingBox();
                if ((aABB.minY % 1D) == 0.5D) this.setEntityBoundingBox(aABB.offset(0D, 0.00001D, 0D));
                //-----------END CUSTOM
                
                this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
                this.worldObj.theProfiler.endSection();
                this.worldObj.theProfiler.startSection("push");

                if (!this.worldObj.isRemote)
                {
                    this.collideWithNearbyEntities();
                }

                this.worldObj.theProfiler.endSection();

                // -from: EntityPlayer
                
                //Omit IAttributeInstance - seems relevant only on server
                
                //Omit        this.jumpMovementFactor = this.speedInAir;
                //(no bounding in space)
                
                float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
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
                        axisalignedbb = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox()).expand(1.0D, 0.0D, 1.0D);
                    }
                    else
                    {
                        axisalignedbb = this.getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
                    }

                    List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);

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
                super.onLivingUpdate();
        }
        catch (RuntimeException e)
        {
            FMLLog.severe("A mod has crashed while Minecraft was doing a normal player tick update.  See details below.  GCEntityClientPlayerMP is in this because that is the player class name when Galacticraft is installed.  This is =*NOT*= a bug in Galacticraft, please report it to the mod indicated by the first lines of the crash report.");
            throw (e);
        }
        ClientProxyCore.playerClientHandler.onLivingUpdatePost(this);
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        ClientProxyCore.playerClientHandler.moveEntity(this, par1, par3, par5);
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
        if (this.worldObj.provider instanceof IZeroGDimension)
        {
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
                if (this.movementInput.sneak != this.sneakLast)
                { 
                    this.sneakLast = this.movementInput.sneak;
                    return false;
                }
                //                if (stats.freefallHandler.testFreefall(this)) return false;
                if (stats.isInFreefall() || stats.getFreefallHandler().onWall)
                {
                    this.sneakLast = false;
                    return false;
                }
            }
        }
        this.sneakLast = false;
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
        if (this.worldObj.provider instanceof IZeroGDimension)
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
        else if (this.isSneaking())
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
//    	if (this.worldObj.provider instanceof WorldProviderOrbit)
//    	{
//    		((WorldProviderOrbit)this.worldObj.provider).setVelocityClient(this, xx, yy, zz);	
//    	}
//    	super.setVelocity(xx, yy, zz);
//    }
//

    /*@Override
    public void setInPortal()
    {
    	if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */

    @Override
    public ResourceLocation getLocationCape()
    {
        ResourceLocation vanillaCape = super.getLocationCape();

        if (!this.checkedCape)
        {
            NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
            this.galacticraftCape = ClientProxyCore.capeMap.get(networkplayerinfo.getGameProfile().getName());
            this.checkedCape = true;
        }

        if ((ConfigManagerCore.overrideCapes || vanillaCape == null) && galacticraftCape != null)
        {
            return galacticraftCape;
        }

        return vanillaCape;
    }
}
