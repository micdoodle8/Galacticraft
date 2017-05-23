package micdoodle8.mods.galacticraft.api.client;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public interface ItemMeshDefinitionCustom extends ItemMeshDefinition
{
    static ItemMeshDefinition create(ItemMeshDefinitionCustom lambda)
    { 
        return lambda;
    }

    ModelResourceLocation getLocation(ItemStack stack);

    @Override
    default ModelResourceLocation getModelLocation(ItemStack stack)
    {
        return getLocation(stack);
    }
}
