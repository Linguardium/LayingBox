package mod.linguardium.layingbox.items;

import mod.linguardium.layingbox.api.components.ChickenComponent;
import mod.linguardium.layingbox.api.components.ModComponents;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.Optional;

public class ProductionWand extends Item {
    public ProductionWand(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.world.isClient()) {
            Optional<ChickenComponent> component = ModComponents.CHICKEN.maybeGet(entity);
            if (component.isPresent()) {
                int production = component.get().getProduction();
                production+=100;
                component.get().setProduction(production);
                user.sendMessage(new TranslatableText("message.layingbox.production", entity.getDisplayName(), ModComponents.getProduction((ComponentProvider) entity) / 100.0F), false);
                entity.world.sendEntityStatus(entity, (byte) 20);
            }
        }
        return ActionResult.SUCCESS;
    //return super.useOnEntity(stack, user, entity, hand);
    }
}
