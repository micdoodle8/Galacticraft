package micdoodle8.mods.galacticraft.api.client;

public interface IGameScreen
{
    /**
     * Draw a screen in the XZ plane with y == 0.
     * Must fit in the coordinate range (0, 0, 0) - (scaleX, 0, scaleZ)
     * 
     * Register your IGameScreen by calling DrawGameScreen.registerScreen();
     *
     * @param type  Type of screen to draw, as registered
     * @param ticks  The worldtime in ticks (including partial ticks)
     * @param scaleX  Dimension of the screen in X direction
     * @param scaleZ  Dimension of the screen in Z direction
     * 
     * NOTE 1: It is not necessary to enclose your code with glPushMatrix and
     * glPopMatrix, as this will be done by the calling method.
     * You can also change the lighting in your render as the calling
     * method will finish by calling RenderHelper.enableStandardItemLighting(). 

     * NOTE 2: if disabling GL11.GL_TEXTURE_2D in your code it is very
     * IMPORTANT to re-enable it again.
     * 
     */
	public void render(int type, float ticks, float scaleX, float scaleZ);
}
