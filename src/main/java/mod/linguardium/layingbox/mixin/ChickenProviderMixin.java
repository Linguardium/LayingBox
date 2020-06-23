package mod.linguardium.layingbox.mixin;

import mod.linguardium.layingbox.api.ChickenStats;
import mod.linguardium.layingbox.api.LayingBoxProvider;
import mod.linguardium.layingbox.config.ChickenConfigs;
import mod.linguardium.layingbox.entity.ResourceChicken;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.List;

@Mixin(ChickenEntity.class)
public abstract class ChickenProviderMixin extends AnimalEntity implements LayingBoxProvider<ChickenEntity>, ChickenStats {
    private static final TrackedData<Integer> PRODUCTION = DataTracker.registerData(ChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);;

    protected ChickenProviderMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(PRODUCTION, 0);
    }

    @Inject(method="createChild",at=@At("TAIL"),cancellable = true)
    private void LayingBox_MakinBabies(PassiveEntity passiveEntity, CallbackInfoReturnable info) {
        Identifier parent1=EntityType.getId(this.getType());
        Identifier parent2=EntityType.getId(passiveEntity.getType());
        Collection<EntityType<ResourceChicken>> children = ChickenConfigs.BreedingMap.get(Pair.of(parent1,parent2));
        children.addAll(ChickenConfigs.BreedingMap.get(Pair.of(parent2,parent1)));
        ChickenEntity baby;
        if (world.getRandom().nextFloat()>1.0 || children.size()<1) {
            baby = (ChickenEntity) this.getType().create(this.world);
        }else {
            EntityType<ChickenEntity> child = (EntityType<ChickenEntity>) children.toArray()[world.getRandom().nextInt(children.size())];
            baby = child.create(this.world);
        }
        if (passiveEntity instanceof ChickenStats)
            ((ChickenStats)baby).setProduction(ChickenStats.averagePlusProduction(world.getRandom(),this, (ChickenStats) passiveEntity));
        info.setReturnValue(baby);
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return (other instanceof ChickenEntity) && (this.isInLove() && other.isInLove());
    }

    @Override
    public int getProduction() {
       return this.dataTracker.get(PRODUCTION);
    }
    @Redirect(at=@At(value="INVOKE",target="net/minecraft/entity/passive/ChickenEntity.dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"),method="tickMovement")
    private ItemEntity LayingBox_dropItem(ChickenEntity me, ItemConvertible item) {
        for (ItemStack stack : getDrops()) {
            dropStack(stack,0);
        }
        return null;
    }
    @Override
    public void setProduction(int value) {
        if (value>1000)
            value=1000;
        this.dataTracker.set(PRODUCTION,value);
    }
    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> stacks = DefaultedList.of();
        int dropadds = this.getProduction()/100;
        int count=1+((dropadds>0)?world.getRandom().nextInt(dropadds):0);
        stacks.add(new ItemStack(Items.EGG,count));
        if (getEntityWorld().getRandom().nextFloat() <= 0.1) {
            stacks.add(new ItemStack(Items.FEATHER));
        }
        return stacks;
    }
    @Inject(at=@At("TAIL"),method="writeCustomDataToTag", locals = LocalCapture.CAPTURE_FAILHARD)
    private void writeProductionToTag(CompoundTag tag, CallbackInfo info) {
        tag.putInt("Production",getProduction());
    }
    @Inject(at=@At("TAIL"),method="readCustomDataFromTag", locals = LocalCapture.CAPTURE_FAILHARD)
    private void readProductionFromTag(CompoundTag tag, CallbackInfo info) {
        if (tag.contains("Production", NbtType.INT))
            setProduction(tag.getInt("Production"));
    }

}
