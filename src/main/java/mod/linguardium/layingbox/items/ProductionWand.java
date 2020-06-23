package mod.linguardium.layingbox.items;

import mod.linguardium.layingbox.api.ChickenStats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ProductionWand extends Item {
    public ProductionWand(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof ChickenStats) {
            if (!user.world.isClient()) {
                ((ChickenStats) entity).setProduction(((ChickenStats) entity).getProduction()+100);
                user.sendMessage(new TranslatableText("message.layingbox.production", entity.getDisplayName(), ((ChickenStats) entity).getProduction() / 100.0F),false);
                entity.world.sendEntityStatus(entity, (byte) 20);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}
