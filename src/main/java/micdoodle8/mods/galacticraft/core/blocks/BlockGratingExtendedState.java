package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import micdoodle8.mods.galacticraft.core.util.JavaUtil;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockGratingExtendedState extends ExtendedBlockState
{
    public BlockGratingExtendedState(Block blockIn, IProperty<?>[] properties, IUnlistedProperty<?>[] unlistedProperties)
    {
        super(blockIn, properties, unlistedProperties);
    }

    @Override
    protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
    {
        if (unlistedProperties == null || unlistedProperties.isEmpty()) return super.createState(block, properties, unlistedProperties);
        return new ExtendedStateImplementation(block, properties, unlistedProperties, null);
    }

    protected static class ExtendedStateImplementation extends StateImplementation implements IExtendedBlockState
    {
        private final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties;
        private Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> normalMap;
        private final Block blockBase;

        protected ExtendedStateImplementation(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> table)
        {
            super(block, properties);
            this.blockBase = block;
            this.unlistedProperties = unlistedProperties;
            this.propertyValueTable = table;
        }

        /**
         * Get a version of this BlockState with the given Property now set to the given value
         */
        @Override
        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value)
        {
            if (!this.getProperties().containsKey(property))
            {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + getBlock().getBlockState());
            }
            else
            {
                if (!property.getAllowedValues().contains(value))
                {
                    throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(getBlock()) + ", it is not an allowed value");
                } else
                {
                    if (this.getProperties().get(property) == value)
                    {
                        return this;
                    }
                    Map<IProperty<?>, Comparable<?>> map = Maps.newHashMap(getProperties());
                    map.put(property, value);
                    if (Iterables.all(unlistedProperties.values(), Predicates.<Optional<?>>equalTo(Optional.absent())))
                    { // no dynamic properties present, looking up in the normal table
                        return normalMap.get(map);
                    }
                    ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> table = propertyValueTable;
                    table = ((StateImplementation) table.get(property, value)).getPropertyValueTable();
                    return new ExtendedStateImplementation(getBlock(), ImmutableMap.copyOf(map), unlistedProperties, table).setMap(this.normalMap);
                }
            }
        }

        @Override
        public <V> IExtendedBlockState withProperty(IUnlistedProperty<V> property, V value)
        {
            if(!this.unlistedProperties.containsKey(property))
            {
                throw new IllegalArgumentException("Cannot set unlisted property " + property + " as it does not exist in " + getBlock().getBlockState());
            }
            if(!property.isValid(value))
            {
                throw new IllegalArgumentException("Cannot set unlisted property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(getBlock()) + ", it is not an allowed value");
            }
            Map<IUnlistedProperty<?>, Optional<?>> newMap = new HashMap<IUnlistedProperty<?>, Optional<?>>(unlistedProperties);
            newMap.put(property, Optional.fromNullable(value));
            if(Iterables.all(newMap.values(), Predicates.<Optional<?>>equalTo(Optional.absent())))
            { // no dynamic properties, lookup normal state
                return (IExtendedBlockState) normalMap.get(getProperties());
            }
            return new ExtendedStateImplementation(getBlock(), getProperties(), ImmutableMap.copyOf(newMap), propertyValueTable).setMap(this.normalMap);
        }

        @Override
        public Collection<IUnlistedProperty<?>> getUnlistedNames()
        {
            return Collections.unmodifiableCollection(unlistedProperties.keySet());
        }

        @Override
        public <V>V getValue(IUnlistedProperty<V> property)
        {
            if(!this.unlistedProperties.containsKey(property))
            {
                throw new IllegalArgumentException("Cannot get unlisted property " + property + " as it does not exist in " + getBlock().getBlockState());
            }
            return property.getType().cast(this.unlistedProperties.get(property).orNull());
        }

        @Override
        public ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties()
        {
            return unlistedProperties;
        }

        @Override
        public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map)
        {
            this.normalMap = map;
            super.buildPropertyValueTable(map);
        }

        private ExtendedStateImplementation setMap(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map)
        {
            this.normalMap = map;
            return this;
        }

        @Override
        public IBlockState getClean()
        {
            return this.normalMap.get(getProperties());
        }

        @Override
        public Block getBlock()
        {
            if (JavaUtil.instance.isCalledBy(BlockFluidClassic.class, BlockFluidBase.class, BlockFluidGC.class))
            {
                IBlockState bs = ((BlockGrating)blockBase).getLiquidBlock(this);
                if (bs != null) return bs.getBlock();
            }
            return this.blockBase;
        }
    }
}
