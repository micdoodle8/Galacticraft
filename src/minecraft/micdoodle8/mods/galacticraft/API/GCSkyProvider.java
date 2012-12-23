package micdoodle8.mods.galacticraft.API;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.IRenderHandler;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

/**
 * SEE GITHUB REPO FOR EXAMPLE USAGE!
 */
public abstract class GCSkyProvider extends IRenderHandler
{
	/**
	 * @return The X rotation for this planet, for example, the sun's rotation would be 
	 * 
	 * public Float[] getXRotation(float partialTicks, WorldClient world, Minecraft mc) 
	 * {
	 *	   return new Float[] {-90.0F + (world.getCelestialAngle(partialTicks) * 360.0F))};
	 * }
	 */
    public abstract Float[] getXRotation(float partialTicks, WorldClient world, Minecraft mc);

	/**
	 * @return The Y rotation for this planet
	 */
    public abstract Float[] getYRotation(float partialTicks, WorldClient world, Minecraft mc);

	/**
	 * @return The Z rotation for this planet
	 */
    public abstract Float[] getZRotation(float partialTicks, WorldClient world, Minecraft mc);
    
    /**
     * @return The size of planet. Start with 10-50 to test.
     */
    public abstract Float[] getSizes();
    
    /**
     * @return The sprite location that your wish to render (location of planet texture).
     */
    public abstract String[] getSpritesForRender();

    @SideOnly(Side.CLIENT)
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
    	for (int i = 0; i < this.getSpritesForRender().length; i++)
    	{
    		if (this.getSpritesForRender()[i] != null && this.getXRotation(partialTicks, world, mc)[i] != null && this.getYRotation(partialTicks, world, mc)[i] != null && this.getZRotation(partialTicks, world, mc)[i] != null && this.getSizes()[i] != null && this.getSpritesForRender()[i] != null)
    		{
        		final float rotateX = this.getXRotation(partialTicks, world, mc)[i];
        		final float rotateY = this.getYRotation(partialTicks, world, mc)[i];
        		final float rotateZ = this.getZRotation(partialTicks, world, mc)[i];
        		final float size = this.getSizes()[i];
        		
                final Tessellator var23 = Tessellator.instance;
                
                GL11.glPushMatrix();
                
                GL11.glRotatef(rotateX, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(rotateY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(rotateZ, 0.0F, 0.0F, 1.0F);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(this.getSpritesForRender()[i]));
                var23.startDrawingQuads();
                var23.addVertexWithUV(-size, 150.0D, -size, 0.0D, 0.0D);
                var23.addVertexWithUV(size, 150.0D, -size, 1.0D, 0.0D);
                var23.addVertexWithUV(size, 150.0D, size, 1.0D, 1.0D);
                var23.addVertexWithUV(-size, 150.0D, size, 0.0D, 1.0D);
                var23.draw();
                
                GL11.glPopMatrix();
    		}
    	}
    }
}
