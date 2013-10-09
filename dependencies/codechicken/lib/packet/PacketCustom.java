package codechicken.lib.packet;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.vec.BlockCoord;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.network.NetworkModHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;

public final class PacketCustom implements MCDataInput, MCDataOutput
{
    public static interface ICustomPacketHandler 
    {
    }
    
    public interface IClientPacketHandler extends ICustomPacketHandler
    {
        public void handlePacket(PacketCustom packetCustom, NetClientHandler nethandler, Minecraft mc);
    }
    
    public interface IServerPacketHandler extends ICustomPacketHandler
    {
        public void handlePacket(PacketCustom packetCustom, NetServerHandler nethandler, EntityPlayerMP sender);
    }
    
    public static class PacketAssembler
    {
        public class AssemblyEntry
        {
            public AssemblyEntry(Object channel, int type, int length)
            {
                this.channel = channel;
                this.type = type;
                data = new byte[length];
            }
            
            public void append(byte[] b, int off, int len)
            {
                System.arraycopy(b, off, data, pos, len);
                pos+=len;
            }
            
            public PacketCustom finished()
            {
                if(pos < data.length)
                    return null;
                
                return new PacketCustom(channel, type, data);
            }
            
            Object channel;
            int type;
            int pos;
            byte[] data;
        }
        
        public HashMap<Integer, AssemblyEntry> assemblerMap = new HashMap<Integer, AssemblyEntry>();
        
        public PacketCustom assemble(IPacketCarrier carrier, Packet packet)
        {
            int type = carrier.readType(packet);
            if(type != 0x80)
                return new PacketCustom(carrier.readChannel(packet), carrier.readType(packet), carrier.readData(packet));
            
            byte[] data = carrier.readData(packet);
            int asmID = readInt(data, 0);
            AssemblyEntry e = assemblerMap.get(asmID);
            if(e == null)
            {
                e = new AssemblyEntry(carrier.readChannel(packet), data[4]&0xFF, readInt(data, 5));
                assemblerMap.put(asmID, e);
                return null;
            }
            
            e.append(data, 4, data.length-4);
            PacketCustom ret = e.finished();
            if(ret != null)
                assemblerMap.remove(asmID);
            return ret;
        }
    }
    
    private static abstract class CustomPacketHandler implements IPacketHandler
    {
        HashMap<Integer, ICustomPacketHandler> handlermap = new HashMap<Integer, ICustomPacketHandler>();
        
        public CustomPacketHandler(String channel) 
        {
            NetworkRegistry.instance().registerChannel(this, channel, getSide());
        }

        @Override
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
        {
            PacketCustom packetCustom = assembler.assemble(carrier250, packet);
            if(packetCustom == null)
                return;
            
            ICustomPacketHandler handler = handlermap.get(packetCustom.getType());
            if(handler != null)
                handle(handler, packetCustom, player);
        }

        public void registerRange(int firstID, int lastID, ICustomPacketHandler handler) 
        {
            for(int i = firstID; i <= lastID; i++)
                handlermap.put(i, handler);
        }
        
        public abstract Side getSide();
        public abstract void handle(ICustomPacketHandler handler, PacketCustom packet, Player player);
    }
    
    private static class ClientPacketHandler extends CustomPacketHandler
    {
        public ClientPacketHandler(String channel) 
        {
            super(channel);
            NetClientHandlerHelper.register();
        }

        @Override
        public Side getSide() 
        {
            return Side.CLIENT;
        }

        @Override
        public void handle(ICustomPacketHandler handler, PacketCustom packet, Player player) 
        {
            ((IClientPacketHandler)handler).handlePacket(packet, NetClientHandlerHelper.handler, Minecraft.getMinecraft());
        }
    }
    
    private static class ServerPacketHandler extends CustomPacketHandler
    {
        public ServerPacketHandler(String channel) 
        {
            super(channel);
        }

        @Override
        public Side getSide() 
        {
            return Side.SERVER;
        }

        @Override
        public void handle(ICustomPacketHandler handler, PacketCustom packet, Player player) 
        {
            ((IServerPacketHandler)handler).handlePacket(packet, ((EntityPlayerMP)player).playerNetServerHandler, (EntityPlayerMP)player);
        }
    }
    
    private static class ServerTinyPacketHandler
    {
        IServerPacketHandler serverHandler;
        
        public ServerTinyPacketHandler(IServerPacketHandler handler)
        {
            serverHandler = handler;
        }

