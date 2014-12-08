package micdoodle8.mods.galacticraft.core.entities.player;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class GCPlayerStatsClient implements IExtendedEntityProperties
{
    public static final String GC_PLAYER_PROP = "GCPlayerStatsClient";

    public WeakReference<EntityPlayerSP> player;

    public boolean usingParachute;
    public boolean lastUsingParachute;
    public boolean usingAdvancedGoggles;
    public int thermalLevel;
    public int thirdPersonView = 0;
    public long tick;
    public boolean oxygenSetupValid = true;
    AxisAlignedBB boundingBoxBefore;
    public boolean lastOnGround;

    public double distanceSinceLastStep;
    public int lastStep;
    public boolean inFreefall;
    public boolean inFreefallFirstCheck;
    public boolean lastRidingCameraZoomEntity;

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

    public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();

    public GCPlayerStatsClient(EntityPlayerSP player)
    {
        this.player = new WeakReference<EntityPlayerSP>(player);
    }

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

        switch (this.gdir.intValue)
        {
        case 1:
            switch (newGravity.intValue)
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
            switch (newGravity.intValue)
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
            switch (newGravity.intValue)
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
            switch (newGravity.intValue)
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
            switch (newGravity.intValue)
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
            switch (newGravity.intValue)
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

    public void setParachute(boolean tf)
    {
        this.usingParachute = tf;

        if (!tf)
        {
            this.lastUsingParachute = false;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt)
    {
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt)
    {
    }

    @Override
    public void init(Entity entity, World world)
    {
    }

    public static void register(EntityPlayerSP player)
    {
        player.registerExtendedProperties(GCPlayerStatsClient.GC_PLAYER_PROP, new GCPlayerStatsClient(player));
    }

    public static GCPlayerStatsClient get(EntityPlayerSP player)
    {
        return (GCPlayerStatsClient) player.getExtendedProperties(GCPlayerStatsClient.GC_PLAYER_PROP);
    }
}
