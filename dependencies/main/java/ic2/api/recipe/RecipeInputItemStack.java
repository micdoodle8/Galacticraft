package ic2.api.recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

/**
 * @deprecated Use {@link Recipes#inputFactory} instead.
 */
@Deprecated
public class RecipeInputItemStack implements IRecipeInput {
	public RecipeInputItemStack(ItemStack input) {
		this(input, input.stackSize);
	}

	public RecipeInputItemStack(ItemStack input, int amount) {
		if (input == null || input.getItem() == null || input.stackSize <= 0) throw new IllegalArgumentException("invalid input stack");

		this.input = input.copy(); // Never forget to copy.
		this.amount = amount;
	}

	@Override
	public boolean matches(ItemStack subject) {
		return subject.getItem() == input.getItem() && // Item matching
				(subject.getMetadata() == input.getMetadata() || input.getMetadata() == OreDictionary.WILDCARD_VALUE) && //meta matching
				(input.getMetadata() == OreDictionary.WILDCARD_VALUE || RecipeUtil.matchesNBT(subject.getTagCompound(), input.getTagCompound())); //nbt matching.
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public List<ItemStack> getInputs() {
		return Collections.unmodifiableList(Arrays.asList(RecipeUtil.setImmutableSize(input, getAmount())));
	}

	@Override
	public String toString() {
		return "RInputItemStack<"+RecipeUtil.setImmutableSize(input, amount)+">";
	}

	public final ItemStack input;
	public final int amount;
}