        public void handle(PacketCustom packetCustom, NetHandler handler)
        {
            serverHandler.handlePacket(packetCustom, (NetServerHandler)handler, ((NetServerHandler)handler).playerEntity);
        }
    }
    
    private static class ClientTinyPacketHandler
    {
        IClientPacketHandler clientHandler;
        
        public ClientTinyPacketHandler(IClientPacketHandler handler)
        {
            clientHandler = handler;
        }

        public void handle(PacketCustom packetCustom, NetHandler handler)
        {
            clientHandler.handlePacket(packetCustom, (NetClientHandler)handler, Minecraft.getMinecraft());
        }
    }
    
    public static final class CustomTinyPacketHandler implements ITinyPacketHandler
    {        
        private ClientTinyPacketHandler clientDelegate;
        private ServerTinyPacketHandler serverDelegate;
        
        @Override
        public void handle(NetHandler handler, Packet131MapData packet)
        {
            PacketCustom packetCustom = assembler.assemble(carrier131, packet);
            if(packetCustom == null)
                return;
            
            if(handler instanceof NetServerHandler)
                serverDelegate.handle(packetCustom, handler);
            else
                clientDelegate.handle(packetCustom, handler);
        }

        private void registerSidedHandler(ICustomPacketHandler handler)
        {
            if(handler instanceof IClientPacketHandler)
            {
                if(clientDelegate != null)
                    throw new IllegalStateException("Client handler already registered");
                
                clientDelegate = new ClientTinyPacketHandler((IClientPacketHandler) handler);
            }
            else if(handler instanceof IServerPacketHandler)
            {
                if(serverDelegate != null)
                    throw new IllegalStateException("Server handler already registered");
                
                serverDelegate = new ServerTinyPacketHandler((IServerPacketHandler) handler);
            }
            else
            {
                throw new IllegalStateException("Handler is not a client or server handler");
            }
        }
    }
    
    private static class NetClientHandlerHelper implements IConnectionHandler
    {
        private static boolean registered = false;
        public static NetClientHandler handler;
        
        public static void register()
        {
            if(registered)
                return;
            
            NetworkRegistry.instance().registerConnectionHandler(new NetClientHandlerHelper());
            registered = true;
        }
        
        @Override
        public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
        {
            handler = (NetClientHandler) netClientHandler;
        }
        
        @Override
        public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
        {
            handler = (NetClientHandler) netClientHandler;
        }
        
        @Override
        public void connectionClosed(INetworkManager manager)
        {
        }
        
        @Override
        public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
        {
            return null;
        }
        
        @Override
        public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
        {
        }
        
        @Override
        public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
        {
        }
    }
    
    public static interface IPacketCarrier
    {
        public int readType(Packet packet);
        public byte[] readData(Packet packet);
        public Object readChannel(Packet packet);
        public Packet write(Object channel, boolean chunkDataPacket, int type, byte[] data);
        public boolean shortCapped();
    }
    
    public static class Packet250Carrier implements IPacketCarrier
    {
        @Override
        public int readType(Packet packet)
        {
            return ((Packet250CustomPayload)packet).data[0]&0xFF;
        }

        @Override
        public byte[] readData(Packet packet)
        {
            byte[] data = ((Packet250CustomPayload)packet).data;
            return Arrays.copyOfRange(data, 1, data.length);
        }
        
        public Object readChannel(Packet packet)
        {
            return ((Packet250CustomPayload)packet).channel;
        }

        @Override
        public Packet write(Object channel, boolean chunkDataPacket, int type, byte[] data)
        {
            byte[] pdata = new byte[data.length+1];
            pdata[0] = (byte) type;
            System.arraycopy(data, 0, pdata, 1, data.length);
            
            Packet250CustomPayload payload = new Packet250CustomPayload();
            payload.channel = (String) channel;
            payload.isChunkDataPacket = chunkDataPacket;
            payload.data = pdata;
            payload.length = payload.data.length;            
            return payload;
        }
        
        public boolean shortCapped()
        {
            return true;
        }
    }
    
    public static class Packet131Carrier implements IPacketCarrier
    {
        @Override
        public int readType(Packet packet)
        {
            return ((Packet131MapData)packet).uniqueID&0xFF;
        }

        @Override
        public byte[] readData(Packet packet)
        {
            return ((Packet131MapData)packet).itemData;
        }
        
