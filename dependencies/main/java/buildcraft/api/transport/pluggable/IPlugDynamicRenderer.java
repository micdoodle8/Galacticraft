package buildcraft.api.transport.pluggable;


import net.minecraft.client.renderer.BufferBuilder;

public interface IPlugDynamicRenderer<P extends PipePluggable> {
    void render(P plug, double x, double y, double z, float partialTicks, BufferBuilder bb);
}
