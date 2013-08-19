package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTerraformer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockTile;
import basiccomponents.common.BasicComponents;

public class GCMarsBlockMachine extends BlockTile
{
    public static final int COAL_GENERATOR_METADATA = 0;
    public static final int UNUSED_MACHINE_0 = 4;
    public static final int UNUSED_MACHINE_1 = 8;

    private Icon iconMachineSide;
    private Icon iconInput;

    private Icon iconTerraformer;
    private Icon unusedIcon0;
    private Icon unusedIcon1;
    private Icon unusedIcon2;

    public GCMarsBlockMachine(int id)
    {
        super(id, UniversalElectricity.machine);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_power_input");

        this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconTerraformer = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "terraformer_0");
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return this.blockIcon;
        }

        if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_1)
        {
            metadata -= GCMarsBlockMachine.UNUSED_MACHINE_1;

            // If it is the front side
            if (side == metadata + 2)
            {
                return this.iconInput;
            }
            // If it is the back side
            else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
            {
                return this.unusedIcon1;
            }
        }
        else if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_0)
        {
            metadata -= GCMarsBlockMachine.UNUSED_MACHINE_0;

            // If it is the front side
            if (side == metadata + 2)
            {
                return this.unusedIcon2;
            }
            // If it is the back side
            else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
            {
                return this.iconInput;
            }

            return this.unusedIcon0;
        }
        else
        {
            if (side == ForgeDirection.UP.ordinal() || side == ForgeDirection.DOWN.ordinal())
            {
                return this.iconMachineSide;
            }
            else if (side == ForgeDirection.getOrientation(metadata + 2).ordinal())
            {
                return this.iconInput;
            }
            else
            {
                return this.iconTerraformer;
            }
        }

        return this.iconMachineSide;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        case 3:
            change = 3;
            break;
        }

        if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_1)
        {
            world.setBlockMetadataWithNotify(x, y, z, GCMarsBlockMachine.UNUSED_MACHINE_1 + change, 3);
        }
        else if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_0)
        {
            switch (angle)
            {
            case 0:
                change = 3;
                break;
            case 1:
                change = 1;
                break;
            case 2:
                change = 2;
                break;
            case 3:
                change = 0;
                break;
            }

            world.setBlockMetadataWithNotify(x, y, z, GCMarsBlockMachine.UNUSED_MACHINE_0 + change, 3);
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, GCMarsBlockMachine.COAL_GENERATOR_METADATA + change, 3);
        }
    }

    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);
        int original = metadata;

        int change = 0;

        if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_1)
        {
            original -= GCMarsBlockMachine.UNUSED_MACHINE_1;
        }
        else if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_0)
        {
            original -= GCMarsBlockMachine.UNUSED_MACHINE_0;
        }

        // Re-orient the block
        switch (original)
        {
        case 0:
            change = 3;
            break;
        case 3:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        }

        if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_1)
        {
            change += GCMarsBlockMachine.UNUSED_MACHINE_1;
        }
        else if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_0)
        {
            change += GCMarsBlockMachine.UNUSED_MACHINE_0;
        }

        par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
        return true;
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);

        if (!par1World.isRemote)
        {
            if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_1)
            {
                par5EntityPlayer.openGui(BasicComponents.getFirstDependant(), -1, par1World, x, y, z);
                return true;
            }
            else if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_0)
            {
                par5EntityPlayer.openGui(BasicComponents.getFirstDependant(), -1, par1World, x, y, z);
                return true;
            }
            else
            {
                par5EntityPlayer.openGui(BasicComponents.getFirstDependant(), -1, par1World, x, y, z);
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_1)
        {
            return null;
        }
        else if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_0)
        {
            return null;
        }
        else
        {
            return new GCMarsTileEntityTerraformer();
        }
    }

    public ItemStack getTerraformer()
    {
        return new ItemStack(this.blockID, 1, GCMarsBlockMachine.COAL_GENERATOR_METADATA);
    }

    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(this.getTerraformer());
    }

    @Override
    public int damageDropped(int metadata)
    {
        if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_1)
        {
            return GCMarsBlockMachine.UNUSED_MACHINE_1;
        }
        else if (metadata >= GCMarsBlockMachine.UNUSED_MACHINE_0)
        {
            return GCMarsBlockMachine.UNUSED_MACHINE_0;
        }
        else
        {
            return GCMarsBlockMachine.COAL_GENERATOR_METADATA;
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int id = this.idPicked(world, x, y, z);

        if (id == 0)
        {
            return null;
        }

        Item item = Item.itemsList[id];

        if (item == null)
        {
            return null;
        }

        int metadata = this.getDamageValue(world, x, y, z);

        return new ItemStack(id, 1, metadata);
    }
}
