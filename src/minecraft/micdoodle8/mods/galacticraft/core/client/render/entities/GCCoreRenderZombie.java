package micdoodle8.mods.galacticraft.core.client.render.entities;

import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderLiving;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderZombie extends RenderLiving
{
    protected ModelBiped modelBipedMain;
    protected float field_77070_b;

    public GCCoreRenderZombie(ModelBiped par1ModelBiped, float par2)
    {
        this(par1ModelBiped, par2, 1.0F);
        this.modelBipedMain = par1ModelBiped;
    }

    public GCCoreRenderZombie(ModelBiped par1ModelBiped, float par2, float par3)
    {
        super(par1ModelBiped, par2);
        this.modelBipedMain = par1ModelBiped;
        this.field_77070_b = par3;
    }
}
