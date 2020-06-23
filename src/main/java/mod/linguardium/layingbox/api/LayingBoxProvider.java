package mod.linguardium.layingbox.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public interface LayingBoxProvider<E extends LivingEntity> {
    World getEntityWorld();
    List<ItemStack> getDrops();

}
