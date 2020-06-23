package mod.linguardium.layingbox.blocks.blockentity;

import mod.linguardium.layingbox.api.BasicInventory;
import mod.linguardium.layingbox.api.LayingBoxProvider;
import mod.linguardium.layingbox.blocks.ModBlocks;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameRules;

import static mod.linguardium.layingbox.items.ModItems.NETTED_ANIMAL;
import static net.minecraft.block.HorizontalFacingBlock.FACING;

public class LayingBoxEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {
    public BasicInventory inventory;
    public DefaultedList<ItemStack> chickens;
    //List<LayingBoxProvider<LivingEntity>> chickens = new ArrayList<>();
    public MobEntity displayChicken=null;
    int tickTimer = 0;
    int tickMulti = 1;
    public LayingBoxEntity() {
        super(ModBlocks.LAYING_BOX_ENTITY);
        inventory = new BasicInventory(9);
        chickens = DefaultedList.of();
        tickTimer = 6000;
    }
    public LayingBoxEntity(int tickMulti) {
        this();
        this.tickMulti=tickMulti;
    }
    public MobEntity removeChicken() {
        MobEntity chicken = null;
        if (chickens.size() > 0) {
            ItemStack stack = chickens.get(0).copy();
            chickens.remove(0);
            CompoundTag itemTag = stack.getOrCreateTag();
            if (itemTag.contains("EntityTag", NbtType.COMPOUND)) {
                CompoundTag entityTag = itemTag.getCompound("EntityTag");
                Entity entity = EntityType.getEntityFromTag(entityTag, world).orElse(null);
                if (entity instanceof MobEntity)
                    chicken = (MobEntity) entity;
            }
        }
        this.sync();
        this.markDirty();
        return chicken;
    }
    public boolean addChicken(ItemStack stack) {
        if (chickens.size()<world.getGameRules().getInt(GameRules.field_19405)) {
            CompoundTag itemTag = stack.getOrCreateTag();
            if (itemTag.contains("EntityTag", NbtType.COMPOUND)) {
                CompoundTag entityTag = itemTag.getCompound("EntityTag");
                Entity entity = EntityType.getEntityFromTag(entityTag, world).orElse(null);
                if (entity instanceof LayingBoxProvider && entity instanceof MobEntity) {
                    chickens.add(stack.copy());
                    this.sync();
                    markDirty();
                    return true;
                    //return addChicken((LayingBoxProvider<LivingEntity>) entity);
                }
            }
        }
        return false;
    }
    public boolean addChicken(LayingBoxProvider<MobEntity> entity) {
        if (chickens.size()<world.getGameRules().getInt(GameRules.field_19405)) {
            ItemStack stack = new ItemStack(NETTED_ANIMAL);
            CompoundTag tag = ((MobEntity)entity).toTag(new CompoundTag());
            tag.putString("id",EntityType.getId(((MobEntity)entity).getType()).toString());
            stack.getOrCreateTag().put("EntityTag",tag);
            addChicken(stack);
            //chickens.add(entity);
            return true;
        }
        return false;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        inventory.readTags(tag.getList("Items",NbtType.COMPOUND));
        ListTag chickenTag = tag.getList("Chickens",NbtType.COMPOUND);
        for(int i = 0; i < chickenTag.size(); ++i) {
            ItemStack itemStack = ItemStack.fromTag(chickenTag.getCompound(i));
            if (!itemStack.isEmpty()) {
                chickens.add(itemStack);
            }
        }
        if (tag.contains("tickTime",NbtType.INT)) {
            tickTimer=tag.getInt("tickTime");
        }
        if (tag.contains("tickMulti",NbtType.INT)) {
            tickMulti=tag.getInt("tickMulti");
        }else{
            tickMulti=1;
        }
    }

    private CompoundTag WriteEntityTag(Entity e,CompoundTag tag) {
        e.toTag(tag);

        tag.putString("id",EntityType.getId(((MobEntity) e).getType()).toString());
        return tag;
    }
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Items",inventory.getTags());
        ListTag chickensTag = new ListTag();
        for (ItemStack stack : chickens) {
            chickensTag.add(stack.toTag(new CompoundTag()));
        }
        tag.put("Chickens",chickensTag);
        tag.putInt("tickTime",tickTimer);
        tag.putInt("tickMulti",tickMulti);
        return tag;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        CompoundTag eTag = tag.getCompound("DisplayChicken").getCompound("EntityTag");
        if (!eTag.isEmpty()) {
            displayChicken = (MobEntity) EntityType.getEntityFromTag(eTag, world).orElse(null);
            if (displayChicken != null) {
                displayChicken.headYaw = world.getBlockState(pos).get(FACING).asRotation();
            }
        }else{
            displayChicken=null;
        }
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        if (chickens.size() > 0)
        tag.put("DisplayChicken",chickens.get(0).getOrCreateTag());
        return tag;
    }

    @Override
    public void tick() {
        if (!world.isClient()) {
            tickTimer-=tickMulti;
            if (tickTimer <= 0) {
                tickTimer = this.world.random.nextInt(6000) + 6000;
                for (ItemStack itm : chickens) {
                    LayingBoxProvider<MobEntity> p = (LayingBoxProvider<MobEntity>) EntityType.getEntityFromTag(itm.getOrCreateSubTag("EntityTag"),world).orElse(null);
                    if (p != null) {
                        for (ItemStack stack : p.getDrops()) {
                            inventory.addStack(stack);
                        }
                        if (world.getRandom().nextFloat()<0.02) {
                            for (ItemStack stack : p.getDrops()) {
                                inventory.addStack(stack);
                            }
                        }
                    }
                }
            }
        }
    }

}
