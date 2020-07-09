package micdoodle8.mods.galacticraft.core;

import net.minecraftforge.fml.LogicalSide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Annotations
{
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface RuntimeInterface
    {
        String clazz();

        String modID();

        String deobfName() default "";

        String[] altClasses() default {};
    }

//	@Retention(RetentionPolicy.RUNTIME)
//	@Target(ElementType.METHOD)
//	public @interface AltForVersion
//	{
//		String version();
//	}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface NetworkedField
    {
        LogicalSide targetSide();
    }

//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.METHOD)
//    public @interface VersionSpecific
//    {
//        String version();
//    }
}
