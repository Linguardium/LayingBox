package mod.linguardium.layingbox.items;

import mod.linguardium.layingbox.LayingBoxMain;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static mod.linguardium.layingbox.LayingBoxMain.MOD_ID;

public class ModItems {

    public static Tag<Item> SEEDS_TAG = TagRegistry.item(new Identifier("c","seeds"));
    public static final PelletItem DEFAULT_PELLET = Registry.register(Registry.ITEM,new Identifier(MOD_ID,"default_pellet"),new PelletItem(9858122,5991183,9858122,new Item.Settings()));

    public static final NettedAnimalItem NETTED_ANIMAL = Registry.register(Registry.ITEM,new Identifier(MOD_ID,"netted_animal"),new NettedAnimalItem(new Item.Settings().maxCount(1).fireproof()));
    public static final AnimalNetItem NET = Registry.register(Registry.ITEM,new Identifier(MOD_ID,"animal_net"),new AnimalNetItem(new Item.Settings().maxDamage(10).group(LayingBoxMain.ITEM_GROUP)));
    public static final LoveWand LOVEWAND = Registry.register(Registry.ITEM,new Identifier(MOD_ID,"love_wand"),new LoveWand(new Item.Settings().maxCount(1).group(LayingBoxMain.ITEM_GROUP)));
    public static final ProductionWand PRODUCTIONWAND = Registry.register(Registry.ITEM,new Identifier(MOD_ID,"production_wand"),new ProductionWand(new Item.Settings().maxCount(1).group(LayingBoxMain.ITEM_GROUP)));
    public static final ChickenInspector INSPECTOR = Registry.register(Registry.ITEM,new Identifier(MOD_ID,"inspector"),new ChickenInspector(new Item.Settings().maxCount(1).group(LayingBoxMain.ITEM_GROUP)));
    public static void init() {

    }
}
