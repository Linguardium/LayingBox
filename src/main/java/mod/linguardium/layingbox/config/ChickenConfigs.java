package mod.linguardium.layingbox.config;


import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.swordglowsblue.artifice.api.Artifice;
import mod.linguardium.layingbox.LayingBoxMain;
import mod.linguardium.layingbox.api.ResourceChickenConfig;
import mod.linguardium.layingbox.entity.ResourceChicken;
import mod.linguardium.layingbox.items.PelletItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

import static mod.linguardium.layingbox.LayingBoxMain.*;


public class ChickenConfigs {

//    public static Map<Pair<Identifier,Identifier>,EntityType<ResourceChicken>> BreedingMap = Maps.newHashMap();
    public static HashMap<EntityType<? extends ResourceChicken>, ResourceChickenConfig> chickens = Maps.newHashMap();
//    public static List<String> chickentypes = new ArrayList<>();
//    public static Multimap<Pair<Identifier,Identifier>,EntityType<? extends ChickenEntity>> BreedingMap = ArrayListMultimap.create();
//    public static HashMap<EntityType<? extends ChickenEntity>,Item> FoodMap = Maps.newHashMap();
//    public static HashMap<Identifier,Set<Identifier>> FavoredBiomeList= Maps.newHashMap();
    public static Path chickenConfigPath = Paths.get( FabricLoader.getInstance().getConfigDirectory().toString() + File.separatorChar + MOD_ID + File.separator + "chickens");
    private static Gson g = new GsonBuilder().registerTypeAdapter(Identifier.class, new TypeAdapter<Identifier>() {

        @Override
        public void write(JsonWriter out, Identifier value) throws IOException {
            out.jsonValue(value.toString());
        }

        @Override
        public Identifier read(JsonReader in) throws IOException {
            return new Identifier(in.nextString());
        }
    }).setPrettyPrinting().create();

    private static void processFolder(Path folder) throws IOException {
        for (Path fName : Files.list(folder).collect(Collectors.toList())) {
            if (fName.equals(folder))
                continue;
            if(Files.isDirectory(fName)) {
                processFolder(fName);
            }else if (fName.getFileName().toString().endsWith(".json")) {
                processJson(fName);
            }
        }
    }
    private static void processJson(Path file) {
        ResourceChickenConfig chickenConfig = null;
        try {
            chickenConfig = g.fromJson(new String(Files.readAllBytes(file)), ResourceChickenConfig.class);
        } catch (IOException e) {
            log(Level.ERROR,"failed to process "+file.getFileName());
        }
        if (chickenConfig.name.isEmpty())
            chickenConfig.name = file.getFileName().toString().substring(0,file.getFileName().toString().lastIndexOf("."));
        register(chickenConfig);
    }
    public static void init() {
        FakeTagRegistry_Biomes.init();
        try {
            if (!chickenConfigPath.toFile().exists()) {
                Files.createDirectories(chickenConfigPath);
            }
            processFolder(chickenConfigPath);
            for(ModContainer container : FabricLoader.getInstance().getAllMods()) {

                Path layingbox_static_data = container.getRootPath().resolve("static_data/layingbox/chickens");

                if (Files.exists(layingbox_static_data) && Files.isDirectory(layingbox_static_data)) {
                    log(Level.INFO,"Found LayingBox chicken data in mod: "+container.getMetadata().getName());
                    processFolder(layingbox_static_data);
                }
            }
            Path layingbox_static_data = Paths.get(FabricLoader.getInstance().getGameDirectory().toString(),"static_data","layingbox","chickens");
            if (layingbox_static_data.toFile().exists()) {
                log(Level.INFO,"Found LayingBox chicken data in the minecraft directory");
                processFolder(layingbox_static_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void register(ResourceChickenConfig config) {
        Identifier id = new Identifier(LayingBoxMain.MOD_ID,"resource_chicken."+config.name);
        EntityType<ResourceChicken> chicken;
        if (!Registry.ENTITY_TYPE.containsId(id)) {
            chicken = Registry.register(Registry.ENTITY_TYPE, id, FabricEntityTypeBuilder
                    .create(SpawnGroup.CREATURE, (EntityType.EntityFactory<ResourceChicken>) (type, world) -> new ResourceChicken(type, world, config.base_color, config.accent_color, config.dropItem, config.base_texture, config.accent_texture))
                    .dimensions(EntityDimensions.changing(0.4F, 0.7F)).trackable(10, 3)
                    .build());
            FabricDefaultAttributeRegistry.register(chicken, ResourceChicken.createChickenAttributes());
        }else{
            log(Level.ERROR,"Duplicate chicken id: "+config.name);
            chicken = (EntityType<ResourceChicken>) Registry.ENTITY_TYPE.get(id);
        }
        chickens.put(chicken,config);
//        Pair<Identifier, Identifier> breedingParents = Pair.of(new Identifier(config.parent1), new Identifier(config.parent2));
//        BreedingMap.put(breedingParents, chicken);
/*        if (config.favored_biome_tags != null && config.favored_biome_tags.size() > 0) {
            Set<Identifier> biomeIds = Sets.newHashSet();
            for (String biomeId : config.favored_biome_tags) {
                if (!biomeId.isEmpty()) {
                    try {
                        biomeIds.add(new Identifier(biomeId));
                    } catch (IllegalArgumentException e) {
                        log(Level.ERROR, "Error while processing " + name + ". May cause problems.");
                    }
                }
            }
//            try {
//                FavoredBiomeList.put(id, biomeIds);
//            } catch(IllegalArgumentException e) {
//                log(Level.ERROR, "Error while processing: "+id+". This may cause problems.\n"+e.getMessage());
//            }
        }*/
        Item food=null;
        if (!Registry.ITEM.containsId(new Identifier(MOD_ID,"spawnegg."+config.name)))
            Registry.register(Registry.ITEM,new Identifier(MOD_ID,"spawnegg."+config.name),new SpawnEggItem(chicken,config.base_color,config.accent_color, new Item.Settings().group(LayingBoxMain.CHICKEN_EGGS_GROUP)));
        if (config.feed == null) {
            if (!Registry.ITEM.containsId(new Identifier(MOD_ID, "chickenfeed." + config.name)))
                food = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "chickenfeed." + config.name), new PelletItem(config.base_color, config.accent_color, config.base_color, new Item.Settings().food(FoodComponents.MELON_SLICE).maxCount(64).group(ITEM_GROUP_PELLETS)));
            else
                food = Registry.ITEM.get(new Identifier(MOD_ID, "chickenfeed." + config.name));
            config.feed=new Identifier(MOD_ID,"chickenfeed."+config.name);
        }
        //chickentypes.add(name);
        //FoodMap.put(chicken,food);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ChickenConfigsClient.RegisterRenderer(chicken);
            if (food != null)
                ChickenConfigsClient.RegisterRenderer(food);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void registerAssets() {
        Artifice.registerAssets(MOD_ID+":spawn_eggs",(pack)->{
            pack.setVisible();
            pack.setDisplayName("LayingBox & Resource Hens");
            for (ResourceChickenConfig c : chickens.values()) {
                pack.addItemModel(new Identifier(MOD_ID, "spawnegg." + c.name), model -> {
                    model.parent(new Identifier("minecraft:item/template_spawn_egg"));
                });
                pack.addItemModel(new Identifier(MOD_ID,"chickenfeed."+c.name),model->{
                    model.parent(new Identifier(MOD_ID,"item/pellet"));
                });
            }
        });
    }

}
