package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.item.IPaintable;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.inventory.ContainerDeconstructor;
import micdoodle8.mods.galacticraft.core.inventory.ContainerPainter;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

//import net.minecraft.item.EnumDyeColor;

public class TileEntityPainter extends TileEntityInventory implements IDisableableMachine, IPacketReceiver, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.painter)
    public static TileEntityType<TileEntityPainter> TYPE;

    private static final int RANGE_DEFAULT = 96;
    public static Map<DimensionType, Set<BlockVec3>> loadedTilesForDim = new HashMap<>();

    public int range = RANGE_DEFAULT;  //currently unused

    public int[] glassColor = new int[]{-1, -1, -1};  //Size of this array must match GlassType enum
    public final Set<PlayerEntity> playersUsing = new HashSet<PlayerEntity>();

    public int guiColor = 0xffffff;

    public TileEntityPainter()
    {
        super(TYPE);
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
        if (item instanceof DyeItem)
        {
            color = ((DyeItem) item).getDyeColor().getColorValue();
        }
        else if (item instanceof BlockItem)
        {
            Block b = ((BlockItem) item).getBlock();
            try
            {
                MaterialColor mc = b.getDefaultState().getMaterialColor(world, null);
                color = mc.colorValue;
            }
            catch (Exception e)
            {
            }
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

    private void applyColorToItem(ItemStack itemStack, int color, PlayerEntity player)
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
            Block b = ((BlockItem) item).getBlock();
            int result = 0;
            if (b instanceof IPaintable)
            {
                result = ((IPaintable) b).setColor(color, player);
            }
//            if (b instanceof BlockSpaceGlass)
//            {
//                int type = ((BlockSpaceGlass)b).type.ordinal();
//                this.glassColor[type] = color;
//                if (result > 0 && LogicalSide == LogicalSide.CLIENT)
//                {
//                    BlockSpaceGlass.updateClientRender();
//                }
//            } TODO Space glass
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
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        if (nbt.contains("guic"))
        {
            this.glassColor[0] = nbt.getInt("G1");
            this.glassColor[1] = nbt.getInt("G2");
            this.glassColor[2] = nbt.getInt("G3");
            this.range = nbt.getInt("rge");
            this.guiColor = nbt.getInt("guic");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("G1", this.glassColor[0]);
        nbt.putInt("G2", this.glassColor[1]);
        nbt.putInt("G3", this.glassColor[2]);
        nbt.putInt("guic", this.guiColor);
        nbt.putInt("rge", this.range);

        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    private static Set<BlockVec3> getLoadedTiles(World world)
    {
        DimensionType dimID = GCCoreUtil.getDimensionType(world);
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
            //Request any networked information from server on first client tick
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
        else
        {
            getLoadedTiles(this.world).add(new BlockVec3(this.pos));
        }
    }

    @Override
    public void onChunkUnloaded()
    {
        getLoadedTiles(this.world).remove(new BlockVec3(this.pos));
        super.onChunkUnloaded();
    }

    @Override
    public void remove()
    {
        if (!this.world.isRemote)
        {
            getLoadedTiles(this.world).remove(new BlockVec3(this.pos));
        }
        super.remove();
    }

    public static void onServerTick(World world)
    {
        Set<BlockVec3> loaded = getLoadedTiles(world);
        DimensionType dimID = GCCoreUtil.getDimensionType(world);
        List<ServerPlayerEntity> allPlayers = PlayerUtil.getPlayersOnline();
        for (final ServerPlayerEntity player : allPlayers)
        {
            if (player.dimension != dimID)
            {
                continue;
            }

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
                    ((TileEntityPainter) te).dominantToPlayer(player);
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

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        //Used to do the painting!
        for (PlayerEntity entityPlayer : playersUsing)
        {
            this.buttonPressed(index, entityPlayer);
        }
    }

    public void buttonPressed(int index, PlayerEntity player)
    {
        switch (index)
        {
        case 0:  //Apply Paint
            this.applyColorToItem(this.getStackInSlot(1), this.guiColor, player);
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

//        if (CompatibilityManager.isIc2Loaded())
//        {
//            ItemStack ic2paintbrush = IC2Items.getItem("painter");
//            if (ic2paintbrush != null && item == ic2paintbrush.getItem())
//            {
//                return DyeItem.DYE_COLORS[itemStack.getDamage()];
//            }
//        } TODO Ic2 support

//        if (CompatibilityManager.isBOPLoaded())
//        {
//            if (item == BOPItems.black_dye) return DyeItem.DYE_COLORS[DyeColor.BLACK.getDyeDamage()];
//            if (item == BOPItems.blue_dye) return DyeItem.DYE_COLORS[DyeColor.BLUE.getDyeDamage()];
//            if (item == BOPItems.brown_dye) return DyeItem.DYE_COLORS[DyeColor.BROWN.getDyeDamage()];
//            if (item == BOPItems.green_dye) return DyeItem.DYE_COLORS[DyeColor.GREEN.getDyeDamage()];
//            if (item == BOPItems.white_dye) return DyeItem.DYE_COLORS[DyeColor.WHITE.getDyeDamage()];
//        } TODO Biomes o plenty support

        return -1;
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerPainter(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.painter");
    }
}
