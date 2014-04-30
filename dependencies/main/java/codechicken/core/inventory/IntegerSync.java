package codechicken.core.inventory;

import codechicken.lib.packet.PacketCustom;

public abstract class IntegerSync implements IContainerSyncVar
{
    public int c_value;
    
    @Override
    public boolean changed()
    {
        return getValue() != c_value;
    }
    
    @Override
    public void reset()
    {
        c_value = getValue();
    }

    @Override
    public void writeChange(PacketCustom packet)
    {
        packet.writeInt(getValue());
    }

    @Override
    public void readChange(PacketCustom packet)
    {
        c_value = packet.readInt();
    }
    
    public abstract int getValue();
}
