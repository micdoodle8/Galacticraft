package micdoodle8.mods.galacticraft.planets.asteroids.client.render;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamOutput;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class NetworkRenderer
{
    public static void renderNetworks(World world, float partialTicks)
    {
        List<TileEntityBeamOutput> nodes = new ArrayList<TileEntityBeamOutput>();

        for (Object o : new ArrayList<TileEntity>(world.loadedTileEntityList))
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

        Tessellator tess = Tessellator.getInstance();
        EntityPlayerSP player = FMLClientHandler.instance().getClient().player;
        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        GL11.glDisable(GL11.GL_TEXTURE_2D);

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

            float posX = (float) (tileEntity.getPos().getX() - interpPosX);
            float posY = (float) (tileEntity.getPos().getY() - interpPosY);
            float posZ = (float) (tileEntity.getPos().getZ() - interpPosZ);
            GL11.glTranslatef(posX, posY, posZ);

            GL11.glTranslatef(outputPoint.floatX() - tileEntity.getPos().getX(), outputPoint.floatY() - tileEntity.getPos().getY(), outputPoint.floatZ() - tileEntity.getPos().getZ());
            GL11.glRotatef(tileEntity.yaw + 180, 0, 1, 0);
            GL11.glRotatef(-tileEntity.pitch, 1, 0, 0);
            GL11.glRotatef(tileEntity.ticks * 10, 0, 0, 1);

            tess.getBuffer().begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

            for (EnumFacing dir : EnumFacing.VALUES)
            {
                tess.getBuffer().pos(dir.getFrontOffsetX() / 40.0F, dir.getFrontOffsetY() / 40.0F, dir.getFrontOffsetZ() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
                tess.getBuffer().pos(dir.getFrontOffsetX() / 40.0F, dir.getFrontOffsetY() / 40.0F, directionLength + dir.getFrontOffsetZ() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
            }

            tess.draw();

            GL11.glPopMatrix();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(1, 1, 1, 1);
    }
}
