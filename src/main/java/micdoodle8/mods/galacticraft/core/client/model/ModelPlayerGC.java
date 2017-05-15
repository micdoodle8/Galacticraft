package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ModelPlayerGC extends ModelPlayer
{
    public static final ResourceLocation oxygenMaskTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/oxygen.png");
    public static final ResourceLocation playerTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/player.png");

    public ModelPlayerGC(float var1, boolean smallArms)
    {
        super(var1, smallArms);

        if (smallArms)
        {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, var1);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, var1);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
        }
        else
        {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, var1);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, var1);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        }
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        ModelBipedGC.setRotationAngles(this, par1, par2, par3, par4, par5, par6, par7Entity);
    }

    public static PlayerGearData getGearData(EntityPlayer player)
    {
        PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getName());

        if (gearData == null)
        {
            String id = player.getGameProfile().getName();

            if (!ClientProxyCore.gearDataRequests.contains(id))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA, GCCoreUtil.getDimensionID(player.world), new Object[] { id }));
                ClientProxyCore.gearDataRequests.add(id);
            }
        }

        return gearData;
    }
}
