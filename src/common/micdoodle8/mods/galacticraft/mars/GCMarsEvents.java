package micdoodle8.mods.galacticraft.mars;

import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBucket;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import cpw.mods.fml.common.FMLLog;

public class GCMarsEvents 
{
	@ForgeSubscribe
	public void fillBucketEvent(FillBucketEvent event)
	{
        if (event.target.typeOfHit == EnumMovingObjectType.TILE)
        {
            int x = event.target.blockX;
            int y = event.target.blockY;
            int z = event.target.blockZ;
            
            if (event.current.getItem() instanceof ItemBucket)
            {
                ItemBucket bucket = (ItemBucket) event.current.getItem();

                if (!event.world.canMineBlock(event.entityPlayer, x, y, z))
                {	
                	event.setResult(Result.ALLOW);
                }

                if (bucket.shiftedIndex == Item.bucketEmpty.shiftedIndex)
                {
                    if (event.world.getBlockMaterial(x, y, z) == GCMarsBlocks.bacterialSludge && event.world.getBlockMetadata(x, y, z) == 0)
                    {
                    	event.world.setBlockWithNotify(x, y, z, 0);

                    	event.world.setBlockWithNotify(MathHelper.floor_double(event.entityPlayer.posX), MathHelper.floor_double(event.entityPlayer.posY + event.entityPlayer.getEyeHeight()), MathHelper.floor_double(event.entityPlayer.posZ), GCMarsBlocks.bacterialSludgeMoving.blockID);
                		
                        event.setResult(Result.DENY);
                        
                        return;
                    }
                }
            }
        }
        
		event.setResult(Result.ALLOW);
	}
}
