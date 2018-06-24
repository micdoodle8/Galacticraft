package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.UUID;

public class BlockTelemetry extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
{
    //Metadata: 0-3 = orientation;  bits 2,3 = reserved for future use
    public BlockTelemetry(String assetName)
    {
        super(Material.IRON);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 3;
            break;
        case 1:
            change = 4;
            break;
        case 2:
            change = 2;
            break;
        case 3:
            change = 5;
            break;
        }

        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        final int metadata = getMetaFromState(world.getBlockState(pos));
        final int facing = metadata & 3;
        int change = 0;

        switch (facing)
        {
        case 0:
            change = 1;
            break;
        case 1:
            change = 3;
            break;
        case 2:
            change = 5;
            break;
        case 3:
            change = 4;
            break;
        case 4:
            change = 2;
            break;
        case 5:
            change = 0;
        }
        change += (12 & metadata);
        world.setBlockState(pos, getStateFromMeta(change), 2);

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityTelemetry();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityTelemetry)
            {
                ItemStack held = entityPlayer.inventory.getCurrentItem();
                //Look for Frequency Module
                if (!held.isEmpty() && held.getItem() == GCItems.basicItem && held.getItemDamage() == 19)
                {
                    NBTTagCompound fmData = held.getTagCompound();
                    if (fmData != null && fmData.hasKey("linkedUUIDMost") && fmData.hasKey("linkedUUIDLeast"))
                    {
                        UUID uuid = new UUID(fmData.getLong("linkedUUIDMost"), fmData.getLong("linkedUUIDLeast"));
                        ((TileEntityTelemetry) tile).addTrackedEntity(uuid);
                        entityPlayer.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.telemetry_succeed.message")));
                    }
                    else
                    {
                        entityPlayer.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.telemetry_fail.message")));

                        if (fmData == null)
                        {
                            fmData = new NBTTagCompound();
                            held.setTagCompound(fmData);
                        }
                    }
                    fmData.setInteger("teCoordX", pos.getX());
                    fmData.setInteger("teCoordY", pos.getY());
                    fmData.setInteger("teCoordZ", pos.getZ());
                    fmData.setInteger("teDim", GCCoreUtil.getDimensionID(world));
                    return true;
                }

                ItemStack wearing = GCPlayerStats.get(entityPlayer).getFrequencyModuleInSlot();
                if (wearing != null)
                {
                    if (wearing.hasTagCompound() && wearing.getTagCompound().hasKey("teDim"))
                    {
                        return false;
                    }
                    entityPlayer.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.telemetry_fail_wearing_it.message")));
                }
                else
                {
                    entityPlayer.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.telemetry_fail_no_frequency_module.message")));
                }
            }
        }
        return false;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
