package micdoodle8.mods.galacticraft.core.tick;

import java.util.EnumSet;
import micdoodle8.mods.galacticraft.API.IDetectableMetadataResource;
import micdoodle8.mods.galacticraft.API.IDetectableResource;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class GCCoreTickHandlerSlowClient implements IScheduledTickHandler
{
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        if (player != null)
        {
            ClientProxyCore.valueableBlocks.clear();

            for (int i = -4; i < 5; i++)
            {
                for (int j = -4; j < 5; j++)
                {
                    for (int k = -4; k < 5; k++)
                    {
                        int x, y, z;

                        x = MathHelper.floor_double(player.posX + i);
                        y = MathHelper.floor_double(player.posY + j);
                        z = MathHelper.floor_double(player.posZ + k);

                        final int id = player.worldObj.getBlockId(x, y, z);

                        if (id != 0)
                        {
                            final Block block = Block.blocksList[id];

                            if (block != null && (block instanceof BlockOre || block instanceof IDetectableResource || block instanceof IDetectableMetadataResource && ((IDetectableMetadataResource) block).isValueable(player.worldObj.getBlockMetadata(x, y, z))))
                            {
                                final int[] blockPos =
                                { x, y, z };

                                if (!this.alreadyContainsBlock(x, y, z))
                                {
                                    ClientProxyCore.valueableBlocks.add(blockPos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean alreadyContainsBlock(int x1, int y1, int z1)
    {
        for (final int[] coordArray : ClientProxyCore.valueableBlocks)
        {
            final int x = coordArray[0];
            final int y = coordArray[1];
            final int z = coordArray[2];

            if (x1 == x && y1 == y && z1 == z)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel()
    {
        return "GalacticraftSlowClient";
    }

    @Override
    public int nextTickSpacing()
    {
        return 20;
    }

}
