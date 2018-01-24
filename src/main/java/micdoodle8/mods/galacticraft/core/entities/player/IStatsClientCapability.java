package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;

public interface IStatsClientCapability
{
    void setGravity(EnumGravity newGravity);

    boolean isUsingParachute();

    void setUsingParachute(boolean usingParachute);

    boolean isLastUsingParachute();

    void setLastUsingParachute(boolean lastUsingParachute);

    boolean isUsingAdvancedGoggles();

    void setUsingAdvancedGoggles(boolean usingAdvancedGoggles);

    int getThermalLevel();

    void setThermalLevel(int thermalLevel);

    boolean isThermalLevelNormalising();

    void setThermalLevelNormalising(boolean thermalLevelNormalising);

    int getThirdPersonView();

    void setThirdPersonView(int thirdPersonView);

    long getTick();

    void setTick(long tick);

    boolean isOxygenSetupValid();

    void setOxygenSetupValid(boolean oxygenSetupValid);

    AxisAlignedBB getBoundingBoxBefore();

    void setBoundingBoxBefore(AxisAlignedBB boundingBoxBefore);

    boolean isLastOnGround();

    void setLastOnGround(boolean lastOnGround);

    double getDistanceSinceLastStep();

    void setDistanceSinceLastStep(double distanceSinceLastStep);

    int getLastStep();

    void setLastStep(int lastStep);

    boolean isInFreefall();

    void setInFreefall(boolean inFreefall);

    boolean isInFreefallLast();

    void setInFreefallLast(boolean inFreefallLast);

    boolean isInFreefallFirstCheck();

    void setInFreefallFirstCheck(boolean inFreefallFirstCheck);

    double getDownMotionLast();

    void setDownMotionLast(double downMotionLast);

    boolean isLastRidingCameraZoomEntity();

    void setLastRidingCameraZoomEntity(boolean lastRidingCameraZoomEntity);

    int getLandingTicks();

    void setLandingTicks(int landingTicks);

    EnumGravity getGdir();

    void setGdir(EnumGravity gdir);

    float getGravityTurnRate();

    void setGravityTurnRate(float gravityTurnRate);

    float getGravityTurnRatePrev();

    void setGravityTurnRatePrev(float gravityTurnRatePrev);

    float getGravityTurnVecX();

    void setGravityTurnVecX(float gravityTurnVecX);

    float getGravityTurnVecY();

    void setGravityTurnVecY(float gravityTurnVecY);

    float getGravityTurnVecZ();

    void setGravityTurnVecZ(float gravityTurnVecZ);

    float getGravityTurnYaw();

    void setGravityTurnYaw(float gravityTurnYaw);

    int getSpaceRaceInviteTeamID();

    void setSpaceRaceInviteTeamID(int spaceRaceInviteTeamID);

    boolean isLastZoomed();

    void setLastZoomed(boolean lastZoomed);

    int getBuildFlags();

    void setBuildFlags(int buildFlags);

    boolean isSsOnGroundLast();

    void setSsOnGroundLast(boolean ssOnGroundLast);

    FreefallHandler getFreefallHandler();

    void setFreefallHandler(FreefallHandler freefallHandler);

    ArrayList<ISchematicPage> getUnlockedSchematics();

    void setUnlockedSchematics(ArrayList<ISchematicPage> unlockedSchematics);

    int getMaxLandingticks();

    float[] getLandingYOffset();

    void setLandingYOffset(float[] landingYOffset);
}
