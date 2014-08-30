package api.player.render;

public interface IRenderPlayerAPI extends IRenderPlayer
{
	RenderPlayerAPI getRenderPlayerAPI();

	net.minecraft.client.renderer.entity.RenderPlayer getRenderPlayer();
}
