package micdoodle8.mods.galacticraft.core.items;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDungeonFinder extends ItemBase
{
   public ItemDungeonFinder(Item.Properties builder) {
      super(builder);
      this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID_CORE, "angle"), new IItemPropertyGetter() {
         @OnlyIn(Dist.CLIENT)
         private double rotation;
         @OnlyIn(Dist.CLIENT)
         private double rota;
         @OnlyIn(Dist.CLIENT)
         private long lastUpdateTick;

         @OnlyIn(Dist.CLIENT)
         public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity livingEntity) {
            if (livingEntity == null && !stack.isOnItemFrame()) {
               return 0.0F;
            } else {
               boolean flag = livingEntity != null;
               Entity entity = flag ? livingEntity : stack.getItemFrame();
               if (world == null) {
                  world = entity.world;
               }

               double target;
               if (livingEntity instanceof PlayerEntity) {
                  double d1 = flag ? (double)entity.rotationYaw : this.getFrameRotation((ItemFrameEntity)entity);
                  d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);
//                  double d2 = this.getSpawnToAngle(world, entity) / (double)((float)Math.PI * 2F);
                  double direction = MathHelper.positiveModulo(GCPlayerStatsClient.get(livingEntity).getDungeonDirection() / 360.0D, 1.0);
                  target = 0.5D - (d1 - 0.25D - direction);
               } else {
                  target = Math.random();
               }

               if (flag) {
                  target = this.wobble(world, target);
               }

               return MathHelper.positiveModulo((float)target, 1.0F);
            }
         }

         @OnlyIn(Dist.CLIENT)
         private double wobble(World worldIn, double angle) {
            if (worldIn.getGameTime() != this.lastUpdateTick) {
               this.lastUpdateTick = worldIn.getGameTime();
               double d0 = angle - this.rotation;
               d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
               this.rota += d0 * 0.1D;
               this.rota *= 0.8D;
               this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
            }

            return this.rotation;
         }

         @OnlyIn(Dist.CLIENT)
         private double getFrameRotation(ItemFrameEntity p_185094_1_) {
            return MathHelper.wrapDegrees(180 + p_185094_1_.getHorizontalFacing().getHorizontalIndex() * 90);
         }

         @OnlyIn(Dist.CLIENT)
         private double getSpawnToAngle(IWorld world, Entity entity) {
            BlockPos blockpos = world.getSpawnPoint();
            return Math.atan2((double)blockpos.getZ() - entity.getPosZ(), (double)blockpos.getX() - entity.getPosX());
         }
      });
   }
}
