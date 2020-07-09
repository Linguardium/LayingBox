package mod.linguardium.layingbox.recipes;

import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.api.builder.data.recipe.CookingRecipeBuilder;
import com.swordglowsblue.artifice.api.util.IdUtils;
import mod.linguardium.layingbox.api.ResourceChickenConfig;
import mod.linguardium.layingbox.items.PelletItem;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

import static mod.linguardium.layingbox.LayingBoxMain.MOD_ID;
import static mod.linguardium.layingbox.config.ChickenConfigs.chickens;

public class ModRecipes {
    //Access Widened
    public static PelletRecipeSerializer<PelletRecipe> PELLET_SERIALIZER= Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "pelletizer"), new PelletRecipeSerializer<>(PelletRecipe::new, 10));
    public static RecipeType<PelletRecipe> PELLET_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_ID,"pelletizer"),new RecipeType<PelletRecipe>() {
        public String toString() {
            return MOD_ID+":pelletizer";
        }
    });
    public static void init() {
        //GenerateRecipes


            Artifice.registerData(MOD_ID+":pellet_recipes",pack->{
                pack.setDisplayName("LayingBox Pellet Recipes");

                for(ResourceChickenConfig c : chickens.values()) {
                    if (Registry.ITEM.get(c.feed) instanceof PelletItem) {
                        addPelletizerRecipe(pack,new Identifier(MOD_ID, "pellet." + c.name), f->
                            f.cookingTime(10).ingredientItem(c.dropItem).result(c.feed)
                        );
                    }
                }
            });


    }

    public static void addPelletizerRecipe(ArtificeResourcePack.ServerResourcePackBuilder pack, Identifier id, Function<CookingRecipeBuilder,CookingRecipeBuilder> f) {
        pack.add(IdUtils.wrapPath("recipes/",id,".json"),
            f.apply(new CookingRecipeBuilder()).type(new Identifier(MOD_ID, "pelletizer")).build()
        );
    }


}
