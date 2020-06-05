package micdoodle8.mods.galacticraft.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CloudRenderer extends IRenderHandler
{
    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(float partialTicks, ClientWorld world, Minecraft mc)
    {
        // Do nothing
    }
}
