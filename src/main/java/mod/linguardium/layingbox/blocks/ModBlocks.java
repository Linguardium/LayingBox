package mod.linguardium.layingbox.blocks;

import mod.linguardium.layingbox.LayingBoxMain;
import mod.linguardium.layingbox.blocks.blockentity.AutomaticPelletizerEntity;
import mod.linguardium.layingbox.blocks.blockentity.EggCollectorEntity;
import mod.linguardium.layingbox.blocks.blockentity.LayingBoxEntity;
import mod.linguardium.layingbox.blocks.blockentity.PelletizerEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static mod.linguardium.layingbox.LayingBoxMain.MOD_ID;

public class ModBlocks {
    public static BlockEntityType<LayingBoxEntity> LAYING_BOX_ENTITY;
    public static BlockEntityType<EggCollectorEntity> EGG_COLLECTOR_ENTITY;
    public static BlockEntityType<PelletizerEntity> PELLETIZER_ENTITY;
    public static BlockEntityType<AutomaticPelletizerEntity> AUTOMATIC_PELLETIZER_ENTITY;
    public static LayingBox LAYING_BOX;
    public static EggCollector EGG_COLLECTOR;
    public static Pelletizer PELLETIZER;
    public static Pelletizer AUTOMATIC_PELLETIZER;
    private static final AbstractBlock.ContextPredicate always = (a,b,c)->true;
    private static final AbstractBlock.TypedContextPredicate typed_always = (a,b,c,d)->true;
    private static final AbstractBlock.TypedContextPredicate typed_never = (a,b,c,d)->false;
    private static final AbstractBlock.ContextPredicate never = (a,b,c)->false;
    public static void init() {
        LAYING_BOX = Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"laying_box"),new LayingBox(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).suffocates(never).allowsSpawning(typed_never).solidBlock(always)) );
        LAYING_BOX_ENTITY =  Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MOD_ID, "laying_box_blockentity"),BlockEntityType.Builder.create(LayingBoxEntity::new, LAYING_BOX).build(null));
        EGG_COLLECTOR = Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"egg_collector"),new EggCollector(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).suffocates(always).allowsSpawning(typed_never).solidBlock(always)) );
        EGG_COLLECTOR_ENTITY =  Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MOD_ID, "egg_collector_blockentity"),BlockEntityType.Builder.create(EggCollectorEntity::new, EGG_COLLECTOR).build(null));
        PELLETIZER = Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"pelletizer"),new Pelletizer(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).suffocates(always).allowsSpawning(typed_never).solidBlock(always)) );
        PELLETIZER_ENTITY =  Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MOD_ID, "pelletizer_blockentity"),BlockEntityType.Builder.create(PelletizerEntity::new, PELLETIZER).build(null));
        AUTOMATIC_PELLETIZER = Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"automatic_pelletizer"),new Pelletizer(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).suffocates(always).allowsSpawning(typed_never).solidBlock(always)) );
        AUTOMATIC_PELLETIZER_ENTITY =  Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MOD_ID, "automatic_pelletizer_blockentity"),BlockEntityType.Builder.create(AutomaticPelletizerEntity::new, AUTOMATIC_PELLETIZER).build(null));
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"laying_box"),new BlockItem(LAYING_BOX, new Item.Settings().group(LayingBoxMain.ITEM_GROUP)));
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"egg_collector"),new BlockItem(EGG_COLLECTOR, new Item.Settings().group(LayingBoxMain.ITEM_GROUP)));
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"pelletizer"),new BlockItem(PELLETIZER, new Item.Settings().group(LayingBoxMain.ITEM_GROUP)));
    }
}
