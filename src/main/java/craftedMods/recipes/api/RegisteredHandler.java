package craftedMods.recipes.api;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredHandler {

	boolean isEnabled() default true;

}
