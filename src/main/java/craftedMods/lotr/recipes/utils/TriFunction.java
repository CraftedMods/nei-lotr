package craftedMods.lotr.recipes.utils;

@FunctionalInterface
public interface TriFunction<U, V, W, X> {

	public X accept(U u, V v, W w);

}
