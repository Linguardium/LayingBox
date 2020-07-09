package mod.linguardium.layingbox.recipes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import static mod.linguardium.layingbox.recipes.ModRecipes.*;

public class PelletRecipe extends AbstractCookingRecipe {

    public PelletRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(PELLET_RECIPE_TYPE, id, group, input, output, experience, cookTime);
    }

    public String getGroup() {
        return group;
    }
    public Ingredient getInput() {
        return input;
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(Blocks.SMITHING_TABLE);
    }

    public RecipeSerializer<?> getSerializer() {
        return PELLET_SERIALIZER;
    }

}
