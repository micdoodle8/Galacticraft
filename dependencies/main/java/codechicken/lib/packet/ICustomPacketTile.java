package codechicken.lib.packet;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;

public interface ICustomPacketTile {

    void writeToPacket(MCDataOutput packet);

    void readFromPacket(MCDataInput packet);

}
