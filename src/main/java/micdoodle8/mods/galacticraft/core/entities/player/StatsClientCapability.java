package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;

public class StatsClientCapability extends GCPlayerStatsClient
{
    public boolean usingParachute;
    public boolean lastUsingParachute;
    public boolean usingAdvancedGoggles;
    public int thermalLevel;
    public boolean thermalLevelNormalising;
    public int thirdPersonView = 0;
    public long tick;
    public boolean oxygenSetupValid = true;
    AxisAlignedBB boundingBoxBefore;
    public boolean lastOnGround;

    public double distanceSinceLastStep;
    public int lastStep;
    public boolean inFreefall;
    public boolean inFreefallLast;
    public boolean inFreefallFirstCheck;
    public double downMotionLast;
    public boolean lastRidingCameraZoomEntity;
    public int landingTicks;
    public static final int MAX_LANDINGTICKS = 15;
    public float[] landingYOffset = new float[MAX_LANDINGTICKS + 1];

    public boolean platformControlled;
    private TileEntityPlatform platformTarget;
    private TileEntityPlatform platformMoving;
    private double platformTargetY;
    private double platformVelocityTarget;
    private double platformVelocityCurrent;
    private boolean platformPacketSent;

    public EnumGravity gdir = EnumGravity.down;
    public float gravityTurnRate;
    public float gravityTurnRatePrev;
    public float gravityTurnVecX;
    public float gravityTurnVecY;
    public float gravityTurnVecZ;
    public float gravityTurnYaw;

    public int spaceRaceInviteTeamID;
    public boolean lastZoomed;
    public int buildFlags = -1;

    public boolean ssOnGroundLast;
    private float dungeonDirection;

    public FreefallHandler freefallHandler = new FreefallHandler(this);

    public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();

    @Override
    public void setGravity(EnumGravity newGravity)
    {
        if (this.gdir == newGravity)
        {
            return;
        }
        this.gravityTurnRatePrev = this.gravityTurnRate = 0.0F;
        float turnSpeed = 0.05F;
        this.gravityTurnVecX = 0.0F;
        this.gravityTurnVecY = 0.0F;
        this.gravityTurnVecZ = 0.0F;
        this.gravityTurnYaw = 0.0F;

        switch (this.gdir.getIntValue())
        {
        case 1:
            switch (newGravity.getIntValue())
            {
            case 1:
                break;
            case 2:
                this.gravityTurnVecX = -2.0F;
                break;
            case 3:
                this.gravityTurnVecY = -1.0F;
                this.gravityTurnYaw = -90.0F;
                break;
            case 4:
                this.gravityTurnVecY = 1.0F;
                this.gravityTurnYaw = 90.0F;
                break;
            case 5:
                this.gravityTurnVecX = 1.0F;
                break;
            case 6:
                this.gravityTurnVecX = -1.0F;
            }

            break;
        case 2:
            switch (newGravity.getIntValue())
            {
            case 1:
                this.gravityTurnVecX = -2.0F;
                break;
            case 2:
                break;
            case 3:
                this.gravityTurnVecY = 1.0F;
                this.gravityTurnYaw = 90.0F;
                break;
            case 4:
                this.gravityTurnVecY = -1.0F;
                this.gravityTurnYaw = -90.0F;
                break;
            case 5:
                this.gravityTurnVecX = -1.0F;
                break;
            case 6:
                this.gravityTurnVecX = 1.0F;
            }

            break;
        case 3:
            switch (newGravity.getIntValue())
            {
            case 1:
                this.gravityTurnVecY = 1.0F;
                this.gravityTurnYaw = 90.0F;
                break;
            case 2:
                this.gravityTurnVecY = -1.0F;
                this.gravityTurnYaw = -90.0F;
                break;
            case 3:
                break;
            case 4:
                this.gravityTurnVecZ = -2.0F;
                break;
            case 5:
                this.gravityTurnVecZ = -1.0F;
                this.gravityTurnYaw = -180.0F;
                break;
            case 6:
                this.gravityTurnVecZ = 1.0F;
            }

            break;
        case 4:
            switch (newGravity.getIntValue())
            {
            case 1:
                this.gravityTurnVecY = -1.0F;
                this.gravityTurnYaw = -90.0F;
                break;
            case 2:
                this.gravityTurnVecY = 1.0F;
                this.gravityTurnYaw = 90.0F;
                break;
            case 3:
                this.gravityTurnVecZ = -2.0F;
                break;
            case 4:
                break;
            case 5:
                this.gravityTurnVecZ = 1.0F;
                this.gravityTurnYaw = -180.0F;
                break;
            case 6:
                this.gravityTurnVecZ = -1.0F;
            }

            break;
        case 5:
            switch (newGravity.getIntValue())
            {
            case 1:
                this.gravityTurnVecX = -1.0F;
                break;
            case 2:
                this.gravityTurnVecX = 1.0F;
                break;
            case 3:
                this.gravityTurnVecZ = 1.0F;
                this.gravityTurnYaw = 180.0F;
                break;
            case 4:
                this.gravityTurnVecZ = -1.0F;
                this.gravityTurnYaw = 180.0F;
                break;
            case 5:
                break;
            case 6:
                this.gravityTurnVecX = -2.0F;
            }

            break;
        case 6:
            switch (newGravity.getIntValue())
            {
            case 1:
                this.gravityTurnVecX = 1.0F;
                break;
            case 2:
                this.gravityTurnVecX = -1.0F;
                break;
            case 3:
                this.gravityTurnVecZ = -1.0F;
                break;
            case 4:
                this.gravityTurnVecZ = 1.0F;
                break;
            case 5:
                this.gravityTurnVecX = -2.0F;
            case 6:
            }
            break;
        }

        this.gdir = newGravity;
    }

