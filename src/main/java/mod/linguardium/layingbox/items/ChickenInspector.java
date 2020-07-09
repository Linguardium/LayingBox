package mod.linguardium.layingbox.items;

import mod.linguardium.layingbox.api.components.ChickenComponent;
import mod.linguardium.layingbox.api.components.ModComponents;
import mod.linguardium.layingbox.blocks.blockentity.LayingBoxEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.Optional;

public class ChickenInspector extends Item {

    public ChickenInspector(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockEntity be =context.getWorld().getBlockEntity(context.getBlockPos());
        if (be instanceof LayingBoxEntity) {
            if (!context.getWorld().isClient()) {
                PlayerEntity player = context.getPlayer();
                for (ItemStack stack : ((LayingBoxEntity) be).chickens) {
                    LivingEntity entity = (LivingEntity) EntityType.getEntityFromTag(stack.getOrCreateSubTag("EntityTag"), context.getWorld()).orElse(null);
                    displayProductionValue(player,entity);
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
            if (!user.world.isClient()) {
                displayProductionValue(user,entity);
            }
            return ActionResult.SUCCESS;
    }
    public static void displayProductionValue(PlayerEntity player, LivingEntity entity) {
        if (player == null || entity==null)
            return;
        Optional<ChickenComponent> productionComponent = ModComponents.CHICKEN.maybeGet(entity);
        productionComponent.ifPresent(chickenComponent ->
                player.sendMessage(new TranslatableText("message.layingbox.production", entity.getDisplayName(), (chickenComponent.getProduction() / 100.0f)), false)
        );

    }
}
