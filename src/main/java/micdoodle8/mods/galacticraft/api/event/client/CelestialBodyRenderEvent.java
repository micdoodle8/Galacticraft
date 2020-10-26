package micdoodle8.mods.galacticraft.api.event.client;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class CelestialBodyRenderEvent extends Event
{
    public final CelestialBody celestialBody;

    public CelestialBodyRenderEvent(CelestialBody celestialBody)
    {
        this.celestialBody = celestialBody;
    }

    public static class CelestialRingRenderEvent extends CelestialBodyRenderEvent
    {
        public CelestialRingRenderEvent(CelestialBody celestialBody)
        {
            super(celestialBody);
        }

        @Cancelable
        public static class Pre extends CelestialRingRenderEvent
        {
            public final Vector3 parentOffset;

            public Pre(CelestialBody celestialBody, Vector3 parentOffset)
            {
                super(celestialBody);
                this.parentOffset = parentOffset;
            }
        }

        public static class Post extends CelestialBodyRenderEvent
        {
            public Post(CelestialBody celestialBody)
            {
                super(celestialBody);
            }
        }
    }

    @Cancelable
    public static class Pre extends CelestialBodyRenderEvent
    {
        public ResourceLocation celestialBodyTexture;
        public int textureSize;

        public Pre(CelestialBody celestialBody, ResourceLocation celestialBodyTexture, int textureSize)
        {
            super(celestialBody);
            this.celestialBodyTexture = celestialBodyTexture;
            this.textureSize = textureSize;
        }
    }

    public static class Post extends CelestialBodyRenderEvent
    {
        public Post(CelestialBody celestialBody)
        {
            super(celestialBody);
        }
    }
}
