package codechicken.lib.render.item;

import codechicken.lib.asm.ObfMapping;
import codechicken.lib.util.ReflectionManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by covers1624 on 31/10/2016.
 */
//Used to make sure all references to RenderItem are of CCL's overridden renderer.
@Mod (modid = "ccl-entityhook")
//TODO Find another way to do this, Maybe an ASM hook to load the CCRenderItem.
public class EntityRendererHooks {

    private static boolean hasSanitized;

    public static EntityRendererHooks instance;

    public EntityRendererHooks() {
        instance = this;
    }

    @EventHandler
    @SideOnly (Side.CLIENT)
    public void preInt(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(instance);
        RenderingRegistry.registerEntityRenderingHandler(DummyEntity.class, new IRenderFactory<DummyEntity>() {
            @Override
            public Render<? super DummyEntity> createRenderFor(RenderManager manager) {
                sanitizeEntityRenderers(manager);
                return new Render<DummyEntity>(manager) {
                    @Override
                    protected ResourceLocation getEntityTexture(DummyEntity entity) {
                        return null;
                    }
                };
            }
        });
    }

    @SuppressWarnings ("unchecked")
    @SubscribeEvent
    @SideOnly (Side.CLIENT)
    public void onGuiInit(GuiOpenEvent event) {
        try {
            if (event.getGui() instanceof GuiModList) {
                ModContainer container = FMLCommonHandler.instance().findContainerFor(instance);
                Field field = ReflectionManager.getField(new ObfMapping("net/minecraftforge/fml/client/GuiModList", "mods", ""));
                ArrayList<ModContainer> mods = (ArrayList<ModContainer>) field.get(event.getGui());
                if (mods.contains(container)) {
                    mods.remove(container);
                } else {
                    FMLLog.warning("CCL's Dummy mod was not found inside the mod gui list!");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to hide CCL Dummy mod from the mods gui!.", e);
        }
    }

    @SideOnly (Side.CLIENT)
    public static void sanitizeEntityRenderers(RenderManager renderManager) {
        if (!hasSanitized) {
            try {
                for (Render<? extends Entity> render : renderManager.entityRenderMap.values()) {
                    if (render != null) {
                        for (Field field : render.getClass().getDeclaredFields()) {
                            if (field.getType().equals(RenderItem.class)) {
                                field.setAccessible(true);
                                field.set(render, CCRenderItem.instance());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to reflect an EntityRenderer!", e);
            }
            hasSanitized = true;
        }
    }

    @SideOnly (Side.CLIENT)
    public class DummyEntity extends Entity {

        public DummyEntity(World worldIn) {
            super(worldIn);
        }

        @Override
        protected void entityInit() {

        }

        @Override
        protected void readEntityFromNBT(NBTTagCompound compound) {

        }

        @Override
        protected void writeEntityToNBT(NBTTagCompound compound) {

        }
    }

}
