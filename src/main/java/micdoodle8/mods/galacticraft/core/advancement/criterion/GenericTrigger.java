package micdoodle8.mods.galacticraft.core.advancement.criterion;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public abstract class GenericTrigger implements ICriterionTrigger
{
    private final ResourceLocation id;
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

    public GenericTrigger(String id)
    {
        super();
        this.id = new ResourceLocation(Constants.ASSET_PREFIX, id);
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancements, ICriterionTrigger.Listener listener)
    {
        Listeners listeners = this.listeners.get(playerAdvancements);

        if (listeners == null)
        {
            listeners = new Listeners(playerAdvancements);
            this.listeners.put(playerAdvancements, listeners);
        }

        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements advancements, ICriterionTrigger.Listener listener)
    {
        Listeners listeners = this.listeners.get(advancements);

        if (listeners != null)
        {
            listeners.remove(listener);

            if (listeners.isEmpty())
            {
                this.listeners.remove(advancements);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements advancements)
    {
        listeners.remove(advancements);
    }

    /**
     * Trigger.
     *
     * @param playerMP the player
     */
    public void trigger(EntityPlayerMP playerMP)
    {
        Listeners listeners = this.listeners.get(playerMP.getAdvancements());

        if (listeners != null)
        {
            listeners.trigger(playerMP);
        }
    }

    public static abstract class Instance extends AbstractCriterionInstance
    {
        public Instance(ResourceLocation resourceLocation)
        {
            super(resourceLocation);
        }

        public abstract boolean test(EntityPlayerMP player);
    }

    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener> listeners = Sets.newHashSet();

        public Listeners(PlayerAdvancements advancements)
        {
            playerAdvancements = advancements;
        }

        public boolean isEmpty()
        {
            return listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener listener)
        {
            listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener listener)
        {
            listeners.remove(listener);
        }

        public void trigger(EntityPlayerMP player)
        {
            ArrayList<ICriterionTrigger.Listener> list = null;

            for (ICriterionTrigger.Listener listener : listeners)
            {
                if (listener.getCriterionInstance() instanceof Instance)
                {
                    if (((Instance) listener.getCriterionInstance()).test(player))
                    {
                        if (list == null)
                        {
                            list = Lists.newArrayList();
                        }

                        list.add(listener);
                    }
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener listener1 : list)
                {
                    listener1.grantCriterion(playerAdvancements);
                }
            }
        }
    }
}