package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IPaintable;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpaceGlass;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import ic2.api.item.IC2Items;
import io.netty.buffer.ByteBuf;

import java.util.*;

import biomesoplenty.api.item.BOPItems;

//import net.minecraft.item.EnumDyeColor;

public class TileEntityPainter extends TileEntity implements IDisableableMachine, IInventoryDefaults, IPacketReceiver
{
    private static final int RANGE_DEFAULT = 96;
    public static Map<Integer, Set<BlockVec3>> loadedTilesForDim = new HashMap<Integer, Set<BlockVec3>>();

    public int range = RANGE_DEFAULT;  //currently unused
    
    public int[] glassColor = new int[]{ -1, -1, -1 };  //Size of this array must match GlassType enum
    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    private NonNullList<ItemStack> containingItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    
    public int guiColor = 0xffffff;

    
    public void takeColorFromItem(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return;
        }

        Item item = itemStack.getItem();
        int color = -1;
        if (item == Items.DYE)
        {
            color = ItemDye.DYE_COLORS[itemStack.getItemDamage()];
        }
        else if (item instanceof ItemBlock)
        {
            Block b = ((ItemBlock)item).getBlock();
            IBlockState bs = b.getStateFromMeta(itemStack.getItemDamage());
            try
            {
                MapColor mc = b.getMapColor(bs, null, null);
                color = mc.colorValue;
            }
            catch (Exception e) { }
        }
        else
        {
            color = tryOtherModDyes(itemStack);
        }

