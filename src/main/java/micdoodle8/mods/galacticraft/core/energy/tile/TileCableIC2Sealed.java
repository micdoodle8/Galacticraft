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
    private static boolean onLoadedDone = false;
    private static boolean loadStateDone = false;
    private static boolean rSWTDone = false;
    private static boolean cableFieldsDone = false;
    private static Method onLoaded = null;
    private static Field loadState = null;
    private static Method requestSingleWorldTick = null;
    private static Field fieldInsulation = null;
    private static Field fieldCableType = null;

    @RuntimeInterface(clazz = "ic2.core.block.wiring.TileEntityCable", modID = CompatibilityManager.modidIC2, deobfName = "EXTENDS")
    public TileCableIC2Sealed setupInsulation(Object cableType, int insulation)
    {
        if (!cableFieldsDone) getCableFields();
        try
        {
            if (fieldInsulation != null) fieldInsulation.set(this, insulation);
            if (cableType != null)
            {
                if (fieldCableType != null) fieldCableType.set(this, cableType);
            }
        } catch (Exception ignore) {}
        
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
                if (!loadStateDone) getLoadState();
                if (loadState != null) loadState.set(this, (byte) 1);
                Object tickHandler = CompatibilityManager.fieldIC2tickhandler.get(null);
                if (!rSWTDone) getRSWT(tickHandler);
                if (requestSingleWorldTick != null) requestSingleWorldTick.invoke(tickHandler, world, this);
            } catch (Exception ignore) {}
        }
    }

    @RuntimeInterface(clazz = "ic2.core.IWorldTickCallback", modID = CompatibilityManager.modidIC2)
    public void onTick(World world)
    {
        if (!this.isInvalid() && world.isBlockLoaded(this.pos) && (world.getBlockState(this.pos)).getBlock() == GCBlocks.sealableBlock && world.getTileEntity(this.pos) == this)
        {
            if (!onLoadedDone) getOnLoaded();
            if (onLoaded != null) {
                try
                {
                    onLoaded.invoke(this);
                } catch (Exception ignore) {}
            }
        }
    }

    private void getOnLoaded()
    {
        Class ic2Tile = this.getClass().getSuperclass().getSuperclass();
        try
        {
            onLoaded = ic2Tile.getDeclaredMethod("onLoaded");
        } catch (NoSuchMethodException e)
        {
            ic2Tile = this.getClass().getSuperclass();
            try
            {
                onLoaded = ic2Tile.getDeclaredMethod("onLoaded");
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }                
        } catch (SecurityException e)
        {
            e.printStackTrace();
        }
        onLoadedDone = true;
    }
    
    private void getLoadState()
    {
        Class tileIC2 = this.getClass().getSuperclass().getSuperclass();
        try
        {
            loadState = tileIC2.getDeclaredField("loadState");
            loadState.setAccessible(true);
        } catch (NoSuchFieldException e)
        {
            tileIC2 = this.getClass().getSuperclass();
            try
            {
                loadState = tileIC2.getDeclaredField("loadState");
                loadState.setAccessible(true);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }                
        } catch (SecurityException e)
        {
            e.printStackTrace();
        }
        loadStateDone = true;
    }
    
    private void getRSWT(Object tickHandler)
    {
        for (Method m : tickHandler.getClass().getMethods())
        {
            if (m.getName().equals("requestSingleWorldTick"))
            {
                requestSingleWorldTick = m;
                break;
            }
        }
        rSWTDone = true;
    }
    
    private void getCableFields()
    {
        Class tileIC2 = this.getClass().getSuperclass();
        try
        {
            fieldInsulation = tileIC2.getDeclaredField("insulation");
            fieldInsulation.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e)
        {
            e.printStackTrace();
        }
        try
        {
            fieldCableType = tileIC2.getDeclaredField("cableType");
            fieldCableType.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e)
        {
            e.printStackTrace();
        }
        cableFieldsDone = true;
    }
}