    @Override
    public boolean isUsingParachute()
    {
        return usingParachute;
    }

    @Override
    public void setUsingParachute(boolean usingParachute)
    {
        this.usingParachute = usingParachute;
    }

    @Override
    public boolean isLastUsingParachute()
    {
        return lastUsingParachute;
    }

    @Override
    public void setLastUsingParachute(boolean lastUsingParachute)
    {
        this.lastUsingParachute = lastUsingParachute;
    }

    @Override
    public boolean isUsingAdvancedGoggles()
    {
        return usingAdvancedGoggles;
    }

    @Override
    public void setUsingAdvancedGoggles(boolean usingAdvancedGoggles)
    {
        this.usingAdvancedGoggles = usingAdvancedGoggles;
    }

    @Override
    public int getThermalLevel()
    {
        return thermalLevel;
    }

    @Override
    public void setThermalLevel(int thermalLevel)
    {
        this.thermalLevel = thermalLevel;
    }

    @Override
    public boolean isThermalLevelNormalising()
    {
        return thermalLevelNormalising;
    }

    @Override
    public void setThermalLevelNormalising(boolean thermalLevelNormalising)
    {
        this.thermalLevelNormalising = thermalLevelNormalising;
    }

    @Override
    public int getThirdPersonView()
    {
        return thirdPersonView;
    }

    @Override
    public void setThirdPersonView(int thirdPersonView)
    {
        this.thirdPersonView = thirdPersonView;
    }

    @Override
    public long getTick()
    {
        return tick;
    }

    @Override
    public void setTick(long tick)
    {
        this.tick = tick;
    }

    @Override
    public boolean isOxygenSetupValid()
    {
        return oxygenSetupValid;
    }

    @Override
    public void setOxygenSetupValid(boolean oxygenSetupValid)
    {
        this.oxygenSetupValid = oxygenSetupValid;
    }

    @Override
    public AxisAlignedBB getBoundingBoxBefore()
    {
        return boundingBoxBefore;
    }

    @Override
    public void setBoundingBoxBefore(AxisAlignedBB boundingBoxBefore)
    {
        this.boundingBoxBefore = boundingBoxBefore;
    }

    @Override
    public boolean isLastOnGround()
    {
        return lastOnGround;
    }

    @Override
    public void setLastOnGround(boolean lastOnGround)
    {
        this.lastOnGround = lastOnGround;
    }

    @Override
    public double getDistanceSinceLastStep()
    {
        return distanceSinceLastStep;
    }

    @Override
    public void setDistanceSinceLastStep(double distanceSinceLastStep)
    {
        this.distanceSinceLastStep = distanceSinceLastStep;
    }