        public Object readChannel(Packet packet)
        {
            return ((Packet131MapData)packet).itemID;
        }

        @Override
        public Packet write(Object channel, boolean chunkDataPacket, int type, byte[] data)
        {
            NetworkModHandler nmh = FMLNetworkHandler.instance().findNetworkModHandler(channel);
            Packet131MapData payload = new Packet131MapData((short) nmh.getNetworkId(), (short) type, data);
            payload.isChunkDataPacket = chunkDataPacket;
            return payload;
        }

        @Override
        public boolean shortCapped()
        {
            return true;
        }
    }

    public static PacketAssembler assembler = new PacketAssembler();
    public static IPacketCarrier carrier250 = new Packet250Carrier();
    public static IPacketCarrier carrier131 = new Packet131Carrier();
    
    public static IPacketCarrier carrierForChannel(Object channel)
    {
        if(channel instanceof String)
            return carrier250;
        if(FMLNetworkHandler.instance().findNetworkModHandler(channel) != null)
            return carrier131;
        
        return null;
    }
    
    private static int assemblyID = 0;
    
    public static void writeInt(byte[] b, int pos, int i)
    {
        b[pos++] = (byte) (i>>>24);
        b[pos++] = (byte) (i>>16);
        b[pos++] = (byte) (i>>8);
        b[pos++] = (byte) (i);
    }
    
    public static int readInt(byte[] b, int pos)
    {
        return (b[pos++]&0xFF)<<24|
                (b[pos++]&0xFF)<<16|
                (b[pos++]&0xFF)<<8|
                b[pos++]&0xFF;
    }
    
    private PacketCustom(Object channel, int type, byte[] data)
    {
        this.channel = channel;
        this.type = type;
        if(type > 0x80)
            data = decompress(data);
        datain = new DataInputStream(new ByteArrayInputStream(data));
    }

    public PacketCustom(Object channel, int type)
    {
        if(type <= 0 || type >= 0x80)
            throw new IllegalArgumentException("Packet type: "+type+" is not within required 0 < t < 0x80");
        
        this.channel = channel;
        this.type = type;
        isChunkDataPacket = false;
        
        dataarrayout = new ByteArrayOutputStream();
        dataout = new DataOutputStream(dataarrayout);
    }

    public boolean incoming()
    {
        return dataout == null;
    }
    
    public int getType()
    {
        return type&0x7F;
    }
    
    public PacketCustom setChunkDataPacket()
    {
        isChunkDataPacket = true;
        return this;
    }
    
    private byte[] decompress(byte[] cdata)
    {
        if((type&0x80) == 0)
            return cdata;
        
        Inflater inflater = new Inflater();
        try
        {
            byte[] ddata = new byte[readInt(cdata, 0)];
            inflater.setInput(cdata, 4, cdata.length-4);
            inflater.inflate(ddata);
            return ddata;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            inflater.end();
        }
    }
    
    public PacketCustom compressed()
    {
        if(incoming())
            throw new IllegalStateException("Tried to compress an incoming packet");
        if((type&0x80) != 0)
            throw new IllegalStateException("Packet already compressed");
        type|=0x80;
        return this;
    }
    
    private byte[] compress(byte[] data)
    {
        Deflater deflater = new Deflater();
        try
        {
            deflater.setInput(data, 0, data.length);
            deflater.finish();
            byte[] cbuf = new byte[data.length];
            int clen = deflater.deflate(cbuf, 0, data.length);
            if(clen == data.length || !deflater.finished())//not worth compressing, gets larger
            {
                type &= 0x7F;
                return data;
            }
            
            byte[] cdata = new byte[clen+4];
            writeInt(cdata, 0, data.length);
            System.arraycopy(cbuf, 0, cdata, 4, clen);
            type|=0x80;
            return cdata;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            deflater.end();
        }
    }
    
