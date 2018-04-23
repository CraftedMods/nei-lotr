package craftedMods.recipes.api;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredRecipeHandlerFactory {

	boolean isEnabled() default true;

}
