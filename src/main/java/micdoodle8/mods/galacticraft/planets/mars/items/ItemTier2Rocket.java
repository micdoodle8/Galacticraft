package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTier2Rocket extends Item implements IHoldableItem, ISortable
{
    public ItemTier2Rocket(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }


    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
    {
        boolean padFound = false;
        TileEntity tile = null;

        if (context.getWorld().isRemote)
        {
            return ActionResultType.PASS;
        }
        else
        {
            float centerX = -1;
            float centerY = -1;
            float centerZ = -1;

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    BlockPos pos1 = context.getPos().add(i, 0, j);
                    BlockState state = context.getWorld().getBlockState(pos1);

                    if (state.getBlock() == GCBlocks.landingPadFull)
                    {
                        padFound = true;
                        tile = context.getWorld().getTileEntity(pos1);

                        centerX = context.getPos().getX() + i + 0.5F;
                        centerY = context.getPos().getY() + 0.4F;
                        centerZ = context.getPos().getZ() + j + 0.5F;

                        break;
                    }
                }

                if (padFound)
                {
                    break;
                }
            }

            if (padFound)
            {
                if (!placeRocketOnPad(stack, context.getWorld(), tile, centerX, centerY, centerZ))
                {
                    return ActionResultType.FAIL;
                }

                if (!context.getPlayer().abilities.isCreativeMode)
                {
                    stack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            else
            {
                return ActionResultType.PASS;
            }
        }
    }

    public boolean placeRocketOnPad(ItemStack stack, World world, TileEntity tile, float centerX, float centerY, float centerZ)
    {
        //Check whether there is already a rocket on the pad
        if (tile instanceof TileEntityLandingPad)
        {
            if (((TileEntityLandingPad) tile).getDockedEntity() != null)
            {
                return false;
            }
        }
        else
        {
            return false;
        }

        EntityAutoRocket rocket;

        if (this == MarsItems.rocketTierTwo || this == MarsItems.rocketTierTwoCargo1 || this == MarsItems.rocketTierTwoCargo2 || this == MarsItems.rocketTierTwoCargo3 || this == MarsItems.rocketTierTwoCreative)
        {
            rocket = EntityTier2Rocket.createEntityTier2Rocket(world, centerX, centerY, centerZ, EntityTier2Rocket.getTypeFromItem(this));
        }
        else
        {
            rocket = EntityCargoRocket.createEntityCargoRocket(world, centerX, centerY, centerZ, EntityCargoRocket.getTypeFromItem(this));
        }

        rocket.setPosition(rocket.getPosX(), rocket.getPosY() + rocket.getOnPadYOffset(), rocket.getPosZ());
        world.addEntity(rocket);

        if (((IRocketType) rocket).getRocketType().getPreFueled())
        {
            if (rocket instanceof EntityTieredRocket)
            {
                ((EntityTieredRocket) rocket).fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), rocket.getMaxFuel()), IFluidHandler.FluidAction.EXECUTE);
            }
            else
            {
                ((EntityCargoRocket) rocket).fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), rocket.getMaxFuel()), IFluidHandler.FluidAction.EXECUTE);
            }
        }
        else if (stack.hasTag() && stack.getTag().contains("RocketFuel"))
        {
            rocket.fuelTank.fill(new FluidStack(GCFluids.FUEL.getFluid(), stack.getTag().getInt("RocketFuel")), IFluidHandler.FluidAction.EXECUTE);
        }

        return true;
    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < EnumRocketType.values().length; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//
//            for (int i = 11; i < 10 + EnumRocketType.values().length; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        EnumRocketType type;

        if (par1ItemStack.getDamage() < 10)
        {
            type = EnumRocketType.values()[par1ItemStack.getDamage()];
        }
        else
        {
            type = EnumRocketType.values()[par1ItemStack.getDamage() - 10];
        }

        if (!type.getTooltip().getFormattedText().isEmpty())
        {
            tooltip.add(type.getTooltip());
        }

        if (type.getPreFueled())
        {
            tooltip.add(new StringTextComponent(EnumColor.RED + "\u00a7o" + GCCoreUtil.translate("gui.creative_only.desc")));
        }

        if (par1ItemStack.hasTag() && par1ItemStack.getTag().contains("RocketFuel"))
        {
            EntityAutoRocket rocket;

            if (par1ItemStack.getDamage() < 10)
            {
                rocket = EntityTier2Rocket.createEntityTier2Rocket(Minecraft.getInstance().world, 0, 0, 0, EnumRocketType.values()[par1ItemStack.getDamage()]);
            }
            else
            {
                rocket = EntityCargoRocket.createEntityCargoRocket(Minecraft.getInstance().world, 0, 0, 0, EnumRocketType.values()[par1ItemStack.getDamage() - 10]);
            }

            tooltip.add(new StringTextComponent(GCCoreUtil.translate("gui.message.fuel.name") + ": " + par1ItemStack.getTag().getInt("RocketFuel") + " / " + rocket.fuelTank.getCapacity()));
        }

        if (par1ItemStack.getDamage() >= 10)
        {
            tooltip.add(new StringTextComponent(EnumColor.AQUA + GCCoreUtil.translate("gui.requires_controller.desc")));
        }
    }

//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        return super.getUnlocalizedName(par1ItemStack) + (par1ItemStack.getDamage() < 10 ? ".t2Rocket" : ".cargo_rocket");
//    }

    @Override
    public boolean shouldHoldLeftHandUp(PlayerEntity player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(PlayerEntity player)
    {
        return true;
    }

    @Override
    public boolean shouldCrouch(PlayerEntity player)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ROCKET;
    }
}
