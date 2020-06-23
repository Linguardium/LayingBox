package mod.linguardium.layingbox.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class LoveWand extends Item {
    public LoveWand(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof AnimalEntity) {
            if (!user.world.isClient()) {
                ((AnimalEntity) entity).setBreedingAge(0);
                ((AnimalEntity) entity).setLoveTicks(6000);
                entity.world.sendEntityStatus(entity, (byte) 18);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
