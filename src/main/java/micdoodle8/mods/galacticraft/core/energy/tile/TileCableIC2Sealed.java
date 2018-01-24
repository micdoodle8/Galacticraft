package micdoodle8.mods.galacticraft.core.energy.tile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileCableIC2Sealed extends TileEntity
{
    @RuntimeInterface(clazz = "ic2.core.block.wiring.TileEntityCable", modID = CompatibilityManager.modidIC2, deobfName = "EXTENDS")
    public TileCableIC2Sealed setupInsulation(Object cableType, int insulation)
    {
        Class tileIC2 = this.getClass().getSuperclass();
        Field ins, cable;
        try
        {
            ins = tileIC2.getDeclaredField("insulation");
            ins.setAccessible(true);
            ins.set(this, insulation);
            if (cableType != null)
            {
                cable = tileIC2.getDeclaredField("cableType");
                cable.setAccessible(true);
                cable.set(this, cableType);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return this;
    }
    
    @Override
    public void validate()
    {
        this.tileEntityInvalid = false;
        World world = this.getWorld();
        if (world != null && this.pos != null)
        {
            try
            {
                Class tileIC2 = this.getClass().getSuperclass().getSuperclass();
                Field loadState = tileIC2.getDeclaredField("loadState");
                loadState.setAccessible(true);
                loadState.set(this, (byte) 1);
                Object tickHandler = CompatibilityManager.fieldIC2tickhandler.get(null);
                for (Method m : tickHandler.getClass().getMethods())
                {
                    if (m.getName().equals("requestSingleWorldTick"))
                    {
                        m.invoke(tickHandler, world, this);
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @RuntimeInterface(clazz = "ic2.core.IWorldTickCallback", modID = CompatibilityManager.modidIC2)
    public void onTick(World world)
    {
        if (!this.isInvalid() && world.isBlockLoaded(this.pos) && (world.getBlockState(this.pos)).getBlock() == GCBlocks.sealableBlock && world.getTileEntity(this.pos) == this)
        {
            Class ic2Tile = this.getClass().getSuperclass().getSuperclass();
            try
            {
                ic2Tile.getDeclaredMethod("onLoaded").invoke(this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

