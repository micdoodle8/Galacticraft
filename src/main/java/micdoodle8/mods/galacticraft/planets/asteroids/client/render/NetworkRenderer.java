package micdoodle8.mods.galacticraft.planets.asteroids.client.render;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamOutput;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class NetworkRenderer
{
    public static void renderNetworks(World world, float partialTicks)
    {
        List<TileEntityBeamOutput> nodes = new ArrayList<TileEntityBeamOutput>();

        for (Object o : world.loadedTileEntityList)
        {
            if (o instanceof TileEntityBeamOutput)
            {
                nodes.add((TileEntityBeamOutput) o);
            }
        }

        if (nodes.isEmpty())
        {
            return;
        }

        Tessellator tess = Tessellator.instance;
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        for (TileEntityBeamOutput tileEntity : nodes)
        {
            if (tileEntity.getTarget() == null)
            {
                continue;
            }

            GL11.glPushMatrix();

            Vector3 outputPoint = tileEntity.getOutputPoint(true);
            Vector3 targetInputPoint = tileEntity.getTarget().getInputPoint();

            Vector3 direction = Vector3.subtract(outputPoint, targetInputPoint);
            float directionLength = (float) direction.getMagnitude();

            float posX = (float) (tileEntity.xCoord - interpPosX);
            float posY = (float) (tileEntity.yCoord - interpPosY);
            float posZ = (float) (tileEntity.zCoord - interpPosZ);
            GL11.glTranslatef(posX, posY, posZ);

            GL11.glTranslatef(outputPoint.floatX() - tileEntity.xCoord, outputPoint.floatY() - tileEntity.yCoord, outputPoint.floatZ() - tileEntity.zCoord);
            GL11.glRotatef(tileEntity.yaw + 180, 0, 1, 0);
            GL11.glRotatef(-tileEntity.pitch, 1, 0, 0);
            GL11.glRotatef(tileEntity.ticks * 10, 0, 0, 1);

            GL11.glColor4f(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F);
            tess.startDrawing(GL11.GL_LINES);

            for (ForgeDirection dir : ForgeDirection.values())
            {
                tess.addVertex(dir.offsetX / 40.0F, dir.offsetY / 40.0F, dir.offsetZ / 40.0F);
                tess.addVertex(dir.offsetX / 40.0F, dir.offsetY / 40.0F, directionLength + dir.offsetZ / 40.0F);
            }

            tess.draw();

            GL11.glPopMatrix();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glColor4f(1, 1, 1, 1);
    }
}
