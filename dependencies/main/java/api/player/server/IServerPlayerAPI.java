package api.player.server;

public interface IServerPlayerAPI extends IServerPlayer
{
	ServerPlayerAPI getServerPlayerAPI();

	net.minecraft.entity.player.EntityPlayerMP getEntityPlayerMP();
}
