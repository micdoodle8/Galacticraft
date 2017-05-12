package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GCEntityOtherPlayerMP extends EntityOtherPlayerMP
{
    private boolean checkedCape = false;
    private ResourceLocation galacticraftCape = null;

    public GCEntityOtherPlayerMP(World par1World, GameProfile profile)
    {
        super(par1World, profile);
    }

    @Override
    public ResourceLocation getLocationCape()
    {
        ResourceLocation vanillaCape = super.getLocationCape();

        if (!this.checkedCape)
        {
            NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
            this.galacticraftCape = ClientProxyCore.capeMap.get(networkplayerinfo.getGameProfile().getName());
            this.checkedCape = true;
        }

        if ((ConfigManagerCore.overrideCapes || vanillaCape == null) && galacticraftCape != null)
        {
            return galacticraftCape;
        }

        return vanillaCape;
    }
}
