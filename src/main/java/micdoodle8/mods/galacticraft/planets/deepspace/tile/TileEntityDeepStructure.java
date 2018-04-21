package micdoodle8.mods.galacticraft.planets.deepspace.tile;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPanelLight;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDeepStructure extends TileEntity implements IPacketReceiver
{
    public int meta;
    private IBlockState superState;
    private static IBlockState defaultLook = AsteroidBlocks.blockBasic.getStateFromMeta(6);
    @SideOnly(Side.CLIENT)
    private AxisAlignedBB renderAABB;

    public TileEntityDeepStructure()
    {
        this(0);
    }

    public TileEntityDeepStructure(int metaIn)
    {
        this.meta = metaIn;
    }

    public void initialise(int type, EnumFacing facing, EntityPlayer player, boolean isRemote, IBlockState superStateClient)
    {
        if (isRemote)
        {
            this.superState = superStateClient;
        }
        else
        {
            this.superState = defaultLook;
        }
    }

    
    public IBlockState getBaseBlock()
    {
        if (this.superState != null && this.superState.getBlock() == Blocks.AIR)
        {
            this.superState = null;
        }
        return this.superState == null ? defaultLook : this.superState;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 1, 1));
        }
        return this.renderAABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.meta = nbt.getInteger("meta");
        NBTTagCompound tag = nbt.getCompoundTag("sust");
        if (!tag.hasNoTags())
        {
            this.superState = TileEntityPanelLight.readBlockState(tag);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("meta", this.meta);
        if (this.superState != null)
        {
            NBTTagCompound tag = new NBTTagCompound();
            TileEntityPanelLight.writeBlockState(tag, this.superState);
            nbt.setTag("sust", tag);
        }
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    public static IBlockState readBlockState(String name, Integer meta)
    {
        Block block = (Block)Block.REGISTRY.getObject(new ResourceLocation(name));
        if (block == null)
        {
            return Blocks.AIR.getDefaultState();
        }
        return block.getStateFromMeta(meta);
    }

    @Override
    public void onLoad()
    {
        if (this.worldObj.isRemote)
        {
            //Request any networked information from server on first client update
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }
    
    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.worldObj.isRemote)
        {
            return;
        }

        sendData.add((byte)this.meta);
        if (this.superState != null)
        {
            Block block = this.superState.getBlock(); 
            if (block == Blocks.AIR)
            {
                this.superState = null;
                return;
            }
           
            sendData.add(((ResourceLocation)Block.REGISTRY.getNameForObject(block)).toString());
            sendData.add((byte) block.getMetaFromState(this.superState));
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.worldObj.isRemote)
        {
            try
            {
                this.meta = buffer.readByte();
                if (buffer.readableBytes() > 0)
                {
                    String name = ByteBufUtils.readUTF8String(buffer);
                    int otherMeta = buffer.readByte();
                    this.superState = readBlockState(name, otherMeta);
                    this.worldObj.markBlockRangeForRenderUpdate(this.pos, this.pos);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