    @Override
    public int getLastStep()
    {
        return lastStep;
    }

    @Override
    public void setLastStep(int lastStep)
    {
        this.lastStep = lastStep;
    }

    @Override
    public boolean isInFreefall()
    {
        return inFreefall;
    }

    @Override
    public void setInFreefall(boolean inFreefall)
    {
        this.inFreefall = inFreefall;
    }

    @Override
    public boolean isInFreefallLast()
    {
        return inFreefallLast;
    }

    @Override
    public void setInFreefallLast(boolean inFreefallLast)
    {
        this.inFreefallLast = inFreefallLast;
    }

    @Override
    public boolean isInFreefallFirstCheck()
    {
        return inFreefallFirstCheck;
    }

    @Override
    public void setInFreefallFirstCheck(boolean inFreefallFirstCheck)
    {
        this.inFreefallFirstCheck = inFreefallFirstCheck;
    }

    @Override
    public double getDownMotionLast()
    {
        return downMotionLast;
    }

    @Override
    public void setDownMotionLast(double downMotionLast)
    {
        this.downMotionLast = downMotionLast;
    }

    @Override
    public boolean isLastRidingCameraZoomEntity()
    {
        return lastRidingCameraZoomEntity;
    }

    @Override
    public void setLastRidingCameraZoomEntity(boolean lastRidingCameraZoomEntity)
    {
        this.lastRidingCameraZoomEntity = lastRidingCameraZoomEntity;
    }

    @Override
    public int getLandingTicks()
    {
        return landingTicks;
    }

    @Override
    public void setLandingTicks(int landingTicks)
    {
        this.landingTicks = landingTicks;
    }

    @Override
    public EnumGravity getGdir()
    {
        return gdir;
    }

    @Override
    public void setGdir(EnumGravity gdir)
    {
        this.gdir = gdir;
    }

    @Override
    public float getGravityTurnRate()
    {
        return gravityTurnRate;
    }

    @Override
    public void setGravityTurnRate(float gravityTurnRate)
    {
        this.gravityTurnRate = gravityTurnRate;
    }

    @Override
    public float getGravityTurnRatePrev()
    {
        return gravityTurnRatePrev;
    }

    @Override
    public void setGravityTurnRatePrev(float gravityTurnRatePrev)
    {
        this.gravityTurnRatePrev = gravityTurnRatePrev;
    }

    @Override
    public float getGravityTurnVecX()
    {
        return gravityTurnVecX;
    }

    @Override
    public void setGravityTurnVecX(float gravityTurnVecX)
    {
        this.gravityTurnVecX = gravityTurnVecX;
    }

    @Override
    public float getGravityTurnVecY()
    {
        return gravityTurnVecY;
    }

    @Override
    public void setGravityTurnVecY(float gravityTurnVecY)
    {
        this.gravityTurnVecY = gravityTurnVecY;
    }

    @Override
    public float getGravityTurnVecZ()
    {
        return gravityTurnVecZ;
    }

    @Override
    public void setGravityTurnVecZ(float gravityTurnVecZ)
    {
        this.gravityTurnVecZ = gravityTurnVecZ;
    }

    @Override
    public float getGravityTurnYaw()
    {
        return gravityTurnYaw;
    }

    @Override
    public void setGravityTurnYaw(float gravityTurnYaw)
    {
        this.gravityTurnYaw = gravityTurnYaw;
    }

    @Override
    public int getSpaceRaceInviteTeamID()
    {
        return spaceRaceInviteTeamID;
    }

