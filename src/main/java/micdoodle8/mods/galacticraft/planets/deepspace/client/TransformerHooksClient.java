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
            int cz = pos.getZ() >> 4;
            int org = MathHelper.floor_double(playerZ) >> 4;
            int h = (pos.getY() - heightBaseline);
            if (h < 0) h -= 15;
            h = (h / 16) * 16;
            adjust(org, org - cz, h, 0F, 0F, 0F, 0F);
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
            int cz = tile.getPos().getZ() >> 4;
            int org = MathHelper.floor_double(pZ) >> 4;
            int h = (tile.getPos().getY() - heightBaseline);
            int zz = tile.getPos().getZ() & 0x0f;
            adjust(org, org - cz, h, zz, (float) dX, (float) dY, (float) dZ);
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
            int cz = MathHelper.floor_double(posZ) >> 4;
            int org = MathHelper.floor_double(pZ) >> 4;
            float h = (float) posY - heightBaseline;
            double zz = posZ % 16D;
            if (zz < 0D) zz += 16D;
            adjust(org, org - cz, h, zz, (float) dX, (float) dY, (float) dZ);
        }
    }

    public static void preSelectionBox(World world, BlockPos pos, double playerX, double playerY, double playerZ)
    {
        GL11.glPushMatrix();
        if (world.provider instanceof WorldProviderDeepSpace)
        {
            int cZ = pos.getZ() >> 4;
            int org = MathHelper.floor_double(playerZ) >> 4;
            int offsetZ = org - cZ;
            if (offsetZ != 0)
            {
                float dY = pos.getY() - (float) (playerY);
                float dZ = pos.getZ() - (float) (playerZ);
                int h = (pos.getY() - heightBaseline);
                int zz = pos.getZ() & 0x0f;
                adjust(org, offsetZ, h, zz, 0F, dY, dZ);
            }
        }
    }

    private static void adjust(int pz, int offsetZ, float h, double zz, float dX, float dY, float dZ)
    {
        float theta = TransformerHooksClient.theta;
        float thetaAdjust = TransformerHooksClient.phi;

        // Bring the chunk / entity to the origin with offset because it will be rotated around pivot (0, ddY, 0)
        GL11.glTranslatef(dX, -ddY + dY, offsetZ * 16F + 8F + dZ);

        // To make a joined up circle, the angle needs to be slightly different (thetaAdjust) for some chunks - org is the offset of that special chunk from origin
        int org = pz % 6;
        if (org < 0) org += 6;
        offsetZ -= org;
        theta *= offsetZ;

        // (#1) Reverse the effect of thetaAdjust and org on the offsetZ == 0 chunk (where the player is) - and apply the same to all chunks
        int offsetAdjust = (org + 5) / 6;
        offsetAdjust += offsetAdjust - (org % 6 == 0 ? 0 : 1);
        if (org > 0)
        {
            float ar = MathHelper.sqrt_float(a * a + b * b);
            float deltaTheta = (TransformerHooksClient.theta * org + thetaAdjust) / Constants.RADIANS_TO_DEGREES - (float) MathHelper.atan2(a, b);
            GL11.glTranslatef(0F, ar * MathHelper.sin(deltaTheta), -ar * MathHelper.cos(deltaTheta) );
            GL11.glRotatef(TransformerHooksClient.theta * org + thetaAdjust, 1F, 0F, 0F);
        }

        // Rotate the chunk and move it around the circle, with adjustments for thetaAdjust
        if (offsetZ > 0)
        {
            thetaAdjust *= getOffsetAdjust(-1, offsetZ);
            GL11.glRotatef(theta + thetaAdjust, 1F, 0F, 0F);
        }
        else if (offsetZ < 0)
        {
            thetaAdjust *= -getOffsetAdjust(1, -offsetZ);
            GL11.glRotatef(-theta - thetaAdjust, -1F, 0F, 0F);
        }
        else
        {
            thetaAdjust = 0F;
        }

        // Finally, stabilise the offsetZ == 0 chunk by undoing the reversal at #1 above (and again apply the same to all chunks)
        GL11.glRotatef(TransformerHooksClient.phi * (offsetAdjust - (org > 0 ? 1 : 0)), 1F, 0F, 0F);

        // Set up theta correctly for the intra-chunk adjustments for h and zz below 
        theta += thetaAdjust + TransformerHooksClient.theta * org + TransformerHooksClient.phi * offsetAdjust;

        // Intra-chunk adjustments because the chunk is now being rendered at an angle
        float y = 0F;
        float z = 0F;
        if (h != 0F)
        {
            y = h * (1F - MathHelper.cos(theta  / Constants.RADIANS_TO_DEGREES));
            z = h * MathHelper.sin(theta / Constants.RADIANS_TO_DEGREES);
        }
        if (zz > 0D)
        {
            y -= zz * MathHelper.sin(theta / Constants.RADIANS_TO_DEGREES);
            z += zz * (1F - MathHelper.cos(theta / Constants.RADIANS_TO_DEGREES));
        }

        // Restore the chunk to its true position with intra-chunk offset (y, z)
        // and also cancel the effect of the (dx, dy, dz) translation which the vanilla chunk / entity renderer will now make
        GL11.glTranslatef(-dX, ddY + y - dY, z - dZ - 8F);
    }

    private static int getOffsetAdjust(int cz, int offsetZ)
    {
        int offsetAdjust = (offsetZ + 5) / 6;
        offsetAdjust += offsetAdjust - (offsetZ % 6 == 0 ? 0 : 1);
        switch (offsetAdjust % 8)
        {
        case 1:
            GL11.glTranslatef(0F, a, b * cz);
            break;
        case 2:
            GL11.glTranslatef(0F, c, c * cz);
            break;
        case 3:
            GL11.glTranslatef(0F, c + c, b * cz);
            break;
        case 4:
            GL11.glTranslatef(0F, c + c + a, 0F);
            break;
        case 5:
            GL11.glTranslatef(0F, c + c, -b * cz);
            break;
        case 6:
            GL11.glTranslatef(0F, c, -c * cz);
            break;
        case 7:
            GL11.glTranslatef(0F, a, -b * cz);
            break;
        default:
        }
        return offsetAdjust;
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
