//package micdoodle8.mods.galacticraft.core.entities;
//
//import net.minecraft.entity.ai.goal.LookAtGoal;
//import net.minecraft.entity.player.PlayerEntity;
//
//public class EntityAILookAtTradePlayerGC extends LookAtGoal
//{
//    private final EntityAlienVillager theMerchant;
//
//    public EntityAILookAtTradePlayerGC(EntityAlienVillager theMerchantIn)
//    {
//        super(theMerchantIn, PlayerEntity.class, 8.0F);
//        this.theMerchant = theMerchantIn;
//    }
//
//    /**
//     * Returns whether the EntityAIBase should begin execution.
//     */
//    @Override
//    public boolean shouldExecute()
//    {
//        if (this.theMerchant.isTrading())
//        {
//            this.closestEntity = this.theMerchant.getCustomer();
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }
////}