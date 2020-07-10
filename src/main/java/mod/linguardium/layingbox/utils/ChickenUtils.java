package mod.linguardium.layingbox.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import mod.linguardium.layingbox.api.ResourceChickenConfig;
import mod.linguardium.layingbox.api.components.ChickenComponent;
import mod.linguardium.layingbox.api.components.ModComponents;
import mod.linguardium.layingbox.config.FakeTagRegistry_Biomes;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static mod.linguardium.layingbox.config.ChickenConfigs.chickens;

public class ChickenUtils {
    public static Identifier getBabyType(Random random, Entity parent1, Entity parent2, boolean explicit) {
        return getBabyType(random, Registry.ENTITY_TYPE.getId(parent1.getType()),parent1.getEntityWorld().getBiome(parent1.getBlockPos()),Registry.ENTITY_TYPE.getId(parent2.getType()),parent2.getEntityWorld().getBiome(parent2.getBlockPos()), explicit);
    }
    public static Identifier getBabyType(Random random, Identifier p1Id, Biome p1Biome, Identifier p2Id, Biome p2Biome, boolean explicit) {
        List<EntityType> list = Lists.newArrayList();
        if (p1Id.equals(p2Id) && explicit) {
            // if both parents are the same and it is explicit to biomes (allows breeding same parents in any biome)
            list.add(Registry.ENTITY_TYPE.get(p1Id));
        }
        // add all the chicken types according to biome (or not)
        chickens.forEach((type,config)->{
            boolean p1is1 = config.parent1.equals(p1Id);
            boolean p1is2 = config.parent1.equals(p2Id);
            boolean p2is1 = config.parent2.equals(p1Id);
            boolean p2is2 = config.parent2.equals(p2Id);
            if (p1is1 && p2is2 || p1is2 && p2is1) {
               if (!explicit) {
                   list.add(type);
               }else if(isFavoredBiome(type,p1Biome, false) || isFavoredBiome(type,p2Biome, false)) {
                   list.add(type); // extra chance in explicit to spawn specific biome types rather than all biome types
               }
               if (isFavoredBiome(type,p1Biome, explicit) || isFavoredBiome(type,p2Biome, explicit)) {
                   list.add(type);
               }


            }
        });
        // list is now "dual parent type + list of chicken types spawnable here"
        if (explicit) {
            return Registry.ENTITY_TYPE.getId(list.get(random.nextInt(list.size()))); // parent type or biome specific type
        }
        if (list.size() > 0 && random.nextFloat()<0.10F) {
            return Registry.ENTITY_TYPE.getId(list.get(random.nextInt(list.size()))); // chance of parent type or biome specific type at 10%/# of types
        }else if (p1Id.equals(p2Id)){ // parent type
            return p1Id;
        }
        return Registry.ENTITY_TYPE.getId(EntityType.CHICKEN);
        /// list = all children possible from group
    }

    public static boolean isFavoredBiome(ChickenEntity entity, boolean explicit) {
            return isFavoredBiome((EntityType<? extends ChickenEntity>) entity.getType(),entity.getEntityWorld().getBiome(entity.getBlockPos()),explicit);
    }
    public static boolean isFavoredBiome(EntityType<? extends ChickenEntity> type, Biome biome, boolean explicit) {
        ResourceChickenConfig c = chickens.get(type);
        Set<Identifier> favored=null;
        if (c!=null && c.favored_biome_tags != null) {
            favored = Sets.newLinkedHashSet(c.favored_biome_tags);
        }
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
        }else{
            return explicit;
        }
        return false;
    }
    public static int getAverageProduction(ComponentProvider e1, ComponentProvider e2) {
        Optional<ChickenComponent> comp1 = ModComponents.CHICKEN.maybeGet(e1);
        Optional<ChickenComponent> comp2 = ModComponents.CHICKEN.maybeGet(e2);

        return (ModComponents.getProduction(e1) + ModComponents.getProduction(e2)) / ((comp1.isPresent() && comp2.isPresent())?2:1);

    }

}
