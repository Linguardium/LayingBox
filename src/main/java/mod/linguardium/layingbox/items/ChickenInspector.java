package mod.linguardium.layingbox.items;

import mod.linguardium.layingbox.api.ChickenStats;
import mod.linguardium.layingbox.blocks.blockentity.LayingBoxEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ChickenInspector extends Item {

    public ChickenInspector(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockEntity be =context.getWorld().getBlockEntity(context.getBlockPos());
        if (be instanceof LayingBoxEntity) {
            if (!context.getWorld().isClient()) {
                for (ItemStack stack : ((LayingBoxEntity) be).chickens) {
                    Entity entity = EntityType.getEntityFromTag(stack.getOrCreateSubTag("EntityTag"), context.getWorld()).orElse(null);
                    if (entity instanceof ChickenStats) {
                        context.getPlayer().sendMessage(new TranslatableText("message.layingbox.production", entity.getDisplayName(), ((ChickenStats) entity).getProduction() / 100.0f), false);
                    }
                }
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof ChickenStats) {
            if (!user.world.isClient()) {
                user.sendMessage(new TranslatableText("message.layingbox.production", entity.getDisplayName(), ((ChickenStats) entity).getProduction() / 100.0f), false);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

}
