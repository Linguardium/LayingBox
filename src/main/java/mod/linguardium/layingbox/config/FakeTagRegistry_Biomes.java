package mod.linguardium.layingbox.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import mod.linguardium.layingbox.api.FakeTagType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static mod.linguardium.layingbox.LayingBoxMain.log;

public class FakeTagRegistry_Biomes {
    public static HashMap<Identifier, List<Identifier>> BiomeTags = new HashMap<>();
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
        FakeTagType tag = null;
        try {
            tag = g.fromJson(new String(Files.readAllBytes(file)), FakeTagType.class);
            String name = file.getFileName().toString().substring(0,file.getFileName().toString().lastIndexOf("."));
            BiomeTags.put(new Identifier("c",name), tag.values.stream().map(Identifier::new).collect(Collectors.toList()));
        } catch (IOException | JsonSyntaxException e) {
            log(Level.ERROR, "failed to process " + file.getFileName());
        }

    }
    public static void init() {
        try {
            for(ModContainer container : FabricLoader.getInstance().getAllMods()) {
                Path layingbox_static_data = container.getRootPath().resolve("static_data/layingbox/tags/biome");
                if (Files.exists(layingbox_static_data) && Files.isDirectory(layingbox_static_data)) {
                    log(Level.INFO,"Found LayingBox biome data in mod: "+container.getMetadata().getName());
                    processFolder(layingbox_static_data);
                }
            }
            Path layingbox_static_data = Paths.get(FabricLoader.getInstance().getGameDirectory().toString(),"static_data","layingbox","tags","biome");
            if (layingbox_static_data.toFile().exists()) {
                log(Level.INFO,"Found LayingBox biome data in the minecraft directory");
                processFolder(layingbox_static_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean tagContains(Identifier tagId, Identifier biome) {
        List<Identifier> tag = BiomeTags.get(tagId);
        return tag != null && tag.contains(biome);
    }
    public static boolean checkTags(List<Identifier> tags, Identifier biome) {
        return tags.stream().anyMatch(t->tagContains(t,biome));
    }
}
