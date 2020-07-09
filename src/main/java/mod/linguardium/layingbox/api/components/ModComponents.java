package mod.linguardium.layingbox.api.components;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.Identifier;

import static mod.linguardium.layingbox.LayingBoxMain.MOD_ID;

public class ModComponents {
    public static final ComponentType<ChickenComponent> CHICKEN =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MOD_ID,"chicken"), ChickenComponent.class)
            .attach(EntityComponentCallback.event(ChickenEntity.class),chicken->new ChickenComponent());
    public static Integer getProduction(ComponentProvider provider) {
        // Retrieve a provided component
        return CHICKEN.maybeGet(provider).map(ChickenComponent::getProduction).orElse(0);
    }
    public static Integer getTickTime(ComponentProvider provider) {
        // Retrieve a provided component
        return CHICKEN.maybeGet(provider).map(ChickenComponent::getProduction).orElse(6000);
    }

}

