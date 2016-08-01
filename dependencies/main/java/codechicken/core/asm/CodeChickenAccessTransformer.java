package codechicken.core.asm;

import codechicken.lib.asm.ObfMapping;
import com.google.common.collect.ImmutableBiMap;
import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import java.io.IOException;
import java.lang.reflect.Field;

public class CodeChickenAccessTransformer extends AccessTransformer {
    private static boolean makeAllPublic;
    private static Field f_classNameBiMap;
    private static Object emptyMap = ImmutableBiMap.of();

    public CodeChickenAccessTransformer() throws IOException {
        super();
        loadPublicConfig();
    }

    private void loadPublicConfig() {
        if (ObfMapping.obfuscated) {
            return;
        }

        makeAllPublic = CodeChickenCoreModContainer.config.getTag("dev.runtimePublic").setComment("Enabling this setting will make all minecraft classes public at runtime in MCP just as they are in modloader." +
                "\nYou should ONLY use this when you are testing with a mod that relies on runtime publicity and doesn't include access transformers." +
                "\nSuch mods are doing the wrong thing and should be fixed.").getBooleanValue(false);

        if (!makeAllPublic) {
            return;
        }

        try {
            f_classNameBiMap = FMLDeobfuscatingRemapper.class.getDeclaredField("classNameBiMap");
            f_classNameBiMap.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        boolean setPublic = makeAllPublic && name.startsWith("net.minecraft.");
        if (setPublic) {
            setClassMap(name);
        }
        bytes = super.transform(name, transformedName, bytes);
        if (setPublic) {
            restoreClassMap();
        }
        return bytes;
    }

    private void restoreClassMap() {
        try {
            f_classNameBiMap.set(FMLDeobfuscatingRemapper.INSTANCE, emptyMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setClassMap(String name) {
        try {
            f_classNameBiMap.set(FMLDeobfuscatingRemapper.INSTANCE, ImmutableBiMap.of(name.replace('.', '/'), ""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
