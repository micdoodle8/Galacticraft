package micdoodle8.mods.galacticraft.planets.deepspace.client;

import org.lwjgl.opengl.GL11;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.FMLClientHandler;

public class TransformerHooksClient
{
    private final static int heightBaseline = 64;
    private final static float base = 11.25F;//6.92307692F;//7.12501635F;
    private static Minecraft mc = FMLClientHandler.instance().getClient();

    
    public static void preRenderChunk(BlockPos pos, double dX, double dY, double dZ)
    {
        int offsetZ = (MathHelper.floor_double(dZ) - pos.getZ()) >> 4;
        float ddY = heightBaseline - 192F;
        float theta = (float) MathHelper.atan2(8D, -ddY) * Constants.RADIANS_TO_DEGREES * 2F;
        theta *= offsetZ;
        if (theta > 0F)
        {
            GL11.glTranslatef(0F, -ddY, offsetZ * 16F + 8F);
            GL11.glRotatef(theta, 1, 0, 0);
            GL11.glTranslatef(0F, ddY, -8F);
        }
        else if (theta < 0F)
        {
            GL11.glTranslatef(0F, -ddY, offsetZ * 16F + 8F);
            GL11.glRotatef(-theta, -1, 0, 0);
            GL11.glTranslatef(0F, ddY, -8F);
        }
        int h = (pos.getY() - heightBaseline);
        if (h < 0) h -= 15;
        h = (h / 16) * 16;
        float y = 0F;
        float z = 0F;
        if (h != 0)
        {
            y = h * (1F - MathHelper.cos(theta / Constants.RADIANS_TO_DEGREES));
            z = h * MathHelper.sin(theta / Constants.RADIANS_TO_DEGREES);
        }
        if (y != 0F)
        {
            GL11.glTranslatef(0F, y, z);
        }
    }
    
    public static void preRenderTE(TileEntity tile, double dX, double dY, double dZ)
    {
        GL11.glPushMatrix();
        double pZ = tile.getPos().getZ() - dZ;
        if (pZ == 0D)
        {
            //TileEntity renders in inventory or other GUIs
            return;
        }
        GL11.glTranslatef((float) dX, (float) dY, (float) dZ);
        int offsetZ = ((MathHelper.floor_double(pZ)) >> 4) - (tile.getPos().getZ() >> 4);
        float ddY = heightBaseline - 192F;
        float theta = (float) MathHelper.atan2(8D, -ddY) * Constants.RADIANS_TO_DEGREES * 2F;
        theta *= offsetZ;
        if (theta > 0F)
        {
            GL11.glTranslatef(0F, -ddY, offsetZ * 16F + 8F);
            GL11.glRotatef(theta, 1, 0, 0);
            GL11.glTranslatef(0F, ddY, -8F);
        }
        else if (theta < 0F)
        {
            GL11.glTranslatef(0F, -ddY, offsetZ * 16F + 8F);
            GL11.glRotatef(-theta, -1, 0, 0);
            GL11.glTranslatef(0F, ddY, -8F);
        }
        int h = (tile.getPos().getY() - heightBaseline);
        float y = 0F;
        float z = 0F;
        if (h != 0)
        {
            y = h * (1F - MathHelper.cos(theta / Constants.RADIANS_TO_DEGREES));
            z = h * MathHelper.sin(theta / Constants.RADIANS_TO_DEGREES);
        }
        int zz = tile.getPos().getZ() & 0x0f;
        if (zz > 0)
        {
            y -= zz * MathHelper.sin(theta / Constants.RADIANS_TO_DEGREES);
            z += zz * (1F - MathHelper.cos(theta / Constants.RADIANS_TO_DEGREES));
        }
        GL11.glTranslatef((float) -dX, y - (float) dY, z - (float)dZ);
    }

    public static void preRenderEntity(Entity entity, double dX, double dY, double dZ, float partialTicks)
    {
        GL11.glPushMatrix();
        if (dZ == 0D)
        {
            //TileEntity renders in inventory or other GUIs
            return;
        }
        GL11.glTranslatef((float) dX, (float) dY, (float) dZ);
        double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        double pZ = posZ - dZ;
        int offsetZ = ((MathHelper.floor_double(pZ)) >> 4) - (MathHelper.floor_double(posZ) >> 4);
        float ddY = heightBaseline - 192F;
        float theta = (float) MathHelper.atan2(8D, -ddY) * Constants.RADIANS_TO_DEGREES * 2F;
        theta *= offsetZ;
        if (theta > 0F)
        {
            GL11.glTranslatef(0F, -ddY, offsetZ * 16F + 8F);
            GL11.glRotatef(theta, 1, 0, 0);
            GL11.glTranslatef(0F, ddY, -8F);
        }
        else if (theta < 0F)
        {
            GL11.glTranslatef(0F, -ddY, offsetZ * 16F + 8F);
            GL11.glRotatef(-theta, -1, 0, 0);
            GL11.glTranslatef(0F, ddY, -8F);
        }
        float h = (float) posY - heightBaseline;
        float y = 0F;
        float z = 0F;
        if (h != 0)
        {
            y = h * (1F - MathHelper.cos(theta / Constants.RADIANS_TO_DEGREES));
            z = h * MathHelper.sin(theta / Constants.RADIANS_TO_DEGREES);
        }
        double zz = posZ % 16D;
        if (zz < 0D) zz += 16D;
        if (zz > 0D)
        {
            y -= zz * MathHelper.sin(theta / Constants.RADIANS_TO_DEGREES);
            z += zz * (1F - MathHelper.cos(theta / Constants.RADIANS_TO_DEGREES));
        }
        GL11.glTranslatef((float) -dX, y - (float) dY, z - (float)dZ);
    }
}
