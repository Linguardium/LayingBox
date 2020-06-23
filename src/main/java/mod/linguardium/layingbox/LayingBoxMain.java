package mod.linguardium.layingbox;

import mod.linguardium.layingbox.blocks.ModBlocks;
import mod.linguardium.layingbox.config.ChickenConfigs;
import mod.linguardium.layingbox.entity.ModEntities;
import mod.linguardium.layingbox.items.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static mod.linguardium.layingbox.blocks.ModBlocks.LAYING_BOX;

public class LayingBoxMain implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "layingbox";
    public static final String MOD_NAME = "LayingBox";
    public static ItemGroup CHICKEN_EGGS_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID,"spawn_eggs_group")).icon(()-> new ItemStack(Items.CAT_SPAWN_EGG)).build();
    public static ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID,"item_group")).icon(()-> new ItemStack(LAYING_BOX)).build();
    @Override
    public void onInitialize() {

        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        ChickenConfigs.init();
        log(Level.INFO, "1, 2, 3 chicken (eggs)");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}