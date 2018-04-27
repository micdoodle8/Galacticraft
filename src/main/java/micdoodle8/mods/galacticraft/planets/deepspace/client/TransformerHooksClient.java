package micdoodle8.mods.galacticraft.planets.deepspace.client;

import org.lwjgl.opengl.GL11;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.deepspace.dimension.WorldProviderDeepSpace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class TransformerHooksClient
{
    private final static int heightBaseline = 64;
    private static Minecraft mc = FMLClientHandler.instance().getClient();
    private static float ddY = heightBaseline - 128F;
    private static float theta = (float) Math.atan2(8D, -ddY) * Constants.RADIANS_TO_DEGREES * 2F;
    private static float phi = 45 - theta * 3; 
    private static float cosPhi = (float) Math.cos((theta + phi) / Constants.RADIANS_TO_DEGREES);
    private static float sinPhi = (float) Math.sin((theta + phi) / Constants.RADIANS_TO_DEGREES);
    private static float a = ddY * (1 - cosPhi) + 8F * sinPhi;
    private static float b = ddY * (float) Math.sin(phi / Constants.RADIANS_TO_DEGREES);
    private static float c = a + b;
    
    public static void preRenderChunk(RenderChunk chunk, BlockPos pos, double playerX, double playerY, double playerZ)
    {
        if (chunk.getWorld().provider instanceof WorldProviderDeepSpace)
        {
            int offsetZ = (MathHelper.floor_double(playerZ) - pos.getZ()) >> 4;
            int h = (pos.getY() - heightBaseline);
            if (h < 0) h -= 15;
            h = (h / 16) * 16;
            adjust(offsetZ, h, 0F, 0F, 0F, 0F, false);
        }
    }

    public static void preRenderTE(TileEntity tile, double dX, double dY, double dZ)
    {
        GL11.glPushMatrix();
        if (tile.getWorld() != null && tile.getWorld().provider instanceof WorldProviderDeepSpace)
        {
            double pZ = tile.getPos().getZ() - dZ;
            if (pZ == 0D)
            {
                //TileEntity renders in inventory or other GUIs
                return;
            }
            int offsetZ = ((MathHelper.floor_double(pZ)) >> 4) - (tile.getPos().getZ() >> 4);
            int h = (tile.getPos().getY() - heightBaseline);
            int zz = tile.getPos().getZ() & 0x0f;
            adjust(offsetZ, h, zz, (float) dX, (float) dY, (float) dZ, false);
        }
    }

    public static void preRenderEntity(Entity entity, double dX, double dY, double dZ, float partialTicks)
    {
        GL11.glPushMatrix();
        if (entity.getEntityWorld() != null && entity.getEntityWorld().provider instanceof WorldProviderDeepSpace)
        {
            if (dZ == 0D)
            {
                //TileEntity renders in inventory or other GUIs
                return;
            }
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            double pZ = posZ - dZ;
            int offsetZ = ((MathHelper.floor_double(pZ)) >> 4) - (MathHelper.floor_double(posZ) >> 4);
            float h = (float) posY - heightBaseline;
            double zz = posZ % 16D;
            if (zz < 0D) zz += 16D;
            adjust(offsetZ, h, zz, (float) dX, (float) dY, (float) dZ, false);
        }
    }

    public static void preSelectionBox(World world, BlockPos pos, double playerX, double playerY, double playerZ)
    {
        GL11.glPushMatrix();
        if (world.provider instanceof WorldProviderDeepSpace)
        {
            int offsetZ = (MathHelper.floor_double(playerZ) >> 4) - (pos.getZ() >> 4);
            if (offsetZ != 0)
            {
                float dY = pos.getY() - (float) (playerY);
                float dZ = pos.getZ() - (float) (playerZ);
                int h = (pos.getY() - heightBaseline);
                int zz = pos.getZ() & 0x0f;
                adjust(offsetZ, h, zz, 0F, dY, dZ, true);
            }
        }
    }

    private static void adjust(int offsetZ, float h, double zz, float dX, float dY, float dZ, boolean selBox)
    {
        float theta = TransformerHooksClient.theta;
        float thetaAdjust = TransformerHooksClient.phi;
        if (offsetZ > 0)
        {
            int offsetAdjust = (offsetZ + 5) / 6;
            offsetAdjust += offsetAdjust - (offsetZ % 6 == 0 ? 0 : 1);
            switch (offsetAdjust % 8)
            {
            case 1:
                GL11.glTranslatef(0F, a, -b);
                break;
            case 2:
                GL11.glTranslatef(0F, c, -c);
                break;
            case 3:
                GL11.glTranslatef(0F, c + c, -b);
                break;
            case 4:
                GL11.glTranslatef(0F, c + c + a, 0);
                break;
            case 5:
                GL11.glTranslatef(0F, c + c, b);
                break;
            case 6:
                GL11.glTranslatef(0F, c, c);
                break;
            case 7:
                GL11.glTranslatef(0F, a, b);
                break;
            default:
            }
            theta *= offsetZ;
            thetaAdjust *= offsetAdjust;
            GL11.glTranslatef(dX, -ddY + dY, offsetZ * 16F + 8F + dZ);
            GL11.glRotatef(theta + thetaAdjust, 1, 0, 0);
            if (selBox)
            {
                GL11.glTranslatef(0F, ddY - dY, -8F - dZ);
            }
            else
            {
                GL11.glTranslatef(0F, ddY, -8F);
            }
        }
        else if (offsetZ < 0)
        {
            int offsetAdjust = (-offsetZ + 5) / 6;
            offsetAdjust += offsetAdjust - (-offsetZ % 6 == 0 ? 0 : 1);
            switch (offsetAdjust % 8)
            {
            case 1:
                GL11.glTranslatef(0F, a, b);
                break;
            case 2:
                GL11.glTranslatef(0F, c, c);
                break;
            case 3:
                GL11.glTranslatef(0F, c + c, b);
                break;
            case 4:
                GL11.glTranslatef(0F, c + c + a, 0);
                break;
            case 5:
                GL11.glTranslatef(0F, c + c, -b);
                break;
            case 6:
                GL11.glTranslatef(0F, c, -c);
                break;
            case 7:
                GL11.glTranslatef(0F, a, -b);
                break;
            default:
            }
            thetaAdjust *= -offsetAdjust;
            theta *= offsetZ;
            GL11.glTranslatef(dX, -ddY + dY, offsetZ * 16F + 8F + dZ);
            GL11.glRotatef(-theta - thetaAdjust, -1, 0, 0);
            if (selBox)
            {
                GL11.glTranslatef(0F, ddY - dY, -8F - dZ);
            }
            else
            {
                GL11.glTranslatef(0F, ddY, -8F);
            }
        }
        else
        {
            theta = 0F;
            thetaAdjust = 0F;
            if (!selBox) GL11.glTranslatef(dX, dY, dZ);
        }
        float y = 0F;
        float z = 0F;
        if (h != 0F)
        {
            y = h * (1F - MathHelper.cos((theta + thetaAdjust) / Constants.RADIANS_TO_DEGREES));
            z = h * MathHelper.sin((theta + thetaAdjust) / Constants.RADIANS_TO_DEGREES);
        }
        if (zz > 0D)
        {
            y -= zz * MathHelper.sin((theta + thetaAdjust) / Constants.RADIANS_TO_DEGREES);
            z += zz * (1F - MathHelper.cos((theta + thetaAdjust) / Constants.RADIANS_TO_DEGREES));
        }
        if (selBox)
        {
            if (y != 0F)
            {
                GL11.glTranslatef(0F, y, z);
            }
        }
        else
        {
            GL11.glTranslatef(-dX, y - dY, z - dZ);
        }
    }

    public static AxisAlignedBB renderChunkAABB(AxisAlignedBB aabb, RenderChunk chunk)
    {
        if (chunk.getWorld().provider instanceof WorldProviderDeepSpace)
        {
            if (aabb.minX > -17D && aabb.minX < 17D) //TODO always three chunks width?
            {
                //Ensure it stays visible even if outside the "standard" frustum
                return new AxisAlignedBB(aabb.minX, 0D, aabb.minZ - 64D, aabb.maxX, 512D, aabb.maxZ + 64D);
            }
        }
        return aabb;
    }
}
