package mod.linguardium.layingbox.entity;

import mod.linguardium.layingbox.LayingBoxMain;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static EntityType<ResourceChicken> RESOURCE_CHICKEN_TYPE = Registry.register(Registry.ENTITY_TYPE,new Identifier(LayingBoxMain.MOD_ID,"resource_chicken_type"), FabricEntityTypeBuilder.
            create(SpawnGroup.CREATURE, (EntityType.EntityFactory<ResourceChicken>) (type, world)->new ResourceChicken(type, world, -1, -1, null,null,null))
            .dimensions(EntityDimensions.changing(0.4F, 0.7F)).trackable(10,3)
            .build());
    public static void init() {
        FabricDefaultAttributeRegistry.register(RESOURCE_CHICKEN_TYPE, ResourceChicken.createChickenAttributes());
    }
}