        if (color >= 0)
        {
            color = ColorUtil.addColorsBasic(color, this.guiColor);
            this.guiColor = color;
        }
    }

    private void applyColorToItem(ItemStack itemStack, int color, Side side, EntityPlayer player)
    {
        if (itemStack == null)
        {
            return;
        }

        Item item = itemStack.getItem();
        
        if (item instanceof IPaintable)
        {
            //TODO  e.g. flags, eggs, rockets???
        }
        else if (item instanceof ItemBlock)
        {
            color = ColorUtil.lighten(color, 0.03F);
            Block b = ((ItemBlock)item).getBlock();
            int result = 0;
            if (b instanceof IPaintable)
            {
                result = ((IPaintable) b).setColor(color, player, side);
            }
            if (b instanceof BlockSpaceGlass)
            {
                int type = ((BlockSpaceGlass)b).type.ordinal(); 
                this.glassColor[type] = color;
                if (result > 0 && side == Side.CLIENT)
                {
                    BlockSpaceGlass.updateClientRender();
                }
            }
        }
    }
    
    private void setGlassColors(int color1, int color2, int color3)
    {
        boolean changes = false;
        if (this.glassColor[0] != color1)
        {
            changes = true;
            this.glassColor[0] = color1;
        }
        if (this.glassColor[1] != color2)
        {
            changes = true;
            this.glassColor[1] = color2;
        }
        if (this.glassColor[2] != color3)
        {
            changes = true;
            this.glassColor[2] = color3;
        }

//        if (changes)
//            ColorUtil.updateColorsForArea(this.world), this.pos, this.range, this.glassColor[0], this.glassColor[1], this.glassColor[2]);;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("guic"))
        {
            this.glassColor[0] = nbt.getInteger("G1");
            this.glassColor[1] = nbt.getInteger("G2");
            this.glassColor[2] = nbt.getInteger("G3");
            this.range = nbt.getInteger("rge");
            this.guiColor = nbt.getInteger("guic");
        }
        
        ItemStackHelper.loadAllItems(nbt, this.containingItems);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("G1", this.glassColor[0]);
        nbt.setInteger("G2", this.glassColor[1]);
        nbt.setInteger("G3", this.glassColor[2]);
        nbt.setInteger("guic", this.guiColor);
        nbt.setInteger("rge", this.range);

        ItemStackHelper.saveAllItems(nbt, this.containingItems);
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    private static Set<BlockVec3> getLoadedTiles(World world)
    {
        int dimID = GCCoreUtil.getDimensionID(world);
        Set<BlockVec3> loaded = loadedTilesForDim.get(dimID);
        
        if (loaded == null)
        {
            loaded = new HashSet<BlockVec3>();
            loadedTilesForDim.put(dimID, loaded);
        }
        
        return loaded;
    }
    
    @Override
    public void onLoad()
    {
        if (this.world.isRemote)
        {
            //Request any networked information from server on first client update
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
        else
        {
            this.getLoadedTiles(this.world).add(new BlockVec3(this.pos));
        }
    }

    @Override
    public void onChunkUnload()
    {
        this.getLoadedTiles(this.world).remove(new BlockVec3(this.pos));
        super.onChunkUnload();
    }

    @Override
    public void invalidate()
    {
        if (!this.world.isRemote)
            this.getLoadedTiles(this.world).remove(new BlockVec3(this.pos));
        super.invalidate();
    }

    public static void onServerTick(World world)
    {
        Set<BlockVec3> loaded = getLoadedTiles(world);
        int dimID = GCCoreUtil.getDimensionID(world);
        List<EntityPlayerMP> allPlayers = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        for (final EntityPlayerMP player : allPlayers)
        {
            if (player.dimension != dimID) continue;

            BlockVec3 playerPos = new BlockVec3(player);
            BlockVec3 nearest = null;
            int shortestDistance = RANGE_DEFAULT * RANGE_DEFAULT;
            for (final BlockVec3 bv : loaded)
            {
                int distance = bv.distanceSquared(playerPos);
                if (distance < shortestDistance)
                {
                    shortestDistance = distance;
                    nearest = bv;
                }
            }
            
            if (nearest != null)
            {
                TileEntity te = nearest.getTileEntity(world);
                if (te instanceof TileEntityPainter)
                {
                    ((TileEntityPainter)te).dominantToPlayer(player);
                }
            }

            //TODO
            //Make sure this works in a way so that the nearest Painter quickly takes priority, but there is no race condition...
            //Also maybe some hysteresis?
        }
    }

    private void dominantToPlayer(EntityPlayerMP player)
    {
        GCPlayerStats.get(player).setGlassColors(this.glassColor[0], this.glassColor[1], this.glassColor[2]);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("tile.machine3.9.name");
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.world.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        //Used to do the painting!
        for (EntityPlayer entityPlayer : playersUsing)
        {
            this.buttonPressed(index, entityPlayer, Side.SERVER);
        }
    }
    
    public void buttonPressed(int index, EntityPlayer player, Side side)
    {
        switch (index)
        {
        case 0:  //Apply Paint
            this.applyColorToItem(this.getStackInSlot(1), this.guiColor, side, player);
            break;
        case 1:  //Mix Colors
            this.takeColorFromItem(this.getStackInSlot(0));
            break;
        case 2:  //Reset Colors
            this.guiColor = 0xffffff;
            break;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return 2;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems.get(par1);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.containingItems, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack oldstack = ItemStackHelper.getAndRemove(this.containingItems, index);
        if (!oldstack.isEmpty())
        {
        	this.markDirty();
        }
    	return oldstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.containingItems.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.containingItems)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }


    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.world.isRemote)
        {
            return;
        }

        sendData.add(this.guiColor);
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.world.isRemote)
        {
            try
            {
                this.guiColor = buffer.readInt();
            }
            catch (Exception ignore)
            {
                ignore.printStackTrace();
            }
        }
    }

    //Any special cases go here, e.g. coloured dye or paint items added by other mods
    private static int tryOtherModDyes(ItemStack itemStack)
    {
        Item item = itemStack.getItem();

        if (CompatibilityManager.isIc2Loaded())
        {
            ItemStack ic2paintbrush = IC2Items.getItem("painter");
            if (ic2paintbrush != null && item == ic2paintbrush.getItem())
            {
                return ItemDye.DYE_COLORS[itemStack.getItemDamage()];
            }
        }

        if (CompatibilityManager.isBOPLoaded())
        {
            if (item == BOPItems.black_dye) return ItemDye.DYE_COLORS[EnumDyeColor.BLACK.getDyeDamage()];
            if (item == BOPItems.blue_dye) return ItemDye.DYE_COLORS[EnumDyeColor.BLUE.getDyeDamage()];
            if (item == BOPItems.brown_dye) return ItemDye.DYE_COLORS[EnumDyeColor.BROWN.getDyeDamage()];
            if (item == BOPItems.green_dye) return ItemDye.DYE_COLORS[EnumDyeColor.GREEN.getDyeDamage()];
            if (item == BOPItems.white_dye) return ItemDye.DYE_COLORS[EnumDyeColor.WHITE.getDyeDamage()];
        }

        return -1;
    }
}
