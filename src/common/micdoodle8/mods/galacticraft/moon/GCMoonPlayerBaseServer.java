package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.core.GCCoreEntityMeteor;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import cpw.mods.fml.common.FMLLog;

public class GCMoonPlayerBaseServer extends ServerPlayerBase
{
	public int lastStep;
	
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
			if (this.player.worldObj.getBlockId(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) == GCMoonBlocks.moonGrass.blockID)
			{
				if (this.player.worldObj.getBlockMetadata(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ)) == 0)
				{
					switch (this.lastStep)
					{
					case 1:
						this.player.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), 2);
						this.lastStep = 2;
						break;
					case 2:
						this.player.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), 1);
						this.lastStep = 1;
						break;
					default:
						this.player.worldObj.setBlockMetadataWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY - 1), MathHelper.floor_double(player.posZ), 1);
						this.lastStep = 1;
						break;
					}
				}
			}
		}
		
		if (player.worldObj.provider instanceof GCMoonWorldProvider)
		{
			if (player.worldObj.rand.nextInt(20) == 0)
			{
				int x, y, z;
				double motX, motZ;
				x = player.worldObj.rand.nextInt(20) - 10;
				y = player.worldObj.rand.nextInt(20) + 200;
				z = player.worldObj.rand.nextInt(20) - 10;
				motX = player.worldObj.rand.nextDouble() * 5;
				motZ = player.worldObj.rand.nextDouble() * 5;
				
				GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(player.worldObj, player.posX + x, y, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);
				player.worldObj.spawnEntityInWorld(meteor);
			}
			if (player.worldObj.rand.nextInt(100) == 0)
			{
				int x, y, z;
				double motX, motZ;
				x = player.worldObj.rand.nextInt(20) - 10;
				y = player.worldObj.rand.nextInt(20) + 200;
				z = player.worldObj.rand.nextInt(20) - 10;
				motX = player.worldObj.rand.nextDouble() * 5;
				motZ = player.worldObj.rand.nextDouble() * 5;
				
				GCCoreEntityMeteor meteor = new GCCoreEntityMeteor(player.worldObj, player.posX + x, y, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);
				player.worldObj.spawnEntityInWorld(meteor);
			}
		}
    }
}
