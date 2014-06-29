package micdoodle8.mods.galacticraft.api.event.client;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import net.minecraft.util.ResourceLocation;

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
        public static class Pre extends CelestialBodyRenderEvent
        {
            public Pre(CelestialBody celestialBody)
            {
                super(celestialBody);
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