    @Override
    public void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID)
    {
        this.spaceRaceInviteTeamID = spaceRaceInviteTeamID;
    }

    @Override
    public boolean isLastZoomed()
    {
        return lastZoomed;
    }

    @Override
    public void setLastZoomed(boolean lastZoomed)
    {
        this.lastZoomed = lastZoomed;
    }

    @Override
    public int getBuildFlags()
    {
        return buildFlags;
    }

    @Override
    public void setBuildFlags(int buildFlags)
    {
        this.buildFlags = buildFlags;
    }

    @Override
    public boolean isSsOnGroundLast()
    {
        return ssOnGroundLast;
    }

    @Override
    public void setSsOnGroundLast(boolean ssOnGroundLast)
    {
        this.ssOnGroundLast = ssOnGroundLast;
    }

    @Override
    public FreefallHandler getFreefallHandler()
    {
        return freefallHandler;
    }

    @Override
    public void setFreefallHandler(FreefallHandler freefallHandler)
    {
        this.freefallHandler = freefallHandler;
    }

    @Override
    public ArrayList<ISchematicPage> getUnlockedSchematics()
    {
        return unlockedSchematics;
    }

    @Override
    public void setUnlockedSchematics(ArrayList<ISchematicPage> unlockedSchematics)
    {
        this.unlockedSchematics = unlockedSchematics;
    }

    @Override
    public int getMaxLandingticks()
    {
        return MAX_LANDINGTICKS;
    }

    @Override
    public float[] getLandingYOffset()
    {
        return landingYOffset;
    }

    @Override
    public void setLandingYOffset(float[] landingYOffset)
    {
        this.landingYOffset = landingYOffset;
    }

    @Override
    public void setDungeonDirection(float dir)
    {
        this.dungeonDirection = dir;
    }

    @Override
    public float getDungeonDirection()
    {
        return this.dungeonDirection;
    }

    @Override
    public boolean getPlatformControlled()
    {
        return this.platformControlled;
    }
    
    @Override
    public void startPlatformAscent(TileEntityPlatform noCollide, TileEntityPlatform moving, double target)
    {
        this.platformControlled = true;
        this.platformTarget = noCollide;
        this.platformMoving = moving;
        noCollide.markNoCollide(0, true);
        this.platformTargetY = target;
        this.platformVelocityCurrent = 0D;
        this.platformVelocityTarget = 0D;
        this.platformPacketSent = false;
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_NOCLIP_PLAYER, GCCoreUtil.getDimensionID(noCollide), new Object[] { true }));
    }

    @Override
    public void finishPlatformAscent()
    {
    }

    @Override
    public double getPlatformVelocity(double posY)
    {
        double delta = this.platformTargetY - posY + 0.03D;
        if (Math.abs(delta) < 0.04D)
        {
            this.platformVelocityCurrent = 0D;
            this.platformVelocityTarget = 0D;
            this.platformTarget.markNoCollide(0, false);
            this.platformControlled = false;
            this.platformTarget.stopMoving();
            this.platformMoving.stopMoving();
            if (!this.platformPacketSent)
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_NOCLIP_PLAYER, GCCoreUtil.getDimensionID(this.platformTarget), new Object[] { false }));
            return 0D;
        }
        else
        {
            if (delta > 0D)
            {
                this.platformVelocityTarget = (delta < 1.0D + 8 * this.platformVelocityCurrent * this.platformVelocityCurrent) ? 0.08D : 0.45D;
                if (delta < 0.6D && !this.platformPacketSent)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_NOCLIP_PLAYER, GCCoreUtil.getDimensionID(this.platformTarget), new Object[] { false }));
                    this.platformPacketSent = true;
                }
            }
            else if (delta < 0D)
            {
                this.platformVelocityTarget = (delta > -1.0D - 8 * this.platformVelocityCurrent * this.platformVelocityCurrent) ? -0.08D : -0.45D;
                if (delta > -1.0D && !this.platformPacketSent)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_NOCLIP_PLAYER, GCCoreUtil.getDimensionID(this.platformTarget), new Object[] { false }));
                    this.platformPacketSent = true;
                }
            }
            if (this.platformVelocityCurrent < this.platformVelocityTarget)
            {
                this.platformVelocityCurrent += 0.036D;
                if (this.platformVelocityCurrent > this.platformVelocityTarget)
                {
                    this.platformVelocityCurrent = this.platformVelocityTarget;
                }
            }
            else if (this.platformVelocityCurrent > this.platformVelocityTarget)
            {
                this.platformVelocityCurrent -= 0.036D;
                if (this.platformVelocityCurrent < this.platformVelocityTarget)
                {
                    this.platformVelocityCurrent = this.platformVelocityTarget;
                }
            }
        }
        return this.platformVelocityCurrent;
    }
}
