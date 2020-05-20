package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IPaintable;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpaceGlass;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import ic2.api.item.IC2Items;
import io.netty.buffer.ByteBuf;

import java.util.*;

import biomesoplenty.api.item.BOPItems;

//import net.minecraft.item.EnumDyeColor;

public class TileEntityPainter extends TileEntityInventory implements IDisableableMachine, IPacketReceiver
{
    private static final int RANGE_DEFAULT = 96;
    public static Map<Integer, Set<BlockVec3>> loadedTilesForDim = new HashMap<Integer, Set<BlockVec3>>();

    public int range = RANGE_DEFAULT;  //currently unused
    
    public int[] glassColor = new int[]{ -1, -1, -1 };  //Size of this array must match GlassType enum
    public final Set<PlayerEntity> playersUsing = new HashSet<PlayerEntity>();
    
    public int guiColor = 0xffffff;

    public TileEntityPainter()
    {
        super("tile.machine3.9.name");
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

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
            color = DyeItem.DYE_COLORS[itemStack.getItemDamage()];
        }
        else if (item instanceof BlockItem)
        {
            Block b = ((BlockItem)item).getBlock();
            BlockState bs = b.getStateFromMeta(itemStack.getItemDamage());
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

    private void applyColorToItem(ItemStack itemStack, int color, Side side, PlayerEntity player)
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
        else if (item instanceof BlockItem)
        {
            color = ColorUtil.lighten(color, 0.03F);
            Block b = ((BlockItem)item).getBlock();
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
    public void readFromNBT(CompoundNBT nbt)
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
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("G1", this.glassColor[0]);
        nbt.setInteger("G2", this.glassColor[1]);
        nbt.setInteger("G3", this.glassColor[2]);
        nbt.setInteger("guic", this.guiColor);
        nbt.setInteger("rge", this.range);

        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.writeToNBT(new CompoundNBT());
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
        List<ServerPlayerEntity> allPlayers = PlayerUtil.getPlayersOnline();
        for (final ServerPlayerEntity player : allPlayers)
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

    private void dominantToPlayer(ServerPlayerEntity player)
    {
        GCPlayerStats.get(player).setGlassColors(this.glassColor[0], this.glassColor[1], this.glassColor[2]);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        //Used to do the painting!
        for (PlayerEntity entityPlayer : playersUsing)
        {
            this.buttonPressed(index, entityPlayer, Side.SERVER);
        }
    }
    
    public void buttonPressed(int index, PlayerEntity player, Side side)
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
                return DyeItem.DYE_COLORS[itemStack.getItemDamage()];
            }
        }

        if (CompatibilityManager.isBOPLoaded())
        {
            if (item == BOPItems.black_dye) return DyeItem.DYE_COLORS[DyeColor.BLACK.getDyeDamage()];
            if (item == BOPItems.blue_dye) return DyeItem.DYE_COLORS[DyeColor.BLUE.getDyeDamage()];
            if (item == BOPItems.brown_dye) return DyeItem.DYE_COLORS[DyeColor.BROWN.getDyeDamage()];
            if (item == BOPItems.green_dye) return DyeItem.DYE_COLORS[DyeColor.GREEN.getDyeDamage()];
            if (item == BOPItems.white_dye) return DyeItem.DYE_COLORS[DyeColor.WHITE.getDyeDamage()];
        }

        return -1;
    }
}
