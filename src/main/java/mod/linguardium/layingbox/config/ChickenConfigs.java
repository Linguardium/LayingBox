package mod.linguardium.layingbox.config;


import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swordglowsblue.artifice.api.Artifice;
import mod.linguardium.layingbox.LayingBoxMain;
import mod.linguardium.layingbox.api.ResourceChickenConfig;
import mod.linguardium.layingbox.entity.ResourceChicken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static mod.linguardium.layingbox.LayingBoxMain.MOD_ID;
import static mod.linguardium.layingbox.LayingBoxMain.log;


public class ChickenConfigs {

//    public static Map<Pair<Identifier,Identifier>,EntityType<ResourceChicken>> BreedingMap = Maps.newHashMap();
    public static List<String> eggs = new ArrayList<>();
    public static Multimap<Pair<Identifier,Identifier>,EntityType<ResourceChicken>> BreedingMap = ArrayListMultimap.create();
    public static HashMap<Identifier,Set<Identifier>> FavoredBiomeList= Maps.newHashMap();
    public static Path chickenConfigPath = Paths.get( FabricLoader.getInstance().getConfigDirectory().toString() + File.separatorChar + MOD_ID + File.separator + "chickens");
    private static Gson g = new GsonBuilder().setPrettyPrinting().create();

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
        String name = file.getFileName().toString().substring(0,file.getFileName().toString().lastIndexOf("."));
        register(name,chickenConfig);
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

    private static void register(String name, ResourceChickenConfig config) {
        Identifier id = new Identifier(LayingBoxMain.MOD_ID,"resource_chicken."+name);
        EntityType<ResourceChicken> chicken;
        if (!Registry.ENTITY_TYPE.containsId(id)) {
            chicken = Registry.register(Registry.ENTITY_TYPE, id, FabricEntityTypeBuilder
                    .create(SpawnGroup.CREATURE, (EntityType.EntityFactory<ResourceChicken>) (type, world) -> new ResourceChicken(type, world, config.base_color, config.accent_color, config.dropItem, config.base_texture, config.accent_texture))
                    .dimensions(EntityDimensions.changing(0.4F, 0.7F)).trackable(10, 3)
                    .build());
            FabricDefaultAttributeRegistry.register(chicken, ResourceChicken.createChickenAttributes());
        }else{
            log(Level.ERROR,"Duplicate chicken id: "+name);
            chicken = (EntityType<ResourceChicken>) Registry.ENTITY_TYPE.get(id);
        }
        Pair<Identifier, Identifier> breedingParents = Pair.of(new Identifier(config.parent1), new Identifier(config.parent2));
        BreedingMap.put(breedingParents, chicken);
        if (config.favored_biome_tags != null && config.favored_biome_tags.size() > 0) {
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
            try {
                FavoredBiomeList.put(id, biomeIds);
            } catch(IllegalArgumentException e) {
                log(Level.ERROR, "Error while processing: "+id+". This may cause problems.\n"+e.getMessage());
            }
        }
        if (!Registry.ITEM.containsId(new Identifier(MOD_ID,"spawnegg."+name)))
            Registry.register(Registry.ITEM,new Identifier(MOD_ID,"spawnegg."+name),new SpawnEggItem(chicken,config.base_color,config.accent_color, new Item.Settings().group(LayingBoxMain.CHICKEN_EGGS_GROUP)));
        eggs.add(name);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ChickenConfigsClient.RegisterRenderer(chicken);
        }
    }


    @Environment(EnvType.CLIENT)
    public static void registerAssets() {
        Artifice.registerAssets(MOD_ID+":spawn_eggs",(pack)->{
            pack.setVisible();
            pack.setDisplayName("LayingBox & Resource Hens");
            for (String name : eggs) {
                pack.addItemModel(new Identifier(MOD_ID, "spawnegg." + name), model -> {
                    model.parent(new Identifier("minecraft:item/template_spawn_egg"));
                });
            }


        });
    }
    public static boolean isFavoredBiome(ChickenEntity entity, boolean explicit) {
        return isFavoredBiome(entity.getType(),entity.getEntityWorld().getBiome(entity.getBlockPos()),explicit);
    }
    public static boolean isFavoredBiome(EntityType type, Biome biome, boolean explicit) {
        Set<Identifier> favored = FavoredBiomeList.get(EntityType.getId(type));
        if (favored != null) {
            if (explicit && favored.contains(new Identifier("all_biomes"))) {
                return true;
            }
            if (favored.size() > 0) {
                Identifier biomeId = Registry.BIOME.getId(biome);

                if (favored.stream().anyMatch(identifier -> FakeTagRegistry_Biomes.tagContains(identifier,biomeId) ||
                                                        (biome.hasParent() && FakeTagRegistry_Biomes.tagContains(identifier,new Identifier(biome.getParent()))))) {
                    return true;
                }
            }
        }else if (explicit) {
            return true;
        }
        return false;
    }
}
