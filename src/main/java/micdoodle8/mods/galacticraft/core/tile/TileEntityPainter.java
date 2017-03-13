package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.entities.player.CapabilityStatsHandler;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

//import net.minecraft.item.EnumDyeColor;

public class TileEntityPainter extends TileEntity
{
    private static final int RANGE_DEFAULT = 96;
    public static Map<Integer, Set<BlockVec3>> loadedTilesForDim = new HashMap<Integer, Set<BlockVec3>>();

    public int range = RANGE_DEFAULT;  //currently unused
    
    public int glassColor1 = -1;
    public int glassColor2 = -1;
    public int glassColor3 = -1;

    //For proof of concept and testing
    public void setAllGlassColors(ItemStack itemStack, EntityPlayerMP player)
    {
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
            MapColor mc = b.getMapColor(bs);
            color = mc.colorValue;
        }

        if (color >= 0)
        {
            color = ColorUtil.lighten(color, 0.03F);
            this.setGlassColors(color, color, color);

            //GCPlayerStats.get(player).setGlassColors(this.glassColor1, this.glassColor2, this.glassColor3);
        }
    }
    
    private void setGlassColors(int color1, int color2, int color3)
    {
        boolean changes = false;
        if (this.glassColor1 != color1)
        {
            changes = true;
            this.glassColor1 = color1;
        }
        if (this.glassColor2 != color2)
        {
            changes = true;
            this.glassColor2 = color2;
        }
        if (this.glassColor3 != color3)
        {
            changes = true;
            this.glassColor3 = color3;
        }

//        if (changes)
//            ColorUtil.updateColorsForArea(this.worldObj.provider.getDimensionId(), this.pos, this.range, this.glassColor1, this.glassColor2, this.glassColor3);;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Glass1"))
        {
            this.glassColor1 = nbt.getInteger("Glass1");
            this.glassColor2 = nbt.getInteger("Glass2");
            this.glassColor3 = nbt.getInteger("Glass3");
            this.range = nbt.getInteger("Range");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("Glass1", this.glassColor1);
        nbt.setInteger("Glass2", this.glassColor2);
        nbt.setInteger("Glass3", this.glassColor3);
        nbt.setInteger("Range", this.range);
        return nbt;
    }

    private static Set<BlockVec3> getLoadedTiles(World world)
    {
        int dimID = world.provider.getDimension();
        Set<BlockVec3> loaded = loadedTilesForDim.get(dimID);
        
        if (loaded == null)
        {
            loaded = new HashSet<BlockVec3>();
            loadedTilesForDim.put(dimID, loaded);
        }
        
        return loaded;
    }
    
    @Override
    public void validate()
    {
        super.validate();
        if (!this.worldObj.isRemote)
        {
            this.getLoadedTiles(this.worldObj).add(new BlockVec3(this.pos));
        }
    }

    @Override
    public void onChunkUnload()
    {
        this.getLoadedTiles(this.worldObj).remove(new BlockVec3(this.pos));
        super.onChunkUnload();
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
            this.getLoadedTiles(this.worldObj).remove(new BlockVec3(this.pos));
        super.invalidate();
    }

    public static void onServerTick(World world)
    {
        Set<BlockVec3> loaded = getLoadedTiles(world);
        int dimID = world.provider.getDimension();
        List<EntityPlayerMP> allPlayers = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList();
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
        player.getCapability(CapabilityStatsHandler.GC_STATS_CAPABILITY, null).setGlassColors(this.glassColor1, this.glassColor2, this.glassColor3);
    }

    //TODO: create a GUI and inventory, place specific colourable items (glass, flags etc) in this
}
