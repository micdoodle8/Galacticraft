package micdoodle8.mods.galacticraft.core.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

public class GalacticraftStateMapper extends StateMapperBase
{
    public static final GalacticraftStateMapper INSTANCE = new GalacticraftStateMapper();

    public static String getPropertyString(IBlockState state)
    {
        return INSTANCE.getPropertyString(state.getProperties());
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        ResourceLocation loc = Block.REGISTRY.getNameForObject(state.getBlock());
        loc = new ResourceLocation(loc.getResourceDomain().replace("|", ""), loc.getResourcePath());
        return new ModelResourceLocation(loc, getPropertyString(state));
    }
}
