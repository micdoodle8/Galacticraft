package micdoodle8.mods.miccore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cpw.mods.fml.relauncher.Side;

public interface Annotations
{
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface RuntimeInterface
	{
		String clazz();

		String modID();
		
		String[] altClasses() default {};
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface NetworkedField
	{
		Side targetSide();
	}
}