    public Packet toPacket()
    {
        if(incoming())
            throw new IllegalStateException("Tried to write an incoming packet");
        
        try
        {
            dataout.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        
        byte[] data = dataarrayout.toByteArray();
        if(data.length > 32000 || (type&0x80) != 0)
            data = compress(data);

        IPacketCarrier carrier = carrierForChannel(channel);
        if(data.length > 32000 && carrier.shortCapped())
        {
            MetaPacket payload = new MetaPacket();
            int asmID = assemblyID++;
            
            byte[] hdata = new byte[9];
            writeInt(hdata, 0, asmID);
            hdata[4] = (byte) type;
            writeInt(hdata, 5, data.length);
            payload.packets.add(carrier.write(channel, isChunkDataPacket, 0x80, hdata));
            
            for(int i = 0; i < data.length; i+=32000)
            {
                int size = Math.min(data.length-i, 32000);
                byte[] sdata = new byte[size+4];
                writeInt(sdata, 0, asmID);
                System.arraycopy(data, i, sdata, 4, size);
                payload.packets.add(carrier.write(channel, isChunkDataPacket, 0x80, sdata));
            }
            
            return payload;
        }
        
        return carrier.write(channel, isChunkDataPacket, type, data);
    }
        
    public PacketCustom writeBoolean(boolean b)
    {
        try
        {
            dataout.writeBoolean(b);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeByte(int b)
    {
        try
        {
            dataout.writeByte(b);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeShort(int s)
    {
        try
        {
            dataout.writeShort(s);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeInt(int i)
    {
        try
        {
            dataout.writeInt(i);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeFloat(float f)
    {
        try
        {
            dataout.writeFloat(f);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeDouble(double d)
    {
        try
        {
            dataout.writeDouble(d);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeLong(long l)
    {
        try
        {
            dataout.writeLong(l);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
        
    @Override
    public PacketCustom writeChar(char c)
    {
        try
        {
            dataout.writeChar(c);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeByteArray(byte[] barray)
    {
        try
        {
            dataout.write(barray);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeCoord(int x, int y, int z)
    {
        writeInt(x);
        writeInt(y);
        writeInt(z);
        return this;
    }
    
    public PacketCustom writeCoord(BlockCoord coord)
    {
        writeInt(coord.x);
        writeInt(coord.y);
        writeInt(coord.z);
        return this;
    }
    
    public PacketCustom writeString(String s)
    {
        try
        {
            if(s.length() > 65535)
                throw new IOException("String length: "+s.length()+"too long.");
            dataout.writeShort(s.length());
            dataout.writeChars(s);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public PacketCustom writeItemStack(ItemStack stack)
    {
        writeItemStack(stack, false);
        return this;
    }
    
    public PacketCustom writeItemStack(ItemStack stack, boolean large)
    {
        if (stack == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(stack.itemID);
            if(large)
                writeInt(stack.stackSize);
            else
                writeByte(stack.stackSize);
            writeShort(stack.getItemDamage());
            writeNBTTagCompound(stack.stackTagCompound);
        }
        return this;
    }
        
    public PacketCustom writeNBTTagCompound(NBTTagCompound compound)
    {
        try
        {            
            if (compound == null)
            {
                writeShort(-1);
            }
            else
            {
                byte[] bytes = CompressedStreamTools.compress(compound);
                writeShort((short)bytes.length);
                writeByteArray(bytes);
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }

    public PacketCustom writeFluidStack(FluidStack fluid)
    {
        if (fluid == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(fluid.fluidID);
            writeInt(fluid.amount);
            writeNBTTagCompound(fluid.tag);
        }
        return this;
    }

    public boolean readBoolean()
    {
        try
        {
            return datain.readBoolean();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public int readUByte()
    {
        return readByte() & 0xFF;
    }
    
    public int readUShort()
    {
        return readShort() & 0xFFFF;
    }
    
    public byte readByte()
    {
        try
        {
            return datain.readByte();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public short readShort()
    {
        try
        {
            return datain.readShort();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public int readInt()
    {
        try
        {
            return datain.readInt();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public float readFloat()
    {
        try
        {
            return datain.readFloat();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public double readDouble()
    {
        try
        {
            return datain.readDouble();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public long readLong()
    {
        try
        {
            return datain.readLong();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public char readChar()
    {
        try
        {
            return datain.readChar();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public BlockCoord readCoord()
    {
        return new BlockCoord(readInt(), readInt(), readInt());
    }
    
    public byte[] readByteArray(int length)
    {
        try
        {
            byte[] barray = new byte[length];
            datain.readFully(barray, 0, length);
            return barray;
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public String readString()
    {
        try
        {
            int length = datain.readUnsignedShort();
            char[] chars = new char[length];
            for(int i = 0; i < length; i++)
                chars[i] = readChar();
            
            return new String(chars);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        
    }

    public ItemStack readItemStack()
    {
        return readItemStack(false);
    }
    
    public ItemStack readItemStack(boolean large)
    {
        ItemStack item = null;
        short itemID = readShort();

        if (itemID >= 0)
        {
            int stackSize = large ? readInt() : readByte();
            short damage = readShort();
            item = new ItemStack(itemID, stackSize, damage);
            item.stackTagCompound = readNBTTagCompound();
        }

        return item;
    }
    
    public NBTTagCompound readNBTTagCompound()
    {
        try
        {
            short len = readShort();
    
            if (len < 0)
                return null;
            
            byte[] bytes = readByteArray(len);
            return CompressedStreamTools.decompress(bytes);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
        
    public FluidStack readFluidStack()
    {
        FluidStack fluid = null;
        short fluidID = readShort();

        if (fluidID >= 0)
            fluid = new FluidStack(fluidID, readInt(), readNBTTagCompound());

        return fluid;
    }

    private Object channel;
    private int type;
    private boolean isChunkDataPacket;
    
    private ByteArrayOutputStream dataarrayout;
    private DataOutputStream dataout;
    
    private DataInputStream datain;
    
    private static HashMap<String, CustomPacketHandler> clienthandlermap = new HashMap<String, CustomPacketHandler>();    
    private static HashMap<String, CustomPacketHandler> serverhandlermap = new HashMap<String, CustomPacketHandler>();
    
    public static void assignHandler(String channel, int firstID, int lastID, ICustomPacketHandler IHandler)
    {
        Side side = IHandler instanceof IClientPacketHandler ? Side.CLIENT : Side.SERVER;
        HashMap<String, CustomPacketHandler> handlerMap = side.isClient() ? clienthandlermap : serverhandlermap;
        CustomPacketHandler handler = handlerMap.get(channel);
            
        if(handler == null)
        {
            if(side.isClient())
                handler = new ClientPacketHandler(channel);
            else
                handler = new ServerPacketHandler(channel);
            
            handlerMap.put(channel, handler);
        }
        handler.registerRange(firstID, lastID, IHandler);
    }

    public static void assignHandler(Object mod, ICustomPacketHandler handler)
    {
        NetworkModHandler nmh = FMLNetworkHandler.instance().findNetworkModHandler(mod);
        if(nmh == null || nmh.getTinyPacketHandler() == null || !(nmh.getTinyPacketHandler() instanceof CustomTinyPacketHandler))
            throw new IllegalStateException("Invalid network tiny packet handler for mod: "+mod);
        
        ((CustomTinyPacketHandler)nmh.getTinyPacketHandler()).registerSidedHandler(handler);
    }
    
    public void sendToPlayer(EntityPlayer player)
    {
        sendToPlayer(toPacket(), player);
    }
    
    public static void sendToPlayer(Packet packet, EntityPlayer player)
    {
        if(player == null)
            sendToClients(packet);
        else
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
    }
    
    public void sendToClients()
    {
        sendToClients(toPacket());
    }    
    
    public static void sendToClients(Packet packet)
    {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
    }
    
    public void sendPacketToAllAround(double x, double y, double z, double range, int dim)
    {
        sendToAllAround(toPacket(), x, y, z, range, dim);
    }
    
    public static void sendToAllAround(Packet packet, double x, double y, double z, double range, int dim)
    {
        MinecraftServer.getServer().getConfigurationManager().sendToAllNear(x, y, z, range, dim, packet);
    }
    
    public void sendToDimension(int dim)
    {
        sendToDimension(toPacket(), dim);
    }
    
    public static void sendToDimension(Packet packet, int dim)
    {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dim);
    }

    public void sendToChunk(World world, int chunkX, int chunkZ)
    {
        sendToChunk(toPacket(), world, chunkX, chunkZ);
    }
    
    public static void sendToChunk(Packet packet, World world, int chunkX, int chunkZ)
    {
        PlayerInstance p = ((WorldServer)world).getPlayerManager().getOrCreateChunkWatcher(chunkX, chunkZ, false);
        if(p != null)
            p.sendToAllPlayersWatchingChunk(packet);
    }
    
    public void sendToOps()
    {
        sendToOps(toPacket());
    }

    public static void sendToOps(Packet packet)
    {
        for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            if(MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.username))
                sendToPlayer(packet, player);
    }
    
    @SideOnly(Side.CLIENT)
    public void sendToServer()
    {
        sendToServer(toPacket());
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(Packet packet)
    {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }
}
