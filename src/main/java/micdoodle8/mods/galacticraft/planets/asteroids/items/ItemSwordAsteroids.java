package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSwordAsteroids extends SwordItem implements ISortable
{
    //    private double attackDamageD;
//
    public ItemSwordAsteroids(Item.Properties builder)
    {
        super(EnumItemTierAsteroids.TITANIUM, 5, -2.8F, builder);
//        this.setUnlocalizedName(assetName);
//        this.attackDamageD = 9.0D;
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + assetName);
    }

    @Override
    public float getAttackDamage()
    {
        return 6.0F;
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TOOLS;
    }

//    @Override
//    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EquipmentSlotType equipmentSlot)
//    {
//        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
//
//        if (equipmentSlot == EquipmentSlotType.MAINHAND)
//        {
//            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamageD, 0));
//            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.8D, 0));
//        }
//
//        return multimap;
//    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        float hardness = state.getBlockHardness(worldIn, pos);
        if (hardness > 0F)
        {
            stack.damageItem(hardness > 0.2001F ? 2 : 1, entityLiving, (e) ->
            {
                e.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }
}
