package api.player.client;

public interface IClientPlayerAPI extends IClientPlayer
{
	ClientPlayerAPI getClientPlayerAPI();

	net.minecraft.client.entity.EntityPlayerSP getEntityPlayerSP();
}
