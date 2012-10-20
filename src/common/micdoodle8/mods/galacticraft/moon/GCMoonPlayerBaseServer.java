package micdoodle8.mods.galacticraft.moon;

import net.minecraft.src.MathHelper;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import cpw.mods.fml.common.FMLLog;

public class GCMoonPlayerBaseServer extends ServerPlayerBase
{
	public GCMoonPlayerBaseServer(ServerPlayerAPI var1) 
	{
		super(var1);
	}
	
	@Override
    public void onUpdate()
    {
		super.onUpdate();
		
		if (this.player.worldObj != null && this.player.worldObj.provider instanceof GCMoonWorldProvider)
		{
//			FMLLog.info("d" + this.player.worldObj.getBlockId(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) + " " + GCMoonBlocks.moonGrass.blockID);
			if (this.player.worldObj.getBlockId(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) == GCMoonBlocks.moonGrass.blockID)
			{
				if (this.player.worldObj.getBlockMetadata(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) != 1)
				{
					this.player.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), 1);
				}
			}
		}
    }
}
