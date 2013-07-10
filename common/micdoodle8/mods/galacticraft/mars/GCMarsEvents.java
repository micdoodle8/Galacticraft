package micdoodle8.mods.galacticraft.mars;

public class GCMarsEvents
{
    // @ForgeSubscribe
    // public void fillBucketEvent(FillBucketEvent event)
    // {
    // if (event.target.typeOfHit == EnumMovingObjectType.TILE)
    // {
    // final int x = event.target.blockX;
    // final int y = event.target.blockY;
    // final int z = event.target.blockZ;
    //
    // if (event.current.getItem() instanceof ItemBucket)
    // {
    // final ItemBucket bucket = (ItemBucket) event.current.getItem();
    //
    // if (!event.world.canMineBlock(event.entityPlayer, x, y, z))
    // {
    // event.setResult(Result.ALLOW);
    // }
    //
    // if (bucket.itemID == Item.bucketEmpty.itemID)
    // {
    // if (event.world.getBlockMaterial(x, y, z) == GCMarsBlocks.bacterialSludge
    // && event.world.getBlockMetadata(x, y, z) == 0)
    // {
    // event.world.setBlockWithNotify(x, y, z, 0);
    //
    // event.world.setBlockWithNotify(MathHelper.floor_double(event.entityPlayer.posX),
    // MathHelper.floor_double(event.entityPlayer.posY +
    // event.entityPlayer.getEyeHeight()),
    // MathHelper.floor_double(event.entityPlayer.posZ),
    // GCMarsBlocks.bacterialSludgeMoving.blockID);
    //
    // event.setResult(Result.DENY);
    //
    // return;
    // }
    // else
    // {
    // event.setResult(Result.ALLOW);
    // return;
    // }
    // }
    // }
    // }
    //
    // event.setResult(Result.ALLOW);
    // }
}
